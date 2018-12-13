package Saving_837P_5010_ToDatabase;

import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Common.Activation;
import RDPCrystalEDILibrary.EDIDocument;
import RDPCrystalEDILibrary.EDIRulesReader;
import RDPCrystalEDILibrary.EDIRulesReader.ErrorEvent;
import RDPCrystalEDILibrary.EDIRulesReaderEventArg;
import RDPCrystalEDILibrary.EDISource;
import RDPCrystalEDILibrary.EDIValidator;
import RDPCrystalEDILibrary.FileType;
import RDPCrystalEDILibrary.Documents.DocumentLoop;
import RDPCrystalEDILibrary.Documents.X12.X125010Document;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.CLM;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.DMG;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.DTP;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.HI;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.NM1;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.SBR;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.SV1;
import jio.System.Collections.Generic.List;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.UI.Winforms.Controls.EDIDocumentViewer ediDocumentViewer;
	private RDPCrystalEDILibrary.EDIFileLoader ediFileLoader;
    private RDPCrystalEDILibrary.EDIValidator ediValidator;
    private RDPCrystalEDILibrary.EDIDocument ediDocument;
    private RDPCrystalEDILibrary.EDIRulesReader ediRuleReader;
    private JTable dgPatients;
    private JTable dgClaims;
    private JTable dgServiceLines;
	private JProgressBar pbSEF;
	private JTextArea txtEDIData;
	private JProgressBar pb2;
	
	private Connection dbConnection;
	private Statement stat;
	
	public Form1() {
		Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
		Activation.initializeJavonet(); //TODO: temporarily required to be removed in final version
		InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
		
		 CreateDatabaseTables();
	}
	
	private void InitializeComponents() {
		this.setSize(870, 750);
		this.setTitle("Loading 5010 837 Into SQL Database");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 870, 50);
		JLabel lblTitle = new JLabel("   Loading 5010 837 Into SQL Database");
		lblTitle.setFont(new Font("Microsoft Sans Serif",1,18));
		lblTitle.setForeground(Color.white);
		title.add(lblTitle);
		this.add(title);
		
		JPanel header = new JPanel(null);
		Insets hinsets = header.getInsets();
		JLabel logo = new JLabel(new ImageIcon(".\\Resources\\rdplogo.png"));
		logo.setBounds(hinsets.left,hinsets.top+10,350,97);
		header.setBounds(insets.left,insets.top+50,850,110);
		
		header.add(logo);
		
		Button createFile = new Button("Load & Create Tables");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+582,hinsets.top+10,256,63);
		createFile.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnLoad_Click();
			}
			
		});
		header.add(createFile);
		
		this.add(header);
		
		JPanel content = new JPanel(null);		
		content.setBounds(insets.left,insets.top+160,850,640);
		
		insets = content.getInsets();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		//ImageIcon icon = createImageIcon("images/middle.gif");

		JComponent panel1 = new JPanel(null);
		tabbedPane.addTab("Database Tables", null, panel1,
				"Database Tables");
		
		JPanel groupBox1 = new JPanel(null);
		groupBox1.setBounds(8, 10, 818, 123);
		groupBox1.setBorder(BorderFactory.createTitledBorder("Patients"));
		panel1.add(groupBox1);
		
		//TAB 1
		dgPatients = new JTable(new DefaultTableModel(new Object[][] {{""}},new Object[] {"test"}));
		dgPatients.setFont(new Font("Microsoft Sans Serif",0,16));
		dgPatients.setFillsViewportHeight(true);
		dgPatients.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,16));
		dgPatients.setAutoResizeMode(0);
		dgPatients.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            // do some actions here, for example
	            // print first column value from selected row
	        	try {
					dgPatients_SelectionChanged();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
		
		JScrollPane dgPatientsscrollPane = new JScrollPane(dgPatients);
		dgPatientsscrollPane.setBounds(3,20,818, 103);
		groupBox1.add(dgPatientsscrollPane);
		
		JPanel groupBox2 = new JPanel(null);
		groupBox2.setBounds(8, 130, 818, 123);
		groupBox2.setBorder(BorderFactory.createTitledBorder("Claims"));
		panel1.add(groupBox2);
		
		dgClaims = new JTable(new DefaultTableModel(new Object[][] {},new Object[] {}));
		dgClaims.setFont(new Font("Microsoft Sans Serif",0,16));
		dgClaims.setFillsViewportHeight(true);
		dgClaims.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,16));
		dgClaims.setAutoResizeMode(0);
		dgClaims.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            // do some actions here, for example
	            // print first column value from selected row
	        	try {
					dgClaims_SelectionChanged();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
		
		JScrollPane dgClaimsscrollPane = new JScrollPane(dgClaims);
		dgClaimsscrollPane.setBounds(3,30,818, 103);
		groupBox2.add(dgClaimsscrollPane);
		
		JPanel groupBox3 = new JPanel(null);
		groupBox3.setBounds(8, 250, 818, 123);
		groupBox3.setBorder(BorderFactory.createTitledBorder("Service Line"));
		panel1.add(groupBox3);
		
		dgServiceLines = new JTable(new DefaultTableModel(new Object[][] {},new Object[] {}));
		dgServiceLines.setFont(new Font("Microsoft Sans Serif",0,16));
		dgServiceLines.setFillsViewportHeight(true);
		dgServiceLines.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,16));
		dgServiceLines.setAutoResizeMode(0);
		dgServiceLines.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            // do some actions here, for example
	            // print first column value from selected row
	        	try {
					dgClaims_SelectionChanged();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
		
		JScrollPane dgServiceLinesscrollPane = new JScrollPane(dgServiceLines);
		dgServiceLinesscrollPane.setBounds(3,20,818, 103);
		groupBox3.add(dgServiceLinesscrollPane);
		
		panel1.add(groupBox3);

		JComponent panel2 = new JPanel(null);
		tabbedPane.addTab("EDI Document Tree", null, panel2,
				"EDI Document Tree");
		
		JComponent panel3 = new JPanel(null);
		tabbedPane.addTab("Original EDI Data", null, panel3,
				"Original EDI Data");
		
		// TAB 3
		txtEDIData = new JTextArea(20,0);
		Border border2 = BorderFactory.createLineBorder(Color.BLACK);
		txtEDIData.setBorder(BorderFactory.createCompoundBorder(border2,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		txtEDIData.setBorder(border2);
		txtEDIData.setFont(new Font("Microsoft Sans Serif",0,16));
		txtEDIData.setEditable(true);
		txtEDIData.setBounds(10,30,800,600);
		panel3.add(txtEDIData);
		
		tabbedPane.setBounds(10, 10, 830, 400);
		content.add(tabbedPane);
		
		JLabel lblReadingRules = new JLabel("Reading Rules");
		lblReadingRules.setBounds(10,35+176+35+176+10,200,20);
		lblReadingRules.setFont(new Font("Tahoma",0,18));
		content.add(lblReadingRules);
		
		JLabel lblValidating = new JLabel("Validating");
		lblValidating.setBounds(10,35+176+35+176+10+30,200,20);
		lblValidating.setFont(new Font("Tahoma",0,18));
		content.add(lblValidating);
		
		pbSEF = new JProgressBar(0, 100);
		pbSEF.setValue(0);
		pbSEF.setStringPainted(true);
		pbSEF.setBounds(200,35+176+35+176+10,630,20);
		content.add(pbSEF);
		
		pb2 = new JProgressBar(0, 100);
		pb2.setValue(0);
		pb2.setStringPainted(true);
		pb2.setBounds(200,35+176+35+176+10+30,630,20);
		content.add(pb2);
		
		JButton btnSourceCode = new JButton("Source Code");
		btnSourceCode.setBackground(new Color(2,119,189));
		btnSourceCode.setForeground(Color.white);
		btnSourceCode.setFont(new Font("Microsoft Sans Serif",0,16));
		btnSourceCode.setBounds(insets.left+610+55,35+376+35+136+10+30+30,165,32);
		btnSourceCode.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				 String path = System.getProperty("user.dir");
				 
				 try {
					Desktop.getDesktop().open(new File(path));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 
			}
			
		});
		this.add(btnSourceCode);
		/* Components */
        // 
        // ediFileLoader
        // 
		this.ediFileLoader = new RDPCrystalEDILibrary.EDIFileLoader();
		this.ediFileLoader.setEDIFile("");
        // 
        // ediDocumentViewer1
        // 
		this.ediDocumentViewer = new RDPCrystalEDILibrary.UI.Winforms.Controls.EDIDocumentViewer();
		this.ediDocumentViewer.setBounds(insets.left+10,insets.top+10,810,327);
		panel2.add(this.ediDocumentViewer);
        // 
        // ediRuleReader
        // 
        this.ediRuleReader=new EDIRulesReader();
        this.ediRuleReader.setEDIRulesFile(null);
        this.ediRuleReader.setEDIRulesFileData("");
        this.ediRuleReader.setEDIRulesFilePath("");
        this.ediRuleReader.setSampleData(null);
        this.ediRuleReader.addProgressChanged(new EDIRulesReader.GeneralEvent() {
        	public void Invoke (java.lang.Integer progress){
        		sefReader_ProgressChanged(progress);
        	}
        });
        this.ediRuleReader.addOnErrorOccurred(new ErrorEvent() {
        	public void Invoke (Object sender,EDIRulesReaderEventArg e) {
        		sefReader_ErrorOccurred(sender,e);
        	}
        });
        // 
        // ediValidator
        // 
		this.ediValidator=new EDIValidator();
        this.ediValidator.setAutoDetectDelimiters(true);
        this.ediValidator.setCopyElementNumber(true);
        this.ediValidator.setEDIFile("");
        this.ediValidator.setEDIRulesFile("");
        this.ediValidator.setEDIRulesFileData("");
        this.ediValidator.setEDIRulesReader(this.ediRuleReader);
        this.ediValidator.setEDISource(RDPCrystalEDILibrary.EDISource.DataString);
        this.ediValidator.setLoadValidatedData(true);
        this.ediValidator.setMaxErrorsBeforeThrowingException(((long)(-1)));
        this.ediValidator.addProgressChanged(new EDIValidator.GeneralEvent() {
        	public void Invoke (java.lang.Integer progress){
        		ediValidator_ProgressChanged(progress);
        	}
        });
        // 
        // ediDocument
        // 
        this.ediDocument=new EDIDocument();
        this.ediDocument.setEDIFilePath("");
        this.ediDocument.setFileStream(null);

		this.add(content);
		this.setVisible(true);
	}
	

    private void CreateDatabaseTables()
    {
        //We are going to use SQLLite to demonstrate how to simple load EDI data and save it in a simple database
    	 
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }

        try {
        	File f = new File("MyDatabase.sqlite");
        	if (f.exists())
        		f.delete();
            dbConnection = DriverManager.getConnection("jdbc:sqlite:MyDatabase.sqlite");
            stat = dbConnection.createStatement();
            
            //Creates the patient's table
            String patientTable = "CREATE TABLE Patients (Id INT PRIMARY KEY, First TEXT, Last TEXT, Insurance TEXT, RelationShip TEXT, InsuranceType TEXT, DOB TEXT, Gender TEXT)";
            String claimTable = "CREATE TABLE Claims (Id INT, PatientID INT, ClaimID TEXT, Amount REAL, Diagnosis1 TEXT)";
            String servicesTable = "CREATE TABLE ServiceLines (Id INT, ClaimID INT, Procedure TEXT, Charge REAL, ServiceDate TEXT)";

            stat.execute(patientTable);
            stat.execute(claimTable);
            stat.execute(servicesTable);            
            
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }
    }

    private void InitializeEDI() throws IOException
    {
        //EDI Rules files are used the EDIValidator component to validate and load EDI files.
        //This is just a sample EDI rules file. Please go to our online store for a production level rules file
        //We offer the following file (http://www.rdpcrystal.com/products/edi-library/edi-rules-creator/edi-rules-files/) or you can create 
        //your own with the EDI Rules Creator Studio application
        ediValidator.setEDIRulesFile("EDIFiles\\Rules_5010_837P_005010X222A1.Rules");

        txtEDIData.setText(ReadTextFile("EDIFiles\\sampleEDIFile.txt"));

        //loading from a string rather than a file
        ediValidator.setEDISource(EDISource.File);

        //Set EDI file to validate
        ediValidator.setEDIFile("EDIFiles\\sampleEDIFile.txt");

        //Set the type of file to load
        ediValidator.setEDIFileType(FileType.X12);

        ediValidator.setAutoDetectDelimiters(true);

        //Tell the ediValidator to load the EDI file data
        ediValidator.setLoadValidatedData(true);
    }
    

    private void LoadEDI()
    {
        try
        {
            ediValidator.Validate();

            //The EDI Document will contain all the data of the 837 file
            if (ediValidator.getEDILightWeightDocument() != null)
            {
                X125010Document doc = new X125010Document(ediValidator.getEDILightWeightDocument());

                DocumentLoop functionalHeaderLoop = doc.getMainSection().GetLoop("FUNCTIONAL GROUP");
                DocumentLoop stHeaderLoop = functionalHeaderLoop.GetLoop("ST HEADER");

                //Get the 2000A BILLING/PAY-TO PROVIDER HIERARCHICAL LEVEL
                //In an 837 file this loop is 2000A
                DocumentLoop bpHierLevel = stHeaderLoop.GetLoop("2000A");

                //Get all the subscribers and claims information
                List<DocumentLoop> subscriberAndClaimsSections = bpHierLevel.GetLoops("2000B");
                SaveAllClaims(subscriberAndClaimsSections);

                ResultSet result = stat.executeQuery("Select * from Patients");
                dgPatients.setModel(buildTableModel(result));

                //display a tree of the EDI data
                ediDocumentViewer.LoadDocument(ediValidator.getEDILightWeightDocument());
            }
        }
        catch (Exception ex)
        {
        	JOptionPane.showMessageDialog(this, ex.toString());
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void SaveAllClaims(List<DocumentLoop> claims) throws SQLException
    {
        int patientRecordID = 1;
        int claimRecordID = 1;
        int serviceLineRecordID = 1;

        for (DocumentLoop claimSection : claims)
        {
            SBR subscriber = claimSection.<SBR>GetSegment(SBR.class);
            DocumentLoop subscriberInfo = claimSection.GetLoop("2010BA");
            NM1 subName = subscriberInfo.<NM1>GetSegment(NM1.class);

            DMG demographics = subscriberInfo.<DMG>GetSegment(DMG.class);

            DocumentLoop insuranceInfoSection = claimSection.GetLoop("2010BB");
            NM1 insuranceName = insuranceInfoSection.<NM1>GetSegment(NM1.class);

            String insert = String.format("INSERT INTO Patients (Id, First, Last, Insurance, Relationship, InsuranceType, DOB, Gender) VALUES (%d,'%s','%s','%s','%s','%s','%s','%s')",
            patientRecordID, subName.getFirstName(), subName.getNameLastOrOrganizationName(), insuranceName.getNameLastOrOrganizationName(), subscriber.getIndividualRelationshipCode(), subscriber.getClaimFilingIndicatorCode(), demographics.getDateTimePeriod(), demographics.getGenderCode());

            stat.execute(insert);

            List<DocumentLoop> claimLoopSections = claimSection.GetLoops("2300");

            for(DocumentLoop cl : claimLoopSections)
            {
                CLM clm = cl.<CLM>GetSegment(CLM.class);
                HI hi = cl.<HI>GetSegment(HI.class);
                
                String diagnosisCode = hi.getHealthCareCodeInformation1().getIndustryCode1();

                String claimTable = String.format("INSERT INTO Claims (Id, PatientID, ClaimID, Amount, Diagnosis1) VALUES (%d,%d,'%s','%s','%s')",
                 claimRecordID, patientRecordID, clm.getClaimSubmitterIdentifier(), clm.getMonetaryAmount(), diagnosisCode);

                stat.execute(claimTable);

                List<DocumentLoop> serviceLineSections = cl.GetLoops("2400");
                for (DocumentLoop sl : serviceLineSections)
                {
                    //Get the service line segment
                    SV1 sv1 = sl.<SV1>GetSegment(SV1.class);
                    DTP serviceDate = sl.<DTP>GetSegment(DTP.class);

                    String serviceLine = String.format("INSERT INTO ServiceLines (Id, ClaimID, Procedure, Charge, ServiceDate) VALUES (%d,%d,'%s','%s','%s')",
                        serviceLineRecordID++, claimRecordID, sv1.getCompositeMedicalProcedureIdentifier().getProductServiceID1(), sv1.getMonetaryAmount1(), serviceDate.getDateTimePeriod());

                    stat.execute(serviceLine);
                }

                claimRecordID++;
            }

            patientRecordID++;
        }
    }
    
    
    private void sefReader_ProgressChanged(int progress)
    {
        IncreaseProgressBar(progress);
    }

    private void ediValidator_ProgressChanged(int progress)
    {
        IncreaseProgressBar2(progress);
    }

    private void sefReader_ErrorOccurred(Object sender, EDIRulesReaderEventArg e)
    {
    	JOptionPane.showMessageDialog(this, e.getLineText() + "?" + e.getMessage());
    }

    private void IncreaseProgressBar(int percentage)
    {
        if ((pbSEF.getValue() < pbSEF.getMaximum()))
            pbSEF.setValue(percentage);
    }

    private void IncreaseProgressBar2(int percentage)
    {
    	 if ((pb2.getValue() < pb2.getMaximum()))
             pb2.setValue(percentage);
    }
    
    private void btnLoad_Click()
    {
     	this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        //Reset the progress bar & tables
        pb2.setValue(0);

        try {
			InitializeEDI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        LoadEDI();
    }
    

    private void dgPatients_SelectionChanged() throws SQLException
    {
        if (dgPatients.getSelectedRow()>=0)
        {
            Integer patientID = (Integer) dgPatients.getValueAt(dgPatients.getSelectedRow(), 0);

            ResultSet result = stat.executeQuery("Select * from Claims where patientID=" + patientID.toString());
            dgClaims.setModel(buildTableModel(result));

            dgClaims.clearSelection();
            dgServiceLines.clearSelection();
        }
    }

    private void dgClaims_SelectionChanged() throws SQLException
    {
    	   if (dgClaims.getSelectedRow()>=0)
           {
               Integer claimID = (Integer) dgClaims.getValueAt(dgClaims.getSelectedRow(), 0);

               ResultSet result = stat.executeQuery("Select * from ServiceLines where claimID=" + claimID.toString());
               dgServiceLines.setModel(buildTableModel(result));
           }
    }

    
    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    private String ReadTextFile(String path) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(
    		    new FileInputStream(path), "UTF-8"));
    	try {
    	    StringBuilder sb = new StringBuilder();
    	    String line = br.readLine();

    	    while (line != null) {
    	        sb.append(line);
    	        sb.append(System.lineSeparator());
    	        line = br.readLine();
    	    }
    	    String everything = sb.toString();
    	    return everything;
    	} finally {
    	    br.close();
    	}
    }
    
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Form1();
			}
			
		});
	}
}

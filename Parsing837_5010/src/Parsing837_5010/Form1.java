package Parsing837_5010;

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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import Common.Activation;
import RDPCrystalEDILibrary.EDIDocument;
import RDPCrystalEDILibrary.EDILightWeightDocument;
import RDPCrystalEDILibrary.EDIRulesReader;
import RDPCrystalEDILibrary.EDIRulesReader.ErrorEvent;
import RDPCrystalEDILibrary.EDIRulesReaderEventArg;
import RDPCrystalEDILibrary.EDISource;
import RDPCrystalEDILibrary.EDIValidator;
import RDPCrystalEDILibrary.FileType;
import RDPCrystalEDILibrary.LightWeightElement;
import RDPCrystalEDILibrary.LightWeightLoop;
import RDPCrystalEDILibrary.LightWeightLoops;
import RDPCrystalEDILibrary.LightWeightSegment;
import jio.System.Collections.ObjectModel.Collection;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.UI.Winforms.Controls.EDIDocumentViewer ediDocumentViewer;
	private RDPCrystalEDILibrary.EDIFileLoader ediFileLoader;
    private RDPCrystalEDILibrary.EDIValidator ediValidator;
    private RDPCrystalEDILibrary.EDIDocument ediDocument;
    private RDPCrystalEDILibrary.EDIRulesReader ediRuleReader;
	private JTable dgInterchangeHeader;
	private JTable dgFunctionalHeader;
	private JTable dataGrid;
	private JProgressBar pbSEF;
	private JProgressBar pb2;
	private JTextField txtReceiverName;
	private JTextField txtSubmitterPhone;
	private JTextField txtBillingProviderName;
	private JTextField txtProviderAddress;
	private JTextField txtProviderStreet;
	private JTextField txtSubmitterName;
	private JButton drillDown, drillUp;
	
    private ArrayList<Object[]> subscriberTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> claimsTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> serviceLineTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> interchangeHeaderTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> functionalGroupTable = new ArrayList<Object[]>();
    
	String[] subscriberColumns = {
			"Claim #","Subscriber Name","Insurance Company","Relation Type","Insurance Type"};
	
	String[] claimsColumns = {
			"RowKey", "Claim ID", "Amount", "Diagnosis 1"
	};
	String[] serviceLineInfoColumns = {
			"RowKey", "Procedure", "Charge","Quantity","Diagnostic Code"
	};
	
	private int currentLevel=0;
	private Object subId;

	
	public Form1() {
		Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
		Activation.initializeJavonet(); //TODO: temporarily required to be removed in final version
		InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
	}
	
	private void InitializeComponents() {
		this.setSize(870, 750);
		this.setTitle("Loading HIPAA 5010 837 Professional");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 870, 50);
		JLabel lblTitle = new JLabel("   Loading HIPAA 5010 837 Professional");
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
		
		Button createFile = new Button("LOAD DOCUMENT");
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
		tabbedPane.addTab("Document Tree", null, panel1,
				"Document Tree");

		JComponent panel2 = new JPanel(null);
		tabbedPane.addTab("EDI File Headers", null, panel2,
				"EDI File Headers");
		
		JPanel interchangeHeaderGroup = new JPanel(null);
		interchangeHeaderGroup.setBounds(10, 10, 810, 170);
		interchangeHeaderGroup.setBorder(BorderFactory.createTitledBorder("Interchange Header"));
		panel2.add(interchangeHeaderGroup);
		
		Object[][] data = {{"",""}};
		String[] columnNames = {
				"Header Item", "Header Value"};
		dgInterchangeHeader = new JTable(new DefaultTableModel(data,columnNames));
		dgInterchangeHeader.setFont(new Font("Microsoft Sans Serif",0,16));
		dgInterchangeHeader.setFillsViewportHeight(true);
		dgInterchangeHeader.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,16));
		dgInterchangeHeader.setAutoResizeMode(0);
		TableColumn column = null;
		for (int i = 0; i < 2; i++) {
		    column = dgInterchangeHeader.getColumnModel().getColumn(i);
		    column.setPreferredWidth(100);
		}
		
		JScrollPane scrollPane = new JScrollPane(dgInterchangeHeader);
		scrollPane.setBounds(10,20,790,130);
		interchangeHeaderGroup.add(scrollPane);
		
		JPanel functionalGroupHeader = new JPanel(null);
		functionalGroupHeader.setBounds(10, 190, 810, 170);
		functionalGroupHeader.setBorder(BorderFactory.createTitledBorder("Functional Group Header"));
		panel2.add(functionalGroupHeader);
		
		Object[][] dgFunctionalHeaderdata = {{"",""}};
		String[] dgFunctionalHeadercolumnNames = {
				"Header Item", "Header Value"};
		dgFunctionalHeader = new JTable(new DefaultTableModel(dgFunctionalHeaderdata,dgFunctionalHeadercolumnNames));
		dgFunctionalHeader.setFont(new Font("Microsoft Sans Serif",0,16));
		dgFunctionalHeader.setFillsViewportHeight(true);
		dgFunctionalHeader.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,16));
		dgFunctionalHeader.setAutoResizeMode(0);
		TableColumn dgFunctionalHeadercolumn = null;
		for (int i = 0; i < 2; i++) {
			dgFunctionalHeadercolumn = dgFunctionalHeader.getColumnModel().getColumn(i);
			dgFunctionalHeadercolumn.setPreferredWidth(100);
		}
		
		JScrollPane dgFunctionalHeaderscrollPane = new JScrollPane(dgFunctionalHeader);
		dgFunctionalHeaderscrollPane.setBounds(10,20,790,130);
		functionalGroupHeader.add(dgFunctionalHeaderscrollPane);
		
		JComponent panel3 = new JPanel(null);
		tabbedPane.addTab("EDI File Information", null, panel3,
				"EDI File Information");
		
		// TAB 3
		JPanel groupBox1 = new JPanel(null);
		groupBox1.setBounds(8, 6, 283, 81);
		groupBox1.setBorder(BorderFactory.createTitledBorder("Submitter Information"));
		panel3.add(groupBox1);
		
		JLabel label4 = new JLabel("Name:");
		label4.setBounds(11,21,100,23);
		label4.setFont(new Font("Tahoma",0,12));
		groupBox1.add(label4);

		txtSubmitterName = new JTextField();
		txtSubmitterName.setBounds(70,18,189,27);
		txtSubmitterName.setFont(new Font("Tahoma",0,12));
		groupBox1.add(txtSubmitterName);
		
		JLabel label5 = new JLabel("Phone:");
		label5.setBounds(11,48,100,23);
		label5.setFont(new Font("Tahoma",0,12));
		groupBox1.add(label5);
		
		txtSubmitterPhone = new JTextField();
		txtSubmitterPhone.setBounds(70,42,189,27);
		txtSubmitterPhone.setFont(new Font("Tahoma",0,12));
		groupBox1.add(txtSubmitterPhone);
		
		JPanel groupBox3 = new JPanel(null);
		groupBox3.setBounds(8, 90, 283, 106);
		groupBox3.setBorder(BorderFactory.createTitledBorder("Billing Provider Information"));
		panel3.add(groupBox3);
		
		JLabel label8 = new JLabel("Name:");
		label8.setBounds(11,23,100,23);
		label8.setFont(new Font("Tahoma",0,12));
		groupBox3.add(label8);
		
		txtBillingProviderName = new JTextField();
		txtBillingProviderName.setBounds(70,20,189,27);
		txtBillingProviderName.setFont(new Font("Tahoma",0,12));
		groupBox3.add(txtBillingProviderName);
		
		JLabel label9 = new JLabel("Street:");
		label9.setBounds(11,50,100,23);
		label9.setFont(new Font("Tahoma",0,12));
		groupBox3.add(label9);

		txtProviderStreet = new JTextField();
		txtProviderStreet.setBounds(70,44,189,27);
		txtProviderStreet.setFont(new Font("Tahoma",0,12));
		groupBox3.add(txtProviderStreet);
		
		JLabel label10 = new JLabel("City/State/Zip::");
		label10.setBounds(11,72,100,23);
		label10.setFont(new Font("Tahoma",0,12));
		groupBox3.add(label10);
		
		txtProviderAddress = new JTextField();
		txtProviderAddress.setBounds(103,68,156,27);
		txtProviderAddress.setFont(new Font("Tahoma",0,12));
		groupBox3.add(txtProviderAddress);
		
		JPanel groupBox2 = new JPanel(null);
		groupBox2.setBounds(297, 6, 398, 190);
		groupBox2.setBorder(BorderFactory.createTitledBorder("Receiver Information"));
		panel3.add(groupBox2);
		
		JLabel label6 = new JLabel("Name:");
		label6.setBounds(11,23,100,23);
		label6.setFont(new Font("Tahoma",0,12));
		groupBox2.add(label6);
		
		txtReceiverName = new JTextField();
		txtReceiverName.setBounds(70,20,189,27);
		txtReceiverName.setFont(new Font("Tahoma",0,12));
		groupBox2.add(txtReceiverName);
		
		JPanel groupBox4 = new JPanel(null);
		groupBox4.setBounds(8, 199, 690, 226);
		groupBox4.setBorder(BorderFactory.createTitledBorder("Claims Information"));
		panel3.add(groupBox4);
		
		drillUp = new JButton("Up");
		drillUp.setBackground(new Color(2,119,189));
		drillUp.setForeground(Color.white);
		drillUp.setFont(new Font("Microsoft Sans Serif",0,10));
		drillUp.setBounds(10,20,50,20);
		drillUp.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				drillUp();
			}
			
		});
		groupBox4.add(drillUp);
		
		drillDown = new JButton("Enter");
		drillDown.setBackground(new Color(2,119,189));
		drillDown.setForeground(Color.white);
		drillDown.setFont(new Font("Microsoft Sans Serif",0,10));
		drillDown.setBounds(70,20,60,20);
		drillDown.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				drillDown();
			}
			
		});
		groupBox4.add(drillDown);
		
		//TODO: Add support for nested tables
		Object[][] dataGriddata = {{"","","","",""}};
		dataGrid = new JTable(new DefaultTableModel(dataGriddata,subscriberColumns));
		dataGrid.setFont(new Font("Microsoft Sans Serif",0,16));
		dataGrid.setFillsViewportHeight(true);
		dataGrid.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,16));
		dataGrid.setAutoResizeMode(0);
		TableColumn dataGridcolumn = null;
		for (int i = 0; i < 5; i++) {
			dataGridcolumn = dataGrid.getColumnModel().getColumn(i);
			dataGridcolumn.setPreferredWidth(100);
		}
		
		JScrollPane dataGridscrollPane = new JScrollPane(dataGrid);
		dataGridscrollPane.setBounds(3,50,684,150);
		groupBox4.add(dataGridscrollPane);
		
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
		panel1.add(this.ediDocumentViewer);
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
	
    private void InitializeEDI()
    {
        //EDI Rules files are used the EDIValidator component to validate and load EDI files.
        //This is just a sample EDI rules file. Please go to our online store for a production level rules file
        //We offer the following file (http://www.rdpcrystal.com/products/edi-library/edi-rules-creator/edi-rules-files/) or you can create 
        //your own with the EDI Rules Creator Studio application
        ediValidator.setEDIRulesFile("EDIFiles\\Rules_5010_837P_005010X222A1.Rules");

        //load from a file rather from a string
        ediValidator.setEDISource(EDISource.File);

        //Set the type of file to load
        ediValidator.setEDIFileType(FileType.X12);

        //Set the EDI data to load
        ediValidator.setEDIFile("EDIFiles\\sample837P.txt");

        ediValidator.setAutoDetectDelimiters(true);

        //Tell the ediValidator to load the EDI file data
        ediValidator.setLoadValidatedData(true);
    }

    private void LoadEDI()
    {
        try
        {
            ediValidator.Validate();

            //The EDI Document will contain all the data of the 834 file
            if (ediValidator.getEDILightWeightDocument() != null)
            {
                EDILightWeightDocument file837 = ediValidator.getEDILightWeightDocument();

                LightWeightLoop interchangeHeaderLoop = file837.getLoops().get_Item(0, LightWeightLoop.class).GetLoop("INTERCHANGE HEADER");

                DisplayInterchangeHeader(interchangeHeaderLoop.GetSegment("ISA"));

                LightWeightLoop functionalHeaderLoop = interchangeHeaderLoop.GetLoop("FUNCTIONAL GROUP");

                DisplayFunctionalHeader(functionalHeaderLoop.GetSegment("GS"));

                LightWeightLoop stHeaderLoop = functionalHeaderLoop.GetLoop("ST HEADER");

                //Get the submitter loop. In an 837 file the submitter loop is 1000A
                LightWeightLoop submitterLoop = stHeaderLoop.GetLoop("1000A");
                DisplaySubmitterInformation(submitterLoop);

                //Get the receiver loop. In an 837 file the receiver loop is 1000B
                LightWeightLoop receiverLoop = stHeaderLoop.GetLoop("1000B");
                DisplayReceiverInformation(receiverLoop);

                //Get the 2000A BILLING/PAY-TO PROVIDER HIERARCHICAL LEVEL
                //In an 837 file this loop is 2000A
                LightWeightLoop bpHierLevel = stHeaderLoop.GetLoop("2000A");

                LightWeightLoop billingProviderLoop = bpHierLevel.GetLoop("2010AA");
                DisplayBillingProviderInformation(billingProviderLoop);

                //Get all the subscribers and claims information
                LightWeightLoops subscriberAndClaimsLoops = bpHierLevel.GetLoopCollection("2000B");
                DisplayAllClaims(subscriberAndClaimsLoops);

                //Show the document on the screen
                ediDocumentViewer.LoadDocument(ediValidator.getEDILightWeightDocument());
            }
        }
        catch (Exception e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage());
        }
        this.setCursor(Cursor.getDefaultCursor());
        JOptionPane.showMessageDialog(this, "Parsing Complete");
    }
    private void DisplayInterchangeHeader(LightWeightSegment isa)
    {
        CreateInterchangeHeaderItem("Authorization Information Qualifier", isa.getElements().get_Item(0, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Authorization Information", isa.getElements().get_Item(1, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Security Information Qualifier", isa.getElements().get_Item(2, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Security Information", isa.getElements().get_Item(3, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange ID Qualifier1", isa.getElements().get_Item(4, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange Sender ID", isa.getElements().get_Item(5, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange ID Qualifier2", isa.getElements().get_Item(6, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange Receiver ID", isa.getElements().get_Item(7, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange Date", isa.getElements().get_Item(8, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange Time", isa.getElements().get_Item(9, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Repetition Separator", isa.getElements().get_Item(10, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange Control Version Number", isa.getElements().get_Item(11, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange Control Number", isa.getElements().get_Item(12, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Acknowledgment Requested", isa.getElements().get_Item(13, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Interchange Usage Indicator", isa.getElements().get_Item(14, LightWeightElement.class).getDataValue());
        CreateInterchangeHeaderItem("Component Element Separator", isa.getElements().get_Item(15, LightWeightElement.class).getDataValue());

        DefaultTableModel dm = (DefaultTableModel)dgInterchangeHeader.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
       	DefaultTableModel model = (DefaultTableModel) dgInterchangeHeader.getModel();
       	for (Object[] row : interchangeHeaderTable)
       	{
       		model.addRow(row);
       	}
    }

    private void DisplayFunctionalHeader(LightWeightSegment gs)
    {
        CreateFunctionalHeaderItem("Functional Identifier Code", gs.getElements().get_Item(0, LightWeightElement.class).getDataValue());
        CreateFunctionalHeaderItem("Application Sender’s Code", gs.getElements().get_Item(1, LightWeightElement.class).getDataValue());
        CreateFunctionalHeaderItem("Application Receiver’s Code", gs.getElements().get_Item(2, LightWeightElement.class).getDataValue());
        CreateFunctionalHeaderItem("Date", gs.getElements().get_Item(3, LightWeightElement.class).getDataValue());
        CreateFunctionalHeaderItem("Time", gs.getElements().get_Item(4, LightWeightElement.class).getDataValue());
        CreateFunctionalHeaderItem("Group Control Number", gs.getElements().get_Item(5, LightWeightElement.class).getDataValue());
        CreateFunctionalHeaderItem("Responsible Agency Code", gs.getElements().get_Item(6, LightWeightElement.class).getDataValue());
        CreateFunctionalHeaderItem("Version / Release / Industry Identifier Code", gs.getElements().get_Item(7, LightWeightElement.class).getDataValue());

        DefaultTableModel dm = (DefaultTableModel)dgFunctionalHeader.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
    	DefaultTableModel model = (DefaultTableModel) dgFunctionalHeader.getModel();
       	for (Object[] row : functionalGroupTable)
       	{
       		model.addRow(row);
       	}
    }

    private void CreateInterchangeHeaderItem(String headerItem, String headerValue)
    {
        Object[] row = new Object[2];
        row[0] = headerItem;
        row[1] = headerValue;

        interchangeHeaderTable.add(row);
    }

    private void CreateFunctionalHeaderItem(String headerItem, String headerValue)
    {
        Object[] row = new Object[2];
        row[0] = headerItem;
        row[1] = headerValue;

        functionalGroupTable.add(row);
    }
    
    private void DisplayAllClaims(LightWeightLoops claims)
    {
        int rowNumber = 1;
        for (LightWeightLoop loop : claims)
        {
            LightWeightSegment subscriber = loop.GetSegment("SBR");
            LightWeightLoop subscriberInfo = loop.GetLoop("2010BA");
            LightWeightSegment subName = subscriberInfo.GetSegment("NM1");

            LightWeightLoop insuranceInfo = loop.GetLoop("2010BB");
            LightWeightSegment insuranceName = insuranceInfo.GetSegment("NM1");

            Object[] subInfoRow = new Object[5];
            subInfoRow[0] = rowNumber;
            subInfoRow[1] = subName.getElements().get_Item(2, LightWeightElement.class).getDataValue() + " " + subName.getElements().get_Item(3, LightWeightElement.class).getDataValue() + " " + subName.getElements().get_Item(4, LightWeightElement.class).getDataValue();
            subInfoRow[2] = insuranceName.getElements().get_Item(2, LightWeightElement.class).getDataValue();
            subInfoRow[3] = subscriber.getElements().get_Item(1, LightWeightElement.class).getDataValue();
            subInfoRow[4] = subscriber.getElements().get_Item(8, LightWeightElement.class).getDataValue();

            subscriberTable.add(subInfoRow);

            Collection<LightWeightLoop> claimLoop = loop.GetLoopCollection("2300");

            for (LightWeightLoop cl : claimLoop)
            {
                LightWeightSegment clm = cl.GetSegment("CLM");
                LightWeightSegment hi = cl.GetSegment("HI");

                Object[] claimInfoRow = new Object[4];
                claimInfoRow[0] = rowNumber;
                claimInfoRow[1] = clm.getElements().get_Item(0, LightWeightElement.class).getDataValue();
                claimInfoRow[2] = clm.getElements().get_Item(1, LightWeightElement.class).getDataValue();
                claimInfoRow[3] = hi.getElements().get_Item(0, LightWeightElement.class).getElements().get_Item(1, LightWeightElement.class).getDataValue();

                claimsTable.add(claimInfoRow);

                Collection<LightWeightLoop> serviceLineLoops = cl.GetLoopCollection("2400");
                for (LightWeightLoop sl : serviceLineLoops)
                {
                    //Get the service line segment
                    LightWeightSegment sv1 = sl.GetSegment("SV1");

                    Object[] serviceLineInfoRow = new Object[5];
                    serviceLineInfoRow[0] = rowNumber;
                    serviceLineInfoRow[1] = sv1.getElements().get_Item(0, LightWeightElement.class).getElements().get_Item(1, LightWeightElement.class).getDataValue();
                    serviceLineInfoRow[2] = sv1.getElements().get_Item(1, LightWeightElement.class).getDataValue();
                    serviceLineInfoRow[3] = sv1.getElements().get_Item(3, LightWeightElement.class).getDataValue();
                    serviceLineInfoRow[4] = sv1.getElements().get_Item(6, LightWeightElement.class).getElements().get_Item(0, LightWeightElement.class).getDataValue();

                    serviceLineTable.add(serviceLineInfoRow);
                }
            }

            //process situation where the subscriber and the patient are different
            Collection<LightWeightLoop> patHL = loop.GetLoopCollection("2000C");
            if (patHL != null && patHL.getCount() > 0)
            {

                Collection<LightWeightLoop> patClaimLoop = patHL.get_Item(0, LightWeightLoop.class).GetLoopCollection("2300");

                for (LightWeightLoop cl : patClaimLoop)
                {
                    LightWeightSegment clm = cl.GetSegment("CLM");
                    LightWeightSegment hi = cl.GetSegment("HI");

                    Object[] claimInfoRow = new Object[4];
                    claimInfoRow[0] = rowNumber;
                    claimInfoRow[1] = clm.getElements().get_Item(0, LightWeightElement.class).getDataValue();
                    claimInfoRow[2] = clm.getElements().get_Item(1, LightWeightElement.class).getDataValue();
                    claimInfoRow[3] = hi.getElements().get_Item(0, LightWeightElement.class).getElements().get_Item(1, LightWeightElement.class).getDataValue();

                    claimsTable.add(claimInfoRow);

                    Collection<LightWeightLoop> serviceLineLoops = cl.GetLoopCollection("2400");
                    for (LightWeightLoop sl : serviceLineLoops)
                    {
                        //Get the service line segment
                        LightWeightSegment sv1 = sl.GetSegment("SV1");

                        Object[] serviceLineInfoRow = new Object[3];
                        serviceLineInfoRow[0] = rowNumber;
                        serviceLineInfoRow[1] = sv1.getElements().get_Item(0, LightWeightElement.class).getElements().get_Item(1, LightWeightElement.class).getDataValue();
                        serviceLineInfoRow[2] = sv1.getElements().get_Item(1, LightWeightElement.class).getDataValue();

                        serviceLineTable.add(serviceLineInfoRow);
                    }
                }
            }

            rowNumber++;
        }
        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
       	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
       	for (Object[] row : subscriberTable)
       	{
       		model.addRow(row);
       	}
        //dataGrid.NavigateTo(0, "Subcriber Information");
    }

    private void DisplayBillingProviderInformation(LightWeightLoop billingProviderNameLoop)
    {
        //Get the Billing Provider's name
        LightWeightSegment nm1 = billingProviderNameLoop.GetSegment("NM1");
        txtBillingProviderName.setText(nm1.getElements().get_Item(2, LightWeightElement.class).getDataValue() + nm1.getElements().get_Item(3, LightWeightElement.class).getDataValue());

        //Get the street address
        LightWeightSegment n3 = billingProviderNameLoop.GetSegment("N3");
        txtProviderStreet.setText(n3.getElements().get_Item(0, LightWeightElement.class).getDataValue());

        //Get the city state and zip code
        LightWeightSegment n4 = billingProviderNameLoop.GetSegment("N4");
        txtProviderAddress.setText(n4.getElements().get_Item(0, LightWeightElement.class).getDataValue() + "," + n4.getElements().get_Item(1, LightWeightElement.class).getDataValue() + "," + n4.getElements().get_Item(2, LightWeightElement.class).getDataValue());
    }

    private void DisplaySubmitterInformation(LightWeightLoop submitterLoop)
    {
        //Get the submitter's name
        LightWeightSegment nm1 = submitterLoop.GetSegment("NM1");
        txtSubmitterName.setText(nm1.getElements().get_Item(2, LightWeightElement.class).getDataValue() + nm1.getElements().get_Item(3, LightWeightElement.class).getDataValue());

        //Get the submitter's contact information
        LightWeightSegment per = submitterLoop.GetSegment("PER");
        if (per != null)
        {
            txtSubmitterPhone.setText(per.getElements().get_Item(3, LightWeightElement.class).getDataValue());
        }
    }

    private void DisplayReceiverInformation(LightWeightLoop receiverLoop)
    {
        //Get the receiver's name
        LightWeightSegment nm1 = receiverLoop.GetSegment("NM1");
        txtReceiverName.setText(nm1.getElements().get_Item(2, LightWeightElement.class).getDataValue() + nm1.getElements().get_Item(3, LightWeightElement.class).getDataValue());
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
    
    private void drillDown() {
    	if (dataGrid.getSelectedRow()>=0) {
    		currentLevel++;
    		if (currentLevel==2) {
    			drillDown.setEnabled(false);
    			
    			Object rowNumber = claimsTable.get(dataGrid.getSelectedRow())[0];
    	    	
    	    	dataGrid.setModel(new DefaultTableModel(new Object[][] {},serviceLineInfoColumns));
    	    	
    	        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
    	        dm.getDataVector().removeAllElements();
    	        dm.fireTableDataChanged(); 
    	        
    	       	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
    	       	for (Object[] row : serviceLineTable)
    	       	{
    	       		if (row[0]==rowNumber)
    	       			model.addRow(row);
    	       	}
    		}
    		else if (currentLevel==1) {
    			Object rowNumber = claimsTable.get(dataGrid.getSelectedRow())[0];
    			subId=rowNumber;
    	    	dataGrid.setModel(new DefaultTableModel(new Object[][] {},claimsColumns));
    	    	
    	        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
    	        dm.getDataVector().removeAllElements();
    	        dm.fireTableDataChanged(); 
    	        
    	       	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
    	       	for (Object[] row : claimsTable)
    	       	{
    	       		if (row[0]==rowNumber)
    	       			model.addRow(row);
    	       	}
    		}
	    
    	}
    	else {
    		JOptionPane.showMessageDialog(this, "Select row first");
    	}
    }

    private void drillUp() {
    	if (currentLevel==2) {
    		currentLevel--;
    		drillDown.setEnabled(true);
    		
			Object rowNumber = subId;
			
	    	dataGrid.setModel(new DefaultTableModel(new Object[][] {},claimsColumns));
	    	
	        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
	        dm.getDataVector().removeAllElements();
	        dm.fireTableDataChanged(); 
	        
	       	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
	       	for (Object[] row : claimsTable)
	       	{
	       		if (row[0]==rowNumber)
	       			model.addRow(row);
	       	}
    	}
    	else if (currentLevel==1) {
    		currentLevel--;
    		drillDown.setEnabled(true);
        	dataGrid.setModel(new DefaultTableModel(new Object[][] { {"","",""}},subscriberColumns));
        	
            DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
            dm.getDataVector().removeAllElements();
            dm.fireTableDataChanged(); 
            
           	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
           	for (Object[] row : subscriberTable)
           	{
           		model.addRow(row);
           	}
    	}
    	
    	
    }
    
    private void btnLoad_Click()
    {
     	this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        //Reset the progress bar & tables
        pb2.setValue(0);

        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
        claimsTable.clear();
        serviceLineTable.clear();
        subscriberTable.clear();

        InitializeEDI();

        LoadEDI();
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

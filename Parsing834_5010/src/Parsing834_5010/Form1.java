package Parsing834_5010;

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
import RDPCrystalEDILibrary.EDIRulesReader;
import RDPCrystalEDILibrary.EDIRulesReader.ErrorEvent;
import RDPCrystalEDILibrary.EDIRulesReaderEventArg;
import RDPCrystalEDILibrary.EDISource;
import RDPCrystalEDILibrary.EDIValidator;
import RDPCrystalEDILibrary.FileType;
import RDPCrystalEDILibrary.Documents.DocumentLoop;
import RDPCrystalEDILibrary.Documents.DocumentSegment;
import RDPCrystalEDILibrary.Documents.X12.X125010Document;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.DTP;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.GS;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.HD;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.INS;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.ISA;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.N1;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.NM1;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.REF;
import jio.System.Collections.Generic.List;

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
	private JTextField txtSponser;
	private JTextField txtSponserID;
	private JTextField txtPayerName;
	private JTextField txtPayerID;
	
    private ArrayList<Object[]> memberTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> interchangeHeaderTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> functionalGroupTable = new ArrayList<Object[]>();

	
	public Form1() {
//please go to javonet to register for a free trial and get your javonet serial number at : my.javonet.com/signin?type=free 
        Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
        Activation.initializeJavonet();
        InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
	}
	
	private void InitializeComponents() {
		this.setSize(870, 750);
		this.setTitle("Loading HIPAA 5010 834 Benefit Enrollment");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 870, 50);
		JLabel lblTitle = new JLabel("   Loading HIPAA 5010 834 Benefit Enrollment");
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
		groupBox1.setBorder(BorderFactory.createTitledBorder("Sponser Name"));
		panel3.add(groupBox1);
		
		JLabel label4 = new JLabel("Name:");
		label4.setBounds(11,21,100,23);
		label4.setFont(new Font("Tahoma",0,12));
		groupBox1.add(label4);

		txtSponser = new JTextField();
		txtSponser.setBounds(70,18,189,27);
		txtSponser.setFont(new Font("Tahoma",0,12));
		groupBox1.add(txtSponser);
		
		JLabel label5 = new JLabel("ID:");
		label5.setBounds(11,48,100,23);
		label5.setFont(new Font("Tahoma",0,12));
		groupBox1.add(label5);
		
		txtSponserID = new JTextField();
		txtSponserID.setBounds(70,42,189,27);
		txtSponserID.setFont(new Font("Tahoma",0,12));
		groupBox1.add(txtSponserID);
		
		JPanel groupBox3 = new JPanel(null);
		groupBox3.setBounds(297, 6, 456, 81);
		groupBox3.setBorder(BorderFactory.createTitledBorder("Payer Information"));
		panel3.add(groupBox3);
		
		JLabel label8 = new JLabel("Name:");
		label8.setBounds(11,23,100,23);
		label8.setFont(new Font("Tahoma",0,12));
		groupBox3.add(label8);
		
		txtPayerName = new JTextField();
		txtPayerName.setBounds(70,20,189,27);
		txtPayerName.setFont(new Font("Tahoma",0,12));
		groupBox3.add(txtPayerName);
		
		JLabel label9 = new JLabel("ID:");
		label9.setBounds(11,50,100,23);
		label9.setFont(new Font("Tahoma",0,12));
		groupBox3.add(label9);

		txtPayerID = new JTextField();
		txtPayerID.setBounds(70,44,189,27);
		txtPayerID.setFont(new Font("Tahoma",0,12));
		groupBox3.add(txtPayerID);
		
		JPanel groupBox4 = new JPanel(null);
		groupBox4.setBounds(8, 199, 690, 226);
		groupBox4.setBorder(BorderFactory.createTitledBorder("Policy Information"));
		panel3.add(groupBox4);
		
		Object[][] dataGriddata = {{"","","","","","",""}};
		String[] dataGridcolumnNames = {
				"Member Name","Subscriber ID","Subscriber","Relationship","Health Coverage","Policy Begin Date","Policy End Date"};
		dataGrid = new JTable(new DefaultTableModel(dataGriddata,dataGridcolumnNames));
		dataGrid.setFont(new Font("Microsoft Sans Serif",0,16));
		dataGrid.setFillsViewportHeight(true);
		dataGrid.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,16));
		dataGrid.setAutoResizeMode(0);
		TableColumn dataGridcolumn = null;
		for (int i = 0; i < 7; i++) {
			dataGridcolumn = dataGrid.getColumnModel().getColumn(i);
			dataGridcolumn.setPreferredWidth(100);
		}
		
		JScrollPane dataGridscrollPane = new JScrollPane(dataGrid);
		dataGridscrollPane.setBounds(3,20,684,200);
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
        ediValidator.setEDIRulesFile("EDIFiles\\Rules_5010_834_005010X220A1.Rules");

        //load from a file rather from a string
        ediValidator.setEDISource(EDISource.File);

        //Set the type of file to load
        ediValidator.setEDIFileType(FileType.X12);

        //Set the EDI data to load
        ediValidator.setEDIFile("EDIFiles\\Sample_834_5010.txt");

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
                //Show the document on the screen
                ediDocumentViewer.LoadDocument(ediValidator.getEDILightWeightDocument());

                //Convert to a typed document
                X125010Document doc = new X125010Document(ediValidator.getEDILightWeightDocument());

                //Display ISA and GS header segments
                DisplayInterchangeHeader(doc.getMainSection());
                DocumentLoop functionalHeaderLoop = doc.getMainSection().GetLoop("FUNCTIONAL GROUP");
                DisplayFunctionalHeader(functionalHeaderLoop);

                //Get the ST transaction loop
                DocumentLoop stHeaderLoop = functionalHeaderLoop.GetLoop("ST HEADER");

                //Display Sponser Info
                DocumentLoop sponserSection = stHeaderLoop.GetLoop("1000A");
                DisplaySponserInformation(sponserSection);

                //Display Payer Info
                DocumentLoop payerSection = stHeaderLoop.GetLoop("1000B");
                DisplayPayerInformation(payerSection);

                //Display member insurance info
                List<DocumentLoop> memberLevels = stHeaderLoop.GetLoops("2000");

                DisplayMemberLevels(memberLevels);
            }
        }
        catch (Exception e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage());
        }
        this.setCursor(Cursor.getDefaultCursor());
        JOptionPane.showMessageDialog(this, "Parsing Complete");
    }

    private void DisplayInterchangeHeader(DocumentLoop header)
    {
        ISA isa = header.<ISA>GetSegment(ISA.class);
        CreateInterchangeHeaderItem("Authorization Information Qualifier", isa.getAuthorizationInformationQualifier());
        CreateInterchangeHeaderItem("Authorization Information", isa.getAuthorizationInformation());
        CreateInterchangeHeaderItem("Security Information Qualifier", isa.getSecurityInformationQualifier());
        CreateInterchangeHeaderItem("Security Information", isa.getSecurityInformation());
        CreateInterchangeHeaderItem("Interchange ID Qualifier1", isa.getInterchangeIDQualifier1());
        CreateInterchangeHeaderItem("Interchange Sender ID", isa.getInterchangeSenderID());
        CreateInterchangeHeaderItem("Interchange ID Qualifier2", isa.getInterchangeIDQualifier2());
        CreateInterchangeHeaderItem("Interchange Receiver ID", isa.getInterchangeReceiverID());
        CreateInterchangeHeaderItem("Interchange Date", isa.getInterchangeDate());
        CreateInterchangeHeaderItem("Interchange Time", isa.getInterchangeTime());
        CreateInterchangeHeaderItem("Repetition Separator", isa.getRepetitionSeparator());
        CreateInterchangeHeaderItem("Interchange Control Version Number", isa.getInterchangeControlVersionNumber());
        CreateInterchangeHeaderItem("Interchange Control Number", isa.getInterchangeControlNumber());
        CreateInterchangeHeaderItem("Acknowledgment Requested", isa.getAcknowledgmentRequested());
        CreateInterchangeHeaderItem("Interchange Usage Indicator", isa.getInterchangeUsageIndicator());
        CreateInterchangeHeaderItem("Component Element Separator", isa.getComponentElementSeparator());

        DefaultTableModel dm = (DefaultTableModel)dgInterchangeHeader.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
       	DefaultTableModel model = (DefaultTableModel) dgInterchangeHeader.getModel();
       	for (Object[] row : interchangeHeaderTable)
       	{
       		model.addRow(row);
       	}
    }

    private void DisplayFunctionalHeader(DocumentLoop header)
    {
        GS gs = header.<GS>GetSegment(GS.class);
        CreateFunctionalHeaderItem("Functional Identifier Code", gs.getFunctionalIdentifierCode());
        CreateFunctionalHeaderItem("Application Sender’s Code", gs.getApplicationReceiverCode());
        CreateFunctionalHeaderItem("Application Receiver’s Code", gs.getApplicationReceiverCode());
        CreateFunctionalHeaderItem("Date", gs.getDate());
        CreateFunctionalHeaderItem("Time", gs.getTime());
        CreateFunctionalHeaderItem("Group Control Number", gs.getGroupControlNumber());
        CreateFunctionalHeaderItem("Responsible Agency Code", gs.getResponsibleAgencyCode());
        CreateFunctionalHeaderItem("Version / Release / Industry Identifier Code", gs.getVersionReleaseIndustryIdentifierCode());

        DefaultTableModel dm = (DefaultTableModel)dgFunctionalHeader.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
    	DefaultTableModel model = (DefaultTableModel) dgFunctionalHeader.getModel();
       	for (Object[] row : functionalGroupTable)
       	{
       		model.addRow(row);
       	}
    }

    private void DisplaySponserInformation(DocumentLoop submitterSection)
    {
        //Get the sponser name
        N1 n1 = submitterSection.<N1>GetSegment(N1.class);
        txtSponser.setText(n1.getName());
        txtSponserID.setText(n1.getIdentificationCode());
    }

    private void DisplayPayerInformation(DocumentLoop receiverSection)
    {
        //Get the payer name
        N1 n1 = receiverSection.<N1>GetSegment(N1.class);
        txtPayerName.setText(n1.getName());
        txtPayerID.setText(n1.getIdentificationCode());
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

    private void DisplayMemberLevels(List<DocumentLoop> memberLevels)
    {
        for (DocumentLoop memberLoop : memberLevels)
        {
            INS ins = memberLoop.<INS>GetSegment(INS.class);
            REF subscriberID = memberLoop.<REF>GetSegment(REF.class);

            DocumentLoop memberNameLoop = memberLoop.GetLoop("2100A");
            NM1 memberName = memberNameLoop.<NM1>GetSegment(NM1.class);

            DocumentLoop healthCoverageLoop = memberLoop.GetLoop("2300");
            HD healthCoverage = healthCoverageLoop.<HD>GetSegment(HD.class);

            List<DocumentSegment> dates = healthCoverageLoop.GetSegments("DTP");

            DTP policyBeginDate = (DTP) dates.get_Item(0, DTP.class);
            DTP policyEndDate = (DTP) dates.get_Item(1, DTP.class);

            Object[] subInfoRow = new Object[7];
            subInfoRow[0] = memberName.getNameLastOrOrganizationName() + " " + memberName.getFirstName();
            subInfoRow[1] = subscriberID.getReferenceIdentification();
            subInfoRow[2] = ins.getYesNoConditionOrResponseCode1();
            subInfoRow[3] = ins.getIndividualRelationshipCode();
            subInfoRow[4] = healthCoverage.getInsuranceLineCode();
            subInfoRow[5] = policyBeginDate.getDateTimePeriod();
            subInfoRow[6] = policyEndDate.getDateTimePeriod();

            memberTable.add(subInfoRow);
        }
    	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
       	for (Object[] row : memberTable)
       	{
       		model.addRow(row);
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
        memberTable.clear();

        InitializeEDI();

        LoadEDI();
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

package Parsing835_4010;

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
import RDPCrystalEDILibrary.EDISpecification;
import RDPCrystalEDILibrary.EDIValidator;
import RDPCrystalEDILibrary.FileType;
import RDPCrystalEDILibrary.LightWeightElement;
import RDPCrystalEDILibrary.LightWeightLoop;
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
	private JTextField txtPayerAdd1;
	private JTextField txtPayerAdd2;
	private JTextField txtPayerName;
	private JTextField txtPayeeName;
	private JTextField txtPayeeAdd1;
	private JTextField txtPayeeAdd2;
	private JButton drillDown, drillUp;
	
    private ArrayList<Object[]> claimPaymentInfoTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> servicePaymentInfoTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> interchangeHeaderTable = new ArrayList<Object[]>();
    private ArrayList<Object[]> functionalGroupTable = new ArrayList<Object[]>();
    
	String[] claimPaymentInfoColumns = {
			"Claim #", "Patient Name","Insured Name"
	};
	
	String[] servicePaymentInfoColumns = {
			"RowKey","Product/Service ID", "Amount", "Quantity"
	};

	
	public Form1() {
//please go to javonet to register for a free trial and get your javonet serial number at : my.javonet.com/signin?type=free 
        Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
        Activation.initializeJavonet();
        InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
		
		Form1_Load();
	}
	
	private void InitializeComponents() {
		this.setSize(870, 750);
		this.setTitle("Loading HIPAA 4010 835 Claim Payment/Advice file");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 870, 50);
		JLabel lblTitle = new JLabel("   Loading HIPAA 4010 835 Claim Payment/Advice file");
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
		groupBox1.setBorder(BorderFactory.createTitledBorder("PAYER Identification (First ST Group)"));
		panel3.add(groupBox1);
		
		JLabel label4 = new JLabel("Name:");
		label4.setBounds(11,21,100,23);
		label4.setFont(new Font("Tahoma",0,12));
		groupBox1.add(label4);

		txtPayerName = new JTextField();
		txtPayerName.setBounds(70,18,189,27);
		txtPayerName.setFont(new Font("Tahoma",0,12));
		groupBox1.add(txtPayerName);
		
		JLabel label5 = new JLabel("Address 1:");
		label5.setBounds(11,48,100,23);
		label5.setFont(new Font("Tahoma",0,12));
		groupBox1.add(label5);
		
		txtPayerAdd1 = new JTextField();
		txtPayerAdd1.setBounds(70,42,189,27);
		txtPayerAdd1.setFont(new Font("Tahoma",0,12));
		groupBox1.add(txtPayerAdd1);
		
		JLabel label7 = new JLabel("Address 2:");
		label7.setBounds(11,48,100,23);
		label7.setFont(new Font("Tahoma",0,12));
		groupBox1.add(label7);
		
		txtPayerAdd2 = new JTextField();
		txtPayerAdd2.setBounds(70,42,189,27);
		txtPayerAdd2.setFont(new Font("Tahoma",0,12));
		groupBox1.add(txtPayerAdd2);
		
		JPanel groupBox3 = new JPanel(null);
		groupBox3.setBounds(297, 6, 456, 81);
		groupBox3.setBorder(BorderFactory.createTitledBorder("PAYEE Identification (First ST Group)"));
		panel3.add(groupBox3);
		
		JLabel label8 = new JLabel("Name:");
		label8.setBounds(11,23,100,23);
		label8.setFont(new Font("Tahoma",0,12));
		groupBox3.add(label8);
		
		txtPayeeName = new JTextField();
		txtPayeeName.setBounds(70,20,189,27);
		txtPayeeName.setFont(new Font("Tahoma",0,12));
		groupBox3.add(txtPayeeName);
		
		JLabel label9 = new JLabel("Address 1:");
		label9.setBounds(11,50,100,23);
		label9.setFont(new Font("Tahoma",0,12));
		groupBox3.add(label9);

		txtPayeeAdd1 = new JTextField();
		txtPayeeAdd1.setBounds(70,44,189,27);
		txtPayeeAdd1.setFont(new Font("Tahoma",0,12));
		groupBox3.add(txtPayeeAdd1);
		
		JLabel label6 = new JLabel("Address 2:");
		label6.setBounds(11,48,100,23);
		label6.setFont(new Font("Tahoma",0,12));
		groupBox1.add(label6);
		
		txtPayeeAdd2 = new JTextField();
		txtPayeeAdd2.setBounds(70,42,189,27);
		txtPayeeAdd2.setFont(new Font("Tahoma",0,12));
		groupBox1.add(txtPayeeAdd2);
		
		JPanel groupBox4 = new JPanel(null);
		groupBox4.setBounds(8, 90, 690, 226);
		groupBox4.setBorder(BorderFactory.createTitledBorder("Policy Information"));
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
		
		
		//TODO: Add support for nested sub records
		Object[][] dataGriddata = {{"","",""}};

		dataGrid = new JTable(new DefaultTableModel(dataGriddata,claimPaymentInfoColumns));
		dataGrid.setFont(new Font("Microsoft Sans Serif",0,12));
		dataGrid.setFillsViewportHeight(true);
		dataGrid.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,12));
		dataGrid.setAutoResizeMode(0);
		TableColumn dataGridcolumn = null;
		for (int i = 0; i < 3; i++) {
			dataGridcolumn = dataGrid.getColumnModel().getColumn(i);
			dataGridcolumn.setPreferredWidth(100);
		}
		
		JScrollPane dataGridscrollPane = new JScrollPane(dataGrid);
		dataGridscrollPane.setBounds(10,50,504,160);
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
        ediValidator.setEDIDataString(SampleEDIFileData.SampleEDI835Data);

        //load from a file rather from a string
        ediValidator.setEDISource(EDISource.DataString);

        //Set the type of file to load
        ediValidator.setEDIFileType(FileType.X12);

        ediValidator.setAutoDetectDelimiters(true);

        //Tell the ediValidator to load the EDI file data
        ediValidator.setLoadValidatedData(true);
        
        EDISpecification spec = EDISpecification.FindSpecificationFromString(SampleEDIFileData.SampleEDI835Data);
        System.out.println(spec.getEDIFileType());
        System.out.println(spec.getX12TransactionSetCode());
        System.out.println(spec.getX12Version());
        System.out.println(spec.getX12VersionCode());
    }

    private void LoadEDI()
    {
        try
        {
            ediValidator.Validate();

            //The EDI Document will contain all the data of the 834 file
            if (ediValidator.getEDILightWeightDocument() != null)
            {
                EDILightWeightDocument file835 = ediValidator.getEDILightWeightDocument();

                LightWeightLoop interchangeHeaderLoop = file835.getLoops().get_Item(0, LightWeightLoop.class).GetLoop("INTERCHANGE HEADER");

                DisplayInterchangeHeader(interchangeHeaderLoop.GetSegment("ISA"));

                LightWeightLoop functionalHeaderLoop = interchangeHeaderLoop.GetLoop("FUNCTIONAL GROUP");

                DisplayFunctionalHeader(functionalHeaderLoop.GetSegment("GS"));

                //Get the ST loops.  The sample EDI file contains 2 ST-SE groups however we will only display one
                Collection<LightWeightLoop> stHeaderLoops = functionalHeaderLoop.GetLoopCollection("ST HEADER");

                int numberOfST_SE_groups = 1;
                for(LightWeightLoop stHeaderLoop : stHeaderLoops)
                {
                    //In this demo we are only displaying data in the first ST-SE group
                    //However you can display as many ST-SE groups you desire

                    if (numberOfST_SE_groups == 1)
                    {
                        //Get the payer identification loop. In an 835 file the payer identification loop is 1000A
                        LightWeightLoop payerIdentificationLoop = stHeaderLoop.GetLoop("1000A");
                        DisplayPayerIdenfificationInformation(payerIdentificationLoop);

                        // Get the receiver loop. In an 837 file the receiver loop is 1000B
                        LightWeightLoop payeeIdentificationLoop = stHeaderLoop.GetLoop("1000B");
                        DisplayPayeeIdenfificationInformation(payeeIdentificationLoop);

                        // Get the 2000A BILLING/PAY-TO PROVIDER HIERARCHICAL LEVEL
                        //In an 837 file this loop is 2000A
                        LightWeightLoop headerNumberLoop = stHeaderLoop.GetLoop("2000");
                        Collection<LightWeightLoop> claimPaymentInformationLoops = headerNumberLoop.GetLoopCollection("2100");

                        DisplayAllClaimPayments(claimPaymentInformationLoops);

                        numberOfST_SE_groups++;
                    }
                }

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

    private void DisplayAllClaimPayments(Collection<LightWeightLoop> claimPayments)
    {
        int rowNumber = 1;
        for (LightWeightLoop loop : claimPayments)
        {
            Collection<LightWeightSegment> parties = loop.GetSegmentCollection("NM1");

            String patientName = parties.get_Item(0, LightWeightSegment.class).getElements().get_Item(2, LightWeightElement.class).getDataValue() + " " + parties.get_Item(0, LightWeightSegment.class).getElements().get_Item(3, LightWeightElement.class).getDataValue();
            String insuredName = parties.get_Item(1, LightWeightSegment.class).getElements().get_Item(2, LightWeightElement.class).getDataValue() + " " + parties.get_Item(1, LightWeightSegment.class).getElements().get_Item(3, LightWeightElement.class).getDataValue();

            Object[] subInfoRow = new Object[3];
            subInfoRow[0] = rowNumber;
            subInfoRow[1] = patientName;
            subInfoRow[2] = insuredName;

            claimPaymentInfoTable.add(subInfoRow);

            Collection<LightWeightLoop> paymentInformationLoop = loop.GetLoopCollection("2110");

            for (LightWeightLoop paymentInformation : paymentInformationLoop)
            {
                LightWeightSegment svc = paymentInformation.GetSegment("SVC");

                Object[] claimInfoRow = new Object[4];
                claimInfoRow[0] = rowNumber;
                claimInfoRow[1] = svc.getElements().get_Item(0, LightWeightElement.class).getElements().get_Item(1, LightWeightElement.class).getDataValue();
                claimInfoRow[2] = svc.getElements().get_Item(1, LightWeightElement.class).getDataValue();
                claimInfoRow[3] = svc.getElements().get_Item(4, LightWeightElement.class).getDataValue();

                servicePaymentInfoTable.add(claimInfoRow);
            }

            rowNumber++;
        }

        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
       	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
       	for (Object[] row : claimPaymentInfoTable)
       	{
       		model.addRow(row);
       	}
        //dataGrid.NavigateTo(0, "Claim Payment Information");
    }

    private void DisplayPayerIdenfificationInformation(LightWeightLoop loop)
    {
        //Get the name
        LightWeightSegment n1 = loop.GetSegment("N1");
        txtPayerName.setText(n1.getElements().get_Item(1, LightWeightElement.class).getDataValue());

        //Get the address 1
        LightWeightSegment n3 = loop.GetSegment("N3");
        if (n3 != null)
        {
            txtPayerAdd1.setText(n3.getElements().get_Item(0, LightWeightElement.class).getDataValue());
        }

        //Get the address 1
        LightWeightSegment n4 = loop.GetSegment("N4");
        if (n4 != null)
        {
            txtPayerAdd2.setText(n4.getElements().get_Item(0, LightWeightElement.class).getDataValue() + " " + n4.getElements().get_Item(0, LightWeightElement.class).getDataValue() + " " + n4.getElements().get_Item(0, LightWeightElement.class).getDataValue());
        }
    }

    private void DisplayPayeeIdenfificationInformation(LightWeightLoop loop)
    {
        //Get the name
        LightWeightSegment n1 = loop.GetSegment("N1");
        txtPayeeName.setText(n1.getElements().get_Item(1, LightWeightElement.class).getDataValue());

        //Get the address 1
        LightWeightSegment n3 = loop.GetSegment("N3");
        if (n3 != null)
        {
            txtPayeeAdd1.setText(n3.getElements().get_Item(0, LightWeightElement.class).getDataValue());
        }

        //Get the address 1
        LightWeightSegment n4 = loop.GetSegment("N4");
        if (n4 != null)
        {
            txtPayeeAdd2.setText(n4.getElements().get_Item(0, LightWeightElement.class).getDataValue() + " " + n4.getElements().get_Item(0, LightWeightElement.class).getDataValue() + " " + n4.getElements().get_Item(0, LightWeightElement.class).getDataValue());
        }
    }
    
    private void Form1_Load()
    {
        //EDI Rules files are used the EDIValidator component to validate and load EDI files.
        //This is just a sample EDI rules file. Please go to our online store for a production level rules file
        //We offer the following file (http://www.rdpcrystal.com/products/edi-library/edi-rules-creator/edi-rules-files/) or you can create 
        //your own with the EDI Rules Creator Studio application
        ediValidator.setEDIRulesFile("EDIFiles\\Rules_4010_835_004010X091A1.Rules");
    }

    private void btnLoad_Click()
    {
    	this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        //Reset the progress bar & tables
        pb2.setValue(0);

        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        servicePaymentInfoTable.clear();
        claimPaymentInfoTable.clear();

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
    
    private void drillDown() {
    	if (dataGrid.getSelectedRow()>=0) {
    		drillDown.setEnabled(false);
    	Object rowNumber = claimPaymentInfoTable.get(dataGrid.getSelectedRow())[0];
    	
    	dataGrid.setModel(new DefaultTableModel(new Object[][] { {"","","",""}},servicePaymentInfoColumns));
    	
        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
       	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
       	for (Object[] row : servicePaymentInfoTable)
       	{
       		if (row[0]==rowNumber)
       			model.addRow(row);
       	}
    	}
    	else {
    		JOptionPane.showMessageDialog(this, "Select row first");
    	}
    }

    private void drillUp() {
    	drillDown.setEnabled(true);
    	dataGrid.setModel(new DefaultTableModel(new Object[][] { {"","",""}},claimPaymentInfoColumns));
    	
        DefaultTableModel dm = (DefaultTableModel)dataGrid.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 
        
       	DefaultTableModel model = (DefaultTableModel) dataGrid.getModel();
       	for (Object[] row : claimPaymentInfoTable)
       	{
       		model.addRow(row);
       	}
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

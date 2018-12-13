package CreateEDIDocument;

import java.awt.Button;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Common.Activation;
import RDPCrystalEDILibrary.Delimiters;
import RDPCrystalEDILibrary.Documents.DocumentLoop;
import RDPCrystalEDILibrary.Documents.X12.X125010Document;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.BHT;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.GE;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.GS;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.IEA;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.ISA;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.SE;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.ST;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.EDIFileLoader ediFileLoader;
	private JTextArea txtEDIFile;
	
	public Form1() {
		Activation.setLicense("lberwid@sdncenter.pl", "Jm9a-Mr78-Hi5x-r4P5-x2SJ");
		Activation.initializeJavonet(); //TODO: temporarily required to be removed in final version
		InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
		
		Form1_Load();
	}
	
	private void InitializeComponents() {
		this.setSize(870, 690);
		this.setTitle("Creating an X12 EDI Document");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 870, 50);
		JLabel lblTitle = new JLabel("   Creating an X12 EDI Document");
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
		
		Button createFile = new Button("Generate EDI File");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+582,hinsets.top+10,256,63);
		createFile.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCreateEDIFile_Click();
			}
			
		});
		header.add(createFile);
		
		this.add(header);
		
		JPanel content = new JPanel(null);		
		content.setBounds(insets.left,insets.top+160,850,640);
		
		insets = content.getInsets();
		
		JLabel lblDataToValidate = new JLabel("EDI File Structure To Create");
		lblDataToValidate.setBounds(10,10,400,20);
		lblDataToValidate.setFont(new Font("Tahoma",0,18));
		content.add(lblDataToValidate);
		
		JLabel ediStructure = new JLabel(new ImageIcon(".\\Resources\\EDIStructure1.png"));
		ediStructure.setBounds(10,35,393,383);
		ediStructure.setBackground(Color.white);
		
		content.add(ediStructure);
		
		
		JLabel lblLoadedData = new JLabel("Newly Created EDI file");
		lblLoadedData.setBounds(10+425,10,200,20);
		lblLoadedData.setFont(new Font("Tahoma",0,18));
		content.add(lblLoadedData);
		
		txtEDIFile = new JTextArea(20,0);
		Border border2 = BorderFactory.createLineBorder(Color.BLACK);
		txtEDIFile.setBorder(BorderFactory.createCompoundBorder(border2,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		txtEDIFile.setBorder(border2);
		txtEDIFile.setFont(new Font("Microsoft Sans Serif",0,16));
		txtEDIFile.setEditable(true);
		txtEDIFile.setBounds(insets.left+10+425,insets.top+35,393,383);
		content.add(txtEDIFile);
		
		JLabel lblReadingRules = new JLabel("Location to save EDI file:");
		lblReadingRules.setBounds(10,35+176+35+176+10,200,20);
		lblReadingRules.setFont(new Font("Tahoma",0,18));
		content.add(lblReadingRules);
	
		JTextField txtEDIFileLocation = new JTextField("C:\\SampleEDIFile.txt");
		txtEDIFileLocation.setBounds(220, 35+176+35+176+10, 400, 20);
		txtEDIFileLocation.setFont(new Font("Tahoma",0,18));
		content.add(txtEDIFileLocation);
		
		JButton btnSourceCode = new JButton("Source Code");
		btnSourceCode.setBackground(new Color(2,119,189));
		btnSourceCode.setForeground(Color.white);
		btnSourceCode.setFont(new Font("Microsoft Sans Serif",0,16));
		btnSourceCode.setBounds(insets.left+610+55,35+376+35+136,165,32);
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

		this.add(content);
		this.setVisible(true);
	}
	
    private void Form1_Load()
    {
    	
    }
    
    private void btnCreateEDIFile_Click()
    {
        try
        {
            X125010Document sampleEDIFile = new X125010Document();

            //Create an interchance loop. This loop will contain all other loops in the EDI structure
            DocumentLoop interchangeHeader = sampleEDIFile.getMainSection().CreateLoop("Interchange Header");
                
            //Create an ISA segment in the interchange loop
            ISA isa = interchangeHeader.<ISA>CreateSegment(ISA.class);
            isa.setAuthorizationInformationQualifier("00");
            isa.setAuthorizationInformation("");
            isa.setSecurityInformationQualifier("00");
            isa.setSecurityInformation("");
            isa.setInterchangeIDQualifier1("ZZ");
            isa.setInterchangeSenderID("InterchangeSenderID");
            isa.setInterchangeIDQualifier2("ZZ");
            isa.setInterchangeReceiverID("InterchangeReceiverID");
            isa.setInterchangeDate("150303");
            isa.setInterchangeTime("1804");
            isa.setRepetitionSeparator("^");
            isa.setInterchangeControlVersionNumber("00501");
            isa.setInterchangeControlNumber("1");
            isa.setAcknowledgmentRequested("1");
            isa.setInterchangeUsageIndicator("P");
            isa.setComponentElementSeparator(":");

            //Now create the function header section
            DocumentLoop functionalHeader = interchangeHeader.CreateLoop("Functional Header");

            GS gs = functionalHeader.<GS>CreateSegment(GS.class);
            gs.setFunctionalIdentifierCode("HC");
            gs.setApplicationSenderCode("ApplicationSenderCode");
            gs.setApplicationReceiverCode("ApplicationReceiverCode");
            gs.setDate("20150303");
            gs.setTime("1424");
            gs.setGroupControlNumber("1234");
            gs.setResponsibleAgencyCode("X");
            gs.setVersionReleaseIndustryIdentifierCode("005010X222");
          
            //Create a transaction header loop
            DocumentLoop transactionHeader = functionalHeader.CreateLoop("Transaction Header");
            ST st = transactionHeader.<ST>CreateSegment(ST.class);
            st.setTransactionSetIdentifierCode("837");
            st.setTransactionSetControlNumber("1");
            st.setImplementationConventionReference("005010X222");

            //Add an BHT segment to the transaction header loop
            BHT bht = transactionHeader.<BHT>CreateSegment(BHT.class);
            bht.setHierarchicalStructureCode("0019");
            bht.setTransactionSetPurposeCode("00");
            bht.setReferenceIdentification("44445");
            bht.setDate("20040213");
            bht.setTime("0345");

            //Create the end of the transaction (SE segment). This must be done in a loop.
            DocumentLoop endOfTransactionLoop = transactionHeader.CreateLoop("End Of Transaction");
            SE se = endOfTransactionLoop.<SE>CreateSegment(SE.class);
            se.setTransactionSetControlNumber("1");
            se.setNumberOfIncludedSegments("3");

            //Create the end of the functional group (GE segment). This must be done in a loop.
            DocumentLoop endOfFunctionalGroupLoop = functionalHeader.CreateLoop("End Functional Group");
            GE ge = endOfFunctionalGroupLoop.<GE>CreateSegment(GE.class);
            ge.setGroupControlNumber("11234");
            ge.setNumberOfTransactionSetsIncluded("1");

            //Finally create the end of the interchange loop (IEA segment). This must be done in a loop.
            DocumentLoop endInterchangeSection = interchangeHeader.CreateLoop("End Interchange");
            IEA iea = endInterchangeSection.<IEA>CreateSegment(IEA.class);
            iea.setInterchangeControlNumber("123");
            iea.setNumberOfIncludedFunctionalGroups("1");
  
            //Generate the EDI file
            Delimiters d = new Delimiters();
            String ediData = sampleEDIFile.ToEDIString(d);

            txtEDIFile.setText(ediData);
        }
        catch (Exception ex)
        {
        	JOptionPane.showMessageDialog(this, ex.getMessage());
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

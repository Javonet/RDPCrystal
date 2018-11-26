package Create270EDIFile;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import Common.Activation;
import RDPCrystalEDILibrary.Delimiters;
import RDPCrystalEDILibrary.FileType;
import RDPCrystalEDILibrary.Documents.DocumentLoop;
import RDPCrystalEDILibrary.Documents.X12.X125010Document;
import RDPCrystalEDILibrary.Documents.X12.IG5010.Segments.*;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.UI.Winforms.Controls.EDIDocumentViewer ediDocumentViewer;
	private RDPCrystalEDILibrary.EDIFileLoader ediFileLoader;
	private JTextArea textArea;
	
	public Form1() {
		Activation.setLicense("dpooran@rdpcrystal.com", "z8X4-e3RE-n9BG-r2PM-Ky6e");
		Activation.initializeJavonet(); //TODO: temporarily required to be removed in final version
		InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
	}
	
	private void InitializeComponents() {
		this.setSize(1295, 953);
		this.setTitle("Create HIPAA 5010 270 EDI File");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 1275, 50);
		JLabel lblTitle = new JLabel("   Create HIPAA 5010 270 EDI File");
		lblTitle.setFont(new Font("Microsoft Sans Serif",1,18));
		lblTitle.setForeground(Color.white);
		title.add(lblTitle);
		this.add(title);
		
		JPanel header = new JPanel(null);
		Insets hinsets = header.getInsets();
		JLabel logo = new JLabel(new ImageIcon(".\\Resources\\rdplogo.png"));
		logo.setBounds(hinsets.left,hinsets.top+10,350,97);
		header.setBounds(insets.left,insets.top+50,1275,110);
		
		header.add(logo);
		
		Button createFile = new Button("CREATE 270 FILE");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+802+55,hinsets.top+10,384,97);
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
		content.setSize(1275,103);
		content.setBounds(insets.left,insets.top+160,1275,823);
		
		insets = content.getInsets();
		
		textArea = new JTextArea(20,0);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		textArea.setBorder(BorderFactory.createCompoundBorder(border,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		textArea.setBorder(border);
		textArea.setFont(new Font("Microsoft Sans Serif",0,16));
		textArea.setEditable(true);

		textArea.setBounds(insets.left+30,insets.top+30,770,633);
		content.add(textArea);
		
		Button btnSourceCode = new Button("Source Code");
		btnSourceCode.setBackground(new Color(2,119,189));
		btnSourceCode.setForeground(Color.white);
		btnSourceCode.setFont(new Font("Microsoft Sans Serif",0,16));
		btnSourceCode.setBounds(insets.left+1020+55,insets.top+843,165,32);
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
		this.ediDocumentViewer = new RDPCrystalEDILibrary.UI.Winforms.Controls.EDIDocumentViewer();
		this.ediDocumentViewer.setBounds(insets.left+830,insets.top+30,413,635);
		content.add(this.ediDocumentViewer);

		this.add(content);
		this.setVisible(true);
	}
	
    private void btnCreateEDIFile_Click()
    {
		X125010Document doc270 = new X125010Document();

        doc270.setAutoPlaceCorrectNumOfSegments(true);  //puts the correct number of segments in X12 SE segment
        doc270.setAutoPlaceNumOfTransactions(true);
        doc270.setAutoPlaceNumOfFunctionalGroups(true);
        doc270.setPadISAElementValues(true);

        DocumentLoop interchangeHeader = doc270.getMainSection().CreateLoop("Interchange Header");

        ISA isa = interchangeHeader.<ISA>CreateSegment(ISA.class);
        isa.setAuthorizationInformationQualifier("00");
        isa.setAuthorizationInformation("");
        isa.setSecurityInformationQualifier("00");
        isa.setSecurityInformation("");
        isa.setInterchangeIDQualifier1("ZZ");
        isa.setInterchangeSenderID("SenderID");
        isa.setInterchangeIDQualifier2("ZZ");
        isa.setInterchangeReceiverID("ReceiverID");
        isa.setInterchangeDate("030101");
        isa.setInterchangeTime("1253");
        isa.setRepetitionSeparator("^");
        isa.setInterchangeControlVersionNumber("00501");
        isa.setInterchangeControlNumber("000000905");
        isa.setAcknowledgmentRequested("1");
        isa.setInterchangeUsageIndicator("T");
        isa.setComponentElementSeparator(":");

        //Now create the function header section
        DocumentLoop functionalHeader = interchangeHeader.CreateLoop("Functional Header");

        GS gs = functionalHeader.<GS>CreateSegment(GS.class);
        gs.setFunctionalIdentifierCode("HS");
        gs.setApplicationSenderCode("ApplicationSenderCode");
        gs.setApplicationReceiverCode("ApplicationReceiverCode");
        gs.setDate("20150303");
        gs.setTime("1424");
        gs.setGroupControlNumber("1234");
        gs.setResponsibleAgencyCode("X");
        gs.setVersionReleaseIndustryIdentifierCode("005010X279");

        //Create a transaction header loop
        DocumentLoop transactionHeader = functionalHeader.CreateLoop("Transaction Header");
        // ST*270*1234*005010X279~
        ST st = transactionHeader.<ST>CreateSegment(ST.class);
        st.setTransactionSetIdentifierCode("270");
        st.setTransactionSetControlNumber("000");
        st.setImplementationConventionReference("005010X279");

        //BHT*0022*13*10001234*20060501*1319~
        BHT bht = transactionHeader.<BHT>CreateSegment(BHT.class);
        bht.setHierarchicalStructureCode("0022");
        bht.setTransactionSetPurposeCode("13");
        bht.setReferenceIdentification("10001234");
        bht.setDate("20060501");
        bht.setTime("1319");

        DocumentLoop loop2000A = transactionHeader.CreateLoop("INFORMATION SOURCE LEVEL");
        //HL*1**20*1~
        HL hl = loop2000A.<HL>CreateSegment(HL.class);
        hl.setHierarchicalIDNumber("1");
        hl.setHierarchicalLevelCode("20");
        hl.setHierarchicalChildCode("1");

        DocumentLoop loop2100A = loop2000A.CreateLoop("INFORMATION SOURCE NAME");

        //NM1*PR*2*ABC COMPANY*****PI*842610001~
        NM1 nm1 = loop2100A.<NM1>CreateSegment(NM1.class);
        nm1.setEntityIdentifierCode("PR");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("ABC COMPANY");
        nm1.setIdentificationCodeQualifier("PI");
        nm1.setIdentificationCode("842610001");

        ////HL*2*1*21*1~
        DocumentLoop loop2000B = transactionHeader.CreateLoop("INFORMATION RECEIVER LEVEL");
        //HL*1**20*1~
        HL hl2000B = loop2000B.<HL>CreateSegment(HL.class);
        hl2000B.setHierarchicalIDNumber("2");
        hl2000B.setHierarchicalParentIDNumber("1");
        hl2000B.setHierarchicalLevelCode("20");
        hl2000B.setHierarchicalChildCode("1");

        DocumentLoop loop2100B = loop2000B.CreateLoop("INFORMATION RECEIVER NAME");

        //NM1*1P*2*BONE AND JOINT CLINIC*****SV*2000035~
        NM1 nm1loop2100B = loop2100B.<NM1>CreateSegment(NM1.class);
        nm1loop2100B.setEntityIdentifierCode("1P");
        nm1loop2100B.setEntityTypeQualifier("2");
        nm1loop2100B.setNameLastOrOrganizationName("BONE AND JOINT CLINIC");
        nm1loop2100B.setIdentificationCodeQualifier("SV");
        nm1loop2100B.setIdentificationCode("2000035");

        DocumentLoop loop2000C = transactionHeader.CreateLoop("SUBSCRIBER LEVEL");
        //HL*3*2*22*0~
        HL hl2000C = loop2000C.<HL>CreateSegment(HL.class);
        hl2000C.setHierarchicalIDNumber("3");
        hl2000C.setHierarchicalParentIDNumber("2");
        hl2000C.setHierarchicalLevelCode("23");
        hl2000C.setHierarchicalChildCode("0");

        //TRN*1*93175-012547*9877281234~
        TRN trn = loop2000C.<TRN>CreateSegment(TRN.class);
        trn.setTraceTypeCode("1");
        trn.setReferenceIdentification1("93175-012547");
        trn.setOriginatingCompanyIdentifier("9877281234");

        DocumentLoop loop2100C = loop2000C.CreateLoop("SUBSCRIBER NAME");

        //NM1*IL*1*SMITH*ROBERT****MI*11122333301~
        NM1 nm1loop2100C = loop2100C.<NM1>CreateSegment(NM1.class);
        nm1loop2100C.setEntityIdentifierCode("1L");
        nm1loop2100C.setEntityTypeQualifier("1");
        nm1loop2100C.setNameLastOrOrganizationName("SMITH");
        nm1loop2100C.setFirstName("ROBERT");
        nm1loop2100C.setIdentificationCodeQualifier("MI");
        nm1loop2100C.setIdentificationCode("11122333301");

        //DMG*D8*19430519~
        DMG dmg = loop2100C.<DMG>CreateSegment(DMG.class);
        dmg.setDateTimePeriodFormatQualifier("D8");
        dmg.setDateTimePeriod("19430519");

        //DTP*291*D8*20060501~
        DTP dtp = loop2100C.<DTP>CreateSegment (DTP.class);
        dtp.setDateTimeQualifier("291");
        dtp.setDateTimePeriodFormatQualifier("D8");
        dtp.setDateTimePeriod("20060501");

        DocumentLoop loop2110C =   loop2100C.CreateLoop("SUBSCRIBER ELIGIBILITY OR BENEFIT INQUIRY");

        //EQ*30~
        EQ eq = loop2110C.<EQ>CreateSegment(EQ.class);
        eq.setServiceTypeCode("30");

        //Create the end of the transaction (SE segment). This must be done in a loop.
        DocumentLoop endOfTransactionLoop = transactionHeader.CreateLoop("End Of Transaction");
        SE se = endOfTransactionLoop.<SE>CreateSegment(SE.class);
        se.setTransactionSetControlNumber("1");

        //Create the end of the functional group (GE segment). This must be done in a loop.
        DocumentLoop endOfFunctionalGroupLoop = functionalHeader.CreateLoop("End Functional Group");
        GE ge = endOfFunctionalGroupLoop.<GE>CreateSegment(GE.class);
        ge.setGroupControlNumber("11234");

        //Finally create the end of the interchange loop (IEA segment). This must be done in a loop.
        DocumentLoop endInterchangeSection = interchangeHeader.CreateLoop("End Interchange");
        IEA iea = endInterchangeSection.<IEA>CreateSegment(IEA.class);
        iea.setInterchangeControlNumber("123");


        Delimiters del = new Delimiters();
        textArea.setText(doc270.ToEDIString(del));

        //Display the new segments
        this.ediFileLoader = new RDPCrystalEDILibrary.EDIFileLoader();
        ediFileLoader.setAutoDetectDelimiters(true);
        ediFileLoader.setEDIFileType(FileType.X12);
        ediFileLoader.setEDIDataString(textArea.getText());
        ediDocumentViewer.LoadDocument(ediFileLoader.Load());
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

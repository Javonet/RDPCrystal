package Create837ProfessionalEDIFile;

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
//please go to javonet to register for a free trial and get your javonet serial number at : my.javonet.com/signin?type=free 
        Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
        Activation.initializeJavonet();
        InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
	}
	
	private void InitializeComponents() {
		this.setSize(1295, 953);
		this.setTitle("Create HIPAA 5010 837 Professional EDI File");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 1275, 50);
		JLabel lblTitle = new JLabel("   Create HIPAA 5010 837 Professional EDI File");
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
		
		Button createFile = new Button("Create 837 File");
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
        // 
        // ediFileLoader
        // 
		this.ediFileLoader = new RDPCrystalEDILibrary.EDIFileLoader();
		this.ediFileLoader.setEDIFile("");
        // 
        // ediDocumentViewer1
        // 
		this.ediDocumentViewer = new RDPCrystalEDILibrary.UI.Winforms.Controls.EDIDocumentViewer();
		this.ediDocumentViewer.setBounds(insets.left+830,insets.top+30,413,635);
		content.add(this.ediDocumentViewer);

		this.add(content);
		this.setVisible(true);
	}
	
    private void btnCreateEDIFile_Click()
    {
        X125010Document doc837P = new X125010Document();

        doc837P.setAutoPlaceCorrectNumOfSegments(true);  //puts the correct number of segments in X12 SE segment
        doc837P.setAutoPlaceNumOfTransactions(true);
        doc837P.setAutoPlaceNumOfFunctionalGroups(true);
        doc837P.setPadISAElementValues(true);

        DocumentLoop interchangeHeader = doc837P.getMainSection().CreateLoop("Interchange Header");

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

        GS gs = new GS();
        functionalHeader.Add(gs);
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
        //ST*837*0021*005010X222~
        ST st = transactionHeader.<ST>CreateSegment(ST.class);
        st.setTransactionSetIdentifierCode("837");
        st.setTransactionSetControlNumber("0021");
        st.setImplementationConventionReference("005010X222");

        //BHT*0019*00*244579*20061015*1023*CH~
        BHT bht = transactionHeader.<BHT>CreateSegment(BHT.class);
        bht.setHierarchicalStructureCode("0019");
        bht.setTransactionSetPurposeCode("10");
        bht.setReferenceIdentification("244579");
        bht.setDate("20061015");
        bht.setTime("1023");
        bht.setTransactionTypeCode("CH");

        AddSubmitter(transactionHeader);
        AddReceiver(transactionHeader);
        AddBillingProvider(transactionHeader);
        AddSubscriber(transactionHeader);

        //Create the end of the transaction (SE segment). This must be done in a loop.
        DocumentLoop endOfTransactionLoop = transactionHeader.CreateLoop("End Of Transaction");
       
        //SE*42*37~
        SE se = endOfTransactionLoop.<SE>CreateSegment(SE.class);
        se.setTransactionSetControlNumber("42");

        //Create the end of the functional group (GE segment). This must be done in a loop.
        DocumentLoop endOfFunctionalGroupLoop = functionalHeader.CreateLoop("End Functional Group");
        GE ge = endOfFunctionalGroupLoop.<GE>CreateSegment(GE.class);
        ge.setGroupControlNumber("11234");


        //Finally create the end of the interchange loop (IEA segment). This must be done in a loop.
        DocumentLoop endInterchangeSection = interchangeHeader.CreateLoop("End Interchange");
        IEA iea = endInterchangeSection.<IEA>CreateSegment(IEA.class);
        iea.setInterchangeControlNumber("123");

        this.textArea.setText(doc837P.ToEDIString(new Delimiters()));

        //Display the new segments
        ediFileLoader.setAutoDetectDelimiters(true);
        ediFileLoader.setEDIFileType(FileType.X12);
        ediFileLoader.setEDIDataString(this.textArea.getText());
        this.ediDocumentViewer.LoadDocument(ediFileLoader.Load());
    }

    private void AddBillingProvider(DocumentLoop transactionHeader)
    {
        DocumentLoop loop2000A = transactionHeader.CreateLoop("2000A BILLING PROVIDER HIERARCHICAL LEVEL");

        //HL*1**20*1~
        HL hl2000C = loop2000A.<HL>CreateSegment(HL.class);
        hl2000C.setHierarchicalIDNumber("1");
        hl2000C.setHierarchicalLevelCode("20");
        hl2000C.setHierarchicalChildCode("1");

        //PRV*BI*PXC*203BF0100Y~
        PRV prv = loop2000A.<PRV>CreateSegment(PRV.class);  //BILLING PROVIDER SPECIALTY INFORMATION
        prv.setProviderCode("BI");
        prv.setReferenceIdentification("PXC");
        prv.setReferenceIdentification("203BF0100Y");

        DocumentLoop loop2010AA = loop2000A.CreateLoop("2010AA BILLING PROVIDER NAME");

        //NM1*85*2*BEN KILDARE SERVICE*****XX*9876543210~
        NM1 nm1 = loop2010AA.<NM1>CreateSegment(NM1.class); //BILLING PROVIDER NAME
        nm1.setEntityIdentifierCode("85");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("BEN KILDARE SERVICE");
        nm1.setIdentificationCodeQualifier("XX");
        nm1.setIdentificationCode("9876543210");

        //N3*234 SEAWAY ST~
        N3 n3 = loop2010AA.<N3>CreateSegment(N3.class);  //BILLING PROVIDER ADDRESS
        n3.setAddressInformation1("234 SEAWAY ST");

        //N4*MIAMI*FL*33111~
        N4 n4 = loop2010AA.<N4>CreateSegment(N4.class);  //BILLING PROVIDER LOCATION
        n4.setCityName("MIAMI");
        n4.setStateOrProvinceCode("FL");
        n4.setPostalCode("33111");

        //REF*EI*587654321~
        REF reff = loop2010AA.<REF>CreateSegment(REF.class);  //BILLING PROVIDER TAX IDENTIFICATION
        reff.setReferenceIdentificationQualifier("EI");
        reff.setReferenceIdentification("587654321");

        AddPayToProvider(loop2000A);
    }

    private void AddSubscriber(DocumentLoop transactionHeader)
    {
        DocumentLoop loop2000B = transactionHeader.CreateLoop("2000B SUBSCRIBER HIERARCHICAL LEVEL");

        //HL*2*1*22*1~
        HL hl2000C = loop2000B.<HL>CreateSegment(HL.class);
        hl2000C.setHierarchicalIDNumber("2");
        hl2000C.setHierarchicalParentIDNumber("1");
        hl2000C.setHierarchicalLevelCode("22");
        hl2000C.setHierarchicalChildCode("1");

        //SBR*P**2222-SJ******CI~
        SBR sbr = loop2000B.<SBR>CreateSegment(SBR.class);   //SUBSCRIBER INFORMATION
        sbr.setPayerResponsibilitySequenceNumberCode("P");
        sbr.setReferenceIdentification("2222-SJ");
        sbr.setClaimFilingIndicatorCode("CI");

        AddSubscriberName(loop2000B);
        AddPayerName(loop2000B);

        AddPatient(loop2000B);
    }

    private void AddPatient(DocumentLoop loop2000B)
    {
        DocumentLoop loop2000C = loop2000B.CreateLoop("2000C PATIENT HIERARCHICAL LEVEL");

        //HL*3*2*23*0~
        HL hl2000C = loop2000C.<HL>CreateSegment(HL.class);
        hl2000C.setHierarchicalIDNumber("3");
        hl2000C.setHierarchicalParentIDNumber("2");
        hl2000C.setHierarchicalLevelCode("23");
        hl2000C.setHierarchicalChildCode("0");

        //PAT*19~
        PAT pat = loop2000C.<PAT>CreateSegment(PAT.class);   //PAT PATIENT INFORMATION
        pat.setIndividualRelationshipCode("19");

        DocumentLoop loop2010CA = loop2000C.CreateLoop("2010CA PATIENT NAME");

        //NM1*QC*1*SMITH*TED~
        NM1 nm1 = loop2010CA.<NM1>CreateSegment(NM1.class); //PATIENT NAME
        nm1.setEntityIdentifierCode("QC");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("SMITH");
        nm1.setFirstName("TED");

        //N3*236 N MAIN ST~
        N3 n3 = loop2010CA.<N3>CreateSegment(N3.class);  //PATIENT ADDRESS
        n3.setAddressInformation1("236 N MAIN ST");

        //N4*MIAMI*FL*33413~
        N4 n4 = loop2010CA.<N4>CreateSegment(N4.class);  //PATIENT CITY/STATE/ZIP
        n4.setCityName("MIAMI");
        n4.setStateOrProvinceCode("FL");
        n4.setPostalCode("33413");

        //DMG*D8*19730501*M~
        DMG dmg = loop2010CA.<DMG>CreateSegment(DMG.class); //PATIENT DEMOGRAPHIC INFORMATION
        dmg.setDateTimePeriodFormatQualifier("D8");
        dmg.setDateTimePeriod("19730501");
        dmg.setGenderCode("M");

        AddClaim(loop2000C);
    }

    private static void AddClaim(DocumentLoop loop2000C)
    {
        DocumentLoop loop2300 = loop2000C.CreateLoop("2300 CLAIM INFORMATION");

        //CLM*26463774*100***11:B:1*Y*A*Y*I~
        CLM clm = loop2300.<CLM>CreateSegment(CLM.class);   // CLM CLAIM LEVEL INFORMATION
        clm.setClaimSubmitterIdentifier("26463774");
        clm.setMonetaryAmount("100");

        clm.getHealthCareServiceLocation().setFacilityCodeValue("11");  //Composite data
        clm.getHealthCareServiceLocation().setFacilityCodeQualifier("B");
        clm.getHealthCareServiceLocation().setClaimFrequencyTypeCode("1");

        clm.setYesNoConditionOrResponseCode1("Y");
        clm.setAssignmentOrPlanParticipationCode("A");
        clm.setYesNoConditionOrResponseCode2("Y");
        clm.setReleaseOfInformationCode("I");

        //REF*D9*17312345600006351~
        REF reff = loop2300.<REF>CreateSegment(REF.class); // CLAIM IDENTIFICATION NUMBER FOR CLEARING HOUSES (Added by C.H.)
        reff.setReferenceIdentificationQualifier("D9");
        reff.setReferenceIdentification("17312345600006351");

        //HI*BK:0340*BF:V7389~
        HI hi = loop2300.<HI>CreateSegment(HI.class);  //HEALTH CARE DIAGNOSIS CODES
        hi.getHealthCareCodeInformation1().setCodeListQualifierCode("BK");
        hi.getHealthCareCodeInformation1().setIndustryCode1("0340");

        hi.getHealthCareCodeInformation2().setCodeListQualifierCode("BF");
        hi.getHealthCareCodeInformation2().setIndustryCode1("V7389");

        AddServiceLine(loop2300);
    }

    private static void AddServiceLine(DocumentLoop loop2300)
    {
        for (Integer i=1; i < 3; i++)
        {
            DocumentLoop loop2400 = loop2300.CreateLoop("2400 SERVICE LINE NUMBER");

            //LX*1~
            LX lx1 = loop2400.<LX>CreateSegment(LX.class);  //SERVICE LINE COUNTER
            lx1.setAssignedNumber(i.toString());

            //SV1*HC:99213*40*UN*1***1~
            SV1 sv1 = loop2400.<SV1>CreateSegment(SV1.class);  // SV1 PROFESSIONAL SERVICE
            sv1.getCompositeMedicalProcedureIdentifier().setProductServiceIDQualifier("HC");
            sv1.getCompositeMedicalProcedureIdentifier().setProductServiceID1("99213");

            sv1.setMonetaryAmount1("40");
            sv1.setUnitOrBasisForMeasurementCode("UN");
            sv1.setQuantity("1");
            sv1.getCompositeDiagnosticCodePointer().setDiagnosisCodePointer1("1");

            //DTP*472*D8*20061003~
            DTP serviceDate = loop2400.<DTP>CreateSegment(DTP.class);  //DTP DATE - SERVICE DATE(S)
            serviceDate.setDateTimeQualifier("472");
            serviceDate.setDateTimePeriodFormatQualifier("D8");
            serviceDate.setDateTimePeriod("20061003");
        }
    }

    private static void AddSubscriberName(DocumentLoop loop2000B)
    {
        DocumentLoop loop2010BA = loop2000B.CreateLoop("2010BA SUBSCRIBER NAME");

        //NM1*IL*1*SMITH*JANE****MI*JS00111223333~
        NM1 nm1 = loop2010BA.<NM1>CreateSegment(NM1.class); //SUBSCRIBER NAME
        nm1.setEntityIdentifierCode("IL");
        nm1.setEntityTypeQualifier("1");
        nm1.setNameLastOrOrganizationName("Smith");
        nm1.setFirstName("Jane");
        nm1.setIdentificationCodeQualifier("MI");
        nm1.setIdentificationCode("JS00111223333");

        //DMG*D8*19430501*F~
        DMG dmg = loop2010BA.<DMG>CreateSegment(DMG.class); //SUBSCRIBER DEMOGRAPHIC INFORMATION
        dmg.setDateTimePeriodFormatQualifier("D8");
        dmg.setDateTimePeriod("19430501");
        dmg.setGenderCode("F");
    }

    private static void AddPayerName(DocumentLoop loop2000B)
    {
        DocumentLoop loop2010BB = loop2000B.CreateLoop("2010BB PAYER NAME");

        //NM1*PR*2*KEY INSURANCE COMPANY*****PI*999996666~
        NM1 nm1 = loop2010BB.<NM1>CreateSegment(NM1.class); //PAYER NAME
        nm1.setEntityIdentifierCode("PR");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("KEY INSURANCE COMPANY");
        nm1.setIdentificationCodeQualifier("PI");
        nm1.setIdentificationCode("999996666");

        // REF*G2*KA6663~
        REF reff = loop2010BB.<REF>CreateSegment(REF.class);  //PAYER SECONDARY ID
        reff.setReferenceIdentificationQualifier("G2");
        reff.setReferenceIdentification("KA6663");
    }

    private void AddPayToProvider(DocumentLoop billingProviderHLLevel)
    {
        DocumentLoop loop2010AB = billingProviderHLLevel.CreateLoop("2010AB PAY-TO ADDRESS NAME");

        //NM1*87*2~
        NM1 nm1 = loop2010AB.<NM1>CreateSegment(NM1.class); //PAY-TO PROVIDER NAME
        nm1.setEntityIdentifierCode("87");
        nm1.setEntityTypeQualifier("2");

        //N3*2345 OCEAN BLVD~
        N3 n3 = loop2010AB.<N3>CreateSegment(N3.class);  // PAY-TO PROVIDER ADDRESS
        n3.setAddressInformation1("2345 OCEAN BLVD");

        //N4*MIAMI*FL*33111~
        N4 n4 = loop2010AB.<N4>CreateSegment(N4.class);  // PAY-TO PROVIDER CITY
        n4.setCityName("MIAMI");
        n4.setStateOrProvinceCode("FL");
        n4.setPostalCode("33111");
    }

    private void AddSubmitter(DocumentLoop transactionHeader)
    {
        DocumentLoop submitter = transactionHeader.CreateLoop("1000A SUBMITTER NAME");

        //NM1*41*2*PREMIER BILLING SERVICE*****46*TGJ23~
        NM1 nm1 = submitter.<NM1>CreateSegment(NM1.class);
        nm1.setEntityIdentifierCode("41");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("PREMIER BILLING SERVICE");
        nm1.setIdentificationCodeQualifier("46");
        nm1.setIdentificationCode("TGJ23");

        //PER*IC*JERRY*TE*3055552222*EX*231~
        PER per = submitter.<PER>CreateSegment(PER.class);
        per.setContactFunctionCode("IC");
        per.setContactName("JERRY");
        per.setCommunicationNumberQualifier1("TE");
        per.setCommunicationNumber1("3055552222");
        per.setCommunicationNumberQualifier2("EX");
        per.setCommunicationNumber3("231");
    }

    private void AddReceiver(DocumentLoop transactionHeader)
    {
        DocumentLoop receiver = transactionHeader.CreateLoop("1000B RECEIVER NAME");

        //NM1*40*2*KEY INSURANCE COMPANY*****46*66783JJT~
        NM1 nm1 = receiver.<NM1>CreateSegment(NM1.class);
        nm1.setEntityIdentifierCode("40");
        nm1.setEntityTypeQualifier("2");
        nm1.setNameLastOrOrganizationName("KEY INSURANCE COMPANY");
        nm1.setIdentificationCodeQualifier("46");
        nm1.setIdentificationCode("66783JJT");
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

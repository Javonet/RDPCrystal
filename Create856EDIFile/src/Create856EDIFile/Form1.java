package Create856EDIFile;

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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import Common.Activation;
import RDPCrystalEDILibrary.DataSegment;
import RDPCrystalEDILibrary.EDIDocument;
import RDPCrystalEDILibrary.Loop;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.UI.Winforms.Controls.EDIDocumentViewer ediDocumentViewer;
	private RDPCrystalEDILibrary.EDIFileLoader ediFileLoader;
	private JTextArea textArea;
    private EDIDocument sampleEDI856File;
    private ShipmentNotice shipmentNotice;
	
	public Form1() {
		Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
		Activation.initializeJavonet(); //TODO: temporarily required to be removed in final version
		InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
		
		CreateShipmentNotice();
	}
	
	private void InitializeComponents() {
		this.setSize(1295, 953);
		this.setTitle("Create 4010 856 Shipment Notice");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 1275, 50);
		JLabel lblTitle = new JLabel("   Create 4010 856 Shipment Notice");
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
		
		Button createFile = new Button("Create 856 File");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+802+55,hinsets.top+10,384,97);
		createFile.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnGenerate_Click();
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
	
    private void CreateShipmentNotice()
    {
        shipmentNotice = new ShipmentNotice();
        shipmentNotice.setShipmentID("ww8VH00");
        shipmentNotice.setNoticeType(NoticeType.PickAndPack);
        shipmentNotice.setDateCreated(new Date());
        shipmentNotice.setShipmentDate(new Date());
                      
        shipmentNotice.setPackingCode("CTN25");
        shipmentNotice.setWeightingQualifier("G");
        shipmentNotice.setUnitOfMeasure("LB");
        shipmentNotice.setLadingQuantity(1);
        shipmentNotice.setWeight(1);
                      
        shipmentNotice.setCarrier(new Carrier());
        shipmentNotice.getCarrier().setRoutingSequenceCode("M");
        shipmentNotice.getCarrier().setRoutingSequenceID("UPSN");
        shipmentNotice.getCarrier().setVendorNumber("414");
        shipmentNotice.getCarrier().setProNbrReference("1141812E0116945162"); //Carrier's PRO Nbr Reference
                     
        shipmentNotice.setShipToParty(new Party());
        shipmentNotice.getShipToParty().setName("ABC Company");
        shipmentNotice.getShipToParty().setID("117107dfd2112");
        shipmentNotice.getShipToParty().setAddress("123 Maple Street");
        shipmentNotice.getShipToParty().setCity("Queens");
        shipmentNotice.getShipToParty().setState("NY");
        shipmentNotice.getShipToParty().setZipCode("11212");

        Order order = new Order();
        order.setPONumber("23434");

        Package pckg = new Package();
        pckg.setPalletNumber("114X811E0346945512");

        Item item = new Item();
        item.setPartNumber("PartNumber");
        item.setUPCNumber("021285110131");
        item.setVenderCatalogNumber("1200462");
        item.setNumberOfUnitsShipped(200);
        item.setUnitOfMeasurement("EA");

        pckg.getItems().add(item);
        order.getPackages().add(pckg);
        shipmentNotice.getOrders().add(order);
    }

    private void CreateEDIFile()
    {
        sampleEDI856File.setSegmentSeparatorString("\r\n");

        sampleEDI856File.setFileBufferSize(16);

        //Create an interchance loop. This loop will contain all other loops in the EDI structure
        Loop interchangeHeaderLoop = new Loop("Interchange Header", "Interchange Header");

        //Create an ISA segment in the interchange loop
        DataSegment isa = interchangeHeaderLoop.Loop___CreateSegment("ISA");

        //Add elements with data to the ISA segment
        isa.getElementsAsDataSegment().Add(new String[] {"00", " ", "00", " ", "ZZ", "InterchangeSenderID", "ZZ", "InterchangeReceiverID", "070303", "18:04", "U", "00401", "1", "1", "T", ":"});

        //Now create the function header loop
        Loop functionalHeaderLoop = interchangeHeaderLoop.Loop___CreateLoop("GS Functional Header Loop", "Functional Header Loop");


        //Add a GS segment to it
        DataSegment gs = functionalHeaderLoop.Loop___CreateSegment("GS");

        //Set its elements and their values
        gs.getElementsAsDataSegment().Add("SH");
        gs.getElementsAsDataSegment().Add("ApplicationSenderCode");
        gs.getElementsAsDataSegment().Add("ApplicationReceiverCode");
        gs.getElementsAsDataSegment().Add("2005");
        gs.getElementsAsDataSegment().Add("132334");
        gs.getElementsAsDataSegment().Add("1");
        gs.getElementsAsDataSegment().Add("X");
        gs.getElementsAsDataSegment().Add("004010");

        //Create a transaction header loop
        Loop transactionLoop = functionalHeaderLoop.Loop___CreateLoop("Transaction Header", "ST Transaction Header");

        //Add an ST segment to the transaction header loop
        DataSegment st = transactionLoop.Loop___CreateSegment("ST");

        //Set its elements and their values
        st.getElementsAsDataSegment().Add("856");
        st.getElementsAsDataSegment().Add("3000012");

        int hierLevel = 1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        //Add an BSN segment to the transaction header loop
        DataSegment bsn = transactionLoop.Loop___CreateSegment("BSN");
        bsn.getElementsAsDataSegment().Add(shipmentNotice.getShipmentID()); //SHIPMENT IDENTIFICATION
        bsn.getElementsAsDataSegment().Add(sdf.format(shipmentNotice.getDateCreated())); //DATE CREATED
        bsn.getElementsAsDataSegment().Add(sdf1.format(shipmentNotice.getDateCreated()));  //TIME CREATED
        bsn.getElementsAsDataSegment().Add(shipmentNotice.getNoticeType() == NoticeType.PickAndPack ? "0001" : "0002");
        //SHIPMENT LOOP
        Loop shipmentLoop = transactionLoop.Loop___CreateLoop("LOOP HL", "Begin HL Shipment Loop");

        DataSegment hl = shipmentLoop.Loop___CreateSegment("HL");
        hl.getElementsAsDataSegment().Add("" +hierLevel++); //Hierarchy level
        hl.getElementsAsDataSegment().Add("");
        hl.getElementsAsDataSegment().Add("S"); //S- Shipment loop

        //Shipment details
        DataSegment td1 = shipmentLoop.Loop___CreateSegment("TD1");
        td1.getElementsAsDataSegment().Add(shipmentNotice.getPackingCode());//Packing Code
        td1.getElementsAsDataSegment().Add(( "" + shipmentNotice.getLadingQuantity())); //lading quantity
        td1.getElementsAsDataSegment().Add("");
        td1.getElementsAsDataSegment().Add("");
        td1.getElementsAsDataSegment().Add("");
        td1.getElementsAsDataSegment().Add("" + shipmentNotice.getWeightingQualifier()); //Weighting qualifier
        td1.getElementsAsDataSegment().Add("" + shipmentNotice.getWeight()); //Weight
        td1.getElementsAsDataSegment().Add("" + shipmentNotice.getUnitOfMeasure()); //unit of measure

        //Carrier details
        DataSegment td5 = shipmentLoop.Loop___CreateSegment("TD5");
        td5.getElementsAsDataSegment().Add("" +shipmentNotice.getCarrier().getRoutingSequenceCode()); //Routing sequence qualifier
        td5.getElementsAsDataSegment().Add("2"); //ID code qualifier
        td5.getElementsAsDataSegment().Add(shipmentNotice.getCarrier().getRoutingSequenceID());  //Id code

        //Reference number:Vendor number
        DataSegment refn = shipmentLoop.Loop___CreateSegment("REF");
        refn.getElementsAsDataSegment().Add("IA"); //vendor number
        refn.getElementsAsDataSegment().Add(shipmentNotice.getCarrier().getVendorNumber()); //Id code qualifier

        //Reference numbers:Carrier's PRO Nbr Reference
        DataSegment refn2 = shipmentLoop.Loop___CreateSegment("REF");
        refn.getElementsAsDataSegment().Add("CN"); //
        refn.getElementsAsDataSegment().Add(shipmentNotice.getCarrier().getProNbrReference()); //Carrier's PRO Nbr Reference

        //Shipping Date
        DataSegment dtp = shipmentLoop.Loop___CreateSegment("DTP");
        dtp.getElementsAsDataSegment().Add("011"); //qualifier
        dtp.getElementsAsDataSegment().Add(sdf.format(shipmentNotice.getShipmentDate())); //shipment date

        //Shipment party 
        Loop shipmentParty = shipmentLoop.Loop___CreateLoop("LOOP NM1", "Shipment Party");

        //Shipment party
        DataSegment n1 = shipmentParty.Loop___CreateSegment("N1");
        n1.getElementsAsDataSegment().Add("ST");  //ST-Ship To
        n1.getElementsAsDataSegment().Add(shipmentNotice.getShipToParty().getName()); //
        n1.getElementsAsDataSegment().Add("92"); //ID
        n1.getElementsAsDataSegment().Add(shipmentNotice.getShipToParty().getID()); //ID Code

        //Shipment address 1
        DataSegment n3 = shipmentParty.Loop___CreateSegment("N3");
        n3.getElementsAsDataSegment().Add(shipmentNotice.getShipToParty().getAddress());

        //Shipment address 2
        DataSegment n4 = shipmentParty.Loop___CreateSegment("N4");
        n4.getElementsAsDataSegment().Add(shipmentNotice.getShipToParty().getCity());
        n4.getElementsAsDataSegment().Add(shipmentNotice.getShipToParty().getState());
        n4.getElementsAsDataSegment().Add(shipmentNotice.getShipToParty().getZipCode());


        Order[] orders = shipmentNotice.getOrders().toArray(new Order[] {});
        for (int i = 0; i < orders.length; i++)
        {
            int orderHierarchy = hierLevel++;

            //ORDER LOOP
            Loop orderLoop = shipmentLoop.Loop___CreateLoop("Order Loop", "Order Loop");

            DataSegment hl2 = orderLoop.Loop___CreateSegment("HL");
            hl2.getElementsAsDataSegment().Add("" + orderHierarchy); //Hierarchy level
            hl2.getElementsAsDataSegment().Add("1");
            hl2.getElementsAsDataSegment().Add("O");  //O-Order Loop

            //Purchase Order reference
            DataSegment prf = orderLoop.Loop___CreateSegment("PRF");
            prf.getElementsAsDataSegment().Add(orders[i].getPONumber()); //PO Number

            for (Package pckg : orders[i].getPackages())
            {
                int packageHierarchy = hierLevel++;
                //PACK LOOP
                Loop packLoop = orderLoop.Loop___CreateLoop("Pack Loop", "Pack Loop");

                DataSegment packHL = packLoop.Loop___CreateSegment("HL");
                packHL.getElementsAsDataSegment().Add("" +packageHierarchy);
                packHL.getElementsAsDataSegment().Add("" +orderHierarchy); //parent HL is order loop
                packHL.getElementsAsDataSegment().Add("P");//P-Pack Loop

                //Marks and Numbers
                DataSegment man = packLoop.Loop___CreateSegment("MAN");
                man.getElementsAsDataSegment().Add("W");
                man.getElementsAsDataSegment().Add(pckg.getPalletNumber());

                Item[] items = pckg.getItems().toArray(new Item[] {});
                for (int j=0; j < items.length; j++)
                {
                    //ITEM LOOP
                    Loop itemLoop = packLoop.Loop___CreateLoop("Items", "Items Loop");

                    DataSegment itemsHL = itemLoop.Loop___CreateSegment("HL");
                    itemsHL.getElementsAsDataSegment().Add("" + hierLevel++);
                    itemsHL.getElementsAsDataSegment().Add("" + packageHierarchy); //parent HL is pack loop
                    itemsHL.getElementsAsDataSegment().Add("I");  //I-Item loop

                    //Line Item detail
                    DataSegment lin = itemLoop.Loop___CreateSegment("LIN");
                    lin.getElementsAsDataSegment().Add(""); //assigned it
                    lin.getElementsAsDataSegment().Add("CB"); //product/service id. CB- Customer part number
                    lin.getElementsAsDataSegment().Add(items[j].getPartNumber());  //part number
                    lin.getElementsAsDataSegment().Add("UP");  //UPC number
                    lin.getElementsAsDataSegment().Add(items[j].getUPCNumber());
                    lin.getElementsAsDataSegment().Add("VC");
                    lin.getElementsAsDataSegment().Add(items[j].getVenderCatalogNumber());

                    //Line Item detail
                    //"SN1**1*EA~" +
                    DataSegment sn1 = itemLoop.Loop___CreateSegment("SN1");
                    sn1.getElementsAsDataSegment().Add(""); //assigned it
                    sn1.getElementsAsDataSegment().Add("" +items[j].getNumberOfUnitsShipped()); // number of units shipped
                    sn1.getElementsAsDataSegment().Add(items[j].getUnitOfMeasurement());  //units of measurement
                }
            }

        }

        //Create the end of the transaction (SE segment). This must be done in a loop.
        Loop endOfTransactionLoop = transactionLoop.Loop___CreateLoop("End Of Transaction", "SE End Of Transaction");
        DataSegment se = endOfTransactionLoop.Loop___CreateSegment("SE");
        se.getElementsAsDataSegment().Add("0");
        se.getElementsAsDataSegment().Add("Segment Count");
        se.getElementsAsDataSegment().Add("1");

        //Create the end of the functional group (GE segment). This must be done in a loop.
        Loop endOfFunctionalGroupLoop = functionalHeaderLoop.Loop___CreateLoop("GE End Functional Group", "GE End Functional Group");
        DataSegment ge = endOfFunctionalGroupLoop.Loop___CreateSegment("GE");
        ge.getElementsAsDataSegment().Add("0");
        ge.getElementsAsDataSegment().Add("1");

        //Finally create the end of the interchange loop (IEA segment). This must be done in a loop.
        Loop endInterchangeLoop = interchangeHeaderLoop.Loop___CreateLoop("End Interchange", "IEA End Interchange");
        DataSegment iea = endInterchangeLoop.Loop___CreateSegment("IEA");
        iea.getElementsAsDataSegment().Add("0");
        iea.getElementsAsDataSegment().Add("1");

        //Add the interchange loop to the EDI document
        sampleEDI856File.getLoops().Add(interchangeHeaderLoop);
    }

    private void btnGenerate_Click()
    {
        try
        {
            sampleEDI856File = new EDIDocument();

            CreateEDIFile();

            this.textArea.setText(sampleEDI856File.GenerateEDIData());

            this.ediDocumentViewer.LoadDocument(sampleEDI856File);
        }
        catch (Exception ex)
        {
        	JOptionPane.showMessageDialog(this, ex.toString());
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

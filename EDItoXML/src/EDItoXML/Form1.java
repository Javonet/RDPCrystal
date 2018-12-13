package EDItoXML;

import java.awt.Button;
import java.awt.Color;
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
import RDPCrystalEDILibrary.DataSegment;
import RDPCrystalEDILibrary.EDIDocument;
import RDPCrystalEDILibrary.Loop;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.EDIFileLoader ediFileLoader;
	private JTextArea txtEDIFile;
	private JTextField txtEDIFileLocation;
	
	public Form1() {
//please go to javonet to register for a free trial and get your javonet serial number at : my.javonet.com/signin?type=free 
        Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
        Activation.initializeJavonet();
        InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");

	}
	
	private void InitializeComponents() {
		this.setSize(870, 690);
		this.setTitle(" EDI to XML");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 870, 50);
		JLabel lblTitle = new JLabel("    EDI to XML");
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
		
		Button createFile = new Button("Generate XML EDI file");
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
	
		txtEDIFileLocation = new JTextField("SampleXMLFile.txt");
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
	

    //PRICING: http://www.rdpcrystal.com/pricing/
    private void btnCreateEDIFile_Click()
    {
        try
        {
            //Instantiate an instance of EDIDocument and set the name of the file to be created
            //We could also use Typed EDI documents as well
            EDIDocument sampleEDIFile = new EDIDocument(txtEDIFileLocation.getText());

            //Create an interchance loop. This loop will contain all other loops in the EDI structure
            Loop interchangeHeaderLoop = new Loop("Interchange Header");

            //Create an ISA segment in the interchange loop
            DataSegment isa = interchangeHeaderLoop.Loop___CreateSegment("ISA");

            //Add getElementsAsDataSegment() with data to the ISA segment
            isa.getElementsAsDataSegment().Add("00");
            isa.getElementsAsDataSegment().Add(" ");
            isa.getElementsAsDataSegment().Add("00");
            isa.getElementsAsDataSegment().Add(" ");
            isa.getElementsAsDataSegment().Add("ZZ");
            isa.getElementsAsDataSegment().Add("InterchangeSenderID");
            isa.getElementsAsDataSegment().Add("ZZ");
            isa.getElementsAsDataSegment().Add("InterchangeReceiverID");
            isa.getElementsAsDataSegment().Add("070303");
            isa.getElementsAsDataSegment().Add("18:04");
            isa.getElementsAsDataSegment().Add("U");
            isa.getElementsAsDataSegment().Add("00401");
            isa.getElementsAsDataSegment().Add("1");
            isa.getElementsAsDataSegment().Add("1");
            isa.getElementsAsDataSegment().Add("T");
            isa.getElementsAsDataSegment().Add(":");

            //Now create the function header loop
            Loop functionalHeaderLoop = interchangeHeaderLoop.Loop___CreateLoop("Functional Header Loop");

            //Add a GS segment to it
            DataSegment gs = functionalHeaderLoop.Loop___CreateSegment("GS");

            //Set its getElementsAsDataSegment() and their values
            gs.getElementsAsDataSegment().Add("HC");
            gs.getElementsAsDataSegment().Add("ApplicationSenderCode");
            gs.getElementsAsDataSegment().Add("ApplicationReceiverCode");
            gs.getElementsAsDataSegment().Add("2005");
            gs.getElementsAsDataSegment().Add("132334");
            gs.getElementsAsDataSegment().Add("1");
            gs.getElementsAsDataSegment().Add("X");
            gs.getElementsAsDataSegment().Add("004010X098A1");

            //Create a transaction header loop
            Loop transactionLoop = functionalHeaderLoop.Loop___CreateLoop("Transaction Header");

            //Add an ST segment to the transaction header loop
            DataSegment st = transactionLoop.Loop___CreateSegment("ST");

            //Set its getElementsAsDataSegment() and their values
            st.getElementsAsDataSegment().Add("837");
            st.getElementsAsDataSegment().Add("1");

            //Add an ST segment to the transaction header loop
            DataSegment bht = transactionLoop.Loop___CreateSegment("BHT");
            bht.getElementsAsDataSegment().Add("0019");
            bht.getElementsAsDataSegment().Add("00");
            bht.getElementsAsDataSegment().Add("1");
            bht.getElementsAsDataSegment().Add("20070515");
            bht.getElementsAsDataSegment().Add("094553");
            bht.getElementsAsDataSegment().Add("CH");

            //Create the end of the transaction (SE segment). This must be done in a loop.
            Loop endOfTransactionLoop = transactionLoop.Loop___CreateLoop("End Of Transaction");
            DataSegment se = endOfTransactionLoop.Loop___CreateSegment("SE");
            se.getElementsAsDataSegment().Add(DataSegment.getNumberCreated().toString(), "Segment Count");
            se.getElementsAsDataSegment().Add("1");

            //Create the end of the functional group (GE segment). This must be done in a loop.
            Loop endOfFunctionalGroupLoop = functionalHeaderLoop.Loop___CreateLoop("End Functional Group");
            DataSegment ge = endOfFunctionalGroupLoop.Loop___CreateSegment("GE");
            ge.getElementsAsDataSegment().Add("1");
            ge.getElementsAsDataSegment().Add("1");

            //Finally create the end of the interchange loop (IEA segment). This must be done in a loop.
            Loop endInterchangeLoop = interchangeHeaderLoop.Loop___CreateLoop("End Interchange");
            DataSegment iea = endInterchangeLoop.Loop___CreateSegment("IEA");
            iea.getElementsAsDataSegment().Add("1");
            iea.getElementsAsDataSegment().Add("1");

            //Add the interchange loop to the EDI document
            sampleEDIFile.getLoops().Add(interchangeHeaderLoop);

            //Create an XML version of the EDI file save
            //save it to disk
            sampleEDIFile.ToXml(txtEDIFileLocation.getText());
        
            //Show the xml string. 
            txtEDIFile.setText(ReadTextFile(txtEDIFileLocation.getText()));
        }
        catch (Exception ex)
        {
        	JOptionPane.showMessageDialog(this, ex.toString());
        }
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

package EDIFileScrubber;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import Common.Activation;
import RDPCrystalEDILibrary.EDIFileScrubber;
import RDPCrystalEDILibrary.EDILightWeightDocument;
import RDPCrystalEDILibrary.EDISource;
import RDPCrystalEDILibrary.FileType;
import RDPCrystalEDILibrary.ScrubPosition;
import RDPCrystalEDILibrary.ScrubRule;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JTextArea scrubbedTextArea;
    private RDPCrystalEDILibrary.EDIFileScrubber ediFileScrubber1;

	public Form1() throws IOException {
		Activation.setLicense("dpooran@rdpcrystal.com", "z8X4-e3RE-n9BG-r2PM-Ky6e");
		Activation.initializeJavonet(); //TODO: temporarily required to be removed in final version
		InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
		
		Form1_Load();
	}
	
	private void InitializeComponents() {
		this.setSize(1295, 953);
		this.setTitle("EDI File Scrubber");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 1275, 50);
		JLabel lblTitle = new JLabel("   EDI File Scrubber");
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
		
		Button createFile = new Button("Scrub File");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+802+55,hinsets.top+10,384,97);
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
		content.setSize(1275,103);
		content.setBounds(insets.left,insets.top+160,1275,823);
		
		insets = content.getInsets();
		
		JLabel lblEdiFile = new JLabel("EDI File");
		lblEdiFile.setBounds(insets.left+30,insets.top+30,570,20);
		lblEdiFile.setFont(new Font("Tahoma",0,18));
		content.add(lblEdiFile);
		
		textArea = new JTextArea(20,0);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		textArea.setBorder(BorderFactory.createCompoundBorder(border,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		textArea.setBorder(border);
		textArea.setFont(new Font("Microsoft Sans Serif",0,16));
		textArea.setEditable(true);
		textArea.setLineWrap(true);

		textArea.setBounds(insets.left+30,insets.top+50,570,613);
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
        // ediFileScrubber1
        // 
		this.ediFileScrubber1=new EDIFileScrubber();
        this.ediFileScrubber1.setEDIFile("");

		JLabel lblParsed = new JLabel("Scrubbed File");
		lblParsed.setBounds(insets.left+630,insets.top+30,613,20);
		lblParsed.setFont(new Font("Tahoma",0,18));
		content.add(lblParsed);
		
		this.scrubbedTextArea = new JTextArea(20,0);
		scrubbedTextArea.setBorder(BorderFactory.createCompoundBorder(border,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		scrubbedTextArea.setBorder(border);
		scrubbedTextArea.setFont(new Font("Microsoft Sans Serif",0,16));
		scrubbedTextArea.setEditable(true);
		scrubbedTextArea.setLineWrap(true);
		this.scrubbedTextArea.setBounds(insets.left+630,insets.top+50,613,615);
		content.add(this.scrubbedTextArea);

		this.add(content);
		this.setVisible(true);
	}
	
   private void Form1_Load() throws IOException
   {
	   textArea.setText(ReadTextFile("EDIFiles\\SampleEDIFile.txt"));
   }
   

   //PRICING: http://www.rdpcrystal.com/pricing/
   private void btnLoad_Click()
   {
       try
       {
           ScrubRule rule = new ScrubRule();
           rule.setSegmentName("ISA");
           rule.setReplaceCharacter('Q');
           rule.getScrubPositions().Add(new ScrubPosition(0));
           rule.getScrubPositions().Add(new ScrubPosition(1));
           rule.getScrubPositions().Add(new ScrubPosition(2));
           rule.getScrubPositions().Add(new ScrubPosition(3));

           ScrubRule rule2 = new ScrubRule();
           rule2.setSegmentName("SVC");
           rule2.setReplaceCharacter('W');
           rule2.getScrubPositions().Add(new ScrubPosition(0,0));

           ediFileScrubber1.getScrubRules().Add(rule);
           ediFileScrubber1.getScrubRules().Add(rule2);

           ediFileScrubber1.setAutoDetectDelimiters(true);
           ediFileScrubber1.setEDIFileType(FileType.X12);
           ediFileScrubber1.setEDISource(EDISource.DataString);
           ediFileScrubber1.setEDIDataString(textArea.getText());

           EDILightWeightDocument doc = ediFileScrubber1.Scrub();
           scrubbedTextArea.setText(doc.GenerateEDIData());
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
    		br.mark(1);
    		if (br.read() != 0xFEFF)
    			br.reset();
    		
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
				try {
					new Form1();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}
}

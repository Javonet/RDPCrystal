package ValidationUnit;

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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Common.Activation;
import RDPCrystalEDILibrary.GeneralEventArgs;
import RDPCrystalEDILibrary.UI.Winforms.Controls.ValidationUnit;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.UI.Winforms.Controls.ValidationUnit validationUnit1;
	
	public Form1() {
//please go to javonet to register for a free trial and get your javonet serial number at : my.javonet.com/signin?type=free 
        Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
        Activation.initializeJavonet();
        InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
		
		Form1_Load();
	}
	
	private void InitializeComponents() {
		this.setSize(1070, 950);
		this.setTitle("Validation Unit - Validating an HIPAA 5010 837 File");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 1070, 50);
		JLabel lblTitle = new JLabel("   Validation Unit - Validating an HIPAA 5010 837 File");
		lblTitle.setFont(new Font("Microsoft Sans Serif",1,18));
		lblTitle.setForeground(Color.white);
		title.add(lblTitle);
		this.add(title);
		
		JPanel header = new JPanel(null);
		Insets hinsets = header.getInsets();
		JLabel logo = new JLabel(new ImageIcon(".\\Resources\\rdplogo.png"));
		logo.setBounds(hinsets.left,hinsets.top+10,350,97);
		header.setBounds(insets.left,insets.top+50,1070,110);
		
		header.add(logo);
	
		
		this.add(header);
		
		JPanel content = new JPanel(null);		
		content.setBounds(insets.left,insets.top+160,850,640);
		
		insets = content.getInsets();
		
		
		JButton btnSourceCode = new JButton("Source Code");
		btnSourceCode.setBackground(new Color(2,119,189));
		btnSourceCode.setForeground(Color.white);
		btnSourceCode.setFont(new Font("Microsoft Sans Serif",0,16));
		btnSourceCode.setBounds(hinsets.left+652,hinsets.top+30,165,32);
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
		header.add(btnSourceCode);
		
		/* Components */
		this.validationUnit1 = new ValidationUnit();
		// 
        // validationUnit1
        // 
        this.validationUnit1.setFont(new Font("Tahoma",0,10));
        this.validationUnit1.addErrorOccurred(new RDPCrystalEDILibrary.UI.Winforms.Controls.ValidationUnit.ErrorEvent() {

			@Override
			public void Invoke(Object arg0, GeneralEventArgs arg1) {
				// TODO Auto-generated method stub
				//this.validationUnit1_ErrorOccurred(arg0, arg1);
			}
        	
        });
        this.validationUnit1.setBounds(12,166,1039,682);
		this.add(this.validationUnit1);


		this.add(content);
		this.setVisible(true);
	}
	

    //PRICING: http://www.rdpcrystal.com/pricing/
    private void Form1_Load()
    {
        validationUnit1.setAutoDetectDelimiters(true);
        validationUnit1.getDelimiters().setCompositeTerminatorCharacter(':');

        validationUnit1.setTrimString("\r\n");
        validationUnit1.setEDIFile("EDIFiles\\SampleEDIFile.txt");

        //EDI Rules files are used the EDIValidator component to validate and load EDI files.
        //This is just a sample EDI rules file. Please go to our online store for a production level rules file
        validationUnit1.setEDIRuleFile("EDIFiles\\Rules_5010_837P_005010X222A1.Rules");

        validationUnit1.setAutoDetectDelimiters(true);
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

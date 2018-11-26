package EDIFileSplitter;

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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Common.Activation;
import RDPCrystalEDILibrary.EDIFileScrubber;
import RDPCrystalEDILibrary.FileOperationCompletedEventArgs;
import RDPCrystalEDILibrary.FileOperationStatus;
import RDPCrystalEDILibrary.FileSplitLevel;
import RDPCrystalEDILibrary.FileType;
import jio.System.EventHandler;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
    private RDPCrystalEDILibrary.EDIFileScrubber ediFileScrubber1;

	public Form1() throws IOException {
		Activation.setLicense("dpooran@rdpcrystal.com", "z8X4-e3RE-n9BG-r2PM-Ky6e");
		Activation.initializeJavonet(); //TODO: temporarily required to be removed in final version
		InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");

	}
	
	private void InitializeComponents() {
		this.setSize(890, 300);
		this.setTitle("Split HIPAA EDI Files");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 1275, 50);
		JLabel lblTitle = new JLabel("   Split HIPAA EDI Files");
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
		
		Button createFile = new Button("Split File");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+402+55,hinsets.top+10,384,97);
		createFile.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCombile_Click();
			}
			
		});
		header.add(createFile);
		
		
		this.add(header);
		
		Button btnSourceCode = new Button("Source Code");
		btnSourceCode.setBackground(new Color(2,119,189));
		btnSourceCode.setForeground(Color.white);
		btnSourceCode.setFont(new Font("Microsoft Sans Serif",0,16));
		btnSourceCode.setBounds(insets.left+620+55,insets.top+183,165,32);
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

		this.setVisible(true);
	}
   

   //PRICING: http://www.rdpcrystal.com/pricing/
   private void btnCombile_Click()
   {
       RDPCrystalEDILibrary.EDIFileSplitter ediFileSplitter = new RDPCrystalEDILibrary.EDIFileSplitter();
       ediFileSplitter.setEDIFileType(FileType.X12);
       ediFileSplitter.setFileSplitLevel(FileSplitLevel.HEADER);
       ediFileSplitter.setNumberOfItemsPerFile(2);
       ediFileSplitter.setOutputFilename("MySplitFile");

       ediFileSplitter.addOnFileOperationCompleted(new EventHandler<FileOperationCompletedEventArgs>() {

		@Override
		public void Invoke(Object arg0, FileOperationCompletedEventArgs arg1) {
			// TODO Auto-generated method stub
			joiner_OnFileOperationCompleted(arg0,arg1);
		}
    	   
       });

       try
       {
           String currentDirectory = System.getProperty("user.dir");
           ediFileSplitter.Split("EDIFiles\\sample.txt", currentDirectory);

           JOptionPane.showMessageDialog(this, "Split Complete");
       }
       catch (Exception ex)
       {
           JOptionPane.showMessageDialog(this, ex.toString());
       }
   }

   private void joiner_OnFileOperationCompleted(Object sender, FileOperationCompletedEventArgs e)
   {
       if (e.getStatus() != FileOperationStatus.Success)
       {
    	   JOptionPane.showMessageDialog(this, "Error splitting file");
       }
       else if (e.getStatus() == FileOperationStatus.Success)
       {
    	   JOptionPane.showMessageDialog(this, "File Split Success");
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

package EDIFileJoiner;

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
import RDPCrystalEDILibrary.EDIFileJoiner;
import RDPCrystalEDILibrary.FileJoinLevel;
import RDPCrystalEDILibrary.FileOperationCompletedEventArgs;
import RDPCrystalEDILibrary.FileOperationStatus;
import RDPCrystalEDILibrary.FileType;
import jio.System.EventHandler;
import jio.System.Collections.Generic.List;

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
		this.setTitle("EDI File Joiner");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 1275, 50);
		JLabel lblTitle = new JLabel("   EDI File Joiner");
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
		
		Button createFile = new Button("Combine EDI files GS");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+802+55,hinsets.top+10,384,97);
		createFile.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCombineGroup_Click();
			}
			
		});
		header.add(createFile);
		
		Button btnCombile = new Button("Combine EDI files by ST ");
		btnCombile.setBackground(new Color(2,119,189));
		btnCombile.setForeground(Color.white);
		btnCombile.setFont(new Font("Microsoft Sans Serif",0,16));
		btnCombile.setBounds(hinsets.left+402+55,hinsets.top+10,384,97);
		btnCombile.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCombile_Click();
			}
			
		});
		header.add(btnCombile);
		
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
	

    //PRICING: http://www.rdpcrystal.com/pricing/
    private void btnCombile_Click()
    {
        EDIFileJoiner joiner = new EDIFileJoiner();
        joiner.setAutoDetectDelimiters(true);

        joiner.setFileJoinLevel(FileJoinLevel.HEADER);

        joiner.addOnFileOperationCompleted(new EventHandler<FileOperationCompletedEventArgs>() {

			@Override
			public void Invoke(Object sender, FileOperationCompletedEventArgs e) {
				// TODO Auto-generated method stub
				joiner_OnFileOperationCompleted(sender,e);
			}
        	
        });

        List<String> files = new List<String>(String.class);

        joiner.setEachSegmentInNewLine(true);
        joiner.setAutoDetectDelimiters(true);

        files.Add("EDIFiles/834_1.txt");
        files.Add("EDIFiles/834_2.txt");

        try
        {
            joiner.JoinAsync(files, "CombinedAll.txt",null);
        }
        catch (Exception ex)
        {
        	JOptionPane.showMessageDialog(this, ex.toString());
        }
    }

    private void btnCombineGroup_Click()
    {
        EDIFileJoiner joiner = new EDIFileJoiner();
        joiner.setAutoDetectDelimiters(true);

        joiner.setFileJoinLevel(FileJoinLevel.FUNCTIONALGROUP);

        joiner.addOnFileOperationCompleted(new EventHandler<FileOperationCompletedEventArgs>() {

			@Override
			public void Invoke(Object arg0, FileOperationCompletedEventArgs arg1) {
				// TODO Auto-generated method stub
				joiner_OnFileOperationCompleted(arg0,arg1);
			}
        	
        });

        List<String> files = new List<String>(String.class);

        joiner.setEachSegmentInNewLine(true);
        joiner.setAutoDetectDelimiters(true);

        files.Add("EDIFiles/834_1.txt");
        files.Add("EDIFiles/834_2.txt");

        try
        {
            joiner.JoinAsync(files, "CombinedAll.txt",null);
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
        	JOptionPane.showMessageDialog(this, "Error combining files");
        }
        else if (e.getStatus() == FileOperationStatus.Success)
        {
        	EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					  try {
						textArea.setText(ReadTextFile("CombinedAll.txt"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	                  //Display the new segments
	                  ediFileLoader.setAutoDetectDelimiters(true);
	                  ediFileLoader.setEDIFileType(FileType.X12);
	                  ediFileLoader.setEDIDataString(textArea.getText());
	                  ediDocumentViewer.LoadDocument(ediFileLoader.Load());
				}
        	
        	});
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

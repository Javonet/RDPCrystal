package Validate270_5010;

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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import Common.Activation;
import RDPCrystalEDILibrary.EDIDocument;
import RDPCrystalEDILibrary.EDIError;
import RDPCrystalEDILibrary.EDIRulesReader;
import RDPCrystalEDILibrary.EDIRulesReader.ErrorEvent;
import RDPCrystalEDILibrary.EDIRulesReaderEventArg;
import RDPCrystalEDILibrary.EDISource;
import RDPCrystalEDILibrary.EDISpecification;
import RDPCrystalEDILibrary.EDIValidator;
import RDPCrystalEDILibrary.EDIWarning;
import RDPCrystalEDILibrary.FileType;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.UI.Winforms.Controls.EDIDocumentViewer ediDocumentViewer;
	private RDPCrystalEDILibrary.EDIFileLoader ediFileLoader;
    private RDPCrystalEDILibrary.EDIValidator ediValidator;
    private RDPCrystalEDILibrary.EDIDocument ediDocument;
    private RDPCrystalEDILibrary.EDIRulesReader ediRuleReader;
	private JTextArea txtFile;
	private JTextArea txt999;
	private JTable lv;
	private JProgressBar pbSEF;
	private JProgressBar pb2;
	
	public Form1() {
		Activation.setLicense("dpooran@rdpcrystal.com", "z8X4-e3RE-n9BG-r2PM-Ky6e");
		Activation.initializeJavonet(); //TODO: temporarily required to be removed in final version
		InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
		
		Form1_Load();
	}
	
	private void InitializeComponents() {
		this.setSize(870, 750);
		this.setTitle("Validate 5010 270 - Eligibility, Coverage, or Benefit Inquiry");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 870, 50);
		JLabel lblTitle = new JLabel("   Validate 5010 270 - Eligibility, Coverage, or Benefit Inquiry");
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
		
		Button createFile = new Button("VALIDATE");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+582,hinsets.top+10,256,63);
		createFile.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnValidate_Click();
			}
			
		});
		header.add(createFile);
		
		this.add(header);
		
		JPanel content = new JPanel(null);		
		content.setBounds(insets.left,insets.top+160,850,640);
		
		insets = content.getInsets();
		
		JLabel lblDataToValidate = new JLabel("Data To Validate");
		lblDataToValidate.setBounds(10,10,200,20);
		lblDataToValidate.setFont(new Font("Tahoma",0,18));
		content.add(lblDataToValidate);
		
		JLabel lblLoadedData = new JLabel("Loaded Data");
		lblLoadedData.setBounds(10+425,10,200,20);
		lblLoadedData.setFont(new Font("Tahoma",0,18));
		content.add(lblLoadedData);
		
		txtFile = new JTextArea(20,0);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		txtFile.setBorder(BorderFactory.createCompoundBorder(border,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		txtFile.setBorder(border);
		txtFile.setFont(new Font("Microsoft Sans Serif",0,16));
		txtFile.setEditable(true);
		txtFile.setBounds(insets.left+10,insets.top+35,419,176);
		content.add(txtFile);
		
		JLabel lblErrorsAndWarnings = new JLabel("Errors & Warnings");
		lblErrorsAndWarnings.setBounds(10,35+176+10,200,20);
		lblErrorsAndWarnings.setFont(new Font("Tahoma",0,18));
		content.add(lblErrorsAndWarnings);
		
		JLabel lblGeneratedData = new JLabel("Generate 999 Data");
		lblGeneratedData.setBounds(10+425,35+176+10,200,20);
		lblGeneratedData.setFont(new Font("Tahoma",0,18));
		content.add(lblGeneratedData);
		
		txt999 = new JTextArea(20,0);
		Border border2 = BorderFactory.createLineBorder(Color.BLACK);
		txt999.setBorder(BorderFactory.createCompoundBorder(border2,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		txt999.setBorder(border2);
		txt999.setFont(new Font("Microsoft Sans Serif",0,16));
		txt999.setEditable(true);
		txt999.setBounds(10+425,35+176+10+25,393,176);
		content.add(txt999);

		Object[][] data = {{"","","","","","","","","",""}};
		String[] columnNames = {
			"Error/Warning #", "Line #","Snip Level",
			"SegmentPosition", "Message", "Loop",
			"Segment","Element Num.","Composite Elem.",
			"Description"};
		//lv = new JTable(data,columnNames);
		lv = new JTable(new DefaultTableModel(data,columnNames));
		lv.setFont(new Font("Microsoft Sans Serif",0,16));
		lv.setFillsViewportHeight(true);
		lv.getTableHeader().setFont(new Font("Microsoft Sans Serif",0,16));
		lv.setAutoResizeMode(0);
		TableColumn column = null;
		for (int i = 0; i < 10; i++) {
		    column = lv.getColumnModel().getColumn(i);
		    column.setPreferredWidth(100);
		}
		
		JScrollPane scrollPane = new JScrollPane(lv);
		scrollPane.setBounds(10,35+176+10+25,419,176);
		content.add(scrollPane);
		
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
		this.ediDocumentViewer.setBounds(insets.left+10+425,insets.top+35,393,176);
		content.add(this.ediDocumentViewer);
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
	

    private void Form1_Load()
    {
        try {
			txtFile.setText(ReadTextFile("EDIFiles\\sample270.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //EDI Rules files are used the EDIValidator component to validate and load EDI files.
        //This is just a sample EDI rules file. Please go to our online store for a production level rules file
        //Rules File Format http://www.rdpcrystal.com/products/edirulesformat/
        ediValidator.setEDIRulesFile("EDIFiles\\Rules_5010_270_005010X279A1.Rules");
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

    //PRICING: http://www.rdpcrystal.com/pricing/
    private void btnValidate_Click()
    {
        if (txtFile.getText() == "")
        {
        	JOptionPane.showMessageDialog(this, "Please enter EDI data");
            return;
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        pb2.setValue(0);

        DefaultTableModel dm = (DefaultTableModel)lv.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged(); 

        this.setCursor(Cursor.getDefaultCursor());

        StartEDIValidator();
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

    private void StartEDIValidator()
    {
        ediValidator.setAutoDetectDelimiters(true);
        ediValidator.setTrimString("\r\n");
        ediValidator.setEDIDataString(txtFile.getText());
        ediValidator.setEDIFileType(FileType.X12);
        ediValidator.setEDISource(EDISource.DataString);

        EDISpecification spec = EDISpecification.FindSpecificationFromString(txtFile.getText());
        System.out.println(spec.getEDIFileType());
        System.out.println(spec.getX12TransactionSetCode());
        System.out.println(spec.getX12Version());
        System.out.println(spec.getX12VersionCode());

        Thread validateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ValidateEDI();
			}
        	
        });
        validateThread.start();
    }

    private void ValidateEDI()
    {
        try
        {
            ediValidator.Validate();

            DefaultTableModel dm = (DefaultTableModel)lv.getModel();
            dm.getDataVector().removeAllElements();
            dm.fireTableDataChanged(); 

            //Display the errors and warnings
            Integer i = 1;
            
            for (EDIError error : ediValidator.getErrors())
            {
                Object[] d = new String[] { i.toString(), error.getLineNumber().toString(), error.getSnipLevel().toString(), error.getSegmentPositionInTransaction().toString(), error.getMessage().toString(), error.getLoop(), error.getSegment(), error.getElementOrdinal().toString(), error.getCompositeElementOrdinal().toString(), error.getDescription() };
                ListViewAdd(d);
                i++;
            }
            
            for (EDIWarning error : ediValidator.getWarnings())
            {
            	Object[] d = new String[] { i.toString(), error.getLineNumber().toString(), error.getSnipLevel().toString(), error.getSegmentPositionInTransaction().toString(), error.getMessage().toString(), error.getLoop(), error.getSegment(), error.getElementOrdinal().toString(), error.getCompositeElementOrdinal().toString(), "(WARNING)" + error.getDescription() };
                ListViewAdd(d);
                i++;
            }

            //Display the document visually
            InitializeViewer();

            RDPCrystalEDILibrary.Ack999Generator ack = new RDPCrystalEDILibrary.Ack999Generator();
            EDIDocument doc999 = ack.Generate999Acknowledgement(ediValidator);

            SetText(txt999, doc999.GenerateEDIData());

        }
        catch (Exception e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage());
        }

        JOptionPane.showMessageDialog(this, "Validation Complete");
    }


    private void InitializeViewer()
    {
        EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ediDocumentViewer.LoadDocument(ediValidator.getEDILightWeightDocument());
			}
        	
        });
    }

    private void SetText(JTextArea txtBox, String text)
    {
    	txtBox.setText(text);
    }

    private void IncreaseProgressBar(int percentage)
    {
        if ((pbSEF.getValue() < pbSEF.getMaximum()))
            pbSEF.setValue(percentage);
    }

    private void ListViewAdd(Object[] rowData)
    {
    	DefaultTableModel model = (DefaultTableModel) lv.getModel();
    	model.addRow(rowData);
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

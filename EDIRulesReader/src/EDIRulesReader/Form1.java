package EDIRulesReader;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import Common.Activation;
import RDPCrystalEDILibrary.DataElement;
import RDPCrystalEDILibrary.DataSegment;
import RDPCrystalEDILibrary.EDIFileLoader.EDIFileLoadingCompletedEvent;
import RDPCrystalEDILibrary.EDIRulesReader;
import RDPCrystalEDILibrary.ElementConditionalConstraint;
import RDPCrystalEDILibrary.ElementExclusionConstraint;
import RDPCrystalEDILibrary.ElementListConditionalConstraint;
import RDPCrystalEDILibrary.ElementPairConstraint;
import RDPCrystalEDILibrary.ElementRequiredConstraint;
import RDPCrystalEDILibrary.FileLoadingEventArgs;
import RDPCrystalEDILibrary.Loop;
import RDPCrystalEDILibrary.SemanticRule;

public class Form1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private RDPCrystalEDILibrary.EDIFileLoader ediFileLoader;
	private JTree tvRules;
    private JLabel lblProgress;
    private DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Root");

	public Form1() throws IOException {
//please go to javonet to register for a free trial and get your javonet serial number at : my.javonet.com/signin?type=free 
        Activation.setLicense("<your_email>", "<your_javonet_serialnumber");
        Activation.initializeJavonet();
        InitializeComponents();

		RDPCrystalEDILibrary.PackageLicense.setKey("Enter Serial Number Here");
	}
	
	private void InitializeComponents() {
		this.setSize(1295, 953);
		this.setTitle("Loading EDI Validation Rules");
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Insets insets = this.getInsets();
		
		JPanel title = new JPanel(new GridLayout());
		title.setBackground(new Color(2,119,189));
		title.setBounds(insets.left, insets.top, 1275, 50);
		JLabel lblTitle = new JLabel("   Loading EDI Validation Rules");
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
		
		Button createFile = new Button("Load Rules");
		createFile.setBackground(new Color(2,119,189));
		createFile.setForeground(Color.white);
		createFile.setFont(new Font("Microsoft Sans Serif",0,16));
		createFile.setBounds(hinsets.left+802+55,hinsets.top+10,384,97);
		createFile.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnLoadRules_Click();
			}
			
		});
		header.add(createFile);
		
		
		this.add(header);
		
		JPanel content = new JPanel(null);		
		content.setSize(1275,103);
		content.setBounds(insets.left,insets.top+160,1275,823);
		
		insets = content.getInsets();
		
		JLabel lblEdiFile = new JLabel("This demonstation program will load a sample EDI Rules file and display its structure");
		lblEdiFile.setBounds(insets.left+30,insets.top+30,870,20);
		lblEdiFile.setFont(new Font("Tahoma",0,18));
		content.add(lblEdiFile);
		
		
		JPanel groupBox3 = new JPanel(null);
		groupBox3.setBounds(10, 60, 1255, 600);
		groupBox3.setBorder(BorderFactory.createTitledBorder("EDI Rules"));
		content.add(groupBox3);
		
		tvRules = new JTree(treeNode); 
		JScrollPane treeView = new JScrollPane(tvRules);
		treeView.setBounds(20,30,1200,550);
		groupBox3.add(treeView);
		
		JLabel lblProgressName = new JLabel("Progress:");
		lblProgressName.setBounds(insets.left+10,insets.top+843,105,32);
		lblProgressName.setFont(new Font("Tahoma",0,18));
		this.add(lblProgressName);
		
		lblProgress = new JLabel("0%");
		lblProgress.setBounds(insets.left+115,insets.top+843,165,32);
		lblProgress.setFont(new Font("Tahoma",0,18));
		this.add(lblProgress);
		
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
		this.ediFileLoader.addFileLoadingCompleted(new EDIFileLoadingCompletedEvent() {
			
			@Override
			public void Invoke(Object arg0, FileLoadingEventArgs arg1) {
				// TODO Auto-generated method stub
				//ediFileLoader1_FileLoadingCompleted(arg0,arg1);
			}
		});
		this.ediFileLoader.setEDIFile("");

		this.add(content);
		this.setVisible(true);
	}
	
	
    //PRICING: http://www.rdpcrystal.com/pricing/
    private void btnLoadRules_Click()
    {
        try
        {
            //Path of the EDI rules file
            String sampleRuleFile = "EDIFiles\\Sample837.Rules";

            EDIRulesReader reader = new EDIRulesReader();

            //Subscribe to the ProgressChanged event
            reader.addProgressChanged(new EDIRulesReader.GeneralEvent() {

				@Override
				public void Invoke(Integer arg0) {
					// TODO Auto-generated method stub
					reader_ProgressChanged(arg0);
				}
            
            });

            //Set the EDI rules file
            reader.setEDIRulesFilePath(sampleRuleFile);

            reader.Parse();

            //Copy the rules from the EDI rule file to a treeview
            CreateRuleTreeStructure(reader.getSchema(), treeNode);

            tvRules.updateUI();
            //tvRules.getNodes().Add(treeNode);
        }
        catch (Exception ex)
        {
        	JOptionPane.showMessageDialog(this, ex.toString());
        }
    }

    private void reader_ProgressChanged(int progress)
    {
        lblProgress.setText(progress + "%");
    }

    private void CreateRuleTreeStructure(Loop main, DefaultMutableTreeNode parentNode)
    {
        //Set the loop requirements
        if (main.getRepetitionIndefinite())
            parentNode.setUserObject(main.getName() + " Repeat>1" + " [" + main.getDescription() + "]");
        else
            parentNode.setUserObject(main.getName() + " Repeat=" + main.getMaximumOccurrences().toString() + " [" + main.getDescription() + "]");

        //Go through and add a treenode for each segment
        for (DataSegment seg : main.getSegmentsAsLoop())
        {
        	DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
            parentNode.add(childNode);

            if (seg.getRepeatIndefinite())
                childNode.setUserObject(seg.getName() + "[" + seg.getOrdinalNumber().toString() + "] Repeat=>1 Usage=" + seg.getUsage().toString() + " [" + seg.getDescription() + "]");
            else
                childNode.setUserObject(seg.getName() + "[" + seg.getOrdinalNumber().toString() + "] Repeat=" + seg.getMaximumOccurrences() + " Usage=" + seg.getUsage().toString() + " [" + seg.getDescription() + "]");

            int i = 1;

            String fs = "%s  %s  %s  %s  %s  %s";

            for (DataElement element : seg.getElementsAsDataSegment())
            {
            	DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode();

                childNode.add(elemNode);
                //TODO: make i.tostring(d2)
                elemNode.setUserObject(String.format(fs, 
                		"[" + seg.getName() + i + "]", 
                		element.getElementNumber().toString(), 
                		"Min:" + element.getMinimumLength().toString(), 
                		"Max: " + element.getMaximumLength().toString(), 
                		"Data Type=" + element.getDataType().toString(),
                		"Usage=" + element.getUsage().toString()));

                if (element.getComposite())
                {
                    elemNode.setUserObject(" Composite");
                    int j = 1;
                    for (DataElement c : element.getElementsAsDataElement())
                    {
                    	DefaultMutableTreeNode comNode = new DefaultMutableTreeNode();

                        elemNode.add(comNode);
                        comNode.setUserObject(String.format(fs, "[" + j + "]", c.getElementNumber().toString(), "Min:" + c.getMinimumLength().toString(), "Max: " + c.getMaximumLength().toString(), "Data Type=" + c.getDataType().toString(), "Usage=" + c.getUsage().toString()));

                        if (c.HasAcceptedValues())
                        {
                        	DefaultMutableTreeNode cvaluesNode = new DefaultMutableTreeNode();

                            comNode.add(cvaluesNode);
                            cvaluesNode.setUserObject("Accepted Values");

                            for (String value : c.getAcceptedValues())
                            {
                            	DefaultMutableTreeNode codeNode = new DefaultMutableTreeNode();
                                cvaluesNode.add(codeNode);

                                String value1 = value;

                                if (c.getCodes() != null && c.getCodes().ContainsKey(value1))
                                    value1 = value1 + " : " + c.getCodes().get_ItemProperty(value1,String.class);

                                codeNode.setUserObject(value1);
                            }
                        }

                        j++;
                    }
                }

                if (element.HasAcceptedValues())
                {
                	DefaultMutableTreeNode valuesNode = new DefaultMutableTreeNode();
                    elemNode.add(valuesNode);
                    valuesNode.setUserObject("Accepted Values");

                    for (String value : element.getAcceptedValues())
                    {
                    	DefaultMutableTreeNode codeNode = new DefaultMutableTreeNode();
                        valuesNode.add(codeNode);

                        String value1 = value;

                        if (element.getCodes() != null && element.getCodes().ContainsKey(value1))
                            value1 = value1 + " : " + element.getCodes().get_ItemProperty(value1,String.class);
                        else
                        {
                            if (element.getCodeList() != null && element.getCodeList().getCodes().ContainsKey(value1))
                            {
                                value1 = value1 + " : " + element.getCodeList().getCodes().get_ItemProperty(value1,String.class).getDescription();

                            }
                        }

                        codeNode.setUserObject(value1);
                    }
                }

                i++;
            }

            //Check if the segment has any element pair constraints
            if (seg.HasElementPairConstraints())
            {
            	DefaultMutableTreeNode constNode = new DefaultMutableTreeNode();

                childNode.add(constNode);
                constNode.setUserObject("Constraints");

                for (ElementPairConstraint p : seg.getElementPairConstraints())
                {
                	DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode();

                    constNode.add(elemNode);
                    elemNode.setUserObject("Pair Contraints:");

                    String res="";
                    for (int g : p.getElementOrdinals())
                    {
                        res += g + ",";
                    }
                    elemNode.setUserObject(res);
                }

                for (ElementConditionalConstraint c : seg.getElementConditionalConstraints())
                {
                	DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode();

                    constNode.add(elemNode);
                    elemNode.setUserObject("Conditional Contraints:");

                    String res="";
                    for (int g : c.getElementOrdinals())
                    {
                       res  += g + ",";
                    }
                    elemNode.setUserObject(res);
                }

                for (ElementRequiredConstraint c : seg.getElementRequiredConstraint())
                {
                	DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode();

                    constNode.add(elemNode);
                    elemNode.setUserObject("Required Contraints: At least one element required:");

                    String res="";
                    for (int g : c.getElementOrdinals())
                    {
                    	res+= g + ",";
                    }
                    elemNode.setUserObject(res);
                }

                for (ElementListConditionalConstraint c : seg.getElementListConditionConstraints())
                {
                	DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode();

                    constNode.add(elemNode);
                    elemNode.setUserObject("Required List Contraints: ");

                    String res="";
                    for (int g : c.getElementOrdinals())
                    {
                    	res += g + ",";
                    }
                    elemNode.setUserObject(res);
                }

                for (ElementExclusionConstraint c : seg.getElementExclusionConstraints())
                {
                	DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode();

                    constNode.add(elemNode);
                    elemNode.setUserObject("Exclusion Contraints: ");

                    String res="";
                    for (int g : c.getElementOrdinals())
                    {
                    	res += g + ",";
                    }
                    elemNode.setUserObject(res);
                }
            }

            //Check to see if the segment has any rules
            for (SemanticRule rule : seg.getRules())
            {
            	DefaultMutableTreeNode rulesNode = new DefaultMutableTreeNode();
                childNode.add(rulesNode);
                rulesNode.setUserObject("Rule=" + rule.ToString());
            }
        }

        //Process all child loops of the main loop
        for (Loop ls : main.getLoopsAsLoop())
        {
        	DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();

            parentNode.add(childNode);

            CreateRuleTreeStructure(ls, childNode);
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

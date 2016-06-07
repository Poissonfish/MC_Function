import org.rosuda.JRI.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.MigLayout;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class MC_Function {
	public static void main(String[] args){        
		JFrame main = new FrameLayout();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setTitle("M.C.Lab");		
		main.pack();
		main.show();
	}
}


class FrameLayout extends JFrame implements ActionListener{
	int loft = 2;
	int gs = 1;
	
	String Path ="";
	JButton start = new JButton("START");
	JTextField wd = new JTextField(20);

	JTextField folder = new JTextField(5);
	JTextField pf = new JTextField(20);
	JTextField pr = new JTextField(20);
	JTextField npf = new JTextField(3);
	JTextField npr = new JTextField(3);
	JTextField lp = new JTextField(20);
	JTextField us = new JTextField(10);
	JTextField pw = new JTextField(10);
	JTextField sc = new JTextField(20);
	JRadioButton buttononline = new JRadioButton("Online(FTP)");
	JRadioButton buttonlocal = new JRadioButton("Local");
	JCheckBox genesearch = new JCheckBox("BLAST sequences");
	JLabel WD = new JLabel("Working Directory");
	JLabel FD = new JLabel("Folder Name");
	JLabel PF = new JLabel("Forward Sequence");
	JLabel PR = new JLabel("Reverse Sequence");
	JLabel NPF = new JLabel("Forward Suffix");
	JLabel NPR = new JLabel("Reverse suffix");
	JLabel FP = new JLabel("Local File Path");
	JLabel US = new JLabel("Username");
	JLabel PW = new JLabel("Password");
	JLabel FT = new JLabel("Address URL");
	
	JButton browse = new JButton("Browse");
	JFileChooser choose = new JFileChooser();
	JButton browse2 = new JButton("Browse");
	JFileChooser choose2 = new JFileChooser();
	int value;
	int value2;
	
	Rengine r = new Rengine(new String[]{"--no-save"}, false, new TextConsole2());

	JTextArea textArea;
    static final String NEWLINE = System.getProperty("line.separator");
    String con;
    PrintStream out ;
    
	public void submit() {	
		r.eval("print('start')");
		String desdir = wd.getText();
		String fold = folder.getText();
		String primer_f = pf.getText();
		String primer_r = pr.getText();
		String name_primer_f = npf.getText();
		String name_primer_r = npr.getText();
		String local_path = lp.getText();
		String source = sc.getText();
		String uss = us.getText();
		String pdd = pw.getText();
		
		r.eval("folder='"+fold+"'");
		r.eval("primer_f='"+primer_f+"'");
		r.eval("primer_r='"+primer_r+"'");
		r.eval("name_primer_f='"+name_primer_f+"'");
		r.eval("name_primer_r='"+name_primer_r+"'");
		r.eval("local_path='"+local_path+"'");
		r.eval("source='"+source+"'");	
		r.eval("username='"+uss+"'");		
		r.eval("password='"+pdd+"'");
		r.eval("desdir='"+desdir+"'");
		
		if(loft==1){
			r.eval("local=FALSE");
		}else{
			r.eval("local=TRUE");
		}
		r.eval("setwd(desdir)");
		r.eval("time=proc.time()[3]");
		r.eval("source('./RScripts/Library.R')");
	}
	public void submit2(){
		r.eval("print('Creating')");
		output("Creating Folders...");
		System.out.println("Creating");
		r.eval("source('./RScripts/CreatingFolders.R')");
		output("Done!");
	}
	
	public void submit3(){
		newoutput("Primer Analyzing...");
		System.out.println("Primer");
		r.eval("source('./RScripts/PrimerAnalyzing.R')");
		output("Done!");
	}
	public void submit4(){
		if(loft==1){
			System.out.println("no");
			newoutput("File Downloading...");
			r.eval("source('./RScripts/FileDownloading.R')");
			output("Done!");
		}
	}
	public void submit5(){
		newoutput("Renaming...");
		System.out.println("Renaming");
		r.eval("setwd(desdir)");
		r.eval("source('./RScripts/Renaming.R')");
		output("Done!");
	}
	public void submit6(){
		newoutput("Vector Screening...");
		System.out.println("Vector");
		r.eval("source('../../../RScripts/VectorScreening.R')");
		output("Done!");
	}
	public void submit7(){
		newoutput("Candidate Sequences Evaluating...");
		System.out.println("Candidate");
		r.eval("source('../../../RScripts/CandidateSequence.R')");
		output("Done!");
	}
	public void submit8(){
		newoutput("Sequences Alignment...");
		System.out.println("Sequences");		
		r.eval("source('../../../RScripts/SequenceAlignment.R')");
		output("Done!");
	}
	/*
    public void tew(){
		System.out.println("Rtew");	
		r.eval("print('tew')");
    }*/
	public void submit9(){
	//	r.eval("Sys.sleep(10)");
		if(gs==-1){
			newoutput("Fetching sequence information....");
			System.out.println("Fetching");				
			r.eval("nt_search=TRUE");
			r.eval("source('../../..//RScripts/BLAST.R')");
			output("Done!");
		}
	}
	public void submit10(){
		newoutput("Exporting Summary Information...");
		System.out.println("Summary");				
		r.eval("source('../../..//RScripts/Summary.R')");	
		output("Done!");
		r.eval("t=proc.time()[3]-time");
		
		r.eval("hr=t%/%3600");
		r.eval("hrre=t%%3600");
		r.eval("min=hrre%/%60");
		r.eval("sec=hrre%%60");
		REXP time=r.eval("paste0('It tooks ', hr, ' hour ', min, ' minute ', sec, ' second to complete.' )");
		String newtime=((REXP)time).asString();		
		output(newtime);
		
	}	


	public FrameLayout(){
		
		textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
		System.setOut(new PrintStream(new RConsoleOutputStream(r, 0)));
		System.setErr(new PrintStream(new RConsoleOutputStream(r,1 )));
		
		wd.setText("/home/mclab/workspace/M.C.Function");
		sc.setText("ftp://140.109.56.5/");
		us.setText("rm208");
		pw.setText("167cm");
	
		start.setFont(new Font("Arial", Font.BOLD, 40));
		buttonlocal.setSelected(true);
		us.setEnabled(false);
		pw.setEnabled(false);
		sc.setEnabled(false);
		ButtonGroup ftplocal = new ButtonGroup();
		ftplocal.add(buttononline);
		ftplocal.add(buttonlocal);
					
		start.addActionListener(this);
		browse.addActionListener(this);
		browse2.addActionListener(this);
		buttononline.addActionListener(this);
		buttonlocal.addActionListener(this);
		genesearch.addActionListener(this);		
		
		JPanel ruPanel = new JPanel(new BorderLayout());
		ruPanel.add(start, BorderLayout.CENTER);
		ruPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Run"));
		
		JPanel wdPanel = new JPanel(new MigLayout("fillx"));
		wdPanel.add(WD,"cell 0 0");
		wdPanel.add(wd,"cell 0 1");
		wdPanel.add(browse,"cell 1 1");
		wdPanel.add(FD,"cell 0 2");
		wdPanel.add(folder,"cell 0 3");
		wdPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Destination"));	
		
		JPanel srPanel= new JPanel(new MigLayout("fillx"));
		srPanel.add(buttononline, "cell 0 0");
		srPanel.add(FT, "cell 1 0");
		srPanel.add(sc, "cell 1 1");
		srPanel.add(US, "cell 2 0");
		srPanel.add(us, "cell 3 0");	
		srPanel.add(PW, "cell 2 1");
		srPanel.add(pw, "cell 3 1");
			
		srPanel.add(buttonlocal, "cell 0 2");
		srPanel.add(FP, "cell 1 2");
		srPanel.add(lp, "cell 1 4");
		srPanel.add(browse2, "cell 2 4");
		
		srPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Data Source"));
		
		JPanel prPanel= new JPanel(new MigLayout("fill"));
		prPanel.add(PF, "cell 0 0");
		prPanel.add(pf, "cell 0 1");
		prPanel.add(NPF, "cell 1 0");
		prPanel.add(npf, "cell 1 1");
		prPanel.add(PR, "cell 0 2");
		prPanel.add(pr, "cell 0 3");
		prPanel.add(NPR, "cell 1 2");
		prPanel.add(npr, "cell 1 3");
		prPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Primers Information"));

		JPanel gePanel= new JPanel(new MigLayout("fillx"));
		gePanel.add(genesearch);
		gePanel.setBorder(new TitledBorder(new EtchedBorder(),
				"NCBI BLAST"));

		JPanel mainPanel= new JPanel(new MigLayout("fill","[grow][]","[][grow][]"));
		mainPanel.setPreferredSize(new Dimension(590,550));
	/*
		scrollPane.setPreferredSize(new Dimension(590, 200));
		*/
		mainPanel.add(srPanel,"dock north");		
		mainPanel.add(wdPanel,"cell 0 0, grow");		
		mainPanel.add(prPanel,"cell 0 1, grow");
		mainPanel.add(ruPanel,"cell 1 0 1 3, grow");			
		mainPanel.add(gePanel,"cell 0 2, grow");	
		mainPanel.add(scrollPane,"dock south");
		
		

		/*
        MessageConsole mc = new MessageConsole(textArea);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(100);
		*/
		this.setContentPane(mainPanel);	

		
	}
	
	public void actionPerformed(ActionEvent ae){
	      Object source = ae.getSource();	
	      if (source == start){
	    	  /*
	        submit();
			submit2();
			submit3();
			submit4();
			submit5();
			submit6();
			submit7();
			submit8();
			*/
	    	  
	    	tew();
			submit9();
			submit10();

	      }else if (source == buttononline){
	    	loft=1;  
	    	lp.setEnabled(false);
	    	us.setEnabled(true);
			pw.setEnabled(true);
			sc.setEnabled(true);
	      }else if (source == buttonlocal){
	        loft=2;
	        lp.setEnabled(true);
	    	us.setEnabled(false);
			pw.setEnabled(false);
			sc.setEnabled(false);
	      }else if (source == genesearch){
	    	gs=gs*(-1);  
	      }else if (source == browse){
	    	choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    value= choose.showOpenDialog(null);
		    if (value == JFileChooser.APPROVE_OPTION){
		        File selectedfile = choose.getSelectedFile();
		    	wd.setText(selectedfile.getAbsolutePath());
		   	};
	      }else if (source == browse2){
		   	choose2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			value2= choose2.showOpenDialog(null);
			if (value2 == JFileChooser.APPROVE_OPTION){
				File selectedfile = choose2.getSelectedFile();
			    lp.setText(selectedfile.getAbsolutePath());
			};
		 }
	 }
	
    void newoutput(String eventDescription) {
        textArea.append(NEWLINE+
        				eventDescription);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    void output(String eventDescription) {
        textArea.append(eventDescription);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    
}





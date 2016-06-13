import org.rosuda.JRI.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;
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
	JTextField wd = new JTextField(30);

	JTextField folder = new JTextField(5);
	JTextField pf = new JTextField(30);
	JTextField pr = new JTextField(30);
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
	JButton load = new JButton("Connect to DateBase");
	int value;
	int value2;
	

	JTextArea textArea;
    static final String NEWLINE = System.getProperty("line.separator");
    String con;
    PrintStream out ;
    
    Rengine r;
    
    static class LoggingConsole implements RMainLoopCallbacks {
        private JTextArea textarea;

        LoggingConsole(JTextArea textarea) {
        	this.textarea= textarea;
        }

        public void rWriteConsole(Rengine re, String text, int oType) {
        	textarea.append(text+NEWLINE);
        	textarea.setCaretPosition(textarea.getDocument().getLength());
        }

        public void rBusy(Rengine re, int which) {
            System.out.println("rBusy("+which+")");
        }
        

        public void rShowMessage(Rengine re, String message) {
        	textarea.append(message+NEWLINE);
        	textarea.setCaretPosition(textarea.getDocument().getLength());
        }

        public String rReadConsole(Rengine re, String prompt, int addToHistory) {
            return null;
        }

        public String rChooseFile(Rengine re, int newFile) {
            return null;
        }

        public void rFlushConsole(Rengine re) {
        }

        public void rLoadHistory(Rengine re, String filename) {
        }

        public void rSaveHistory(Rengine re, String filename) {
        }
    }


	public void submit() {	
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
		r.eval("source('./RScripts/Library.R')");
		r.eval("time=proc.time()[3]");
		
		r.eval("ha= tryCatch( {print('hhhaaa')}, finally = {print('finallyyyyy')} )");
    	REXP haa= r.eval("ha%>%as.character()");
    	String haas=((REXP)haa).asString();
    	r.eval("print('fi')");
    
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
	public void testf(){
		r.eval("Sys.sleep(5)");
	}
	public void submit9(){
	//	
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
        /*
		System.setOut(new PrintStream(new RConsoleOutputStream(r, 0)));
		System.setErr(new PrintStream(new RConsoleOutputStream(r,1 )));
		*/
		wd.setText("/home/mclab/workspace/M.C.Function");
		sc.setText("ftp://140.109.56.5/");
		us.setText("rm208");
		pw.setText("167cm");
	
		start.setFont(new Font("Arial", Font.BOLD, 40));
		buttonlocal.setSelected(true);
		us.setEnabled(false);
		pw.setEnabled(false);
		sc.setEnabled(false);
		genesearch.setEnabled(false);
		ButtonGroup ftplocal = new ButtonGroup();
		ftplocal.add(buttononline);
		ftplocal.add(buttonlocal);
					
		start.addActionListener(this);
		browse.addActionListener(this);
		browse2.addActionListener(this);
		buttononline.addActionListener(this);
		buttonlocal.addActionListener(this);
		genesearch.addActionListener(this);		
		load.addActionListener(this);
		
		JPanel ruPanel = new JPanel(new MigLayout("fillx"));
		ruPanel.add(start, "alignx c");
		ruPanel.setBorder(new TitledBorder(new EtchedBorder(), ""));
		
		JPanel wdPanel = new JPanel(new MigLayout());
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
		
		srPanel.setBorder(new TitledBorder(new EtchedBorder(), "Data Source"));
		
		JPanel prPanel= new JPanel(new MigLayout("fill"));
		prPanel.add(PF, "cell 0 0");
		prPanel.add(pf, "cell 0 1");
		prPanel.add(NPF, "cell 1 0");
		prPanel.add(npf, "cell 1 1");
		prPanel.add(PR, "cell 0 2");
		prPanel.add(pr, "cell 0 3");
		prPanel.add(NPR, "cell 1 2");
		prPanel.add(npr, "cell 1 3");
		prPanel.setBorder(new TitledBorder(new EtchedBorder(), "Primers Information"));

		JPanel gePanel= new JPanel(new MigLayout("filly"));
		gePanel.add(load,"wrap");
		gePanel.add(genesearch,"aligny c");
		gePanel.setBorder(new TitledBorder(new EtchedBorder(), "NCBI BLAST"));

		JPanel mainPanel= new JPanel(new MigLayout("fill","[grow][]","[][grow][]"));
		mainPanel.setPreferredSize(new Dimension(1100,475));
	
		scrollPane.setPreferredSize(new Dimension(400, 0));
		
		mainPanel.add(wdPanel,"cell 0 0 2 1, grow");		
		mainPanel.add(prPanel,"cell 0 1, grow");
//		mainPanel.add(ruPanel,"cell 1 0 1 3, grow");			
			
		mainPanel.add(gePanel,"cell 1 1, grow");	
		mainPanel.add(scrollPane,"dock east");
		mainPanel.add(ruPanel,"dock south");
		mainPanel.add(srPanel,"dock north");		
		/*
        MessageConsole mc = new MessageConsole(textArea);
        mc.redirectOut();
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(100);
		*/
		this.setContentPane(mainPanel);	
		r = new Rengine(new String[]{"--no-save"}, false, new LoggingConsole(textArea));

		r.eval("library(annotate)");
	  	r.eval("library(Biostrings)");
	  	r.eval("library(rBLAST)");
	  	r.eval("library(rMSA)");
	  	r.eval("library(devtools)");
	  	r.eval("library(magrittr)");
	  	r.eval("library(seqinr)");
	  	r.eval("library(ape)");
	  	r.eval("library(data.table)");
	  	r.eval("library(lubridate)");
	  	r.eval("library(RCurl)");
	  	r.eval("library(magrittr)");
	  	r.eval("library(R.utils)");
	  	r.eval("library(downloader)");
	  	r.eval("library(ggplot2)");
	  	r.eval("library(gridExtra)");
	  	r.eval("library(plyr)");
	  	r.eval("library(taxize)");
	  	r.eval("library(rentrez)");
	  	r.eval("print('fffffi')");
	}
	
	public void actionPerformed(ActionEvent ae){
	      Object source = ae.getSource();	
	      if (source == start){
	    
	        submit();
	        testf();
	      /*
			submit2();
			submit3();
			submit4();
			submit5();
			submit6();
			submit7();
			submit8();   
			submit9();
			submit10();
*/
	      }else if (source == load){
			
			String work=wd.getText();
			r.eval("wdtest='"+work+"'");
	    	r.eval("handle1= tryCatch( {setwd(wdtest)}, error=function(e){print(e)} )");
	    	REXP handle1r= r.eval("handle1%>%as.character()");
	    	String handle1rs=((REXP)handle1r).asString();
	    	if(handle1rs.indexOf("Error")>=0){
		  		 newoutput(handle1rs);
		  		genesearch.setEnabled(false);
	    	}else{
	    		r.eval("print(getwd())");
		    	r.eval("handle2= tryCatch( {t=blast(db='./db/nt') %>% capture.output()}, error=function(e){print(e)} )");
		    	REXP handle2r= r.eval("handle2%>%as.character()");
		  	  	String handle2rs=((REXP)handle2r).asString();
		  	  	
		  	  	if(handle2rs.indexOf("Error")>=0){
			  		 newoutput(handle2rs);
			  		genesearch.setEnabled(false);
			  	}else{
				  	 r.eval("t2=t[4]%>% gsub('\\t','',.) %>%strsplit('; ')");
				  	 REXP db1=r.eval("t[2]%>% strsplit(': ')%>%(function(x){x[[1]][2]})");
				  	 REXP db2=r.eval("t[3]%>% strsplit(': ')%>%(function(x){x[[1]][2]})");
				  	 REXP db3=r.eval("t2[[1]][1]");
				  	 REXP db4=r.eval("t2[[1]][2]");
				  	 REXP db5=r.eval("t[6]%>% gsub('\\t','',.)%>%strsplit('Long')%>%(function(x){x[[1]][1]})%>%gsub('Date: ','',.)");
				  	  	
				  	 String db1s=((REXP)db1).asString();
				  	 String db2s=((REXP)db2).asString();
				  	 String db3s=((REXP)db3).asString();
				  	 String db4s=((REXP)db4).asString();
				  	 String db5s=((REXP)db5).asString();
				  	 newoutput("Location:");
				  	 newoutput("   "+db1s);
				  	 newoutput("");
				  	 newoutput("Database:");
				  	 newoutput("   "+db2s);
				  	 newoutput("   "+db3s);
				  	 newoutput("   "+db4s);
				  	 newoutput("");
				  	 newoutput("Date:");
				  	 newoutput("   "+db5s);
				  	 newoutput("");
				  	 newoutput("");
				  	 newoutput("The operation environment is ready");  
				  	 genesearch.setEnabled(true);
			  	}	
	    	}
	    	
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
	
    void output(String eventDescription) {
        textArea.append(eventDescription);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    void newoutput(String eventDescription) {
        textArea.append(eventDescription+NEWLINE);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    public void rWriteConsole(Rengine r, String text, int oType) {
    	textArea.append(text);
    }
    
    
}





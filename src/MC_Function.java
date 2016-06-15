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
import java.util.prefs.*;
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
		main.setResizable(false);
		main.pack();
		main.show();
	}
}

class settingframe extends JFrame implements ActionListener{
	JButton ok= new JButton("OK");
	JButton cancel= new JButton("Cancel");
	JButton reset= new JButton("Reset");

	JLabel name= new JLabel("Names");
	JLabel sequence= new JLabel("Sequences");
	JTextField[] ps= new JTextField[15];
	JTextField[] pn= new JTextField[15];
	JPanel panel;
	String[] sn= new String[15];
	String[] sp= new String[15];
	JComboBox pf;
	JComboBox pr;
	Preferences prefsdemo;
	
	public settingframe(String[] stringname, String[] stringprimer, JComboBox f, JComboBox r){
		this.sn=stringname;
		this.sp=stringprimer;
		this.pf=f;
		this.pr=r;
		
		panel= new JPanel(new MigLayout());
		panel.add(name);
		panel.add(sequence,"wrap");
		for (int i=0; i<15; i++){
			ps[i]= new JTextField(20);
			pn[i]= new JTextField(3);
			panel.add(pn[i]);
			panel.add(ps[i],"span 2, wrap");
		}
		panel.add(ok);
		panel.add(cancel);
		panel.add(reset);
		
		ok.addActionListener(this);
		cancel.addActionListener(this);
		reset.addActionListener(this);
		
		prefsdemo = Preferences.userRoot().node("/com/sunway/spc");  
	    for (int i = 0; i < 15; i++) {               
            pn[i].setText(prefsdemo.get("name"+i, " "));
            ps[i].setText(prefsdemo.get(sn[i], " "));
        }  
        
		this.setContentPane(panel);
		this.pack();
		this.setTitle("Primer Setting Panel");
		this.show();
	}
	public void actionPerformed(ActionEvent ae){
	      Object source = ae.getSource();	
	      if (source == ok){
	    	  for (int i=0; i<15; i++){
	    		sp[i]=ps[i].getText();
	    		sn[i]=pn[i].getText();
	    		prefsdemo.put("name"+i, sn[i]);  
	            prefsdemo.put(sn[i], sp[i]);
	            if(i!=14){pf.removeItemAt(0);
	            		  pr.removeItemAt(0);
	            		  }
	            pf.addItem(sn[i]);
	            pr.addItem(sn[i]);
	    	  } 
	    	  pf.removeItemAt(0);
    		  pr.removeItemAt(0);
	 
	    	  this.dispose();
	    	  
	      }else if (source == cancel){
	    	  this.dispose();
	    	  
	      }else if (source == reset){
	    	  for (int i=0; i<15; i++){
		    		ps[i].setText("");
		    		pn[i].setText("C"+(i+1));
		    	  }  
	      }
	}
	
}

class FrameLayout extends JFrame implements ActionListener{
	int loft = 2;
	int gs = 1;
	
	Preferences prefsdemo = Preferences.userRoot().node("/com/sunway/spc");  
    
	String Path ="";
	JButton start = new JButton("START");
	JTextField wd = new JTextField(25);

	String[] sn= new String[15];
	String[] sp= new String[15];
	JComboBox pf, pr;
	JButton setpri = new JButton("Primer Setting");
	JButton showpri = new JButton("Show Primer");
	
	//JTextField pf = new JTextField(15);
	//JTextField pr = new JTextField(15);
	JTextField npf = new JTextField(3);
	JTextField npr = new JTextField(3);
		
	JTextField folder = new JTextField(5);
	JTextField lp = new JTextField(20);
	JTextField us = new JTextField(10);
	JTextField pw = new JTextField(10);
	JTextField sc = new JTextField(20);
	JRadioButton buttononline = new JRadioButton("Online(FTP)");
	JRadioButton buttonlocal = new JRadioButton("Local");
	JCheckBox genesearch = new JCheckBox("BLAST sequences");
	JLabel WD = new JLabel("Working Directory");
	JLabel FD = new JLabel("Folder Name");
	JLabel PF = new JLabel("Primer.1");
	JLabel PR = new JLabel("Primer.2");
	JLabel NPF = new JLabel("Forward");
	JLabel NPR = new JLabel("Reverse");
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
    Boolean library=false;
	
	public FrameLayout(){
		//Combolist
		for (int i = 0; i < 15; i++) {               
	        sn[i]=prefsdemo.get("name"+i, " ");
	        sp[i]=prefsdemo.get(sn[i], " ");
	    }  
		pf = new JComboBox(sn);
		pf.addActionListener(this);   
		pr = new JComboBox(sn);
		pr.addActionListener(this); 
		//
		
		textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
		wd.setText("/home/mclab/workspace/M.C.Function");
		sc.setText("ftp://140.109.56.5/");
		us.setText("rm208");
		pw.setText("167cm");
	
		start.setFont(new Font("Ariashowpril", Font.BOLD, 40));
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
		setpri.addActionListener(this);
		showpri.addActionListener(this);
		
		JPanel ruPanel = new JPanel(new MigLayout("fill"));
		ruPanel.add(start, "alignx c");
		//ruPanel.setBorder(new TitledBorder(new EtchedBorder(), ""));
		
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
		srPanel.add(sc, "cell 2 0 2 1");
		srPanel.add(US, "cell 1 1");
		srPanel.add(us, "cell 2 1");	
		srPanel.add(PW, "cell 1 2");
		srPanel.add(pw, "cell 2 2");
		
		srPanel.add(buttonlocal, "cell 0 3");
		//srPanel.add(FP, "cell 1 3");
		srPanel.add(lp, "cell 1 3 2 1");
		srPanel.add(browse2, "cell 3 3");
		srPanel.setBorder(new TitledBorder(new EtchedBorder(), "Data Source"));
		
		JPanel sfPanel= new JPanel(new MigLayout("filly"));
		sfPanel.add(NPF);
		sfPanel.add(npf, "wrap");
		sfPanel.add(NPR);
		sfPanel.add(npr);
		sfPanel.setBorder(new TitledBorder(new EtchedBorder(), "Suffix"));
		
		JPanel prPanel= new JPanel(new MigLayout("fill"));
		prPanel.add(PF, "cell 0 0");
		prPanel.add(pf, "cell 1 0");
		prPanel.add(PR, "cell 0 1");
		prPanel.add(pr, "cell 1 1");
		prPanel.add(setpri, "cell 0 2 2 2, alignx c");
		
		prPanel.setBorder(new TitledBorder(new EtchedBorder(), "Primers Selection"));

		JPanel gePanel= new JPanel(new MigLayout("filly"));
		gePanel.add(load,"wrap");
		gePanel.add(genesearch,"aligny c");
		gePanel.setBorder(new TitledBorder(new EtchedBorder(), "NCBI BLAST"));

		JPanel adPanel= new JPanel(new MigLayout("filly"));
		adPanel.add(showpri, "aligny c");
		adPanel.setBorder(new TitledBorder(new EtchedBorder(), "Advance"));
		
		JPanel mainPanel= new JPanel(new MigLayout(" fill","[][grow][grow][]","[][]"));
		mainPanel.setPreferredSize(new Dimension(1100,470));
		scrollPane.setPreferredSize(new Dimension(500, 0));
		
/*
		mainPanel.add(srPanel,"cell 0 0 3 1, grow");
		mainPanel.add(sfPanel,"cell 3 0, grow");
		mainPanel.add(wdPanel,"cell 0 1 2 1, grow");
		mainPanel.add(prPanel,"cell 2 1 2 1 , grow");
		mainPanel.add(gePanel,"cell 0 2, grow");
		mainPanel.add(scrollPane,"dock east");
		mainPanel.add(ruPanel,"cell 1 2 3 1, grow");		
	*/
		mainPanel.add(scrollPane,"dock east");
		mainPanel.add(srPanel,"dock north");
		mainPanel.add(wdPanel,"cell 0 0 3 1, grow");
		mainPanel.add(sfPanel,"cell 3 0, grow");
		mainPanel.add(prPanel,"cell 0 1 2 1 , grow");
		mainPanel.add(gePanel,"cell 2 1 2 1, grow");
		mainPanel.add(ruPanel,"dock south");
		
		this.setContentPane(mainPanel);	
		r = new Rengine(new String[]{"--no-save"}, true, new rconsole(textArea));
		System.setOut(new PrintStream(new RConsoleOutputStream(r, 0)));
		System.setErr(new PrintStream(new RConsoleOutputStream(r, 1)));
	 	
	}
	public void submit() {	
		if(!library){
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
		  	library=true;
		}
		String desdir = wd.getText();
		String fold = folder.getText();
		String primer_f = prefsdemo.get((String)pf.getSelectedItem(),"");
		String primer_r = prefsdemo.get((String)pr.getSelectedItem(),"");
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
	}
	
	public void actionPerformed(ActionEvent ae){
	      Object source = ae.getSource();	
	      if (source == start){
	    	textArea.setForeground(Color.BLACK);
	        submit();
	        Boolean err=false;
	        
	        r.eval("catch= tryCatch( { source('./RScripts/CreatingFolders.R') }, error=function(e){e} )");
	    	REXP rcatch= r.eval("catch%>%as.character()");
	    	String rcatchs=((REXP)rcatch).asString();
	    	if(rcatchs.indexOf("Error")>=0){
	    		textArea.setForeground(Color.RED);
	    		newoutput(rcatchs);
	    		err=true;
	    	}else{
	    		output("Creating Folders..."); newoutput("Done!");
	    	}
	        
	    	if(!err){
	    		r.eval("catch= tryCatch( { source('./RScripts/PrimerAnalyzing.R') }, error=function(e){e} )");
	 	    	rcatch= r.eval("catch%>%as.character()");
	 	    	rcatchs=((REXP)rcatch).asString();
	 	    	if(rcatchs.indexOf("Error")>=0){
	 	    		textArea.setForeground(Color.RED);
	 	    		newoutput(rcatchs);
	 	    		err=true;
	 	    	}else{
	 	    		output("Primer Analyzing..."); newoutput("Done!");
	 	    	}
	    	}
			
	    	if(!err&loft==1){
	    		r.eval("catch= tryCatch( { source('./RScripts/FileDownloading.R') }, error=function(e){e} )");
	 	    	rcatch= r.eval("catch%>%as.character()");
	 	    	rcatchs=((REXP)rcatch).asString();
	 	    	if(rcatchs.indexOf("Error")>=0){
	 	    		textArea.setForeground(Color.RED);
	 	    		newoutput(rcatchs);
	 	    		err=true;
	 	    	}else{
	 	    		output("File Downloading..."); newoutput("Done!");
	 	    	}
	    	}
	    	
	    	if(!err){
	    		r.eval("setwd(desdir)");
	    		r.eval("catch= tryCatch( { source('./RScripts/Renaming.R') }, error=function(e){e} )");
	 	    	rcatch= r.eval("catch%>%as.character()");
	 	    	rcatchs=((REXP)rcatch).asString();
	 	    	if(rcatchs.indexOf("Error")>=0){
	 	    		textArea.setForeground(Color.RED);
	 	    		newoutput(rcatchs);
	 	    		err=true;
	 	    	}else{
	 	    		output("Renaming..."); newoutput("Done!");
	 	    	}
	    	}
		
	    	if(!err){
	    		r.eval("catch= tryCatch( { source('../../../RScripts/VectorScreening.R') }, error=function(e){e} )");
	 	    	rcatch= r.eval("catch%>%as.character()");
	 	    	rcatchs=((REXP)rcatch).asString();
	 	    	if(rcatchs.indexOf("Error")>=0){
	 	    		textArea.setForeground(Color.RED);
	 	    		newoutput(rcatchs);
	 	    		err=true;
	 	    	}else{
	 	    		output("Vector Screening..."); newoutput("Done!");
	 	    	}
	    	}
			
	    	if(!err){
	    		r.eval("catch= tryCatch( { source('../../../RScripts/CandidateSequence.R') }, error=function(e){e} )");
	 	    	rcatch= r.eval("catch%>%as.character()");
	 	    	rcatchs=((REXP)rcatch).asString();
	 	    	if(rcatchs.indexOf("Error")>=0){
	 	    		textArea.setForeground(Color.RED);
	 	    		newoutput(rcatchs);;
	 	    		err=true;
	 	    	}else{
	 	    		output("Candidate Sequences Evaluating..."); newoutput("Done!");
	 	    	}
	    	}
			
	    	if(!err){
	    		r.eval("catch= tryCatch( { source('../../../RScripts/SequenceAlignment.R') }, error=function(e){e} )");
	 	    	rcatch= r.eval("catch%>%as.character()");
	 	    	rcatchs=((REXP)rcatch).asString();
	 	    	if(rcatchs.indexOf("Error")>=0){
	 	    		textArea.setForeground(Color.RED);
	 	    		newoutput(rcatchs);
	 	    		err=true;
	 	    	}else{
	 	    		output("Sequences Alignment..."); newoutput("Done!");
	 	    	}
	    	}		
			
	    	if(!err&gs==-1){
	    		r.eval("nt_search=TRUE");
	    		r.eval("catch= tryCatch( { source('../../../RScripts/BLAST.R') }, error=function(e){e} )");
	 	    	rcatch= r.eval("catch%>%as.character()");
	 	    	rcatchs=((REXP)rcatch).asString();
	 	    	if(rcatchs.indexOf("Error")>=0){
	 	    		textArea.setForeground(Color.RED);
	 	    		newoutput(rcatchs);
	 	    		err=true;
	 	    	}else{
	 	    		output("Fetching sequence information...."); newoutput("Done!");
	 	    	}
	    	}else{
	    		r.eval("nt_search=FALSE");
	    	}
	    	
	    	if(!err){
	    		r.eval("catch= tryCatch( { source('../../../RScripts/Summary.R') }, error=function(e){e} )");
	 	    	rcatch= r.eval("catch%>%as.character()");
	 	    	rcatchs=((REXP)rcatch).asString();
	 	    	if(rcatchs.indexOf("Error")>=0){
	 	    		textArea.setForeground(Color.RED);
	 	    		newoutput(rcatchs);
	 	    		err=true;
	 	    	}else{
	 				output("Exporting Summary Information..."); newoutput("Done!");
	 	    	}
	    	}	
	    	
			r.eval("t=proc.time()[3]-time");
			r.eval("hr=t%/%3600");
			r.eval("hrre=t%%3600");
			r.eval("min=hrre%/%60");
			r.eval("sec=hrre%%60");
			REXP time=r.eval("paste0('It tooks ', hr, ' hour ', min, ' minute ', sec%>%round(2), ' second to complete.' )");
			String newtime=((REXP)time).asString();		
			newoutput(newtime);
			newoutput("");
		  	newoutput("");

	      }else if (source == load){
	    	 if(!library){
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
	  		  	library=true;
	  		}
	    	textArea.setForeground(Color.BLACK);
			String work=wd.getText();
			r.eval("wdtest='"+work+"'");
	    	r.eval("handle1= tryCatch( {setwd(wdtest)}, error=function(e){e} )");
	    	REXP handle1r= r.eval("handle1%>%as.character()");
	    	String handle1rs=((REXP)handle1r).asString();
	    	if(handle1rs.indexOf("Error")>=0){
	    		newoutput("Error in changing working directory, it's an invalid path.");
		  		genesearch.setEnabled(false);
	    	}else{
	    		REXP showwd= r.eval("getwd()");
		  	  	String showwds=((REXP)showwd).asString();
		    	r.eval("handle2= tryCatch( {t=blast(db='./db/nt') %>% capture.output()}, error=function(e){e} )");
		    	REXP handle2r= r.eval("handle2%>%as.character()");
		  	  	String handle2rs=((REXP)handle2r).asString();
		  	  	
		  	  	if(handle2rs.indexOf("Error")>=0){
			  		newoutput("BLAST database does not exist at "+showwds);	
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
				  	 newoutput("The environment is ready");  
				  	 newoutput("");newoutput("");
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
		    }
	      }else if (source == browse2){
	    	choose2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	value2= choose2.showOpenDialog(null);
	    	if (value2 == JFileChooser.APPROVE_OPTION){
	    		File selectedfile = choose2.getSelectedFile();
				lp.setText(selectedfile.getAbsolutePath());
	    	}  
	      }else if (source == setpri){
	    	 JFrame ff= new settingframe(sn, sp, pf, pr);
		  }else if (source == showpri){
			 System.out.println((String)pf.getSelectedItem()+": "+prefsdemo.get((String)pf.getSelectedItem(),"")); 
	  	     System.out.println((String)pr.getSelectedItem()+": "+prefsdemo.get((String)pr.getSelectedItem(),"")); 
		  }
	    	
	      /*
	     }else if (source == pf|source == pr){
	    	 
	    	 JComboBox cb = (JComboBox)ae.getSource();
		     String newSelection = (String)cb.getSelectedItem();
		     System.out.println( newSelection+": "+prefsdemo.get(newSelection,""));
		     */
	}
	
    void output(String text) {
        textArea.append(text);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    void newoutput(String text) {
        textArea.append(text+NEWLINE);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }    
  
    
    public void testf(){
		r.eval("Sys.sleep(5)");
	}
}





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
		main.setTitle("MC_Function");	
		main.setLocation(500,  250);
		main.setResizable(false);
		main.pack();
		main.show();
	}
}



class FrameLayout extends JFrame implements ActionListener{
	int loft = 2;
	int gs = 1;
	int priuse =1;
	
	Preferences prefsdemo = Preferences.userRoot().node("/com/sunway/spc");  
    //com/sunway/spc
	String Path ="";
	JButton start = new JButton("START");
	JTextField wd = new JTextField(25);

	String[] sn= new String[20];
	String[] sp= new String[20];
	JComboBox pf, pr;
	JButton setpri = new JButton("Primer Setting");
	JButton showpri = new JButton("Show Primer");

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
	JCheckBox primeruse = new JCheckBox("No Primer Used");
	
	JLabel WD = new JLabel("Working Directory");
	JLabel FD = new JLabel("Task Name");
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
	JButton load = new JButton("Connect to Datebase");
	JButton blast = new JButton("<html> BLAST Local <br> Sequences </html>");
	JButton update = new JButton("<html> Database  <br> Update </html>");
	
	int value;
	int value2;

	JTextArea textArea;
    static final String NEWLINE = System.getProperty("line.separator");

    PrintStream out ; 
    Boolean library=false;
    
    //warning frame
    Boolean proceed=false;
	
	
	public FrameLayout(){
		//Combolist
		for (int i = 0; i < 20; i++) {               
	        sn[i]=prefsdemo.get("name"+i, " ");
	        sp[i]=prefsdemo.get(sn[i], " ");
	    }  
		pf = new JComboBox(sn);
		pf.addActionListener(this);   
		pr = new JComboBox(sn);
		pr.addActionListener(this); 
		
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
		update.setFont(new Font("Ariashowpril", Font.BOLD, 16));
		buttonlocal.setSelected(true);
		us.setEnabled(false);
		pw.setEnabled(false);
		sc.setEnabled(false);
		genesearch.setEnabled(false);
		blast.setEnabled(false);
		ButtonGroup ftplocal = new ButtonGroup();
		ftplocal.add(buttononline);
		ftplocal.add(buttonlocal);
					
		start.addActionListener(this);
		blast.addActionListener(this);
		browse.addActionListener(this);
		browse2.addActionListener(this);
		buttononline.addActionListener(this);
		buttonlocal.addActionListener(this);
		genesearch.addActionListener(this);		
		load.addActionListener(this);
		setpri.addActionListener(this);
		showpri.addActionListener(this);
		update.addActionListener(this);
		primeruse.addActionListener(this);
		
		JPanel ruPanel = new JPanel(new MigLayout("fill"));
		ruPanel.add(start, "alignx c");
		//ruPanel.add(blast, "alignx c");
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
		prPanel.add(primeruse, "cell 2 1, aligny c");
		prPanel.add(setpri, "cell 0 2 2 1, alignx c");
		
		prPanel.setBorder(new TitledBorder(new EtchedBorder(), "Primers Selection"));

		JPanel gePanel= new JPanel(new MigLayout("fill"));
		gePanel.add(load,"wrap, align c, wrap");
		
		gePanel.add(genesearch,"align c");
		gePanel.add(blast, "cell 1 0");
		gePanel.add(update, "cell 1 1");
		gePanel.setBorder(new TitledBorder(new EtchedBorder(), "NCBI BLAST"));

		JPanel adPanel= new JPanel(new MigLayout("filly"));
		adPanel.add(showpri, "aligny c");
		adPanel.setBorder(new TitledBorder(new EtchedBorder(), "Advance"));
		
		JPanel mainPanel= new JPanel(new MigLayout(" fill","[][grow][grow][]","[][]"));
		mainPanel.setPreferredSize(new Dimension(1100,500));
		scrollPane.setPreferredSize(new Dimension(500, 0));
		
		mainPanel.add(scrollPane,"dock east");
		mainPanel.add(srPanel,"dock north");
		mainPanel.add(wdPanel,"cell 0 0 3 1, grow");
		mainPanel.add(sfPanel,"cell 3 0, grow");
		mainPanel.add(prPanel,"cell 0 1 2 1 , grow");
		mainPanel.add(gePanel,"cell 2 1 2 1, grow");
		mainPanel.add(ruPanel,"dock south");
		
		this.setContentPane(mainPanel);	
//		r = new Rengine(new String[]{"--no-save"}, true, new TextConsole2());

	}
	
	
	public void actionPerformed(ActionEvent ae){
	      Object source = ae.getSource();	
	      if (source == start){
	    	  
	      }else if (source == load){
	    	  
	      }else if (source == buttononline){
	    	loft=1;  
	    	lp.setEnabled(false);
	    	blast.setEnabled(false);
	    	us.setEnabled(true);
			pw.setEnabled(true);
			sc.setEnabled(true);
	      }else if (source == buttonlocal){
	        loft=2;
	        lp.setEnabled(true);
	        if(genesearch.isEnabled()){blast.setEnabled(true);}
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
	    	ff.setLocation(450,  250);
	    	ff.setResizable(false);
		  }else if (source == update){
			  JFrame opf=new JFrame();
			  String[] ops= {"Proceed!","Cancel"};
			  int opt=JOptionPane.showOptionDialog(opf,"It may take at least\n5.5 hours to complete.","Everything correct?",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.INFORMATION_MESSAGE, 
                      null, ops, "pokok");
			  if (opt==JOptionPane.YES_OPTION) {
				  
			  }
			  
		  }else if (source == blast){
			  JFrame opf=new JFrame();
			  String[] ops= {"Proceed!","Cancel"};
			  int opt=JOptionPane.showOptionDialog(opf,"Each sequence may take \nat least 3 mins to complete.","Everything correct?",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.INFORMATION_MESSAGE, 
                      null, ops, "pokok");
			  if (opt==JOptionPane.YES_OPTION) {
				  
			  }		       
		  }else if (source == primeruse){
			  priuse=priuse*(-1);
			  if(priuse==-1){
				  setpri.setEnabled(false);
				  pf.setEnabled(false);
				  pr.setEnabled(false);
			  }else{
				  setpri.setEnabled(true);
				  pf.setEnabled(true);
				  pr.setEnabled(true);				  
			  }
		  }
	}
	
    void output(String text) {
        textArea.append(text);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    void newoutput(String text) {
        textArea.append(text+NEWLINE);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }    
  
    
}


class settingframe extends JFrame implements ActionListener{
	JButton ok= new JButton("OK");
	JButton cancel= new JButton("Cancel");
	JButton reset= new JButton("Reset");

	JLabel name= new JLabel("Names");
	JLabel sequence= new JLabel("Sequences");
	JTextField[] ps= new JTextField[20];
	JTextField[] pn= new JTextField[20];
	JPanel panel;
	String[] sn= new String[20];
	String[] sp= new String[20];
	JComboBox pf;
	JComboBox pr;
	Preferences prefsdemo;
	
	Boolean pro;
	
	public settingframe(String[] stringname, String[] stringprimer, JComboBox f, JComboBox r){
		this.sn=stringname;
		this.sp=stringprimer;
		this.pf=f;
		this.pr=r;
		
		panel= new JPanel(new MigLayout());
		panel.add(name);
		panel.add(sequence,"wrap");
		for (int i=0; i<20; i++){
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
	    for (int i = 0; i < 20; i++) {               
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
	    	  for (int i=0; i<20; i++){
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
	    	  for (int i=0; i<20; i++){
		    		ps[i].setText("");
		    		pn[i].setText("C"+(i+1));
		    	  }  
	      }
	}
	
}


class warningframe extends JFrame implements ActionListener{
	JButton k1= new JButton("Proceed!");
	JButton c1= new JButton("Cancel");
	Boolean proc;
	JPanel panel;
	
	public warningframe(){
		
		panel= new JPanel(new MigLayout());
		panel.add(k1);
		panel.add(c1);
		
		k1.addActionListener(this);
		c1.addActionListener(this);
        
		this.setContentPane(panel);
		this.pack();
		this.show();
	}
	public void actionPerformed(ActionEvent ae){
	      Object source = ae.getSource();	
	      if (source == k1){	
	    	  proc=true;
	    	  
	      }else if (source == c1){
	    	  proc=false;   	  
	      }
	}
}
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JTextArea;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

public class rconsole implements RMainLoopCallbacks {
        private JTextArea textarea;
        static final String NEWLINE = System.getProperty("line.separator");

        rconsole(JTextArea textarea) {
        	this.textarea= textarea;
        }
        
        public void rWriteConsole(Rengine re, String text, int oType) {
        	textarea.append(text+NEWLINE);
        	textarea.setCaretPosition(textarea.getDocument().getLength());
        }
        
        public void rBusy(Rengine re, int which) {
        }	
        
        public String rReadConsole(Rengine re, String prompt, int addToHistory) {
            return null;
        }
        
        public void rShowMessage(Rengine re, String message) {
        	  System.out.println("rShowMessage \""+message+"\"");
        }
        
        public String rChooseFile(Rengine re, int newFile) {
        	   return null;
        }
        
        public void   rFlushConsole (Rengine re) {
    	}
        
        public void   rLoadHistory  (Rengine re, String filename) {
        }			
        
        public void   rSaveHistory  (Rengine re, String filename) {
        }			
}


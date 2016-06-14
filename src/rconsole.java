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
            System.out.println("rBusy("+which+")");
        }
        
        public String rReadConsole(Rengine re, String prompt, int addToHistory) {
            System.out.print(prompt);
            try {
                BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                String s=br.readLine();
                return (s==null||s.length()==0)?s:s+"\n";
            } catch (Exception e) {
                System.out.println("jriReadConsole exception: "+e.getMessage());
            }
            return null;
        }
        
        public void rShowMessage(Rengine re, String message) {
        	textarea.append(message+NEWLINE);
        	textarea.setCaretPosition(textarea.getDocument().getLength());
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


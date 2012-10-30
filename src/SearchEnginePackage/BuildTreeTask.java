package SearchEnginePackage;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author bones
 */
//This build task builds a new B+ tree as well as updates a progress bar for the 
//file loading
public class BuildTreeTask extends SwingWorker<BPlusTree, Integer>
{
    private String filename;
    private String threadname;
    private JTextArea textArea;
    private BPlusTree bTree;
    
    public BuildTreeTask(String filename, String threadname, JTextArea textArea) 
    {
        this.filename = filename;
        this.threadname = threadname;
        this.textArea = textArea;
    }

    @Override
    protected BPlusTree doInBackground() throws Exception 
    {
        return new BPlusTree(filename);
     
    }
    
    @Override 
    public void done()
    {
        try
        {
            SearchCmd.SetBTree((BPlusTree)this.get());
            textArea.setText("File is ready for searching.");
        }
        catch(Exception ignored)
        {
            
        }
    }
}

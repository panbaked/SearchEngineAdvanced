package SearchEnginePackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JTextArea;

class Searcher {

    public static boolean exists (SimpleMap hashMap, String word) 
    {
    	int hash = word.hashCode();
    	if(hashMap.get(hash) != null)
        {
    		return true;
        }
    	return false;    		
    }
    
    public static boolean exists(BinarySearchTree bst, String word)
    {
    	int hash = word.hashCode();
    	if(bst.find(hash, bst.root) != null)
        {
    		return true;
        }
    	return false;
    }
    
    public static SimpleMap buildHashMap(String filename) throws IOException 
    {
    	String line, lastURL = "";
    	SimpleMap hashMap = new SimpleMap();
    	BufferedReader infile = new BufferedReader(new FileReader(filename));
    	
    	line = infile.readLine();
    	
    	while(line != null)
    	{
            if(isPage(line))
            {
                lastURL = getURL(line);
            }
            else
            {
                if(exists(hashMap, line))
                {
                     URLList duplicateURLEntry = find(hashMap.get(line.hashCode()), lastURL); //check for this url already exisiting with this name
                     if(duplicateURLEntry == null) //if it is not a duplicate we update the hashMap
                     {
                        hashMap.put(line, line.hashCode(), new URLList(lastURL, null));
                     }
                }
                else
                {
                    hashMap.put(line, line.hashCode(), new URLList(lastURL, null));
                }
            }
            line = infile.readLine();
    	}
    	infile.close();
    	return hashMap;
    }
    
    public static BinarySearchTree buildBST(String filename) throws IOException
    {
    	String line, lastURL = "";
    	BinarySearchTree bst = new BinarySearchTree();
    	BufferedReader infile = new BufferedReader(new FileReader(filename));
    	
    	line = infile.readLine();
    	
    	while(line != null)
    	{
            if(isPage(line))
            {
                lastURL = getURL(line);
            }
            else
            {
                if(exists(bst, line))
                {
                    URLList duplicateURLEntry = find(bst.find(line.hashCode(), bst.root).value, lastURL); //check for this url already exisiting with this name
                    if(duplicateURLEntry == null) //if it is not a duplicate we update the hashMap
                    {
                        bst.insert(new BSTNode(line, line.hashCode(), new URLList(lastURL, null)));
                    }

                }
                else
                {
                    bst.insert(new BSTNode(line, line.hashCode(), new URLList(lastURL, null)));
                }
            }
            line = infile.readLine();
    	}
    	infile.close();
    	return bst;
    }
    
    public static boolean isPage(String line)
    {
    	if(line.length() < 6)
        {
            return false;
        }
    	if(line.substring(0, 6).equals("*PAGE:"))
        {
            return true;
        }
    	else
        {
            return false;
        }
    }
    
    public static String getURL(String pageLine)
    {
    	if(pageLine.length() < 6)
        {
            return "";
        }
    	return pageLine.substring(6);
    }
    
    public static URLList find(URLList l, String url)
    {
        while (l != null) 
        {
            if (l.url.equals (url)) 
            {
                return l;
            }
            l = l.next;
        }
        return null;
    }
    public static void printURLs(URLList urlList)
    {    	
    	int i = 0;
            while(urlList != null)
            {
                System.out.println(urlList.url);
                urlList = urlList.next;
		i++;
            }
            System.out.println("There were "+ i + " URLs attached.");			
	}
    
    public static String getURLString(URLList urlList)
    {    	
        String s = "";
        while(urlList != null)
        {
            s += urlList.url + "\n";
            urlList = urlList.next;
        }
        return s;
    }
}

public class SearchCmd {

    private static BPlusTree theBTree;
   
    public static void main(String[] args)
    {
        
              /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SearcherUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SearcherUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SearcherUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SearcherUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try
                {
                    new SearcherUI().setVisible(true);
                }
                catch(Exception e)
                {
                    System.out.println("There was an error in loading.");
                }
            }
        });
        
    }
    
    public static void BuildTree(String filename, JTextArea textArea) throws IOException, Exception
    {
        BuildTreeTask task = new BuildTreeTask(filename, "build_tree_task", textArea);
        try
        {
            task.execute();
        }
        catch(Exception e)
        {
            if(e instanceof IOException)
            {
                throw e;
            }
            else
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void SetBTree(BPlusTree bTree)
    {
        theBTree = bTree;
    }
    
    public static String Search(String word) throws IOException
    {
        if(theBTree != null)
        {
            return processInput(word, theBTree);
        }
        return "The database is currently being built.";
    }
 
    public static String Search(String word, BPlusTree bTree)
    {
        return processInput(word, bTree);
    }
    
    public static String processInput(String line, BPlusTree bTree)
    {
        StringTokenizer andTokenizer = new StringTokenizer(line, "ANDand");
    	
    	if(andTokenizer.countTokens() == 2) //We have two parts X AND Y
    	{
            return processAnd(andTokenizer, bTree);
    	}
    	StringTokenizer orTokenizer = new StringTokenizer(line, "ORor");
    	
    	if(orTokenizer.countTokens() == 2) //We have two parts X OR Y
    	{
            return processOr(orTokenizer, bTree);
    	}
    	
    	//We have a single query
    	return processSingleQuery(line, bTree);    
    }
    
    public static String processAnd(StringTokenizer tokenizer, BPlusTree bTree)
    {
    	String x = tokenizer.nextToken();
    	String y = tokenizer.nextToken();
    	x = trimSpaces(x);
    	y = trimSpaces(y);
    	
    	URLList commonURLs = findCommonURLs(x, y, bTree);
    	
        return Searcher.getURLString(commonURLs);
    }
   
    public static String processOr(StringTokenizer tokenizer, BPlusTree bTree)
    {
    	String x = tokenizer.nextToken();
        String y = tokenizer.nextToken();
        
        x = trimSpaces(x);
        y = trimSpaces(y);
        
        URLList xURLs = bTree.getURLs(x.hashCode());
        URLList yURLs = bTree.getURLs(y.hashCode());
        
        URLList current = xURLs;
        while(current.next != null)
        {
            current = current.next;
        }
        current.next = yURLs;
        
        return Searcher.getURLString(xURLs);
    }
    
    public static String processSingleQuery(String line, BPlusTree bTree)
    {
    	URLList queryResult = bTree.getURLs(line.hashCode());
    	
    	if(queryResult != null)
        {
            return Searcher.getURLString(queryResult);
        }
    	else
    	{
            return "The word \""+line+"\" has NOT been found.";
    	}
    	
    }
    
    public static URLList findCommonURLs(String x, String y, BPlusTree bTree)
    {
    	URLList currentX = bTree.getURLs(x.hashCode());
    	
    	URLList returnList = null;
    	while(currentX != null)
    	{
    		URLList currentY = bTree.getURLs(y.hashCode());
    		while(currentY != null)
    		{
                    System.out.println("Checking:"+currentX.url+","+currentY.url);
                    if(currentX.url.equals(currentY.url))
                    {
                            URLList newEntry = new URLList(currentX.url, returnList);
                            returnList = newEntry; //insert at the beginning
                    }
                    currentY = currentY.next;
                }
    		currentX = currentX.next;
    	}
    	
    	return returnList;
    }
    public static String trimSpaces(String s)
    {
    	return s.replaceAll("\\s", "");
    }
}

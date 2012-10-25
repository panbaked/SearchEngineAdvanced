import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Searcher {
	
    public static boolean exists (SimpleMap hashMap, String word) {
    	int hash = word.hashCode();
    	if(hashMap.get(hash) != null)
    		return true;
    	
    	return false;    		
    }
    public static boolean exists(BinarySearchTree bst, String word)
    {
    	int hash = word.hashCode();
    	if(bst.find(hash, bst.root) != null)
    		return true;
    	
    	return false;
    }
    public static SimpleMap buildHashMap(String filename) throws IOException {
    	String line, lastURL = "";
    	SimpleMap hashMap = new SimpleMap();
    	BufferedReader infile = new BufferedReader(new FileReader(filename));
    	
    	line = infile.readLine();
    	
    	while(line != null)
    	{
    		if(isPage(line))
    			lastURL = getURL(line);
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
    			lastURL = getURL(line);
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
    
    public static BPlusTree buildBTree(String filename) throws IOException
    {
    	String line, lastURL = "";
    	BPlusTree bTree = new BPlusTree();
    	BufferedReader infile = new BufferedReader(new FileReader(filename));
    	
    	line = infile.readLine();
    	
    	while(line != null)
    	{
    		if(isPage(line))
                    lastURL = getURL(line);
    		else
    		{
                    /*
                        
                    URLList list = bTree.find(line.hashCode());
                    if(list != null)
                    {
                        URLList duplicateURLEntry = find(list, lastURL); //check for this url already exisiting with this name
                        if(duplicateURLEntry == null) //if it is not a duplicate we update the hashMap
                        {
                                bTree.insert(line.hashCode(), new URLList(lastURL, null));
                        }

                    }
                    else
                    {
                            bTree.insert(line.hashCode(), new URLList(lastURL, null));
                    }
                    * */
                    bTree.insert(line.hashCode(), new URLList(lastURL, null));
    		}
    		line = infile.readLine();
    			
    	}
    	infile.close();
    	return bTree;
    }
    public static boolean isPage(String line)
    {
    	if(line.length() < 6)
    		return false;
    	
    	if(line.substring(0, 6).equals("*PAGE:"))
    		return true;
    	else
    		return false;
    }
    
    public static String getURL(String pageLine)
    {
    	if(pageLine.length() < 6)
    		return "";
    	return pageLine.substring(6);
    }
    
    public static URLList find(URLList l, String url)
    {
	   	 while (l != null) {
	         if (l.url.equals (url)) {
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

    /*
    public static void main (String[] args) throws IOException {
        String name;

        // Check that a filename has been given as argument
        if (args.length != 1) {
            System.out.println("Usage: java SearchCmd <datafile>");
            System.exit(1);
        }
       
        // Read the file and create the hash map
        //SimpleMap hashMap = Searcher.buildHashMap(args[0]);
        
        //Read the file a create the BST
        BinarySearchTree bst = Searcher.buildBST(args[0]);
        
        // Ask for a word to search
        BufferedReader inuser =
            new BufferedReader (new InputStreamReader (System.in));

        System.out.println ("Hit return to exit.");
        boolean quit = false;
        while (!quit) {
            System.out.print ("Search for: ");
            name = inuser.readLine(); // Read a line from the terminal
            if (name == null || name.length() == 0) 
            {
                quit = true;
            } 
            else
            {
            	//processInput(name, bst);
            }
        }
    }
    */
    
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
                new SearcherUI().setVisible(true);
            }
        });
    }
    public static String Search(String word, String filename) throws IOException
    {
        //Read the file a create the BST
        BPlusTree bTree = Searcher.buildBTree(filename);
        
        // Ask for a word to search
        BufferedReader inuser =
            new BufferedReader (new InputStreamReader (System.in));
        
        boolean quit = false;
        return processInput(word, bTree);
        
    }
    /*
    public static void processInput(String line, SimpleMap hashMap)
    {
    	StringTokenizer andTokenizer = new StringTokenizer(line, "AND");
    	
    	if(andTokenizer.countTokens() == 2) //We have two parts X AND Y
    	{
    		processAnd(andTokenizer, hashMap);
    		return;
    	}
    	StringTokenizer orTokenizer = new StringTokenizer(line, "OR");
    	
    	if(orTokenizer.countTokens() == 2) //We have two parts X OR Y
    	{
    		processOr(orTokenizer, hashMap);
    		return;
    	}
    	
    	//We have a single query
    	processSingleQuery(line, hashMap);    	
    		
    }
    */
    public static String processInput(String line, BPlusTree bTree)
    {
        StringTokenizer andTokenizer = new StringTokenizer(line, "AND");
    	
    	if(andTokenizer.countTokens() == 2) //We have two parts X AND Y
    	{
    		return processAnd(andTokenizer, bTree);
    	}
    	StringTokenizer orTokenizer = new StringTokenizer(line, "OR");
    	
    	if(orTokenizer.countTokens() == 2) //We have two parts X OR Y
    	{
    		//processOr(orTokenizer, bst);
    		//return processOr(orTokenizer, bTree);
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
   
    public static void processOr(StringTokenizer tokenizer, SimpleMap hashMap)
    {
    	System.out.println("Got an OR with X:"+tokenizer.nextToken()+" and Y:"+tokenizer.nextToken());
    }
    
    public static String processSingleQuery(String line, BPlusTree bTree)
    {
    	URLList queryResult = bTree.find(line.hashCode());
    	
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
    	URLList currentX = bTree.find(x.hashCode());
    	
    	URLList returnList = null;
    	while(currentX != null)
    	{
    		URLList currentY = bTree.find(y.hashCode());
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

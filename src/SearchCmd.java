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
    public static SimpleMap readHashMap(String filename) throws IOException {
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
}

public class SearchCmd {

    public static void main (String[] args) throws IOException {
        String name;

        // Check that a filename has been given as argument
        if (args.length != 1) {
            System.out.println("Usage: java SearchCmd <datafile>");
            System.exit(1);
        }
       
        // Read the file and create the linked list
        SimpleMap hashMap = Searcher.readHashMap(args[0]);

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
            	processInput(name, hashMap);
            }
        }
    }
    
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
    
    public static void processAnd(StringTokenizer tokenizer, SimpleMap hashMap)
    {
    	String x = tokenizer.nextToken();
    	String y = tokenizer.nextToken();
    	x = trimSpaces(x);
    	y = trimSpaces(y);
    	
    	URLList commonURLs = findCommonURLs(x, y, hashMap);
    	
    	System.out.println("Got an AND with X:"+x+" and Y:"+y);
    	Searcher.printURLs(commonURLs);

    }
    
    public static void processOr(StringTokenizer tokenizer, SimpleMap hashMap)
    {
    	System.out.println("Got an OR with X:"+tokenizer.nextToken()+" and Y:"+tokenizer.nextToken());
    }
    
    public static void processSingleQuery(String line, SimpleMap hashMap)
    {
    	URLList queryResult = hashMap.get(line.hashCode());
    	
    	if(queryResult != null)
		{
			System.out.println ("The word \""+line+"\" has been found.");
	        System.out.println("URLs linked to "+ line);

    		Searcher.printURLs(queryResult);
		}
    	else
    	{
    		System.out.println ("The word \""+line+"\" has NOT been found.");
    	}
    	
    }
    public static URLList findCommonURLs(String x, String y, SimpleMap hashMap)
    {
    	URLList currentX = hashMap.get(x.hashCode());
    	
    	URLList returnList = null;
    	while(currentX != null)
    	{
    		URLList currentY = hashMap.get(y.hashCode());
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
    		System.out.println("boop");
    		currentX = currentX.next;
    	}
    	
    	return returnList;
    }
    public static String trimSpaces(String s)
    {
    	return s.replaceAll("\\s", "");
    }
}

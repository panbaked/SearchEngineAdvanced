package SearchEnginePackage;

// Used to keep track of promotions which need to float up towards the root upon insertion

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

class BPTNodeInfo {
    BPTNode node;
    int key;
}

abstract class BPTNode {
    // How many keys we're storing
    int nKeys; 
    // We store one more so we can "fill up" before splitting
    int[] keys = new int[BPlusTree.N+1]; 
    
    // Returns true if a promotion was necessary
    abstract boolean insert(int key, int urlList, BPTNodeInfo promotionNode); 
}
class InternalNode extends BPTNode {
    // Always contains one more than the keys set, can be either an InternalNode or LeafNode
    BPTNode[] children = new BPTNode[BPlusTree.N+2]; 

    @Override
    public boolean insert(int key, int urlList, BPTNodeInfo promotionNode) {
        // In this method i use toArray a bunch, 
        // I'm not sure of the overhead of this, 
        // but these arrays are from 2-4 in length
        // So the damage shouldn't be too big
        
        int i; // Keeps track of which child we navigate through to insert the new entry
        
        // Cycle through the keys and see if we are smaller than one of them, 
        // this is where we are directed to the child node
        for(i = 0; i < nKeys; i++) {
            if(key <= keys[i]) {
                break; // We have found our path, it is to the left of key i
            }
        }
        
        // If we did not find a path to the left of any keys it is all the way to the right
        // Note that we always have 1 more child than keys so this never fails

        // We then navigate through the i-th child and check if it required a promote
        // This is the recursion step which eventually reaches the end of the tree
        // If a promote is required this floats up towards the root
        boolean promotionRequired = children[i].insert(key, urlList, promotionNode);

        // Two options, no promotion (trivial) and promotion, when a promotion 
        // happens a key is moved from a child up to a parent
        //
        // This parent tries to accommodate the key, however if it is full it splits
        // 
        // Splitting involves find a median among the keys, the keys lower 
        // than the median are placed into a new left node
        // and the keys higher than the median are placed into a new right node, 
        // the median key is placed into the parent's keys (promotion)
        //
        // This promotion can cause the parent to split, and so on until 
        // the root is eventually split and the tree grows in height by 1
        if(promotionRequired) {
            // We have the i-th child index
            // The key is inserted at position i
            // The child is inserted at position i+1 (the i+1 child belongs to the i key)
            // So we shift every entry > i up to make space for the new key
            for(int j = nKeys; j > i; j--) {
                keys[j] = keys[j-1]; //shift key up
                children[j+1] = children[j]; //shift child up
            }

            keys[i] = promotionNode.key;
            children[i+1] = promotionNode.node;
            nKeys++;

            // Now that we have grown we can check and see if we are big 
            // enough to require a split
            if(nKeys <= BPlusTree.N) {
                // No splitting required
                return false;
            } else {
                // We are full, so we must split into two nodes and promote our median key
                // The middle key (N/2) is promoted
                promotionNode.key = keys[BPlusTree.N/2];

                // Create the new node and fill it with keys > N/2+1
                InternalNode newNode = new InternalNode();

                promotionNode.node = newNode;
                // The next two iterators are stored outside the "for" 
                // so we can add in the final child 
                int j; // Index into this node's arrays
                int k = 0; // This is our index into the newNode's array
                for(j = BPlusTree.N/2+1; j <= BPlusTree.N; j++) {
                    newNode.keys[k] = keys[j];
                    newNode.children[k] = children[j];
                    k++;
                }
                // And don't forget to add in the last child
                newNode.children[k] = children[j]; 
                newNode.nKeys = BPlusTree.N/2;

                nKeys = BPlusTree.N/2;
                return true; // Return to our parent that a promotion was required
            }
        }
        return false; // No promotion required we succeeded
    }
}

class LeafNode extends BPTNode {
    HashSet<Integer>[] data = new HashSet[BPlusTree.N+1];
    LeafNode sibling; // The leaf to the right, allows for quick sequential access

    public LeafNode() {

    }
    
    @Override
    public boolean insert(int key, int urlIndex, BPTNodeInfo promotionNode) {
        int i, j;

        if(nKeys > 0) {
            // Cycle through the keys and find if the key is smaller than another
            for(i = 0; i < nKeys; i++) {
                if(key < keys[i]) {
                    break; // Stop here we will insert at position i
                } else if(key == keys[i]) { // We have a duplicate
                    data[i].add(urlIndex);
                    // We check for an existing entry
                    /*URLListIndexed existingList = BPlusTree.find(data[i], url.urlIndex); 
                    if(existingList == null) { // If one doesn't exist we add this url
                        url.next = data[i];
                        data[i] = url;
                    }
                    */
                    return false; // No promotion is required
                }
            }
        } else {
            // We're empty so just add it
            i = 0;
        }
        // Now that we have the new position we insert it by shifting the keys up
        for(j = nKeys; j > i; j--) {
            keys[j] = keys[j-1];
            data[j] = data[j-1];
        }

        keys[i] = key;
        // url.next = data[i]; // Insert the urllist entry at the start of the list
        // data[i] = url;
        data[i] = new HashSet<Integer>();
        data[i].add(urlIndex);
        nKeys++;

        if(nKeys <= BPlusTree.N) { // Are we a valid size
            return false; // No promotion required
        } else {
            // We're full so split into two and float up the middle key to the parent
            // A new leaf is now required so we create that and make it our sibling
            LeafNode newNode = new LeafNode();
            newNode.sibling = sibling; // Insert the new node into the sibling list
            sibling = newNode;

            // Now we move all of the keys and data higher than N/2+1 to the new node
            int k = 0; // Our index into the new node's arrays
            
            for(j = BPlusTree.N/2+1; j <= BPlusTree.N; j++) {
                newNode.keys[k] = keys[j];
                newNode.data[k] = data[j];
                k++;
            }

            newNode.nKeys = k;

            // Setup the node to be promoted (sent to the parent)
            promotionNode.key = keys[BPlusTree.N/2];
            promotionNode.node = newNode;

            nKeys = BPlusTree.N/2; 

            return true; // We promoted successfully
        }
    }
}

public class BPlusTree {
    BPTNode root;
    LeafNode firstLeaf; // So we can go through the items sequentially we store this reference
    static final int N = 2;
    String[] urlTable = new String[64];
    int urlCount;
    
    public BPlusTree() {
        // do nothing, we use this for unit testing
    }
    
    public BPlusTree(String filename) throws IOException {
        buildBTree(filename);
    }
    
    /**
     * Builds the B+ tree from a given file, the file is expected to begin contain
     * lines with *PAGE indicating a web page, where subsequent lines contain words 
     * associated with the page.     * 
     * @param filename the filename of the file to build from
     * @throws IOException when the file cannot be found
     */
    private void buildBTree(String filename) throws IOException {
    	String line, lastURL = "";
        int lastURLTableKey = 0;
    	BufferedReader infile = new BufferedReader(new FileReader(filename));
    	
    	line = infile.readLine();
    	
    	while(line != null) {
            if(isPage(line)) {
                lastURL = getURL(line);
                lastURLTableKey = addNewURL(lastURL);
            } else {
                insert(line.hashCode(), lastURLTableKey);
            }
            line = infile.readLine();
    	}
    	infile.close();
    }
    
    /**
     * A helper method to check if a given string represent a url 
     * @param line a string
     * @return true if the String starts with "*PAGE:"
     */
    public static boolean isPage(String line) {
    	if(line.length() < 6) {
            return false;
        }
    	if(line.substring(0, 6).equals("*PAGE:")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns the URL from a *PAGE line
     * @param pageLine the line to parse
     * @return the URL string
     */
    public static String getURL(String pageLine) {
    	if(pageLine.length() < 6) {
            return "";
        }
    	return pageLine.substring(6);
    }
    
    /**
     * Returns the linked-list associated with a given hash code key.
     * @param key the hash code to search for
     * @return the URL linked-list associated with a given hash code
     */
    public HashSet<Integer> find(int key) {
        return find(key, root);
    }
    
    /**
     * The internal search method, called by the publicly visible find(int key).
     * This method is recursive and calls itself on child nodes to traverse the
     * tree.
     * @param key the hash code to search for
     * @param node the node being searched
     * @return 
     */
    private HashSet<Integer> find(int key, BPTNode node) {
        if(node == null) {
            return null;
        }
        
        // We step down through the tree until we reach a leaf
        // Each step if we are less than or equal to the key then we choose the left path
        int i;
        for(i = 0; i < node.nKeys; i++) {
            if(key <= node.keys[i]) {
                break; // We found our child to navigate through
            }
        }

        // Here we test if we are an internal node, if so we continue our recursion
        if(node instanceof InternalNode) {
            return find(key, ((InternalNode)node).children[i]);
        } else if(node instanceof LeafNode) {
            LeafNode leafNode = (LeafNode)node;

            for(int j = 0; j < node.nKeys; j++) {
                if(key == leafNode.keys[j]) {
                    return ((LeafNode)node).data[i]; // We found it
                }
            }
        }

        return null;
    }
    
    /**
     * Inserts a new URLList into the tree
     * @param key the hash code of the new element
     * @param urlList the URLList to insert
     */
    // We need to store a node used while inserting to "carry" promotion of a node higher up
    public void insert(int key, int urlListIndex) {
        BPTNodeInfo promotionNode = new BPTNodeInfo(); 

        if(root == null) {
            // Empty try so we just make a leaf and place our value there
            firstLeaf = new LeafNode();
            root = firstLeaf;
        }

        if(!root.insert(key, urlListIndex, promotionNode)) {
            // The data fit into the tree perfectly
        } else {
            // A promotion has propogated all the way to the root, so we split the root
            InternalNode newRoot = new InternalNode();
            // move the current root down as the left child
            newRoot.children[0] = root; 
            // The propagated key is our new top key
            newRoot.keys[0] = promotionNode.key; 
            // And the right child is the node that was promoted
            newRoot.children[1] = promotionNode.node; 
            newRoot.nKeys = 1;

            root = newRoot;
        }
    }
    
    /**
     * Adds a new URL to the internal URL table, expanding the table if necessary
     * @param url the URL to add
     * @return the current urlCount (our reference to this URL)
     */
    private int addNewURL(String url) {
        // We add a new url to the url table, if this new url will fill the array we expand
        if(urlCount + 1 >= urlTable.length) {
            urlTable = Arrays.copyOf(urlTable, urlTable.length*2);
        }
        
        urlTable[urlCount] = url;
        urlCount++;
        
        return urlCount;
    }
    
    /**
     * A helper method to see if a given URL exists within a URLList
     * @param l the URLList to search
     * @param url the URL string to search for
     * @return the URLList if one is found, otherwise null
     */
    public static URLList find(URLList l, String url) {
        while (l != null) {
            if (l.url.equals (url)) {
                return l;
            }
            l = l.next;
        }
        return null;
    }
    
    /**
     * Compiles a URLList with 
     * @param hashCode
     * @return 
     */
    public URLList getURLs(int hashCode) {
        HashSet<Integer> indices = find(hashCode);
        
        if(indices == null) {
            return null;
        }
        
        URLList urlList = null;
        // I know this operation is slow for hashset's but it's better than 
        // thousands of contains on a linked list
        for(Integer i : indices) {
            urlList = new URLList(urlTable[i], urlList);
        }
        return urlList;
    }
}

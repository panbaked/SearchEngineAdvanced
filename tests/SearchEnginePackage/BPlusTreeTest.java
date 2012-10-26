package SearchEnginePackage;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bones
 */
public class BPlusTreeTest {
    
    public BPlusTreeTest() {
    }
    
    /**
     * Test of find method, of class BPlusTree.
     */
    @Test
    public void testFind_int() 
    {
        int key = 0;
        BPlusTree instance = new BPlusTree();
        URLList expResult = null;
        URLList result = instance.find(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of find method, of class BPlusTree.
     */
    @Test
    public void testFind_int_BPTNode() {
        System.out.println("find");
        int key = 0;
        BPlusTree instance = new BPlusTree();
        instance.insert(key, new URLList("Zero", null));
        
        String expResult = "Zero";
        String result = instance.find(key).url;
        assertEquals(expResult, result);
    }

    /**
     * Test of insert method, of class BPlusTree.
     */
    @Test
    public void testInsert() {
        int key = 5;
        URLList urlList = new URLList("Five", null);
        BPlusTree instance = new BPlusTree();
        instance.insert(key, urlList);
        
        assertEquals(key, instance.root.keys[0]);
    }
    
    @Test
    public void testFindInt() {
        BPlusTree bTree = new BPlusTree();

        bTree.insert(1, new URLList("One", null));
        bTree.insert(2, new URLList("Two", null));
        bTree.insert(3, new URLList("Three", null));

        URLList list = bTree.find(1);
        if(!list.url.equals("One"))
            fail("Did not find the right entry");
    }

    @Test
    public void testInsertSimple() {
        BPlusTree bTree = new BPlusTree();

        bTree.insert(1, new URLList("One", null));
        bTree.insert(2, new URLList("Two", null));
        bTree.insert(3, new URLList("Three", null));

        if(!(bTree.root instanceof InternalNode))
        {
            fail("Root is not an internal node");
        }
        else
        {
            InternalNode rootINode = (InternalNode)bTree.root;
            assertEquals(2, rootINode.keys[0]); //the root contains the key 0
            assertEquals(3, rootINode.children[1].keys[0]); //the right child contains the key 1
            assertEquals(1, rootINode.children[0].keys[0]); //the left child contains the key -1
        }
    }
    
    @Test
    public void testInsertSimple2() {
        BPlusTree bTree = new BPlusTree();

        bTree.insert(1, new URLList("One", null));
        bTree.insert(2, new URLList("Two", null));
        bTree.insert(3, new URLList("Three", null));
        bTree.insert(27, new URLList("Twenty-Seven", null));
        bTree.insert(-95, new URLList("Negative Ninety-Five", null));
        
        if(!(bTree.root instanceof InternalNode))
        {
            fail("Root is not an internal node");
        }
        else
        {
            InternalNode rootINode = (InternalNode)bTree.root;
            assertEquals(2, rootINode.keys[0]); //the root contains the key 0
            assertEquals(3, rootINode.children[1].keys[0]); //the right inner child contains the key 3
            assertEquals(-95, rootINode.children[0].keys[0]); //the left outer child is -95
            assertEquals(1, rootINode.children[0].keys[1]); //the left inner child is 1
            assertEquals(27, rootINode.children[1].keys[1]); //the right most child is 27
        }
    }
    
    @Test
    public void testFindSimple() {
        BPlusTree bTree = new BPlusTree();

        bTree.insert(1, new URLList("One", null));
        bTree.insert(2, new URLList("Two", null));
        bTree.insert(3, new URLList("Three", null));

        if(!(bTree.root instanceof InternalNode))
        {
            fail("Root is not an internal node");
        }
        else
        {
            URLList list = bTree.find(3);
            assertNotNull(list);
            assertEquals("Three", list.url);
        }
    }
    
      @Test
    public void testBuildSpeedSmall() {
        try {
            Searcher.buildBTree("small.txt");
        } catch (IOException e) {
            System.out.println("Could not find the specified file.");
        }
    }

    @Test
    public void testBuildSpeedMedium() {
        try {
            Searcher.buildBTree("medium.txt");
        } catch (IOException e) {
            System.out.println("Could not find the specified file.");
        }
    }

    @Test
    public void testBuildSpeedLarge() {
        try {
            Searcher.buildBTree("large.txt");
        } catch (IOException e) {
            System.out.println("Could not find the specified file.");
        }
    }
    
    @Test
    public void testSearchSpeedLarge()
    {
        try
        {
            BPlusTree bTree = Searcher.buildBTree("large.txt");
            SearchCmd.Search("IT", bTree);
        }
        catch (IOException e)
        {
            System.out.println("Could not find the specified file.");
        }
    }
}

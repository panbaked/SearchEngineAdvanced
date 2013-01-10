package SearchEnginePackage;

import java.io.IOException;
import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class BPlusTreeTest {
    
    public BPlusTreeTest() {
    }
    

    @Test
    public void testFind_int() 
    {
        int key = 0;
        BPlusTree instance = new BPlusTree();
        URLList expResult = null;
        HashSet result = instance.find(key);
        assertEquals(expResult, result);
    }

  
    @Test
    public void testFind_int_BPTNode() {
        System.out.println("find");
        int key = 0;
        BPlusTree instance = new BPlusTree();
        instance.insert(key, 0);
        boolean result = instance.find(key).contains(0);
        assertEquals(true, result);
    }

    @Test
    public void testInsert() {
        int key = 5;

        BPlusTree instance = new BPlusTree();
        instance.insert(key, 5);
        
        assertEquals(key, instance.root.keys[0]);
    }
   
    @Test
    public void testInsertSimple() {
        BPlusTree bTree = new BPlusTree();

        bTree.insert(1, 1);
        bTree.insert(2, 2);
        bTree.insert(3, 3);

        if(!(bTree.root instanceof InternalNode))
        {
            fail("Root is not an internal node");
        }
        else
        {
            InternalNode rootINode = (InternalNode)bTree.root;
            //the root contains the key 0
            assertEquals(2, rootINode.keys[0]); 
            //the right child contains the key 1
            assertEquals(3, rootINode.children[1].keys[0]); 
            //the left child contains the key -1
            assertEquals(1, rootINode.children[0].keys[0]); 
        }
    }
    
    @Test
    public void testInsertSimple2() {
        BPlusTree bTree = new BPlusTree();

        bTree.insert(1, 1);
        bTree.insert(2, 2);
        bTree.insert(3, 3);
        bTree.insert(27, 27);
        bTree.insert(-95, -95);
        
        if(!(bTree.root instanceof InternalNode))
        {
            fail("Root is not an internal node");
        }
        else
        {
            InternalNode rootINode = (InternalNode)bTree.root;
            //the root contains the key 0
            assertEquals(2, rootINode.keys[0]); 
            //the right inner child contains the key 3
            assertEquals(3, rootINode.children[1].keys[0]); 
            //the left outer child is -95
            assertEquals(-95, rootINode.children[0].keys[0]); 
            //the left inner child is 1
            assertEquals(1, rootINode.children[0].keys[1]); 
            //the right most child is 27
            assertEquals(27, rootINode.children[1].keys[1]); 
        }
    }
    
    @Test
    public void testFindSimple() {
        BPlusTree bTree = new BPlusTree();

        bTree.insert(1, 1);
        bTree.insert(2, 2);
        bTree.insert(3, 3);

        if(!(bTree.root instanceof InternalNode))
        {
            fail("Root is not an internal node");
        }
        else
        {
            boolean hasThree = bTree.find(3).contains(3);
            assertEquals(true, hasThree);
        }
    }
    
      @Test
    public void testBuildSpeedSmall() {
        try 
        {
            BPlusTree bTree = new BPlusTree("small.txt");
        } 
        catch (IOException e) 
        {
            System.out.println("Could not find the specified file.");
        }
    }

    @Test
    public void testBuildSpeedMedium() {
        try 
        {
            BPlusTree bTree = new BPlusTree("medium.txt");
        } 
        catch (IOException e) 
        {
            System.out.println("Could not find the specified file.");
        }
    }

    @Test
    public void testBuildSpeedLarge() {
        try {
            BPlusTree bTree = new BPlusTree("large.txt");
        } 
        catch (IOException e) 
        {
            System.out.println("Could not find the specified file.");
        }
    }
    
    @Test
    public void testSearchSpeedLarge()
    {
        try
        {
            BPlusTree bTree = new BPlusTree("large.txt");
            SearchCmd.Search("IT", bTree);
        }
        catch (IOException e)
        {
            System.out.println("Could not find the specified file.");
        }
    }
}

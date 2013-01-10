package SearchEnginePackage;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinarySearchTreeTest {

    public BinarySearchTreeTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of find method, of class BinarySearchTree.
     */
    @Test
    public void testFind() {
        
        int key = "Five".hashCode();
        
        BSTNode node = new BSTNode("Five", key, new URLList("Five", null));
        
        BinarySearchTree bst = new BinarySearchTree();
        
        bst.insert(node);
        
        BSTNode expResult = node;
        BSTNode result = bst.find(key, node);
        
        assertEquals(expResult, result);

    }

    /**
     * Test of insert method, of class BinarySearchTree.
     */
    @Test
    public void testInsert_BSTNode() {
        BSTNode newNode = new BSTNode("Five", "Five".hashCode(), new URLList("Five", null));
        BinarySearchTree bst = new BinarySearchTree();
        bst.insert(newNode);

        assertEquals(newNode, bst.root);
    }

    @Test
    public void testInsertSimple() {
        BinarySearchTree bst = new BinarySearchTree();

        BSTNode fNode = new BSTNode("f", "f".hashCode(), null);
        BSTNode aNode = new BSTNode("a", ("a").hashCode(), null);
        BSTNode zNode = new BSTNode("z", "z".hashCode(), null);
        bst.insert(fNode);
        bst.insert(aNode);
        bst.insert(zNode);

        assertNotNull(bst.root);
        assertEquals(bst.root.key, fNode.key);
        assertEquals(bst.root.leftChild.key, aNode.key);
        assertEquals(bst.root.rightChild.key, zNode.key);
    }

    @Test
    public void testInsertSimple2() {
        BinarySearchTree bst = new BinarySearchTree();

        BSTNode fNode = new BSTNode("f", "f".hashCode(), null);
        BSTNode aNode = new BSTNode("a", ("a").hashCode(), null);
        BSTNode zNode = new BSTNode("z", "z".hashCode(), null);
        BSTNode mNode = new BSTNode("m", "m".hashCode(), null);

        bst.insert(fNode);
        bst.insert(aNode);
        bst.insert(zNode);
        bst.insert(mNode);

        assertNotNull(bst.root);
        assertEquals(fNode.key, bst.root.key);
        assertEquals(aNode.key, bst.root.leftChild.key);
        assertEquals(zNode.key, bst.root.rightChild.key);
        assertEquals(mNode.key, bst.root.rightChild.leftChild.key);
    }

    @Test
    public void testFindSimple() {
        BinarySearchTree bst = new BinarySearchTree();

        BSTNode fNode = new BSTNode("f", "f".hashCode(), null);
        BSTNode aNode = new BSTNode("a", ("a").hashCode(), null);
        BSTNode zNode = new BSTNode("z", "z".hashCode(), null);
        BSTNode mNode = new BSTNode("m", "m".hashCode(), null);

        bst.insert(fNode);
        bst.insert(aNode);
        bst.insert(zNode);
        bst.insert(mNode);

        BSTNode foundNode = bst.find("m".hashCode(), bst.root);

        assertEquals(mNode, foundNode);
    }

    @Test
    public void testBuildSpeedSmall() {
        try {
            Searcher.buildBST("small.txt");
        } catch (IOException e) {
            System.out.println("Could not find the specified file.");
        }
    }

    @Test
    public void testBuildSpeedMedium() {
        try {
            Searcher.buildBST("medium.txt");
        } catch (IOException e) {
            System.out.println("Could not find the specified file.");
        }
    }

    @Test
    public void testBuildSpeedLarge() {
        try {
            Searcher.buildBST("large.txt");
        } catch (IOException e) {
            System.out.println("Could not find the specified file.");
        }
    }
}

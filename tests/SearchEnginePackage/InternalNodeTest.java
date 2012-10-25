/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchEnginePackage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bones
 */
public class InternalNodeTest {
    
    public InternalNodeTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insert method, of class InternalNode.
     */
    @Test
    public void testInsert() {
        System.out.println("insert");
        int key = 0;
        URLList urlList = null;
        BPTNodeInfo promotionNode = null;
        InternalNode instance = new InternalNode();
        boolean expResult = false;
        boolean result = instance.insert(key, urlList, promotionNode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

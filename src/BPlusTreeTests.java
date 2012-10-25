import static org.junit.Assert.*;

import org.junit.Test;


public class BPlusTreeTests {
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
	public void testInsert() {
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

}

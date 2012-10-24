import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;


public class BinarySearchTreeTests {

	@Test
	public void testInsertIntoEmpty()
	{
		BinarySearchTree bst = new BinarySearchTree();
		
		BSTNode newNode = new BSTNode("a", ("a").hashCode(), null);
		bst.insert(newNode);
		
		assertNotNull(bst.root);
		assertEquals(bst.root.key, newNode.key);
	}
	
	@Test
	public void testInsertSimple()
	{
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
	public void testInsertSimple2()
	{
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
	public void testFind()
	{
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
	public void testBuildSpeedSmall()
	{
		try 
		{
			Searcher.buildBST("small.txt");
		}
		catch (IOException e) {
			System.out.println("Could not find the specified file.");
		}
	}
	
	@Test
	public void testBuildSpeedMedium()
	{
		try 
		{
			Searcher.buildBST("medium.txt");
		}
		catch (IOException e) {
			System.out.println("Could not find the specified file.");
		}
	}
	
	@Test
	public void testBuildSpeedLarge()
	{
		try 
		{
			Searcher.buildBST("large.txt");
		}
		catch (IOException e) {
			System.out.println("Could not find the specified file.");
		}
	}
}

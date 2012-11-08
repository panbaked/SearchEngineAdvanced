package SearchEnginePackage;


class BSTNode
{
	String word;
	int key;
	URLList value;
	BSTNode leftChild;
	BSTNode rightChild;
	
	public BSTNode(String word, int key, URLList value)
	{
		this.word = word;
		this.key = key;
		this.value = value;
	}
	
	
}
//This binary search tree uses hashcodes to determine the nodes
public class BinarySearchTree 
{
	BSTNode root;
	
	public BinarySearchTree()
	{
		root = null;
	}
	//Searches recursively through the tree to find a given element
	public BSTNode find(int key, BSTNode node)
	{
		if(node == null)
			return null;
		
		if(key == node.key)
			return node;
		else if(key < node.key)
			return find(key, node.leftChild);
		else
			return find(key, node.rightChild);
		
	}
	public void insert(BSTNode newNode)
	{
            if(root == null)
                root = newNode;
            else
                insert(root, newNode);

	}
	public void insert(BSTNode treeNode, BSTNode newNode)
	{
		if(newNode.key == treeNode.key)
		{
			URLList newURL = new URLList(newNode.value.url, treeNode.value);
			treeNode.value = newURL; //insert at the beginning
		}
		else if(newNode.key < treeNode.key)
			if(treeNode.leftChild == null)
				treeNode.leftChild = newNode;
			else
				insert(treeNode.leftChild, newNode);
		else
			if(treeNode.rightChild == null)
				treeNode.rightChild = newNode;
			else
				insert(treeNode.rightChild, newNode);
	}
}

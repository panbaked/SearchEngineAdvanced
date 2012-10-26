package SearchEnginePackage;

//used to keep track of promotions which need to float up towards the root upon insertion
class BPTNodeInfo
{
    BPTNode node;
    int key;
}
abstract class BPTNode
{
    int nKeys; //how many keys we're storing
    int[] keys = new int[BPlusTree.N+1]; //we store one more so we can "fill up" before splitting

    //returns true if a promotion was necessary
    abstract boolean insert(int key, URLList urlList, BPTNodeInfo promotionNode);
}
class InternalNode extends BPTNode
{
    BPTNode[] children = new BPTNode[BPlusTree.N+2]; //always contains one more than the keys set, can be either an InternalNode or LeafNode

    @Override
    public boolean insert(int key, URLList urlList, BPTNodeInfo promotionNode) 
    {
        //in this method i use toArray a bunch, I'm not sure of the overhead of this, but these arrays are from 2-4 in length
        //so the damage shouldn't be too big

        int i; //keeps track of which child we navigate through to insert the new entry
        //Cycle through the keys and see if we are smaller than one of them, this is where we are directed to the child node
        for(i = 0; i < nKeys; i++)
        {
            if(key <= keys[i])
                break; //We have found our path, it is to the left of key i
        }
        //if we did not find a path to the left of any keys it is all the way to the right
        //note that we always have 1 more child than keys so this never fails

        //we then navigate through the i-th child and check if it required a promote
        //this is the recursion step which eventually reaches the end of the tree
        //if a promote is required this floats up towards the root
        boolean promotionRequired = children[i].insert(key, urlList, promotionNode);

        //two options, no promotion (trivial) and promotion, when a promotion happens a key is moved from a child up to a parent
        //this parent tries to accommodate the key, however if it is full it splits
        //splitting involves find a median among the keys, the keys lower than the median are placed into a new left node
        //and the keys higher than the median are placed into a new right node, the median key is placed into the parent's keys (promotion)
        //this promotion can cause the parent to split, and so on until the root is eventually split and the tree grows in height by 1

        if(promotionRequired)
        {
            //we have the i-th child index
            //the key is inserted at position i
            //the child is inserted at position i+1 (the i+1 child belongs to the i key)
            //so we shift every entry > i up to make space for the new key

            for(int j = nKeys; j > i; j--)
            {
                keys[j] = keys[j-1]; //shift key up
                children[j+1] = children[j]; //shift child up
            }

            keys[i] = promotionNode.key;
            children[i+1] = promotionNode.node;
            nKeys++;

            //now that we have grown we can check and see if we are big enough to require a split
            if(nKeys <= BPlusTree.N)
            {
                    //no splitting required
                    return false;
            }
            else
            {
                //we are full, so we must split into two nodes and promote our median key
                //the middle key (N/2) is promoted
                promotionNode.key = keys[BPlusTree.N/2];

                //Create the new node and fill it with keys > N/2+1
                InternalNode newNode = new InternalNode();

                promotionNode.node = newNode;
                //the next to iterators are stored outside the for so we can add in the final child 
                int j; //index into this node's arrays
                int k = 0; //this is our index into the newNode's array
                for(j = BPlusTree.N/2+1; j <= BPlusTree.N; j++)
                {
                        newNode.keys[k] = keys[j];
                        newNode.children[k] = children[j];
                        k++;
                }
                //and don't forget to add in the last child
                newNode.children[k] = children[j]; 
                newNode.nKeys = BPlusTree.N/2;

                nKeys = BPlusTree.N/2;
                return true; //return to our parent that a promotion was required
            }

        }

        return false; //no promotion required we succeeded

    }
}

class LeafNode extends BPTNode
{
    URLList[] data = new URLList[BPlusTree.N+1];

    LeafNode sibling; //the leaf to the right, allows for quick sequential access

    public LeafNode()
    {

    }

    public boolean insert(int key, URLList url, BPTNodeInfo promotionNode)
    {
        int i, j;

        if(nKeys > 0)
        {
            //Cycle through the keys and find if the key is smaller than another
            for(i = 0; i < nKeys; i++)
            {
                if(key < keys[i])
                {
                    break; //stop here we will insert at position i
                }
                else if(key == keys[i]) //we have a duplicate
                {
                    URLList existingList = BPlusTree.find(data[i], url.url); //we check for an existing entry
                    if(existingList == null) //if one doesn't exist we add this url
                    {
                        url.next = data[i];
                        data[i] = url;
                    }
                    
                    return false; //no promotion is required
                }
            }
        }
        else
        {
            //we're empty so just add it
            i = 0;
        }

        //now that we have the new position we insert it by shifting the keys up
        for(j = nKeys; j > i; j--)
        {
            keys[j] = keys[j-1];
            data[j] = data[j-1];
        }

        
        keys[i] = key;
        url.next = data[i]; //insert the urllist entry at the start of the list
        data[i] = url;
        nKeys++;

        if(nKeys <= BPlusTree.N) //are we a valid size
        {
            return false; //no promotion required
        }
        else
        {
            //We're full so split into two and float up the middle key to the parent

            //A new leaf is now required so we create that and make it our sibling
            LeafNode newNode = new LeafNode();
            newNode.sibling = sibling; //insert the new node into the sibling list
            sibling = newNode;

            //now we move all of the keys and data higher than N/2+1 to the new node
            int k = 0; //our index into the new node's arrays
            for(j = BPlusTree.N/2+1; j <= BPlusTree.N; j++)
            {
                newNode.keys[k] = keys[j];
                newNode.data[k] = data[j];
                k++;
            }

            newNode.nKeys = k;

            //setup the node to be promoted (sent to the parent)
            promotionNode.key = keys[BPlusTree.N/2];
            promotionNode.node = newNode;

            nKeys = BPlusTree.N/2; 

            return true; //we promoted successfully
        }
    }
}
public class BPlusTree {

    BPTNode root;
    LeafNode firstLeaf; //so we can go through the items sequentially we store this reference
    static final int N = 2;

    public URLList find(int key)
    {
        return find(key, root);
    }

    public URLList find(int key, BPTNode node)
    {
        if(node == null)
            return null;
        //we step down through the tree until we reach a leaf
        //each step if we are less than or equal to the key then we choose the left path
        int i;
        for(i = 0; i < node.nKeys; i++)
        {
            if(key <= node.keys[i])
            {
                break; //we found our child to navigate through
            }
        }

        //here we test if we are an internal node, if so we continue our recursion
        if(node instanceof InternalNode)
        {
            return find(key, ((InternalNode)node).children[i]);
        }
        else if(node instanceof LeafNode)
        {
            LeafNode leafNode = (LeafNode)node;

            for(int j = 0; j < node.nKeys; j++)
            {
                if(key == leafNode.keys[j])
                {
                    return ((LeafNode)node).data[i]; //we found it
                }
            }
        }

        return null;
    }

    public void insert(int key, URLList urlList)
    {
        BPTNodeInfo promotionNode = new BPTNodeInfo(); //we need to store a node used while inserting to "carry" promotion of a node higher up

        if(root == null)
        {
            //empty try so we just make a leaf and place our value there
            firstLeaf = new LeafNode();
            root = firstLeaf;
        }

        if(!root.insert(key, urlList, promotionNode))
        {
            //the data fit into the tree perfectly
        }
        else
        {
            //a promotion has propogated all the way to the root, so we split the root
            InternalNode newRoot = new InternalNode();
            newRoot.children[0] = root; //move the current root down as the left child
            newRoot.keys[0] = promotionNode.key; //the propagated key is our new top key
            newRoot.children[1] = promotionNode.node; //and the right child is the node that was promoted

            newRoot.nKeys = 1;

            root = newRoot;
        }
    }

    public static URLList find(URLList l, String url)
    {
        while (l != null) 
        {
            if (l.url.equals (url)) 
            {
                return l;
            }
            l = l.next;
        }
        return null;
    }
}

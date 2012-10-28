package SearchEnginePackage;

//This class contains a linked list node for the url linked list.
//some more
public class URLList {
    String url;
    URLList next;

    URLList(String _url, URLList n)
    {
            url = _url;
            next = n;
    }
}

class URLListIndexed 
{
    int urlIndex;
    URLListIndexed next;
    
    public URLListIndexed(int urlIndex, URLListIndexed next)
    {
        this.urlIndex = urlIndex;
        this.next = next;
    }
}
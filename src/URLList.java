//This class contains a linked list node for the url linked list.
public class URLList {
	String url;
	URLList next;
	
	URLList(String _url, URLList n)
	{
		url = _url;
		next = n;
	}
}
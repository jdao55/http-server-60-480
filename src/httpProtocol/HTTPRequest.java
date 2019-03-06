package httpProtocol;

import java.util.Map;

public class HTTPRequest {
	private String method;
	private String URL;
	private String methodLine;
	private String query;
	private String protocol;
	private Map<String,String> header;
	private String body;
	
	public HTTPRequest(String info,Map<String,String> header, String _body) {
		methodLine=info;
		String[] tokens = info.split("\\s+");
		method = tokens[0];
		String [] urlParts= tokens[1].split("\\?");

		URL = urlParts[0];
		if (urlParts.length>1)
			query = urlParts[1];
		else
			query=null;

		protocol = tokens[2];
		this.header = header;
		body=_body;
		
	}
	
	public String getMethod() {
		return method;
	}
	public String getQuery() {return query;}

	public String getURL() {
		return URL;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public String getBody(){return body;}
	
	public Map<String,String> getHeader() {
		return header;
	}
	public String toString()
	{
		String ret=methodLine+"\r\n";
		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			ret += key + ": " + value + "\r\n";
		}
		ret+="\r\n";
		ret+=body;
		return ret;
	}
	//get boudary for mulitparts form data
	public String getContentBoundary()
	{
		String ctype =header.get("Content-Type");
		if (ctype==null)
			return "";
		String[] splitsemi = ctype.split(";");
		for (String a: splitsemi)
		{
			if (a.contains("boundary"))
			{
				return a.split("=")[1];
			}
		}
		return "";
	}
}

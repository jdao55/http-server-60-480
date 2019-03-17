package httpProtocol;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HTTPRequest {
	private String method;
	private String URL;
	private String methodLine;
	private String query;
	private String protocol;
	private Map<String,String> header;
	private byte[] body;
	
	public HTTPRequest(String info,Map<String,String> header, byte[] _body) {
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

	public byte[] getBody(){return body;}

	public String getBodyString(Charset encoding)   {
		if  (body!=null) {
			return new String(body, encoding);
		}
		else return "";
	}

	public Map<String,String> getHeader() {
		return header;
	}

	public String getHeader(String k) {
		return header.get(k);
	}

	public String toString() {
		String ret=methodLine+"\r\n";
		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			ret += key + ": " + value + "\r\n";
		}
		ret+="\r\n";
		ret+=getBodyString(StandardCharsets.UTF_8);
		return ret;
	}

	public byte[] toBytes()
	{
		String ret=methodLine+"\r\n";
		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			ret += key + ": " + value + "\r\n";
		}
		ret+="\r\n";
		byte[] head=ret.getBytes();
		if(body!=null) {
			byte[] res = new byte[ret.length() + body.length];
			System.arraycopy(head, 0, res, 0, head.length);
			System.arraycopy(body, 0, res, head.length, body.length);
			return res;
		}
		else return head;
	}
	//get boundary for mulitparts form data
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

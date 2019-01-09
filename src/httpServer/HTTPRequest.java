import java.util.Map;

public class HTTPRequest {
	private String method;
	private String URL;
	private String protocol;
	private Map<String,String> header;
	
	public HTTPRequest(String info,Map<String,String> header) {
		String[] tokens = info.split("\\s+");
		method = tokens[0];
		URL = tokens[1];
		protocol = tokens[2];
		this.header = header;
		
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getURL() {
		return URL;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public Map<String,String> getHeader() {
		return header;
	}
}

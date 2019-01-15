package httpUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.TimeZone;

public class HttpsResponse {
  private int httpVersionMajor;
  private int httpVersionMinor;
  private int statusCode;
  private String body;
  private HashMap<String, String> HeaderMap;

  public HttpsResponse(String _body, int _statusCode){
    body=_body;
    HeaderMap.put("Content-Length", _body.Length());
    HeaderMap.put("Date", getServerTime())
        statusCode = _statusCode;

  }
  //TODO add methods to add headers to hash map
  public void  addHeader(String key, String value)
  {
    HeaderMap.put(key, value);
  }

  public byte[] getHeaderBytes()
  {
    String resp = String.Format("HTTP/%d.%d %s\r\n", 1,1 statusCode, getStatusString(statusCode));
    for (Map.Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      resp+=key+": "+value+"\r\n";
    }
    resp+="\r\n"+body;
    return resp.getBytes();
  }

  private String getStatusString(int status)
  {

    //TODO return correct strings
    return "OK";
  }

  private String getServerTime() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(calendar.getTime());
  }
}

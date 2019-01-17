package httpUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.TimeZone;

public class HTTPResponse {
    private int httpVersionMajor;
    private int httpVersionMinor;
    private int statusCode;
    private byte[] body;
    private HashMap<String, String> headerMap;

    public HTTPResponse(byte[] _body, int _statusCode) {
        body = _body;
        httpVersionMajor = 1;
        httpVersionMinor = 1;
        headerMap = new HashMap<String, String>();
        headerMap.put("Content-Length", Integer.toString(body.length));
        headerMap.put("Date", getServerTime());
        statusCode = _statusCode;

    }


    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }
    public String getHeader(String key) {
        return headerMap.get(key);
    }

    public byte[] getReponseBytes() {
        //create header
        String respHeader = String.format("HTTP/%d.%d %d %s\r\n", httpVersionMajor, httpVersionMinor, statusCode, getStatusString(statusCode));
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            respHeader += key + ": " + value + "\r\n";
        }
        respHeader += "\r\n";

        //create header byte array from header and body
        byte[] respHeaderBytes=respHeader.getBytes();
        byte[] resp= new byte[respHeaderBytes.length+body.length];
        System.arraycopy(respHeaderBytes,0, resp,0, respHeaderBytes.length);
        System.arraycopy(body,0, resp, respHeaderBytes.length, body.length);
        return resp;
    }



    public byte[] getBody() {
        return body;
    }
    public void setBody(byte[] _b) {
        body = _b;
    }

    public void setHttpVersion(int major, int minor) {
        httpVersionMajor = major;
        httpVersionMinor = minor;
    }
    public String getHttpVersion() {
        return httpVersionMajor+"."+httpVersionMinor;
    }

    public void setStatusCode(int code){statusCode=code;}
    public int getStatusCode(){return statusCode;}
    private String getStatusString(int status) {

        //TODO return correct strings
        switch (status) {
            case 200:
                return "OK";
            case 404:
                return "NOT FOUND";
        }
        return "";
    }

    //get current date in html format
    private String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
}

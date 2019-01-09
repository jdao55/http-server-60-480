package httpUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class HttpsResponse {
    private int httpVersionMajor;
    private int httpVersionMinor;
    private int statusCode;
    private byte[] body;
    private HashMap<String, String> HeaderMap;

    //TODO finish constuctors
    public HttpsResponse(String body){

    }
    //TODO add methods to add headers to hash map


    private String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
}

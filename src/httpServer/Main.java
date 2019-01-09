package httpServer;
import javax.imageio.IIOException;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Main {
    private static ServerSocket server;
    private static int port= 8081;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        server = new ServerSocket(port);

        while(true){
            Socket socket =  server.accept();
            BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //TODO parse and handle http requests

            socket.getOutputStream().write(Response("hello world!"));

            inStream.close();
            socket.close();

        }



    }
    public static byte[] Response(String text){
        String HtmlContent="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<p>"+text+"</p>\n"+
                "</body>\n"
                +"</html>";
        return ("HTTP/1.1 200 OK\r\n"+
                "Date: "+getServerTime()+"\r\n"+
                "Content-Type: text/html; charset=UTF-8\r\n"+
                "Content-Length: "+HtmlContent.length()+"\r\n\r\n"+
                HtmlContent).getBytes();

    }

    public static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
}

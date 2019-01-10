package httpServer;

import javax.imageio.IIOException;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import httpUtil.*;

public class Main {
    private static ServerSocket server;
    private static int port= 8081;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        server = new ServerSocket(port);
        Scanner in = new Scanner(System.in);

        while(true){
            Socket socket =  server.accept();

            BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
            String input,info;
            Map<String,String> header = new HashMap<String,String>();
            
            info = inStream.readLine();
            input = inStream.readLine();
            while(input.length()>0) {         
            	String[] tokens = input.split(":",2);                               		
                header.put(tokens[0], tokens[1]);
                input = inStream.readLine();
            }
                  
            HTTPRequest request = new HTTPRequest(info,header);
                                 
            if (request.getMethod().equals("GET") && request.getURL().equals("/")) {
            	System.out.println("Enter message: ");
                String message = in.next();
                socket.getOutputStream().write(Response(message));
            }

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

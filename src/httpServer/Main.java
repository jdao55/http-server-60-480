package httpServer;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import httpUtil.*;
import util.*;

public class Main {
    private static ServerSocket server;
    private static int port= 8081;

    //TODO add site rootPath, and port number as commandline args
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        server = new ServerSocket(port);
        Scanner in = new Scanner(System.in);
        FileUtil.rootPath=System.getProperty("user.dir");

        while(true){
            Socket socket =  server.accept();

            BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
            String input,info;
            Map<String,String> header = new HashMap<String,String>();

            //read Request
            info = inStream.readLine();
            input = inStream.readLine();
            while(input.length()>0) {         
            	String[] tokens = input.split(":",2);                               		
                header.put(tokens[0], tokens[1]);
                input = inStream.readLine();
            }
                  
            HTTPRequest request = new HTTPRequest(info,header);

            //process request
            handleRequest(request, socket);

            inStream.close();
            socket.close();
        }
   


    }

    //handle incomming requests
    private static void handleRequest(HTTPRequest req, Socket soc) {
        try {
            switch (req.getMethod()) {
                case "GET":
                    handleGet(req, soc);
                    break;
            }
        }
        catch (IOException e)
        {}
    }

    //handle get request
    private static void handleGet(HTTPRequest req, Socket soc) throws IOException
    {
        try {
            String body = FileUtil.readFile(req.getURL(), Charset.forName("UTF-8"));
            HTTPResponse resp = new HTTPResponse(body, 200);
            soc.getOutputStream().write(resp.getHeaderBytes());
        }
        catch (FileNotFoundException e)
        {
            HTTPResponse resp = new HTTPResponse("<h>404 Not Found</h>", 404);
            soc.getOutputStream().write(resp.getHeaderBytes());
        }

    }
}

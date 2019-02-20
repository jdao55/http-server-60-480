package httpServer;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import httpProtocol.*;
import util.*;

public class Main {
    private static ServerSocket server;
    private static int port= 8081;


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        FileUtil.rootPath=System.getProperty("user.dir");

        try {
            parseCliArgs(args);
            server = new ServerSocket(port);
            Scanner in = new Scanner(System.in);

            while (true) {
                Socket socket = server.accept();

                BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String input, info;
                Map<String, String> header = new HashMap<String, String>();

                //read Request
                info = inStream.readLine();
                if (info == null){
                    inStream.close();
                    socket.close();
                    continue;
                }
                System.out.println(info);
                input = inStream.readLine();
                while (input.length() > 0) {
                    if (input != null && input.length()>0) {
                        String[] tokens = input.split(":", 2);
                        header.put(tokens[0].trim(), tokens[1].trim());
                        input = inStream.readLine();
                    }
                }

                String reqBody="";
                int bodySize=0;
                if (header.containsKey("Content-Length")
                        &&((bodySize= Integer.parseInt(header.get("Content-Length")))>0))                {
                    if (bodySize>0){
                        char[] buffer= new char[bodySize];
                        inStream.read(buffer,0,bodySize);
                        reqBody=new String(buffer);
                    }
                }

                HTTPRequest request = new HTTPRequest(info, header, reqBody);
                //process request
                RequestHandler.handleRequest(request, socket);

                inStream.close();
                socket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void parseCliArgs(String[] args) throws Exception
    {
        for(int i=0;i< args.length;i++)
        {
            if (args[i].matches("(-d)|(-dir)"))
            {
                if (i+1==args.length)
                    throw new Exception("Invalid args");
                FileUtil.rootPath=args[++i];
            }
            if (args[i].matches("(-p)|(-port)"))
            {
                if (i+1==args.length)
                    throw new Exception("Invalid args");
                port = Integer.parseInt(args[++i]);
            }

        }
    }
}

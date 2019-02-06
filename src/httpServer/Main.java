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

    //TODO add site rootPath, and port number as commandline args
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
                input = inStream.readLine();
                System.out.println(info);
                if (input != null && input.length()>0) {
                    while (input.length() > 0) {
                        if (input != null && input.length()>0) {
                            String[] tokens = input.split(":", 2);
                            header.put(tokens[0].trim(), tokens[1].trim());

                            input = inStream.readLine();
                        }
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
                if (info == null){
                    inStream.close();
                    socket.close();
                    continue;
                }
                HTTPRequest request = new HTTPRequest(info, header, reqBody);

                //process request
                handleRequest(request, socket);

                inStream.close();
                socket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
   


    }

    //handle incomming requests
    private static void handleRequest(HTTPRequest req, Socket soc) {
        try {
            switch (req.getMethod()) {
                case "GET":
                    handleGet(req, soc);
                    break;
                case "POST":
                    handlePost(req, soc);
            }
        }
        catch (IOException e)
        {}
    }

    //handle get request
    private static void handleGet(HTTPRequest req, Socket soc) throws IOException
    {
        try {
            //redirect '/' to '/index.html'
            String url;
            byte [] body;
            if ((url=req.getURL()).equals("/"))
                url="/index.html";
            //execute .out file
            if(url.matches(".*\\.exe$") )
            {
                String qu=req.getQuery()==null?" ":req.getQuery();
                body = util.FileUtil.excecuteProgram(url, qu, qu);
            }
            if(url.matches(".*\\.py$"))
            {
                String qu=req.getQuery()==null?" ":req.getQuery();
                body = util.FileUtil.excecutePyProgram(url, qu, qu);
            }
            //load load
            else {
                body = FileUtil.readFileBytes(url);
            }
            //send response
            HTTPResponse resp = new HTTPResponse(body, 200);
            resp.addHeader("Content-Type", "charset=utf-8");
            soc.getOutputStream().write(resp.getReponseBytes());
        }
        catch (FileNotFoundException e)
        {
            HTTPResponse resp = new HTTPResponse("<h>404 Not Found</h>".getBytes(), 404);
            soc.getOutputStream().write(resp.getReponseBytes());
        }
        catch (Exception e)
        {
            HTTPResponse resp = new HTTPResponse("<h>Server Error</h>".getBytes(), 500);
            soc.getOutputStream().write(resp.getReponseBytes());
        }

    }

    private static void handlePost(HTTPRequest req, Socket soc) throws IOException
    {
        try {
            //redirect '/' to '/index.html'
            String uri=req.getURL();
            byte [] body;
            if(uri.matches(".*\\.exe$") || uri.matches(".*\\.py$"))
            {
                body = util.FileUtil.excecuteProgram(uri, req.getBody(), req.getBody());
            }
            if(uri.matches(".*\\.py$"))
            {
                body = util.FileUtil.excecutePyProgram(uri, req.getBody(), req.getBody());
            }
            //load load
            else {
                body = FileUtil.readFileBytes(uri);
            }
            //send response
            HTTPResponse resp = new HTTPResponse(body, 200);
            soc.getOutputStream().write(resp.getReponseBytes());
        }
        catch (FileNotFoundException e)
        {
            HTTPResponse resp = new HTTPResponse("<h>404 Not Found</h>".getBytes(), 404);
            soc.getOutputStream().write(resp.getReponseBytes());
        }
        catch (Exception e)
        {
            HTTPResponse resp = new HTTPResponse("<h>Server Error</h>".getBytes(), 500);
            soc.getOutputStream().write(resp.getReponseBytes());
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

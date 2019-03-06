package httpServer;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import httpProtocol.*;
import util.*;
public class RequestHandler {
    //handle incoming requests
    public static void handleRequest(HTTPRequest req, Socket soc) throws Exception {
        try {
            switch (req.getMethod()) {
                case "GET":
                    if (loadStatic(req, soc))
                        return;
                    if (loadDynamic(req, soc))
                        return;
                    handle404(soc);
                    break;
                case "POST":
                    handleUpload(req, soc);

            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }


    private static boolean loadDynamic(HTTPRequest req, Socket soc) throws Exception {
        String url;
        byte[] body;
        if ((url = req.getURL()).equals("/"))
            url = "/index.html";
        try {
            body = util.FileUtil.excecuteProgram("dynamic" + url, "", req.toString());
            HTTPResponse resp = new HTTPResponse(body, 200);
            soc.getOutputStream().write(resp.getReponseBytes());
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }


    private static boolean loadStatic(HTTPRequest req, Socket soc) throws Exception {
        String url;
        byte[] body;
        if ((url = req.getURL()).equals("/"))
            url = "static/index.html";
        else {
            url = "static" + url;
        }
        try {
            body = FileUtil.readFileBytes(url);
            //send response
            HTTPResponse resp = new HTTPResponse(body, 200);
            soc.getOutputStream().write(resp.getReponseBytes());
            return true;
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
    }

    private static void handle404(Socket soc) throws  Exception
    {
        String message="404 not found";
        HTTPResponse resp = new HTTPResponse(message.getBytes(), 404);
        soc.getOutputStream().write(resp.getReponseBytes());
    }

    private static void handleUpload(HTTPRequest req, Socket soc) throws Exception
    {
        String bound=req.getContentBoundary();
        String body=req.getBody();
        String[] form= body.split("-*"+bound+"-*");
        String files="";
        for(String s: form)
        {
            String temp =uploadfile(s);
            if(temp!=null)
            {
                files+="temp/"+temp+"\n";
            }
        }

        byte[] retbody =FileUtil.excecuteProgram("dynamic/upload.py","", "temp/");
        HTTPResponse resp = new HTTPResponse(retbody, 200);
        soc.getOutputStream().write(resp.getReponseBytes());

    }

    private static String uploadfile(String form_part) throws Exception{
        String[] list= form_part.split("\r\n\r\n");
        if(list.length<2)
            return null;
        String filename =getFilename(list[0]);
        FileUtil.writeTempFile(filename, list[1]);
        return filename;
    }

    private static String getFilename(String s) throws Exception
    {
        String[] lines=s.split("\r\n");
        for(String a:lines) {
            for(String b:a.split(";"))
            {
                if (b.matches("\\s*filename=.*"))
                {
                    String name=b.split("=")[1].trim();
                    name =name.replace("\"", "");
                    return name;
                }
            }
        }
        return null;

    }
}
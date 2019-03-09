package httpServer;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import httpProtocol.*;
import util.*;

import javax.xml.stream.events.StartDocument;

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
        String body=new String(req.getBody(), StandardCharsets.ISO_8859_1);
        String[] form= body.split("--"+bound);
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
        int bodySplit=form_part.indexOf("\r\n\r\n");
        if(bodySplit<0 || bodySplit==form_part.length())
            return null;
        String header=form_part.substring(0,bodySplit);
        String body= form_part.substring(bodySplit+4);

        String filename =getFilename(header);
        body=body.substring(0,body.length()-2);
        byte[] body_bytes= body.getBytes(StandardCharsets.ISO_8859_1);
        FileUtil.writeTempFile(filename, body_bytes);
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

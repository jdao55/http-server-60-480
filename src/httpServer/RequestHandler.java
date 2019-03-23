package httpServer;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


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
        if(url.matches(".login\\.py.*"))
            return handleLogin(req,soc);
        if(url.matches(".logout\\.py.*"))
            return handleLogout(req,soc);
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
        String cookie_id="-1";
        int i=0;
        if(req.getHeader().containsKey("Cookie"))
        {
            cookie_id= (req.getHeader("Cookie").split("=")[1].trim());
        }
        String fname;
        for(String s: form)
        {
            if (i==0)
            {
                fname="source"+cookie_id+".c";
            }
            else
                fname="binary"+cookie_id+".out";
            //String temp =uploadfile(s);
            if(uploadfile(s,fname)!=null)
                i++;
        }

        byte[] retbody =FileUtil.excecuteProgram("dynamic/upload.py","","temp/");
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

    private static String uploadfile(String form_part, String fname) throws Exception{
        int bodySplit=form_part.indexOf("\r\n\r\n");
        if(bodySplit<0 || bodySplit==form_part.length())
            return null;
        String body= form_part.substring(bodySplit+4);

        body=body.substring(0,body.length()-2);
        byte[] body_bytes= body.getBytes(StandardCharsets.ISO_8859_1);
        FileUtil.writeTempFile(fname, body_bytes);
        return fname;
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

    private static boolean handleLogin(HTTPRequest req, Socket soc)
    {
        byte[] body;

        try {
            body = util.FileUtil.excecuteProgram("dynamic/login.py" , "", req.getQuery());
            soc.getOutputStream().write(body);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    private static boolean handleLogout(HTTPRequest req, Socket soc) throws  Exception{
        String body="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Logged Out</h1>\n" +
                "<a href=\"index.html\">Back</a>\n" +
                "</body>\n" +
                "</html>";
        try {
            HTTPResponse resp = new HTTPResponse(body.getBytes(), 200);
            resp.addHeader("Set-Cookie", "id=1; Expires=Fri, 22 Mar 2019 20:42:43 GMT");
            soc.getOutputStream().write(resp.getReponseBytes());
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

}

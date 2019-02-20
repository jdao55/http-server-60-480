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
                case "POST":
                    if (!loadDynamic(req, soc))
                        loadStatic(req, soc);
            }
        } catch (IOException e) {
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


    private static void loadStatic(HTTPRequest req, Socket soc) throws Exception {
        String url;
        byte[] body;
        if ((url = req.getURL()).equals("/"))
            url = "static/index.html";
        else {
            url = "static" + url;
        }
        body = FileUtil.readFileBytes(url);
        //send response
        HTTPResponse resp = new HTTPResponse(body, 200);
        soc.getOutputStream().write(resp.getReponseBytes());
    }

}
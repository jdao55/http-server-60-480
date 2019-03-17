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

        FileUtil.rootPath = System.getProperty("user.dir");

        try {
            parseCliArgs(args);
            server = new ServerSocket(port);
            Scanner in = new Scanner(System.in);

            while (true) {
                Socket socket = server.accept();
                RequestThread rt= new RequestThread(socket);
                Thread object = new Thread(rt);
                object.start();

            }
        } catch (Exception e) {
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

    public static String readHeader(InputStream is) throws Exception{
        StringBuilder header = new StringBuilder();
        header.append((char)is.read());
        header.append((char)is.read());
        header.append((char)is.read());
        header.append((char)is.read());
        while(isHeader(header)){
            header.append((char)is.read());
        }
        return header.toString();

    }
    private static boolean isHeader( StringBuilder h)
    {
        String last4chars=h.toString().substring(h.length()-4);
        return !(last4chars.equals("\r\n\r\n"));}

}

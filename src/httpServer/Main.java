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

                InputStream is=socket.getInputStream();
                String headerString=readHeader(is);
                BufferedReader header_stream = new BufferedReader(new StringReader(headerString));

                String input, info;
                Map<String, String> header = new HashMap<String, String>();

                //read Request
                info = header_stream.readLine();
                if (info == null) {
                    header_stream.close();
                    socket.close();
                    continue;
                }
                System.out.println(info);
                input = header_stream.readLine();
                while (input.length() > 0) {
                    if (input != null && input.length() > 0) {
                        String[] tokens = input.split(":", 2);
                        header.put(tokens[0].trim(), tokens[1].trim());
                        input = header_stream.readLine();
                    }
                }
                header_stream.close();
                byte[] reqBody = null;
                int bodySize = 0;
                if (header.containsKey("Content-Length")
                        && ((bodySize = Integer.parseInt(header.get("Content-Length"))) > 0)) {
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                        int nRead;
                        int read_count=0;
                        byte[] data = new byte[90384];
                        nRead = is.read(data,  0, data.length);
                        buffer.write(data, 0, nRead);
                        read_count+=nRead;
                        while (read_count<bodySize) {
                            nRead = is.read(data,  0, data.length);
                            buffer.write(data, 0, nRead);
                            read_count+=nRead;
                        }
                        reqBody= buffer.toByteArray();

                }

                //
                // FileUtil.writeTempFile("test", reqBody);
                HTTPRequest request = new HTTPRequest(info, header, reqBody);
                //process request
                RequestHandler.handleRequest(request, socket);



                is.close();
                socket.close();
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

    private static String readHeader(InputStream is) throws Exception{
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

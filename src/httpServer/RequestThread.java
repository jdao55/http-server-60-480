package httpServer;

import httpProtocol.HTTPRequest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static httpServer.Main.readHeader;

public class RequestThread implements Runnable {

    Socket soc;
    public RequestThread(Socket s){soc=s;}
    public void run(){
        try {
            InputStream is = soc.getInputStream();

            String headerString = readHeader(is);
            BufferedReader header_stream = new BufferedReader(new StringReader(headerString));

            String input, info;
            Map<String, String> header = new HashMap<String, String>();

            //read Request
            info = header_stream.readLine();
            if (info == null) {
                header_stream.close();
                soc.close();
                return;
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
                int read_count = 0;
                byte[] data = new byte[90384];
                nRead = is.read(data, 0, data.length);
                buffer.write(data, 0, nRead);
                read_count += nRead;
                while (read_count < bodySize) {
                    nRead = is.read(data, 0, data.length);
                    buffer.write(data, 0, nRead);
                    read_count += nRead;
                }
                reqBody = buffer.toByteArray();

            }

            //
            // FileUtil.writeTempFile("test", reqBody);
            HTTPRequest request = new HTTPRequest(info, header, reqBody);
            //process request
            RequestHandler.handleRequest(request, soc);

            is.close();
            soc.close();
        }
        catch (Exception e)
        {
            try {
                soc.close();
            }
            catch (Exception e2)
            {
            }
        }


    }
}

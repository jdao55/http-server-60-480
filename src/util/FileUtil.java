package util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileUtil {
    //edit rootPath to change root path of site
    public static String rootPath = "";

    public static String readFile(String path, Charset encoding) throws IOException, FileNotFoundException {
        Path p = Paths.get(rootPath, path);
        if (!p.toFile().exists())
            throw new FileNotFoundException();

        byte[] encoded = Files.readAllBytes(p);
        return new String(encoded, encoding);
    }

    public static byte[] readFileBytes(String path) throws IOException, FileNotFoundException {
        Path p = Paths.get(rootPath, path);
        if (!p.toFile().exists())
            throw new FileNotFoundException();
        return Files.readAllBytes(p);
    }

    public static byte[] excecuteCProgram(String path, String args, String input) throws Exception {
        Path rootp = Paths.get(rootPath, path);
        if (!rootp.toFile().exists())
            throw new FileNotFoundException();
        path =path.charAt(0)=='/'?"."+path:path;
        Process p = Runtime.getRuntime().exec("gcc -O2 " +path, null, new File(rootPath));
        return excecuteProgram("./a.out", "", input);
     }


    public static byte[] excecuteProgram(String program, String args, String input) throws Exception {

        Process p = Runtime.getRuntime().exec(program +" " +args, null, new File(rootPath));

        OutputStream outStream = p.getOutputStream();
        outStream.write(input.getBytes());
        outStream.close();

        InputStream is = p.getInputStream();
        byte[] byteArr = new byte[100000];

        int size =is.read(byteArr);
        is.close();
        //resize byte array
        byte[] retarr= Arrays.copyOfRange(byteArr,0,size);
        String a= new String(retarr, StandardCharsets.UTF_16);

        return retarr;
    }
    public static byte[] excecutePyProgram(String path, String args, String input) throws Exception {
        Path rootp = Paths.get(rootPath, path);
        if (!rootp.toFile().exists())
            throw new FileNotFoundException();
        Process p = Runtime.getRuntime().exec("python3 ."+path +" " +args,null, new File(rootPath));

        OutputStream outStream = p.getOutputStream();
        outStream.write(input.getBytes());
        outStream.close();

        InputStream is = p.getInputStream();
        byte[] byteArr = new byte[100000];
        int size =is.read(byteArr);
        is.close();

        //resize byte array
        byte[] retarr= Arrays.copyOfRange(byteArr,0,size);
        return retarr;
    }


}

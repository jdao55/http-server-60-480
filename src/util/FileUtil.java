package util;

import java.io.*;
import java.nio.charset.Charset;
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

    public static byte[] excecuteProgram(String path, String args) throws Exception {
        Path rootp = Paths.get(rootPath, path);
        if (!rootp.toFile().exists())
            throw new FileNotFoundException();
        Process p = Runtime.getRuntime().exec(rootp.toString() +" " +args);
        InputStream is = p.getInputStream();
        byte[] byteArr = new byte[100000];
        int size =is.read(byteArr);
        //resize byte array
        byte[] retarr= Arrays.copyOfRange(byteArr,0,size);

        return retarr;
    }

}

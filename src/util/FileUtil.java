package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    //edit rootPath to change root path of site
    public static String rootPath="";

    public static String readFile(String path, Charset encoding) throws IOException, FileNotFoundException {
        Path p = Paths.get(rootPath, path);
        if(!p.toFile().exists())
            throw new FileNotFoundException();

        byte[] encoded = Files.readAllBytes(p);
        return new String(encoded, encoding);
    }

    public static byte[] readFileBytes(String path) throws IOException, FileNotFoundException {
        Path p = Paths.get(rootPath, path);
        if(!p.toFile().exists())
            throw new FileNotFoundException();
        return Files.readAllBytes(p);
    }

}

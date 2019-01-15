package Util;
//TODO add imports
//import;

import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

class FileUtil{

static String readFile(String path, Charset encoding)
  throws IOException
{
  byte[] encoded = Files.readAllBytes(Paths.get(path));
  return new String(encoded, encoding);
}

  static byte[] readFileBytes(String path) throws IOException
  {
    return Files.readAllBytes(Paths.get(path));
  }

}

package Util;
//TODO add imports
//import;

class FileUtil{

static String readFile(String path, Charset encoding)
  throws IOException
{
  byte[] encoded = Files.readAllBytes(Paths.get(path));
  return new String(encoded, encoding);
}

  static byte[] readFileBytes(String path)
  {
    return Files.readAllBytes(Paths.get(path));
  }

}

package copus.corenlp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WikiCorpusPrep {
  
  public static final String baseInputPath = "rawcorpus";
  public static final String baseOutputPath = "corpus";
  
  byte[] readBinaryFile(String aFileName) throws IOException {
    Path path = Paths.get(aFileName);
    return Files.readAllBytes(path);
  }
  
  void writeBinaryFile(byte[] aBytes, String aFileName) throws IOException {
    Path path = Paths.get(aFileName);
    Files.write(path, aBytes); //creates, overwrites
  }
  
  public static void main(String[] args) {
    File dir = new File(baseInputPath);
    String[] filesInDir = dir.list();
    for(String filename : filesInDir) {
      System.out.println("Processing: " + filename);
      Path path = Paths.get(baseInputPath + "/" + filename);
      byte[] bytes = null;
      try {
        bytes = Files.readAllBytes(path);
      } catch (IOException ex) {
        ex.printStackTrace();
        System.exit(1);
      }
      for(int i = 0; i < bytes.length; i++) {
        switch (bytes[i]) {
          case (byte)0x91:
          case (byte)0x92: bytes[i] = '\''; break;
          case (byte)0x93:
          case (byte)0x94: bytes[i] = '\"'; break;
          case (byte)0x96: bytes[i] = '-'; break;
        }
      }//for
      path = Paths.get(baseOutputPath + "/" + filename);
      try {
        Files.write(path, bytes); //creates, overwrites
      } catch (IOException ex) {
        ex.printStackTrace();
        System.exit(1);
      }
    }//outer for
  
  }//main()
}

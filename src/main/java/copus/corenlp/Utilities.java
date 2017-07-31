package copus.corenlp;

public class Utilities {
  
  public static String normalizeFilename(String originalName) {
    // if filename contains forward or backward slash[es] and/or
    // colon character, convert them to underscores. Convert also
    // to lower case.
    return originalName.replace(':', '_').replace('\\', '_').replace('/', '_').toLowerCase();
  }//normalizeFilename()
  
}//class copus.corenlp.copus.corenlp.Utilities

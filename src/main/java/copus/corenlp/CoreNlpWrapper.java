package copus.corenlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class CoreNlpWrapper {
  
  static Random random = new Random();
  
  private static StanfordCoreNLP getPipeline() {
    return CoreNlpPipeline.getPipeline();
  }
  
  private static ArrayList<String> lemmatizeString(String input) {
    ArrayList<String> lemmas = new ArrayList<>();
    Annotation annotation = getPipeline().process(input);
    for (CoreLabel token: annotation.get(CoreAnnotations.TokensAnnotation.class)) {
      String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
      //add this as a lemma if it is a word that starts with an alpha character
      if(Character.isLetter(lemma.charAt(0)))
        lemmas.add(lemma);
    }
    return lemmas;
  }//processString()
  
  public static String[] F(String filePath) {
    //extracts lemmatized words from a dataset
    //  input: path to the input file
    //  rval:  lemmas corresponding to all the words (starting with alpha chars)
    //           in the input file. Duplicates will be included.
    
    ArrayList<String> lemmas = new ArrayList<>();
    //read the file line-by-line and extract the lemmas
    try {
      BufferedReader in = new BufferedReader(new FileReader(filePath));
      String line = null;
      while ((line = in.readLine()) != null) {
        lemmas.addAll(lemmatizeString(line));
      }
    } catch (IOException iox) {
      iox.printStackTrace();
    }
    return lemmas.toArray(new String[lemmas.size()]);
  }//F()
  
  public static Document prepareText(String text) {
    return new Document(getPipeline().process(text));
  }//prepareText()
  
  public static void writeDocument(Document document, String path, String filename) {
    System.out.println(document);
    try {
      FileOutputStream fos = new FileOutputStream(new File(path + filename));
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(document);
      oos.close();
      fos.close();

    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Error initializing stream");
      e.printStackTrace();
    }
  }//writeDocument()
  
  public static Document readDocument(String path, String filename) {
    Document document = null;
    try {
      FileInputStream fis = new FileInputStream(new File(path + filename));
      ObjectInputStream ois = new ObjectInputStream(fis);
      document = (Document) ois.readObject();
      ois.close();
      fis.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Error initializing stream");
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    //System.out.println(document == null ? "[null document]" : document);
    return document;
  }//readDocument()
  
  public static double scoreDocument(Document document, Question question) {
    double score = 0.0;
    for(Token qtok : question.getTargetTerms()) {
      for(Token dtok : document.getTokens()) {
        String qword = qtok.getLemma(), dword = dtok.getLemma();
        if(qword.length() >= 3 && dword.length() >= 3 && qword.equals(dword))
          score += 1.0;
      }
    }
    return score;
  }//scoreDocument()
  
  public static double scoreSentence(Sentence sentence, Question question) {
    double score = 0.0;
    for(Token qtok : question.getTargetTerms()) {
      for(Token stok : sentence.getTokens()) {
        String qword = qtok.getLemma(), sword = stok.getLemma();
        if(qword.length() >= 3 && sword.length() >= 3 && qword.equals(sword))
          score += 1.0;
      }
    }
    return score;
  }//scoreSentence()
  
  public static String extractAnswer(Sentence sentence, Question question) {
    ArrayList<NamedEntityClass> objectives = new ArrayList<>();
    switch (question.getInterrogativeType()) {
      case WHO   :
      case WHAT  :
        objectives.add(NamedEntityClass.PERSON);
        objectives.add(NamedEntityClass.ORGANIZATION);
        break;
      case WHEN  :
        objectives.add(NamedEntityClass.DATE);
        objectives.add(NamedEntityClass.TIME);
        break;
      case WHERE :
        objectives.add(NamedEntityClass.LOCATION);
        break;
      default: return null;
    }
    NamedEntityClass[] targets = new NamedEntityClass[objectives.size()];
    objectives.toArray(targets);
    Token[] tokens = sentence.getTokens();
    int start = -1, end = -1;
    for (int i = 0; i < tokens.length; i++) {
      for (int j = 0; j < targets.length; j++) {
        if(tokens[i].getNec() == targets[j]) {
          if (start < 0) {
            start = i;
          }
          end = i;
        }
      }//inner for
    }//outer for
    if(start == -1)
      return null;
    StringBuffer sb = new StringBuffer();
    for(int i = start; i < end; i++)
      sb.append(tokens[i].getText() + " ");
    return sb.toString();
  }//extractAnswer()
  
  // main() used for unit testing only
  public static void main(String[] args) {

  }//main()
    
}//class CoreNlpWrapper

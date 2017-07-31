package copus.corenlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Document implements Serializable {
  private Sentence[] sentences;
  private Token[] tokens;
  
  public Document(Annotation doc) {
    ArrayList<Sentence> ss = new ArrayList<>();
    for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
     ss.add(new Sentence(sentence));
    }
    sentences = new Sentence[ss.size()];
    ss.toArray(sentences);
    
    ArrayList<Token> tokens = new ArrayList<>();
    for(Sentence s : sentences) {
      tokens.addAll(Arrays.asList(s.getTokens()));
    }
    this.tokens = new Token[tokens.size()];
    tokens.toArray(this.tokens);
  }//Document()
  
  public Sentence[] getSentences() {
    return sentences;
  }
  
  public Token[] getTokens() {
    return tokens;
  }
  
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    for(Sentence sentence : sentences)
      sb.append(sentence + "\n");
    return sb.toString();
  }//toString();
}//class Document

package copus.corenlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

import java.io.Serializable;
import java.util.ArrayList;

public class Sentence implements Serializable {
  private Token[] tokens;
  private String originalText;
  
  public Sentence(CoreMap sentence) {
    originalText = sentence.get(CoreAnnotations.TextAnnotation.class);
    ArrayList<Token> ts = new ArrayList<>();
    for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
      ts.add(new Token(token));
    }
    tokens = new Token[ts.size()];
    ts.toArray(tokens);
  }//Sentence
  
  public Token[] getTokens() {
    return tokens;
  }
  
  public String getOriginalText() {
    return originalText;
  }
  
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("<" + originalText + "> ");
    for(Token token : tokens)
      sb.append(token + " ");
    return sb.toString();
  }//toString();
}//Sentence

package copus.corenlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Question implements Serializable {
  private String text;
  private Annotation annotation;
  private Token[] tokens;
  
  private InterrogativeType interrogativeType;
  public Token[] targetTerms;
  
  public Question(String text) {
    this.text = text;
    annotation = CoreNlpPipeline.getPipeline().process(text);
    tokens = extractTokens();
    interrogativeType = extractInterrogativeType();
    targetTerms = extractTargetTerms();
  }//Question()
  
  private Token[] extractTokens() {
    List<CoreLabel> tokens = annotation.get(CoreAnnotations.TokensAnnotation.class);
    Token[] tokensArray = new Token[tokens.size()];
    int i = 0;
    for(CoreLabel tok : tokens)
      tokensArray[i++] = new Token(tok);
    return tokensArray;
  }//extractTokens()
  
  private InterrogativeType extractInterrogativeType() {
    //  given a token with pos == "WP" or pos == "WRB", extract its lemma, and
    //  use it to determine the question type.
    for(Token tok : tokens) {
      if (tok.getPos() == PartOfSpeech.WP || tok.getPos() == PartOfSpeech.WRB) {
        switch (tok.getLemma().toLowerCase()) {
          case "who"   : return InterrogativeType.WHO;
          case "what"  : return InterrogativeType.WHAT;
          case "when"  : return InterrogativeType.WHEN;
          case "where" : return InterrogativeType.WHERE;
        }//switch
        }
    }
    return InterrogativeType.UNKNOWN;
  }//extractInterrogativeType()
  
  private Token[] extractTargetTerms() {
    //collect the nouns and verbs from the question
    ArrayList<Token> targets = new ArrayList<>();
    for(Token tok : tokens)
      if(tok.isNoun() || tok.isVerb())
        targets.add(tok);
    return targets.toArray(new Token[targets.size()]);
  }//extractTargetTerms()
  
  public String getText() {
    return text;
  }
  
  public Token[] getTokens() {
    return tokens;
  }
  
  public InterrogativeType getInterrogativeType() {
    return interrogativeType;
  }
  
  public Token[] getTargetTerms() {
    return targetTerms;
  }
}//class Question

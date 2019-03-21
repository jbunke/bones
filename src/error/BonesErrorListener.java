package error;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class BonesErrorListener extends BaseErrorListener {
  private List<BonesError> errors;

  public BonesErrorListener() {
    errors = new ArrayList<>();
  }

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                          int line, int charPositionInLine, String msg,
                          RecognitionException e) {
    errors.add(new BonesError(BonesError.Category.SYNTAX,
            "(" + line + ":" + charPositionInLine + ") " + msg));
  }

  public void semanticError(String msg) {
    errors.add(new BonesError(BonesError.Category.SEMANTIC, msg));
  }

  public boolean hasError() {
    return !errors.isEmpty();
  }

  public List<BonesError> getErrors() {
    return errors;
  }
}

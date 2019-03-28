package error;

import execution.RuntimeErrorExit;
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
                          int line, int positionInLine, String msg,
                          RecognitionException e) {
    errors.add(new BonesError(BonesError.Category.SYNTAX,
            "(" + line + ":" + positionInLine + ") " + msg));
  }

  public void semanticError(String msg, int line, int positionInLine) {
    errors.add(new BonesError(BonesError.Category.SEMANTIC,
            "(" + line + ":" + positionInLine + ") " + msg));
  }

  public void runtimeError(String msg, boolean fatal, int exitCode,
                           int line, int positionInLine) {
    errors.add(new BonesError(BonesError.Category.RUNTIME,
            "(" + line + ":" + positionInLine + ") " + msg));

    if (fatal) RuntimeErrorExit.exit(this, exitCode);
  }

  public boolean hasError() {
    return !errors.isEmpty();
  }

  public List<BonesError> getErrors() {
    return errors;
  }
}

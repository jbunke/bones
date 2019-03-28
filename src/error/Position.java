package error;

import org.antlr.v4.runtime.Token;

public class Position {
  private final int line;
  private final int positionInLine;

  public Position(int line, int positionInLine) {
    this.line = line;
    this.positionInLine = positionInLine;
  }

  public int getLine() {
    return line;
  }

  public int getPositionInLine() {
    return positionInLine;
  }

  public static Position fromToken(Token token) {
    return new Position(token.getLine(), token.getCharPositionInLine());
  }
}

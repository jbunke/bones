package execution;

public class StatementControl {
  private final boolean cont;
  private final Object value;

  private StatementControl(boolean cont) {
    this.cont = cont;
    this.value = null;
  }

  private StatementControl(Object value) {
    this.cont = false;
    this.value = value;
  }

  public static StatementControl cont() {
    return new StatementControl(true);
  }

  public static StatementControl voidReturn() {
    return new StatementControl(false);
  }

  public static StatementControl returnWith(Object value) {
    return new StatementControl(value);
  }

  public boolean shouldContinue() { return cont; }

  public Object getValue() {
    return value;
  }
}

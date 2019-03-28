package execution;

import error.BonesErrorListener;

public class RuntimeErrorExit {
  public static final int RUNTIME_ERROR_EXIT = 1;

  public static void exit(BonesErrorListener errorListener, int exitCode) {
    errorListener.getErrors().forEach(System.out::println);
    System.exit(exitCode);
  }
}

package execution;

import error.BonesErrorListener;
import formatting.ANSIFormatting;

public class RuntimeErrorExit {
  public static final int RUNTIME_ERROR_EXIT = 1;

  public static void exit(BonesErrorListener errorListener, int exitCode) {
    ANSIFormatting.setBold();
    ANSIFormatting.setRed();
    errorListener.getErrors().forEach(System.out::println);
    ANSIFormatting.resetANSI();
    System.exit(exitCode);
  }
}

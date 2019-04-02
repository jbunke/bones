package formatting;

public class ANSIFormatting {
  /* CONSTANTS */
  private static final String RESET = "\u001B[0m";
  private static final String BOLD = "\u001B[1m";
  private static final String RED = "\u001B[31m";
  private static final String GREEN = "\u001B[32m";
  private static final String YELLOW = "\u001B[33m";
  private static final String BLUE = "\u001B[34m";

  /* ANSI UTILITIES */
  public static void resetANSI() {
    System.out.print(RESET);
  }

  public static void setGreen() {
    System.out.print(GREEN);
  }

  public static void setBold() {
    System.out.print(BOLD);
  }

  public static void setRed() {
    System.out.print(RED);
  }

  public static void setYellow() {
    System.out.print(YELLOW);
  }

  public static void setBlue() {
    System.out.print(BLUE);
  }
}

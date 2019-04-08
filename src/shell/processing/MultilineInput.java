package shell.processing;

import formatting.ANSIFormatting;
import shell.ShellMain;

import java.util.Scanner;

public class MultilineInput {
  public static String checkMultiline(String input, Scanner scanner) {
    String res = input;

    while (res.endsWith("~")) {
      res = res.substring(0, res.length() - 1);

      multilinePrompt();

      String next = scanner.nextLine();
      res += next;
    }

    return res;
  }

  private static void multilinePrompt() {
    /* Get length of original prompt */
    int length = ("(" + ShellMain.username + ") ").length();

    if (ShellMain.directoryPath.size() > 3) {
      length += 3; // ...
      for (int i = ShellMain.directoryPath.size() - 3;
           i < ShellMain.directoryPath.size(); i++) {
        length += ("/" + ShellMain.directoryPath.get(i)).length();
      }
    } else {
      length += (ShellMain.generateDirectoryPath()).length();
    }

    length += " ".length();

    /* Format and print multiline prompt */
    ANSIFormatting.setBold();
    ANSIFormatting.setGreen();

    for (int i = 0; i < length; i++) {
      System.out.print(" ");
    }
    System.out.print("| ");

    ANSIFormatting.resetANSI();
  }
}

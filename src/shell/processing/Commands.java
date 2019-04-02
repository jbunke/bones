package shell.processing;

import formatting.ANSIFormatting;
import shell.ShellMain;

import java.io.File;

public class Commands {
  private static final String commandPrefix = ":";

  /* COMMANDS */
  private static final String[] quit = new String[] { "q", "quit" };
  private static final String[] me = new String[] { "me" };
  private static final String[] listFiles =
          new String[] { "ls", "dir", "list" };
  private static final String[] changeDir = new String[] { "cd" };

  public enum Status {
    QUIT,
    MATCHED,
    DID_NOT_MATCH
  }

  public static Status checkIfMatched(String input) {
    if (input.startsWith(commandPrefix)) {
      if (matchesCommand(input, quit)) return Status.QUIT;
      processCommand(input);
      return Status.MATCHED;
    }
    return Status.DID_NOT_MATCH;
  }

  private static boolean matchesCommand(String input, String[] options) {
    String trimmed = input.substring(1);

    if (trimmed.contains(" ")) {
      String[] parts = trimmed.split(" ");
      trimmed = parts[0];
    }

    for (String option : options) {
      if (trimmed.equals(option)) return true;
    }
    return false;
  }

  private static void processCommand(String input) {
    if (matchesCommand(input, me)) {
      /* :me | :me <USERNAME> */
      String[] commandParts = commandParts(input);
      if (commandParts.length > 1) {
        // SET
        ShellMain.username = input.substring(":me ".length());
        ShellMain.saveUsername();
      } else {
        // VIEW
        ANSIFormatting.setYellow();
        System.out.println(ShellMain.username);
      }
    } else if (matchesCommand(input, listFiles)) {
      /* :ls | :dir | :list */
      ANSIFormatting.setYellow();

      if (ShellMain.directory != null &&
              ShellMain.directory.isDirectory()) {
        File directory = ShellMain.directory;
        String[] contents = directory.list();

        if (contents != null) {
          for (String file : contents) System.out.println(file);
        } else System.out.println("[Nothing in directory]");
      } else System.out.println("[Not currently in a directory]");
    } else if (matchesCommand(input, changeDir)) {
      /* :cd .. | :cd someFolder | :cd folder1/+ */
      String[] commandParts = commandParts(input);

      String[] path = commandParts[1].split("/");

      for (String stage : path) ShellMain.changeDirectory(stage);
    }
    ANSIFormatting.resetANSI();
  }

  private static String[] commandParts(String input) {
    return input.substring(1).split(" ");
  }
}

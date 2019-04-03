package shell.processing;

import formatting.ANSIFormatting;
import shell.ShellMain;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

import java.io.File;
import java.util.List;

public class Commands {
  private static final String commandPrefix = ":";

  /* COMMANDS */
  private static final String[] quit = new String[] { "q", "quit" };
  private static final String[] me = new String[] { "me" };
  private static final String[] listFiles =
          new String[] { "ls", "dir", "list" };
  private static final String[] changeDir = new String[] { "cd" };
  private static final String[] variables = new String[] { "v", "variables" };
  // private static final String[] functions = new String[] { "f", "functions" };

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

      if (commandParts.length <= 1) {
        ShellMain.changeDirectory("");
        return;
      }

      String[] path = commandParts[1].split("/");

      for (String stage : path) ShellMain.changeDirectory(stage);
    } else if (matchesCommand(input, variables)) {
      /* :v | :variables */
      showVariables();
    }
    ANSIFormatting.resetANSI();
  }

  private static void showVariables() {
    ANSIFormatting.setYellow();

    List<Symbol> symbols =
            ShellMain.shellTable.getAll(SymbolTable.Filter.VARIABLES);

    for (Symbol symbol : symbols) {
      Variable variable = (Variable) symbol;
      System.out.println(variable.toString());
    }

    ANSIFormatting.resetANSI();
  }

  private static String[] commandParts(String input) {
    return input.substring(1).split(" ");
  }
}

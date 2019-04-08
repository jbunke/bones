package shell;

import shell.processing.Commands;
import shell.processing.Commands.Status;
import shell.processing.MultilineInput;
import structural_representation.Compile;
import structural_representation.Context;
import structural_representation.atoms.Atom;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.symbol_table.SymbolTable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import formatting.ANSIFormatting;

public class ShellMain {

  /* GLOBAL STATIC ACCESS */
  private static final Scanner in = new Scanner(System.in);
  public static final SymbolTable shellTable =
          new SymbolTable(null, null);

  public static String username = "user";
  public static List<String> directoryPath = new ArrayList<>();
  public static File directory = null;

  public static void main(String[] args) {
    startupSequence();

    commandCycle(in);
  }

  private static void commandCycle(Scanner scanner) {
    do {
      prompt();
      String input = scanner.nextLine().trim();

      if (input.equals("")) continue;

      input = MultilineInput.checkMultiline(input, scanner);

      Status status = Commands.checkIfMatched(input);

      switch (status) {
        case DID_NOT_MATCH:
          processBones(input);
          break;
        case QUIT:
          quitSequence();
          return;
      }

    } while (scanner.equals(in) || scanner.hasNext());
  }

  private static void startupSequence() {
    startupText();
    loadUsername();
    loadDirectory();
  }

  private static void startupText() {
    ANSIFormatting.setBold();
    ANSIFormatting.setBlue();

    System.out.println("--------------- BONES SHELL ---------------");
    System.out.println("Developed by Jordan Bunke for personal use");
    System.out.println("\n");
  }

  private static void loadUsername()  {
    File file = new File("res/shell_settings/username");

    try {
      if (!file.createNewFile()) {
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        List<String> lines = br.lines().collect(Collectors.toList());
        if (lines.size() > 0) username = lines.get(0).trim();
      } else {
        username = "user";
        saveUsername();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void loadDirectory() {
    File file = new File("res/shell_settings/directory");

    try {
      if (!file.createNewFile()) {
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        List<String> lines = br.lines().collect(Collectors.toList());
        if (lines.size() > 0) {
          String firstLine = lines.get(0).trim();
          String[] folders = firstLine.split("/");
          directoryPath = new ArrayList<>();
          directoryPath.addAll(Arrays.asList(folders));
          directory = new File(generateDirectoryPath());
        }
      } else {
        directoryPath = new ArrayList<>();
        directory = null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void quitSequence() {
    saveUsername();
    saveDirectory();
    quitText();
  }

  private static void quitText() {
    ANSIFormatting.setBold();
    ANSIFormatting.setBlue();
    System.out.println("\n\nSettings saved, exiting shell...");
    ANSIFormatting.resetANSI();
  }

  public static void saveUsername() {
    try {
      FileWriter writer = new FileWriter(
              new File("res/shell_settings/username"), false);
      writer.write(username);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void saveDirectory() {
    try {
      FileWriter writer = new FileWriter(
              new File("res/shell_settings/directory"), false);
      writer.write(generateDirectoryPath());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void processBones(String input) {
    Context context = Compile.createStructure(input,
            Compile.SourceType.STRING, Compile.InputType.COMMAND, shellTable);

    Atom structure = context.getStructure();

    if (context.getErrorListener().hasError()) return;

    if (structure instanceof ExpressionAtom) {
      Object result = ((ExpressionAtom) structure).evaluate(shellTable,
              context.getErrorListener());

      ANSIFormatting.setYellow();
      System.out.println(result);
    } else if (structure instanceof StatementAtom) {
      ((StatementAtom) structure).execute(shellTable,
              context.getErrorListener());
    }
    ANSIFormatting.resetANSI();
  }

  public static void changeDirectory(String dir) {
    switch (dir) {
      case "..":
        if (directoryPath.size() > 1) {
          directoryPath.remove(directoryPath.size() - 1);
          directory = new File(generateDirectoryPath());
        } else if (directoryPath.size() > 0) {
          directoryPath = new ArrayList<>();
          directory = directory.getParentFile();
        }
        break;
      case "":
        directoryPath = new ArrayList<>();
        directory = null;
        break;
      default:
        directoryPath.add(dir);
        directory = new File(generateDirectoryPath());

        if (!directory.isDirectory()) {

          ANSIFormatting.setRed();
          System.out.println("[Not a directory]");

          directoryPath.remove(directoryPath.size() - 1);
          directory = new File(generateDirectoryPath());
        }
        break;
    }

    saveDirectory();
    ANSIFormatting.resetANSI();
  }

  /* TEXT UTILITIES */
  private static void prompt() {
    ANSIFormatting.setBold();
    ANSIFormatting.setGreen();

    System.out.print("(" + username + ") ");

    ANSIFormatting.setBold();
    ANSIFormatting.setBlue();

    if (directoryPath.size() > 3) {
      System.out.print("...");
      for (int i = directoryPath.size() - 3; i < directoryPath.size(); i++) {
        System.out.print("/" + directoryPath.get(i));
      }
    } else {
      System.out.print(generateDirectoryPath());
    }

    ANSIFormatting.setBold();
    ANSIFormatting.setGreen();

    System.out.print(" > ");

    ANSIFormatting.resetANSI();
  }

  public static String generateDirectoryPath() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < directoryPath.size(); i++) {
      if (i > 0) { sb.append("/"); sb.append(directoryPath.get(i)); }
      else sb.append(directoryPath.get(i));
    }

    return sb.toString();
  }
}

package structural_representation;

import antlr.BonesLexer;
import antlr.BonesParser;
import error.BonesErrorListener;
import formatting.ANSIFormatting;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import structural_representation.atoms.Atom;
import structural_representation.atoms.special.ClassAtom;
import structural_representation.symbol_table.SymbolTable;

import java.io.IOException;

public class Compile {
  public static final int SEMANTIC_ERROR_EXIT = 100;
  public static final int RUNTIME_ERROR_EXIT = 1;

  public enum SourceType {
    FILE, STRING
  }

  public enum InputType {
    CLASS, COMMAND
  }

  private static BonesParser createParser(String source,
                                          SourceType sourceMode) {
    CharStream stream = null;

    switch (sourceMode) {
      case STRING:
        stream = CharStreams.fromString(source);
        break;
      case FILE:
      default:
        try {
          stream = CharStreams.fromFileName(source);
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
    }

    BonesLexer lexer = new BonesLexer(stream);
    lexer.removeErrorListeners();

    TokenStream lexerTokens = new CommonTokenStream(lexer);
    BonesParser parser = new BonesParser(lexerTokens);
    parser.removeErrorListeners();

    return parser;
  }

  public static Context createStructure(String source,
                                        SourceType sourceMode,
                                        InputType inputType,
                                        SymbolTable forCommandsOnly) {
    BonesParser parser = createParser(source, sourceMode);

    BonesErrorListener errorListener = new BonesErrorListener();
    parser.addErrorListener(errorListener);

    switch (inputType) {
      case COMMAND:
        BonesParser.Shell_ruleContext commandParseTree = parser.shell_rule();

        /* SYNTAX ERROR CHECK */
        printErrorsAndExit(errorListener, false, SEMANTIC_ERROR_EXIT);

        if (errorListener.hasError())
          return new Context(null, null, errorListener);

        BonesVisitor commandVisitor = new BonesVisitor();
        Atom command = commandVisitor.visit(commandParseTree);

        /* SEMANTIC ERROR CHECK */
        command.semanticErrorCheck(forCommandsOnly, errorListener);
        printErrorsAndExit(errorListener, false, SEMANTIC_ERROR_EXIT);

        return new Context(command, forCommandsOnly, errorListener);
      case CLASS:
      default:
        BonesParser.Class_ruleContext parseTree = parser.class_rule();

        /* SYNTAX ERROR CHECK */
        printErrorsAndExit(errorListener, true, SEMANTIC_ERROR_EXIT);

        BonesVisitor visitor = new BonesVisitor();
        ClassAtom structure = (ClassAtom) visitor.visit(parseTree);

        SymbolTable rootTable = new SymbolTable(structure, null);

        if (sourceMode == SourceType.FILE)
          structure.processImports(source, rootTable);

        /* SEMANTIC ERROR CHECK */
        structure.semanticErrorCheck(rootTable, errorListener);
        printErrorsAndExit(errorListener, true, SEMANTIC_ERROR_EXIT);

        return new Context(structure, rootTable, errorListener);
    }
  }

  public static void printErrorsAndExit(BonesErrorListener errorListener,
                                         boolean quit, int exitCode) {
    if (errorListener.hasError()) {

      ANSIFormatting.setBold();
      ANSIFormatting.setRed();

      errorListener.getErrors().forEach(System.out::println);

      ANSIFormatting.resetANSI();

      if (quit) System.exit(exitCode);
    }
  }
}

package structural_representation;

import antlr.BonesLexer;
import antlr.BonesParser;
import error.BonesError;
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
  private static final int BAD_EXIT_CODE = 100;

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
        printErrorsAndExit(errorListener, false);

        if (errorListener.hasError())
          return new Context(null, null, errorListener);

        BonesVisitor commandVisitor = new BonesVisitor();
        Atom command = commandVisitor.visit(commandParseTree);

        /* SEMANTIC ERROR CHECK */
        command.semanticErrorCheck(forCommandsOnly, errorListener);
        printErrorsAndExit(errorListener, false);

        return new Context(command, forCommandsOnly, errorListener);
      case CLASS:
      default:
        BonesParser.Class_ruleContext parseTree = parser.class_rule();

        /* SYNTAX ERROR CHECK */
        printErrorsAndExit(errorListener, true);

        BonesVisitor visitor = new BonesVisitor();
        ClassAtom structure = (ClassAtom) visitor.visit(parseTree);

        SymbolTable rootTable = new SymbolTable(structure, null);

        /* SEMANTIC ERROR CHECK */
        structure.semanticErrorCheck(rootTable, errorListener);
        printErrorsAndExit(errorListener, true);

        return new Context(structure, rootTable, errorListener);
    }
  }

  private static void printErrorsAndExit(BonesErrorListener errorListener,
                                         boolean quit) {
    if (errorListener.hasError()) {

      ANSIFormatting.setBold();
      ANSIFormatting.setRed();

      for (BonesError error : errorListener.getErrors()) {
        System.out.println(error);
      }

      ANSIFormatting.resetANSI();

      if (quit) System.exit(BAD_EXIT_CODE);
    }
  }
}

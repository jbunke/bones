import antlr.BonesLexer;
import antlr.BonesParser;
import error.BonesError;
import error.BonesErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import structural_representation.BonesVisitor;
import structural_representation.atoms.special.ClassAtom;
import structural_representation.symbol_table.SymbolTable;

import java.io.IOException;

public class CompilerMain {
  private static final int EXPECTED_NUM_ARGS = 1;
  private static final int BAD_EXIT_CODE = 100;

  public static void main(String[] args) {
    if (args.length != EXPECTED_NUM_ARGS) {
      System.out.println("Expecting one argument: FILEPATH.b");
      System.exit(BAD_EXIT_CODE);
    }

    try {
      CharStream stream = CharStreams.fromFileName(args[0]);
      BonesLexer lexer = new BonesLexer(stream);
      lexer.removeErrorListeners();

      TokenStream lexerTokens = new CommonTokenStream(lexer);
      BonesParser parser = new BonesParser(lexerTokens);
      parser.removeErrorListeners();

      BonesErrorListener errorListener = new BonesErrorListener();
      parser.addErrorListener(errorListener);

      BonesParser.Class_ruleContext parseTree = parser.class_rule();

      /* SYNTAX ERROR CHECK */
      printErrorsAndExit(errorListener);

      BonesVisitor visitor = new BonesVisitor();
      ClassAtom structure = (ClassAtom) visitor.visit(parseTree);

      SymbolTable rootTable = new SymbolTable(structure, null);

      /* SEMANTIC ERROR CHECK */
      structure.semanticErrorCheck(rootTable, errorListener);
      printErrorsAndExit(errorListener);

      System.out.println("DONE");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void printErrorsAndExit(BonesErrorListener errorListener) {
    if (errorListener.hasError()) {
      for (BonesError error : errorListener.getErrors()) {
        System.out.println(error);
      }

      System.exit(BAD_EXIT_CODE);
    }
  }
}

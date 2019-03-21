import antlr.BonesLexer;
import antlr.BonesParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import structural_representation.BonesVisitor;
import structural_representation.atoms.special.ClassAtom;

import java.io.IOException;

public class CompilerMain {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Expecting one argument: FILEPATH.b");
      System.exit(1);
    }

    try {
      CharStream stream = CharStreams.fromFileName(args[0]);
      BonesLexer lexer = new BonesLexer(stream);

      TokenStream lexerTokens = new CommonTokenStream(lexer);
      BonesParser parser = new BonesParser(lexerTokens);

      BonesParser.Class_ruleContext parseTree = parser.class_rule();

      BonesVisitor visitor = new BonesVisitor();
      ClassAtom structure = (ClassAtom) visitor.visit(parseTree);

      System.out.println("DONE");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

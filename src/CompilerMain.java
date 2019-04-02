import error.BonesErrorListener;
import structural_representation.Compile;
import structural_representation.Context;
import structural_representation.atoms.special.ClassAtom;
import structural_representation.symbol_table.SymbolTable;

import java.util.Scanner;

public class CompilerMain {
  private static final Scanner in = new Scanner(System.in);
  private static final int EXPECTED_NUM_ARGS = 1;

  public static void main(String[] args) {
    String filepath;

    if (args.length != EXPECTED_NUM_ARGS) {
      System.out.println("Expecting one argument: FILEPATH.b");
      System.out.println("Provide a Bones source code filepath: ");

      filepath = in.nextLine();
    } else {
      filepath = args[0];
    }

    Context context = Compile.createStructure(filepath,
            Compile.SourceType.FILE, Compile.InputType.CLASS, null);
    ClassAtom structure = (ClassAtom) context.getStructure();
    SymbolTable rootTable = context.getSymbolTable();
    BonesErrorListener errorListener = context.getErrorListener();

      /* PROGRAM EXECUTION */
    structure.execute(rootTable, errorListener);
  }
}

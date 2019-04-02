package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.Position;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.DeterminedAtRuntimeType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.atoms.types.primitives.FloatType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.atoms.types.primitives.StringType;
import structural_representation.symbol_table.SymbolTable;

import java.util.Scanner;

public class ReadExpressionAtom extends ExpressionAtom {
  private BonesType type;
  public ReadExpressionAtom(Position position) {
    this.position = position;
    this.type = null;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    if (type == null) return new DeterminedAtRuntimeType();
    return type;
  }

  @Override
  public Object evaluate(SymbolTable table,
                         BonesErrorListener errorListener) {
    Scanner in = new Scanner(System.in);
    String input = in.nextLine();

    if (input.contains(".") && numericSequence(input)) {
      type = new FloatType();
      return Float.parseFloat(input);
    } else if (numericSequence(input)) {
      type = new IntType();
      return Integer.parseInt(input);
    } else if (input.equals("true") || input.equals("false")) {
      type = new BoolType();
      return Boolean.parseBoolean(input);
    }

    type = new StringType();
    return input;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
  }

  private boolean numericSequence(String sequence) {
    int periods = 0;
    for (char c : sequence.toCharArray()) {
      switch (c) {
        case '0': case '1': case '2': case '3':
        case '4': case '5': case '6': case '7':
        case '8': case '9':
          break;
        case '.':
          periods++;
          break;
        default:
          return false;
      }
    }
    return periods <= 1;
  }
}

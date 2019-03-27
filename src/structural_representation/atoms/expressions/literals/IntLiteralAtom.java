package structural_representation.atoms.expressions.literals;

import error.BonesErrorListener;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.symbol_table.SymbolTable;

public class IntLiteralAtom extends ExpressionAtom {
  private final int value;

  public IntLiteralAtom(int value) {
    this.value = value;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return new IntType();
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    return value;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {

  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}

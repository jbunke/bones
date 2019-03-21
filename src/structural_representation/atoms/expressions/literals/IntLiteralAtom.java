package structural_representation.atoms.expressions.literals;

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
}

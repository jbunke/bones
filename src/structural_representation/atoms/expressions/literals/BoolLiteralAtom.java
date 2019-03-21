package structural_representation.atoms.expressions.literals;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.symbol_table.SymbolTable;

public class BoolLiteralAtom extends ExpressionAtom {
  private final boolean value;

  public BoolLiteralAtom(boolean value) {
    this.value = value;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return new BoolType();
  }
}

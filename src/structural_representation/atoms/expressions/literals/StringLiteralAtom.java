package structural_representation.atoms.expressions.literals;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.StringType;
import structural_representation.symbol_table.SymbolTable;

public class StringLiteralAtom extends ExpressionAtom {
  private final String value;

  public StringLiteralAtom(String value) {
    this.value = value;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return new StringType();
  }
}

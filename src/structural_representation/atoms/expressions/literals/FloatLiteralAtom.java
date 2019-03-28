package structural_representation.atoms.expressions.literals;

import error.BonesErrorListener;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.FloatType;
import structural_representation.symbol_table.SymbolTable;

public class FloatLiteralAtom extends ExpressionAtom {
  private final float value;

  public FloatLiteralAtom(float value) {
    this.value = value;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return new FloatType();
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

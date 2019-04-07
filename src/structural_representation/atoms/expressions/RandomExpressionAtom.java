package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.Position;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.FloatType;
import structural_representation.symbol_table.SymbolTable;

public class RandomExpressionAtom extends ExpressionAtom {
  public RandomExpressionAtom(Position position) {
    this.position = position;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return new FloatType();
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    return (float) Math.random();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {

  }
}

package structural_representation.atoms.expressions;

import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.atoms.types.primitives.VoidType;
import structural_representation.symbol_table.SymbolTable;

public class UnaryOperationAtom extends ExpressionAtom {
  private final ExpressionAtom expr;
  private final UnaryOperationAtom.Operator operator;

  public UnaryOperationAtom(ExpressionAtom expr, String opString) {
    this.expr = expr;
    operator = operatorFromString(opString);
  }

  private enum Operator {
    NOT, SIZE, MINUS
  }

  private Operator operatorFromString(String opString) {
    switch (opString) {
      case "!":
        return Operator.NOT;
      case "#":
        return Operator.SIZE;
      case "-":
        return Operator.MINUS;
      default:
        return null;
    }
  }

  @Override
  public BonesType getType(SymbolTable table) {
    switch (operator) {
      case SIZE:
        return new IntType();
      case NOT:
        return new BoolType();
      case MINUS:
        return expr.getType(table);
      default:
        return new VoidType();
    }
  }
}

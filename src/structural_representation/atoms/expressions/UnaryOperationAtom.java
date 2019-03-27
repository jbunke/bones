package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.*;
import structural_representation.symbol_table.SymbolTable;

public class UnaryOperationAtom extends ExpressionAtom {
  private final ExpressionAtom expr;
  private final UnaryOperationAtom.Operator operator;

  public UnaryOperationAtom(ExpressionAtom expr, String opString) {
    this.expr = expr;
    operator = operatorFromString(opString);
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    switch (operator) {
      case NOT:
        if (!expr.getType(symbolTable).equals(new BoolType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Not operation",
                          new BoolType(), expr.getType(symbolTable)));
        }
        break;
      case SIZE:
        if (!(expr.getType(symbolTable) instanceof ListType) &&
                !(expr.getType(symbolTable) instanceof ArrayType) &&
                !(expr.getType(symbolTable) instanceof StringType)) {
          errorListener.semanticError(
                  ErrorMessages.calledSizeOnNonCollection());
        }
        break;
      case MINUS:
        if (!(expr.getType(symbolTable) instanceof IntType) &&
                !(expr.getType(symbolTable) instanceof FloatType)) {
          errorListener.semanticError(
                  ErrorMessages.calledMinusOnNonNumeric());
        }
        break;
    }

    expr.semanticErrorCheck(symbolTable, errorListener);
  }

  private enum Operator {
    NOT, SIZE, MINUS
  }

  private String operatorToString() {
    switch (operator) {
      case NOT:
        return "!";
      case MINUS:
        return "-";
      case SIZE:
        return "#";
      default:
        return "";
    }
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

  @Override
  public String toString() {
    return operatorToString() + expr.toString();
  }
}

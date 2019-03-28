package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.BonesArray;
import execution.BonesList;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.*;
import structural_representation.symbol_table.SymbolTable;

public class UnaryOperationAtom extends ExpressionAtom {
  private final ExpressionAtom expr;
  private final UnaryOperationAtom.Operator operator;

  public UnaryOperationAtom(ExpressionAtom expr, String opString,
                            Position position) {
    this.expr = expr;
    operator = operatorFromString(opString);
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    switch (operator) {
      case NOT:
        if (!expr.getType(symbolTable).equals(new BoolType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Not operation",
                          new BoolType(), expr.getType(symbolTable)),
                  getPosition().getLine(), getPosition().getPositionInLine());
        }
        break;
      case SIZE:
        if (!(expr.getType(symbolTable) instanceof ListType) &&
                !(expr.getType(symbolTable) instanceof ArrayType) &&
                !(expr.getType(symbolTable) instanceof StringType)) {
          errorListener.semanticError(
                  ErrorMessages.calledSizeOnNonCollection(),
                  getPosition().getLine(), getPosition().getPositionInLine());
        }
        break;
      case MINUS:
        if (!(expr.getType(symbolTable) instanceof IntType) &&
                !(expr.getType(symbolTable) instanceof FloatType)) {
          errorListener.semanticError(
                  ErrorMessages.calledMinusOnNonNumeric(),
                  getPosition().getLine(), getPosition().getPositionInLine());
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
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    Object value = expr.evaluate(table, errorListener);

    switch (operator) {
      case SIZE:
        if (value instanceof String) {
          return ((String) value).length();
        } else if (value instanceof BonesList) {
          return ((BonesList) value).size();
        } else if (value instanceof BonesArray) {
          return ((BonesArray) value).size();
        }
        break;
      case MINUS:
        if (value instanceof Integer) {
          return -((Integer) value);
        } else if (value instanceof Float) {
          return -((Float) value);
        }
        break;
      case NOT:
        return !(Boolean) value;
    }
    return null;
  }

  @Override
  public String toString() {
    return operatorToString() + expr.toString();
  }
}

package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import execution.BonesArray;
import execution.BonesList;
import execution.RuntimeErrorExit;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.*;
import structural_representation.symbol_table.SymbolTable;

public class BinaryOperationAtom extends ExpressionAtom {
  private final ExpressionAtom LHS;
  private final ExpressionAtom RHS;
  private final Operator operator;
  private final String opString;

  public BinaryOperationAtom(ExpressionAtom lhs, ExpressionAtom rhs,
                             String opString) {
    LHS = lhs;
    RHS = rhs;
    this.opString = opString;
    operator = operatorFromString(opString);
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    final BonesType ltype = LHS.getType(symbolTable);
    final BonesType rtype = RHS.getType(symbolTable);

    switch (operator) {
      case RAISE:
      case TIMES:
      case DIVIDE:
      case MOD:
      case PLUS:
      case MINUS:
      case GT:
      case LT:
      case GEQ:
      case LEQ:
        if (!isNumeric(ltype)) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Operation \"" +
                                  opString + "\"", new IntType(), ltype));
        }
        if (!isNumeric(rtype)) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Operation \"" +
                                  opString + "\"", new IntType(), rtype));
        }
        break;
      case AT_INDEX:
        if (!rtype.equals(new IntType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs(
                          "Index operand", new IntType(), rtype));
        }
        if (!(ltype instanceof ListType) && !(ltype instanceof ArrayType) &&
                !(ltype instanceof StringType)) {
          errorListener.semanticError(ErrorMessages.
                  calledAtIndexOnNonCollection());
        }
        break;
      case OR:
      case AND:
        if (!ltype.equals(new BoolType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Operation \"" +
                          opString + "\"", new BoolType(), ltype));
        }
        if (!rtype.equals(new BoolType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Operation \"" +
                          opString + "\"", new BoolType(), rtype));
        }
        break;
    }

    LHS.semanticErrorCheck(symbolTable, errorListener);
    RHS.semanticErrorCheck(symbolTable, errorListener);
  }

  private static boolean isNumeric(BonesType type) {
    return type.equals(new IntType()) || type.equals(new FloatType());
  }

  private enum Operator {
    RAISE,
    TIMES, DIVIDE, MOD,
    PLUS, MINUS,
    AT_INDEX,
    GEQ, LEQ, GT, LT,
    EQUAL, NOT_EQUAL,
    AND,
    OR
  }

  private Operator operatorFromString(String opString) {
    switch (opString) {
      case "^":
        return Operator.RAISE;
      case "*":
        return Operator.TIMES;
      case "/":
        return Operator.DIVIDE;
      case "%":
        return Operator.MOD;
      case "+":
        return Operator.PLUS;
      case "-":
        return Operator.MINUS;
      case "@":
        return Operator.AT_INDEX;
      case ">=":
        return Operator.GEQ;
      case "<=":
        return Operator.LEQ;
      case ">":
        return Operator.GT;
      case "<":
        return Operator.LT;
      case "==":
        return Operator.EQUAL;
      case "!=":
        return Operator.NOT_EQUAL;
      case "&&":
        return Operator.AND;
      case "||":
        return Operator.OR;
      default:
          return null;
    }
  }

  @Override
  public BonesType getType(SymbolTable table) {
    BonesType leftType = LHS.getType(table);
    BonesType rightType = RHS.getType(table);

    switch (operator) {
      case RAISE:
      case TIMES:
        if (leftType instanceof FloatType ||
                rightType instanceof FloatType) {
          return new FloatType();
        }
        return new IntType();
      case DIVIDE:
      case MOD:
        if (rightType instanceof FloatType) {
          return new FloatType();
        }
        return new IntType();
      case PLUS:
        /* SPECIAL CASE AS MAY BE EXTENDED FOR CONCATENATION */
        if (rightType instanceof StringType &&
                leftType instanceof StringType) {
          return new StringType();
        } else if (rightType instanceof FloatType &&
                leftType instanceof FloatType) {
          return new FloatType();
        }
        return new IntType();
      case MINUS:
        if (rightType instanceof FloatType &&
                leftType instanceof FloatType) {
          return new FloatType();
        }
        return new IntType();
      case GEQ:
      case LEQ:
      case GT:
      case LT:
      case EQUAL:
      case NOT_EQUAL:
      case AND:
      case OR:
        return new BoolType();
      default:
        return new VoidType();
    }
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    Object left = LHS.evaluate(table, errorListener);
    Object right = RHS.evaluate(table, errorListener);

    if (left == null || right == null) return null;

    switch (operator) {
      case AT_INDEX:
        if (!(right instanceof Integer)) break;
        if (left instanceof BonesList) {
          return ((BonesList) left).at((Integer) right);
        } else if (left instanceof BonesArray) {
          return ((BonesArray) left).at((Integer) right);
        }
        break;
      case RAISE:
        if (left instanceof Number && right instanceof Number) {
          return Math.pow((Double) left, (Double) right);
        }
        break;
      case PLUS:
      case MINUS:
      case TIMES:
      case DIVIDE:
      case MOD:
      case GT:
      case LT:
      case GEQ:
      case LEQ:
        if (left instanceof Integer && right instanceof Integer) {
          Integer lefti = ((Number) left).intValue();
          Integer righti = ((Number) right).intValue();

          switch (operator) {
            case GT:
              return lefti > righti;
            case LT:
              return lefti < righti;
            case GEQ:
              return lefti >= righti;
            case LEQ:
              return lefti <= righti;
            case PLUS:
              return lefti + righti;
            case MINUS:
              return lefti - righti;
            case TIMES:
              return lefti * righti;
            case DIVIDE:
              if (right.equals(0)) errorListener.runtimeError(
                      ErrorMessages.divideByZero(),
                      true, RuntimeErrorExit.RUNTIME_ERROR_EXIT);
              return lefti / righti;
            case MOD:
              if (right.equals(0)) errorListener.runtimeError(
                      ErrorMessages.divideByZero(),
                      true, RuntimeErrorExit.RUNTIME_ERROR_EXIT);
              return lefti % righti;
          }
        } else if (left instanceof Float || right instanceof Float) {
          Float leftf = ((Number) left).floatValue();
          Float rightf = ((Number) right).floatValue();

          switch (operator) {
            case GT:
              return leftf > rightf;
            case LT:
              return leftf < rightf;
            case GEQ:
              return leftf >= rightf;
            case LEQ:
              return leftf <= rightf;
            case PLUS:
              return leftf + rightf;
            case MINUS:
              return leftf - rightf;
            case TIMES:
              return leftf * rightf;
            case DIVIDE:
              if (right.equals(0f)) errorListener.runtimeError(
                      ErrorMessages.divideByZero(),
                      true, RuntimeErrorExit.RUNTIME_ERROR_EXIT);
              return leftf / rightf;
            case MOD:
              if (right.equals(0f)) errorListener.runtimeError(
                      ErrorMessages.divideByZero(),
                      true, RuntimeErrorExit.RUNTIME_ERROR_EXIT);
              return leftf % rightf;
          }
        }
        break;
      case EQUAL:
        return left.equals(right);
      case NOT_EQUAL:
        return !left.equals(right);
      case AND:
        return (Boolean) left && (Boolean) right;
      case OR:
        return (Boolean) left || (Boolean) right;
    }
    return null;
  }

  @Override
  public String toString() {
    return LHS.toString() + " " + opString + " " + RHS.toString();
  }
}

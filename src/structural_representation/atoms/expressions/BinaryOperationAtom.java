package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.BonesArray;
import execution.BonesList;
import structural_representation.Compile;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.DeterminedAtRuntimeType;
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
                             String opString, Position position) {
    LHS = lhs;
    RHS = rhs;
    this.opString = opString;
    operator = operatorFromString(opString);
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    LHS.semanticErrorCheck(symbolTable, errorListener);
    RHS.semanticErrorCheck(symbolTable, errorListener);

    if (errorListener.hasError()) return;

    final BonesType ltype = LHS.getType(symbolTable);
    final BonesType rtype = RHS.getType(symbolTable);

    switch (operator) {
      case PLUS:
        if (!((ltype.equals(new StringType()) &&
                rtype.equals(new StringType())) ||
                isNumeric(ltype) && isNumeric(rtype))) {
          errorListener.semanticError(
                  ErrorMessages.invalidTypesForPlusConcat(),
                  position.getLine(), position.getPositionInLine());
        }
        break;
      case RAISE:
      case TIMES:
      case DIVIDE:
      case MOD:
      case MINUS:
      case GT:
      case LT:
      case GEQ:
      case LEQ:
        if (!isNumeric(ltype)) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Operation \"" +
                                  opString + "\"", new IntType(), ltype),
                  position.getLine(), position.getPositionInLine());
        }
        if (!isNumeric(rtype)) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Operation \"" +
                                  opString + "\"", new IntType(), rtype),
                  position.getLine(), position.getPositionInLine());
        }
        break;
      case AT_INDEX:
        if (!rtype.equals(new IntType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs(
                          "Index operand", new IntType(), rtype),
                  position.getLine(), position.getPositionInLine());
        }
        if (!(ltype instanceof ListType) && !(ltype instanceof ArrayType) &&
                !(ltype instanceof StringType)) {
          errorListener.semanticError(ErrorMessages.
                  calledAtIndexOnNonCollection(),
                  position.getLine(), position.getPositionInLine());
        }
        break;
      case OR:
      case AND:
        if (!ltype.equals(new BoolType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Operation \"" +
                          opString + "\"", new BoolType(), ltype),
                  position.getLine(), position.getPositionInLine());
        }
        if (!rtype.equals(new BoolType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Operation \"" +
                          opString + "\"", new BoolType(), rtype),
                  position.getLine(), position.getPositionInLine());
        }
        break;
    }
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
        /* Concatenation */
        if (rightType instanceof StringType &&
                leftType instanceof StringType) {
          return new StringType();
        } else if (rightType instanceof FloatType ||
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
      case AT_INDEX:
        if (leftType instanceof ListType) {
          return ((ListType) leftType).getElementType();
        } else if (leftType instanceof ArrayType) {
          return ((ArrayType) leftType).getElementType();
        } else if (leftType.equals(new StringType())) return new CharType();
        return new DeterminedAtRuntimeType();
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
        } else if (left instanceof String) {
          return ((String) left).charAt((Integer) right);
        }
        break;
      case RAISE:
        if (left instanceof Number && right instanceof Number) {
          // TODO: breaks on ints
          return Math.pow((Double) left, (Double) right);
        }
        break;
      case PLUS:
        if (left instanceof Integer && right instanceof Integer) {
          Integer lefti = ((Number) left).intValue();
          Integer righti = ((Number) right).intValue();

          return lefti + righti;
        } else if (left instanceof Float || right instanceof Float) {
          Float leftf = ((Number) left).floatValue();
          Float rightf = ((Number) right).floatValue();

          return leftf + rightf;
        } else if (left instanceof String && right instanceof String) {
          /* Concatenation */
          String lefts = (String) left;
          String rights = (String) right;

          return lefts + rights;
        }
        break;
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
            case MINUS:
              return lefti - righti;
            case TIMES:
              return lefti * righti;
            case DIVIDE:
              if (right.equals(0)) errorListener.runtimeError(
                      ErrorMessages.divideByZero(),
                      true, Compile.RUNTIME_ERROR_EXIT,
                      position.getLine(), position.getPositionInLine());
              return lefti / righti;
            case MOD:
              if (right.equals(0)) errorListener.runtimeError(
                      ErrorMessages.divideByZero(),
                      true, Compile.RUNTIME_ERROR_EXIT,
                      position.getLine(), position.getPositionInLine());
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
            case MINUS:
              return leftf - rightf;
            case TIMES:
              return leftf * rightf;
            case DIVIDE:
              if (right.equals(0f)) errorListener.runtimeError(
                      ErrorMessages.divideByZero(),
                      true, Compile.RUNTIME_ERROR_EXIT,
                      position.getLine(), position.getPositionInLine());
              return leftf / rightf;
            case MOD:
              if (right.equals(0f)) errorListener.runtimeError(
                      ErrorMessages.divideByZero(),
                      true, Compile.RUNTIME_ERROR_EXIT,
                      position.getLine(), position.getPositionInLine());
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

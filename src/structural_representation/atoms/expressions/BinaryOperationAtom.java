package structural_representation.atoms.expressions;

import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.*;
import structural_representation.symbol_table.SymbolTable;

public class BinaryOperationAtom extends ExpressionAtom {
  private final ExpressionAtom LHS;
  private final ExpressionAtom RHS;
  private final Operator operator;

  public BinaryOperationAtom(ExpressionAtom lhs, ExpressionAtom rhs,
                             String opString) {
    LHS = lhs;
    RHS = rhs;
    operator = operatorFromString(opString);
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
}

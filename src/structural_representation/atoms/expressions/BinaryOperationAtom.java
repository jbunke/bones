package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
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
}

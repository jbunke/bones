package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.atoms.types.primitives.FloatType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.symbol_table.SymbolTable;

public class OperandSEAtom extends AssignmentAtom {
  private final Operator operator;
  private final ExpressionAtom expression;

  public OperandSEAtom(AssignableAtom assignable, Operator operator,
                       ExpressionAtom expression) {
    this.assignable = assignable;
    this.operator = operator;
    this.expression = expression;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    OperatorOperands operands = OperatorOperands.ALL;

    switch (operator) {
      case OR_ASSIGN:
      case AND_ASSIGN:
        operands = OperatorOperands.BOOL;
        break;
      case ADD_ASSIGN:
      case SUB_ASSIGN:
      case MUL_ASSIGN:
      case DIV_ASSIGN:
      case MOD_ASSIGN:
        operands = OperatorOperands.INT_FLOAT;
        break;
    }

    if (!compliant(assignable.getType(symbolTable), operands)) {
      errorListener.semanticError(ErrorMessages.
              sideEffectOperatorAssignable(operatorToString(),
                      assignable.getType(symbolTable)));
    }

    if (!compliant(expression.getType(symbolTable), operands)) {
      errorListener.semanticError(ErrorMessages.
              sideEffectOperatorOperand(operatorToString(),
                      expression.getType(symbolTable)));
    }
  }

  public enum Operator {
    ADD_ASSIGN, SUB_ASSIGN,
    MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN,
    AND_ASSIGN, OR_ASSIGN
  }

  private String operatorToString() {
    switch (operator) {
      case ADD_ASSIGN:
        return "+";
      case SUB_ASSIGN:
        return "-";
      case MUL_ASSIGN:
        return "*";
      case DIV_ASSIGN:
        return "/";
      case MOD_ASSIGN:
        return "%";
      case AND_ASSIGN:
        return "&&";
      case OR_ASSIGN:
        return "||";
      default:
        return "";
    }
  }

  public enum OperatorOperands {
    BOOL, ALL, INT, INT_FLOAT
  }

  private boolean compliant(BonesType type, OperatorOperands operands) {
    if (type instanceof BoolType) {
      return operands == OperatorOperands.BOOL;
    } else if (type instanceof IntType) {
      return operands == OperatorOperands.INT
              || operands == OperatorOperands.INT_FLOAT;
    } else if (type instanceof FloatType) {
      return operands == OperatorOperands.INT_FLOAT;
    }
    return false;
  }
}

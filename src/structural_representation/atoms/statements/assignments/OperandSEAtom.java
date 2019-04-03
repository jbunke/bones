package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import structural_representation.Compile;
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
                       ExpressionAtom expression, Position position) {
    this.assignable = assignable;
    this.operator = operator;
    this.expression = expression;
    this.position = position;
  }

  @Override
  public StatementControl execute(SymbolTable table, BonesErrorListener errorListener) {
    Object increment = expression.evaluate(table, errorListener);
    Object value = assignable.getInitialCollectionValue(table);

    switch (operator) {
      case OR_ASSIGN:
        value = (Boolean) value || (Boolean) increment;
        break;
      case AND_ASSIGN:
        value = (Boolean) value && (Boolean) increment;
        break;
      case ADD_ASSIGN:
        if (value instanceof Integer && increment instanceof Integer) {
          value = (Integer) value + (Integer) increment;
        } else {
          value = (Float) value + (Float) increment;
        }
        break;
      case SUB_ASSIGN:
        if (value instanceof Integer && increment instanceof Integer) {
          value = (Integer) value - (Integer) increment;
        } else {
          value = (Float) value - (Float) increment;
        }
        break;
      case MUL_ASSIGN:
        if (value instanceof Integer && increment instanceof Integer) {
          value = (Integer) value * (Integer) increment;
        } else {
          value = (Float) value * (Float) increment;
        }
        break;
      case DIV_ASSIGN:
        if (value instanceof Integer && increment instanceof Integer) {
          if (increment.equals(0)) errorListener.runtimeError(
                  ErrorMessages.divideByZero(), true,
                  Compile.RUNTIME_ERROR_EXIT,
                  getPosition().getLine(), getPosition().getPositionInLine());
          value = (Integer) value / (Integer) increment;
        } else {
          if (increment.equals(0f)) errorListener.runtimeError(
                  ErrorMessages.divideByZero(), true,
                  Compile.RUNTIME_ERROR_EXIT,
                  getPosition().getLine(), getPosition().getPositionInLine());
          value = (Float) value / (Float) increment;
        }
        break;
      case MOD_ASSIGN:
        if (value instanceof Integer && increment instanceof Integer) {
          if (increment.equals(0)) errorListener.runtimeError(
                  ErrorMessages.divideByZero(), true,
                  Compile.RUNTIME_ERROR_EXIT,
                  getPosition().getLine(), getPosition().getPositionInLine());
          value = (Integer) value % (Integer) increment;
        } else {
          if (increment.equals(0f)) errorListener.runtimeError(
                  ErrorMessages.divideByZero(), true,
                  Compile.RUNTIME_ERROR_EXIT,
                  getPosition().getLine(), getPosition().getPositionInLine());
          value = (Float) value % (Float) increment;
        }
        break;
    }

    assignable.assignmentSymbolTableUpdate(table, value);

    return StatementControl.cont();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    expression.semanticErrorCheck(symbolTable, errorListener);

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
                      assignable.getType(symbolTable)),
              getPosition().getLine(), getPosition().getPositionInLine());
    }

    if (!compliant(expression.getType(symbolTable), operands)) {
      errorListener.semanticError(ErrorMessages.
              sideEffectOperatorOperand(operatorToString(),
                      expression.getType(symbolTable)),
              getPosition().getLine(), getPosition().getPositionInLine());
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
        return "+=";
      case SUB_ASSIGN:
        return "-=";
      case MUL_ASSIGN:
        return "*=";
      case DIV_ASSIGN:
        return "/=";
      case MOD_ASSIGN:
        return "%=";
      case AND_ASSIGN:
        return "&=";
      case OR_ASSIGN:
        return "|=";
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

  @Override
  public String toString() {
    return assignable.toString() + " " + operatorToString() +
            " " + expression.toString() + ";";
  }
}

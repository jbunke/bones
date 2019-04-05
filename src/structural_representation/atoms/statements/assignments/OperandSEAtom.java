package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.BonesList;
import execution.StatementControl;
import structural_representation.Compile;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.atoms.types.primitives.FloatType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.atoms.types.primitives.StringType;
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
    Object value = assignable.getInitialCollectionValue(table, errorListener);

    switch (operator) {
      case REM_AT_INDEX:
        Integer index = (Integer) expression.evaluate(table, errorListener);

        if (index < 0 || index >= ((BonesList) value).size())
          errorListener.runtimeError(
                  ErrorMessages.collectionIndexOutOfBounds("List"),
                  true, Compile.RUNTIME_ERROR_EXIT,
                  expression.getPosition().getLine(),
                  expression.getPosition().getPositionInLine());

        ((BonesList) value).remove((int) index);
        break;
      case OR_ASSIGN:
        value = (Boolean) value || (Boolean) increment;
        break;
      case AND_ASSIGN:
        value = (Boolean) value && (Boolean) increment;
        break;
      case ADD_ASSIGN:
        if (value instanceof Integer && increment instanceof Integer) {
          value = (Integer) value + (Integer) increment;
        } else if (value instanceof String && increment instanceof String) {
          value = value.toString() + increment.toString();
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

    assignable.assignmentSymbolTableUpdate(table, value, errorListener);

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
        operands = OperatorOperands.INT_FLOAT_STRING;
        break;
      case SUB_ASSIGN:
      case MUL_ASSIGN:
      case DIV_ASSIGN:
      case MOD_ASSIGN:
        operands = OperatorOperands.INT_FLOAT;
        break;
      case REM_AT_INDEX:
        BonesType exprType = expression.getType(symbolTable);

        if (!exprType.equals(new IntType())) {
          errorListener.semanticError(ErrorMessages.collectionIndexType(),
                  expression.getPosition().getLine(),
                  expression.getPosition().getPositionInLine());
          return;
        }

        BonesType assignableType = assignable.getType(symbolTable);

        if (!(assignableType instanceof ListType)) {
          errorListener.semanticError(
                  ErrorMessages.compoundAssignmentListOpOnNonList(),
                  assignable.getPosition().getLine(),
                  assignable.getPosition().getPositionInLine());
        }
        return;
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
    AND_ASSIGN, OR_ASSIGN,
    REM_AT_INDEX
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
      case REM_AT_INDEX:
        return "-@";
      default:
        return "";
    }
  }

  public enum OperatorOperands {
    BOOL, ALL, INT, INT_FLOAT, INT_FLOAT_STRING
  }

  private boolean compliant(BonesType type, OperatorOperands operands) {
    if (type instanceof BoolType) {
      return operands == OperatorOperands.BOOL;
    } else if (type instanceof IntType) {
      return operands == OperatorOperands.INT
              || operands == OperatorOperands.INT_FLOAT
              || operands == OperatorOperands.INT_FLOAT_STRING;
    } else if (type instanceof FloatType) {
      return operands == OperatorOperands.INT_FLOAT ||
              operands == OperatorOperands.INT_FLOAT_STRING;
    } else if (type instanceof StringType) {
      return operands == OperatorOperands.INT_FLOAT_STRING;
    }
    return false;
  }

  @Override
  public String toString() {
    return assignable.toString() + " " + operatorToString() +
            " " + expression.toString() + ";";
  }
}

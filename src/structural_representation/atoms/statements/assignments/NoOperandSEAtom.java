package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import execution.StatementControl;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

public class NoOperandSEAtom extends AssignmentAtom {
  private final Operator operator;

  public NoOperandSEAtom(AssignableAtom assignable,
                         Operator operator) {
    this.assignable = assignable;
    this.operator = operator;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    BonesType operatorType = null;

    switch (operator) {
      case NEGATE:
        operatorType = new BoolType();
        break;
      case INCREMENT:
      case DECREMENT:
        operatorType = new IntType();
        break;
    }

    if (!assignable.getType(symbolTable).equals(operatorType)) {
      errorListener.semanticError(ErrorMessages.
              sideEffectOperatorAssignable(operatorToString(),
                      assignable.getType(symbolTable)));
    }
  }

  private String operatorToString() {
    switch (operator) {
      case NEGATE:
        return "!!";
      case INCREMENT:
        return "++";
      case DECREMENT:
        return "--";
      default:
        return "";
    }
  }

  public enum Operator {
    NEGATE, INCREMENT, DECREMENT
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    Object value;

    if (assignable instanceof IdentifierAtom) {
      value = ((Variable) table.get(assignable.toString())).getValue();
    } else {
      // TODO: else if is list elem or array elem
      // temp
      value = ((Variable) table.get(assignable.toString())).getValue();
    }

    switch (operator) {
      case NEGATE:
        value = !(Boolean) value;
        break;
      case DECREMENT:
        if (value instanceof Integer) {
          value = (Integer) value - 1;
        } else if (value instanceof Float) {
          value = (Float) value - 1f;
        }
        break;
      case INCREMENT:
        if (value instanceof Integer) {
          value = (Integer) value + 1;
        } else if (value instanceof Float) {
          value = (Float) value + 1f;
        }
        break;
    }

    if (assignable instanceof IdentifierAtom) {
      table.update(assignable.toString(), value);
    }
    // TODO: else if is list elem or array elem

    return StatementControl.cont();
  }

  @Override
  public String toString() {
    return assignable.toString() + operatorToString() + ";";
  }
}

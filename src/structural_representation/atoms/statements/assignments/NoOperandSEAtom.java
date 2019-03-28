package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.atoms.types.primitives.FloatType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.symbol_table.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class NoOperandSEAtom extends AssignmentAtom {
  private final Operator operator;

  public NoOperandSEAtom(AssignableAtom assignable,
                         Operator operator, Position position) {
    this.assignable = assignable;
    this.operator = operator;
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    List<BonesType> acceptable = new ArrayList<>();

    switch (operator) {
      case NEGATE:
        acceptable.add(new BoolType());
        break;
      case INCREMENT:
      case DECREMENT:
        acceptable.add(new IntType());
        acceptable.add(new FloatType());
        /* TODO: acceptable.add ListType
        BUT be aware that contains will no
        longer work as it checks with equality */
        break;
    }

    if (!acceptable.contains(assignable.getType(symbolTable))) {
      errorListener.semanticError(ErrorMessages.
              sideEffectOperatorAssignable(operatorToString(),
                      assignable.getType(symbolTable)),
              getPosition().getLine(), getPosition().getPositionInLine());
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
    Object value = assignable.getInitialCollectionValue(table);

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

    assignable.assignmentSymbolTableUpdate(table, value);

    return StatementControl.cont();
  }

  @Override
  public String toString() {
    return assignable.toString() + operatorToString() + ";";
  }
}

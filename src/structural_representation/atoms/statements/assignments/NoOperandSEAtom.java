package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.symbol_table.SymbolTable;

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
}

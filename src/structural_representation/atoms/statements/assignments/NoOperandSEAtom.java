package structural_representation.atoms.statements.assignments;

import structural_representation.atoms.expressions.assignables.AssignableAtom;

public class NoOperandSEAtom extends AssignmentAtom {
  private final Operator operator;

  public NoOperandSEAtom(AssignableAtom assignable,
                         Operator operator) {
    this.assignable = assignable;
    this.operator = operator;
  }

  public enum Operator {
    NEGATE, INCREMENT, DECREMENT
  }
}

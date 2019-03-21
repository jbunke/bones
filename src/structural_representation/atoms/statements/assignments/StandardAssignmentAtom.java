package structural_representation.atoms.statements.assignments;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.AssignableAtom;

public class StandardAssignmentAtom extends AssignmentAtom {
  private final ExpressionAtom RHS;

  public StandardAssignmentAtom(AssignableAtom assignable,
                                ExpressionAtom RHS) {
    this.assignable = assignable;
    this.RHS = RHS;
  }
}

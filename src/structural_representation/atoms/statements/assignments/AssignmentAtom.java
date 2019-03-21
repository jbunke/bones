package structural_representation.atoms.statements.assignments;

import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.statements.StatementAtom;

public abstract class AssignmentAtom extends StatementAtom {
  AssignableAtom assignable;
}

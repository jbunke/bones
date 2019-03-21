package structural_representation.atoms.statements.control_flow;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.statements.StatementAtom;

import java.util.List;

public class IfStatementAtom extends StatementAtom {
  private final List<ExpressionAtom> conditions;
  private final List<List<StatementAtom>> bodies;
  private final boolean hasElse;

  public IfStatementAtom(List<ExpressionAtom> conditions,
                         List<List<StatementAtom>> bodies) {
    if (conditions.size() > bodies.size() ||
            conditions.size() + 1 < bodies.size() ) {
      throw new IllegalArgumentException();
    }

    this.conditions = conditions;
    this.bodies = bodies;

    hasElse = conditions.size() < bodies.size();
  }
}

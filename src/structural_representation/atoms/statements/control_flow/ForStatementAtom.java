package structural_representation.atoms.statements.control_flow;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.statements.InitialisationAtom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.statements.assignments.AssignmentAtom;

import java.util.List;

public class ForStatementAtom extends StatementAtom {
  private final InitialisationAtom initialisation;
  private final ExpressionAtom loopCondition;
  private final AssignmentAtom incrementation;

  private final List<StatementAtom> body;

  public ForStatementAtom(InitialisationAtom initialisation,
                          ExpressionAtom loopCondition,
                          AssignmentAtom incrementation,
                          List<StatementAtom> body) {
    this.initialisation = initialisation;
    this.loopCondition = loopCondition;
    this.incrementation = incrementation;
    this.body = body;
  }
}

package structural_representation.atoms.statements.control_flow;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.statements.StatementAtom;

import java.util.List;

public class WhileStatementAtom extends StatementAtom {
  private final ExpressionAtom loopCondition;

  private final List<StatementAtom> body;

  public WhileStatementAtom(ExpressionAtom loopCondition,
                            List<StatementAtom> body) {
    this.loopCondition = loopCondition;
    this.body = body;
  }
}

package structural_representation.atoms.statements.control_flow;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.statements.StatementAtom;

import java.util.List;

public class ForEachStatementAtom extends StatementAtom {
  private final IdentifierAtom token;
  private final ExpressionAtom collection;

  private final List<StatementAtom> body;

  public ForEachStatementAtom(IdentifierAtom token,
                              ExpressionAtom collection,
                              List<StatementAtom> body) {
    this.token = token;
    this.collection = collection;
    this.body = body;
  }
}

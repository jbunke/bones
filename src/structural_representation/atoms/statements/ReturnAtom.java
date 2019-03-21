package structural_representation.atoms.statements;

import structural_representation.atoms.expressions.ExpressionAtom;

public class ReturnAtom extends StatementAtom {
  private final ExpressionAtom expression;

  public ReturnAtom(ExpressionAtom expression) {
    this.expression = expression;
  }
}

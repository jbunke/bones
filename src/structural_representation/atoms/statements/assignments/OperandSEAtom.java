package structural_representation.atoms.statements.assignments;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.AssignableAtom;

public class OperandSEAtom extends AssignmentAtom {
  private final Operator operator;
  private final ExpressionAtom expression;

  public OperandSEAtom(AssignableAtom assignable, Operator operator,
                       ExpressionAtom expression) {
    this.assignable = assignable;
    this.operator = operator;
    this.expression = expression;
  }

  public enum Operator {
    ADD_ASSIGN, SUB_ASSIGN,
    MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN,
    AND_ASSIGN, OR_ASSIGN
  }
}

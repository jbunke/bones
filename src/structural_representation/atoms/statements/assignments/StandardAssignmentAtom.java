package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import execution.StatementControl;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public class StandardAssignmentAtom extends AssignmentAtom {
  private final ExpressionAtom RHS;

  public StandardAssignmentAtom(AssignableAtom assignable,
                                ExpressionAtom RHS) {
    this.assignable = assignable;
    this.RHS = RHS;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    if (assignable instanceof IdentifierAtom) {
      table.update(assignable.toString(), RHS.evaluate(table, errorListener));
    }
    // TODO: else if list elem or array elem

    return StatementControl.cont();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    RHS.semanticErrorCheck(symbolTable, errorListener);
    BonesType rhsType = RHS.getType(symbolTable);
    if (!assignable.getType(symbolTable).equals(rhsType)) {
      errorListener.semanticError(ErrorMessages.
              expectedTypeButExpressionIs("Assignment",
                      assignable.getType(symbolTable), rhsType));
    }
  }

  @Override
  public String toString() {
    return assignable.toString() + " = " + RHS.toString() + ";";
  }
}

package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public class StandardAssignmentAtom extends AssignmentAtom {
  private final ExpressionAtom RHS;

  public StandardAssignmentAtom(AssignableAtom assignable,
                                ExpressionAtom RHS, Position position) {
    this.assignable = assignable;
    this.RHS = RHS;
    this.position = position;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    assignable.assignmentSymbolTableUpdate(table,
            RHS.evaluate(table, errorListener));

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
                      assignable.getType(symbolTable), rhsType),
              getPosition().getLine(), getPosition().getPositionInLine());
    }
  }

  @Override
  public String toString() {
    return assignable.toString() + " = " + RHS.toString() + ";";
  }
}

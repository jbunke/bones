package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.special.rhs.RHSAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public class StandardAssignmentAtom extends AssignmentAtom {
  private final RHSAtom RHS;

  public StandardAssignmentAtom(AssignableAtom assignable,
                                RHSAtom RHS, Position position) {
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
    if (!rhsType.equals(assignable.getType(symbolTable))) {
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

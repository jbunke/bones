package structural_representation.atoms.statements.control_flow;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.statements.InitialisationAtom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.statements.assignments.AssignmentAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.symbol_table.SymbolTable;

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

  @Override
  public void returnTypeSet(BonesType returnType) {
    body.forEach(x -> x.returnTypeSet(returnType));
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    SymbolTable localTable = new SymbolTable(this, symbolTable);
    initialisation.semanticErrorCheck(localTable, errorListener);

    if (!loopCondition.getType(localTable).equals(new BoolType())) {
      errorListener.semanticError(ErrorMessages.conditionIsNotBoolean());
    }

    incrementation.semanticErrorCheck(localTable, errorListener);

    body.forEach(x -> x.semanticErrorCheck(localTable, errorListener));
  }
}

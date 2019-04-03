package structural_representation.atoms.statements.control_flow;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import formatting.Tabs;
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
                          List<StatementAtom> body, Position position) {
    this.initialisation = initialisation;
    this.loopCondition = loopCondition;
    this.incrementation = incrementation;
    this.body = body;
    this.position = position;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    SymbolTable localTable = table.findChild(this);

    StatementControl status = StatementControl.cont();

    initialisation.execute(localTable, errorListener);

    while ((Boolean) loopCondition.evaluate(localTable, errorListener)) {
      for (StatementAtom statement : body) {
        if (!status.shouldContinue()) return status;
        status = statement.execute(localTable, errorListener);
      }

      incrementation.execute(localTable, errorListener);
    }

    return status;
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
      errorListener.semanticError(ErrorMessages.conditionIsNotBoolean(),
              getPosition().getLine(), getPosition().getPositionInLine());
    }

    incrementation.semanticErrorCheck(localTable, errorListener);

    body.forEach(x -> x.semanticErrorCheck(localTable, errorListener));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("for (");
    sb.append(initialisation.toString());
    sb.append(" ");
    sb.append(loopCondition.toString());
    sb.append("; ");
    sb.append(incrementation.toString());
    sb.append(") {\n");

    body.forEach(x -> sb.append(Tabs.tabLines(x.toString())));
    sb.append("}");

    return sb.toString();
  }
}

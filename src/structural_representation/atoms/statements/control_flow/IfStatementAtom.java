package structural_representation.atoms.statements.control_flow;

import error.BonesErrorListener;
import error.ErrorMessages;
import execution.StatementControl;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class IfStatementAtom extends StatementAtom {
  private final List<ExpressionAtom> conditions;
  private final List<List<StatementAtom>> bodies;
  private final boolean hasElse;

  public IfStatementAtom(List<ExpressionAtom> conditions,
                         List<List<StatementAtom>> bodies) {
    if (conditions.size() > bodies.size() ||
            conditions.size() + 1 < bodies.size() ) {
      throw new IllegalArgumentException();
    }

    this.conditions = conditions;
    this.bodies = bodies;

    hasElse = conditions.size() < bodies.size();
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    SymbolTable localTable = table.findChild(this);

    StatementControl status = StatementControl.cont();
    boolean triggeredCondition = false;

    for (int i = 0; !triggeredCondition && i < conditions.size(); i++) {
      triggeredCondition = (Boolean) conditions.get(i).evaluate(
              localTable, errorListener);
      if (triggeredCondition) {
        for (StatementAtom statement : bodies.get(i)) {
          if (!status.shouldContinue()) return status;
          status = statement.execute(localTable, errorListener);
        }
      }
    }

    if (!triggeredCondition && hasElse) {
      for (StatementAtom statement : bodies.get(bodies.size() - 1)) {
        if (!status.shouldContinue()) return status;
        status = statement.execute(localTable, errorListener);
      }
    }

    return status;
  }

  @Override
  public void returnTypeSet(BonesType returnType) {
    bodies.forEach(
            body -> body.forEach(
                    x -> x.returnTypeSet(returnType))
    );
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    SymbolTable localTable = new SymbolTable(this, symbolTable);
    for (ExpressionAtom condition : conditions) {
      if (!condition.getType(localTable).equals(new BoolType())) {
        errorListener.semanticError(ErrorMessages.conditionIsNotBoolean());
      }
    }

    bodies.forEach(
            body -> body.forEach(
                    x -> x.semanticErrorCheck(localTable, errorListener))
    );
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < bodies.size(); i++) {
      if (i > 0) sb.append("else ");

      if (i < conditions.size()) {
        sb.append("if (");
        sb.append(conditions.get(i));
        sb.append(") ");
      }

      sb.append("{\n");

      bodies.get(i).forEach(x -> {
        sb.append("\t");
        sb.append(x.toString());
        sb.append("\n");
      });
      sb.append("} ");
    }

    return sb.toString();
  }
}

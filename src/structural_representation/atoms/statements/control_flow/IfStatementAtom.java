package structural_representation.atoms.statements.control_flow;

import error.BonesErrorListener;
import error.ErrorMessages;
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
}

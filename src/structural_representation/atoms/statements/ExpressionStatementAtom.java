package structural_representation.atoms.statements;

import error.BonesErrorListener;
import execution.StatementControl;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.symbol_table.SymbolTable;

public class ExpressionStatementAtom extends StatementAtom {
  private final ExpressionAtom expression;

  public ExpressionStatementAtom(ExpressionAtom expression) {
    this.expression = expression;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    expression.evaluate(table, errorListener);

    return StatementControl.cont();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    expression.semanticErrorCheck(symbolTable, errorListener);
  }

  @Override
  public String toString() {
    return expression.toString() + ";";
  }
}

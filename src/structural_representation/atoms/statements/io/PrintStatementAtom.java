package structural_representation.atoms.statements.io;

import error.BonesErrorListener;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.symbol_table.SymbolTable;

public class PrintStatementAtom extends StatementAtom {
  private final ExpressionAtom toPrint;
  private final boolean ln;

  public PrintStatementAtom(ExpressionAtom toPrint,
                            boolean ln, Position position) {
    this.toPrint = toPrint;
    this.ln = ln;
    this.position = position;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    Object value = toPrint.evaluate(table, errorListener);

    if (ln) System.out.println(value.toString());
    else System.out.print(value.toString());

    return StatementControl.cont();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    toPrint.semanticErrorCheck(symbolTable, errorListener);
  }

  @Override
  public String toString() {
    return "print" + (ln ? "ln(" : "(") + toPrint.toString() + ");";
  }
}

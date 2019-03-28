package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public class ReturnAtom extends StatementAtom {
  private final ExpressionAtom expression;
  private BonesType expectedReturnType;

  public ReturnAtom(ExpressionAtom expression, Position position) {
    this.expression = expression;
    this.position = position;
  }

  @Override
  public void returnTypeSet(BonesType returnType) {
    expectedReturnType = returnType;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    return StatementControl.returnWith(
            expression.evaluate(table, errorListener));
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    expression.semanticErrorCheck(symbolTable, errorListener);
    BonesType actualType = expression.getType(symbolTable);
    if (!expectedReturnType.equals(actualType)) {
      errorListener.semanticError(
              ErrorMessages.expectedTypeButExpressionIs("Return",
                      expectedReturnType, actualType),
              getPosition().getLine(), getPosition().getPositionInLine());
    }
  }

  @Override
  public String toString() {
    return "return " + expression.toString() + ";";
  }
}

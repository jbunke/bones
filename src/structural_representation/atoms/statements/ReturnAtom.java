package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public class ReturnAtom extends StatementAtom {
  private final ExpressionAtom expression;
  private BonesType expectedReturnType;

  public ReturnAtom(ExpressionAtom expression) {
    this.expression = expression;
  }

  @Override
  public void returnTypeSet(BonesType returnType) {
    expectedReturnType = returnType;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    BonesType actualType = expression.getType(symbolTable);
    if (!expectedReturnType.equals(actualType)) {
      errorListener.semanticError(
              ErrorMessages.expectedTypeButExpressionIs("Return",
                      expectedReturnType, actualType));
    }
  }

  @Override
  public String toString() {
    return "return " + expression.toString() + ";";
  }
}

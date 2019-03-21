package structural_representation.atoms.expressions.assignables;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.Variable;

public class IdentifierAtom extends AssignableAtom {
  private final String token;

  public IdentifierAtom(String token) {
    this.token = token;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    Symbol symbol = table.get(token);
    Variable variable = (Variable) symbol;

    return variable.getType();
  }

  @Override
  public String toString() {
    return token;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    Symbol symbol = symbolTable.get(token);
    if (symbol == null) {
      errorListener.semanticError(ErrorMessages.
              variableHasNotBeenDeclared(token));
    } else if (!(symbol instanceof Variable)) {
      errorListener.semanticError(ErrorMessages.
              identifierIsNotAVariable(token));
    }
  }
}

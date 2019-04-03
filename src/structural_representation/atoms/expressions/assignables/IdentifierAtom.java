package structural_representation.atoms.expressions.assignables;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import structural_representation.Compile;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.Variable;

public class IdentifierAtom extends AssignableAtom {
  private final String token;

  public IdentifierAtom(String token, Position position) {
    this.token = token;
    this.position = position;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    Symbol symbol = table.get(token);
    Variable variable = (Variable) symbol;

    return variable.getType();
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    Variable variable = (Variable) table.get(token);
    Object value =  variable.getValue();

    if (value == null) errorListener.runtimeError(ErrorMessages.nullPointer(),
            true, Compile.RUNTIME_ERROR_EXIT,
            getPosition().getLine(), getPosition().getPositionInLine());

    return value;
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
              variableHasNotBeenDeclared(token),
              getPosition().getLine(), getPosition().getPositionInLine());
    } else if (!(symbol instanceof Variable)) {
      errorListener.semanticError(ErrorMessages.
              identifierIsNotAVariable(token),
              getPosition().getLine(), getPosition().getPositionInLine());
    }
  }
}

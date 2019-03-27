package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

public class DeclarationAtom extends StatementAtom {
  final BonesType type;
  final IdentifierAtom ident;

  public DeclarationAtom(BonesType type, IdentifierAtom ident) {
    this.type = type;
    this.ident = ident;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    type.semanticErrorCheck(symbolTable, errorListener);

    if (symbolTable.tableContainsKeyInScope(ident.toString())) {
      errorListener.semanticError(
              ErrorMessages.alreadyDeclaredInScope(ident.toString()));
    }

    symbolTable.put(ident.toString(), new Variable(type));
  }

  @Override
  public String toString() {
    return type.toString() + " " + ident.toString() + ";";
  }
}

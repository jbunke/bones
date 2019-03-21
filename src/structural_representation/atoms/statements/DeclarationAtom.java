package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public class DeclarationAtom extends StatementAtom {
  final BonesType type;
  private final IdentifierAtom ident;

  public DeclarationAtom(BonesType type, IdentifierAtom ident) {
    this.type = type;
    this.ident = ident;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    if (symbolTable.tableContainsKeyInScope(ident.toString())) {
      errorListener.semanticError(
              ErrorMessages.alreadyDeclaredInScope(ident.toString()));
    }
  }
}

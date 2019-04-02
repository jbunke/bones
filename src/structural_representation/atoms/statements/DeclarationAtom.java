package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

public class DeclarationAtom extends StatementAtom {
  final BonesType type;
  final IdentifierAtom ident;

  public DeclarationAtom(BonesType type, IdentifierAtom ident,
                         Position position) {
    this.type = type;
    this.ident = ident;
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    type.semanticErrorCheck(symbolTable, errorListener);

    if (symbolTable.tableContainsKeyInScope(ident.toString())) {
      errorListener.semanticError(
              ErrorMessages.alreadyDeclaredInScope(ident.toString()),
              getPosition().getLine(), getPosition().getPositionInLine());
    }

    symbolTable.put(ident.toString(), new Variable(type, ident.toString()));
  }

  @Override
  public String toString() {
    return type.toString() + " " + ident.toString() + ";";
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    table.put(ident.toString(), new Variable(type, ident.toString()));

    return StatementControl.cont();
  }
}

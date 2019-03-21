package structural_representation.atoms.special;

import error.BonesErrorListener;
import structural_representation.atoms.Atom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

public class ParamAtom extends Atom {
  private final BonesType type;
  private final IdentifierAtom ident;

  public ParamAtom(BonesType type, IdentifierAtom ident) {
    this.type = type;
    this.ident = ident;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    type.semanticErrorCheck(symbolTable, errorListener);
    symbolTable.update(ident.toString(), new Variable(type));
  }
}

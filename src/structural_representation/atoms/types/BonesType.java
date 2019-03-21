package structural_representation.atoms.types;

import error.BonesErrorListener;
import structural_representation.atoms.Atom;
import structural_representation.symbol_table.SymbolTable;

public abstract class BonesType extends Atom {
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) { }
}

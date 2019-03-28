package structural_representation.atoms;

import error.BonesErrorListener;
import structural_representation.symbol_table.SymbolTable;

public abstract class Atom {
  public abstract void semanticErrorCheck(SymbolTable symbolTable,
                                          BonesErrorListener errorListener);
}

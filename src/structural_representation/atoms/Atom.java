package structural_representation.atoms;

import error.BonesErrorListener;
import error.Position;
import structural_representation.symbol_table.SymbolTable;

public abstract class Atom {
  public abstract void semanticErrorCheck(SymbolTable symbolTable,
                                          BonesErrorListener errorListener);

  public Position position;

  public Position getPosition() {
    return position;
  }
}

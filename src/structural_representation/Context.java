package structural_representation;

import error.BonesErrorListener;
import structural_representation.atoms.Atom;
import structural_representation.symbol_table.SymbolTable;

public class Context {
  private final Atom structure;
  private SymbolTable symbolTable;
  private final BonesErrorListener errorListener;

  Context(Atom structure, SymbolTable symbolTable,
                 BonesErrorListener errorListener) {
    this.structure = structure;
    this.errorListener = errorListener;
    this.symbolTable = symbolTable;
  }

//  void setSymbolTable(SymbolTable symbolTable) {
//    this.symbolTable = symbolTable;
//  }

  public Atom getStructure() {
    return structure;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public BonesErrorListener getErrorListener() {
    return errorListener;
  }
}

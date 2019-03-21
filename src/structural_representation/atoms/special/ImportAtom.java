package structural_representation.atoms.special;

import error.BonesErrorListener;
import structural_representation.atoms.Atom;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class ImportAtom extends Atom {
  private final List<String> path;

  public ImportAtom(List<String> path) {
    this.path = path;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    // TODO
  }
}

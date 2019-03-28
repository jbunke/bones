package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.Position;
import structural_representation.atoms.Atom;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class PathAtom extends Atom {
  private final List<String> path;

  public PathAtom(List<String> path, Position position) {
    this.path = path;
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    // TODO
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("path ");

    for (int i = 0; i < path.size(); i++) {
      if (i > 0) sb.append(".");

      sb.append(path.get(i));
    }
    sb.append(";");

    return sb.toString();
  }
}

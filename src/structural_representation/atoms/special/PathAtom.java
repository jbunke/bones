package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.Position;
import structural_representation.atoms.Atom;
import structural_representation.symbol_table.SymbolTable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PathAtom extends Atom {
  private final List<String> path;

  public PathAtom(List<String> path, Position position) {
    this.path = path;
    this.position = position;
  }

  public String cutoffPathFromFilepath(String filepath) {
    List<String> fp =
            new LinkedList<>(Arrays.asList(filepath.split("/")));

    // pull off filename
    fp.remove(fp.size() - 1);

    int index = 1;
    while (index <= path.size() && fp.size() > 0 &&
            fp.get(fp.size() - 1).equals(path.get(path.size() - index))) {
      fp.remove(fp.size() - 1);
      index++;
    }

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < fp.size(); i++) {
      if (i > 0) sb.append("/");
      sb.append(fp.get(i));
    }

    return sb.toString();
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

package structural_representation.atoms.special;

import structural_representation.atoms.Atom;

import java.util.List;

public class ImportAtom extends Atom {
  private final List<String> path;

  public ImportAtom(List<String> path) {
    this.path = path;
  }
}

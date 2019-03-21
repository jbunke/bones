package structural_representation.atoms.special;

import structural_representation.atoms.Atom;

import java.util.List;

public class PathAtom extends Atom {
  private final List<String> path;

  public PathAtom(List<String> path) {
    this.path = path;
  }
}

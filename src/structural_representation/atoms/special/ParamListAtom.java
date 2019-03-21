package structural_representation.atoms.special;

import structural_representation.atoms.Atom;

import java.util.List;

public class ParamListAtom extends Atom {
  private final List<ParamAtom> params;

  public ParamListAtom(List<ParamAtom> params) {
    this.params = params;
  }
}

package structural_representation.atoms.special;

import structural_representation.atoms.Atom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;

public class ParamAtom extends Atom {
  private final BonesType type;
  private final IdentifierAtom ident;

  public ParamAtom(BonesType type, IdentifierAtom ident) {
    this.type = type;
    this.ident = ident;
  }
}

package structural_representation.atoms.statements;

import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;

public class DeclarationAtom extends StatementAtom {
  private final BonesType type;
  private final IdentifierAtom ident;

  public DeclarationAtom(BonesType type, IdentifierAtom ident) {
    this.type = type;
    this.ident = ident;
  }
}

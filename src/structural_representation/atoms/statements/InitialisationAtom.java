package structural_representation.atoms.statements;

import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;

public class InitialisationAtom extends DeclarationAtom {
  private final ExpressionAtom RHS;

  public InitialisationAtom(BonesType type, IdentifierAtom ident,
                            ExpressionAtom RHS) {
    super(type, ident);
    this.RHS = RHS;
  }
}

package structural_representation.atoms.expressions.assignables;

import structural_representation.symbol_table.SymbolTable;
import structural_representation.atoms.types.BonesType;

public class IdentifierAtom extends AssignableAtom {
  private final String token;

  public IdentifierAtom(String token) {
    this.token = token;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    // TODO
    return null;
  }

  @Override
  public String toString() {
    return token;
  }
}

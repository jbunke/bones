package structural_representation.symbol_table;

import structural_representation.atoms.types.BonesType;

public class Variable implements Symbol {
  private final BonesType type;
  private Object value;

  public Variable(BonesType type) {
    this.type = type;
    this.value = null;
  }

  @Override
  public BonesType getType() {
    return type;
  }
}

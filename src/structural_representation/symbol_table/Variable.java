package structural_representation.symbol_table;

import structural_representation.atoms.types.BonesType;

public class Variable implements Symbol {
  private final BonesType type;
  private final String ident;
  private Object value;

  public Variable(BonesType type, String ident) {
    this.type = type;
    this.ident = ident;
    this.value = null;
  }

  public Variable(BonesType type, String ident, Object value) {
    this.type = type;
    this.ident = ident;
    this.value = value;
  }

  void update(Object value) { this.value = value; }

  public Object getValue() {
    return value;
  }

  @Override
  public BonesType getType() {
    return type;
  }

  @Override
  public String toString() {
    if (value == null)
      return "( " + type.toString() + " ) " + ident;

    return "( " + type.toString() + " ) " + ident + " -> " + value.toString();
  }
}

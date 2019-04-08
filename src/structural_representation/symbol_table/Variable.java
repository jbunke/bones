package structural_representation.symbol_table;

import execution.Instance;
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

  Variable cloneVar() {
    return new Variable(type, ident, value);
  }

  void update(Object value) { this.value = value; }

  public Object getValue() {
    return value;
  }

  public String identAndValue() {
    return ident + " -> " +
            ((value instanceof Instance ? "\n" : "")) + value.toString();
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

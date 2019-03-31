package structural_representation.atoms.types.primitives;

import error.Position;
import structural_representation.atoms.types.BonesType;

public class BoolType extends BonesType {
  public BoolType() {
    this.position = new Position(-1, -1);
  }

  public BoolType(Position position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BoolType;
  }

  @Override
  public String toString() {
    return "bool";
  }

  @Override
  public Object defaultValue() {
    return false;
  }
}

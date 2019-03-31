package structural_representation.atoms.types.primitives;

import error.Position;
import structural_representation.atoms.types.BonesType;

public class FloatType extends BonesType {
  public FloatType() {
    this.position = new Position(-1, -1);
  }

  public FloatType(Position position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof FloatType;
  }

  @Override
  public String toString() {
    return "float";
  }

  @Override
  public Object defaultValue() {
    return 0f;
  }
}

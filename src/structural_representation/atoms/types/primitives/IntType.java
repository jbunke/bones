package structural_representation.atoms.types.primitives;

import error.Position;
import structural_representation.atoms.types.BonesType;

public class IntType extends BonesType {
  public IntType() {
    this.position = new Position(-1, -1);
  }

  public IntType(Position position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof IntType;
  }

  @Override
  public String toString() {
    return "int";
  }

  @Override
  public Object defaultValue() {
    return 0;
  }
}

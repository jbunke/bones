package structural_representation.atoms.types.primitives;

import error.Position;
import structural_representation.atoms.types.BonesType;

public class StringType extends BonesType {
  public StringType() {
    this.position = new Position(-1, -1);
  }

  public StringType(Position position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof StringType;
  }

  @Override
  public String toString() {
    return "string";
  }

  @Override
  public Object defaultValue() {
    return "";
  }
}

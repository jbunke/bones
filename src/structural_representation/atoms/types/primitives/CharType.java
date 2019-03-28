package structural_representation.atoms.types.primitives;

import error.Position;
import structural_representation.atoms.types.BonesType;

public class CharType extends BonesType {
  public CharType() {
    this.position = new Position(-1, -1);
  }

  public CharType(Position position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof CharType;
  }

  @Override
  public String toString() {
    return "char";
  }
}

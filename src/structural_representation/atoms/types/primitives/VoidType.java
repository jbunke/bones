package structural_representation.atoms.types.primitives;

import error.Position;
import structural_representation.atoms.types.BonesType;

public class VoidType extends BonesType {
  public VoidType() {
    this.position = new Position(-1, -1);
  }

  public VoidType(Position position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof VoidType;
  }

  @Override
  public String toString() {
    return "void";
  }
}

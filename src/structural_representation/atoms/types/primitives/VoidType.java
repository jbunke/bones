package structural_representation.atoms.types.primitives;

import structural_representation.atoms.types.BonesType;

public class VoidType extends BonesType {
  public VoidType() {}

  @Override
  public boolean equals(Object obj) {
    return obj instanceof VoidType;
  }

  @Override
  public String toString() {
    return "void";
  }
}

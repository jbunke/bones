package structural_representation.atoms.types.primitives;

import structural_representation.atoms.types.BonesType;

public class BoolType extends BonesType {
  public BoolType() {}

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BoolType;
  }

  @Override
  public String toString() {
    return "bool";
  }
}

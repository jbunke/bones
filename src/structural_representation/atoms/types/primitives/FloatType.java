package structural_representation.atoms.types.primitives;

import structural_representation.atoms.types.BonesType;

public class FloatType extends BonesType {
  public FloatType() {}

  @Override
  public boolean equals(Object obj) {
    return obj instanceof FloatType;
  }

  @Override
  public String toString() {
    return "float";
  }
}

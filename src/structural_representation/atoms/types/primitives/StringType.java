package structural_representation.atoms.types.primitives;

import structural_representation.atoms.types.BonesType;

public class StringType extends BonesType {
  public StringType() {}

  @Override
  public boolean equals(Object obj) {
    return obj instanceof StringType;
  }

  @Override
  public String toString() {
    return "string";
  }
}

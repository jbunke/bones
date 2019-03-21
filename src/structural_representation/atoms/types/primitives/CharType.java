package structural_representation.atoms.types.primitives;

import structural_representation.atoms.types.BonesType;

public class CharType extends BonesType {
  public CharType() {}

  @Override
  public boolean equals(Object obj) {
    return obj instanceof CharType;
  }

  @Override
  public String toString() {
    return "char";
  }
}

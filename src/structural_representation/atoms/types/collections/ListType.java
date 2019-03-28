package structural_representation.atoms.types.collections;

import structural_representation.atoms.types.BonesType;

public class ListType extends BonesType {
  private final BonesType elementType;

  public ListType(BonesType elementType) {
    this.elementType = elementType;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ListType) {
      ListType other = (ListType) obj;
      return elementType.equals(other.elementType);
    }
    return false;
  }

  public BonesType getElementType() {
    return elementType;
  }

  @Override
  public String toString() {
    return elementType.toString() + "()";
  }
}

package structural_representation.atoms.types.collections;

import error.Position;
import execution.BonesList;
import structural_representation.atoms.types.BonesType;

public class ListType extends BonesType {
  private final BonesType elementType;

  public ListType(BonesType elementType) {
    this.elementType = elementType;
  }

  public ListType(BonesType elementType, Position position) {
    this.elementType = elementType;
    this.position = position;
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

  @Override
  public Object defaultValue() {
    return new BonesList<>();
  }
}

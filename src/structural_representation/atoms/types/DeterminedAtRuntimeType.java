package structural_representation.atoms.types;

public class DeterminedAtRuntimeType extends BonesType {
  public DeterminedAtRuntimeType() { }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BonesType;
  }

  @Override
  public Object defaultValue() {
    return null;
  }
}

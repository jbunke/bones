package structural_representation.atoms.types.collections;

import error.BonesErrorListener;
import error.Position;
import execution.BonesArray;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public class ArrayType extends BonesType {
  private final BonesType elementType;

  public ArrayType(BonesType elementType) {
    this.elementType = elementType;
  }

  public ArrayType(BonesType elementType, Position position) {
    this.elementType = elementType;
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable, BonesErrorListener errorListener) {
    super.semanticErrorCheck(symbolTable, errorListener);

    elementType.semanticErrorCheck(symbolTable, errorListener);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ArrayType) {
      ArrayType other = (ArrayType) obj;
      return elementType.equals(other.elementType);
    }
    return false;
  }

  public BonesType getElementType() {
    return elementType;
  }

  @Override
  public String toString() {
    return elementType.toString() + "[]";
  }

  @Override
  public Object defaultValue() {
    return new BonesArray<>();
  }
}

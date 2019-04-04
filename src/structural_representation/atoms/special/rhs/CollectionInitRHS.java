package structural_representation.atoms.special.rhs;

import error.BonesErrorListener;
import error.Position;
import execution.BonesArray;
import execution.BonesList;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.symbol_table.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class CollectionInitRHS extends RHSAtom {
  private final BonesType elementType;
  private final int size;
  private final CollectionType collectionType;

  public CollectionInitRHS(BonesType elementType, int size,
                           CollectionType collectionType,
                           Position position) {
    this.elementType = elementType;
    this.size = size;
    this.collectionType = collectionType;
    this.position = position;
  }

  public enum CollectionType {
    LIST,
    ARRAY
  }

  @Override
  public BonesType getType(SymbolTable table) {
    if (collectionType == CollectionType.LIST) {
      return new ListType(elementType);
    }
    return new ArrayType(elementType);
  }

  @Override
  public Object evaluate(SymbolTable table,
                         BonesErrorListener errorListener) {
    List<Object> res = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      res.add(elementType.defaultValue());
    }

    if (collectionType == CollectionType.LIST) return BonesList.fromList(res);

    return BonesArray.fromList(res);
  }

  @Override
  public String toString() {
    return elementType.toString() +
            (collectionType == CollectionType.LIST ? "(" : "[") +
            String.valueOf(size) +
            (collectionType == CollectionType.LIST ? ")" : "]");
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    elementType.semanticErrorCheck(symbolTable, errorListener);
  }
}

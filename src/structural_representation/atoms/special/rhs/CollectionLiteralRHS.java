package structural_representation.atoms.special.rhs;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.BonesArray;
import execution.BonesList;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.VoidType;
import structural_representation.symbol_table.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class CollectionLiteralRHS extends RHSAtom {
  private final List<RHSAtom> elements;
  private final CollectionType collectionType;

  public CollectionLiteralRHS(List<RHSAtom> elements,
                              CollectionType collectionType,
                              Position position) {
    this.elements = elements;
    this.collectionType = collectionType;
    this.position = position;
  }

  public enum CollectionType {
    LIST,
    ARRAY
  }

  @Override
  public BonesType getType(SymbolTable table) {
    BonesType elementType = new VoidType();

    if (!elements.isEmpty()) {
      elementType = elements.get(0).getType(table);
    }

    if (collectionType == CollectionType.LIST)
      return new ListType(elementType);

    return new ArrayType(elementType);
  }

  @Override
  public Object evaluate(SymbolTable table,
                         BonesErrorListener errorListener) {
    List<Object> res = new ArrayList<>();

    elements.forEach(x -> res.add(x.evaluate(table, errorListener)));

    if (collectionType == CollectionType.LIST) {
      return BonesList.fromList(res);
    }

    return BonesArray.fromList(res);
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    if (!elements.isEmpty()) {
      BonesType elementType = elements.get(0).getType(symbolTable);
      for (RHSAtom element : elements) {
        element.semanticErrorCheck(symbolTable, errorListener);
        if (!elementType.equals(element.getType(symbolTable))) {
          errorListener.semanticError(
                  ErrorMessages.typesOfCollectionLiteralElementsDontMatch(
                          collectionType.toString().toLowerCase()),
                  element.getPosition().getLine(),
                  element.getPosition().getPositionInLine());
          break;
        }
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(
            collectionType.toString().toLowerCase());
    sb.append(" { ");

    for (int i = 0; i < elements.size(); i++) {
      if (i > 0) sb.append(", ");

      sb.append(elements.get(i).toString());
    }

    sb.append(" }");
    return sb.toString();
  }
}

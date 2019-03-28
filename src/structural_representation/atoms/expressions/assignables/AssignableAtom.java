package structural_representation.atoms.expressions.assignables;

import execution.BonesArray;
import execution.BonesList;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.VoidType;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

import java.util.List;

public abstract class AssignableAtom extends ExpressionAtom {
  BonesType getCollectionType(SymbolTable table,
                                     String identifier,
                                     List<Integer> indices) {
    if (indices == null || indices.size() == 0) {
      return new VoidType();
    }

    Symbol symbol = table.get(identifier);
    Variable variable = (Variable) symbol;

    BonesType type = variable.getType();
    for (int i = 0; i < indices.size(); i++) {
      if (type instanceof ListType) {
        type = ((ListType) type).getElementType();
      } else if (type instanceof ArrayType) {
        type = ((ArrayType) type).getElementType();
      } else {
        break;
      }
    }
    return type;
  }

  public void assignmentSymbolTableUpdate(SymbolTable table, Object value) {
    if (this instanceof IdentifierAtom) {
      table.update(this.toString(), value);
    } else if (this instanceof ArrayElemAtom) {
      ArrayElemAtom array = (ArrayElemAtom) this;
      table.updateCollection(array.getIdentifier(),
              array.getIndices(), value);
    } else if (this instanceof ListElemAtom) {
      ListElemAtom list = (ListElemAtom) this;
      table.updateCollection(list.getIdentifier(),
              list.getIndices(), value);
    }
  }

  public Object getInitialCollectionValue(SymbolTable table) {
    Object value = null;

    if (this instanceof IdentifierAtom) {
      value = ((Variable) table.get(toString())).getValue();
    } else if (this instanceof ArrayElemAtom ||
            this instanceof ListElemAtom) {
      Object reference;
      List<Integer> indices;

      if (this instanceof ArrayElemAtom) {
        ArrayElemAtom array = (ArrayElemAtom) this;
        reference =
                ((Variable) table.get(array.getIdentifier())).getValue();
        indices = array.getIndices();
      } else {
        ListElemAtom list = (ListElemAtom) this;
        reference =
                ((Variable) table.get(list.getIdentifier())).getValue();
        indices = list.getIndices();
      }

      for (int index : indices) {
        if (reference instanceof BonesArray) {
          value = ((BonesArray) reference).at(index);
          reference = ((BonesArray) reference).at(index);
        } else if (reference instanceof BonesList) {
          value = ((BonesList) reference).at(index);
          reference = ((BonesList) reference).at(index);
        } else break;
      }
    }

    return value;
  }
}

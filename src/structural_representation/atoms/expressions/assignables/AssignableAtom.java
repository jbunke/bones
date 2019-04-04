package structural_representation.atoms.expressions.assignables;

import error.BonesErrorListener;
import execution.BonesArray;
import execution.BonesList;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.DeterminedAtRuntimeType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

import java.util.ArrayList;
import java.util.List;

public abstract class AssignableAtom extends ExpressionAtom {
  BonesType getCollectionType(SymbolTable table,
                                     String identifier,
                                     List<ExpressionAtom> indices) {
    if (indices == null || indices.size() == 0) {
      return new DeterminedAtRuntimeType(); // TODO
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

  public void assignmentSymbolTableUpdate(SymbolTable table, Object value,
                                          BonesErrorListener errorListener) {
    if (this instanceof IdentifierAtom) {
      table.update(this.toString(), value);
    } else if (this instanceof ArrayElemAtom) {
      ArrayElemAtom array = (ArrayElemAtom) this;
      table.updateCollection(array.getIdentifier(),
              generateIndices(array.getIndices(), table, errorListener), value);
    } else if (this instanceof ListElemAtom) {
      ListElemAtom list = (ListElemAtom) this;
      table.updateCollection(list.getIdentifier(),
              generateIndices(list.getIndices(), table, errorListener), value);
    }
  }

  private List<Integer> generateIndices(List<ExpressionAtom> expressions,
                                        SymbolTable table,
                                        BonesErrorListener errorListener) {
    List<Integer> indices = new ArrayList<>();

    expressions.forEach(x ->
            indices.add((Integer) x.evaluate(table, errorListener)));

    return indices;
  }

  public Object getInitialCollectionValue(SymbolTable table,
                                          BonesErrorListener errorListener) {
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
        indices = generateIndices(array.getIndices(), table, errorListener);
      } else {
        ListElemAtom list = (ListElemAtom) this;
        reference =
                ((Variable) table.get(list.getIdentifier())).getValue();
        indices = generateIndices(list.getIndices(), table, errorListener);
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

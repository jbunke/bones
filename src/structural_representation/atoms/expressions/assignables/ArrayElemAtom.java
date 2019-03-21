package structural_representation.atoms.expressions.assignables;

import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class ArrayElemAtom extends AssignableAtom {
  private final String identifier;
  private final List<Integer> indices;

  public ArrayElemAtom(String identifier, List<Integer> indices) {
    this.identifier = identifier;
    this.indices = indices;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return null;
  }
}

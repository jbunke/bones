package execution;

import structural_representation.atoms.special.ClassAtom;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

import java.util.List;

public class Instance {
  private final ClassAtom classAtom;
  private final SymbolTable instanceTable;

  public Instance(ClassAtom classAtom, SymbolTable instanceTable) {
    this.classAtom = classAtom;
    this.instanceTable = instanceTable;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(classAtom.getClassName());
    sb.append(" object: (");

    List<Symbol> vars = instanceTable.getAll(SymbolTable.Filter.VARIABLES);

    for (int i = 0; i < vars.size(); i++) {
      if (i > 0) sb.append(", ");
      sb.append(((Variable) vars.get(i)).identAndValue());
    }

    return sb.append(")").toString();
  }
}

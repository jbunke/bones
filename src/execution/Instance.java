package execution;

import formatting.Tabs;
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
    sb.append(":\n");

    List<Symbol> vars = instanceTable.getAll(SymbolTable.Filter.VARIABLES);

    vars.forEach(x ->
            sb.append(Tabs.tabLines(((Variable) x).identAndValue())));

    return sb.toString();
  }
}

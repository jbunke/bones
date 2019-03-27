package structural_representation.symbol_table;

import structural_representation.atoms.Atom;
import structural_representation.atoms.special.FunctionAtom;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
  private final Atom scope;
  private final SymbolTable parent;
  private final Map<Atom, SymbolTable> children;
  private final Map<String, Symbol> contents;

  public SymbolTable(Atom scope, SymbolTable parent) {
    this.scope = scope;
    this.parent = parent;
    this.children = new HashMap<>();
    this.contents = new HashMap<>();

    if (parent != null) parent.addChild(scope, this);
  }

  private void addChild(Atom scope, SymbolTable symbolTable) {
    children.put(scope, symbolTable);
  }

  public void put(String key, Symbol symbol) {
    contents.put(key, symbol);
  }

  public void update(String key, Object value) {
    if (!contents.containsKey(key)) return;
    Symbol symbol = contents.get(key);
    if (!(symbol instanceof Variable)) return;
    Variable variable = (Variable) symbol;
    variable.update(value);
  }

  public boolean tableContainsKeyInScope(String key) {
    return contents.containsKey(key);
  }

  public Symbol get(String key) {
    if (contents.containsKey(key)) {
      return contents.get(key);
    } else if (parent != null) {
      return parent.get(key);
    }
    return null;
  }

  public Object evaluate(String key) {
    Symbol symbol = get(key);

    if (symbol != null && symbol instanceof Variable) {
      Variable var = (Variable) symbol;
      return var.getValue();
    }

    return null;
  }

  public SymbolTable tableForFunction(FunctionAtom function) {
    if (scope.equals(function)) return this;

    SymbolTable table = this;
    while (table.parent != null) table = table.parent;

    if (table.children.containsKey(function))
      return table.children.get(function);

    return null;
  }
}

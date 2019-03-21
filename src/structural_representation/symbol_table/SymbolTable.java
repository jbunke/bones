package structural_representation.symbol_table;

import structural_representation.atoms.Atom;

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

  public void update(String key, Symbol symbol) {
    contents.put(key, symbol);
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
}

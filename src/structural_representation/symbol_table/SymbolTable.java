package structural_representation.symbol_table;

import execution.BonesArray;
import execution.BonesList;
import structural_representation.atoms.Atom;
import structural_representation.atoms.special.FunctionAtom;

import java.util.HashMap;
import java.util.List;
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

  public SymbolTable findChild(Atom scope) {
    if (children.containsKey(scope)) return children.get(scope);

    return null;
  }

  public void put(String key, Symbol symbol) {
    contents.put(key, symbol);
  }

  public void update(String key, Object value) {
    if (!contents.containsKey(key)) {
      if (parent != null) parent.update(key, value);
      return;
    }
    Symbol symbol = contents.get(key);
    if (!(symbol instanceof Variable)) return;
    Variable variable = (Variable) symbol;
    variable.update(value);
  }

  public void updateCollection(String identifier,
                               List<Integer> indices, Object value) {
    if (!contents.containsKey(identifier)) {
      if (parent != null) parent.updateCollection(identifier, indices, value);
      return;
    }
    Symbol symbol = contents.get(identifier);
    if (!(symbol instanceof Variable)) return;
    Variable variable = (Variable) symbol;

    Object reference = variable.getValue();

    for (int i = 0; i < indices.size() - 1; i++) {
      if (reference instanceof BonesArray) {
        reference = ((BonesArray) reference).at(indices.get(i));
      } else if (reference instanceof BonesList) {
        reference = ((BonesList) reference).at(indices.get(i));
      }
    }

    if (reference instanceof BonesArray) {
      BonesArray array = (BonesArray) reference;
      array.set(indices.get(indices.size() - 1), value);
    } else if (reference instanceof BonesList) {
      BonesList list = (BonesList) reference;
      list.set(indices.get(indices.size() - 1), value);
    }
  }

  public boolean tableContainsKeyInScope(String key) {
    return contents.containsKey(key);
  }

  public Symbol get(String key) {
    if (contents.containsKey(key)) {
      return contents.get(key);
    } else if (contents.containsKey("param_" + key)) {
      return contents.get("param_" + key);
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

  public SymbolTable root() {
    if (parent == null) return this;
    return parent.root();
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

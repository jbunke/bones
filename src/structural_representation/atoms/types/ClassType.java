package structural_representation.atoms.types;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.Instance;
import structural_representation.atoms.special.ClassAtom;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;

public class ClassType extends BonesType implements Symbol {
  private ClassAtom classAtom;
  private SymbolTable classTable;

  private boolean resolved;
  private String ident;

  public ClassType(ClassAtom classAtom, SymbolTable classTable) {
    this.classAtom = classAtom;
    this.classTable = classTable;

    resolved = true;
  }

  public ClassType(String ident, Position position) {
    this.classAtom = null;
    this.classTable = null;

    this.ident = ident;
    this.position = position;

    resolved = false;
  }

  private void resolve(SymbolTable table, BonesErrorListener errorListener) {
    if (resolved) return;

    Symbol symbol = table.root().get(ident);

    if (symbol == null || !(symbol instanceof ClassType)) {
      errorListener.semanticError(ErrorMessages.invalidTypeIdentifier(ident),
              position.getLine(), position.getPositionInLine());
      return;
    }

    ClassType classType = (ClassType) symbol;

    this.classTable = classType.classTable;
    this.classAtom = classType.classAtom;

    resolved = true;
  }

  public ClassAtom getClassAtom() {
    return classAtom;
  }

  public SymbolTable getClassTable() {
    return classTable;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    super.semanticErrorCheck(symbolTable, errorListener);

    resolve(symbolTable, errorListener);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ClassType)) return false;

    ClassType other = (ClassType) obj;

    if (resolved)
      return classAtom.getClassName().equals(other.classAtom.getClassName());
    return ident.equals(other.ident);
  }

  @Override
  public Object defaultValue() {
    return null;
  }

  @Override
  public BonesType getType() {
    return this;
  }

  @Override
  public String toString() {
    if (classAtom == null) return ident;
    return classAtom.getClassName();
  }

  public Object generateInstance() {
    return new Instance(classAtom,
            classTable.cloneTable(classAtom, null));
  }
}

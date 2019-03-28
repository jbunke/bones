package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.Position;
import structural_representation.atoms.Atom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

public class ParamAtom extends Atom {
  private final BonesType type;
  private final IdentifierAtom ident;

  public ParamAtom(BonesType type, IdentifierAtom ident, Position position) {
    this.type = type;
    this.ident = ident;
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    type.semanticErrorCheck(symbolTable, errorListener);
    symbolTable.put("param_" + ident.toString(), new Variable(type));
  }

  public BonesType getType() {
    return type;
  }

  public IdentifierAtom getIdent() {
    return ident;
  }

  @Override
  public String toString() {
    return type.toString() + " " + ident.toString();
  }
}

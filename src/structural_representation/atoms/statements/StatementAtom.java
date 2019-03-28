package structural_representation.atoms.statements;

import error.BonesErrorListener;
import execution.StatementControl;
import structural_representation.atoms.Atom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public abstract class StatementAtom extends Atom {
  public void returnTypeSet(BonesType returnType) { }

  public StatementControl execute(SymbolTable table,
                                           BonesErrorListener errorListener) {
    return StatementControl.cont();
  }
}

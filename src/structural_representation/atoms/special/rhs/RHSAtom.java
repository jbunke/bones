package structural_representation.atoms.special.rhs;

import error.BonesErrorListener;
import structural_representation.atoms.Atom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public abstract class RHSAtom extends Atom {
  public abstract BonesType getType(SymbolTable table);

  public abstract Object evaluate(SymbolTable table,
                                  BonesErrorListener errorListener);
}

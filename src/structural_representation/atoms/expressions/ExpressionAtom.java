package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import structural_representation.atoms.Atom;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.atoms.types.BonesType;

public abstract class ExpressionAtom extends Atom {
  public abstract BonesType getType(SymbolTable table);

  public abstract Object evaluate(SymbolTable table,
                                  BonesErrorListener errorListener);
}

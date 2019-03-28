package structural_representation.atoms.types;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.Atom;
import structural_representation.atoms.types.primitives.VoidType;
import structural_representation.symbol_table.SymbolTable;

public abstract class BonesType extends Atom {
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    if (this instanceof VoidType) {
      errorListener.semanticError(ErrorMessages.attemptedToUseVoidType());
    }
  }
}

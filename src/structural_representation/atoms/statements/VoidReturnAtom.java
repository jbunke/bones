package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.VoidType;
import structural_representation.symbol_table.SymbolTable;

public class VoidReturnAtom extends StatementAtom {
  /* should always be void */
  private BonesType expectedReturnType;

  public VoidReturnAtom() {}

  @Override
  public void returnTypeSet(BonesType returnType) {
    expectedReturnType = returnType;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    if (!expectedReturnType.equals(new VoidType())) {
      errorListener.semanticError(ErrorMessages.voidReturnUsedInNonVoid());
    }
  }

  @Override
  public String toString() {
    return "return;";
  }
}

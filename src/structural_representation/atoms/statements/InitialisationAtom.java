package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

public class InitialisationAtom extends DeclarationAtom {
  private final ExpressionAtom RHS;

  public InitialisationAtom(BonesType type, IdentifierAtom ident,
                            ExpressionAtom RHS) {
    super(type, ident);
    this.RHS = RHS;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    super.semanticErrorCheck(symbolTable, errorListener);

    BonesType rhsType = RHS.getType(symbolTable);

    if (!type.equals(rhsType)) {
      errorListener.semanticError(ErrorMessages.
              expectedTypeButExpressionIs("Initialisation",
                      type, rhsType));
    }
  }

  @Override
  public String toString() {
    return type.toString() + " " + ident.toString() +
            " = " + RHS.toString() + ";";
  }
}

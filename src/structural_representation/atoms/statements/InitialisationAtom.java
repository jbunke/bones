package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.special.rhs.RHSAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.ClassType;
import structural_representation.symbol_table.SymbolTable;

public class InitialisationAtom extends DeclarationAtom {
  private final RHSAtom RHS;

  public InitialisationAtom(BonesType type, IdentifierAtom ident,
                            RHSAtom RHS, Position position) {
    super(type, ident, position);
    this.RHS = RHS;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    table.update(ident.toString(), RHS.evaluate(table, errorListener));

    return StatementControl.cont();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    super.semanticErrorCheck(symbolTable, errorListener);

    RHS.semanticErrorCheck(symbolTable, errorListener);

    if (errorListener.hasError()) return;

    BonesType rhsType = RHS.getType(symbolTable);

    if (!rhsType.equals(type)) {
      errorListener.semanticError(ErrorMessages.
                      expectedTypeButExpressionIs("Initialisation",
                              type, rhsType),
              getPosition().getLine(), getPosition().getPositionInLine());
    }

    if (type instanceof ClassType)
      symbolTable.update(ident.toString(),
              ((ClassType) type).generateInstance());
  }

  @Override
  public String toString() {
    return type.toString() + " " + ident.toString() +
            " = " + RHS.toString() + ";";
  }
}

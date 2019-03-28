package structural_representation.atoms.statements;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

public class InitialisationAtom extends DeclarationAtom {
  private final ExpressionAtom RHS;

  public InitialisationAtom(BonesType type, IdentifierAtom ident,
                            ExpressionAtom RHS, Position position) {
    super(type, ident, position);
    this.RHS = RHS;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    table.put(ident.toString(),
            new Variable(type, RHS.evaluate(table, errorListener)));

    return StatementControl.cont();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    super.semanticErrorCheck(symbolTable, errorListener);

    BonesType rhsType = RHS.getType(symbolTable);

    if (!type.equals(rhsType)) {
      errorListener.semanticError(ErrorMessages.
              expectedTypeButExpressionIs("Initialisation",
                      type, rhsType),
              getPosition().getLine(), getPosition().getPositionInLine());
    }
  }

  @Override
  public String toString() {
    return type.toString() + " " + ident.toString() +
            " = " + RHS.toString() + ";";
  }
}

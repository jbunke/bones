package structural_representation.atoms.expressions.literals;

import error.BonesErrorListener;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.CharType;
import structural_representation.symbol_table.SymbolTable;

public class CharacterLiteralAtom extends ExpressionAtom {
  private final char value;

  public CharacterLiteralAtom(char value) {
    this.value = value;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return new CharType();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {

  }
}

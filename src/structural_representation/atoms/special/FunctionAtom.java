package structural_representation.atoms.special;

import error.BonesErrorListener;
import structural_representation.atoms.Atom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class FunctionAtom extends Atom implements Symbol {
  private final BonesType returnType;
  private final String name;
  private final ParamListAtom paramList;
  private final List<StatementAtom> statements;

  public FunctionAtom(BonesType returnType, String name,
                      ParamListAtom paramList,
                      List<StatementAtom> statements) {
    this.returnType = returnType;
    this.name = name;
    this.paramList = paramList;
    this.statements = statements;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    // TODO
  }

  @Override
  public BonesType getType() {
    return returnType;
  }
}

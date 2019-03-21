package structural_representation.atoms.special;

import error.BonesErrorListener;
import structural_representation.atoms.Atom;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class ParamListAtom extends Atom {
  private final List<ParamAtom> params;

  public ParamListAtom(List<ParamAtom> params) {
    this.params = params;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    params.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));
  }

  public List<ParamAtom> getParams() {
    return params;
  }
}

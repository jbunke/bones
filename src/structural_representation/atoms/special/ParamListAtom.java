package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.Position;
import structural_representation.atoms.Atom;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class ParamListAtom extends Atom {
  private final List<ParamAtom> params;

  public ParamListAtom(List<ParamAtom> params, Position position) {
    this.params = params;
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    params.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));
  }

  public List<ParamAtom> getParams() {
    return params;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < params.size(); i++) {
      if (i > 0) sb.append(", ");

      sb.append(params.get(i));
    }

    return sb.toString();
  }
}

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

  private boolean beenChecked;

  public FunctionAtom(BonesType returnType, String name,
                      ParamListAtom paramList,
                      List<StatementAtom> statements) {
    this.returnType = returnType;
    this.name = name;
    this.paramList = paramList;
    this.statements = statements;
  }

  public boolean hasBeenChecked() {
    return beenChecked;
  }

  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    // TODO

    return null;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    symbolTable.put(name, this);

    SymbolTable functionTable = new SymbolTable(this, symbolTable);

    statements.forEach(x -> x.returnTypeSet(returnType));
    if (paramList != null)
      paramList.semanticErrorCheck(functionTable, errorListener);
    statements.forEach(x ->
            x.semanticErrorCheck(functionTable, errorListener));

    beenChecked = true;
  }

  @Override
  public BonesType getType() {
    return returnType;
  }

  public ParamListAtom getParamList() {
    return paramList;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(returnType);
    sb.append(" ");
    sb.append(name);
    sb.append("(");
    sb.append(paramList.toString());
    sb.append(") {\n");

    statements.forEach(x -> {
      sb.append("\t");
      sb.append(x.toString());
      sb.append("\n");
    });

    sb.append("}");

    return sb.toString();
  }
}

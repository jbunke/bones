package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.Position;
import execution.StatementControl;
import formatting.Tabs;
import structural_representation.atoms.Atom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.primitives.StringType;
import structural_representation.atoms.types.primitives.VoidType;
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
                      List<StatementAtom> statements, Position position) {
    this.returnType = returnType;
    this.name = name;
    this.paramList = paramList;
    this.statements = statements;
    this.position = position;
  }

  public boolean hasBeenChecked() {
    return beenChecked;
  }

  public boolean isMain() {
    return name.equals("main") && returnType instanceof VoidType &&
            paramList != null && paramList.getParams().size() == 1 &&
            paramList.getParams().get(0).getType().equals(
                    new ArrayType(new StringType()));
  }

  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {

    StatementControl status = StatementControl.cont();

    for (StatementAtom statement : statements) {
      status = statement.execute(table, errorListener);
      if (!status.shouldContinue()) break;
    }

    return status.getValue();
  }

  String getName() {
    return name;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    if (!(returnType.equals(new VoidType())))
      returnType.semanticErrorCheck(symbolTable, errorListener);

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
    if (paramList != null) sb.append(paramList.toString());
    sb.append(") {\n");

    statements.forEach(x -> sb.append(Tabs.tabLines(x.toString())));

    sb.append("}");

    return sb.toString();
  }
}

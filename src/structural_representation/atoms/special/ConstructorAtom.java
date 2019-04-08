package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.Position;
import execution.StatementControl;
import structural_representation.atoms.Atom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.ClassType;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class ConstructorAtom extends Atom implements Symbol {
  private boolean populatedReturnType;
  private ClassType returnType;
  private final ParamListAtom paramList;
  private final List<StatementAtom> statements;

  public ConstructorAtom(ParamListAtom paramList,
                         List<StatementAtom> statements, Position position) {
    this.paramList = paramList;
    this.statements = statements;
    this.position = position;

    populatedReturnType = false;
    this.returnType = null;
  }

  void populateReturnType(ClassType returnType) {
    if (!populatedReturnType) {
      this.returnType = returnType;
      populatedReturnType = true;
    }
  }

  String generateSymbolTableID() {
    StringBuilder sb = new StringBuilder("constructor (");
    sb.append(returnType.toString());
    sb.append(") :: (");

    if (paramList != null) {
      List<BonesType> paramTypes = new ArrayList<>();
      paramList.getParams().forEach(x -> paramTypes.add(x.getType()));

      for (int i = 0; i < paramTypes.size(); i++) {
        if (i > 0) sb.append(", ");
        sb.append(paramTypes.get(i).toString());
      }
    }
    sb.append(")");

    return sb.toString();
  }

  public ParamListAtom getParamList() {
    return paramList;
  }

  public Object evaluate(SymbolTable table,
                         BonesErrorListener errorListener) {
    /* Re-call field declarations */
    ClassAtom theClass = returnType.getClassAtom();
    theClass.getFields().forEach(x ->
            x.execute(returnType.getClassTable(),
                    errorListener));

    StatementControl status;

    for (StatementAtom statement : statements) {
      status = statement.execute(table, errorListener);
      if (!status.shouldContinue()) break;
    }

    return returnType.generateInstance();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    returnType.semanticErrorCheck(symbolTable, errorListener);

    String symbolTableID = generateSymbolTableID();
    symbolTable.put(symbolTableID, this);

    SymbolTable constructorTable = new SymbolTable(this, symbolTable);

    statements.forEach(x -> x.returnTypeSet(returnType));
    if (paramList != null)
      paramList.semanticErrorCheck(constructorTable, errorListener);
    statements.forEach(x ->
            x.semanticErrorCheck(constructorTable, errorListener));
  }

  @Override
  public BonesType getType() {
    if (!populatedReturnType) return null;
    return returnType;
  }
}

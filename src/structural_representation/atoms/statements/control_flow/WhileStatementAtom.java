package structural_representation.atoms.statements.control_flow;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.BoolType;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class WhileStatementAtom extends StatementAtom {
  private final ExpressionAtom loopCondition;

  private final List<StatementAtom> body;

  public WhileStatementAtom(ExpressionAtom loopCondition,
                            List<StatementAtom> body) {
    this.loopCondition = loopCondition;
    this.body = body;
  }

  @Override
  public void returnTypeSet(BonesType returnType) {
    body.forEach(x -> x.returnTypeSet(returnType));
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    SymbolTable localTable = new SymbolTable(this, symbolTable);
    if (!loopCondition.getType(localTable).equals(new BoolType())) {
      errorListener.semanticError(ErrorMessages.conditionIsNotBoolean());
    }

    body.forEach(x -> x.semanticErrorCheck(localTable, errorListener));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("while (");
    sb.append(loopCondition.toString());
    sb.append(") {\n");

    body.forEach(x -> {
      sb.append("\t");
      sb.append(x.toString());
      sb.append("\n");
    });
    sb.append("}");

    return sb.toString();
  }
}

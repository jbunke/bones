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
    for (StatementAtom statement : body) {
      statement.returnTypeSet(returnType);
    }
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    SymbolTable localTable = new SymbolTable(this, symbolTable);
    if (!loopCondition.getType(localTable).equals(new BoolType())) {
      errorListener.semanticError(ErrorMessages.conditionIsNotBoolean());
    }

    for (StatementAtom statement : body) {
      statement.semanticErrorCheck(localTable, errorListener);
    }
  }
}

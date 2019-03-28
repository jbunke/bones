package structural_representation.atoms.statements.control_flow;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.BonesArray;
import execution.BonesList;
import execution.StatementControl;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.CharType;
import structural_representation.atoms.types.primitives.StringType;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

import java.util.List;

public class ForEachStatementAtom extends StatementAtom {
  private final IdentifierAtom token;
  private final ExpressionAtom collection;

  private final List<StatementAtom> body;

  public ForEachStatementAtom(IdentifierAtom token,
                              ExpressionAtom collection,
                              List<StatementAtom> body, Position position) {
    this.token = token;
    this.collection = collection;
    this.body = body;
    this.position = position;
  }

  @Override
  public StatementControl execute(SymbolTable table,
                                  BonesErrorListener errorListener) {
    SymbolTable localTable = table.findChild(this);

    StatementControl status = StatementControl.cont();

    Object collection = this.collection.evaluate(table, errorListener);

    if (collection instanceof BonesList) {
      BonesList list = (BonesList) collection;

      for (int i = 0; i < list.size(); i++) {
        localTable.update(token.toString(), list.at(i));

        for (StatementAtom statement : body) {
          if (!status.shouldContinue()) return status;
          status = statement.execute(localTable, errorListener);
        }
      }
    } else if (collection instanceof BonesArray) {
      BonesArray array = (BonesArray) collection;

      for (int i = 0; i < array.size(); i++) {
        localTable.update(token.toString(), array.at(i));

        for (StatementAtom statement : body) {
          if (!status.shouldContinue()) return status;
          status = statement.execute(localTable, errorListener);
        }
      }
    } else if (collection instanceof String) {
      String string = (String) collection;

      for (int i = 0; i < string.length(); i++) {
        localTable.update(token.toString(), string.charAt(i));

        for (StatementAtom statement : body) {
          if (!status.shouldContinue()) return status;
          status = statement.execute(localTable, errorListener);
        }
      }
    }

    return status;
  }

  @Override
  public void returnTypeSet(BonesType returnType) {
    body.forEach(x -> x.returnTypeSet(returnType));
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    SymbolTable localTable = new SymbolTable(this, symbolTable);
    if (!(collection.getType(localTable) instanceof ArrayType) &&
            !(collection.getType(localTable) instanceof ListType) &&
            !(collection.getType(localTable) instanceof StringType)) {
      errorListener.semanticError(
              ErrorMessages.foreachNotUsedWithCollection(),
              getPosition().getLine(), getPosition().getPositionInLine());
    }

    if (localTable.tableContainsKeyInScope(token.toString())) {
      errorListener.semanticError(
              ErrorMessages.alreadyDeclaredInScope(token.toString()),
              getPosition().getLine(), getPosition().getPositionInLine());
    } else if (collection.getType(localTable) instanceof ArrayType) {
      ArrayType arrayType = (ArrayType) collection.getType(localTable);

      localTable.put(token.toString(),
              new Variable(arrayType.getElementType()));
    } else if (collection.getType(localTable) instanceof ListType) {
      ListType listType = (ListType) collection.getType(localTable);

      localTable.put(token.toString(),
              new Variable(listType.getElementType()));
    } else if (collection.getType(localTable) instanceof StringType) {
      localTable.put(token.toString(), new Variable(new CharType()));
    }

    // TODO: Ensure collection is not modified inside code block

    body.forEach(x -> x.semanticErrorCheck(localTable, errorListener));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("foreach (");
    sb.append(token.toString());
    sb.append(" : ");
    sb.append(collection.toString());
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

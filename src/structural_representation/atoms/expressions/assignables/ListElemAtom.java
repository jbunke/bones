package structural_representation.atoms.expressions.assignables;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.BonesArray;
import execution.BonesList;
import structural_representation.Compile;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

import java.util.List;

public class ListElemAtom extends AssignableAtom {
  private final String identifier;
  private final List<ExpressionAtom> indices;

  public ListElemAtom(String identifier, List<ExpressionAtom> indices,
                      Position position) {
    this.identifier = identifier;
    this.indices = indices;
    this.position = position;
  }

  public String getIdentifier() {
    return identifier;
  }

  public List<ExpressionAtom> getIndices() {
    return indices;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return super.getCollectionType(table, identifier, indices);
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    Variable variable = (Variable) table.get(identifier);
    Object value =  variable.getValue();

    if (value == null) errorListener.runtimeError(ErrorMessages.nullPointer(),
            true, Compile.RUNTIME_ERROR_EXIT,
            getPosition().getLine(), getPosition().getPositionInLine());

    for (ExpressionAtom index : indices) {

      int i = (Integer) index.evaluate(table, errorListener);

      if (value instanceof BonesList) {
        value = ((BonesList) value).at(i);
      } else if (value instanceof BonesArray) {
        value = ((BonesArray) value).at(i);
      }
    }

    return value;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    Symbol symbol = symbolTable.get(identifier);
    Variable variable = (Variable) symbol;

    if (!(variable.getType() instanceof ListType)) {
      errorListener.semanticError(ErrorMessages.
              variableIsNotListInThisContext(identifier),
              getPosition().getLine(), getPosition().getPositionInLine());
    }

    for (ExpressionAtom index : indices) {
      if (!index.getType(symbolTable).equals(new IntType())) {
        errorListener.semanticError(ErrorMessages.collectionIndexType(),
                index.getPosition().getLine(),
                index.getPosition().getPositionInLine());
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(identifier);
    indices.forEach(x -> {
      sb.append("(");
      sb.append(x);
      sb.append(")");
    });

    return sb.toString();
  }
}

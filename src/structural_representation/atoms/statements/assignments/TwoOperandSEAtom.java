package structural_representation.atoms.statements.assignments;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.BonesList;
import execution.StatementControl;
import structural_representation.Compile;
import structural_representation.atoms.expressions.ExpressionAtom;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.special.rhs.RHSAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.IntType;
import structural_representation.symbol_table.SymbolTable;

public class TwoOperandSEAtom extends AssignmentAtom {
  private final Operator operator;
  private final RHSAtom rhs;
  private final ExpressionAtom index;

  public TwoOperandSEAtom(AssignableAtom assignable,
                          Operator operator, RHSAtom rhs,
                          ExpressionAtom index, Position position) {
    this.assignable = assignable;
    this.operator = operator;
    this.rhs = rhs;
    this.index = index;
    this.position = position;
  }

  public enum Operator {
    ADD_AT_INDEX
  }

  private String operatorToString() {
    switch (operator) {
      case ADD_AT_INDEX:
        return "+@";
      default:
        return "";
    }
  }

  @Override
  public StatementControl execute(SymbolTable table, BonesErrorListener errorListener) {
    Integer index = (Integer) this.index.evaluate(table, errorListener);
    Object modifier = rhs.evaluate(table, errorListener);
    Object value = assignable.getInitialCollectionValue(table, errorListener);

    switch (operator) {
      case ADD_AT_INDEX:
        BonesList list = (BonesList) value;

        if (index < 0 || index > list.size())
          errorListener.runtimeError(
                  ErrorMessages.collectionIndexOutOfBounds("List"),
                  true, Compile.RUNTIME_ERROR_EXIT,
                  this.index.getPosition().getLine(),
                  this.index.getPosition().getPositionInLine());


        if (index < list.size()) list.add(index, modifier);
        else list.add(modifier);
        break;
    }

    assignable.assignmentSymbolTableUpdate(table, value, errorListener);

    return StatementControl.cont();
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    rhs.semanticErrorCheck(symbolTable, errorListener);
    index.semanticErrorCheck(symbolTable, errorListener);

    if (errorListener.hasError()) return;

    BonesType indexType = index.getType(symbolTable);

    if (!indexType.equals(new IntType())) {
      errorListener.semanticError(ErrorMessages.collectionIndexType(),
              index.getPosition().getLine(),
              index.getPosition().getPositionInLine());
      return;
    }

    BonesType assignableType = assignable.getType(symbolTable);

    if (!(assignableType instanceof ListType)) {
      errorListener.semanticError(
              ErrorMessages.compoundAssignmentListOpOnNonList(),
              assignable.getPosition().getLine(),
              assignable.getPosition().getPositionInLine());
      return;
    }

    BonesType rhsType = rhs.getType(symbolTable);

    ListType assignableList = (ListType) assignableType;

    if (!assignableList.getElementType().equals(rhsType)) {
      errorListener.semanticError("",
              rhsType.getPosition().getLine(),
              rhsType.getPosition().getPositionInLine());
    }
  }

  @Override
  public String toString() {
    return assignable.toString() + " " + operatorToString() +
            " " + rhs.toString() + ", " + index.toString() + ";";
  }
}

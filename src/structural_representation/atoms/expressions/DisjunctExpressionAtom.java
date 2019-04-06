package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.BonesArray;
import execution.BonesList;
import structural_representation.Compile;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.DeterminedAtRuntimeType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.*;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class DisjunctExpressionAtom extends ExpressionAtom {
  private final ExpressionAtom LHS;
  private final List<ExpressionAtom> alternatives;
  private final Operator operator;
  private final String opString;

  public DisjunctExpressionAtom(ExpressionAtom lhs,
                                List<ExpressionAtom> alternatives,
                                String opString, Position position) {
    LHS = lhs;
    this.alternatives = alternatives;
    this.operator = operatorFromString(opString);
    this.opString = opString;
    this.position = position;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    if (operator == null)
      errorListener.semanticError(
              ErrorMessages.invalidOperatorForDisjunctExpression(),
              getPosition().getLine(), getPosition().getPositionInLine());

    for (ExpressionAtom alternative : alternatives) {
      BinaryOperationAtom altCase = new BinaryOperationAtom(LHS, alternative,
              opString, LHS.getPosition());

      altCase.semanticErrorCheck(symbolTable, errorListener);

      if (errorListener.hasError()) return;
    }
  }

  private enum Operator {
    GEQ, LEQ, GT, LT,
    EQUAL, NOT_EQUAL,
    AND,
    OR
  }

  private Operator operatorFromString(String opString) {
    switch (opString) {
      case ">=":
        return Operator.GEQ;
      case "<=":
        return Operator.LEQ;
      case ">":
        return Operator.GT;
      case "<":
        return Operator.LT;
      case "==":
        return Operator.EQUAL;
      case "!=":
        return Operator.NOT_EQUAL;
      case "&&":
        return Operator.AND;
      case "||":
        return Operator.OR;
      default:
        return null;
    }
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return new BoolType();
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    Boolean matched = false;

    for (ExpressionAtom alternative : alternatives) {
      BinaryOperationAtom altCase = new BinaryOperationAtom(LHS, alternative,
              opString, LHS.getPosition());

      Boolean result = (boolean) altCase.evaluate(table, errorListener);

      matched |= result;
      if (matched) break;
    }

    return matched;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(
            LHS.toString() + " " + opString + " disj { ");

    for (int i = 0; i < alternatives.size(); i++) {
      if (i > 0) sb.append(" | ");
      sb.append(alternatives.get(i).toString());
    }

    sb.append(" }");

    return sb.toString();
  }
}

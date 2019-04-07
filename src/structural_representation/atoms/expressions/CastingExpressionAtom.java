package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import structural_representation.Compile;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.primitives.*;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class CastingExpressionAtom extends ExpressionAtom {
  private final BonesType castType;
  private final ExpressionAtom expression;

  public CastingExpressionAtom(BonesType castType,
                               ExpressionAtom expression,
                               Position position) {
    this.castType = castType;
    this.expression = expression;
    this.position = position;
  }

  private void generateCastError(BonesErrorListener errorListener) {
    errorListener.runtimeError(
            ErrorMessages.castError(castType.toString()),
            true, Compile.RUNTIME_ERROR_EXIT, position.getLine(),
            position.getPositionInLine());
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return castType;
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    Object evalExpression = expression.evaluate(table, errorListener);

    if (castType.equals(new BoolType())) {
      if (evalExpression instanceof Integer) {
        Integer integer = (Integer) evalExpression;

        if (integer == 0) return false;
        else if (integer == 1) return true;
        else generateCastError(errorListener);
      } else if (evalExpression instanceof String) {
        String string = (String) evalExpression;

        if (string.equals("true") || string.equals("false"))
          return Boolean.parseBoolean(string);
        else generateCastError(errorListener);
      } else if (evalExpression instanceof Boolean) return evalExpression;
    } else if (castType.equals(new IntType())) {
      if (evalExpression instanceof Float) {
        Float flt = (Float) evalExpression;
        return flt.intValue();
      } else if (evalExpression instanceof String) {
        // TODO - runtime error for NumberFormatException
        return Integer.parseInt((String) evalExpression);
      } else if (evalExpression instanceof Character) {
        Character character = (Character) evalExpression;
        return (int) character;
      } else if (evalExpression instanceof Boolean) {
        Boolean bool = (Boolean) evalExpression;
        return bool ? 1 : 0;
      } else if (evalExpression instanceof Integer) return evalExpression;
    } else if (castType.equals(new StringType())) {
      if (evalExpression instanceof Integer ||
              evalExpression instanceof Float ||
              evalExpression instanceof Boolean ||
              evalExpression instanceof Character ||
              evalExpression instanceof String) {
        return evalExpression.toString();
      }
    } else if (castType.equals(new CharType())) {
      if (evalExpression instanceof Integer) {
        int integer = (Integer) evalExpression;
        return (char) integer;
      }
    } else if (castType.equals(new FloatType())) {
      if (evalExpression instanceof Integer) {
        Integer integer = (Integer) evalExpression;
        return integer.floatValue();
      }
    }

    return null;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    castType.semanticErrorCheck(symbolTable, errorListener);
    expression.semanticErrorCheck(symbolTable, errorListener);

    BonesType expressionType = expression.getType(symbolTable);

    List<BonesType> validExpTypes;

    if (castType.equals(new BoolType())) {
      validExpTypes =
              List.of(new BoolType(), new StringType(), new IntType());
    } else if (castType.equals(new IntType()) ||
            castType.equals(new StringType())) {
      validExpTypes =
              List.of(new BoolType(), new StringType(), new IntType(),
                      new FloatType(), new CharType());
    } else if (castType.equals(new FloatType())) {
      validExpTypes =
              List.of(new IntType(), new FloatType());
    } else if (castType.equals(new CharType())) {
      validExpTypes =
              List.of(new IntType(), new CharType());
    } else {
      errorListener.semanticError(
              ErrorMessages.castingTypeInvalid(castType.toString()),
              position.getLine(), position.getPositionInLine());
      return;
    }

    if (!validExpTypes.contains(expressionType))
      errorListener.semanticError(
              ErrorMessages.cannotCastTypeToType(
                      castType.toString(), expressionType.toString()),
              position.getLine(), position.getPositionInLine());
  }
}

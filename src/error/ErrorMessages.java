package error;

import structural_representation.atoms.types.BonesType;

public class ErrorMessages {

  /* SEMANTIC */

  public static String expectedTypeButExpressionIs(String statementType,
                                                   BonesType expected,
                                                   BonesType got) {
    return statementType + " expected type " + expected +
            " but expression resolves to type " + got;
  }

  public static String voidReturnUsedInNonVoid() {
    return "Void return used in function with non-void return type";
  }

  public static String alreadyDeclaredInScope(String var) {
    return "Variable \"" + var + "\" already declared in scope";
  }

  public static String conditionIsNotBoolean() {
    return "Condition is not boolean (evaluates to true or false)";
  }

  public static String foreachNotUsedWithCollection() {
    return "For each code block used without a collection";
  }

  public static String sideEffectOperatorAssignable(String operator,
                                                    BonesType assignableType) {
    return "Assignment operator " + operator +
            " not compatible with assignable of type " + assignableType;
  }

  public static String sideEffectOperatorOperand(String operator,
                                                    BonesType operandType) {
    return "Assignment operator " + operator +
            " not compatible with operand of type " + operandType;
  }

  public static String attemptedToUseVoidType() {
    return "Attempted to use void type other than as return type placeholder";
  }

  public static String calledSizeOnNonCollection() {
    return "Attempted to call size (#) operator on non-collection expression";
  }

  public static String calledMinusOnNonNumeric() {
    return "Attempted to call minus operator on non-numeric expression";
  }

  public static String calledAtIndexOnNonCollection() {
    return "Attempted to call at index (@) operator on non-collection expression";
  }

  public static String variableIsNotListInThisContext(String var) {
    return "Variable \"" + var + "\" is not a list in this context";
  }

  public static String variableIsNotArrayInThisContext(String var) {
    return "Variable \"" + var + "\" is not an array in this context";
  }

  public static String variableHasNotBeenDeclared(String var) {
    return "Variable \"" + var + "\" has not been declared";
  }

  public static String identifierIsNotAVariable(String var) {
    return "Identifier \"" + var + "\" is not a variable";
  }

  public static String identifierIsNotAFunction(String func) {
    return "Identifier \"" + func + "\" is not a function in this context";
  }

  public static String parameterArgumentAmount() {
    return "Number of function call arguments does not match " +
            "function parameters for this function";
  }

  public static String typesOfCollectionLiteralElementsDontMatch(
          String collectionType) {
    return "The types of " + collectionType + " literal elements don't match";
  }

  /* RUNTIME */

  public static String divideByZero() {
    return "Attempted to divide by zero";
  }

  public static String nullPointer() {
    return "Null pointer: Value is queried before it is assigned a value";
  }
}

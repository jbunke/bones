package error;

import structural_representation.atoms.types.BonesType;

public class ErrorMessages {
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
}

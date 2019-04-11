package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import execution.Instance;
import structural_representation.atoms.special.FunctionAtom;
import structural_representation.atoms.special.ParamAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.ClassType;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallAtom extends ExpressionAtom {
  /* could also be the object variable identifier */
  private final String className;
  private final String functionName;
  private final List<ExpressionAtom> arguments;
  private final Scope scope;
  private FunctionAtom function = null;

  public FunctionCallAtom(String functionName,
                          List<ExpressionAtom> arguments, Position position) {
    this.className = null;
    this.functionName = functionName;
    this.arguments = arguments;
    this.position = position;
    this.scope = Scope.INTERNAL;
  }

  public FunctionCallAtom(String className, String functionName,
                          List<ExpressionAtom> arguments, Position position) {
    this.className = className;
    this.functionName = functionName;
    this.arguments = arguments;
    this.position = position;
    this.scope = Scope.EXTERNAL;
  }

  public enum Scope {
    INTERNAL, EXTERNAL
  }

  @Override
  public BonesType getType(SymbolTable table) {
    if (function == null) function = (FunctionAtom) table.root().get(functionName);

    return function.getType();
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    SymbolTable functionTable;

    if (scope == Scope.EXTERNAL) {
      Symbol symbol = table.get(className);

      if (symbol instanceof Variable) {
        Instance instance = (Instance) ((Variable) symbol).getValue();
        functionTable = instance.instanceTable;
      } else {
        ClassType classType = (ClassType) symbol;
        functionTable = classType.getClassTable().tableForFunction(function);
      }

    } else functionTable = table.tableForFunction(function);

    List<String> params = new ArrayList<>();
    if (function.getParamList() != null)
      function.getParamList().getParams().forEach(x ->
              params.add("param!" + x.getIdent().toString()));
    List<Object> argValues = new ArrayList<>();
    arguments.forEach(x -> argValues.add(x.evaluate(table, errorListener)));

    if (params.size() != argValues.size()) return null;

    for (int i = 0; i < params.size(); i++) {
      functionTable.update(params.get(i), argValues.get(i));
    }

    return function.evaluate(functionTable, errorListener);
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    arguments.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));

    SymbolTable rootTable;

    if (scope == Scope.EXTERNAL) {
      Symbol symbol = symbolTable.get(className);

      if (symbol instanceof Variable) {
        Instance instance = (Instance) ((Variable) symbol).getValue();
        rootTable = instance.instanceTable;
      } else {
        ClassType classType = (ClassType) symbol;
        rootTable = classType.getClassTable().tableForFunction(function);
      }

    } else rootTable = symbolTable.root();

    if (rootTable.get(functionName) == null) {
      errorListener.semanticError(ErrorMessages.
              identifierIsNotAFunction(functionName),
              getPosition().getLine(), getPosition().getPositionInLine());
      return;
    } else if (!(rootTable.get(functionName) instanceof FunctionAtom)) {
      errorListener.semanticError(ErrorMessages.
              identifierIsNotAFunction(functionName),
              getPosition().getLine(), getPosition().getPositionInLine());
      return;
    } else if (function == null) {
      function = (FunctionAtom) rootTable.get(functionName);
    }

    /* Semantic check the function if it hasn't been done */
    if (!function.hasBeenChecked()) {
      SymbolTable functionTable = rootTable.tableForFunction(function);

      if (functionTable != null)
        function.semanticErrorCheck(functionTable, errorListener);
    }

    if (arguments.isEmpty() && (function.getParamList() == null ||
            function.getParamList().getParams().isEmpty())) return;

    if (function.getParamList().getParams().size() != arguments.size()) {
      errorListener.semanticError(ErrorMessages.parameterArgumentAmount(),
              getPosition().getLine(), getPosition().getPositionInLine());
    } else {
      List<ParamAtom> params = function.getParamList().getParams();
      for (int i = 0; i < params.size(); i++) {
        if (!arguments.get(i).getType(symbolTable).equals(
                params.get(i).getType())) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Parameter",
                          params.get(i).getType(),
                          arguments.get(i).getType(symbolTable)),
                  getPosition().getLine(), getPosition().getPositionInLine());
        }
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("call ");

    if (className != null)
      sb.append(className).append(".");

    sb.append(functionName);
    sb.append("(");

    for (int i = 0; i < arguments.size(); i++) {
      if (i > 0) sb.append(", ");

      sb.append(arguments.get(i));
    }

    sb.append(")");

    return sb.toString();
  }
}

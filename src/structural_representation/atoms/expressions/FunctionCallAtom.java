package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import structural_representation.atoms.special.FunctionAtom;
import structural_representation.atoms.special.ParamAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallAtom extends ExpressionAtom {
  private final String name;
  private final List<ExpressionAtom> arguments;
  private FunctionAtom function = null;

  public FunctionCallAtom(String name, List<ExpressionAtom> arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    if (function == null) function = (FunctionAtom) table.get(name);

    return function.getType();
  }

  @Override
  public Object evaluate(SymbolTable table, BonesErrorListener errorListener) {
    SymbolTable functionTable = table.tableForFunction(function);
    List<String> params = new ArrayList<>();
    function.getParamList().getParams().forEach(x ->
            params.add("param_" + x.getIdent().toString()));
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
    /* Semantic check the function if it hasn't been done */
    if (!function.hasBeenChecked()) {
      SymbolTable functionTable = symbolTable.tableForFunction(function);

      if (functionTable != null)
        function.semanticErrorCheck(functionTable, errorListener);
    }

    if (symbolTable.get(name) == null) {
      errorListener.semanticError(ErrorMessages.
              identifierIsNotAFunction(name));
      return;
    } else if (!(symbolTable.get(name) instanceof FunctionAtom)) {
      errorListener.semanticError(ErrorMessages.
              identifierIsNotAFunction(name));
      return;
    } else if (function == null) {
      function = (FunctionAtom) symbolTable.get(name);
    }

    if (function.getParamList().getParams().size() != arguments.size()) {
      errorListener.semanticError(ErrorMessages.parameterArgumentAmount());
    } else {
      List<ParamAtom> params = function.getParamList().getParams();
      for (int i = 0; i < params.size(); i++) {
        if (!params.get(i).getType().equals(
                arguments.get(i).getType(symbolTable))) {
          errorListener.semanticError(ErrorMessages.
                  expectedTypeButExpressionIs("Parameter",
                          params.get(i).getType(),
                          arguments.get(i).getType(symbolTable)));
        }
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("call ");
    sb.append(name);
    sb.append("(");

    for (int i = 0; i < arguments.size(); i++) {
      if (i > 0) sb.append(", ");

      sb.append(arguments.get(i));
    }

    sb.append(")");

    return sb.toString();
  }
}

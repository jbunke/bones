package structural_representation.atoms.expressions;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import structural_representation.atoms.special.ConstructorAtom;
import structural_representation.atoms.special.ParamAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.ClassType;
import structural_representation.symbol_table.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class ConstructorCallAtom extends ExpressionAtom {
  private final String className;
  private final List<ExpressionAtom> arguments;
  // private final FunctionCallAtom.Scope scope;
  private ConstructorAtom constructor = null;

  public ConstructorCallAtom(String className,
                             List<ExpressionAtom> arguments,
                             Position position) {
    this.className = className;
    this.arguments = arguments;
    this.position = position;
  }

  private String generateSymbolTableID(SymbolTable table) {
    StringBuilder sb = new StringBuilder("constructor (");
    sb.append(className);
    sb.append(") :: (");

    for (int i = 0; i < arguments.size(); i++) {
      if (i > 0) sb.append(", ");
      sb.append(arguments.get(i).getType(table).toString());
    }
    sb.append(")");

    return sb.toString();
  }

  @Override
  public BonesType getType(SymbolTable table) {
    return constructor.getType();
  }

  @Override
  public Object evaluate(SymbolTable table,
                         BonesErrorListener errorListener) {
    ClassType foreignClass = (ClassType) table.root().get(className);
    SymbolTable foreignClassTable = foreignClass.getClassTable();
    SymbolTable constructorTable =
            foreignClassTable.tableForFunction(constructor);

    List<String> params = new ArrayList<>();
    if (constructor.getParamList() != null)
      constructor.getParamList().getParams().forEach(x ->
              params.add("param!" + x.getIdent().toString()));
    List<Object> argValues = new ArrayList<>();
    arguments.forEach(x -> argValues.add(x.evaluate(table, errorListener)));

    if (params.size() != argValues.size()) return null;

    for (int i = 0; i < params.size(); i++) {
      constructorTable.update(params.get(i), argValues.get(i));
    }

    return constructor.evaluate(constructorTable, errorListener);
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    arguments.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));

    ClassType foreignClass = (ClassType) symbolTable.root().get(className);
    SymbolTable foreignClassTable = foreignClass.getClassTable();

    String IDtoMatch = generateSymbolTableID(symbolTable);

    if (foreignClassTable.get(IDtoMatch) == null) {
      errorListener.semanticError(ErrorMessages.
                      noConstructorInClassMatches(className),
              getPosition().getLine(), getPosition().getPositionInLine());
      return;
    } else if (!(foreignClassTable.get(IDtoMatch) instanceof ConstructorAtom)) {
      errorListener.semanticError(ErrorMessages.
                      noConstructorInClassMatches(className),
              getPosition().getLine(), getPosition().getPositionInLine());
      return;
    } else if (constructor == null) {
      constructor = (ConstructorAtom) foreignClassTable.get(IDtoMatch);
    }

    if (arguments.isEmpty() && (constructor.getParamList() == null ||
            constructor.getParamList().getParams().isEmpty())) return;

    if (constructor.getParamList().getParams().size() != arguments.size()) {
      errorListener.semanticError(ErrorMessages.parameterArgumentAmount(),
              getPosition().getLine(), getPosition().getPositionInLine());
    } else {
      List<ParamAtom> params = constructor.getParamList().getParams();
      for (int i = 0; i < params.size(); i++) {
        if (!arguments.get(i).getType(symbolTable).equals(
                params.get(i).getType())) {
          errorListener.semanticError(ErrorMessages.
                          expectedTypeButExpressionIs(
                                  "Constructor parameter",
                                  params.get(i).getType(),
                                  arguments.get(i).getType(symbolTable)),
                  getPosition().getLine(), getPosition().getPositionInLine());
        }
      }
    }
  }
}

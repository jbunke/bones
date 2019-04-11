package structural_representation.atoms.expressions.assignables;

import error.BonesErrorListener;
import error.Position;
import execution.Instance;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.ClassType;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;
import structural_representation.symbol_table.Variable;

import java.util.List;

public class ExternalAssignableAtom extends AssignableAtom {
  private final List<IdentifierAtom> idents;
  private BonesType type;

  public ExternalAssignableAtom(List<IdentifierAtom> idents,
                                Position position) {
    this.idents = idents;
    this.position = position;
    type = null;
  }

  @Override
  public BonesType getType(SymbolTable table) {
    if (type == null) semanticErrorCheck(table, new BonesErrorListener());
    return type;
  }

  @Override
  public Object evaluate(SymbolTable table,
                         BonesErrorListener errorListener) {
    SymbolTable jurisdiction = table;
    Object res = null;

    for (int i = 0; i < idents.size(); i++) {
      Symbol symbol = jurisdiction.get(idents.get(i).toString());

      if (i == idents.size() - 1) {
        res =  idents.get(i).evaluate(jurisdiction, errorListener);
        break;
      }

      if (symbol instanceof ClassType) {
        jurisdiction = ((ClassType) symbol).getClassTable();
      } else if (symbol instanceof Variable) {
        if (((Variable) symbol).getValue() instanceof Instance) {
          jurisdiction =
                  ((Instance) ((Variable) symbol).getValue()).instanceTable;
        }
      }
    }

    return res;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    SymbolTable jurisdiction = symbolTable;
    for (int i = 0; i < idents.size(); i++) {
      idents.get(i).semanticErrorCheck(jurisdiction, errorListener);

      Symbol symbol = jurisdiction.get(idents.get(i).toString());

      if (i == idents.size() - 1)
        type = idents.get(i).getType(jurisdiction);

      if (symbol instanceof ClassType) {
        jurisdiction = ((ClassType) symbol).getClassTable();
      } else if (symbol instanceof Variable) {
        if (((Variable) symbol).getValue() instanceof Instance) {
          jurisdiction =
                  ((Instance) ((Variable) symbol).getValue()).instanceTable;
        }
      }

      if (errorListener.hasError())
        break;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    idents.forEach(x -> sb.append(".").append(x.toString()));

    return sb.toString().substring(1);
  }
}

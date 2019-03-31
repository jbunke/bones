package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.Position;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.statements.DeclarationAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.symbol_table.Symbol;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class ClassAtom extends BonesType implements Symbol {
  private final PathAtom path;
  private final List<ImportAtom> imports;
  private final IdentifierAtom className;
  private final List<DeclarationAtom> fields;
  private final List<FunctionAtom> functions;

  public ClassAtom(PathAtom path, List<ImportAtom> imports,
                   IdentifierAtom className, List<DeclarationAtom> fields,
                   List<FunctionAtom> functions, Position position) {
    this.path = path;
    this.imports = imports;
    this.className = className;
    this.fields = fields;
    this.functions = functions;
    this.position = position;
  }

  public void execute(SymbolTable table, BonesErrorListener errorListener) {
    /* Execute field declarations / initialisations */
    fields.forEach(x -> x.execute(table, errorListener));

    /* Find "main" entry point and run it */
    FunctionAtom main = null;

    for (int i = 0; i < functions.size() && main == null; i++) {
      main = functions.get(i).isMain() ? functions.get(i) : null;
    }

    if (main != null) {
      main.evaluate(table.findChild(main), errorListener);
    }
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    path.semanticErrorCheck(symbolTable, errorListener);
    imports.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));

    fields.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));
    functions.forEach(x -> symbolTable.put(x.getName(), x));
    functions.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));
  }

  @Override
  public Object defaultValue() {
    return null;
  }

  @Override
  public BonesType getType() {
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(path);
    sb.append("\n\n");

    imports.forEach(x -> {
      sb.append(x.toString());
      sb.append("\n");
    });

    sb.append("\nclass ");
    sb.append(className.toString());
    sb.append(" {\n");

    fields.forEach(x -> {
      sb.append("\t");
      sb.append(x.toString());
      sb.append("\n");
    });

    sb.append("\n");

    functions.forEach(x -> {
      sb.append("\t");
      sb.append(x.toString());
      sb.append("\n");
    });

    sb.append("}");

    return sb.toString();
  }
}

package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.ErrorMessages;
import error.Position;
import formatting.Tabs;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.statements.DeclarationAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.ClassType;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class ClassAtom extends BonesType {
  private final PathAtom path;
  private final List<ImportAtom> imports;
  private final IdentifierAtom className;
  private final List<ConstructorAtom> constructors;
  private final List<DeclarationAtom> fields;
  private final List<FunctionAtom> functions;

  public ClassAtom(PathAtom path, List<ImportAtom> imports,
                   IdentifierAtom className, List<DeclarationAtom> fields,
                   List<ConstructorAtom> constructors,
                   List<FunctionAtom> functions, Position position) {
    this.path = path;
    this.imports = imports;
    this.className = className;
    this.constructors = constructors;
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

  public void processImports(String classFilepath, SymbolTable table) {
    String rootPath = path.cutoffPathFromFilepath(classFilepath);

    imports.forEach(x -> x.process(rootPath, table));
  }

  List<DeclarationAtom> getFields() {
    return fields;
  }

  public String getClassName() {
    return className.toString();
  }

  private void uniqueConstructors(BonesErrorListener errorListener) {
    for (ConstructorAtom c1 : constructors) {
      for (ConstructorAtom c2 : constructors) {
        if (!c1.equals(c2) &&
                c1.generateSymbolTableID().equals(c2.generateSymbolTableID())) {
          errorListener.semanticError(
                  ErrorMessages.multipleConstructorsSameSignature(),
                  c2.getPosition().getLine(),
                  c2.getPosition().getPositionInLine());
        }
      }
    }
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    /* populate this class as a type */
    symbolTable.put(className.toString(),
            new ClassType(this, symbolTable));

    path.semanticErrorCheck(symbolTable, errorListener);
    imports.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));

    fields.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));

    uniqueConstructors(errorListener);
    ClassType classType = (ClassType) symbolTable.get(className.toString());
    constructors.forEach(x -> x.populateReturnType(classType));
    constructors.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));

    functions.forEach(x -> symbolTable.put(x.getName(), x));
    functions.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));
  }

  @Override
  public Object defaultValue() {
    return null;
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

    fields.forEach(x -> sb.append(Tabs.tabLines(x.toString())));

    sb.append("\n");

    functions.forEach(x -> sb.append(Tabs.tabLines(x.toString())));

    sb.append("}");

    return sb.toString();
  }
}

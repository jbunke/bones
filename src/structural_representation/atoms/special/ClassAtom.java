package structural_representation.atoms.special;

import error.BonesErrorListener;
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
                   List<FunctionAtom> functions) {
    this.path = path;
    this.imports = imports;
    this.className = className;
    this.fields = fields;
    this.functions = functions;
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    path.semanticErrorCheck(symbolTable, errorListener);
    imports.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));

    fields.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));
    functions.forEach(x -> x.semanticErrorCheck(symbolTable, errorListener));
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

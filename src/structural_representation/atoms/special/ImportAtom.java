package structural_representation.atoms.special;

import error.BonesErrorListener;
import error.Position;
import structural_representation.Compile;
import structural_representation.Context;
import structural_representation.atoms.Atom;
import structural_representation.atoms.types.ClassType;
import structural_representation.symbol_table.SymbolTable;

import java.util.List;

public class ImportAtom extends Atom {
  private final List<String> path;

  public ImportAtom(List<String> path, Position position) {
    this.path = path;
    this.position = position;
  }

  public String getClassName() {
    return path.get(path.size() - 1);
  }

  @Override
  public void semanticErrorCheck(SymbolTable symbolTable,
                                 BonesErrorListener errorListener) {
    // TODO
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("import ");

    for (int i = 0; i < path.size(); i++) {
      if (i > 0) sb.append(".");

      sb.append(path.get(i));
    }
    sb.append(";");

    return sb.toString();
  }

  public void process(String rootPath, SymbolTable rootTable) {
    /* Generate filepath */
    StringBuilder filepath = new StringBuilder(rootPath);

    for (int i = 0; i < path.size(); i++) {
      filepath.append("/");
      filepath.append(path.get(i));
      if (i == path.size() - 1) filepath.append(".b");
    }

    Context context = Compile.createStructure(filepath.toString(),
            Compile.SourceType.FILE, Compile.InputType.CLASS, null);

    if (context.getErrorListener().hasError())
      Compile.printErrorsAndExit(context.getErrorListener(), true,
              Compile.SEMANTIC_ERROR_EXIT);

    ClassAtom importClass = (ClassAtom) context.getStructure();

    ClassType importType =
            new ClassType(importClass, context.getSymbolTable());

    rootTable.put(importClass.getClassName(), importType);
  }
}

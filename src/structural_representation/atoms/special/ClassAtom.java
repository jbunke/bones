package structural_representation.atoms.special;

import structural_representation.atoms.Atom;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.statements.DeclarationAtom;

import java.util.List;

public class ClassAtom extends Atom {
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
}

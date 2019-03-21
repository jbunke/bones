package structural_representation.atoms.special;

import structural_representation.atoms.Atom;
import structural_representation.atoms.statements.StatementAtom;
import structural_representation.atoms.types.BonesType;

import java.util.List;

public class FunctionAtom extends Atom {
  private final BonesType returnType;
  private final String name;
  private final ParamListAtom paramList;
  private final List<StatementAtom> statements;

  public FunctionAtom(BonesType returnType, String name,
                      ParamListAtom paramList,
                      List<StatementAtom> statements) {
    this.returnType = returnType;
    this.name = name;
    this.paramList = paramList;
    this.statements = statements;
  }
}

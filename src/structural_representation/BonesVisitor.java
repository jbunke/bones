package structural_representation;

import antlr.BonesParser;
import antlr.BonesParserBaseVisitor;
import error.Position;
import org.antlr.v4.runtime.Token;
import structural_representation.atoms.Atom;
import structural_representation.atoms.expressions.*;
import structural_representation.atoms.expressions.assignables.ArrayElemAtom;
import structural_representation.atoms.expressions.assignables.AssignableAtom;
import structural_representation.atoms.expressions.assignables.ListElemAtom;
import structural_representation.atoms.expressions.literals.*;
import structural_representation.atoms.expressions.assignables.IdentifierAtom;
import structural_representation.atoms.special.*;
import structural_representation.atoms.special.rhs.CollectionInitRHS;
import structural_representation.atoms.special.rhs.CollectionLiteralRHS;
import structural_representation.atoms.special.rhs.RHSAtom;
import structural_representation.atoms.statements.*;
import structural_representation.atoms.statements.assignments.AssignmentAtom;
import structural_representation.atoms.statements.assignments.NoOperandSEAtom;
import structural_representation.atoms.statements.assignments.OperandSEAtom;
import structural_representation.atoms.statements.assignments.StandardAssignmentAtom;
import structural_representation.atoms.statements.control_flow.ForEachStatementAtom;
import structural_representation.atoms.statements.control_flow.ForStatementAtom;
import structural_representation.atoms.statements.control_flow.IfStatementAtom;
import structural_representation.atoms.statements.control_flow.WhileStatementAtom;
import structural_representation.atoms.statements.io.PrintStatementAtom;
import structural_representation.atoms.types.BonesType;
import structural_representation.atoms.types.collections.ArrayType;
import structural_representation.atoms.types.collections.ListType;
import structural_representation.atoms.types.primitives.*;

import java.util.ArrayList;
import java.util.List;

public class BonesVisitor extends BonesParserBaseVisitor<Atom> {
  @Override
  public Atom visitIdent(BonesParser.IdentContext ctx) {
    Token tok = ctx.IDENTIFIER().getSymbol();
    return new IdentifierAtom(tok.getText(), Position.fromToken(tok));
  }

  @Override
  public Atom visitPath(BonesParser.PathContext ctx) {
    List<String> pathSteps = new ArrayList<>();

    for (BonesParser.IdentContext identContext : ctx.ident()) {
      String step = identContext.IDENTIFIER().getSymbol().getText();
      pathSteps.add(step);
    }

    return new PathAtom(pathSteps, Position.fromToken(ctx.PATH().getSymbol()));
  }

  @Override
  public Atom visitREAD_EXPR(BonesParser.READ_EXPRContext ctx) {
    return new ReadExpressionAtom(Position.fromToken(ctx.READ().getSymbol()));
  }

  @Override
  public Atom visitPRINTLN_STAT(BonesParser.PRINTLN_STATContext ctx) {
    ExpressionAtom toPrint = (ExpressionAtom) visit(ctx.expr());
    return new PrintStatementAtom(toPrint, true,
            Position.fromToken(ctx.PRINTLN().getSymbol()));
  }

  @Override
  public Atom visitPRINT_STAT(BonesParser.PRINT_STATContext ctx) {
    ExpressionAtom toPrint = (ExpressionAtom) visit(ctx.expr());
    return new PrintStatementAtom(toPrint, false,
            Position.fromToken(ctx.PRINT().getSymbol()));
  }

  @Override
  public Atom visitImport_stat(BonesParser.Import_statContext ctx) {
    List<String> importSteps = new ArrayList<>();

    for (BonesParser.IdentContext identContext : ctx.ident()) {
      String step = identContext.IDENTIFIER().getSymbol().getText();
      importSteps.add(step);
    }

    return new ImportAtom(importSteps,
            Position.fromToken(ctx.IMPORT().getSymbol()));
  }

  @Override
  public Atom visitCHAR_TYPE(BonesParser.CHAR_TYPEContext ctx) {
    return new CharType();
  }

  @Override
  public Atom visitSTRING_TYPE(BonesParser.STRING_TYPEContext ctx) {
    return new StringType();
  }

  @Override
  public Atom visitARRAY_TYPE(BonesParser.ARRAY_TYPEContext ctx) {
    BonesType elementType = (BonesType) visit(ctx.type());
    return new ArrayType(elementType);
  }

  @Override
  public Atom visitVOID_TYPE(BonesParser.VOID_TYPEContext ctx) {
    return new VoidType();
  }

  @Override
  public Atom visitFLOAT_TYPE(BonesParser.FLOAT_TYPEContext ctx) {
    return new FloatType();
  }

  @Override
  public Atom visitLIST_TYPE(BonesParser.LIST_TYPEContext ctx) {
    BonesType elementType = (BonesType) visit(ctx.type());
    return new ListType(elementType);
  }

  @Override
  public Atom visitBOOL_TYPE(BonesParser.BOOL_TYPEContext ctx) {
    return new BoolType();
  }

  @Override
  public Atom visitINT_TYPE(BonesParser.INT_TYPEContext ctx) {
    return new IntType();
  }

  @Override
  public Atom visitDecl(BonesParser.DeclContext ctx) {
    BonesType type = (BonesType) visit(ctx.type());
    IdentifierAtom ident = (IdentifierAtom) visitIdent(ctx.ident());

    return new DeclarationAtom(type, ident,
            Position.fromToken(ctx.type().getStart()));
  }

  @Override
  public Atom visitMain(BonesParser.MainContext ctx) {

    List<ParamAtom> paramAtoms = new ArrayList<>();

    paramAtoms.add(new ParamAtom(new ArrayType(new StringType()),
            new IdentifierAtom(ctx.ident().IDENTIFIER().getSymbol().getText(),
                    Position.fromToken(ctx.ident().IDENTIFIER().getSymbol())),
            Position.fromToken(ctx.VOID().getSymbol())));

    List<StatementAtom> statements = new ArrayList<>();

    for (BonesParser.StatContext statContext : ctx.stat()) {
      statements.add((StatementAtom) visit(statContext));
    }

    return new FunctionAtom(new VoidType(), "main",
            new ParamListAtom(paramAtoms,
                    Position.fromToken(ctx.VOID().getSymbol())), statements,
            Position.fromToken(ctx.VOID().getSymbol()));
  }

  @Override
  public Atom visitInit(BonesParser.InitContext ctx) {
    BonesType type = (BonesType) visit(ctx.type());
    IdentifierAtom ident = (IdentifierAtom) visitIdent(ctx.ident());
    RHSAtom expr = (RHSAtom) visit(ctx.rhs());

    return new InitialisationAtom(type, ident, expr,
            Position.fromToken(ctx.type().getStart()));
  }

  @Override
  public Atom visitDECLARED_FIELD(BonesParser.DECLARED_FIELDContext ctx) {
    return visitDecl(ctx.decl());
  }

  @Override
  public Atom visitINITIALISED_FIELD(BonesParser.INITIALISED_FIELDContext ctx) {
    return visitInit(ctx.init());
  }

  @Override
  public Atom visitList_literal(BonesParser.List_literalContext ctx) {
    List<RHSAtom> elements = new ArrayList<>();
    ctx.rhs().forEach(x -> elements.add((RHSAtom) visit(x)));

    return new CollectionLiteralRHS(elements,
            CollectionLiteralRHS.CollectionType.LIST,
            Position.fromToken(ctx.LCURLY().getSymbol()));
  }

  @Override
  public Atom visitArray_literal(BonesParser.Array_literalContext ctx) {
    List<RHSAtom> elements = new ArrayList<>();
    ctx.rhs().forEach(x -> elements.add((RHSAtom) visit(x)));

    return new CollectionLiteralRHS(elements,
            CollectionLiteralRHS.CollectionType.ARRAY,
            Position.fromToken(ctx.LCURLY().getSymbol()));
  }

  @Override
  public Atom visitList_init(BonesParser.List_initContext ctx) {
    BonesType elementType = (BonesType) visit(ctx.type());
    int size = Integer.parseInt(ctx.int_literal().getText());
    return new CollectionInitRHS(elementType, size,
            CollectionInitRHS.CollectionType.LIST,
            Position.fromToken(ctx.type().getStart()));
  }

  @Override
  public Atom visitArray_init(BonesParser.Array_initContext ctx) {
    BonesType elementType = (BonesType) visit(ctx.type());
    int size = Integer.parseInt(ctx.int_literal().getText());
    return new CollectionInitRHS(elementType, size,
            CollectionInitRHS.CollectionType.ARRAY,
            Position.fromToken(ctx.type().getStart()));
  }

  @Override
  public Atom visitEXPR_RHS(BonesParser.EXPR_RHSContext ctx) {
    return visit(ctx.expr());
  }

  @Override
  public Atom visitLIST_LIT_RHS(BonesParser.LIST_LIT_RHSContext ctx) {
    return visitList_literal(ctx.list_literal());
  }

  @Override
  public Atom visitARRAY_LIT_RHS(BonesParser.ARRAY_LIT_RHSContext ctx) {
    return visitArray_literal(ctx.array_literal());
  }

  @Override
  public Atom visitLIST_INIT_RHS(BonesParser.LIST_INIT_RHSContext ctx) {
    return visitList_init(ctx.list_init());
  }

  @Override
  public Atom visitARRAY_INIT_RHS(BonesParser.ARRAY_INIT_RHSContext ctx) {
    return visitArray_init(ctx.array_init());
  }

  @Override
  public Atom visitParam(BonesParser.ParamContext ctx) {
    BonesType type = (BonesType) visit(ctx.type());
    IdentifierAtom ident = (IdentifierAtom) visitIdent(ctx.ident());

    return new ParamAtom(type, ident,
            Position.fromToken(ctx.type().getStart()));
  }

  @Override
  public Atom visitParam_list(BonesParser.Param_listContext ctx) {
    List<ParamAtom> params = new ArrayList<>();

    for (BonesParser.ParamContext paramContext : ctx.param()) {
      params.add((ParamAtom) visitParam(paramContext));
    }

    return new ParamListAtom(params, Position.fromToken(ctx.start));
  }

  @Override
  public Atom visitFunct(BonesParser.FunctContext ctx) {
    BonesType returnType = (BonesType) visit(ctx.type());
    String name = ctx.ident().IDENTIFIER().getSymbol().getText();

    ParamListAtom paramList = null;
    if (ctx.param_list() != null)
      paramList = (ParamListAtom) visitParam_list(ctx.param_list());

    List<StatementAtom> statements = new ArrayList<>();

    for (BonesParser.StatContext statContext : ctx.stat()) {
      statements.add((StatementAtom) visit(statContext));
    }

    return new FunctionAtom(returnType, name, paramList, statements,
            Position.fromToken(ctx.type().getStart()));
  }

  @Override
  public Atom visitTRUE_LITERAL(BonesParser.TRUE_LITERALContext ctx) {
    return new BoolLiteralAtom(true,
            Position.fromToken(ctx.TRUE().getSymbol()));
  }

  @Override
  public Atom visitFALSE_LITERAL(BonesParser.FALSE_LITERALContext ctx) {
    return new BoolLiteralAtom(false,
            Position.fromToken(ctx.FALSE().getSymbol()));
  }

  @Override
  public Atom visitChar_literal(BonesParser.Char_literalContext ctx) {
    String text = ctx.CHAR_LIT().getSymbol().getText();
    return new CharacterLiteralAtom(text.charAt(1),
            Position.fromToken(ctx.CHAR_LIT().getSymbol()));
  }

  @Override
  public Atom visitString_literal(BonesParser.String_literalContext ctx) {
    /* QUOTATION MARK REMOVAL */
    String text = ctx.STRING_LITERAL().getSymbol().getText();
    text = text.substring(1, text.length() - 1);

    return new StringLiteralAtom(text,
            Position.fromToken(ctx.STRING_LITERAL().getSymbol()));
  }

  @Override
  public Atom visitInt_literal(BonesParser.Int_literalContext ctx) {
    String text = ctx.INT_LIT().getSymbol().getText();
    Integer value = Integer.parseInt(text);

    return new IntLiteralAtom(value,
            Position.fromToken(ctx.INT_LIT().getSymbol()));
  }

  @Override
  public Atom visitFloat_literal(BonesParser.Float_literalContext ctx) {
    String text = ctx.FLOAT_LIT().getSymbol().getText();
    Float value = Float.parseFloat(text);

    return new FloatLiteralAtom(value,
            Position.fromToken(ctx.FLOAT_LIT().getSymbol()));
  }

  @Override
  public Atom visitARITH_EXPR(BonesParser.ARITH_EXPRContext ctx) {
    ExpressionAtom LHS = (ExpressionAtom) visit(ctx.expr(0));
    ExpressionAtom RHS = (ExpressionAtom) visit(ctx.expr(1));
    String opString = ctx.op.getText();

    return new BinaryOperationAtom(LHS, RHS, opString,
            Position.fromToken(ctx.expr(0).getStart()));
  }

  @Override
  public Atom visitCOMPARISON_EXPR(BonesParser.COMPARISON_EXPRContext ctx) {
    ExpressionAtom LHS = (ExpressionAtom) visit(ctx.expr(0));
    ExpressionAtom RHS = (ExpressionAtom) visit(ctx.expr(1));
    String opString = ctx.op.getText();

    return new BinaryOperationAtom(LHS, RHS, opString,
            Position.fromToken(ctx.expr(0).getStart()));
  }

  @Override
  public Atom visitOR_EXPR(BonesParser.OR_EXPRContext ctx) {
    ExpressionAtom LHS = (ExpressionAtom) visit(ctx.expr(0));
    ExpressionAtom RHS = (ExpressionAtom) visit(ctx.expr(1));

    return new BinaryOperationAtom(LHS, RHS, "||",
            Position.fromToken(ctx.expr(0).getStart()));
  }

  @Override
  public Atom visitCHAR_EXPR(BonesParser.CHAR_EXPRContext ctx) {
    return visitChar_literal(ctx.char_literal());
  }

  @Override
  public Atom visitPARENTHETICAL(BonesParser.PARENTHETICALContext ctx) {
    return visit(ctx.expr());
  }

  @Override
  public Atom visitSTRING_EXPR(BonesParser.STRING_EXPRContext ctx) {
    return visitString_literal(ctx.string_literal());
  }

  @Override
  public Atom visitAND_EXPR(BonesParser.AND_EXPRContext ctx) {
    ExpressionAtom LHS = (ExpressionAtom) visit(ctx.expr(0));
    ExpressionAtom RHS = (ExpressionAtom) visit(ctx.expr(1));

    return new BinaryOperationAtom(LHS, RHS, "&&",
            Position.fromToken(ctx.expr(0).getStart()));
  }

  @Override
  public Atom visitINT_EXPR(BonesParser.INT_EXPRContext ctx) {
    return visitInt_literal(ctx.int_literal());
  }

  @Override
  public Atom visitFUNCTION_CALL_EXPR(BonesParser.FUNCTION_CALL_EXPRContext ctx) {
    // TODO: temp fix - currently only looking at last part of method path
    String name = ctx.ident(ctx.ident().size() - 1).
            IDENTIFIER().getSymbol().getText();

    List<ExpressionAtom> expressions = new ArrayList<>();

    ctx.expr().forEach(x -> expressions.add((ExpressionAtom) visit(x)));

    return new FunctionCallAtom(name, expressions);
  }

  @Override
  public Atom visitASSIGNABLE_EXPR(BonesParser.ASSIGNABLE_EXPRContext ctx) {
    return visit(ctx.assignable());
  }

  @Override
  public Atom visitMUL_DIV_MOD_EXPR(BonesParser.MUL_DIV_MOD_EXPRContext ctx) {
    ExpressionAtom LHS = (ExpressionAtom) visit(ctx.expr(0));
    ExpressionAtom RHS = (ExpressionAtom) visit(ctx.expr(1));
    String opString = ctx.op.getText();

    return new BinaryOperationAtom(LHS, RHS, opString,
            Position.fromToken(ctx.expr(0).getStart()));
  }

  @Override
  public Atom visitFLOAT_EXPR(BonesParser.FLOAT_EXPRContext ctx) {
    return visitFloat_literal(ctx.float_literal());
  }

  @Override
  public Atom visitEXPONENTIATION_EXPR(BonesParser.EXPONENTIATION_EXPRContext ctx) {
    ExpressionAtom LHS = (ExpressionAtom) visit(ctx.expr(0));
    ExpressionAtom RHS = (ExpressionAtom) visit(ctx.expr(1));

    return new BinaryOperationAtom(LHS, RHS, "^",
            Position.fromToken(ctx.expr(0).getStart()));
  }

  @Override
  public Atom visitAT_INDEX_EXPR(BonesParser.AT_INDEX_EXPRContext ctx) {
    ExpressionAtom LHS = (ExpressionAtom) visit(ctx.expr(0));
    ExpressionAtom RHS = (ExpressionAtom) visit(ctx.expr(1));

    return new BinaryOperationAtom(LHS, RHS, "@",
            Position.fromToken(ctx.expr(0).getStart()));
  }

  @Override
  public Atom visitEQUALITY_EXPR(BonesParser.EQUALITY_EXPRContext ctx) {
    ExpressionAtom LHS = (ExpressionAtom) visit(ctx.expr(0));
    ExpressionAtom RHS = (ExpressionAtom) visit(ctx.expr(1));
    String opString = ctx.op.getText();

    return new BinaryOperationAtom(LHS, RHS, opString,
            Position.fromToken(ctx.expr(0).getStart()));
  }

  @Override
  public Atom visitBOOL_EXPR(BonesParser.BOOL_EXPRContext ctx) {
    return visit(ctx.bool_literal());
  }

  @Override
  public Atom visitUNARY_OP_EXPR(BonesParser.UNARY_OP_EXPRContext ctx) {
    ExpressionAtom expr = (ExpressionAtom) visit(ctx.expr());
    String opString = ctx.op.getText();

    return new UnaryOperationAtom(expr, opString,
            Position.fromToken(ctx.op));
  }

  @Override
  public Atom visitList_elem(BonesParser.List_elemContext ctx) {
    String identifier = ctx.ident().IDENTIFIER().getSymbol().getText();

    List<Integer> indices = new ArrayList<>();

    for (BonesParser.Int_literalContext intContext : ctx.int_literal()) {
      Integer index = Integer.parseInt(
              intContext.INT_LIT().getSymbol().getText());
      indices.add(index);
    }

    return new ListElemAtom(identifier, indices,
            Position.fromToken(ctx.ident().IDENTIFIER().getSymbol()));
  }

  @Override
  public Atom visitArray_elem(BonesParser.Array_elemContext ctx) {
    String identifier = ctx.ident().IDENTIFIER().getSymbol().getText();

    List<Integer> indices = new ArrayList<>();

    for (BonesParser.Int_literalContext intContext : ctx.int_literal()) {
      Integer index = Integer.parseInt(
              intContext.INT_LIT().getSymbol().getText());
      indices.add(index);
    }

    return new ArrayElemAtom(identifier, indices,
            Position.fromToken(ctx.ident().IDENTIFIER().getSymbol()));
  }

  @Override
  public Atom visitIDENT_ASSIGNABLE(BonesParser.IDENT_ASSIGNABLEContext ctx) {
    return visitIdent(ctx.ident());
  }

  @Override
  public Atom visitLIST_ELEM_ASSIGNABLE(BonesParser.LIST_ELEM_ASSIGNABLEContext ctx) {
    return visitList_elem(ctx.list_elem());
  }

  @Override
  public Atom visitARRAY_ELEM_ASSIGNABLE(BonesParser.ARRAY_ELEM_ASSIGNABLEContext ctx) {
    return visitArray_elem(ctx.array_elem());
  }

  @Override
  public Atom visitSTANDARD_ASSIGNMENT(BonesParser.STANDARD_ASSIGNMENTContext ctx) {
    AssignableAtom lhs = (AssignableAtom) visit(ctx.assignable());
    RHSAtom rhs = (RHSAtom) visit(ctx.rhs());

    return new StandardAssignmentAtom(lhs, rhs,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitNEGATE_ASSIGNMENT(BonesParser.NEGATE_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());

    return new NoOperandSEAtom(assignable, NoOperandSEAtom.Operator.NEGATE,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitINCREMENT_ASSIGNMENT(BonesParser.INCREMENT_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());

    return new NoOperandSEAtom(assignable, NoOperandSEAtom.Operator.INCREMENT,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitDECREMENT_ASSIGNMENT(BonesParser.DECREMENT_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());

    return new NoOperandSEAtom(assignable, NoOperandSEAtom.Operator.DECREMENT,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitADD_ASSIGNMENT(BonesParser.ADD_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());

    return new OperandSEAtom(assignable,
            OperandSEAtom.Operator.ADD_ASSIGN, expression,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitSUB_ASSIGNMENT(BonesParser.SUB_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());

    return new OperandSEAtom(assignable,
            OperandSEAtom.Operator.SUB_ASSIGN, expression,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitMUL_ASSIGNMENT(BonesParser.MUL_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());

    return new OperandSEAtom(assignable,
            OperandSEAtom.Operator.MUL_ASSIGN, expression,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitDIV_ASSIGNMENT(BonesParser.DIV_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());

    return new OperandSEAtom(assignable,
            OperandSEAtom.Operator.DIV_ASSIGN, expression,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitMOD_ASSIGNMENT(BonesParser.MOD_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());

    return new OperandSEAtom(assignable,
            OperandSEAtom.Operator.MOD_ASSIGN, expression,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitAND_ASSIGNMENT(BonesParser.AND_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());

    return new OperandSEAtom(assignable,
            OperandSEAtom.Operator.AND_ASSIGN, expression,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitOR_ASSIGNMENT(BonesParser.OR_ASSIGNMENTContext ctx) {
    AssignableAtom assignable = (AssignableAtom) visit(ctx.assignable());
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());

    return new OperandSEAtom(assignable,
            OperandSEAtom.Operator.OR_ASSIGN, expression,
            Position.fromToken(ctx.assignable().getStart()));
  }

  @Override
  public Atom visitFOR_STAT(BonesParser.FOR_STATContext ctx) {
    InitialisationAtom initialisation =
            (InitialisationAtom) visitInit(ctx.init());
    ExpressionAtom loopCondition = (ExpressionAtom) visit(ctx.expr());
    AssignmentAtom incrementation = (AssignmentAtom) visit(ctx.assignment());

    List<StatementAtom> body = new ArrayList<>();

    for (BonesParser.StatContext statContext : ctx.body().stat()) {
      StatementAtom stat = (StatementAtom) visit(statContext);
      body.add(stat);
    }

    return new ForStatementAtom(initialisation,
            loopCondition, incrementation, body,
            Position.fromToken(ctx.FOR().getSymbol()));
  }

  @Override
  public Atom visitFOREACH_STAT(BonesParser.FOREACH_STATContext ctx) {
    IdentifierAtom token = (IdentifierAtom) visitIdent(ctx.ident());
    ExpressionAtom collection = (ExpressionAtom) visit(ctx.expr());

    List<StatementAtom> body = new ArrayList<>();

    for (BonesParser.StatContext statContext : ctx.body().stat()) {
      StatementAtom stat = (StatementAtom) visit(statContext);
      body.add(stat);
    }

    return new ForEachStatementAtom(token, collection, body,
            Position.fromToken(ctx.FOREACH().getSymbol()));
  }

  @Override
  public Atom visitIF_STAT(BonesParser.IF_STATContext ctx) {
    List<ExpressionAtom> conditions = new ArrayList<>();
    for (BonesParser.ExprContext exprContext : ctx.expr()) {
      ExpressionAtom condition = (ExpressionAtom) visit(exprContext);
      conditions.add(condition);
    }

    List<List<StatementAtom>> bodies = new ArrayList<>();
    for (BonesParser.BodyContext bodyContext : ctx.body()) {
      List<StatementAtom> body = new ArrayList<>();
      for (BonesParser.StatContext statContext : bodyContext.stat()) {
        StatementAtom statement = (StatementAtom) visit(statContext);
        body.add(statement);
      }
      bodies.add(body);
    }

    return new IfStatementAtom(conditions, bodies,
            Position.fromToken(ctx.IF(0).getSymbol()));
  }

  @Override
  public Atom visitWHILE_STAT(BonesParser.WHILE_STATContext ctx) {
    ExpressionAtom loopCondition = (ExpressionAtom) visit(ctx.expr());

    List<StatementAtom> body = new ArrayList<>();

    for (BonesParser.StatContext statContext : ctx.body().stat()) {
      StatementAtom stat = (StatementAtom) visit(statContext);
      body.add(stat);
    }

    return new WhileStatementAtom(loopCondition, body,
            Position.fromToken(ctx.WHILE().getSymbol()));
  }

  @Override
  public Atom visitVOID_RETURN_STAT(BonesParser.VOID_RETURN_STATContext ctx) {
    return new VoidReturnAtom(Position.fromToken(ctx.RETURN().getSymbol()));
  }

  @Override
  public Atom visitRETURN_STAT(BonesParser.RETURN_STATContext ctx) {
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());

    return new ReturnAtom(expression,
            Position.fromToken(ctx.RETURN().getSymbol()));
  }

  @Override
  public Atom visitDECLARATION_STAT(BonesParser.DECLARATION_STATContext ctx) {
    return visitDecl(ctx.decl());
  }

  @Override
  public Atom visitINITIALISATION_STAT(BonesParser.INITIALISATION_STATContext ctx) {
    return visitInit(ctx.init());
  }

  @Override
  public Atom visitASSIGNMENT_STAT(BonesParser.ASSIGNMENT_STATContext ctx) {
    return visit(ctx.assignment());
  }

  @Override
  public Atom visitEXPRESSION_STAT(BonesParser.EXPRESSION_STATContext ctx) {
    ExpressionAtom expression = (ExpressionAtom) visit(ctx.expr());
    return new ExpressionStatementAtom(expression,
            Position.fromToken(ctx.expr().getStart()));
  }

  @Override
  public Atom visitClass_rule(BonesParser.Class_ruleContext ctx) {
    PathAtom path = (PathAtom) visitPath(ctx.path());

    List<ImportAtom> imports = new ArrayList<>();

    for (BonesParser.Import_statContext importContext : ctx.import_stat()) {
      ImportAtom imp = (ImportAtom) visitImport_stat(importContext);
      imports.add(imp);
    }

    IdentifierAtom className = (IdentifierAtom) visitIdent(ctx.ident());

    List<DeclarationAtom> fields = new ArrayList<>();

    for (BonesParser.FieldContext fieldContext : ctx.field()) {
      DeclarationAtom field = (DeclarationAtom) visit(fieldContext);
      fields.add(field);
    }

    List<FunctionAtom> functions = new ArrayList<>();

    if (ctx.main() != null) {
      FunctionAtom main = (FunctionAtom) visitMain(ctx.main());
      functions.add(main);
    }

    for (BonesParser.FunctContext functContext : ctx.funct()) {
      FunctionAtom function = (FunctionAtom) visitFunct(functContext);
      functions.add(function);
    }

    return new ClassAtom(path, imports, className, fields, functions,
            Position.fromToken(ctx.CLASS().getSymbol()));
  }
}

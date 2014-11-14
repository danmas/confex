package ru.erv.drakongen.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.erv.drakongen.j2dg.japa.AspectView;
import ru.erv.drakongen.j2dg.japa.CompilationNodeDG;
import ru.erv.drakongen.j2dg.japa.WithMembersNodeDG;
import japa.parser.ast.BlockComment;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;
import japa.parser.ast.visitor.VoidVisitor;
import japa.parser.ast.visitor.DumpVisitor.SourcePrinter;


public final class TestDumpVisitorGml implements VoidVisitor<Object> {

    private static class SourcePrinter {

        private int level = 0;


        //@Override
        public String toString() {
            return getSource();
        }
    }

    
    //-- текущий источник
    protected static WithMembersNodeDG src_node = null;
    
    //-- карта аспектов
    protected static Map<String,AspectView> cur_aspects = new HashMap<String,AspectView>();

    
    public static AspectView getAspect(String name) {
    	return cur_aspects.get(name);
    }

    
    private static class GmlPrinter extends SourcePrinter {

    	public GmlPrinter() {
    		print("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
    				"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">\n" +
    				"  <!--Created by DG2J v.0.9.7-->\n" +
    				"  <key for=\"graphml\" id=\"d0\" yfiles.type=\"resources\"/>\n" +
    				"  <key for=\"port\" id=\"d1\" yfiles.type=\"portgraphics\"/>\n" +
    				"  <key for=\"port\" id=\"d2\" yfiles.type=\"portgeometry\"/>\n" +
    				"  <key for=\"port\" id=\"d3\" yfiles.type=\"portuserdata\"/>\n" +
    				"  <key attr.name=\"mark\" attr.type=\"string\" for=\"node\" id=\"d4\"/>\n" +
    				"  <key attr.name=\"url\" attr.type=\"string\" for=\"node\" id=\"d5\"/>\n" +
    				"  <key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d6\"/>\n" +
    				"  <key for=\"node\" id=\"d7\" yfiles.type=\"nodegraphics\"/>\n" +
    				"  <key attr.name=\"Description\" attr.type=\"string\" for=\"graph\" id=\"d8\"/>\n" +
    				"  <key attr.name=\"url\" attr.type=\"string\" for=\"edge\" id=\"d9\"/>\n" +
    				"  <key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d10\"/>\n" +
    				"  <key for=\"edge\" id=\"d11\" yfiles.type=\"edgegraphics\"/>\n" +
    				"  <graph edgedefault=\"directed\" id=\"G\">\n" +
    				"    <data key=\"d8\"/>\n" +
    				"");
    	}
    }
    
    public    static AspectView default_aspect = new AspectView("#E5FFFF", "#E5FFFF");
    protected static AspectView current_aspect = null;
    
    public static AspectView getDefaultAspect() {
    	return default_aspect;
    }
    
    public DumpVisitorGml() {
    	super();
        default_aspect = new AspectView("#E5FFFF", "#E5FFFF");
        current_aspect = default_aspect;
        
    	cur_aspects.put("DEFAULT", default_aspect);
    	cur_aspects.put("ASPECTS", new AspectView("#FF00FF", "#FFFF00"));
    }
    
    private final SourcePrinter printer = new SourcePrinter();

    private CompilationNodeDG compilationNodeDG = null;
    
    public String getSource() {
        return printer.getSource();
    }
    
    public String getGmlSource() {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
				"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">\n" +
				"  <!--Created by DG2J v.0.9.7-->\n" +
				"  <key for=\"graphml\" id=\"d0\" yfiles.type=\"resources\"/>\n" +
				"  <key for=\"port\" id=\"d1\" yfiles.type=\"portgraphics\"/>\n" +
				"  <key for=\"port\" id=\"d2\" yfiles.type=\"portgeometry\"/>\n" +
				"  <key for=\"port\" id=\"d3\" yfiles.type=\"portuserdata\"/>\n" +
				"  <key attr.name=\"mark\" attr.type=\"string\" for=\"node\" id=\"d4\"/>\n" +
				"  <key attr.name=\"url\" attr.type=\"string\" for=\"node\" id=\"d5\"/>\n" +
				"  <key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d6\"/>\n" +
				"  <key for=\"node\" id=\"d7\" yfiles.type=\"nodegraphics\"/>\n" +
				"  <key attr.name=\"Description\" attr.type=\"string\" for=\"graph\" id=\"d8\"/>\n" +
				"  <key attr.name=\"url\" attr.type=\"string\" for=\"edge\" id=\"d9\"/>\n" +
				"  <key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d10\"/>\n" +
				"  <key for=\"edge\" id=\"d11\" yfiles.type=\"edgegraphics\"/>\n" +
				"  <graph edgedefault=\"directed\" id=\"G\">\n" +
				"    <data key=\"d8\"/>\n" +
				"";
    	
    	
		compilationNodeDG.calcXY();
		
		
    	return str + compilationNodeDG.print() 
    	    	+ "</graph>\n"
    	    	+ "</graphml>\n";
    			
    }

	public static String ctrl_code = "";

    public String getControlString() {
    	return ctrl_code;
    }

    public static void addCtrlCode(String code) {
    	ctrl_code += code;
    }
    
    
    protected void appendLostCode(String lost_code) {
    	/*
		NodeDG ndg = new NodeDG();
		ndg.setType("ACTION");
		ndg.setCode(lost_code);
		String s = lost_code.trim();
		ndg.setComment("LC: "+s.substring(0,s.length()>20?20:s.length()));
		src_node.addMember(ndg);
		*/
    }

}

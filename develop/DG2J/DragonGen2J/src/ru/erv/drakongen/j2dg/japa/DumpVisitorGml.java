package ru.erv.drakongen.j2dg.japa;

import japa.parser.ast.BlockComment;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.erv.drakongen.DrakonUtils;

/**
 * @author Roman Eremeev 
 */

public final class DumpVisitorGml implements VoidVisitor<Object> {

    private static class SourcePrinter {

        private int level = 0;

        private boolean indented = false;

        private final StringBuilder buf = new StringBuilder();

        private final StringBuilder tmp_buf = new StringBuilder();
        
        
        public void clearTmpBuf() {
        	tmp_buf.setLength(0);
        }
        
        public String getTmpBufStr() {
        	return tmp_buf.toString();
        }
        
        
        public void indent() {
            level++;
        }

        public void unindent() {
            level--;
        }

        private void makeIndent() {
            for (int i = 0; i < level; i++) {
                buf.append("    ");
                tmp_buf.append("    ");
            }
        }


        public void print(String arg) {
        	print(arg,true);
        }
        
        public void print(String arg, boolean write_buf) {
            if (!indented) {
                makeIndent();
                indented = true;
            }
            buf.append(arg);
            if(write_buf)
            	tmp_buf.append(arg);
        }


        public void printLn(String arg) {
        	printLn(arg,true);
        }
        
        public void printLn(String arg, boolean write_buf) {
            print(arg, write_buf);
            printLn();
        }

        public void printLn() {
            buf.append("\n");
            tmp_buf.append("\n");
            indented = false;
        }

        public String getSource() {
            return buf.toString();
        }

        @Override
        public String toString() {
            return getSource();
        }
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

    
    //-- текущий источник
    protected static WithMembersNodeDG src_node = null;
    
    //<DG2J aspect_beg="ASPECTS"/>
    //-- карта аспектов
    protected static Map<String,AspectView> cur_aspects = new HashMap<String,AspectView>();
    //<DG2J aspect_end="ASPECTS"/>    
    
    //<DG2J aspect_begin="ASPECTS"/>
    public    static AspectView default_aspect = new AspectView("DEFAULT","#E5FFFF", "#E5FFFF");
    protected static AspectView current_aspect = null;
    protected static AspectView problem_aspect = null;    
	public static String ctrl_code = "";
	protected String label = "";
    private final SourcePrinter printer = new SourcePrinter();
    private CompilationNodeDG compilationNodeDG = null;
    protected static SwitchNodeDG src_switch_node = null;
    //<DG2J aspect_end="ASPECTS"/>    

    
    
    public static AspectView getAspect(String name) {
    	return cur_aspects.get(name);
    }

    public static AspectView getDefaultAspect() {
    	return default_aspect;
    }
    
    
    /**
     * Remove from str all aspect managment parts
     * 
     * @param str
     * @return
     */
    protected String cleanFromAspect(String str) {
    	int i1 = str.indexOf("//<DG2J ");
    	
    	if(i1 > -1) {
    		str = str.substring(0, i1);
    		int i2 = str.indexOf("\n",0);
    		if(i2 > -1) {
    			str = str.substring(i2+1, str.length()); 
    		}
    	}
    	return str;
    }


    //<DG2J aspect_begin="ASPECTS"/>
	protected void takeAspect(Node n) {
		Comment c = n.getComment();
		if(c != null) {
			takeAspect(c.toString());
		}
	}
    
    /**
     * Extracts the start and the end of the aspect's manager
     * Return the string without aspect string
     * 
     * @param str
     * @return
     */
    //-- <DG2J aspect_begin="ASPECTS"/>
	protected String takeAspect(String str) {
		//--<DG2J aspect_begin="DRAKON"/>
		str = str.trim();
        if(str.startsWith("//<DG2J ") || str.startsWith("// <DG2J ")) {
        	int i = str.indexOf("aspect_begin="); 
        	if(i != -1) {
	        	//-- выделяем аспект
	        	int i2 = str.indexOf("\"",i+"aspect_begin=\"".length()+1);
	        	if(i2 == -1) {
	        		System.err.println("Error in '<DG2J aspect_begin=' str:"+str);
	        		return cleanFromAspect(str);
	        	}
	        	String s_aspect = str.substring(i+"aspect_begin=\"".length(),i2);
	        	
	        	AspectView a = DumpVisitorGml.getAspect(s_aspect);
	        	if(a == null) {
	        		System.err.println("Not found aspect "+ s_aspect +" in cur_aspects.");
	        		current_aspect = DumpVisitorGml.getDefaultAspect();
	        		return cleanFromAspect(str);
	        	}
	        	current_aspect = a;
	        	System.out.println("=A======= " + current_aspect);
        		return cleanFromAspect(str);
        	}
        	i = str.indexOf("aspect_end="); 
        	if(i != -1) {
	        	//-- выделяем аспект
	        	int i2 = str.indexOf("\"",i+"aspect_end=".length()+1);
	        	if(i2 == -1) {
	        		System.err.println("Error in '<DG2J aspect_end=' str:"+str);
	        		return cleanFromAspect(str);
	        	}
	        	//String aspect = str.substring(i+"aspect_begin=".length(),i2);
	        	current_aspect = DumpVisitorGml.getDefaultAspect();
	        	return cleanFromAspect(str);
        	}
        }
        //--<DG2J aspect_end="DRAKON"/>
        return str;
	}
	//<DG2J aspect_end="ASPECTS"/>

	//<DG2J aspect_begin="DRAKON"/>
	protected void setComment(Comment c, String alt_com, NodeDG ndg) {
	    //Comment c = n.getComment();
	    if(c != null) {
	    	String cs = takeAspect(c.toString());
	    	if(cs.length() > 0)
	    		ndg.setComment(cs,ConstantsDG.max_comment_node_length);
	    	else 
	    		ndg.setComment(alt_com,ConstantsDG.max_comment_node_length);
	    } else {
	    	ndg.setComment(alt_com,ConstantsDG.max_comment_node_length);
	    }
	    ndg.setAspect(current_aspect);
	}
	
    
    //<DG2J aspect_beg="ASPECTS"/>
    /**
     * xdfg sdfg 
     * sd fgs dfgsdfg sdfg 
     * sd fgsdfg 
     * sd fgsd fgsdf gsd fgfds g
     * 
     */
    public DumpVisitorGml() {
    	super();
    	//<DG2J aspect_begin="ASPECTS"/>
        default_aspect = new AspectView("DEFAULT","#E5FFFF", "#E5FFFF");
        problem_aspect = new AspectView("PROBLEM","#FF0000", "#FF0000");
        current_aspect = default_aspect;
        
    	cur_aspects.put("DEFAULT", default_aspect);
    	cur_aspects.put("ASPECTS", new AspectView("ASPECTS","#ffff00", "#E5FFFF"));
    	cur_aspects.put("DRAKON", new AspectView("DRAKON","#ffaaaa", "#E5FFFF"));
    	cur_aspects.put("PROBLEM", problem_aspect);
    	//<DG2J aspect_end="ASPECTS"/>
    }
    //<DG2J aspect_end="ASPECTS"/>
    
    
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


    public String getControlString() {
    	return ctrl_code;
    }

    public static void addCtrlCode(String code) {
    	ctrl_code += code;
    }
    
    
    protected void appendLostCode(String lost_code, Node n) {
    	if(lost_code!=null && !lost_code.trim().equals("")) {
    		System.err.println("in "+n.getClass().getName()+" lost_code: "+ lost_code);
    	}
    	
    	//int i = lost_code.indexOf("outer:");
    	//if(i >=0) {
    	//	System.out.println(" catch! ");
    	//}
    	/*
		NodeDG ndg = new NodeDG();
		ndg.setType("ACTION");
		ndg.setCode(lost_code);
		String s = lost_code.trim();
		ndg.setComment("LC: "+s.substring(0,s.length()>20?20:s.length()));
		src_node.addMember(ndg);
		*/
    }
    
    public void visit(CompilationUnit n, Object arg) {
    	//<DG2J aspect_begin="DRAKON"/>
    	compilationNodeDG = new CompilationNodeDG();
    	
    	src_node = compilationNodeDG;
    	
    	compilationNodeDG.setType(DrakonUtils.DI_COMPIL_BEG);
    	compilationNodeDG.setComment("Compilation",ConstantsDG.max_comment_node_length);
    	
    	String lost_code = printer.getTmpBufStr();
    	printer.clearTmpBuf();
    	
    	compilationNodeDG.setCode(printer.getTmpBufStr());
    	
    	NodeDG pnm = new NodeDG();
    	pnm.setType(DrakonUtils.DI_ACTION);
    	src_node.addMember(pnm); 
    	
    	//<DG2J aspect_end="DRAKON"/>
        if (n.getPackage() != null) {
            n.getPackage().accept(this, new Boolean(true));
        }
        if (n.getImports() != null) {
            for (ImportDeclaration i : n.getImports()) {
                i.accept(this, new Boolean(true));
            }
            printer.printLn();
        }
        //<DG2J aspect_begin="DRAKON"/>
        appendLostCode(lost_code,n);
        
        //sdfsdfs
        //<DG2J aspect_begin="DRAKON"/>
        pnm.setCode(printer.getTmpBufStr());
        setComment(n.getComment(),printer.getTmpBufStr(),pnm);
        
    	printer.clearTmpBuf();

    	pnm.setComment("package\nimports",ConstantsDG.max_comment_node_length);
    	//<DG2J aspect_end="DRAKON"/>
        if (n.getTypes() != null) {
            for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i.hasNext();) {
            	lost_code = printer.getTmpBufStr();
            	printer.clearTmpBuf();
            	
                i.next().accept(this, arg);
                printer.printLn();
                
                System.err.println("NOT IN CODES?\n " + lost_code 
                		+ printer.getTmpBufStr() + "\n");
                printer.clearTmpBuf();
                
                if (i.hasNext()) {
                    printer.printLn();
                    //System.err.println("NOT IN CODES?\n " + printer.getTmpBufStr() + "\n");
                }
            }
        }
    }

    
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
    	WithMembersNodeDG rem_src_node = src_node; 

    	ClassNodeDG classnm = new ClassNodeDG();
    	//CLASS
    	classnm.setType(DrakonUtils.DI_SUB_COMPIL); 
    	
    	//<DG2J aspect_begin="DRAKON"/>
    	rem_src_node.addMember(classnm);
    	
    	String lost_code = printer.getTmpBufStr();
    	//<DG2J aspect_end="DRAKON"/>
    	printer.clearTmpBuf();
    	
    	//<DG2J aspect_begin="DRAKON"/>
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        
        printModifiers(n.getModifiers());

      //<DG2J aspect_begin="DRAKON"/>
        if (n.isInterface()) {
        	setComment(n.getComment(), "interface", classnm);
        	//classnm.setComment("interface",ConstantsDG.max_comment_node_length);
            printer.print("interface ");
        } else {
        	setComment(n.getComment(), "class", classnm);
        	//classnm.setComment("class",ConstantsDG.max_comment_node_length);
            printer.print("class ");
        }

        //-- выводим имя
    	printer.print(n.getName());
    	
        printTypeParameters(n.getTypeParameters(), arg);
        
        if (n.getExtends() != null) {
            printer.print(" extends ");
            for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        if (n.getImplements() != null) {
            printer.print(" implements ");
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        printer.printLn(" {");
        
        String buf_beg = printer.getTmpBufStr();
        printer.clearTmpBuf();
        
        appendLostCode(lost_code,n);

        classnm.setCode(buf_beg);
        setComment(n.getComment(),buf_beg,classnm);
//        Comment c = n.getComment();
//        if(c != null) {
//        	classnm.setComment(c.toString(),ConstantsDG.max_comment_node_length);
//        } else {
//        	classnm.setComment(buf_beg,ConstantsDG.max_comment_node_length);
//        }

    	src_node = classnm;
        
        printer.indent();
        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
        
    	NodeDG endnm = new NodeDG();
    	endnm.setAspect(current_aspect);
    	src_node.addMember(endnm);
    	endnm.setType(DrakonUtils.DI_CLASS_END);
    	
    	endnm.setCode("}\\\\--end class\n");
    	//-- обходим закрывающую скобку т.к. она уже есть (откуда?)
        printer.unindent();
        printer.print("}\\\\--end class\n");

    	src_node = rem_src_node;
    }
    //<DG2J aspect_end="DRAKON"/>
    
    public void visit(ConstructorDeclaration n, Object arg) {
    	takeAspect(n);
    	WithMembersNodeDG rem_src_node = src_node; 
    	
    	ProcNodeDG n_proc_beg = new ProcNodeDG();
    	n_proc_beg.setAspect(current_aspect);
    	n_proc_beg.setType(DrakonUtils.DI_PROC_BEG);
    	rem_src_node.addMember(n_proc_beg); 

    	src_node = n_proc_beg;
    	
    	String lost_code = printer.getTmpBufStr();
    	printer.clearTmpBuf();
    	
    	//-- comment
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        
//    	NodeDG docnm = new NodeDG();
//    	docnm.setType("DI_PROC_DOC");
//    	n_proc_beg.addMember(docnm);
//    	
//        Comment c = n.getComment();
//    	
//    	docnm.setComment(j_d_a + c.toString(), 300);
//    	docnm.setCode(j_d_a + c.toString());
    	
    	printer.clearTmpBuf();

        printModifiers(n.getModifiers());

        printTypeParameters(n.getTypeParameters(), arg);
        if (n.getTypeParameters() != null) {
            printer.print(" ");
        }
        printer.print(n.getName());
        //n_proc_beg.setComment(n.getName(),ConstantsDG.max_comment_node_length);
        setComment(n.getComment(), n.getName(), n_proc_beg);
        
        printer.print("(");
        if (n.getParameters() != null) {
            for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");

        if (n.getThrows() != null) {
            printer.print(" throws ");
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        
        appendLostCode(lost_code,n);

        n_proc_beg.setCode(printer.getTmpBufStr() + " {\n");
       	//n_proc_beg.setComment(printer.getTmpBufStr(),ConstantsDG.max_comment_node_length);
       	setComment(n.getComment(), printer.getTmpBufStr(), n_proc_beg);
        printer.clearTmpBuf();

        printer.print(" ");
        n.getBlock().accept(this, arg);
   	
    	
        //-- end of proc
    	NodeDG n_proc_end = new NodeDG();
    	n_proc_end.setAspect(current_aspect);
    	n_proc_end.setType(DrakonUtils.DI_PROC_END);
    	src_node.addMember(n_proc_end); 
    	
    	n_proc_end.setCode(""); //"\n}\\\\--end constructor\n");
    	
    	src_node = rem_src_node;
    }
    
    
    /**
     * 
     */
    public void visit(MethodDeclaration n, Object arg) {
    	WithMembersNodeDG rem_src_node = src_node; 
    	
    	ProcNodeDG n_proc_beg = new ProcNodeDG();
        n_proc_beg.setType(DrakonUtils.DI_PROC_BEG);
        rem_src_node.addMember(n_proc_beg);
        
        src_node = n_proc_beg;
        
    	printer.clearTmpBuf();
    	
    	//-- comment
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        String j_d_a = printer.getTmpBufStr();
        
        //System.err.println("jda:" + j_d_a+ "\n");
        //System.err.println("com:" + n.getComment().toString());
        
    	NodeDG docnm = new NodeDG();
    	docnm.setType("DI_PROC_DOC");
    	n_proc_beg.addMember(docnm);
    	
        Comment c = n.getComment();
        String s = "";
        if(c != null)  { 
        	try {
        	s = new String(c.toString().getBytes(),"UTF-8");
        	} catch (Exception e) {
				System.err.println(e.getMessage());
			}
        	System.out.println("=1======= " + s);
        	s = takeAspect(s);
        	System.out.println("=2======= " + s);
        	n_proc_beg.setAspect(current_aspect);
        }
    	
    	docnm.setComment(j_d_a + s, 300);
    	docnm.setCode(j_d_a + s);
    	
    	printer.clearTmpBuf();
        
        printModifiers(n.getModifiers());

        printTypeParameters(n.getTypeParameters(), arg);
        if (n.getTypeParameters() != null) {
            printer.print(" ");
        }

        n.getType().accept(this, arg);
        printer.print(" ");
        printer.print(n.getName());
        
        n_proc_beg.setComment(n.getName(),ConstantsDG.max_comment_node_length);

        printer.print("(");
        if (n.getParameters() != null) {
            for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");

        for (int i = 0; i < n.getArrayCount(); i++) {
            printer.print("[]");
        }

        if (n.getThrows() != null) {
            printer.print(" throws ");
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        
        n_proc_beg.setCode(printer.getTmpBufStr() + " {\n");
        printer.clearTmpBuf();
        
        if (n.getBody() == null) {
            printer.print(";");
        } else {
            printer.print(" ");
            n.getBody().accept(this, arg);
        }
    	String lost_code = printer.getTmpBufStr();
    	printer.clearTmpBuf();
    	appendLostCode(lost_code,n);
    	
/*
if(compact_proc_body) {    	
    	NodeDG n_body = new NodeDG();
    	n_body.setX(sh_x);
    	n_body.setY(sh_y);
    	sh_y += sh_dy;
        
    	n_body.setType("ACTION");
    	n_body.setComment("Action");
    	
        n_body.setCode(printer.getTmpBufStr());
        printer.clearTmpBuf();
        
    	src_node = n_body; //-- что бы был переход от метода к методу
}
*/    	
        //-- end of proc
    	int sz = src_node.getMembers().size();
    	//NodeDG nn = src_node.getMembers().get(sz-1);
    	//System.err.println("nn-->" + nn.getType() + " " + nn.getComment()) ;
    	if(sz > 0 && src_node.getMembers().get(sz-1).getType().equals(DrakonUtils.DI_RETURN)) { 
    		src_node.getMembers().get(sz-1).setType(DrakonUtils.DI_PROC_END);
    	} else {
	    	NodeDG n_proc_end = new NodeDG();
	    	n_proc_end.setAspect(current_aspect);
	    	n_proc_end.setType(DrakonUtils.DI_PROC_END);
	    	src_node.addMember(n_proc_end);
	    	
	    	n_proc_end.setCode(""); //"\n}\\\\-- end proc\n");
    	}
    	
    	src_node = rem_src_node;
    }
    

    public void visit(LabeledStmt n, Object arg) {
    	takeAspect(n);
    	printer.clearTmpBuf();
    	
        printer.print(n.getLabel());
        printer.print(": ");
        
        label = printer.getTmpBufStr();
        printer.clearTmpBuf();
        
        n.getStmt().accept(this, new Boolean(true));
        label = "";
    }

    
    public void visit(EmptyStmt n, Object arg) {
        printer.print(";");
    }

    
    public void visit(MethodCallExpr n, Object arg) {
    	Boolean collapse = new Boolean(false); 
    	if(arg != null && arg.getClass().equals(collapse.getClass())) {
    		collapse = (Boolean) arg;
    	}
    	NodeDG nm = null;
    	String lost_code = ""; 
    	if(!collapse) {
	    	nm = new NodeDG();
	        nm.setType(NodeDG.DI_CALL_PROC);
	        
	        src_node.addMember(nm);
	        
	    	lost_code = printer.getTmpBufStr();
	    	printer.clearTmpBuf();
    	}
    	
        if (n.getScope() != null) {
            n.getScope().accept(this, new Boolean(true));
            printer.print(".");
        }
        printTypeArgs(n.getTypeArgs(), arg);
        printer.print(n.getName());
        printArguments(n.getArgs(), arg);
        
    	if(!collapse) {
	        appendLostCode(lost_code,n);
	        nm.setCode(printer.getTmpBufStr()+";");
	        setComment(n.getComment(), printer.getTmpBufStr()+";", nm);
	        printer.clearTmpBuf();
    	}
    }

    
    public void visit(ExpressionStmt n, Object arg) {
    	Boolean collapse = new Boolean(false); 
    	if(arg != null && arg.getClass().equals(collapse.getClass())) {
    		collapse = (Boolean) arg;
    	}
    	String lost_code = "";
    	
    	if(!collapse) {
   	    	lost_code = printer.getTmpBufStr();
   	    	printer.clearTmpBuf();
    	}
        n.getExpression().accept(this, arg);
        
        if(!collapse && printer.getTmpBufStr().length() > 0) {
        	NodeDG nm = null;
   	    	nm = new NodeDG();
   	        nm.setType(DrakonUtils.DI_ACTION);
    	        
   	        src_node.addMember(nm);

   	        printer.print(";");
   	        
	        appendLostCode(lost_code,n);
	        nm.setCode(printer.getTmpBufStr());
	        setComment(n.getComment(), printer.getTmpBufStr(), nm);
	        printer.clearTmpBuf();
    	}
        if(collapse) {
   	        printer.print(";");
        }

    }

    
    public void visit(FieldDeclaration n, Object arg) {
    	NodeDG nm = new NodeDG();
        nm.setType(DrakonUtils.DI_ACTION);
        //nm.setComment("fields",ConstantsDG.max_comment_node_length);
        setComment(n.getComment(), "fields", nm);
        nm.setMarker("");
        
        src_node.addMember(nm);
        
    	String lost_code = printer.getTmpBufStr();
    	printer.clearTmpBuf();
    	
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());
        n.getType().accept(this, new Boolean(true));

        printer.print(" ");
        for (Iterator<VariableDeclarator> i = n.getVariables().iterator(); i.hasNext();) {
            VariableDeclarator var = i.next();
            var.accept(this, new Boolean(true));
            if (i.hasNext()) {
                printer.print(", ");
            }
        }

        printer.print(";");
        
        appendLostCode(lost_code,n);

        nm.setCode(printer.getTmpBufStr());
        setComment(n.getComment(), printer.getTmpBufStr(), nm);
        printer.clearTmpBuf();
    }

    
    public void visit(SwitchStmt n, Object arg) {
    	WithMembersNodeDG rem_src_node = src_node;
    	SwitchNodeDG rem_src_switch_node = src_switch_node; 
    	
    	SwitchNodeDG swnm = new SwitchNodeDG();
        //swnm.setComment("switch",ConstantsDG.max_comment_node_length);
        setComment(n.getComment(), "switch", swnm);
        swnm.setType(DrakonUtils.DI_SW);
        
        src_switch_node = swnm;
        
        rem_src_node.addMember(swnm);

    	PointNodeDG beg = new PointNodeDG();
        beg.setComment("",ConstantsDG.max_comment_node_length);
        beg.setType(DrakonUtils.DI_EI);
        swnm.setBeg(beg);
        
    	String lost_code = printer.getTmpBufStr();
        appendLostCode(lost_code,n);
    	printer.clearTmpBuf();
    	
    	printer.print("switch(");
        n.getSelector().accept(this, new Boolean(true));
        printer.printLn(") {");

        swnm.setCode(printer.getTmpBufStr());
        setComment(n.getComment(), printer.getTmpBufStr(), swnm);
//        Comment c = n.getComment();
//        if(c != null) {
//        	swnm.setComment(c.toString(),ConstantsDG.max_comment_node_length);
//        } else {
//        	swnm.setComment(printer.getTmpBufStr(),ConstantsDG.max_comment_node_length);
//        }
        printer.clearTmpBuf();

        if (n.getEntries() != null) {
            printer.indent();
            for (SwitchEntryStmt e : n.getEntries()) {
                e.accept(this, arg);
            }
            printer.unindent();
        }
        printer.print("}");
        printer.clearTmpBuf();
        
    	PointNodeDG end = new PointNodeDG();
        end.setComment("",ConstantsDG.max_comment_node_length);
        end.setType(DrakonUtils.DI_EI);
        swnm.setEnd(end);
        
        rem_src_node.addMember(end);
    	src_node = rem_src_node;
    	src_switch_node = rem_src_switch_node; 
    }

    
    public void visit(SwitchEntryStmt n, Object arg) {
    	WithMembersNodeDG rem_src_node = src_node;
    	SwitchNodeDG rem_src_switch_node = src_switch_node; 
    	
    	CaseNodeDG casenm = new CaseNodeDG();
        
    	String lost_code = printer.getTmpBufStr();
        appendLostCode(lost_code,n);
    	printer.clearTmpBuf();

    	boolean flg = false;
    	int sz = rem_src_switch_node.getCase_list().size();
    	if(sz > 0) {
    		CaseNodeDG c = rem_src_switch_node.getCase_list().get(sz-1);
    		int sz2 = c.getMembers().size();
    		if(sz2 > 0) {
    			NodeDG n2 = c.getMembers().get(sz2-1);
    			if(!n2.getType().equals(DrakonUtils.DI_BREAK) 
    					&& !n2.getType().equals(DrakonUtils.DI_RETURN)) {
    				flg = true;
    				src_node = c;
    			}
    		} else if(sz2==0) {
    			flg = true;
				src_node = c;
    		}
    	}
    	if(!flg) {
    		rem_src_switch_node.addCase(casenm);
        	src_node = casenm;
    	} else {
    		src_node.addMember(casenm);
    	}
    	
    	if (n.getLabel() != null) {
            printer.print("case ");
            n.getLabel().accept(this, new Boolean(true));
            printer.print(":");
            setComment(n.getComment(),"case",casenm);
            casenm.setType(DrakonUtils.DI_CASE);
        } else {
            printer.print("default:");
            casenm.setType(DrakonUtils.DI_DEFAULT);
        }
        casenm.setCode(printer.getTmpBufStr());
        setComment(n.getComment(), printer.getTmpBufStr(), casenm);
//        Comment c = n.getComment();
//        if(c != null) {
//        	casenm.setComment(c.toString(),ConstantsDG.max_comment_node_length);
//        } else {
//        	casenm.setComment(printer.getTmpBufStr(),ConstantsDG.max_comment_node_length);
//        }
        printer.clearTmpBuf();
    	
        printer.printLn();
        printer.indent();
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
                printer.printLn();
            }
        }
        printer.unindent();
        
        src_node = rem_src_node;
        src_switch_node = rem_src_switch_node;
    }

    public void visit(BreakStmt n, Object arg) {
    	NodeDG nm = new NodeDG();
        nm.setType(DrakonUtils.DI_BREAK);
        src_node.addMember(nm);
        
    	String lost_code = printer.getTmpBufStr();
        printer.clearTmpBuf();

        printer.print("break");
        if (n.getId() != null) {
            printer.print(" ");
            printer.print(n.getId());
        }
        printer.print(";");
        
        appendLostCode(lost_code, n);
        nm.setCode(printer.getTmpBufStr());
        setComment(n.getComment(), printer.getTmpBufStr(), nm);
        printer.clearTmpBuf();
    }

    public void visit(ReturnStmt n, Object arg) {
    	NodeDG nm = new NodeDG();
        nm.setType(DrakonUtils.DI_RETURN);
        
        src_node.addMember(nm);
        
    	String lost_code = printer.getTmpBufStr();
        printer.clearTmpBuf();
    	
        printer.print("return");
        if (n.getExpr() != null) {
            printer.print(" ");
            n.getExpr().accept(this, new Boolean(true));
        }
        printer.print(";");
        
        appendLostCode(lost_code,n);
        String code = printer.getTmpBufStr(); 
        nm.setCode(code);
        printer.clearTmpBuf();
        Comment c = n.getComment();
        if(c != null) {
        	String s = c.toString();
        	s = takeAspect(s);
        	nm.setComment(s,ConstantsDG.max_comment_node_length);
        	nm.setAspect(current_aspect);
        } else {
        	code = code.trim();
        	if(code.startsWith("return "))
        		code = code.substring("return ".length(), code.length());
        	nm.setComment(code,ConstantsDG.max_comment_node_length);
        }
    }

    public void visit(IfStmt n, Object arg) {
    	WithMembersNodeDG rem_src_node = src_node;
    	
    	IfNodeDG ifnm = new IfNodeDG();
        ifnm.setComment("If",ConstantsDG.max_comment_node_length);
        ifnm.setType(DrakonUtils.DI_IF);
        
        rem_src_node.addMember(ifnm);
        
    	String lost_code = printer.getTmpBufStr();
        appendLostCode(lost_code,n);
    	
        printer.print("if (");
    	printer.clearTmpBuf();
    	
        n.getCondition().accept(this, new Boolean(true));

        ifnm.setCode(printer.getTmpBufStr());
        setComment(n.getComment(), printer.getTmpBufStr(), ifnm);
//        Comment c = n.getComment();
//        if(c != null) {
//        	ifnm.setComment(c.toString(),ConstantsDG.max_comment_node_length);
//        } else {
//        	ifnm.setComment(printer.getTmpBufStr(),ConstantsDG.max_comment_node_length);
//        }
        
        printer.print(") "); 
        
        printer.clearTmpBuf();

    	src_node = ifnm.getThen_list();
    	
    	//-- then
    	printer.clearTmpBuf();
    	n.getThenStmt().accept(this, arg);
        
        if (n.getElseStmt() != null) {
        	src_node = ifnm.getElse_list();
        	
        	printer.clearTmpBuf();
            printer.print(" else ");
            
        	printer.clearTmpBuf();
            
            n.getElseStmt().accept(this, arg);
        }

    	PointNodeDG eifnm = new PointNodeDG();
        eifnm.setComment("",ConstantsDG.max_comment_node_length);
        eifnm.setType(DrakonUtils.DI_EI);
        eifnm.setMarker("");
        ifnm.setEnd(eifnm);
        
        rem_src_node.addMember(eifnm);
        
    	src_node = rem_src_node;
    }

    
    public void visit(WhileStmt n, Object arg) {
    	NodeDG nm = new NodeDG();
        nm.setType(DrakonUtils.DI_FOR_BEG);
        src_node.addMember(nm);
        
    	String lost_code = printer.getTmpBufStr();
        printer.clearTmpBuf();
        
        printer.print("while (");
        n.getCondition().accept(this, new Boolean(true));
        printer.print(") ");
        
        appendLostCode(lost_code,n);
        nm.setCode(label + printer.getTmpBufStr() + " {");
        setComment(n.getComment(),printer.getTmpBufStr() + " {",nm);
        printer.clearTmpBuf();
        
        n.getBody().accept(this, arg);
        
    	NodeDG fend = new NodeDG();
    	fend.setAspect(current_aspect);
        fend.setType(DrakonUtils.DI_FOR_END);
        src_node.addMember(fend);
    }

    public void visit(ContinueStmt n, Object arg) {
    	NodeDG nm = new NodeDG();
        nm.setType(DrakonUtils.DI_ACTION);
        src_node.addMember(nm);
        
    	String lost_code = printer.getTmpBufStr();
        printer.clearTmpBuf();
    	
        printer.print("continue");
        if (n.getId() != null) {
            printer.print(" ");
            printer.print(n.getId());
        }
        printer.print(";");
        
        nm.setCode(printer.getTmpBufStr());
        setComment(n.getComment(), printer.getTmpBufStr(), nm);
        printer.clearTmpBuf();
    }

    public void visit(DoStmt n, Object arg) {
    	NodeDG nm = new NodeDG();
        nm.setType(DrakonUtils.DI_FOR_BEG);
        src_node.addMember(nm);
        
    	String lost_code = printer.getTmpBufStr();
        printer.clearTmpBuf();
    	
        printer.print("do ");
        
        appendLostCode(lost_code,n);
        nm.setCode(label +  printer.getTmpBufStr() + " {");
        setComment(n.getComment(), printer.getTmpBufStr()+ " {", nm);
        printer.clearTmpBuf();

        n.getBody().accept(this, arg);
        
        printer.clearTmpBuf();
        
        printer.print(" while (");
        n.getCondition().accept(this, new Boolean(true));
        printer.print(");");
        
    	NodeDG fend = new NodeDG();
    	fend.setAspect(current_aspect);
        fend.setType(DrakonUtils.DI_FOR_END);
        fend.setCode(" } " + printer.getTmpBufStr());
        src_node.addMember(fend);
    }

    public void visit(ForeachStmt n, Object arg) {
    	NodeDG nm = new NodeDG();
        nm.setType(DrakonUtils.DI_FOR_BEG);
        src_node.addMember(nm);
        
    	String lost_code = printer.getTmpBufStr();
        printer.clearTmpBuf();
    	
        printer.print("for (");
        n.getVariable().accept(this, new Boolean(true));
        printer.print(" : ");
        n.getIterable().accept(this, new Boolean(true));
        printer.print(") ");

        appendLostCode(lost_code,n);
        nm.setCode(label + printer.getTmpBufStr() + " {");
        setComment(n.getComment(), printer.getTmpBufStr()+ " {", nm);
//        Comment c = n.getComment();
//        if(c != null) {
//        	nm.setComment(c.toString(),ConstantsDG.max_comment_node_length);
//        } else {
//        	nm.setComment(printer.getTmpBufStr() + " {",ConstantsDG.max_comment_node_length);
//        }
        printer.clearTmpBuf();
        
        n.getBody().accept(this, arg);
        
    	NodeDG fend = new NodeDG();
    	fend.setAspect(current_aspect);
        fend.setType(DrakonUtils.DI_FOR_END);
        src_node.addMember(fend);
    }

    public void visit(ForStmt n, Object arg) {
    	NodeDG nm = new NodeDG();
        nm.setType(DrakonUtils.DI_FOR_BEG);
        src_node.addMember(nm);
        
    	String lost_code = printer.getTmpBufStr();
    	//System.err.println(" lost_code "+lost_code);
    	appendLostCode(lost_code,n);
    	
        printer.clearTmpBuf();

        printer.print("for (");
        
        if (n.getInit() != null) {
            for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print("; ");
        if (n.getCompare() != null) {
            n.getCompare().accept(this, new Boolean(true));
        }
        printer.print("; ");
        if (n.getUpdate() != null) {
            for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(") ");
        
        appendLostCode(lost_code,n);
        nm.setCode(label + printer.getTmpBufStr() + " {");
        setComment(n.getComment(), printer.getTmpBufStr()+ " {", nm);
//        Comment c = n.getComment();
//        if(c != null) {
//        	nm.setComment(c.toString(),ConstantsDG.max_comment_node_length);
//        } else {
//        	nm.setComment(printer.getTmpBufStr() + " {",ConstantsDG.max_comment_node_length);
//        }
        printer.clearTmpBuf();
        
        n.getBody().accept(this, arg);
        
    	NodeDG fend = new NodeDG();
    	fend.setAspect(current_aspect);
        fend.setType(DrakonUtils.DI_FOR_END);
        src_node.addMember(fend);
    }

    public void visit(ThrowStmt n, Object arg) {
        printer.print("throw ");
        n.getExpr().accept(this, new Boolean(true));
        printer.print(";");
    }

    public void visit(SynchronizedStmt n, Object arg) {
        printer.print("synchronized (");
        n.getExpr().accept(this, new Boolean(true));
        printer.print(") ");
        n.getBlock().accept(this, arg);
    }

    public void visit(TryStmt n, Object arg) {
        printer.print("try ");
        
    	NodeDG nm = new NodeDG();
    	nm.setType("DI_TRY");
    	src_node.addMember(nm);
    	nm.setCode("try {\n");
    	setComment(n.getComment(), "try {", nm);
    	printer.clearTmpBuf();
        
        n.getTryBlock().accept(this, new Boolean(true));
        if (n.getCatchs() != null) {
            for (CatchClause c : n.getCatchs()) {
                c.accept(this, new Boolean(true));
            }
        }
        if (n.getFinallyBlock() != null) {
            printer.print(" finally ");
            n.getFinallyBlock().accept(this, new Boolean(true));
        }
        
    	NodeDG cnm = new NodeDG();
    	cnm.setType("DI_CATCH");
    	cnm.setAspect(current_aspect);
    	src_node.addMember(cnm);
    	cnm.setCode(printer.getTmpBufStr());
    	cnm.setComment("catch",ConstantsDG.max_comment_node_length);
    	
    	printer.clearTmpBuf();
    }

    public void visit(CatchClause n, Object arg) {
        printer.print(" catch (");
        n.getExcept().accept(this, new Boolean(true));
        printer.print(") ");
        n.getCatchBlock().accept(this, new Boolean(true));

    }

    public void visit(AnnotationDeclaration n, Object arg) {
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        printer.print("@interface ");
        printer.print(n.getName());
        printer.printLn(" {");
        printer.indent();
        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
        printer.unindent();
        printer.print("}");
    }

    public void visit(AnnotationMemberDeclaration n, Object arg) {
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        n.getType().accept(this, new Boolean(true));
        printer.print(" ");
        printer.print(n.getName());
        printer.print("()");
        if (n.getDefaultValue() != null) {
            printer.print(" default ");
            n.getDefaultValue().accept(this, new Boolean(true));
        }
        printer.print(";");
    }

    public void visit(MarkerAnnotationExpr n, Object arg) {
        printer.print("@");
        n.getName().accept(this, new Boolean(true));
    }

    public void visit(SingleMemberAnnotationExpr n, Object arg) {
        printer.print("@");
        n.getName().accept(this, new Boolean(true));
        printer.print("(");
        n.getMemberValue().accept(this, new Boolean(true));
        printer.print(")");
    }

    public void visit(NormalAnnotationExpr n, Object arg) {
        printer.print("@");
        n.getName().accept(this, new Boolean(true));
        printer.print("(");
        if (n.getPairs() != null) {
            for (Iterator<MemberValuePair> i = n.getPairs().iterator(); i.hasNext();) {
                MemberValuePair m = i.next();
                m.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");
    }

    public void visit(MemberValuePair n, Object arg) {
        printer.print(n.getName());
        printer.print(" = ");
        n.getValue().accept(this, new Boolean(true));
    }

    
    public void visit(LineComment n, Object arg) {
        printer.print("//+++++++++++++++++++");
        printer.printLn(n.getContent());
        /*
        String str = n.getContent();
        if(str.startsWith("<DG2J ")) {
        	int i = str.indexOf("aspect_begin="); 
        	if(i != -1) {
	        	//-- выделяем аспект
	        	int i2 = str.indexOf("\"",i+"aspect_begin=".length()+1);
	        	if(i2 == -1) {
	        		System.err.println("Error in '<DG2J aspect_begin=' str:"+str);
	        		return;
	        	}
	        	String aspect = str.substring(i+"aspect_begin=".length(),i2);
	        	
	        	AspectView a = cur_aspects.get(aspect);
	        	if(a == null) {
	        		System.err.println("Not found aspect "+ aspect +" in cur_aspects.");
	        		current_aspect = default_aspect;
	        		return;
	        	}
	            current_aspect = a;
	        	return;
        	}
        	i = str.indexOf("aspect_end="); 
        	if(i != -1) {
	        	//-- выделяем аспект
	        	int i2 = str.indexOf("\"",i+"aspect_end=".length()+1);
	        	if(i2 == -1) {
	        		System.err.println("Error in '<DG2J aspect_end=' str:"+str);
	        		return;
	        	}
	        	//String aspect = str.substring(i+"aspect_begin=".length(),i2);
	        	current_aspect = default_aspect;
	        	return;
        	}
        	
        }*/
        
    }
    
    public void visit(BlockComment n, Object arg) {
        printer.print("/*");
        printer.print(n.getContent());
        printer.printLn("*/");
    }

    private void printModifiers(int modifiers) {
        if (ModifierSet.isPrivate(modifiers)) {
            printer.print("private ");
        }
        if (ModifierSet.isProtected(modifiers)) {
            printer.print("protected ");
        }
        if (ModifierSet.isPublic(modifiers)) {
            printer.print("public ");
        }
        if (ModifierSet.isAbstract(modifiers)) {
            printer.print("abstract ");
        }
        if (ModifierSet.isStatic(modifiers)) {
            printer.print("static ");
        }
        if (ModifierSet.isFinal(modifiers)) {
            printer.print("final ");
        }
        if (ModifierSet.isNative(modifiers)) {
            printer.print("native ");
        }
        if (ModifierSet.isStrictfp(modifiers)) {
            printer.print("strictfp ");
        }
        if (ModifierSet.isSynchronized(modifiers)) {
            printer.print("synchronized ");
        }
        if (ModifierSet.isTransient(modifiers)) {
            printer.print("transient ");
        }
        if (ModifierSet.isVolatile(modifiers)) {
            printer.print("volatile ");
        }
    }

    private void printMembers(List<BodyDeclaration> members, Object arg) {
        for (BodyDeclaration member : members) {
            printer.printLn();
            member.accept(this, arg);
            printer.printLn();
        }
    }

    private void printMemberAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, new Boolean(true));
                printer.printLn();
            }
        }
    }

    private void printAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, new Boolean(true));
                printer.print(" ");
            }
        }
    }

    private void printTypeArgs(List<Type> args, Object arg) {
        if (args != null) {
            printer.print("<");
            for (Iterator<Type> i = args.iterator(); i.hasNext();) {
                Type t = i.next();
                t.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    private void printTypeParameters(List<TypeParameter> args, Object arg) {
        if (args != null) {
            printer.print("<");
            for (Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
                TypeParameter t = i.next();
                t.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    private void printArguments(List<Expression> args, Object arg) {
        printer.print("(");
        if (args != null) {
            for (Iterator<Expression> i = args.iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");
    }

    private void printJavadoc(JavadocComment javadoc, Object arg) {
        if (javadoc != null) {
            javadoc.accept(this, new Boolean(true));
        }
    }

    public void visit(PackageDeclaration n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
        
        printer.print("package ");
        n.getName().accept(this, new Boolean(true));
        printer.printLn(";");
    }

    public void visit(NameExpr n, Object arg) {
        printer.print(n.getName());
    }

    public void visit(QualifiedNameExpr n, Object arg) {
        n.getQualifier().accept(this, arg);
        printer.print(".");
        printer.print(n.getName());
    }

    public void visit(ImportDeclaration n, Object arg) {
        printer.print("import ");
        if (n.isStatic()) {
            printer.print("static ");
        }
        n.getName().accept(this, new Boolean(true));
        if (n.isAsterisk()) {
            printer.print(".*");
        }
        printer.printLn(";");
    }

    public void visit(EmptyTypeDeclaration n, Object arg) {
        printJavadoc(n.getJavaDoc(), arg);
        printer.print(";");
    }

    public void visit(JavadocComment n, Object arg) {
        printer.print("/**");
        printer.print(n.getContent());
        printer.printLn("*/");
    }

    public void visit(ClassOrInterfaceType n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, new Boolean(true));
            printer.print(".");
        }
        printer.print(n.getName());
        printTypeArgs(n.getTypeArgs(), arg);
    }

    public void visit(TypeParameter n, Object arg) {
        printer.print(n.getName());
        if (n.getTypeBound() != null) {
            printer.print(" extends ");
            for (Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(" & ");
                }
            }
        }
    }

    public void visit(PrimitiveType n, Object arg) {
        switch (n.getType()) {
            case Boolean:
            	System.err.println("");
                printer.print("boolean");
                break;
            case Byte:
                printer.print("byte");
                break;
            case Char:
                printer.print("char");
                break;
            case Double:
                printer.print("double");
                break;
            case Float:
                printer.print("float");
                break;
            case Int:
                printer.print("int");
                break;
            case Long:
                printer.print("long");
                break;
            case Short:
                printer.print("short");
                break;
        }
    }

    public void visit(ReferenceType n, Object arg) {
        n.getType().accept(this, new Boolean(true));
        for (int i = 0; i < n.getArrayCount(); i++) {
            printer.print("[]");
        }
    }

    public void visit(WildcardType n, Object arg) {
        printer.print("?");
        if (n.getExtends() != null) {
            printer.print(" extends ");
            n.getExtends().accept(this, new Boolean(true));
        }
        if (n.getSuper() != null) {
            printer.print(" super ");
            n.getSuper().accept(this, new Boolean(true));
        }
    }

    public void visit(VariableDeclarator n, Object arg) {
        n.getId().accept(this, new Boolean(true));
        if (n.getInit() != null) {
            printer.print(" = ");
            n.getInit().accept(this, new Boolean(true));
        }
    }

    public void visit(VariableDeclaratorId n, Object arg) {
        printer.print(n.getName());
        for (int i = 0; i < n.getArrayCount(); i++) {
            printer.print("[]");
        }
    }

    public void visit(ArrayInitializerExpr n, Object arg) {
        printer.print("{");
        if (n.getValues() != null) {
            printer.print(" ");
            for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
                Expression expr = i.next();
                expr.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(" ");
        }
        printer.print("}");
    }

    public void visit(VoidType n, Object arg) {
        printer.print("void");
    }

    public void visit(ArrayAccessExpr n, Object arg) {
        n.getName().accept(this, new Boolean(true));
        printer.print("[");
        n.getIndex().accept(this, new Boolean(true));
        printer.print("]");
    }

    public void visit(ArrayCreationExpr n, Object arg) {
        printer.print("new ");
        n.getType().accept(this, new Boolean(true));

        if (n.getDimensions() != null) {
            for (Expression dim : n.getDimensions()) {
                printer.print("[");
                dim.accept(this, new Boolean(true));
                printer.print("]");
            }
            for (int i = 0; i < n.getArrayCount(); i++) {
                printer.print("[]");
            }
        } else {
            for (int i = 0; i < n.getArrayCount(); i++) {
                printer.print("[]");
            }
            printer.print(" ");
            n.getInitializer().accept(this, new Boolean(true));
        }
    }

    public void visit(AssignExpr n, Object arg) {
        n.getTarget().accept(this, new Boolean(true));
        printer.print(" ");
        switch (n.getOperator()) {
            case assign:
                printer.print("=");
                break;
            case and:
                printer.print("&=");
                break;
            case or:
                printer.print("|=");
                break;
            case xor:
                printer.print("^=");
                break;
            case plus:
                printer.print("+=");
                break;
            case minus:
                printer.print("-=");
                break;
            case rem:
                printer.print("%=");
                break;
            case slash:
                printer.print("/=");
                break;
            case star:
                printer.print("*=");
                break;
            case lShift:
                printer.print("<<=");
                break;
            case rSignedShift:
                printer.print(">>=");
                break;
            case rUnsignedShift:
                printer.print(">>>=");
                break;
        }
        printer.print(" ");
        n.getValue().accept(this, new Boolean(true));
    }

    public void visit(BinaryExpr n, Object arg) {
        n.getLeft().accept(this, new Boolean(true));
        printer.print(" ");
        switch (n.getOperator()) {
            case or:
                printer.print("||");
                break;
            case and:
                printer.print("&&");
                break;
            case binOr:
                printer.print("|");
                break;
            case binAnd:
                printer.print("&");
                break;
            case xor:
                printer.print("^");
                break;
            case equals:
                printer.print("==");
                break;
            case notEquals:
                printer.print("!=");
                break;
            case less:
                printer.print("<");
                break;
            case greater:
                printer.print(">");
                break;
            case lessEquals:
                printer.print("<=");
                break;
            case greaterEquals:
                printer.print(">=");
                break;
            case lShift:
                printer.print("<<");
                break;
            case rSignedShift:
                printer.print(">>");
                break;
            case rUnsignedShift:
                printer.print(">>>");
                break;
            case plus:
                printer.print("+");
                break;
            case minus:
                printer.print("-");
                break;
            case times:
                printer.print("*");
                break;
            case divide:
                printer.print("/");
                break;
            case remainder:
                printer.print("%");
                break;
        }
        printer.print(" ");
        n.getRight().accept(this, new Boolean(true));
    }

    public void visit(CastExpr n, Object arg) {
        printer.print("(");
        n.getType().accept(this, new Boolean(true));
        printer.print(") ");
        n.getExpr().accept(this, new Boolean(true));
    }

    public void visit(ClassExpr n, Object arg) {
        n.getType().accept(this, new Boolean(true));
        printer.print(".class");
    }

    public void visit(ConditionalExpr n, Object arg) {
        n.getCondition().accept(this, new Boolean(true));
        printer.print(" ? ");
        n.getThenExpr().accept(this, new Boolean(true));
        printer.print(" : ");
        n.getElseExpr().accept(this, new Boolean(true));
    }

    public void visit(EnclosedExpr n, Object arg) {
        printer.print("(");
        n.getInner().accept(this, new Boolean(true));
        printer.print(")");
    }

    public void visit(FieldAccessExpr n, Object arg) {
        n.getScope().accept(this, new Boolean(true));
        printer.print(".");
        printer.print(n.getField());
    }

    public void visit(InstanceOfExpr n, Object arg) {
        n.getExpr().accept(this, new Boolean(true));
        printer.print(" instanceof ");
        n.getType().accept(this, new Boolean(true));
    }

    public void visit(CharLiteralExpr n, Object arg) {
        printer.print("'");
        printer.print(n.getValue());
        printer.print("'");
    }

    public void visit(DoubleLiteralExpr n, Object arg) {
        printer.print(n.getValue());
    }

    public void visit(IntegerLiteralExpr n, Object arg) {
        printer.print(n.getValue());
    }

    public void visit(LongLiteralExpr n, Object arg) {
        printer.print(n.getValue());
    }

    public void visit(IntegerLiteralMinValueExpr n, Object arg) {
        printer.print(n.getValue());
    }

    public void visit(LongLiteralMinValueExpr n, Object arg) {
        printer.print(n.getValue());
    }

    public void visit(StringLiteralExpr n, Object arg) {
        printer.print("\"");
        printer.print(n.getValue());
        printer.print("\"");
    }

    public void visit(BooleanLiteralExpr n, Object arg) {
        printer.print(String.valueOf(n.getValue()));
    }

    public void visit(NullLiteralExpr n, Object arg) {
        printer.print("null");
    }

    public void visit(ThisExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, new Boolean(true));
            printer.print(".");
        }
        printer.print("this");
    }

    public void visit(SuperExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, new Boolean(true));
            printer.print(".");
        }
        printer.print("super");
    }

    public void visit(ObjectCreationExpr n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, new Boolean(true));
            printer.print(".");
        }

        printer.print("new ");

        printTypeArgs(n.getTypeArgs(), arg);
        n.getType().accept(this, new Boolean(true));

        printArguments(n.getArgs(), arg);

        if (n.getAnonymousClassBody() != null) {
            printer.printLn(" {");
            printer.indent();
            printMembers(n.getAnonymousClassBody(), arg);
            printer.unindent();
            printer.print("}");
        }
    }

    public void visit(UnaryExpr n, Object arg) {
        switch (n.getOperator()) {
            case positive:
                printer.print("+");
                break;
            case negative:
                printer.print("-");
                break;
            case inverse:
                printer.print("~");
                break;
            case not:
                printer.print("!");
                break;
            case preIncrement:
                printer.print("++");
                break;
            case preDecrement:
                printer.print("--");
                break;
        }

        n.getExpr().accept(this, new Boolean(true));

        switch (n.getOperator()) {
            case posIncrement:
                printer.print("++");
                break;
            case posDecrement:
                printer.print("--");
                break;
        }
    }


    public void visit(Parameter n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        n.getType().accept(this, new Boolean(true));
        if (n.isVarArgs()) {
            printer.print("...");
        }
        printer.print(" ");
        n.getId().accept(this, new Boolean(true));
    }

    public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
    	Boolean collapse = new Boolean(false); 
    	if(arg != null && arg.getClass().equals(collapse.getClass())) {
    		collapse = (Boolean) arg;
    	}
    	NodeDG nm = null;
    	String lost_code = ""; 
    	if(!collapse) {
	    	nm = new NodeDG();
	        nm.setType(NodeDG.DI_CALL_PROC);
	        
	        src_node.addMember(nm);
	        
	    	lost_code = printer.getTmpBufStr();
	    	printer.clearTmpBuf();
    	}

    	if (n.isThis()) {
            printTypeArgs(n.getTypeArgs(), new Boolean(true));
            printer.print("this");
        } else {
            if (n.getExpr() != null) {
                n.getExpr().accept(this, new Boolean(true));
                printer.print(".");
            }
            printTypeArgs(n.getTypeArgs(), new Boolean(true));
            printer.print("super");
        }
        printArguments(n.getArgs(), new Boolean(true));
        printer.print(";");
    	if(!collapse) {
	        appendLostCode(lost_code,n);
	        nm.setCode(printer.getTmpBufStr());
	        setComment(n.getComment(), printer.getTmpBufStr()+";", nm);
	        printer.clearTmpBuf();
    	}
    }

    public void visit(VariableDeclarationExpr n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        n.getType().accept(this, new Boolean(true));
        printer.print(" ");

        for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext();) {
            VariableDeclarator v = i.next();
            v.accept(this, new Boolean(true));
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
    }

    public void visit(TypeDeclarationStmt n, Object arg) {
        n.getTypeDeclaration().accept(this, arg);
    }

    public void visit(AssertStmt n, Object arg) {
        printer.print("assert ");
        n.getCheck().accept(this, new Boolean(true));
        if (n.getMessage() != null) {
            printer.print(" : ");
            n.getMessage().accept(this, new Boolean(true));
        }
        printer.print(";");
    }

    public void visit(BlockStmt n, Object arg) {
    	Boolean collapse = new Boolean(false); 
    	if(arg != null && arg.getClass().equals(collapse.getClass())) {
    		collapse = (Boolean) arg;
    	}
        printer.printLn("{",collapse); 
        //printer.clearTmpBuf();
        if (n.getStmts() != null) {
            printer.indent();
            for (Statement s : n.getStmts()) {
            	//printer.clearTmpBuf();
                s.accept(this, arg);
                if(!collapse) {
	                String str = printer.getTmpBufStr();
	                if(str!= null) {
	                	str = str.trim();
	                	if(!str.equals("")) {
	                    	NodeDG nm = new NodeDG();
	                    	nm.setAspect(problem_aspect);
	                        nm.setType(DrakonUtils.DI_ACTION);
	                        nm.setCode(str);
	                        nm.setComment("LC: "+ str,ConstantsDG.max_comment_node_length);
	                        src_node.addMember(nm);
	                	}
	                }
                }
                printer.printLn();
            }
            printer.unindent();
        }
        printer.print("}",collapse);
        
        //printer.clearTmpBuf();
    }

    public void visit(EnumDeclaration n, Object arg) {
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        printer.print("enum ");
        printer.print(n.getName());

        if (n.getImplements() != null) {
            printer.print(" implements ");
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        printer.printLn(" {");
        printer.indent();
        if (n.getEntries() != null) {
            printer.printLn();
            for (Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
                EnumConstantDeclaration e = i.next();
                e.accept(this, new Boolean(true));
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        if (n.getMembers() != null) {
            printer.printLn(";");
            printMembers(n.getMembers(), arg);
        } else {
            if (n.getEntries() != null) {
                printer.printLn();
            }
        }
        printer.unindent();
        printer.print("}");
    }

    public void visit(EnumConstantDeclaration n, Object arg) {
        printJavadoc(n.getJavaDoc(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printer.print(n.getName());

        if (n.getArgs() != null) {
            printArguments(n.getArgs(), arg);
        }

        if (n.getClassBody() != null) {
            printer.printLn(" {");
            printer.indent();
            printMembers(n.getClassBody(), arg);
            printer.unindent();
            printer.printLn("}");
        }
    }

    public void visit(EmptyMemberDeclaration n, Object arg) {
        printJavadoc(n.getJavaDoc(), arg);
        printer.print(";");
    }

    public void visit(InitializerDeclaration n, Object arg) {
        printJavadoc(n.getJavaDoc(), arg);
        if (n.isStatic()) {
            printer.print("static ");
        }
        n.getBlock().accept(this, new Boolean(true));
    }

    
}

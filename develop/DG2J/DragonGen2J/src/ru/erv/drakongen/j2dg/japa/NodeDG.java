package ru.erv.drakongen.j2dg.japa;

import ru.erv.drakongen.DrakonUtils;

class ActionFigure {
	static String getFigureStr(int x, int y, int fig_h, int fig_w, int fig_line_w
			, String fig_name, String comment, String color1, String color2, String border_type) {
		x -= fig_w / 2;
		String str = ""
				+ "<data key=\"d7\"\n>"
				+ "<y:GenericNode configuration=\"" + fig_name +"\">\n" +
		 " <y:Geometry height=\"" + fig_h + "\" width=\"" + fig_w + "\" x=\""+ x + "\" y=\""+y+"\"/>\n" +
		 " <y:Fill color=\""+color1+"\" color2=\""+color2+"\" transparent=\"false\"/>\n" +
		 " <y:BorderStyle color=\"#000000\" type=\""+border_type+"\" width=\""+fig_line_w+"\"/>\n" +
		 " <y:NodeLabel alignment=\"center\" autoSizePolicy=\"node_width\" "
		 + "configuration=\"CroppingLabel\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" "
		 + "hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"21.09375\" "
		 + "modelName=\"internal\" modelPosition=\"c\" textColor=\"#000000\" "
		 + "visible=\"true\" width=\"180.0\" x=\"0.0\" y=\"9.453125\">"+ comment 
		 + "</y:NodeLabel>\n" 
		 + "</y:GenericNode>\n"
		 + "</data>\n";
		return str;
	}
}


class AnnotationFigure {
	static String getFigureStr(int x, int y, int fig_h, int fig_w, int fig_line_w
			, String fig_name, String comment, String color1, String color2, String border_type) {
		x -= fig_w / 2;
		String str = ""
				+ "<data key=\"d7\"\n>"
				+ "<y:GenericNode configuration=\"" + fig_name +"\">\n" +
		 " <y:Geometry height=\"" + fig_h + "\" width=\"" + fig_w + "\" x=\""+ x + "\" y=\""+y+"\"/>\n" +
		 " <y:Fill color=\""+color1+"\" color2=\""+color2+"\" transparent=\"false\"/>\n" +
		 " <y:BorderStyle color=\"#000000\" type=\""+border_type+"\" width=\""+fig_line_w+"\"/>\n" +
		 " <y:NodeLabel alignment=\"left\" autoSizePolicy=\"node_size\" "
		 + "configuration=\"CroppingLabel\" fontFamily=\"Dialog\" fontSize=\"8\" fontStyle=\"plain\" "
		 + "hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"21.09375\" "
		 + "modelName=\"internal\" modelPosition=\"t\" textColor=\"#000000\" "
		 + "visible=\"true\" width=\"180.0\" x=\"0.0\" y=\"9.453125\">"+ comment 
		 + "</y:NodeLabel>\n" 
		 + "<y:StyleProperties>\n"
		 + "<y:Property class=\"java.lang.Byte\" name=\"com.yworks.flowchart.style.orientation\" value=\"4\"/>\n"
		 + "</y:StyleProperties>\n"
		 + "</y:GenericNode>\n"
		 + "</data>\n";
		return str;
	}
}



class CompilEndFigure {
	static String getFigureStr(int x, int y, int fig_h, int fig_w, int fig_line_w
			, String color1, String color2) {
		x -= fig_w / 2;
		String str = "" 
+ "<data key=\"d7\">\n"
+ "<y:ShapeNode>\n"
+ "  <y:Geometry height=\""+fig_h+"\" width=\""+fig_w+"\" x=\""+x+"\" y=\""+y+"\"/>\n"
+ "  <y:Fill color=\""+color1+"\" color2=\""+color2+"\" transparent=\"false\"/>\n"
+ "  <y:BorderStyle color=\"#000000\" type=\"line\" width=\""+fig_line_w+"\"/>\n"
+ "  <y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" hasText=\"false\" height=\"4.0\" modelName=\"custom\" "
+ " textColor=\"#000000\" visible=\"true\" width=\"4.0\" x=\"23.0\" y=\"18.0\">\n"
+ "	<y:LabelModel>\n"
+ "	  <y:SmartNodeLabelModel distance=\"4.0\"/>\n"
+ "	</y:LabelModel>\n"
+ "	<y:ModelParameter>\n"
+ "	  <y:SmartNodeLabelModelParameter labelRatioX=\"0.0\" labelRatioY=\"0.0\" nodeRatioX=\"0.0\" nodeRatioY=\"0.0\" offsetX=\"0.0\" offsetY=\"0.0\" upX=\"0.0\" upY=\"-1.0\"/>\n"
+ "	</y:ModelParameter>\n"
+ "  </y:NodeLabel>\n"
+ "  <y:Shape type=\"triangle\"/>\n"
+ "</y:ShapeNode>\n"
+ "</data>\n";
		return str;
	}
}


class ReturnFigure {
	static String getFigureStr(int x, int y, int fig_h, int fig_w, int fig_line_w
			, String comment, String color1, String color2, String border_type) {
		x -= fig_w / 2;
		String str = "" 
+ "<data key=\"d7\">\n"
+ "<y:GenericNode configuration=\"com.yworks.flowchart.terminator\">\n"
+ "<y:Geometry height=\""+fig_h+"\" width=\""+fig_w+"\" x=\""+x+"\" y=\""+y+"\"/>\n"
+ "  <y:Fill color=\""+color1+"\" color2=\""+color1+"\" transparent=\"false\"/>\n"
+ "  <y:BorderStyle color=\"#000000\" type=\""+border_type+"\" width=\""+fig_line_w+"\"/>\n"
+ "  <y:NodeLabel alignment=\"center\" autoSizePolicy=\"node_size\" configuration=\"CroppingLabel\" "
+ " fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" "
+ " hasLineColor=\"false\" height=\"21.09375\" modelName=\"internal\" modelPosition=\"c\" "
+ " textColor=\"#000000\" visible=\"true\" width=\"85.0\" x=\"0.0\" y=\"4.453125\">"
+comment+"</y:NodeLabel>\n"
+ "</y:GenericNode>\n"
+ "</data>\n";
	return str;
}
}


public class NodeDG {

	public static final String DI_TRY = "DI_TRY";	
	public static final String DI_CATCH = "DI_CATCH";	
	public static final String DI_PROC_DOC = "DI_PROC_DOC";	
	public static final String DI_CALL_PROC = "DI_CALL_PROC";	
	
	static int id = 0;
	
	String marker = "";
	String code = "";
	String comment = "";
	String type = null;
	
	String nodeId;
	
	int x=0;
	int y=0;
	int max_x = 0;
	int max_y = 0;
	
	int fig_line_w = 1;
	
	int width = 180;
	int height = 40;
			
	AspectView aspect = new AspectView(); //-- default aspect view 
	
	public AspectView getAspect() {
		return aspect;
	}

	public void setAspect(AspectView aspect) {
		this.aspect = aspect;
	}

	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}

	EdgeDG from_src_edge;
	EdgeDG doc_src_edge;
	
	public EdgeDG makeEdge(NodeDG src) {
		from_src_edge = new EdgeDG(src, this);
		return from_src_edge;
	}

	public EdgeDG makeDocEdge(NodeDG src) {
		doc_src_edge = new EdgeDG(src, this);
		return doc_src_edge;
	}

	
	public NodeDG() {
		nodeId = "n" + this.id++;
	}
	
	protected int getMaxX() {
		return max_x;
	}
	
	protected int getMaxY() {
		return max_y;
	}
	
	protected void calcXY() {
		if(max_x < x)
			max_x = x;
		
		if(max_y < y)
			max_y = y;
	}
	
	public String print() {
		if(getType().equalsIgnoreCase("VIRTUAL")) {
			return "";
		}
		String str = "";
    	str += printNode();
    	str += printMarker();
    	str += printComment();
    	str += printType();
    	str += printCode();
    	str += printNodeEnd();
    	return str += printEdge();
	}
	
	public String printEdge() {
		String sf = "";
		String sd = "";
		if(from_src_edge!=null)
			sf = from_src_edge.printEdge();
		if(doc_src_edge!=null)
			sd = doc_src_edge.printEdge();
		
		return sf+sd;
	}
	
	public String printNode() {
		return "   <node id=\""+ nodeId + "\">";
	}
	public String printNodeEnd() {
		return "   </node>";
	}
	
	public String printMarker() {
		if(marker != null && marker.length()>0) {
			return "      <data key=\"d4\"><![CDATA["+marker+"]]></data>";
		} else {
			return "      <data key=\"d4\"/>"; 
		}
	}
	
	
	public String printCode() {
		return "      <data key=\"d6\"><![CDATA["+code+"]]></data>";
	}
	

	public String printType() {
		return "      <data key=\"d5\"><![CDATA["+type+"]]></data>";
	}
	
	
	public String printComment() {
		String fig_name = "com.yworks.flowchart.process";
		int fig_w = 180;
		int fig_h = 40;
		int fig_line_w = 1; //aspect.line_width;
		
		String border_type = "line";
		//if(type == null) {
		//	type = "A";
		//}
		switch (type) {
		case DrakonUtils.DI_PROC_BEG:
				fig_name = "com.yworks.flowchart.terminator";
				fig_line_w = 2;
			break;
		case DrakonUtils.DI_PROC_END:
			fig_name = "com.yworks.flowchart.onPageReference";
			fig_w = 85;
			fig_h = 30;
			fig_line_w = 2;
			return ReturnFigure.getFigureStr(x, y, fig_h, fig_w, fig_line_w
					, comment, aspect.getColor1(), aspect.getColor2(),"line");
		case DrakonUtils.DI_RETURN:
			fig_name = "com.yworks.flowchart.onPageReference";
			fig_w = 85;
			fig_h = 30;
			return ReturnFigure.getFigureStr(x, y, fig_h, fig_w, fig_line_w
					, comment, aspect.getColor1(), aspect.getColor2(),"dashed");
			
		case DrakonUtils.DI_CLASS_END:	
			fig_name = "com.yworks.flowchart.onPageReference";
			fig_w = 30;
			fig_h = 30;
			fig_line_w = 2;
			return CompilEndFigure.getFigureStr(x, y, fig_h, fig_w, fig_line_w
					, aspect.getColor1(), aspect.getColor2());
			
		case DrakonUtils.DI_ACTION:
			fig_name = "com.yworks.flowchart.process";
			break;
			
		case DI_CALL_PROC:
			fig_name = "com.yworks.flowchart.predefinedProcess";
			setType(DrakonUtils.DI_ACTION);
			break;
		case "IF":
			fig_name = "com.yworks.flowchart.preparation";
			break;
		case DrakonUtils.DI_COMPIL_BEG:
			fig_name = "com.yworks.flowchart.internalStorage";
			fig_line_w = 2;
			break;
		case DrakonUtils.DI_SUB_COMPIL:
			fig_name = "com.yworks.flowchart.document";
			break;
		case DrakonUtils.DI_EI:
			fig_name = "com.yworks.flowchart.start2";
			fig_w = 8;
			fig_h = 8;
			break;
		case DrakonUtils.DI_BREAK:
			fig_name = "com.yworks.flowchart.data";
			fig_w = 95;
			fig_h = 25;
			break;
		case DrakonUtils.DI_COMPIL_END:
		case DrakonUtils.DI_SI_END:
			fig_name = "com.yworks.flowchart.onPageReference";
			fig_w = 30;
			fig_h = 30;
			y += 50;
			return CompilEndFigure.getFigureStr(x, y, fig_h, fig_w, fig_line_w
					, aspect.getColor1(), aspect.getColor2());
			
		case DrakonUtils.DI_FOR_BEG:
			fig_name = "com.yworks.flowchart.loopLimit";
			fig_w = 280;
			fig_h = 35;
			break;
		case DrakonUtils.DI_FOR_END:
			fig_name = "com.yworks.flowchart.loopLimitEnd";
			fig_w = 280;
			fig_h = 35;
			break;
			
		case DI_TRY:
			fig_name = "com.yworks.flowchart.loopLimit";
			fig_w = 320;
			fig_h = 16;
			break;
			
		case DI_CATCH:
			fig_name = "com.yworks.flowchart.loopLimitEnd";
			fig_w = 320;
			fig_h = 16;
			break;
		case DI_PROC_DOC:
			fig_name = "com.yworks.flowchart.annotation";
			fig_w = 180;
			fig_h = 85;
			height = fig_h;
			width = fig_w;
			return AnnotationFigure.getFigureStr(x, y, fig_h, fig_w, fig_line_w, fig_name
					, comment, "#FFFFFF", "#FFFFFF","line");
		default:
			break;
		}
		
	String str = ActionFigure.getFigureStr(x, y, fig_h, fig_w, fig_line_w, fig_name
			, comment, aspect.getColor1(), aspect.getColor2(),border_type);			
	
	return str;
	}

	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
//		int i = code.indexOf("&&");
//		if(i>=0) {
//			code = code.replace("&&", "&amp;&amp;");
//		}
//		i = code.indexOf("<");
//		if(i>=0) {
//			code = code.replace("<", "&lt;");
//		}
		this.code = code;
		DumpVisitorGml.addCtrlCode(code);
	}

	public String getComment() {
		return comment;
	}

	
//	protected void takeAspect(String str) {
//        if(str.startsWith("//<DG2J ") || str.startsWith("// <DG2J ")) {
//        	int i = str.indexOf("aspect_begin="); 
//        	if(i != -1) {
//	        	//-- выделяем аспект
//	        	int i2 = str.indexOf("\"",i+"aspect_begin=\"".length()+1);
//	        	if(i2 == -1) {
//	        		System.err.println("Error in '<DG2J aspect_begin=' str:"+str);
//	        		return;
//	        	}
//	        	String s_aspect = str.substring(i+"aspect_begin=\"".length(),i2);
//	        	
//	        	AspectView a = DumpVisitorGml.getAspect(s_aspect);
//	        	if(a == null) {
//	        		System.err.println("Not found aspect "+ s_aspect +" in cur_aspects.");
//	        		aspect = DumpVisitorGml.getDefaultAspect();
//	        		return;
//	        	}
//	            aspect = a;
//	        	return;
//        	}
//        	i = str.indexOf("aspect_end="); 
//        	if(i != -1) {
//	        	//-- выделяем аспект
//	        	int i2 = str.indexOf("\"",i+"aspect_end=".length()+1);
//	        	if(i2 == -1) {
//	        		System.err.println("Error in '<DG2J aspect_end=' str:"+str);
//	        		return;
//	        	}
//	        	//String aspect = str.substring(i+"aspect_begin=".length(),i2);
//	        	aspect = DumpVisitorGml.getDefaultAspect();
//	        	return;
//        	}
//        	
//        }
//		
//	}
	
	public void setComment(String comment, int comment_size) {
		String s = comment.trim();
		//takeAspect(s);
		
		if(s.startsWith("//--")) {
			s = s.replace("//--", "").trim();
		}
		if(s.startsWith("// --")) {
			s = s.replace("// --", "").trim();
		}
		if(s.startsWith("//")) {
			s = s.replace("//", "").trim();
		}
		s = s.substring(0,s.length()>comment_size 
				?comment_size:s.length());
		int i = s.indexOf("&");
		if(i>=0) {
			s = s.replace("&", "&amp;");
		}
		i = s.indexOf("<");
		if(i>=0) {
			s = s.replace("<", "&lt;");
		}
		//this.comment =  nodeId + ":" +s;
		this.comment =  s;
	}

	public static int getId() {
		return id;
	}

	public String getNodeId() {
		return nodeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}

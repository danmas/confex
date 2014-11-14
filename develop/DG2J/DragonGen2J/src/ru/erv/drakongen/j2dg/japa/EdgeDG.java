package ru.erv.drakongen.j2dg.japa;

import ru.erv.drakongen.DrakonUtils;

public class EdgeDG {
	
	NodeDG src = null;
	NodeDG tgt = null;
	
	static int id = 0;
	
	String edgeId;
	int x = -1;
	int y = -1;
	
	boolean with_point = false;
	boolean right_yes = false;
	boolean down_yes = false;
	
	public EdgeDG(NodeDG src, NodeDG tgt) {
		edgeId = "e" + this.id++;
		this.src = src;
		this.tgt = tgt;
		if(src.getY() != tgt.getY()) {
			if(tgt.getX() > src.getX()) {
				int px  = tgt.getX();
				addPoint(px, src.getY()+src.getHeight()/2);
			} else if(src.getX() > tgt.getX()) {
				int px  = src.getX();
				addPoint(px, tgt.getY()+tgt.getHeight()/2);
			}
		}
	}
	
	public void setRightYes() {
		right_yes = true;
		down_yes = false;
	}
	public void setDownYes() {
		right_yes = false;
		down_yes = true;
	}
	
	
	public void addPoint(int x, int y) {
		with_point = true;
		this.x = x;
		this.y = y;
	}
	
	
	public String edgeLabel() {
		if(right_yes) {
			//-- правый верхний лейбл	
			String str = 
			"<y:EdgeLabel alignment=\"center\" configuration=\"AutoFlippingLabel\" distance=\"2.0\" "
			+ "fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" "
			+ "hasLineColor=\"false\" height=\"18.701171875\" modelName=\"custom\" "
			+ "preferredPlacement=\"anywhere\" ratio=\"0.5\" textColor=\"#000000\" "
			+ "visible=\"true\" width=\"18.80\" x=\"6.75\" y=\"-28.38\""
			+ ">Да<y:LabelModel>\n" 
		    + "<y:SmartEdgeLabelModel autoRotationEnabled=\"false\" defaultAngle=\"0.0\" defaultDistance=\"10.0\"/>\n"
		    + "</y:LabelModel>\n"
		    + "<y:ModelParameter>\n"
		    + "  <y:SmartEdgeLabelModelParameter angle=\"6.283185307179586\" distance=\"19.036454066087003\" distanceToCenter=\"true\" position=\"left\" ratio=\"0.07291751405535811\" segment=\"0\"/>\n"
		    + "</y:ModelParameter>\n"
		    + "<y:PreferredPlacementDescriptor angle=\"0.0\" angleOffsetOnRightSide=\"0\" angleReference=\"absolute\" angleRotationOnRightSide=\"co\" distance=\"-1.0\" frozen=\"true\" placement=\"anywhere\" side=\"anywhere\" sideReference=\"relative_to_edge_flow\"/>\n"
		    + "</y:EdgeLabel>\n";
			return str;
		} else if(down_yes) { 
			String str = 
		    "<y:EdgeLabel alignment=\"center\" configuration=\"AutoFlippingLabel\" distance=\"2.0\" "
		    + "fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" "
		    + "hasLineColor=\"false\" height=\"18.701171875\" modelName=\"custom\" "
		    + "preferredPlacement=\"anywhere\" ratio=\"0.5\" textColor=\"#000000\" "
		    + "visible=\"true\" width=\"18.80\" x=\"-42.41\" y=\"4.98\""
		    + ">Да<y:LabelModel>\n" 
		    + "<y:SmartEdgeLabelModel autoRotationEnabled=\"false\" defaultAngle=\"0.0\" defaultDistance=\"10.0\"/>\n"
		    + "</y:LabelModel>\n"
		    + "<y:ModelParameter>\n"
		    + "  <y:SmartEdgeLabelModelParameter angle=\"6.283185307179586\" distance=\"19.036454066087003\" distanceToCenter=\"true\" position=\"left\" ratio=\"0.07291751405535811\" segment=\"0\"/>\n"
		    + "</y:ModelParameter>\n"
		    + "<y:PreferredPlacementDescriptor angle=\"0.0\" angleOffsetOnRightSide=\"0\" angleReference=\"absolute\" angleRotationOnRightSide=\"co\" distance=\"-1.0\" frozen=\"true\" placement=\"anywhere\" side=\"anywhere\" sideReference=\"relative_to_edge_flow\"/>\n"
		    + "</y:EdgeLabel>\n";
			return str;
		}
		return "";
	}
	
	public String printEdge() {
		String line = "line";
		String color = "#000000";
		String arrow_target = "standart";
		
		if(src.getType().equals(DrakonUtils.DI_RETURN)) {
			line = "dashed";
			color = "#DFDFDF";
		}
		if(src.getType().equals(NodeDG.DI_PROC_DOC)) {
			line = "dashed";
			color = "#DFDFDF";
			arrow_target = "none";
		}
		if(with_point) {
			return "<edge id=\""+edgeId+"\" source=\""+src.getNodeId()+"\" target=\""+tgt.getNodeId()+"\">\n" + 
					"  <data key=\"d11\">\n" +
					"	<y:PolyLineEdge>\n" +
					"	  <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\">\n" +
					"       <y:Point x=\""+x+"\" y=\""+y+"\"/>\n" +
					"     </y:Path>\n" +
					"	  <y:LineStyle color=\""+color+"\" type=\""+line+"\" width=\"1.0\"/>\n" +
					edgeLabel() +
					"	  <y:Arrows source=\"none\" target=\""+arrow_target+"\"/>\n" +
					"	  <y:BendStyle smoothed=\"false\"/>\n" +
					"	</y:PolyLineEdge>\n" +
					"  </data>\n" +
					"</edge>\n"; 
		}
		return "<edge id=\""+edgeId+"\" source=\""+src.getNodeId()+"\" target=\""+tgt.getNodeId()+"\">\n" + 
		"  <data key=\"d11\">\n" +
		"	<y:PolyLineEdge>\n" +
		"	  <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
		"	  <y:LineStyle color=\""+color+"\" type=\""+line+"\" width=\"1.0\"/>\n" +
		edgeLabel() +
		"	  <y:Arrows source=\"none\" target=\""+arrow_target+"\"/>\n" +
		"	  <y:BendStyle smoothed=\"false\"/>\n" +
		"	</y:PolyLineEdge>\n" +
		"  </data>\n" +
		"</edge>\n"; 
		
	}

//	public String printEdge(String type, String color, int x, int y) {
//		return "<edge id=\""+edgeId+"\" source=\""+src.getNodeId()+"\" target=\""+tgt.getNodeId()+"\">\n" + 
//		"  <data key=\"d11\">\n" +
//		"	<y:PolyLineEdge>\n" +
//		"   <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\">\n" +
//		"    <y:Point x=\""+x+"\" y=\""+y+"\"/>\n" +
//		"   </y:Path>\n" +
//		"	  <y:LineStyle color=\""+color+"\" type=\""+type+"\" width=\"1.0\"/>\n" +
//		"	  <y:Arrows source=\"none\" target=\"standard\"/>\n" +
//		"	  <y:BendStyle smoothed=\"false\"/>\n" +
//		"	</y:PolyLineEdge>\n" +
//		"  </data>\n" +
//		"</edge>\n"; 
//	}

	
	
}

package ru.erv.drakongen.parser;

// import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerVertex;


public class DgVertex extends TinkerVertex {
	String ID;
	String code;
	String type;
	String comment;
	
	
	protected DgVertex(final String id, final TinkerGraph graph) {
        super(id, graph);
    }
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	//public DgVertex(String _ID) {
	//	ID = _ID;
	//}
	
	public String toString() {
		String str = super.toString(); 
		return "node:"+ID+ " code:"+ getProperty("code")+" com:"+getProperty("comment")
				+" type:"+getProperty("type") + " "+str;
		//return str;
	}
}

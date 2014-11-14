package ru.erv.drakongen.parser;

public class DgEdge {
	String id = "";
	DgVertex source;
	DgVertex target;
	
	public DgEdge(DgVertex source, DgVertex target) {
		System.err.println("ВЫЗОВ MyEdge(MyNode source, MyNode target)");
		this.source = source;
		this.target = target;
	}
	
	public String getIdentifier() {
		System.err.println("ВЫЗОВ !!!!!! getIdentifier()");
		return "ххх";
	}
	
	public String toString() {
		return "edge:"+source+"->"+target;
	}
	
}

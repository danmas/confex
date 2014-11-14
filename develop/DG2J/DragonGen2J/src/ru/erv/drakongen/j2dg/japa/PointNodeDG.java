package ru.erv.drakongen.j2dg.japa;

import java.util.ArrayList;
import java.util.List;

public class PointNodeDG extends NodeDG{

	List<EdgeDG> in_edges = new ArrayList<EdgeDG>();
	
	public PointNodeDG() {
		height = 8;
	}
	
	/**
	 * сколько раз вызовется столько входов и добавится
	 */
	public EdgeDG makeEdge(NodeDG src) {
		EdgeDG e = new EdgeDG(src, this);
		in_edges.add(e);
		return e;
	}

	public String printEdge() {
		String str = "";
		for(EdgeDG e : in_edges) {
			str += e.printEdge();
		}
		return str;
	}
	
}

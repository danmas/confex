package ru.erv.drakongen.j2dg.japa;

import java.util.LinkedList;
import java.util.List;

import ru.erv.drakongen.DrakonUtils;

public class WithMembersNodeDG  extends NodeDG {
	
List<NodeDG> members = new LinkedList<NodeDG>();
	
	public WithMembersNodeDG() {
		super();
	}
	
	public WithMembersNodeDG(boolean virtual) {
		super();
		if(virtual) {
			id--;
			nodeId = "v:"+id;
			setType("VIRTUAL");
		}
	}
	
	public void addMember(NodeDG n) {
		members.add(n);
	}

	public List<NodeDG> getMembers() {
		return members;
	}

	public String print() {
		String str = "";
		
		str += super.print();
		
		for(NodeDG m : members) {
			str += m.print();
		}
		return str;
	}

	public EdgeDG makeEdge(NodeDG src) {
		EdgeDG ret = super.makeEdge(src);
		
		NodeDG n_src = this;
		for(NodeDG m : members) {
			if(!m.getType().equals(DrakonUtils.DI_EI) && !m.getType().equals(DI_PROC_DOC)) {
				ret = m.makeEdge(n_src);
			}
			if(!m.getType().equals(DI_PROC_DOC)) {
				n_src = m;
			} else {
				this.makeDocEdge(m);
			}
		}
		return ret;
	}
	
	/**
	 * Рассчитывает координаты и максимальные x и y
	 * всех членов.
	 */
	protected void calcXY() {
		int cx = x;
		int cy = y;
		
		for(NodeDG m : members) {
			//if(m.getType().equals("EI") || m.getType().equals("IF")) {
			//	System.out.println("WithMembersNodeDG.calcXY()");
			//}
		
			if(m.getType().equals("DI_PROC_DOC")) {
				m.setX(cx);
				m.setY(cy - m.height - ConstantsDG.sh_dy);
			} else {

			cy += ConstantsDG.sh_dy;
			
			m.setX(cx);
			m.setY(cy);
			
			m.calcXY();

			//super.calcXY();
			if(max_x < cx)
				max_x = cx;
			
			if(max_y < cy)
				max_y = cy;
			
			if(max_x < m.getMaxX()) 
				max_x = m.getMaxX();
			
			if(max_y < m.getMaxY()) 
				max_y = m.getMaxY();
			
			cy = max_y;
			}
		}
	}
	
}

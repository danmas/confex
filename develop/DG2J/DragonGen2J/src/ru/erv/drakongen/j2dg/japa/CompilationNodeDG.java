package ru.erv.drakongen.j2dg.japa;

public class CompilationNodeDG extends WithMembersNodeDG {
	
	NodeDG end_node;

	
	public CompilationNodeDG() {
		super();
	}

	public String print() {
		makeEdge(this);
		String str = super.print();
    	return str;
	}
	
	public NodeDG getEnd_node() {
		return end_node;
	}

	
	public void setEnd_node(NodeDG end_node) {
		this.end_node = end_node;
	}

//	public void makeEdge(NodeDG src) {
//		super.makeEdge(src);
//		
//		NodeDG n_src = this;
//		for(NodeDG m : members) {
//			if(!m.getType().equals("IF")) {
//				m.makeEdge(n_src);
//			}
//			n_src = m;
//		}
//	}
	
	/**
	 * Рассчитывает координаты и максимальные x и y
	 * всех членов.
	 */
	protected void calcXY() {
		int cx = x;
		int cy = y;
		
		for(NodeDG m : members) {
			
			//if(m.type.equals("CLASS_BEG")) {
			//	cy = max_y + ConstantsDG.sh_dy;
				
			cy += ConstantsDG.sh_dy;
			
			m.setX(cx);
			m.setY(cy);
			
			m.calcXY();

			//super.calcXY();
			if(max_x < x)
				max_x = x;
			
			if(max_y < y)
				max_y = y;
			
			
			if(max_x < m.getMaxX()) 
				max_x = m.getMaxX();
			
			if(max_y < m.getMaxY()) 
				max_y = m.getMaxY();
			
			cy = max_y;
		}
	}
	
}

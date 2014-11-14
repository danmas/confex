package ru.erv.drakongen.j2dg.japa;

import java.util.LinkedList;
import java.util.List;

public class IfNodeDG extends NodeDG {
	
	WithMembersNodeDG then_list = new WithMembersNodeDG(true);
	WithMembersNodeDG else_list = new WithMembersNodeDG(true);
	//-- признак того что ветка Да идет вниз
	boolean yes_then = true; 
	
	NodeDG end;
	
	public IfNodeDG() {
		super();
	}
	
	public void addThenMember(NodeDG n) {
		then_list.addMember(n);
	}
	
	public void addElseMember(NodeDG n) {
		else_list.addMember(n);
	}

	
	public String print() {
		String str = super.print();
		
		str += then_list.print();
		str += else_list.print();
		
		return str;
	}

	
	public String printComment() {
		String fig_name = "com.yworks.flowchart.preparation";
		
		String str = ActionFigure.getFigureStr(x, y, height, width, fig_line_w
				, fig_name, comment, aspect.getColor1(), aspect.getColor2(),"line");			
	
		return str;
	}
	
	public EdgeDG makeEdge(NodeDG src) {
		EdgeDG ret = super.makeEdge(src);
		
		NodeDG n_src = this;
		//-- сначала проходим ветку вниз
		for(NodeDG m : then_list.getMembers()) {
			if(!m.getType().equals("EI")) {
				EdgeDG e = m.makeEdge(n_src);
				if(n_src == this && yes_then) {
					e.setDownYes();
				}
			}
			n_src = m;
		}
		EdgeDG e = end.makeEdge(n_src);
		if(n_src == this && yes_then) {
			e.setDownYes();
		}
		
		n_src = this;
		//-- затем ветку вправо
		for(NodeDG m : else_list.getMembers()) {
			if(!m.getType().equals("EI")) {
				e = m.makeEdge(n_src);
				if(n_src == this && !yes_then) {
					e.setRightYes();
				}
			}
			n_src = m;
		}
		e = end.makeEdge(n_src);
		if(n_src == this && !yes_then) {
			e.setRightYes();
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
		max_x = x;
		max_y = y;
		
		//-- переставляем ветки если нужно 
		if(then_list.getMembers().size() > 0 && else_list.getMembers().size() == 0) {
			WithMembersNodeDG tmp = else_list;
			else_list = then_list;
			then_list = tmp;
			yes_then = false;
		}
		
		//-- сначала проходим ветку вниз
		for(NodeDG m : then_list.getMembers()) {
			cy += ConstantsDG.sh_dy;
			
			m.setX(cx);
			m.setY(cy);
			
			m.calcXY();

			super.calcXY();
			
			if(max_x < m.getMaxX()) 
				max_x = m.getMaxX();
			
			if(max_y < m.getMaxY()) 
				max_y = m.getMaxY();
			
			cy = max_y;
		}
		
		//-- потом ветку вправо с отступом на максимальное х
		
		cx = max_x + ConstantsDG.sh_if_dx;
		int then_max_y = max_y;
		cy = y;
		for(NodeDG m : else_list.getMembers()) {
			cy += ConstantsDG.sh_dy;
			
			m.setX(cx);
			m.setY(cy);
			
			m.calcXY();

			super.calcXY();
			
			if(max_x < m.getMaxX()) 
				max_x = m.getMaxX();
			
			if(max_y < m.getMaxY()) 
				max_y = m.getMaxY();
			
			cy = max_y;
		}
		if(max_y < then_max_y)
			max_y = then_max_y;
	}

	public NodeDG getEnd() {
		return end;
	}

	public void setEnd(NodeDG end) {
		this.end = end;
	}

	public WithMembersNodeDG getThen_list() {
		return then_list;
	}

	public WithMembersNodeDG getElse_list() {
		return else_list;
	}
	
}

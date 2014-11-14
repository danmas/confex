package ru.erv.drakongen.j2dg.japa;

import java.util.ArrayList;
import java.util.List;

import ru.erv.drakongen.DrakonUtils;
import ru.erv.drakongen.Settings;

public class SwitchNodeDG  extends NodeDG {
	
	List<CaseNodeDG> case_list = new ArrayList<CaseNodeDG>();
	
	PointNodeDG beg;
	PointNodeDG end;
	
	public SwitchNodeDG() {
		super();
	}
	
	public void addCase(CaseNodeDG c) {
		case_list.add(c);
	}
	
	public List<CaseNodeDG> getCase_list() {
		return case_list;
	}

	public String print() {
		String str = super.print();
		
		str += beg.print();
		
		for(CaseNodeDG c : case_list) {
			str += c.print();
		}
		
		return str;
	}

	
	public String printComment() {
		String fig_name = "com.yworks.flowchart.data";
		
		String str = ActionFigure.getFigureStr(x, y, height, width, fig_line_w
				, fig_name, comment, aspect.getColor1(), aspect.getColor2(),"line");			
	
		return str;
	}
	
	public EdgeDG makeEdge(NodeDG src) {
		EdgeDG ret = super.makeEdge(src);
		
		beg.makeEdge(this);
		
		for(CaseNodeDG c : case_list) {
			EdgeDG e = c.makeEdge(beg);
			end.makeEdge(e.tgt);
		}
		
//		
//		NodeDG n_src = this;
//		//-- сначала проходим ветку вниз
//		for(NodeDG m : then_list.getMembers()) {
//			if(!m.getType().equals("EI")) {
//				EdgeDG e = m.makeEdge(n_src);
//				if(n_src == this && yes_then) {
//					e.setDownYes();
//				}
//			}
//			n_src = m;
//		}
//		EdgeDG e = end.makeEdge(n_src);
//		if(n_src == this && yes_then) {
//			e.setDownYes();
//		}
//		
//		n_src = this;
//		//-- затем ветку вправо
//		for(NodeDG m : else_list.getMembers()) {
//			if(!m.getType().equals("EI")) {
//				e = m.makeEdge(n_src);
//				if(n_src == this && !yes_then) {
//					e.setRightYes();
//				}
//			}
//			n_src = m;
//		}
//		end.makeEdge(n_src);
//		e = end.makeEdge(n_src);
//		if(n_src == this && !yes_then) {
//			e.setRightYes();
//		}
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
		
		beg.setX(cx);
		beg.setY(cy + this.height + (ConstantsDG.sh_dy - this.height)/2 - beg.height/2);
		
		cy += ConstantsDG.sh_dy;
		
		for(CaseNodeDG c : case_list) {
			c.setX(cx);
			c.setY(cy);
			c.calcXY();

			super.calcXY();
			
			if(max_x < c.getMaxX()) 
				max_x = c.getMaxX();
			
			if(max_y < c.getMaxY()) 
				max_y = c.getMaxY();
			
			cx = max_x + ConstantsDG.sh_if_dx;
		}
	}

	public NodeDG getEnd() {
		return end;
	}

	public void setEnd(PointNodeDG end) {
		this.end = end;
	}
	
	public void setBeg(PointNodeDG beg) {
		this.beg = beg;
	}
	
}
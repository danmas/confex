package ru.erv.drakongen.j2dg.japa;

import ru.erv.drakongen.DrakonUtils;


public class ClassNodeDG extends WithMembersNodeDG {
	
	
	public ClassNodeDG() {
		super();
	}

	
	/**
	 * Рассчитывает координаты и максимальные x и y
	 * всех членов.
	 */
	protected void calcXY() {
		int cx = x;
		int cy = y;
		int base_line = y;
		
		max_x = x;
		int max_x_proc = x + ConstantsDG.sh_if_dx;
		
		cx = x + ConstantsDG.sh_if_dx;
		int i = 0;
		for(NodeDG m : members) {
			if(m.type.equals(DrakonUtils.DI_PROC_BEG)) {
				if(i==0) {
					i = 1;
				} else {
					cx = max_x + ConstantsDG.sh_proc_dx;
					max_x_proc = cx;
				}
				cy = base_line; //-- предполагаем, что сначала идут AC а потом PROC_BEG
			} else if(m.type.equals(DrakonUtils.DI_CLASS_END)) {
				cy = base_line + ConstantsDG.sh_dy;
				cx = max_x_proc + ConstantsDG.sh_proc_dx;
			} else {
				cy += ConstantsDG.sh_dy;
				base_line = cy + ConstantsDG.sh_dy;
			}
			
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
		} //-- for
		//-- сбрасываем max_x чтобы не влияло на след класс
		max_x = x;
	}	
}

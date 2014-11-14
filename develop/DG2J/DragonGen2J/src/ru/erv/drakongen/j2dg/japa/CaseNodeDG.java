package ru.erv.drakongen.j2dg.japa;

import ru.erv.drakongen.DrakonUtils;

public class CaseNodeDG extends WithMembersNodeDG {

	
	public String printComment() {
		String fig_name = "com.yworks.flowchart.manualOperation";
		String border_type = "line";
		if(getType().equals(DrakonUtils.DI_DEFAULT))
			border_type = "dashed";
		String str = ActionFigure.getFigureStr(x, y, height, width, fig_line_w
				, fig_name, comment, aspect.getColor1(), aspect.getColor2(),border_type);			
		return str;
	}
	
}

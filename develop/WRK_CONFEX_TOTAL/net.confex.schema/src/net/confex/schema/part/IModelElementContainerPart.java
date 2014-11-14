package net.confex.schema.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;

/**
 * EditPart общий для всех контейнеров элементов 
 * 
 * @author Roman_Eremeev
 */

public interface IModelElementContainerPart {

	public void setLayoutConstraint(EditPart child, IFigure childFigure, Object constraint);
	
	
	/**
	 * Сделать figure и все дочерние Visible
	 * @param vis_flag - true все видимы
	 */
	public void setAllVisible(boolean visible);


	/**
	 * Пререрисовать все немедленно
	 */
	public void immediateRepaint();

}

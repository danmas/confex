package net.confex.schema.figures;

public interface IActiveElementFigure {

	
	/**
	 * Рисует элемент в пассивном состоянии
	 */
	public void drawPassiveState();
	
	
	/**
	 * Рисует элемент в активном состоянии
	 */
	public void drawActiveState();
	
	
	/**
	 * Рисует элемент в состоянии ошибочного завершения
	 */
	public void drawErrorState();
	
	
	/**
	 * Рисует элемент в cостоянии удачного заваршения 
	 */
	public void drawOkState();
	
	
}

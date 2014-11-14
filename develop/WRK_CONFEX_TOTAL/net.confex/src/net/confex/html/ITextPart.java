package net.confex.html;

public interface ITextPart {

	/**
	 * Возвращает часть текста который потом вставляется в документ
	 * Если нужно то конструирует контент
	 * 
	 * @return
	 */
	public String getFullText();
}

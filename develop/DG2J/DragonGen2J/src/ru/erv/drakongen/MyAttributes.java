package ru.erv.drakongen;

public class MyAttributes {

	public void setAttribute(String currentAttributeID,
			String currentAttributeKey, String currentAttributeData) {
		System.err.println("ВЫЗОВ setAttribute("+currentAttributeID+","+currentAttributeKey+","+currentAttributeData);
	}
	public void setAttribute(String currentAttributeID,
			String currentAttributeKey, Double currentAttributeData) {
		System.err.println("ВЫЗОВ setAttribute(2)");
	}
}

package ru.erv.drakongen.test;

import japa.parser.ast.type.PrimitiveType;

public class TestSwitch1 {

	public void visit(PrimitiveType n, Object arg) {
		switch (n.getType()) {
		case Boolean:
			System.err.println("boolean");
		case Double:
			System.err.println("double or boolean");
			break;
		case Byte:
			System.err.println("byte");
			switch (n.getType()) {
			case Boolean:
				System.err.println("boolean");
				break;
			case Byte:
				System.err.println("byte");
				break;
			default:
				System.err.println("---");
				break;
			}
			break;
		default:
			System.err.println("---");
			break;
		}
	}

}

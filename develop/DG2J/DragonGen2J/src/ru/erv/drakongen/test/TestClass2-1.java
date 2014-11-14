//-- Compilation
//-- package//-- imports
package ru.erv.drakongen.test;

//-- public class TestClass2 {
public class TestClass21 {

	// -- int c = 5;
	int c = 5;

	// -- Конструктор
	public TestClass21() {

		// -- c == 5
		if (c == 5) {
			// -- System.out.println("Это тестовое сообщение из конструктора")
			System.out.println("Это тестовое сообщение из конструктора");
		} else {
		}
		// -- //--
	}

	// -- proc1
	public void proc1() {

		// -- C is 5
		if (c == 5) {
			// -- System.out.println("Это тестовое сообщение из proc1()");
			System.out.println("Это тестовое сообщение из proc1()");
		} else {
		}
		// -- //--
	}

	// -- main
	public static void main(String[] args) {

		// -- переменная ct
		TestClass2 ct = new TestClass2();
		// -- ct.proc1();
		ct.proc1();
		// -- переменная ct
		TestClass22 ct22 = new TestClass22();
		// -- ct22.proc1();
		ct22.proc1();
		// -- //--
	}

	// --

}

// -- class TestClass22 {
class TestClass22 {

	// -- Конструктор
	public TestClass22() {

		// -- c == 5
		if (c == 5) {
			// -- System.out.println("Это тестовое сообщение из конструктора 2
			System.out.println("Это тестовое сообщение из конструктора 22");
		} else {
		}
		// -- //--
	}

	// -- proc1
	public void proc1() {

		// -- C is 5
		if (c == 5) {
			// -- System.out.println("Это тестовое сообщение из proc1() 22");
			System.out.println("Это тестовое сообщение из proc1() 22");
		} else {
		}
		// -- //--
	}

	// --

}

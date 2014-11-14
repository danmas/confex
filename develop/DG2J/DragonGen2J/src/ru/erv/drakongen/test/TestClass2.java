package ru.erv.drakongen.test;


public class TestClass2 {

	int c = 5;

	// -- Конструктор
	public TestClass2() {
		if (c == 5) {
			// -- Это тестовое сообщение
			System.out.println("Это тестовое сообщение из конструктора");
		}

	}

	// -- Процедура 1
	public void proc1() {
		// -- C is 5
		if (c == 5) {
			// -- Это тестовое сообщение
			System.out.println("Это тестовое сообщение из proc1()");
		}
	}

	// -- main
	public static void main(String[] args) {
		// -- переменная ct
		TestClass2 ct
		// -- экземпляр класса TestClass1
		= new TestClass2();
		// -- Вызов Процедуры 1
		ct.proc1();
		
		// -- переменная ct
		TestClass22 ct22
		// -- экземпляр класса TestClass1
		= new TestClass22();
		// -- Вызов Процедуры 1
		ct22.proc1();
	}
} 



class TestClass22 {

	int c = 5;


	// -- Конструктор
	public TestClass22() {
		if (c == 5) {
			// -- Это тестовое сообщение
			System.out.println("Это тестовое сообщение из конструктора 22");
		}

	}

	// -- Процедура 1
	public void proc1() {
		// -- C is 5
		if (c == 5) {
			// -- Это тестовое сообщение
			System.out.println("Это тестовое сообщение из proc1() 22");
		}
	}
	
} // -- конец класса

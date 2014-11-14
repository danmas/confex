package ru.erv.drakongen.test;

public class TestClass1 {

	int c = 5;

	// -- Конструктор
	public TestClass1() {
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
		TestClass1 ct
		// -- экземпляр класса TestClass1
		= new TestClass1();
		// -- Вызов Процедуры 1
		ct.proc1();
	}

} // -- конец класса

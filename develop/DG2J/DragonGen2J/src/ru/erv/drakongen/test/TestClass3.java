package ru.erv.drakongen.test;

public class TestClass3 {

	int c = 5;

	
	class TestClass31 {
		
		public TestClass31() {
			System.out.println(" TestClass31() ");
		}
	}
	
	
	// -- Конструктор
	public TestClass3() {
		if (c == 5) {
			// -- Это тестовое сообщение
			System.out.println("Это тестовое сообщение из конструктора");
		}

	}

	
	class TestClass32 {
		
		public TestClass32() {
			System.out.println(" TestClass32() ");
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
		TestClass3 ct
		// -- экземпляр класса TestClass1
		= new TestClass3();
		// -- Вызов Процедуры 1
		ct.proc1();
		
		// -- переменная ct
		TestClass33 ct33
		// -- экземпляр класса TestClass1
		= new TestClass33();
		// -- Вызов Процедуры 1
		ct33.proc1();
	}
} 



class TestClass33 {

	int c = 5;

	// -- Конструктор
	public TestClass33() {
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
	
} 

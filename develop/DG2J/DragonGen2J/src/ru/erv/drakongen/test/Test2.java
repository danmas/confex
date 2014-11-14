//-- Сборка класса Тест2
//-- package//-- imports
package ru.erv.drakongen.test;

//-- сlass Test2
public class Test2 {

	int c = 5;

	// -- Конструктор
	public Test2() {
		if (c == 5) {
			// -- Это тестовое сообщение
			System.out.println("Это тестовое сообщение");
		}

	}

	// -- Процедура 1
	public void proc1() {
		// -- C
		if (c == 5) {
			// -- Это тестовое сообщение
			System.out.println("Это тестовое сообщение");
			// -- //--

		} else {
			// -- //--

		}
	}

	// -- main
	public static void main2(String[] args) {
		// -- переменная ct
		Test2 ct
		// -- экземпляр класса Test2
		= new Test2();
		// -- Вызов Процедуры 1
		ct.proc1();
		// -- //--

	}

	// --

} // -- конец класса


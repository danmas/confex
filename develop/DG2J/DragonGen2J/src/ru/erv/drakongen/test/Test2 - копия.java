/*
 * Copyright (C) 2014 Roman Eremeev
 * 
 * Drakon-Jazz the visual programming tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Drakon-Jazz is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Drakon-Jazz.  If not, see <http://www.gnu.org/licenses/>.
 */
//<DG2J aspect_beg="ASPECTS"/>
package ru.erv.drakongen.test;

import javax.xml.ws.Action;

/**
 * Класс Тест2
 * 
 * @author roman
 *
 */
public class Test2 {

	int a = 7;
	int b = 8;

 /**
 * Конструктор
 *
 */
 public Test2() {
 //<DG2J aspect_begin="ASPECTS"/>
 System.out.println("--> Конструктор");
 //<DG2J aspect_end="ASPECTS"/>
 }

	public void proc0() {
		int c = 5;
		if (c == 1) {
			System.out.println("c=1");
		} else {
		}

		// <DG2J aspect_begin="ASPECTS"/>
		//проверяем что с равно 19
		if (c == 19) {
			// System.out.println("a=19");
		} else {
			if (a == 51) {
				System.out.println("a=51");
				if (c == 72)
					// <DG2J aspect_end=""/>
					System.out.println("c=72");
				else {
					// System.out.println("c!=72");
					if (c == 191) {
					} else {
						System.out.println("c!=191");
					}
				}
			} else {
				System.out.println("a!=51");
				if (c == 93)
					System.out.println("c=93");
				else
					System.out.println("c!=93");
			}
			System.out.println("c!=192");
		}
	}

	/**
	 * Процедура 1
	 * 
	 * <DG2J aspect_end="ASPECTS"/>
	 */
	public String proc1() {
		// -- Это тестовое сообщение
		System.out.println("Это тестовое сообщение");

		int a = 5;
		int c = 7;

		// <DG2J aspect_begin="ASPECTS"/>
		if (a == 5) {
			System.out.println("a=5");
		} else {
			System.out.println("a!=5");
		}
		// <DG2J aspect_end="ASPECTS"/>

		if (c == 7)
			System.out.println("c=7");
		else
			System.out.println("c!=7");

		if (a == 5) {
			System.out.println("a=5");
			if (c == 71) {
				System.out.println("c=71");
				// return "---";
			} else {
				System.out.println("c!=71");
				if (c == 19) {
				} else {
					if (a == 51) {
						System.out.println("a=51");
						if (c == 72)
							System.out.println("c=72");
						else {
							System.out.println("c!=72");
							if (c == 191) {
							} else {
								System.out.println("c!=191");
							}
						}
					} else {
						System.out.println("a!=51");
						if (c == 93)
							System.out.println("c=93");
						else
							System.out.println("c!=93");
					}
					System.out.println("c!=192");
				}
			}
		} else {
			System.out.println("a!=54");
			if (c == 94)
				System.out.println("c=94");
			else
				System.out.println("c!=94");
		}

		if (a == 41) {
			System.out.println("a=41");
		} else {
			System.out.println("a!=41");
		}

		return "--ret_str--";
	}

	/**
	 * Процедура 2
	 */
	public void proc2() {
		// -- Это тестовое сообщение
		System.out.println("Это тестовое сообщение 2");
		return;
	}

	/**
	 * Процедура 3
	 */
	public void proc3() {
		// -- Это тестовое сообщение
		System.out.println("Это тестовое сообщение 3");
		int a = 2;
		if (a == 4) {
			System.out.println("a=4");
		} else {
			System.out.println("a!=4");
			if (a == 4) {
				System.out.println("a=4");
			} else {
				System.out.println("a!=4");
			}
		}
		System.out.println("Это тестовое сообщение 3");
	}

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// -- переменная ct
		Test2 ct
		// -- экземпляр класса Test2
		= new Test2();
		// -- Вызов Процедуры 1
		ct.proc1();
	}

} // -- конец класса


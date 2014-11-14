package ru.erv.drakongen.test;

import java.util.ArrayList;
import java.util.List;

public class Test22 {

	private int i1;
	
	class Test221 {

		//-- Конструктор
		/**
		 * 
		 * 
		 * 
		 */
		//<DG2J aspect_begin="ASPECTS"/>
		public Test221() {
			try {
				System.out.println("---");
			} catch( Exception e) {
				System.err.println(e.getMessage());
			}
		} 
		//<DG2J aspect_end="ASPECTS"/>

		//-- Процедура 1
		/**
		 * -- Процедура 1
		 * 
		 */
		//<DG2J aspect_begin="ASPECTS"/>
		public void proc1() { 
			//-- C
			int c = 2;
			//<DG2J aspect_end="ASPECTS"/>
			if(c == 5) {
				//-- Это тестовое сообщение
				System.out.println("Это тестовое сообщение"); 
				//-- //--             
				


			} else {
				//-- //--             
				


			}
		}
	 }
	
	private int i2;
	
	//-- Конструктор
	public Test22() { 
		//-- //--             
	} 

	
	 public void continueLabel() { 
		 	//<DG2J aspect_begin="DRAKON"/>
		 //--- цикл
		 outer: 
			 for (int i=0; i < 10; i++) { 
				 //outer_2:
                    for (int j = 0; j < 10; j++) { 
		                if(j>i){ 
                               System.out.println(""); 
                               continue outer; 
		                } 
		                System.out.print(" " + (i * j)); 
                     } 
		 		}
	 }
	
	 
	//-- Процедура 1
	public void proc1() { 
		//-- C
		int c = 2;
		List<String> list_str = new ArrayList<String>();
		if(c == 5) {
			//-- for цикл
			for(int i = 1; i< 10; i++) {
				//-- Это тестовое сообщение
				System.out.println("Это тестовое сообщение");
				
				if(c == 5) {
					eee:					
					//-- foreach цикл
					 for(String s : list_str) {
						//-- Это тестовое сообщение
						System.out.println(s);
						continue eee;
					}
					
					//-- do цикл
					sss: do {
						c += 4;
					 	if (c % 2 == 0) continue sss; 
					} while (c < 100);
					
					//-- while цикл
					ddd: while (c < 100) {
						c += 5;
					}
				}
				
			}

		} else {
		}
	} 

	//-- staticProc
	public static void staticProc(String arg) { 
		try {
			//-- переменная ct класса Test2
			Test2 ct  
			//-- экземпляр класса Test2 
			= new Test2(); 
			//-- Вызов Процедуры 1
			ct.proc1();
		} catch( Exception e) {
			System.err.println(e.getMessage());
		}
		             
	} 
	
	
}

 
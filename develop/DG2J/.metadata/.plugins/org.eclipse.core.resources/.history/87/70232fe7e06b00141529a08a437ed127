
//-- Класс DrakonGen2
	//-- упоминание о DrakonGen2
	/**
  * Этот текст сгенерирован программой DrakonGen2
  * @author Erv +
*/ 
	//-- package//-- imports
	package ru.erv.drakongen;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import ru.erv.drakongen.utils.*;
import ru.erv.drakongen.*; 
 
	//-- class DrakonGen2
	public class DrakonGen2 { 
	//-- константы
		public final static String DI_EXT_NEXT = "next";
	public final static String DI_DG_BEG = "DG_BEG";
	public final static String DI_SI_BEG = "SI_BEG";
	public final static String DI_SI_END = "SI_END";
	public final static String DI_CLASS_END = "CLASS_END";
	public final static String DI_COMPIL_BEG = "COMPIL_BEG";
	public final static String DI_COMPIL_END = "COMPIL_END";
	public final static String DI_SH_BEG = "SH_BEG";
	public final static String DI_SH_END = "SH_END"; 
	public final static String DI_PROC_BEG = "PROC_BEG";
	public final static String DI_PROC_END = "PROC_END"; 
	public final static String DI_WR_RES_FILE = "WR_RES_FILE";
	public final static String DI_AC = "AC";		
	public final static String DI_ACTION = "ACTION";		
	public final static String DI_SUB_COMPIL = "SUB_COMPIL";		
	//public final static String DI_IR = "IR";		
	//TODO: удалить DI_ID и DI_IR
	//public final static String DI_EQL = "EQL";		
	//public final static String DI_EQR = "EQR";		
	public final static String DI_IF = "IF";		
	public final static String DI_RY = "RY";		
	public final static String DI_DN = "DN";		
	public final static String DI_EI = "EI";		
	public final static String DI_UK = "UK";		
	public final static String DI_FOR_BEG = "FOR_BEG";		
	public final static String FOR_EACH_BEG = "FOR_EACH_BEG";		
	public final static String DI_FOR_END = "FOR_END";		
	public final static String DI_REF = "REF";
	public final static String DI_BREAK = "BREAK";
	
	public final static String DI_CASE = "CASE";
	public final static String DI_DEFAULT = "DEFAULT";
	public final static String DI_SW = "SWITCH";
	public final static String DI_RETURN = "RETURN";
	public final static String DI_INSERT = "INSERT";
	public final static String DI_OUTPUT = "OUTPUT";
	
	public final static String RELEASE_TYPE_CODE_JAVA = "CODE_JAVA";
	public final static String RELEASE_TYPE_CODE_AS = "CODE_AS"; 
	//-- переменные
	protected String res_str = "";
protected boolean load_finish = false;
protected String CURRENT_RELEASE = RELEASE_TYPE_CODE_AS; 
	//-- Конструктор
	public  DrakonGen2() { 
		//-- //--             
		


	} 

	//-- Парсер схемы
	public String parse_drakon(Graph graph) {
	Object data;
	String descr;
	String di_type;
	
	res_str = ""; 
		//-- Проходим по всем узлам
		for (Vertex v : graph.getVertices()) {
			//-- получаем тип узла
			di_type = DrakonUtils.getIconType(v); ; 
			//-- узел НАЧАЛО?
			if(di_type != null && di_type.equals(DI_DG_BEG)) {
				//-- извлекаем из Начало тип реальности
				CURRENT_RELEASE = (String) DrakonUtils.getCode(v); 
				//-- ---
				DrakonUtils.message("--->Текущая реальность: " + CURRENT_RELEASE); 
				//-- у текущего узла один выход?
				if(DrakonUtils.getOutDegree(v) == 1) {
					//-- теперь текущий узел тот на который указывает выход
					v = DrakonUtils.getOutNode(v,0); 
					//-- ---
					DrakonUtils.message("---> Разбираем силуэт "); 
					//-- Разбираем диаграмму
					parseSiluet(v); 
				} else {
					//-- у текущего узла нет выходов?
					if(DrakonUtils.getOutDegree(v) == 0) {
					} else {
						//-- ОШИБКА! У иконы Начало ...//--  выходов!
						DrakonUtils.error("ОШИБКА! У иконы Начало \"" + DrakonUtils.getOutDegree(v) + "\" выходов!"); 
						//-- null
						return null; 
					}
				}
			} else {
			}
		}
		//-- -- Схема построена
		DrakonUtils.message("//-- Схема построена\n\n "); 
		//-- результирующая строка
		return res_str;



	} 

	//-- Парсер одного силуэта
	public void parseSiluet(Vertex node) {
/**
 * Парсер одного силуэта ДРАКОНА 
 * @param	var graph_data
 */ 
		//-- переменные
		Vertex cur_node = node;
String comment = DrakonUtils.getComment(cur_node);
String di_type;
String code = "";
Vertex next_node;
Vertex next_node2 = null;
int level = 0;
String str = "";  
		//-- -psi- n: 
		DrakonUtils.debug("-psi- n: "+DrakonUtils.getComment(cur_node)); 
		//-- получаем параметры текущего узла
		cur_node = node;
code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
		//-- узел НАЧАЛО СИЛУЭТА?
		if(di_type.equals(DI_SI_BEG) || di_type.equals(DI_COMPIL_BEG)) {
			//-- Проверка выходов проходит?
			if(isCheckOutputs(cur_node)) {
			} else {
				//-- формируем сообщение о ошибке
				str = "ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы " + comment +" (тип " + di_type+")";
res_str += str; 
				//-- ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы 
				DrakonUtils.error(str); 
				//-- //--         
				return; 
			}
		} else {
			//-- формируем сообщение о ошибке
			str = "ОШИБКА! Первый узел шампура должен быть \"" + DI_SI_BEG + "\"!\n"
+ "А узел " + comment +" имеет тип " + di_type;
res_str += str; 
			//-- ОШИБКА! Первый узел шампура должен быть НАЧАЛО СИЛУЭТА
			DrakonUtils.error(str); 
			//-- //--         
			return; 
		}
		//-- меняем в комментарии перевод сроки на //-- и добавляем его в результат
		if(comment != null)	
	comment = comment.replace("\n","//-- ");
if(comment != null) 
	res_str += "\n//-- " + comment + "\n";
if(code != null)	
	res_str += code + "\n"; 
		//-- выходных ребер не 1 и не 2?
		if(DrakonUtils.getOutDegree(cur_node) != 1 && DrakonUtils.getOutDegree(cur_node) != 2) {
			//-- формируем сообщение о ошибке
			str = "ОШИБКА! У иконы Начало Силуэта \"" + DrakonUtils.getOutDegree(cur_node) + "\" выходов!\n"
+ " Должно быть 1 или 2.\n"; 
			//-- ОШИБКА! У иконы Начало Силуэта  Должно быть 1 или 2 выхода
			DrakonUtils.error(str); 
			//-- //--         
			return; 
		} else {
		}
		//-- выходных ребер 2?
		if(DrakonUtils.getOutDegree(cur_node) == 2) {
			//-- получаем тип первго выхода
			Vertex v = DrakonUtils.getOutNode(cur_node,0);
di_type = DrakonUtils.getIconType(v);
 
			//-- на первом выходе ЗАПИСЬ В ФАЙЛ?
			if(di_type.equals(DI_WR_RES_FILE)) {
				//-- след.узлом будет тот что на первом выходе, а текущим станет тот что на втором
				next_node = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
			} else {
				//-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
				next_node = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
			}
		} else {
			//-- ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода
			DrakonUtils.error("ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода"); 
			//-- //--         
			return; 
		}
		//-- тек.узел
		cur_node = 
		//-- Разбираем начальную группу
		parceBegGroup(cur_node, level + 1); 
		//-- тек.узел ЧАСТЬ СБОРКИ?
		if(DrakonUtils.getIconType(cur_node).equals(DI_SUB_COMPIL)) {
			//-- получаем параметры текущего узла
			code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
			//-- добавляем коментарий и код в результат
			if(comment != null)
	res_str +=  "//-- " + comment + "\n";
if(code != null) 
	res_str +=  code + " \n";
 
			//-- по всем ЧАСТЯМ СБОРКИ
			while(cur_node!=null && DrakonUtils.getIconType(cur_node).equals(DI_SUB_COMPIL)) {
				//-- у тек.узела 2 выхода?
				if(DrakonUtils.getOutDegree(cur_node) == 2
) {
					//-- на первом выходе ЧАСТЬ СБОРКИ?
					if(di_type.equals(DI_SUB_COMPIL)) {
						//-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
						next_node2 = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
					} else {
						//-- след.узлом будет тот что на первом выходе, а текущим станет тот что на втором
						next_node2 = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
					}
				} else {
					//-- у тек.узела 1 выход?
					if(DrakonUtils.getOutDegree(cur_node) == 1
) {
						//-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
						next_node2 = null; 
cur_node = DrakonUtils.getOutNode(cur_node,0); 
						//-- след.узлом будет тот что на выходе
						cur_node = DrakonUtils.getOutNode(cur_node,0); 
					} else {
						//-- Ошибка! У иконы ЧАСТЬ СБОРКИ должен быть один или два выхода.
						str = "Ошибка! Ошибка! У иконы ЧАСТЬ СБОРКИ \"" + comment + "\" ("+ DrakonUtils.getIconType(cur_node) + ") должен быть один или два выхода.\n";
DrakonUtils.error(str); 
						//-- //--         
						return; 
					}
				}
				//-- тип тек.узла НАЧАЛО ШАМПУРА?
				if(DrakonUtils.getIconType(cur_node).equals(DI_SH_BEG) || DrakonUtils.getIconType(cur_node).equals(DI_PROC_BEG)) {
				} else {
					//-- тек.узел
					cur_node = 
					//-- Разбираем начальную группу
					parceBegGroup(cur_node, level + 1); 
				}
				//-- Разбираем шампур
				parceShampur(cur_node, level + 1); 
				//-- тек.узел
				cur_node =
 
				//-- след.узел
				next_node2;
 
			}
		} else {
			//-- тип тек.узла НАЧАЛО ШАМПУРА?
			if(DrakonUtils.getIconType(cur_node).equals(DI_SH_BEG) || DrakonUtils.getIconType(cur_node).equals(DI_PROC_BEG)) {
				//-- Разбираем шампур
				parceShampur(cur_node, level + 1); 
			} else {
				//-- Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ Должен быть ЧАСТЬ СБОРКИ или НАЧАЛО ПРОЦЕДУРЫ.
				str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ DrakonUtils.getIconType(cur_node) + ") должен быть ЧАСТЬ СБОРКИ или НАЧАЛО ПРОЦЕДУРЫ. !n";
DrakonUtils.error(str); 
			}
		}
		//-- есть след.узел?
		if(next_node != null) {
			//-- узел ЗАПИСЬ В ФАЙЛ?
			if(DrakonUtils.getIconType(next_node).equals(DI_WR_RES_FILE)) {
				//-- имя файла
				String file_name
 
				//-- из иконы ЗАПИСЬ В ФАЙЛ
				= Settings.getProperty("BASE_DIR") + "\\" +  getCleanReleaseCode(next_node);
 
				//-- Имя файла задано?
				if(file_name != null && file_name.length() > 0) {
				} else {
					//-- формируем сообщение о ошибке
					str = "ОШИБКА. В иконе  ЗАПИСЬ ФАЙЛА не задано имя выходного файла.\n";
res_str += str;
 
					//-- ОШИБКА. В иконе  ЗАПИСЬ ФАЙЛА не задано имя выходного файла.
					DrakonUtils.error(str); 
					//-- имя файла
					file_name
 
					//-- временный файл
					= "tmp.java";
 
				}
				//-- ----
				DrakonUtils.message("----> Записываем файл " + file_name + "\n"); 
				//-- Запись в файл//-- 
				FileUtils.fileWrite(file_name, res_str);
 
			} else {
			}
		} else {
			//-- ----
			DrakonUtils.message("----> ПРЕДУПРЕЖДЕНИЕ. Результат не сохранен в файл.\n"); 
		}
		//-- очищаем результат
		res_str = ""; 
		//-- //--             
		


	} 

	//-- Разбираем начальную группу
	protected Vertex parceBegGroup(Vertex cur_node, int _level) { 
		//-- переменные
		String comment = DrakonUtils.getComment(cur_node);
String di_type = DrakonUtils.getIconType(cur_node);
String code = geReleaseCode(cur_node);
String spaces = "";
Vertex term_yes;
String str;
Vertex node;
Vertex cur_node_d;  
		//-- -pnx- n: 
		DrakonUtils.debug("-pnx- n: "+DrakonUtils.getComment(cur_node)); 
		//-- текущий узел null?
		if(cur_node == null) {
			//-- null
			return null; 
		} else {
		}
		//-- в строку пробелов добавляем табуляторы по глубине уровня
		for (int i = 0; i < _level; i++)  
	spaces += "\t";
 
		//-- обрабатываем перевод строки в комментариях и коде
		if(comment != null)	
	comment = comment.replace("\n","//-- ");
//if(code != null)	
//	code = code.replace("\n","//-- ");
 
		//-- тип текущего узла не задан?
		if(di_type == null || di_type.length() == 0) {
			//-- ОШИБКА! Не задан тип иконы
			DrakonUtils.error("ОШИБКА! Не задан тип иконы"); 
			//-- null
			return null; 
		} else {
		}
		//-- тип узла
		switch(di_type) {
			//-- КОНЕЦ СБОРКИ
			case DI_COMPIL_END:
				//-- КОНЕЦ СИЛУЭТА(SI_END)
				case DI_SI_END:
					//-- добавляем комент и код в результат
					if (comment != null)
	res_str += spaces +"//-- " + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					//-- тек. узел
					return cur_node; 
			//-- НАЧАЛО ПРОЦЕДУРЫ
			case DI_PROC_BEG:
				//-- НАЧАЛО ШАМПУРА(SH_BEG)
				case DI_SH_BEG:
					//-- ЧАСТЬ СБОРКИ
					case DI_SUB_COMPIL:
						//-- тек. узел
						return cur_node; 
			//-- неизвестный тип
			default:
				//-- Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ В НАЧАЛЬНОЙ ГРУППЕ 
				str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ di_type + ") В НАЧАЛЬНОЙ ГРУППЕ. !n";
DrakonUtils.error(str); 
				//-- break
				break; 
			//-- ДЕСТВИЕ(ACTION)
			case DI_ACTION:
				//-- ДЕСТВИЕ(AC)
				case DI_AC:
					//-- добавляем коментарий и код в результат
					if(comment != null)
	res_str += spaces + "//-- " + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
					//-- есть выходы?
					if(DrakonUtils.getOutDegree(cur_node) >= 1) {
						//-- для всех выходов
						for(int i2 = 0; i2 < DrakonUtils.getOutDegree(cur_node); i2++) {
							//-- получаем тип выхода
							Edge e = DrakonUtils.getOutEdge(cur_node, i2);
 
							//-- ребро ссылка-указатель?
							if(DrakonUtils.isReferenceEdge(e)) {
							} else {
								//-- node
								node 
								//-- Разбираем начальную группу
								 = parceBegGroup(DrakonUtils.getOutNode(cur_node,i2), _level); 
								//-- node
								return node; 
							}
						}
					} else {
					}
					//-- формируем сообщение о ошибке
					str = "ОШИБКА! У Действия \"" + comment + "\" должено быть выход!\n";
 
					//-- добавляем в результат
					res_str += spaces + str; 
					//-- "ОШИБКА! У Действия ... должен быть выход.
					DrakonUtils.error(str); 
					//-- тек. узел
					return cur_node; 
		}
		//-- null
		return null;



	} 

	//-- Парсер одного шампура
	protected void parceShampur(Vertex node, int  _level) { 
		//-- переменные
		Vertex cur_node;
		String comment;
		String di_type;
		String code;
		String this_comment;
		String spaces = "";
		Vertex next_node; 
 
		//-- в строку пробелов добавляем табуляторы по глубине уровня
		for (int i = 0; i < _level; i++)  
			spaces += "\t";
 
		//-- получаем параметры текущего узла
		cur_node = node;
		code= geReleaseCode(cur_node);
		di_type = DrakonUtils.getIconType(cur_node);
		comment = DrakonUtils.getComment(cur_node);
this_comment = comment; 
 
		//-- -psh- n: 
		DrakonUtils.debug("-psh- n: "+DrakonUtils.getComment(cur_node)); 
		//-- узел НАЧАЛО ШАМПУРА?
		if(di_type.equals(DI_SH_BEG) || di_type.equals(DI_PROC_BEG)) {
		} else {
			//-- узел КОНЕЦ (СИЛУЭТА,СБОРКИ,КЛАССА)?
			if(di_type.equals(DI_SI_END) ||
di_type.equals(DI_COMPIL_END) ||
di_type.equals(DI_CLASS_END)) {
				//-- добавляем в результат комментарий и код если они есть
				if(comment != null)
	res_str += spaces + "//-- " + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
				//-- //--         
				return; 
			} else {
			}
			//-- формируем сообщение о ошибке
			String str = "ОШИБКА! Первый узел должен быть " + DI_SH_BEG + " а не "+ di_type + " икона "+comment+" !";
res_str += spaces + str;
 
			//-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
			DrakonUtils.error(str); 
			//-- //--         
			return; 
		}
		//-- добавляем в результат комментарий и код если они есть
		if(comment != null)
	res_str += spaces + "//-- " + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
		//-- выходных ребер 2?
		if(DrakonUtils.getOutDegree(cur_node) == 2) {
			//-- получаем тип первго выхода
			Vertex v = DrakonUtils.getOutNode(cur_node,0);
di_type = DrakonUtils.getIconType(v);
 
			//-- на первом выходе НАЧАЛО ШАМПУРА или КОНЕЦ СИЛУЭТА?
			if(di_type.equals(DI_SH_BEG) || di_type.equals(DI_PROC_BEG) || di_type.equals(DI_SI_END) || di_type.equals(DI_COMPIL_END)) {
				//-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
				next_node = DrakonUtils.getOutNode(cur_node,0);
cur_node = DrakonUtils.getOutNode(cur_node,1); 
			} else {
				//-- след.узлом будет тот что на первом  выходе, а текущим станет тот что на втром
				next_node = DrakonUtils.getOutNode(cur_node,1);
cur_node = DrakonUtils.getOutNode(cur_node,0); 
			}
		} else {
			//-- выходных ребер 1?
			if(DrakonUtils.getOutDegree(cur_node) == 1) {
				//-- след.узла не будет, а текущим станет тот что на первом
				cur_node = DrakonUtils.getOutNode(cur_node,0);
next_node = null; 
			} else {
				//-- формируем сообщение о ошибке
				String str = "ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2\n";
 
				//-- ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2
				DrakonUtils.error(str); 
				//-- //--         
				return; 
			}
		}
		//-- Разбираем ветку
		Vertex term = parceNext(cur_node, _level + 1); 
		//-- вернулся пустой терминатор?
		if(term == null) {
			//-- формируем сообщение о ошибке
			String str = "ОШИБКА! в шампуре \"" + this_comment + "\" parceNext() вернул пусой терминатор\n";
res_str += str; 
			//-- ОШИБКА! в шампуре ... вернул пусой терминатор
			DrakonUtils.error(str); 
		} else {
			//-- терминатор КОНЕЦ СИЛУЭТА?
			if(DrakonUtils.getIconType(term).equals(DI_SI_END) ||
DrakonUtils.getIconType(term).equals(DI_COMPIL_END)) {
			} else {
				//-- добавляем конец процедуры в результат
				res_str += spaces + "} \n\n"; 
			}
		}
		//-- есть след.узел?
		if(next_node != null) {
			//-- Разбираем след. шамапур
			parceShampur(next_node,_level); 
		} else {
		}
		//-- //--             
		


	} 

	//-- Парсинг следующего узла
	protected Vertex parceNext(Vertex cur_node, int _level) {
/**
 * @param	cur_node
 * @param	res_str
 * @return terminator - последний узел на котором закончилось движение
 */ 
		//-- переменные
		String comment = DrakonUtils.getComment(cur_node);
String di_type = DrakonUtils.getIconType(cur_node);
String code = geReleaseCode(cur_node);
String spaces = "";
Vertex term_yes;
String str;
Vertex node;
Vertex cur_node_d;  
		//-- -pnx- n: 
		DrakonUtils.debug("-pnx- n: "+DrakonUtils.getComment(cur_node)); 
		//-- текущий узел null?
		if(cur_node == null) {
			//-- формируем сообщение о ошибке
			str = "\nОШИБКА! Следующий за узлом \"" + comment + "\" узел отсутствует."; 
			//-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
			DrakonUtils.error(str); 
			//-- null
			return null; 
		} else {
		}
		//-- в строку пробелов добавляем табуляторы по глубине уровня
		for (int i = 0; i < _level; i++)  
	spaces += "\t";
 
		//-- обрабатываем перевод строки в комментариях и коде
		if(comment != null)	
	comment = comment.replace("\n","//-- ");
//if(code != null)	
//	code = code.replace("\n","//-- ");
 
		//-- тип текущего узла не задан?
		if(di_type == null || di_type.length() == 0) {
			//-- в результат добавляем  ПРЕДУПРЕЖДЕНИЕ! не установлен тип узла
			res_str += spaces +"//-- ПРЕДУПРЕЖДЕНИЕ! не установлен тип узла \"" + comment + "\" \n"; 
			//-- добавляем коментарий и код в результат
			if(comment != null) 
	res_str += "\n//-- " + comment + "\n";
if(code != null)	
	res_str += code + "\n"; 
			//-- node
			node 
			//-- Разбираем ветку
			 = parceNext(DrakonUtils.getOutNode(cur_node,0), _level); 
			//-- node
			return node; 
		} else {
		}
		//-- -pnx- n -2-: 
		DrakonUtils.debug("-pnx- -2- n: "+DrakonUtils.getComment(cur_node)); 
		//-- Проверка выходов проходит?
		if(isCheckOutputs(cur_node)) {
		} else {
			//-- формируем сообщение о ошибке
			str = "ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы " + comment +" (тип " + di_type+")";
res_str += str; 
			//-- ОШИБКА! НЕ ПРОШЛА ПРОВЕРКА ВЫХОДОВ для иконы 
			DrakonUtils.error(str); 
		}
		//-- тип узла
		switch(di_type) {
			//-- НАЧАЛО ЦИКЛА(FOR_BEG)
			case DI_FOR_BEG:
				//-- записываем комментарии  и код в результат
				if (comment != null)
	res_str += spaces +"//-- " + comment + "\n";
res_str += spaces + code +"\n"; 
				//-- один выход?
				if(DrakonUtils.getOutDegree(cur_node) == 1) {
				} else {
					//-- формируем сообщение о ошибке
					str = "ОШИБКА! У Начала цикла \"" + comment + "\" должен быть один выход!\n";
 
					//-- ОШИБКА! У Начала цикла ... должен быть один выход!
					DrakonUtils.error(str); 
					//-- записываем сообщение о ошибке в результат
					res_str += str; 
					//-- тек. узел
					return cur_node; 
				}
				//-- делаем текущим выход.узел 
				cur_node = DrakonUtils.getOutNode(cur_node,0); 
				//-- терминатор
				Vertex term 
				//-- Разбираем ветку
				= parceNext(cur_node, _level + 1); 
				//-- записываем в результат "}"
				res_str += spaces +"}\n"; 
				//-- вернулся не КОНЕЦ ЦИКЛА?
				if(DrakonUtils.getIconType(term).equals(DI_FOR_END)) {
				} else {
					//-- формируем сообщение о ошибке
					str = "ОШИБКА! У Цикла \"" + comment + "\" нет конца!\n"; 
					//-- ОШИБКА! У Цикла ... нет конца!
					DrakonUtils.error(str); 
					//-- записываем сообщение о ошибке в результат
					res_str += str; 
					//-- терминатор
					return term; 
				}
				//-- один выход у терминатора?
				if(DrakonUtils.getOutDegree(term) == 1) {
				} else {
					//-- формируем сообщение о ошибке
					str = "ОШИБКА! У Цикла \"" + comment + "\" нет продолжения пути!\n"; 
					//-- ОШИБКА: У Цикла ... нет продолжения пути.
					DrakonUtils.error(str); 
					//-- записываем сообщение о ошибке в результат
					res_str += str;
 
					//-- терминатор
					return term; 
				}
				//-- делаем текущим выход терминатора
				cur_node = DrakonUtils.getOutNode(term,0);  
				//-- node
				node 
				//-- Разбираем ветку
				 = parceNext(cur_node, _level); 
				//-- node
				return node; 
			//-- DEFAULT
			case DI_DEFAULT:
				//-- записываем default выражение в результат
				res_str += spaces +"//-- " + comment + "\n";
res_str += spaces +"default:\n"; 
				//-- делаем текущим выход.узел 
				cur_node = DrakonUtils.getOutNode(cur_node,0);  
				//-- term_yes
				term_yes 
				//-- Разбираем ветку
				= parceNext(cur_node, _level + 1); 
				//-- терминатор
				return term_yes; 
			//-- неизвестный тип
			default:
				//-- формируем сообщение о ошибке
				str = "Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ \"" + comment + "\" ("+ di_type + ")!n"; 
				//-- Ошибка! НЕИЗВЕСТНЫЙ ТИП ИКОНЫ ...
				DrakonUtils.error(str); 
				//-- break
				break; 
			//-- КОНЕЦ УСЛОВИЯ(EI)
			case DI_EI:
				//-- тек. узел
				return cur_node; 
			//-- CASE
			case DI_CASE:
				//-- записываем case выражение в результат
				res_str += spaces +"//-- " + comment + "\n";
res_str += spaces +"case " + code + ":\n"; 
				//-- делаем текущим выход.узел 
				cur_node = DrakonUtils.getOutNode(cur_node,0);  
				//-- term_yes
				term_yes 
				//-- Разбираем ветку
				= parceNext(cur_node, _level + 1); 
				//-- терминатор
				return term_yes; 
			//-- ДЕСТВИЕ(ACTION)
			case DI_ACTION:
				//-- ДЕСТВИЕ(AC)
				case DI_AC:
					//-- ВОЗВРАТ
					case DI_RETURN:
						//-- ПРЕКРАЩЕНИЕ
						case DI_BREAK:
							//-- ВСТАВКА
							case DI_INSERT:
								//-- ВЫВОД
								case DI_OUTPUT:
									//-- добавляем коментарий и код в результат
									if(comment != null)
	res_str += spaces + "//-- " + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
									//-- есть выходы?
									if(DrakonUtils.getOutDegree(cur_node) >= 1) {
										//-- для всех выходов
										for(int i2 = 0; i2 < DrakonUtils.getOutDegree(cur_node); i2++) {
											//-- получаем тип выхода
											Edge e = DrakonUtils.getOutEdge(cur_node, i2);
 
											//-- ребро ссылка-указатель?
											if(DrakonUtils.isReferenceEdge(e)) {
											} else {
												//-- node
												node 
												//-- Разбираем ветку
												 = parceNext(DrakonUtils.getOutNode(cur_node,i2), _level); 
												//-- node
												return node; 
											}
										}
									} else {
									}
									//-- формируем сообщение о ошибке
									str = "ОШИБКА! У Действия \"" + comment + "\" должено быть выход!\n";
 
									//-- добавляем в результат
									res_str += spaces + str; 
									//-- "ОШИБКА! У Действия ... должен быть выход.
									DrakonUtils.error(str); 
									//-- тек. узел
									return cur_node; 
			//-- КОНЕЦ ЦИКЛА(FOR_END)
			case DI_FOR_END:
				//-- тек. узел
				return cur_node; 
			//-- КОНЕЦ ПРОЦЕДУРЫ
			case DI_PROC_END:
				//-- КОНЕЦ ШАМПУРА(SH_END)
				case DI_SH_END:
					//-- добавляем комент и код в результат
					if (comment != null)
	res_str += spaces +"//-- " + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					//-- тек. узел
					return cur_node; 
			//-- НАЧАЛО ПРОЦЕДУРЫ
			case DI_PROC_BEG:
				//-- НАЧАЛО ШАМПУРА(SH_BEG)
				case DI_SH_BEG:
					//-- тек. узел
					return cur_node; 
			//-- ВЫБОР(SWITCH)
			case DI_SW:
				//-- записываем switch выражение в результат
				res_str += spaces +"//-- " + comment + "\n";
res_str += spaces +"switch(" + code + ") {\n"; 
				//-- сбрасываем терминатор
				term_yes = null; 
				//-- у узла switch один выход и это Точка?
				if(DrakonUtils.getOutDegree(cur_node)==1 && DrakonUtils.getIconType(DrakonUtils.getOutNode(cur_node, 0)).equals(DrakonUtils.DI_EI)) {
					//-- делаем текущим узел Точка
					cur_node = DrakonUtils.getOutNode(cur_node, 0); 
				} else {
				}
				//-- Обрабатываем все case ветки
				for(int is2 = 0; is2 < DrakonUtils.getOutDegree(cur_node); is2++) {
					//-- получаем данные очередного рыходного узла
					cur_node_d = DrakonUtils.getOutNode(cur_node,is2);

code = geReleaseCode(cur_node_d);
di_type = DrakonUtils.getIconType(cur_node_d); 
					//-- это  CASE или DEFAULT?
					if(di_type.equals(DI_CASE) 
|| di_type.equals(DI_DEFAULT)) {
						//-- term_yes
						term_yes 
						//-- Разбираем ветку
						= parceNext(cur_node_d, _level + 1); 
					} else {
						//-- формируем сообщение о ошибке
						str = "Ошибка! У Иконы ВЫБОР \"" + comment + "\" все ребра должны вести к Case иконам!"; 
						//-- Ошибка! У Иконы ВЫБОР ... все ребра должны вести к Case иконам!
						DrakonUtils.error(str); 
						//-- записываем в результат сообщение
						res_str += spaces + str; 
						//-- null
						return null; 
					}
				}
				//-- записываем в результат конец "}"
				res_str += spaces +"}\n"; 
				//-- терминатор пуст?
				if(term_yes == null) {
					//-- null
					return null; 
				} else {
				}
				//-- У терминатора 1 выход?
				if(DrakonUtils.getOutDegree(term_yes) == 1) {
					//-- текущий узел выход из терминатора
					cur_node = DrakonUtils.getOutNode(term_yes,0); 
					//-- node
					node 
					//-- Разбираем ветку
					 = parceNext(cur_node, _level); 
					//-- node
					return node; 
				} else {
					//-- терминатор КОНЕЦ ШАМПУРА?
					if(di_type.equals(DI_SH_END) || di_type.equals(DI_PROC_END)) {
					} else {
						//-- формируем предупреждение
						str = "//-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки \"" + comment +"\" имеет "+ DrakonUtils.getOutDegree(term_yes) +" выходов. Должен быть один. \n";
 
						//-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки ... имеет ... выходов. Должен быть один.
						DrakonUtils.message(str); 
					}
					//-- терминатор
					return term_yes; 
				}
			//-- УСЛОВИЕ(IF)
			case DI_IF:
				//-- переменные
				//{
//String di_type_edge;
int i = 0; 
				//-- записываем if выражение в результат {
				res_str += spaces +"//-- " + comment + "\n";
res_str += spaces +"if(" + code + ") {\n"; 
				//-- два выхода?
				if(DrakonUtils.getOutDegree(cur_node) == 2) {
				} else {
					//-- формируем сообщение о ошибке
					str = "ОШИБКА! У Развилки \"" + comment + "\" должено быть два выхода!\n";
res_str += spaces + str; 
					//-- ОШИБКА! У Развилки ... должено быть два выхода!
					DrakonUtils.error(str); 
				}
				//-- берем первый выход, определяем его тип
				Edge edge = DrakonUtils.getOutEdge(cur_node,0);
i = 0; 
				//-- тип не Да?
				if(!DrakonUtils.isEdgeYes(edge)) {
					//-- не тот конец, берем следующий
					edge = DrakonUtils.getOutEdge(cur_node,1);
i = 1; 
				} else {
				}
				//-- тип не Да?
				if(!DrakonUtils.isEdgeYes(edge)) {
					//-- формируем сообщение о ошибке
					str = "Ошибка! У Развилки \"" + comment + "\" должен быть Да конец!"; 
					//-- Ошибка! У Развилки ... должен быть Да конец!
					DrakonUtils.error(str); 
					//-- null
					return null; 
				} else {
				}
				//-- обрабатываем ветку Да
				cur_node_d = DrakonUtils.getOutNode(cur_node,i);
term_yes = parceNext(cur_node_d, _level + 1);
 
				//-- записываем в результат "} else {"
				res_str += spaces +"} else {\n";
 
				//-- обрабатываем ветку Нет
				if (i == 0) {
	cur_node_d = DrakonUtils.getOutNode(cur_node,1);
} else {
	cur_node_d = DrakonUtils.getOutNode(cur_node,0);
}
Vertex term_no = parceNext(cur_node_d, _level+1);
 
				//-- записываем в результат "}"
				res_str += spaces +"}\n";
 
				//-- выбираем куда дальше идти 
				if (DrakonUtils.getIconType(term_yes).equals(DI_SH_END) &&
!DrakonUtils.getIconType(term_no).equals(DI_SH_END)) 
term_yes = term_no; 
				//-- первый выход КОНЕЦ ШАМПУРА а второй нет?
				if(DrakonUtils.getIconType(term_yes).equals(DI_SH_END) &&
!DrakonUtils.getIconType(term_no).equals(DI_SH_END)) {
					//-- делаем текущим второй выход
					term_yes = term_no; 
				} else {
				}
				//-- выбранный терминатор null?
				if(term_yes == null) {
					//-- null
					return null; 
				} else {
				}
				//-- выбранный терминатор КОНЕЦ ЦИКЛА?
				if(DrakonUtils.getIconType(term_yes).equals(DI_FOR_END)) {
					//-- терминатор
					return term_yes; 
				} else {
				}
				//-- У терминатора 1 выход?
				if(DrakonUtils.getOutDegree(term_yes) == 1) {
					//-- делаем текущим узел выхода из терминатора
					cur_node = DrakonUtils.getOutNode(term_yes,0); 
					//-- node
					node 
					//-- Разбираем ветку
					 = parceNext(cur_node, _level); 
					//-- node
					return node; 
				} else {
					//-- терминатор КОНЕЦ ШАМПУРА?
					if(di_type.equals(DI_SH_END) || di_type.equals(DI_PROC_END)) {
					} else {
						//-- формируем предупреждение
						str = "//-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки \"" + comment +"\" имеет "+ DrakonUtils.getOutDegree(term_yes) +" выходов. Должен быть один. \n";
 
						//-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки ... имеет ... выходов. Должен быть один.
						DrakonUtils.message(str); 
					}
					//-- терминатор
					return term_yes; 
				}
			//-- КОНЕЦ СБОРКИ
			case DI_COMPIL_END:
				//-- КОНЕЦ СИЛУЭТА(SI_END)
				case DI_SI_END:
					//-- добавляем комент и код в результат
					if (comment != null)
	res_str += spaces +"//-- " + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
					//-- тек. узел
					return cur_node; 
		}
		//-- null
		return null;



	} 

	//-- Получение рабочего кода узла с маркером
	public String geReleaseCode(Vertex node) { 
		//-- икона из тех что без кода?
		if(DrakonUtils.getIconType(node).equals(DI_EI) ||
DrakonUtils.getIconType(node).equals(DI_FOR_END)) {
			//-- ""
			return ""; 
		} else {
		}
		//-- У иконы нет Коментария?
		if(DrakonUtils.getComment(node).equals("")) {
			//-- ""
			return ""; 
		} else {
		}
		//-- переменная
		String code = ""; 
		//-- реализация установлена?
		if(CURRENT_RELEASE != null && CURRENT_RELEASE.length() != 0) {
			//-- для всех входов
			for(int i = 0; i < DrakonUtils.getInDegree(node); i++) {
				//-- получаем входной узел
				Vertex in_node = DrakonUtils.getInNode(node,i); 
				//-- это узел текущей реализации?
				if(DrakonUtils.getIconType(in_node).equals(CURRENT_RELEASE)) {
					//-- получаем код из вход. узла
					code = DrakonUtils.getCode(in_node); 
					//-- есть маркер?
					if(DrakonUtils.getCodeMark(in_node).length() > 0) {
						//-- добавляем к коду маркер
						
//<DG2J code_mark="n2304:SPECIAL_DG2J_MARK" >
code = "\n//<DG2J code_mark=\"n"+ (String) in_node.getId() + ":" + DrakonUtils.getCodeMark(in_node) + "\" >\n"
+ code + "\n"
+ "//</DG2J>\n";

//</DG2J>
 
					} else {
					}
					//-- код
					return code; 
				} else {
				}
			}
			//-- ОТСУТСТВУЕТ КОД РЕАЛИЗАЦИИ у иконы ... !
			String str = "ОТСУТСТВУЕТ КОД РЕАЛИЗАЦИИ у иконы ... \"" + DrakonUtils.getComment(node) + "\" \n";
DrakonUtils.error(str); 
			//-- ...
			return "ОТСУТСТВУЕТ КОД РЕАЛИЗАЦИИ."; 
		} else {
			//-- получаем код из текущего узла
			code = DrakonUtils.getCode(node); 
			//-- есть маркер?
			if(DrakonUtils.getCodeMark(node).length() > 0) {
				//-- добавляем к коду маркер
				
//<DG2J code_mark="n2278:SPECIAL_DG2J_MARK" >
code = "\n//<DG2J code_mark=\"n"+ (String) node.getId() + ":" + DrakonUtils.getCodeMark(node) + "\" >\n"
+ code + "\n"
+ "//</DG2J>\n";

//</DG2J>
 
			} else {
			}
		}
		//-- код
		return code;



	} 

	//-- Получение рабочего кода узла без маркера
	public String getCleanReleaseCode(Vertex node) { 
		//-- икона из тех что без кода?
		if(DrakonUtils.getIconType(node).equals(DI_EI) ||
DrakonUtils.getIconType(node).equals(DI_FOR_END)) {
			//-- ""
			return ""; 
		} else {
		}
		//-- У иконы нет Коментария?
		if(DrakonUtils.getComment(node).equals("")) {
			//-- ""
			return ""; 
		} else {
		}
		//-- переменная
		String code = ""; 
		//-- реализация установлена?
		if(CURRENT_RELEASE != null && CURRENT_RELEASE.length() != 0) {
			//-- для всех входов
			for(int i = 0; i < DrakonUtils.getInDegree(node); i++) {
				//-- получаем входной узел
				Vertex in_node = DrakonUtils.getInNode(node,i); 
				//-- это узел текущей реализации?
				if(DrakonUtils.getIconType(in_node).equals(CURRENT_RELEASE)) {
					//-- получаем код из вход. узла
					code = DrakonUtils.getCode(in_node); 
					//-- код
					return code; 
				} else {
				}
			}
			//-- ОТСУТСТВУЕТ КОД РЕАЛИЗАЦИИ у иконы ... !
			String str = "ОТСУТСТВУЕТ КОД РЕАЛИЗАЦИИ у иконы ... \"" + DrakonUtils.getComment(node) + "\" \n";
DrakonUtils.error(str); 
			//-- ...
			return "ОТСУТСТВУЕТ КОД РЕАЛИЗАЦИИ."; 
		} else {
			//-- получаем код из текущего узла
			code = DrakonUtils.getCode(node); 
		}
		//-- код
		return code;



	} 

	//-- Проверка выходов иконы
	public boolean isCheckOutputs(Vertex node) { 
		//-- переменные
		Vertex cur_node;
Object data;
String comment;
String di_type;
String code;
int type;
//Edge edge;
Vertex next_node;
int level = 0;
String str = "";
Vertex out_1 = null;
Vertex out_2 = null; 
		//-- получаем параметры текущего узла
		cur_node = node;
code = geReleaseCode(cur_node);
di_type = DrakonUtils.getIconType(cur_node);
comment = DrakonUtils.getComment(cur_node); 
		//-- икона из тех что без выхода?
		if(di_type.equals(DI_SH_END) 
|| di_type.equals(DI_PROC_END) 
|| di_type.equals(DI_SI_END) 
|| di_type.equals(DI_CLASS_END)) {
			//-- выходных узлов 0?
			if(DrakonUtils.getOutDegree(cur_node) == 0) {
			} else {
				//-- НАРУШЕНИЕ ПРАВИЛА! У иконы ... не один выход.
				str = "НАРУШЕНИЕ ПРАВИЛА! У иконы \"" + comment + "\"  не один выход.\n";
DrakonUtils.error(str); 
				//-- фальшь
				return false; 
			}
		} else {
			//-- икона из тех что с одним выходом?
			if(di_type.equals(DI_ACTION) 
|| di_type.equals(DI_AC) 
|| di_type.equals(DI_EI) 
|| di_type.equals(DI_DG_BEG)
|| di_type.equals(DI_CASE)
|| di_type.equals(DI_DEFAULT)
|| di_type.equals(DI_FOR_BEG)
|| di_type.equals(DI_FOR_END)
|| di_type.equals(DI_BREAK)
|| di_type.equals(DI_OUTPUT)
|| di_type.equals(DI_INSERT)) {
				//-- выходных узлов 1?
				if(DrakonUtils.getOutDegree(cur_node) == 1) {
				} else {
					//-- НАРУШЕНИЕ ПРАВИЛА! У иконы ... не один выход.
					str = "НАРУШЕНИЕ ПРАВИЛА! У иконы \"" + comment + "\"  не один выход.\n";
DrakonUtils.error(str); 
					//-- фальшь
					return false; 
				}
				//-- получам первый выход
				out_1 = DrakonUtils.getOutNode(cur_node,0); 
				//-- тип узла
				switch(di_type) {
					//-- CASE
					case DI_CASE:
						//-- DEFAULT
						case DI_DEFAULT:
							//-- тип из группы RG_B  или ВОЗВРАТ или НАЧАЛО ЦИКЛА или ВАРИАНТ?
							if(DrakonUtils.getIconType(out_1).equals(DI_ACTION) 
|| DrakonUtils.getIconType(out_1).equals(DI_SW) 
|| DrakonUtils.getIconType(out_1).equals(DI_IF) 
|| DrakonUtils.getIconType(out_1).equals(DI_RETURN)
|| DrakonUtils.getIconType(out_1).equals(DI_FOR_BEG)
|| DrakonUtils.getIconType(out_1).equals(DI_CASE)
|| DrakonUtils.getIconType(out_1).equals(DI_BREAK)
|| DrakonUtils.getIconType(out_1).equals(DI_OUTPUT)
|| DrakonUtils.getIconType(out_1).equals(DI_INSERT)) {
							} else {
								//-- НАРУШЕНИЕ ПРАВИЛА Вариант! У иконы ... неверный тип выхода.
								str = "НАРУШЕНИЕ ПРАВИЛА Вариант! У иконы \"" + comment + "\"  неверный тип выхода ("+ DrakonUtils.getIconType(out_1) +").\n";
DrakonUtils.error(str); 
							}
							//-- break
							break; 
					//-- ДЕСТВИЕ(ACTION)
					case DI_ACTION:
						//-- ДЕСТВИЕ(AC)
						case DI_AC:
							//-- тип ЧАСТЬ СБОРКИ?
							if(DrakonUtils.getIconType(out_1).equals(DI_SUB_COMPIL)) {
								//-- истина
								return true; 
							} else {
							}
							//-- КОНЕЦ УСЛОВИЯ(EI)
							case DI_EI:
								//-- ПРЕКРАЩЕНИЕ
								case DI_BREAK:
									//-- ВСТАВКА
									case DI_INSERT:
										//-- ВЫВОД
										case DI_OUTPUT:
											//-- тип ВАРИАНТ?
											if(DrakonUtils.getIconType(out_1).equals(DI_CASE)) {
												//-- истина
												return true; 
											} else {
											}
											//-- тип из группы RG_B или ТЕРМИНАТОР?
											if(DrakonUtils.getIconType(out_1).equals(DI_ACTION) 
|| DrakonUtils.getIconType(out_1).equals(DI_SW) 
|| DrakonUtils.getIconType(out_1).equals(DI_IF) 
|| DrakonUtils.getIconType(out_1).equals(DI_EI) 
|| DrakonUtils.getIconType(out_1).equals(DI_BREAK)
|| DrakonUtils.getIconType(out_1).equals(DI_OUTPUT)
|| DrakonUtils.getIconType(out_1).equals(DI_INSERT)) {
											} else {
												//-- тип из группы RG_С?
												if(DrakonUtils.getIconType(out_1).equals(DI_PROC_END) 
|| DrakonUtils.getIconType(out_1).equals(DI_SH_END) 
|| DrakonUtils.getIconType(out_1).equals(DI_RETURN) ) {
												} else {
													//-- тип из группы RG_D?
													if(DrakonUtils.getIconType(out_1).equals(DI_FOR_BEG) 
|| DrakonUtils.getIconType(out_1).equals(DI_FOR_END)) {
													} else {
														//-- тип НАЧАЛО ПРОЦЕДУРЫ?
														if(DrakonUtils.getIconType(out_1).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_1).equals(DI_SH_BEG)) {
														} else {
															//-- НАРУШЕНИЕ ПРАВИЛА Действия! У иконы ... неверный тип выхода.
															str = "НАРУШЕНИЕ ПРАВИЛА Действия! У иконы \"" + comment + "\"  неверный тип выхода ("+ DrakonUtils.getIconType(out_1)+").\n";
DrakonUtils.error(str); 
														}
													}
												}
											}
											//-- break
											break; 
					//-- другой тип
					default:
						//-- -1- D- Проверка правил выходов не выполняется  
						DrakonUtils.message("Проверка правил выходов не выполняется  "+DrakonUtils.getIconType(cur_node) + "икона:" + DrakonUtils.getComment(cur_node) + "\n"); 
						//-- break
						break; 
				}
			} else {
				//-- тип узла
				switch(di_type) {
					//-- ЧАСТЬ СБОРКИ
					case DI_SUB_COMPIL:
						//-- выходных узлов 2?
						if(DrakonUtils.getOutDegree(cur_node) == 2) {
							//-- берем текущие выходы по порядку
							out_1 = DrakonUtils.getOutNode(cur_node,0);
out_2 = DrakonUtils.getOutNode(cur_node,1); 
							//-- первый выход ЧАСТЬ СБОРКИ или КОНЕЦ СБОРКИ?
							if(DrakonUtils.getIconType(out_1).equals(DI_SUB_COMPIL) || DrakonUtils.getIconType(out_1).equals(DI_SI_END)) {
							} else {
								//-- меняем местами выходы
								out_1 = DrakonUtils.getOutNode(cur_node,1);
out_2 = DrakonUtils.getOutNode(cur_node,0); 
								//-- первый выход ЧАСТЬ СБОРКИ или КОНЕЦ СБОРКИ?
								if(DrakonUtils.getIconType(out_1).equals(DI_SUB_COMPIL) || DrakonUtils.getIconType(out_1).equals(DI_SI_END)) {
								} else {
									//-- НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы ЧАСТЬ СБОРКИ ... неправильный тип выхода!
									str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки!  У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
									//-- фальшь
									return false; 
								}
							}
							//-- второй выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ?
							if(DrakonUtils.getIconType(out_2).equals(DI_SH_BEG)
|| DrakonUtils.getIconType(out_2).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_2).equals(DI_ACTION) ) {
							} else {
								//-- НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы ЧАСТЬ СБОРКИ ... неправильный тип выхода!
								str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки!  У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
								//-- фальшь
								return false; 
							}
						} else {
							//-- НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы ЧАСТЬ СБОРКИ ... должно быть 2 выхода
							str = "НАРУШЕНИЕ ПРАВИЛА Часть сборки! У иконы \"" + comment + "\" должно быть два выхода!\n";
DrakonUtils.error(str); 
							//-- фальшь
							return false; 
						}
						//-- break
						break; 
					//-- другой тип
					default:
						//-- break
						break; 
					//-- НАЧАЛО СИЛУЭТА
					case DI_SI_BEG:
						//-- СБОРКА
						case DI_COMPIL_BEG:
							//-- выходных узлов 1?
							if(DrakonUtils.getOutDegree(cur_node) == 1) {
								//-- получам первый выход
								out_1 = DrakonUtils.getOutNode(cur_node,0); 
								//-- тип вых.узла НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ?
								if(DrakonUtils.getIconType(out_1).equals(DI_SH_BEG)
|| DrakonUtils.getIconType(out_1).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_1).equals(DI_ACTION) ) {
								} else {
									//-- НАРУШЕНИЕ ПРАВИЛА Сборка-1! У иконы СБОРКА ... неправильный тип выхода!
									str = "НАРУШЕНИЕ ПРАВИЛА Сборка-1! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
									//-- фальшь
									return false; 
								}
								//-- истина
								return true; 
							} else {
								//-- выходных узлов 2?
								if(DrakonUtils.getOutDegree(cur_node) == 2) {
									//-- текущие выходы по порядку
									out_1 = DrakonUtils.getOutNode(cur_node,0);
out_2 = DrakonUtils.getOutNode(cur_node,1); 
									//-- первый выход ЗАПИСЬ В ФАЙЛ?
									if(DrakonUtils.getIconType(out_1).equals(DI_WR_RES_FILE)) {
										//-- второй выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ?
										if(DrakonUtils.getIconType(out_2).equals(DI_SH_BEG)
|| DrakonUtils.getIconType(out_2).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_2).equals(DI_ACTION) ) {
										} else {
											//-- НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы СБОРКА ... неправильный тип выхода!
											str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
											//-- фальшь
											return false; 
										}
									} else {
										//-- второй выход ЗАПИСЬ В ФАЙЛ?
										if(DrakonUtils.getIconType(out_2).equals(DI_WR_RES_FILE)) {
											//-- первый выход НАЧАЛО ПРОЦЕДУРЫ или ДЕЙСТВИЕ?
											if(DrakonUtils.getIconType(out_1).equals(DI_SH_BEG)
|| DrakonUtils.getIconType(out_1).equals(DI_PROC_BEG) 
|| DrakonUtils.getIconType(out_1).equals(DI_ACTION) ) {
											} else {
												//-- НАРУШЕНИЕ ПРАВИЛА Сборка-1! У иконы СБОРКА ... неправильный тип выхода!
												str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы \"" + comment + "\" неправильный тип выхода!\n";
DrakonUtils.error(str); 
												//-- фальшь
												return false; 
											}
										} else {
											//-- НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы СБОРКА ... один из двух выходов должен быть ЗАПИСЬ В ФАЙЛ
											str = "НАРУШЕНИЕ ПРАВИЛА Сборка-2! У иконы НАЧАЛО СБОРКИ \"" + comment + "\" один из двух выходов должен быть ЗАПИСЬ В ФАЙЛ\n";
DrakonUtils.error(str); 
											//-- фальшь
											return false; 
										}
									}
								} else {
									//-- ОШИБКА! У иконы СБОРКА ... не оди и не два выхода!
									str = "ОШИБКА! У иконы \"" + comment + "\" не оди и не два выхода!\n";
DrakonUtils.error(str); 
									//-- фальшь
									return false; 
								}
							}
							//-- break
							break; 
				}
			}
		}
		//-- истина
		return true;



	} 

	//-- Парсер схемы ДРАКОНА полученной из графа yEd
	public void parseDrakon() { 
		//-- строим код
		//-- //--             
		


	} 

	//-- Отрисовка графа
	protected void build_vis(Object data) { 
		//-- строим код
		//-- //--             
		


	} 

	//-- Масштабирование картинки
	public void zoomAll() { 
		//-- строим код
		//-- //--             
		


	} 

	//-- Загрузка данных
	public void loadData(String query) { 
		//-- строим код
		//-- //--             
		


	} 

	//-- Конфигурация листнеров
	private void configListeners() { 
		//-- строим код
		//-- //--             
		


	} 

	//-- Обработка события "загрузка завершена"
	private void onLoadingFinesh() { 
		//-- строим код
		//-- //--             
		


	} 

	//-- Отрисовка графа загруженного функцией loadData()
	public void buildVis() { 
		//-- строим код
		//-- //--             
		


	} 

	//-- 
            
	} //-- конец класса 

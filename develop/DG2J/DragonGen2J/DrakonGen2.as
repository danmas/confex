
//-- Класс DrakonGen2
	//-- упоминание о DrakonGen2
	/**
  * Этот текст сгенерирован программой DrakonGen2
  * @author Erv +
*/
 
	//-- package//-- imports
	package drakongen 
{
	
	import flare.query.Not;
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.net.URLLoader;
	import flash.net.URLRequest;

	import flare.vis.data.EdgeSprite;
	import flare.vis.data.NodeSprite;
	import flare.vis.events.VisualizationEvent;
	import flare.vis.operator.layout.CircleLayout;
	import flare.vis.data.Data;
	import flare.data.DataSet;
	import flare.vis.Visualization;
	
	import drakongen.utils.FileUtils;
 
	//-- class DrakonGen2
	 public class DrakonGen2 { 
	//-- константы
	public const DI_EXT_NEXT:String = "next";

public const DI_DG_BEG:String = "DG_BEG";
public const DI_SI_BEG:String = "SI_BEG";
public const DI_SI_END:String = "SI_END";
public const DI_SH_BEG:String = "SH_BEG";
public const DI_SH_END:String = "SH_END"; 
public const DI_WR_RES_FILE:String = "WR_RES_FILE";

public const DI_AC:String = "AC";		
public const DI_ID:String = "ID";		
public const DI_IR:String = "IR";		
//TODO: удалить DI_ID и DI_IR
public const DI_IF:String = "IF";		
public const DI_RY:String = "RY";		
public const DI_DN:String = "DN";		
public const DI_EI:String = "EI";		
public const DI_UK:String = "UK";		
public const DI_EQL:String = "EQL";		
public const DI_EQR:String = "EQR";		
public const DI_FOR_BEG:String = "FOR_BEG";		
public const FOR_EACH_BEG:String = "FOR_EACH_BEG";		
public const DI_FOR_END:String = "FOR_END";		
public const DI_REF:String = "REF";

public const DI_CASE:String = "CASE";
public const DI_SW:String = "SWITCH";

public const RELEASE_TYPE_CODE_JAVA:String = "CODE_JAVA";
public const RELEASE_TYPE_CODE_AS:String = "CODE_AS"; 
	//-- переменные
	protected var res_str:String = "";
protected var load_finish:Boolean = false;
protected var graph_data;

protected var CURRENT_RELEASE:String = RELEASE_TYPE_CODE_AS;  
	//-- Конструктор
	public function DrakonGen2() { 
		//-- //--             
	} //-- конец процедуры


	//-- Парсер схемы
	protected function parse_drakon(gd:Data):String {
var data:Object;
var descr:String;
var di_type:String;
res_str = "";
 
		//-- Проходим по всем узлам
		for each(var n:NodeSprite in gd.nodes)  {
			//-- получаем тип узла
			data = n.data;
di_type = data["url"];
 
			//-- узел НАЧАЛО?
			if(di_type == DI_DG_BEG) {
				//-- извлекаем из Начало тип реальности
				data = n.data;
CURRENT_RELEASE = data["description"]; 
				//-- ---
				message("--->Текущая реальность: " + CURRENT_RELEASE); 
				//-- у текущего узла один выход?
				if(n.outDegree == 1) {
					//-- теперь текущий узел тот на который указывает выход
					n = n.getOutNode(0);
 
					//-- ---
					message("---> Разбираем силуэт "); 
					//-- Разбираем диаграмму
					parseSiluet(n); 
				} else {
					//-- у текущего узла нет выходов?
					if(n.outDegree == 0) {
					} else {
						//-- ОШИБКА! У иконы Начало ...//--  выходов!
						message("ОШИБКА! У иконы Начало \"" + n.outDegree + "\" выходов!"); 
						//-- null
						return null; 
					}
				}
			} else {
			}
		}
		//-- -- Схема построена
		message("//-- Схема построена\n\n " + res_str); 
		//-- результирующая строка
		return res_str;

	} //-- конец процедуры


	//-- Парсер одного силуэта
	public function parseSiluet(node:NodeSprite):void {
/**
 * Парсер одного силуэта ДРАКОНА 
 * @param	var graph_data
 */
 
		//-- переменные
		var cur_node:NodeSprite;
var data:Object;
var comment:String;
var di_type:String;
var code:String;
var type:int;
var edge:EdgeSprite;
var next_node:NodeSprite;
var level:int = 0;	

 
		//-- получаем параметры текущего узла
		cur_node = node;
data = cur_node.data;
code = geReleaseCode(cur_node);
di_type = data["url"];
comment = data["NodeLabel"];
 
		//-- -psi- n: 
		//-- узел ЗАПИСЬ В ФАЙЛ?
		if(di_type == DI_WR_RES_FILE) {
			//-- имя файла = из иконы ЗАПИСЬ В ФАЙЛ
			var file_name= code;
			//-- Имя файла задано?
			if(file_name != null) {
			} else {
				//-- формируем сообщение о ошибке
				var str = "ОШИБКА. В иконе  ЗАПИСЬ ФАЙЛА не задано имя выходного файла.\n";
res_str += spaces + str;
 
				//-- ОШИБКА. В иконе  ЗАПИСЬ ФАЙЛА не задано имя выходного файла.
				message(str); 
				//-- имя файла = временный файл
				var file_name= "tmp.as";
			}
			//-- ----
			message("----> Записываем файл " + code + "\n"); 
			//-- Запись в файл//-- 
			FileUtils.fileWrite(fail_name, res_str); 
			//-- очищаем результат
			res_str = "";
 
			//-- у тек.узла один выход?
			if(getOutDegree(cur_node) == 1) {
				//-- делаем текущим первый выход и получаем его параметры
				cur_node = getOutNode(cur_node,0);
code = geReleaseCode(cur_node);
di_type = getIconType(cur_node);
comment = getComment(cur_node); 
			} else {
				//-- у тек.узла нет выходов?
				if(getOutDegree(cur_node) == 0) {
					//-- //--         
					return; 
				} else {
					//-- формируем сообщение о ошибке
					var str = "ОШИБКА! У иконы " + comment + " записи в файл \"" + getOutDegree(cur_node) + "\" выходов!\n";
res_str += str;

 
					//-- ОШИБКА! У иконы ... записи в файл ... выходов!
					message(str); 
					//-- //--         
					return; 
				}
			}
		} else {
		}
		//-- узел НАЧАЛО СИЛУЭТА?
		if(di_type == DI_SI_BEG) {
		} else {
			//-- формируем сообщение о ошибке
			var str:String = "ОШИБКА! Первый узел шампура должен быть \"" + DI_SI_BEG + "\"!\n"
+ "А узел " + comment +" имеет тип " + di_type;
res_str += str;
 
			//-- ОШИБКА! Первый узел шампура должен быть НАЧАЛО СИЛУЭТА
			message(str); 
			//-- //--         
			return; 
		}
		//-- меняем в комментарии перевод сроки на //-- и добавляем его в результат
		var pattern:RegExp = /\n/g;	
if(comment != null)	
	comment = comment.replace(pattern, "\n" + "//-- ");

if(comment != null) 
	res_str += "\n//-- " + comment + "\n";
if(code != null)	
	res_str += code + "\n";
 
		//-- выходных ребер не 1 и не 2?
		if(cur_node.outDegree != 1 && cur_node.outDegree != 2) {
			//-- формируем сообщение о ошибке
			str = "ОШИБКА! У иконы Начало Силуэта \"" + cur_node.outDegree + "\" выходов!\n"
+ " Должно быть 1 или 2.\n";
 
			//-- ОШИБКА! У иконы Начало Силуэта  Должно быть 1 или 2 выхода
			message(str); 
			//-- //--         
			return; 
		} else {
		}
		//-- выходных ребер 2?
		if(cur_node.outDegree == 2) {
			//-- получаем тип первго выхода
			edge = cur_node.getOutEdge(0);
di_type = edge.target.data["url"];
 
			//-- на первом выходе ЗАПИСЬ В ФАЙЛ?
			if(di_type == DI_WR_RES_FILE) {
				//-- след.узлом будет тот что на первом выходе, а текущим станет тот что на втором
				next_node = cur_node.getOutNode(0);
cur_node = cur_node.getOutNode(1);
 
			} else {
				//-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
				next_node = cur_node.getOutNode(1);
cur_node = cur_node.getOutNode(0);

 
			}
		} else {
			//-- ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода
			message("ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода"); 
			//-- //--         
			return; 
		}
		//-- тип тек.узла НАЧАЛО ШАМПУРА?
		if(cur_node.data["url"] == DI_SH_BEG) {
		} else {
			//-- Разбираем ветку
			cur_node = parceNext(cur_node, level + 1); 
		}
		//-- Разбираем шампур
		parceShampur(cur_node, level + 1);
 
		//-- есть след.узел?
		if(next_node != null) {
			//-- Разбираем силуэт след.узла
			parseSiluet(next_node); 
		} else {
		}
		//-- //--             
	} //-- конец процедуры


	//-- Парсер одного шампура
	protected function parceShampur(node:NodeSprite, _level:int) { 
		//-- переменные
		var cur_node:NodeSprite;
var data:Object;
var comment:String;
var di_type:String;
var code:String;
var type:int;
var edge:EdgeSprite;
var this_comment:String;
var spaces:String = "";
var next_node:NodeSprite; 
		//-- в строку пробелов добавляем табуляторы по глубине уровня
		for (var i:int = 0; i < _level; i++)  
	spaces += "\t";
 
		//-- получаем параметры текущего узла
		cur_node = node;
data = cur_node.data;
code= geReleaseCode(cur_node);
di_type = data["url"];
comment = data["NodeLabel"];
this_comment = comment;
 
		//-- -psh- n: 
		//-- узел НАЧАЛО ШАМПУРА?
		if(di_type == DI_SH_BEG) {
		} else {
			//-- узел КОНЕЦ СИЛУЭТА?
			if(di_type == DI_SI_END) {
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
			var str = "ОШИБКА! Первый узел должен быть " + DI_SH_BEG + "!";
res_str += spaces + str;
 
			//-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
			message(str); 
			//-- //--         
			return; 
		}
		//-- добавляем в результат комментарий и код если они есть
		if(comment != null)
	res_str += spaces + "//-- " + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
		//-- выходных ребер 2?
		if(cur_node.outDegree == 2) {
			//-- получаем тип первго выхода
			edge = cur_node.getOutEdge(0);
di_type = edge.target.data["url"];
 
			//-- на первом выходе НАЧАЛО ШАМПУРА или КОНЕЦ СИЛУЭТА?
			if(di_type == DI_SH_BEG || 
di_type == DI_SI_END) {
				//-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
				next_node = edge.target;
cur_node = cur_node.getOutNode(1);
 
			} else {
				//-- след.узлом будет тот что на первом  выходе, а текущим станет тот что на втром
				next_node = cur_node.getOutNode(1);
cur_node = cur_node.getOutNode(0);
 
			}
		} else {
			//-- выходных ребер 1?
			if(cur_node.outDegree == 1) {
				//-- след.узла не будет, а текущим станет тот что на первом
				cur_node = cur_node.getOutNode(0);
next_node = null;


 
			} else {
				//-- формируем сообщение о ошибке
				str = "ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2\n";
 
				//-- ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2
				message(str); 
				//-- //--         
				return; 
			}
		}
		//-- Разбираем ветку
		var term:NodeSprite = parceNext(cur_node, _level + 1); 
		//-- вернулся пустой терминатор?
		if(term == null) {
			//-- формируем сообщение о ошибке
			var str = "ОШИБКА! в шампуре \"" + this_comment + "\" parceNext() вернул пусой терминатор\n";
res_str += str; 
			//-- ОШИБКА! в шампуре ... вернул пусой терминатор
			message(str); 
		} else {
			//-- терминатор КОНЕЦ СИЛУЭТА?
			if(term.data["url"] == DI_SI_END) {
			} else {
				//-- добавляем конец процедуры в результат
				res_str += spaces + "} //-- конец процедуры\n\n\n"; 
			}
		}
		//-- есть след.узел?
		if(next_node != null) {
			//-- Разбираем след. шамапур
			parceShampur(next_node,_level); 
		} else {
		}
		//-- //--             
	} //-- конец процедуры


	//-- Парсинг следующего узла
	protected function parceNext(cur_node:NodeSprite, _level:int):NodeSprite {
/**
 * @param	cur_node
 * @param	res_str
 * @return terminator - последний узел на котором закончилось движение
 */ 
		//-- переменные
		var data:Object = cur_node.data;
var code:String = geReleaseCode(cur_node);
var di_type:String = data["url"];
var comment:String = data["NodeLabel"];
var edge:EdgeSprite;
var term_yes:NodeSprite;
var next_node:NodeSprite;
var spaces:String = ""; 
		//-- -pnx- n: 
		//-- текущий узел null?
		if(cur_node == null) {
			//-- формируем сообщение о ошибке
			var str = "\nОШИБКА! Следующий за узлом \"" + comment + "\" узел отсутствует.";
 
			//-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
			message(str); 
			//-- null
			return null; 
		} else {
		}
		//-- в строку пробелов добавляем табуляторы по глубине уровня
		for (var i:int = 0; i < _level; i++)  
	spaces += "\t";
 
		//-- обрабатываем перевод строки в комментариях и коде
		var pattern:RegExp = /\n/g;	
if(comment != null)	
	comment = comment.replace(pattern, "\n" +spaces +"//-- ");
if(code != null)	
	code = code.replace(pattern, "\n" +spaces);
 
		//-- тип текущего узла не задан?
		if(di_type == null || di_type.length == 0) {
			//-- в результат добавляем  ПРЕДУПРЕЖДЕНИЕ! не установлен тип узла
			res_str += spaces +"//-- ПРЕДУПРЕЖДЕНИЕ! не установлен тип узла \"" + comment + "\" \n"; 
			//-- добавляем коментарий и код в результат
			if (comment != null)
	res_str += spaces +" //--"+  comment + "\n";
if (code != null)
	res_str += spaces +code + "\n";
 
			//-- node = Разбираем ветку
			var node:NodeSprite = parceNext(cur_node.getOutNode(0), _level);
			//-- node
			return node; 
		} else {
		}
		//-- тип узла
		switch(di_type) {
			//-- ДЕСТВИЕ(AC)
			case DI_AC:
				//-- добавляем коментарий и код в результат
				if (comment != null)
	res_str += spaces +" //--"+  comment + "\n";
if (code != null)
	res_str += spaces +code + "\n";
 
				//-- есть выходы?
				if(cur_node.outDegree >= 1) {
					//-- для всех выходов
					for(var i:int = 0; i < cur_node.outDegree; i++) {
						//-- получаем тип выхода
						var et:String = cur_node.getOutEdge(i).data["url"]; 
						//-- ребро ссылка-указатель?
						if(et == DI_REF) {
						} else {
							//-- node = Разбираем ветку
							var node:NodeSprite = parceNext(cur_node.getOutEdge(i).target, _level);
							//-- node
							return node; 
						}
					}
				} else {
				}
				//-- формируем сообщение о ошибке
				var str:String = "ОШИБКА! У Действия \"" + comment + "\" должено быть выход!\n";

 
				//-- добавляем в результат
				res_str += spaces + str;
 
				//-- "ОШИБКА! У Действия ... должен быть выход.
				message(str); 
				//-- тек. узел
				return cur_node; 
			//-- КОНЕЦ ЦИКЛА(FOR_END)
			case DI_FOR_END:
				//-- тек. узел
				return cur_node; 
			//-- УСЛОВИЕ(IF)
			case DI_IF:
				//-- переменные
				var true_edge_type:String;
var di_type_edge:String;
var i = 0; 
				//-- записываем if выражение в результат
				res_str += spaces +"//-- " + comment + "\n";
res_str += spaces +"if(" + code + ") {\n"; 
				//-- два выхода?
				if(getOutDegree(cur_node) == 2) {
				} else {
					//-- формируем сообщение о ошибке
					var str = "ОШИБКА! У Развилки \"" + comment + "\" должено быть два выхода!\n";
res_str += spaces + str;
 
					//-- ОШИБКА! У Развилки ... должено быть два выхода!
					message(str); 
				}
				//-- берем первый выход, определяем его тип
				edge = cur_node.getOutEdge(0);
di_type_edge = edge.data["EdgeLabel"];
i = 0; 
				//-- тип не Да?
				if(di_type_edge.toUpperCase() != "ДА" &&  di_type_edge.toUpperCase() != "YES") {
					//-- не тот конец, берем следующий
					edge = cur_node.getOutEdge(1);
di_type_edge = edge.data["EdgeLabel"];
i = 1; 
				} else {
				}
				//-- тип не Да?
				if(di_type_edge.toUpperCase() != "ДА" &&  di_type_edge.toUpperCase() != "YES") {
					//-- формируем сообщение о ошибке
					str = "Ошибка! У Развилки \"" + comment + "\" должен быть Да конец!";
 
					//-- Ошибка! У Развилки ... должен быть Да конец!
					message(str); 
					//-- null
					return null; 
				} else {
				}
				//-- обрабатываем ветку Да записываем результат } else {
				var cur_node_d = edge.target; // cur_node.getOutNode(i);
term_yes = parceNext(cur_node_d, _level + 1);
res_str += spaces +"} else {\n";
 
				//-- обрабатываем ветку Нет записываем результат
				if (i == 0) {
	cur_node_d = cur_node.getOutEdge(1).target;
} else {
	cur_node_d = cur_node.getOutEdge(0).target;
}
var term_no = parceNext(cur_node_d, _level+1);
res_str += spaces +"}\n";
 
				//-- выбираем куда дальше идти 
				if (term_yes.data["url"] == DI_SH_END &&
	 term_no.data["url"] != DI_SH_END) 
		term_yes = term_no; 
				//-- выбранный терминатор null?
				if(term_yes == null) {
					//-- null
					return null; 
				} else {
				}
				//-- выбранный терминатор КОНЕЦ ЦИКЛА?
				if(term_yes.data["url"] == DI_FOR_END) {
					//-- терминатор
					return term_yes; 
				} else {
				}
				//-- У терминатора 1 выход?
				if(getOutDegree(term_yes) == 1) {
					//-- делаем текущим узел выхода из терминатора
					cur_node = term_yes.getOutNode(0); 
					//-- node = Разбираем ветку
					var node:NodeSprite = parceNext(cur_node, _level);
					//-- node
					return node; 
				} else {
					//-- терминатор КОНЕЦ ШАМПУРА?
					if(di_type == DI_SH_END) {
					} else {
						//-- формируем предупреждение
						str = "//-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки \"" + comment +"\" имеет "+ term_yes.outDegree +" выходов. Должен быть один. \n";
 
						//-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки ... имеет ... выходов. Должен быть один.
						message(str); 
					}
					//-- терминатор
					return term_yes; 
				}
			//-- НАЧАЛО ЦИКЛА(FOR_BEG)
			case DI_FOR_BEG:
				//-- FOR_EACH_BEG//-- 
				case FOR_EACH_BEG:
					//-- записываем комментарии  и код в результат
					if (comment != null)
	res_str += spaces +"//-- " + comment + "\n";
res_str += spaces + code +"\n"; 
					//-- один выход?
					if(cur_node.outDegree == 1) {
					} else {
						//-- формируем сообщение о ошибке
						str = "ОШИБКА! У Начала цикла \"" + comment + "\" должен быть один выход!\n";
 
						//-- ОШИБКА! У Начала цикла ... должен быть один выход!
						message(str); 
						//-- записываем сообщение о ошибке в результат
						res_str += str;
 
						//-- тек. узел
						return cur_node; 
					}
					//-- делаем текущим выход.узел 
					edge = cur_node.getOutEdge(0);
cur_node = edge.target;  
					//-- терминатор = Разбираем ветку
					var term:NodeSprite= parceNext(cur_node, _level + 1);
					//-- записываем в результат "}"
					res_str += spaces +"}\n";
 
					//-- вернулся не КОНЕЦ ЦИКЛА?
					if(term.data["url"] == DI_FOR_END) {
					} else {
						//-- формируем сообщение о ошибке
						str = "ОШИБКА! У Цикла \"" + comment + "\" нет конца!\n";
 
						//-- ОШИБКА! У Цикла ... нет конца!
						message(str); 
						//-- записываем сообщение о ошибке в результат
						res_str += str;
 
						//-- терминатор
						return term; 
					}
					//-- один выход у терминатора?
					if(term.outDegree == 1) {
					} else {
						//-- формируем сообщение о ошибке
						str = "ОШИБКА! У Цикла \"" + comment + "\" нет продолжения пути!\n";
 
						//-- ОШИБКА: У Цикла ... нет продолжения пути.
						message(str); 
						//-- записываем сообщение о ошибке в результат
						res_str += str;
 
						//-- терминатор
						return term; 
					}
					//-- делаем текущим выход терминатора
					cur_node = term.getOutNode(0);  
					//-- node = Разбираем ветку
					var node:NodeSprite = parceNext(cur_node, _level);
					//-- node
					return node; 
			//-- КОНЕЦ СИЛУЭТА(SI_END)
			case DI_SI_END:
				//-- добавляем комент и код в результат
				if (comment != null)
	res_str += spaces +"//-- " + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
				//-- тек. узел
				return cur_node; 
			//-- КОНЕЦ ШАМПУРА(SH_END)
			case DI_SH_END:
				//-- добавляем комент и код в результат
				if (comment != null)
	res_str += spaces +"//-- " + comment + "\n";
if (code != null)
res_str += spaces +code + "\n"; 
				//-- тек. узел
				return cur_node; 
			//-- КОНЕЦ УСЛОВИЯ(EI)
			case DI_EI:
				//-- тек. узел
				return cur_node; 
			//-- НАЧАЛО ШАМПУРА(SH_BEG)
			case DI_SH_BEG:
				//-- тек. узел
				return cur_node; 
			//-- ВЫБОР(SWITCH)
			case DI_SW:
				//-- переменные
				var true_edge_type:String;
var di_type_edge:String; 
				//-- записываем switch выражение в результат
				res_str += spaces +"//-- " + comment + "\n";
res_str += spaces +"switch(" + code + ") {\n"; 
				//-- сбрасываем терминатор
				//-- Обрабатываем все case ветки
				for(var i:int = 0; i < cur_node.outDegree; i++) {
					//-- получаем данные очередного рыходного узла
					edge = cur_node.getOutEdge(i);
var cur_node_d = edge.target; // cur_node.getOutNode(i);

var data:Object = cur_node_d.data;
var code:String = geReleaseCode(cur_node);
var di_type:String = data["url"]; 
					//-- это  CASE?
					if(di_type == DI_CASE) {
						//-- term_yes = Разбираем ветку
						term_yes= parceNext(cur_node_d, _level + 1);
					} else {
						//-- формируем сообщение о ошибке
						str = "Ошибка! У Иконы ВЫБОР \"" + comment + "\" все ребра должны вести к Case иконам!";
 
						//-- Ошибка! У Иконы ВЫБОР ... все ребра должны вести к Case иконам!
						message(str); 
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
				if(term_yes.outDegree == 1) {
					//-- текущий узел выход из терминатора
					cur_node = term_yes.getOutNode(0); 
					//-- node = Разбираем ветку
					var node:NodeSprite = parceNext(cur_node, _level);
					//-- node
					return node; 
				} else {
					//-- терминатор КОНЕЦ ШАМПУРА?
					if(di_type == DI_SH_END) {
					} else {
						//-- формируем предупреждение
						str = "//-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки \"" + comment +"\" имеет "+ term_yes.outDegree +" выходов. Должен быть один. \n";
 
						//-- ПРЕДУПРЕЖДЕНИЕ!   Терминатор развилки ... имеет ... выходов. Должен быть один.
						message(str); 
					}
					//-- терминатор
					return term_yes; 
				}
			//-- ПРИСВОЕНИЕ(EQL)
			case DI_EQL:
				//-- делаем текущим узел первого выхода
				cur_node = cur_node.getOutNode(0); 
				//-- тек. узел не НИЖНЯЯ ЧАСТЬ  ПОЛКИ?
				if(cur_node.data["url"] != DI_EQR) {
					//-- формируем сообщение о ошибке
					var str = "ОШИБКА! В иконе \"Полка\" отсутствует правая часть(EQR)\n";
res_str += "\n" + str;
 
					//-- ОШИБКА! В иконе Полка отсутствует нижняя часть
					message(str); 
					//-- тек. узел
					return cur_node; 
				} else {
				}
				//-- обрабатываем перевод строки в комментариях и коде нижней части//-- добавляем в результат комент и код
				var cod_r:String = geReleaseCode(cur_node);
var com_r:String = cur_node.data["NodeLabel"];

var pattern:RegExp = /\n/g;	
if(com_r != null)	
	com_r = com_r.replace(pattern, "\n" +spaces +"//-- ");
if(cod_r != null)	
	cod_r = cod_r.replace(pattern, "\n" +spaces);

res_str += spaces +"//-- " + comment + " = "+ com_r+ "\n";
res_str += spaces + code + cod_r + "\n";
 
				//-- для всех выходов
				for(var i:int = 0; i < cur_node.outDegree; i++) {
					//-- ребро ссылка-указатель?
					if(cur_node.getOutEdge(i).data["url"] == DI_REF) {
					} else {
						//-- node1 = Разбираем ветку
						var node1:NodeSprite = parceNext(cur_node.getOutEdge(i).target, _level);
						//-- node1
						return node1; 
					}
				}
				//-- break
				break; 
			//-- CASE
			case DI_CASE:
				//-- переменные
				var true_edge_type:String;
var di_type_edge:String; 
				//-- записываем case выражение в результат
				res_str += spaces +"//-- " + comment + "\n";
res_str += spaces +"case " + code + ":\n"; 
				//-- делаем текущим выход.узел 
				edge = cur_node.getOutEdge(0);
cur_node = edge.target;  
				//-- term_yes = Разбираем ветку
				term_yes= parceNext(cur_node, _level + 1);
				//-- терминатор
				return term_yes; 
		}
		//-- null
		return null;
	} //-- конец процедуры


	//-- Получение рабочего кода узла
	protected function geReleaseCode(node:NodeSprite):String { 
		//-- переменная
		var code:String = ""; 
		//-- реализация установлена?
		if(CURRENT_RELEASE != null && CURRENT_RELEASE != "") {
			//-- для всех входов
			for(var i:int = 0; i < node.inDegree; i++) {
				//-- получаем входной узел
				var in_node:NodeSprite = node.getInEdge(i).source; 
				//-- это узел текущей реализации?
				if(getIconType(in_node) == CURRENT_RELEASE) {
					//-- получаем код из вход. узла
					code = in_node.data["description"]; 
					//-- код
					return code; 
				} else {
				}
			}
		} else {
			//-- получаем код из текущего узла
			code = node.data["description"]; 
		}
		//-- код
		return code;
	} //-- конец процедуры


	//-- Получение типа иконы узла
	protected function getIconType(node:NodeSprite):String { 
		//-- получаем иконый тип
		var type:String = node.data["url"]; 
		//-- тип
		return type;
	} //-- конец процедуры


	//-- Получение комента из узла
	protected function getComment(node:NodeSprite):String { 
		//-- получаем коментарий
		var ret:String = node.data["NodeLabel"]; 
		//-- ком.
		return ret;
	} //-- конец процедуры


	//-- Возвращает код из узла
	public function getCode(node:NodeSprite):String { 
		//-- строим код
		return node.data["description"]; 
		//-- //--             
	} //-- конец процедуры


	//-- Парсер схемы ДРАКОНА полученной из графа yEd
			public function parseDrakon() { 
		//-- строим код
			parse_drakon(graph_data);
 
		//-- //--             
	} //-- конец процедуры


	//-- Отрисовка графа
	protected function build_vis(data:Data):void { 
		//-- строим код
					var i = 0;
			for each (var tuple:NodeSprite in data.nodes)
			{
				var _d = tuple.data;
				//tuple.x = new Number(_d["X"]);
				//tuple.y = new Number(_d["Y"]);
				tuple.x = _d["X"];
				tuple.y = _d["Y"];
				tuple.size = 1.5;
				switch(tuple.data["url"]) {
					case DI_DG_BEG:
						tuple.fillColor =   0xFFFFFF;
						break;
					case DI_SI_BEG:
						tuple.fillColor =   0x0000FF;
						break;
					case DI_SI_END:
						tuple.fillColor =   0x0000FF;
						break;
					case DI_SH_BEG:
						tuple.fillColor =   0x00FF00;
						break;
					case DI_SH_END:
						tuple.fillColor =   0x00FF00;
						break;
					case DI_WR_RES_FILE:
						tuple.fillColor =   0xFFFFFF;
						break;
					case DI_AC:
						tuple.fillColor =   0xFFFF00;
						break;
					case DI_IF:
					case DI_ID:
					case DI_IR:
						tuple.fillColor =   0xFF0F0F;
						break;
					case DI_RY:
						tuple.fillColor =   0xFF0F0F;
						break;
					case DI_DN:
						tuple.fillColor =   0xFF0F0F;
						break;
					case DI_EI:
						tuple.fillColor =   0xFF0F0F;
						tuple.size = 0.7;
						break;
					default:
				}
				//tuple.fillColor =   0xFF0F0F;
				//tuple.lineColor = 0x00FF00;
				//tuple.lineWidth = 3;
				//tuple.w = 150.;
				//tuple.radius = 150.;
				tuple.fillAlpha = 1.;
			}
			var vis:Visualization = new Visualization(data);
			Main.main.addChild(vis);
			vis.x = 400;
			vis.y = 400;
			vis.scaleX = 1.;
			vis.scaleY = 1.;
			vis.update();
			vis.operators.add(new CircleLayout());
			// Do something here...        
 
		//-- //--             
	} //-- конец процедуры


	//-- Масштабирование картинки
	public function zoomAll():void { 
		//-- строим код
					Main.informer.writeDebugRightField("before",graph_data.nodes[0].x);
			for each (var tuple:NodeSprite in graph_data.nodes) {
				tuple.x = Main.main.toDisplayX(tuple.data["X"]);
				tuple.y = Main.main.toDisplayY(tuple.data["Y"]);
			}
			Main.informer.writeDebugRightField("after", graph_data.nodes[0].x);
 
		//-- //--             
	} //-- конец процедуры


	//-- Загрузка данных
	public function loadData(query:String):void	{ 
		//-- строим код
					//var query:String = "file://c:/temp/s.graphml";
			// Create an Actionscript URL loader
			var loader:URLLoader = new URLLoader();
			// Wire up event listeners for our load process                      
			configListeners(loader);
			// Create a URL request with the path to the GraphML file
			var request:URLRequest = new URLRequest(query);
			try {
				// Initiate the loading process
				load_finish = false;
				graph_data = null;
				
				loader.load(request);
			} catch (error:Error) {
				message("Unable to load requested document.");
			}
 
		//-- //--             
	} //-- конец процедуры


	//-- Конфигурация листнеров
	private function configListeners(dispatcher:IEventDispatcher):void 	{ 
		//-- строим код
					// Add a completion event listener
			dispatcher.addEventListener(Event.COMPLETE, onLoadingFinesh);
 
		//-- //--             
	} //-- конец процедуры


	//-- Обработка события "загрузка завершена"
	private function onLoadingFinesh(event:Event):void { 
		//-- строим код
					// Get event data - which should be the loaded object
			var loader:URLLoader = URLLoader(event.target);
			
			// Get XML from the loaded object
			var str:String = loader.data;
			
			//-- правим файл убираем лишние xmlns
			str = str.replace("xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ", "");
			str = str.replace(" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\"", "");
			
			//message(str);
			var graphML:XML = new XML(str);
			
			// Use Flare converter to convert GraphML to dataset 
			var gmlc:MyGraphMLConverter = new MyGraphMLConverter();
			var dataSet:DataSet = gmlc.parse(graphML);
			
			// Pass the dataset to our graph routine                  
			graph_data = Data.fromDataSet(dataSet);
			load_finish = true;
			
			buildVis();
			parseDrakon();
 
		//-- //--             
	} //-- конец процедуры


	//-- Отрисовка графа загруженного функцией loadData()
	public function buildVis():void { 
		//-- строим код
					build_vis(graph_data);
 
		//-- //--             
	} //-- конец процедуры


	//-- message()
	public function message(str:String):void { 
		//-- строим код
		trace(str); 
		//-- //--             
	} //-- конец процедуры


	//-- getInDegree()
	public function getInDegree(node:NodeSprite):int { 
		//-- строим код
		return node.inDegree;
 
		//-- //--             
	} //-- конец процедуры


	//-- getOutDegree()
	public function getOutDegree(node:NodeSprite):int { 
		//-- строим код
		return node.inDegree;
 
		//-- //--             
	} //-- конец процедуры


	//-- getInNode()
	public function getInNode(node:NodeSprite, num:int):NodeSprite { 
		//-- строим код
		return node.getInNode(num); 
		//-- //--             
	} //-- конец процедуры


	//-- getOutNode()
	public function getOutNode(node:NodeSprite, num:int):NodeSprite { 
		//-- строим код
		return node.getOutNode(num); 
		//-- //--             
	} //-- конец процедуры


	//-- getOutEdge()
	//-- не нужна для AS
public function getOutEdge():void { 
		//-- строим код
		//-- не нужна для AS 
		//-- //--             
	} //-- конец процедуры


	//-- isEdgeYes()
	//-- не нужна для AS
public function isEdgeYes():void { 
		//-- строим код
		//-- не нужна для AS 
		//-- //--             
	} //-- конец процедуры


	//-- isReferenceEdge()
	//-- не нужна для AS
public function isReferenceEdge():void { 
		//-- строим код
		//-- не нужна для AS 
		//-- //--             
	} //-- конец процедуры


	//-- 
            
	   } //-- конец класса
} //-- крнец пакета 

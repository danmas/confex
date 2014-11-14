
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
 
	//-- class DrakonGen2
	public class DrakonGen2 { 
	//-- константы
		public final static String DI_EXT_NEXT = "next";
	public final static String DI_DG_BEG = "DG_BEG";
	public final static String DI_SI_BEG = "SI_BEG";
	public final static String DI_SI_END = "SI_END";
	public final static String DI_SH_BEG = "SH_BEG";
	public final static String DI_SH_END = "SH_END"; 
	public final static String DI_WR_RES_FILE = "WR_RES_FILE";
	public final static String DI_AC = "AC";		
	public final static String DI_ID = "ID";		
	public final static String DI_IR = "IR";		
	//TODO: удалить DI_ID и DI_IR
	public final static String DI_IF = "IF";		
	public final static String DI_RY = "RY";		
	public final static String DI_DN = "DN";		
	public final static String DI_EI = "EI";		
	public final static String DI_UK = "UK";		
	public final static String DI_EQL = "EQL";		
	public final static String DI_EQR = "EQR";		
	public final static String DI_FOR_BEG = "FOR_BEG";		
	public final static String FOR_EACH_BEG = "FOR_EACH_BEG";		
	public final static String DI_FOR_END = "FOR_END";		
	public final static String DI_REF = "REF";
	
	public final static String DI_CASE = "CASE";
	public final static String DI_SW = "SWITCH";
	
	public final static String RELEASE_TYPE_CODE_JAVA = "CODE_JAVA";
	public final static String RELEASE_TYPE_CODE_AS = "CODE_AS"; 
	//-- переменные
	protected String res_str = "";
protected boolean load_finish = false;
protected String CURRENT_RELEASE = RELEASE_TYPE_CODE_AS; 
	//-- Конструктор
	public  DrakonGen2() { 
		//-- //--             
		
	} //-- конец процедуры


	//-- Парсер схемы
	public String parse_drakon(Graph graph) {
	Object data;
	String descr;
	String di_type;
	
	res_str = ""; 
		//-- Проходим по всем узлам
		for (Vertex v : graph.getVertices()) {
			//-- получаем тип узла
			di_type = getIconType(v); ; 
			//-- узел НАЧАЛО?
			if(di_type != null && di_type.equals(DI_DG_BEG)) {
				//-- извлекаем из Начало тип реальности
				CURRENT_RELEASE = (String) getCode(v); 
				//-- ---
				message("--->Текущая реальность: " + CURRENT_RELEASE); 
				//-- у текущего узла один выход?
				if(getOutDegree(v) == 1) {
					//-- теперь текущий узел тот на который указывает выход
					v = getOutNode(v,0); 
					//-- ---
					message("---> Разбираем силуэт "); 
					//-- Разбираем диаграмму
					parseSiluet(v); 
				} else {
					//-- у текущего узла нет выходов?
					if(getOutDegree(v) == 0) {
					} else {
						//-- ОШИБКА! У иконы Начало ...//--  выходов!
						message("ОШИБКА! У иконы Начало \"" + getOutDegree(v) + "\" выходов!"); 
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
	public void parseSiluet(Vertex node) {
/**
 * Парсер одного силуэта ДРАКОНА 
 * @param	var graph_data
 */
 
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
 
		//-- получаем параметры текущего узла
		cur_node = node;
code = geReleaseCode(cur_node);
di_type = getIconType(cur_node);
comment = getComment(cur_node);

 
		//-- -psi- n: 
		message("-psi- n: "+getComment(cur_node)); 
		//-- узел ЗАПИСЬ В ФАЙЛ?
		if(di_type.equals(DI_WR_RES_FILE)) {
			//-- запись в файл
			if (code == null) {
	code = "temp.as";
	res_str = "ОШИБКА. Имя выходного файла не задано в иконе  WR_RES_FILE.\n" + res_str;
}
message("----> Записываем файл " + code + "\n" + res_str);
FileUtils.fileWrite(code, res_str);
res_str = "";

if (getOutDegree(cur_node) == 1) {
	cur_node = getOutNode(cur_node,0);
	code = geReleaseCode(cur_node);
	di_type = getIconType(cur_node);
	comment = getComment(cur_node);
} else if (getOutDegree(cur_node) == 0) {
	//-- Конец разбора
	return;
} else {
	message("ОШИБКА! У узла иконы " + comment + " записи в файл \"" + getOutDegree(cur_node) + "\" выходов!");
	res_str += "ОШИБКА! У узла иконы " + comment + " записи в файл \"" + getOutDegree(cur_node) + "\" выходов!";
	return;
} 
		} else {
		}
		//-- узел НАЧАЛО СИЛУЭТА?
		if(di_type.equals(DI_SI_BEG)) {
		} else {
			//-- формируем сообщение о ошибке
			String str = "ОШИБКА! Первый узел шампура должен быть \"" + DI_SI_BEG + "\"!\n"
+ "А узел " + comment +" имеет тип " + di_type;
res_str += str; 
			//-- ОШИБКА! Первый узел шампура должен быть НАЧАЛО СИЛУЭТА
			message(str); 
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
		if(getOutDegree(cur_node) != 1 && getOutDegree(cur_node) != 2) {
			//-- формируем сообщение о ошибке
			String str = "ОШИБКА! У иконы Начало Силуэта \"" + getOutDegree(cur_node) + "\" выходов!\n"
+ " Должно быть 1 или 2.\n"; 
			//-- ОШИБКА! У иконы Начало Силуэта  Должно быть 1 или 2 выхода
			message(str); 
			//-- //--         
			return; 
		} else {
		}
		//-- выходных ребер 2?
		if(getOutDegree(cur_node) == 2) {
			//-- получаем тип первго выхода
			Vertex v = getOutNode(cur_node,0);
di_type = getIconType(v);
 
			//-- на первом выходе ЗАПИСЬ В ФАЙЛ?
			if(di_type.equals(DI_WR_RES_FILE)) {
				//-- след.узлом будет тот что на первом выходе, а текущим станет тот что на втором
				next_node = getOutNode(cur_node,0);
cur_node = getOutNode(cur_node,1);
 
			} else {
				//-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
				next_node = getOutNode(cur_node,1);
cur_node = getOutNode(cur_node,0);
 
			}
		} else {
			//-- ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода
			message("ОШИБКА! У иконы Начало Силуэта  Должно быть 2 выхода"); 
			//-- //--         
			return; 
		}
		//-- тип тек.узла НАЧАЛО ШАМПУРА?
		if(getIconType(cur_node).equals(DI_SH_BEG)) {
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
	protected void parceShampur(Vertex node, int  _level) { 
		//-- переменные
		Vertex cur_node;
//var data;
String comment;
String di_type;
String code;
int type;
//var edge:EdgeSprite;
String this_comment;
String spaces = "";
Vertex next_node; 
		//-- в строку пробелов добавляем табуляторы по глубине уровня
		for (int i = 0; i < _level; i++)  
	spaces += "\t";
 
		//-- получаем параметры текущего узла
		cur_node = node;
code= geReleaseCode(cur_node);
di_type = getIconType(cur_node);
comment = getComment(cur_node);
this_comment = comment; 
		//-- -psh- n: 
		message("-psh- n: "+getComment(cur_node)); 
		//-- узел НАЧАЛО ШАМПУРА?
		if(di_type.equals(DI_SH_BEG)) {
		} else {
			//-- узел КОНЕЦ СИЛУЭТА?
			if(di_type.equals(DI_SI_END)) {
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
			 
			//-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
			 
			//-- //--         
			return; 
		}
		//-- добавляем в результат комментарий и код если они есть
		if(comment != null)
	res_str += spaces + "//-- " + comment + "\n";
if(code != null) 
	res_str += spaces + code + " \n";
 
		//-- выходных ребер 2?
		if(getOutDegree(cur_node) == 2) {
			//-- получаем тип первго выхода
			Vertex v = getOutNode(cur_node,0);
di_type = getIconType(v);
 
			//-- на первом выходе НАЧАЛО ШАМПУРА?
			if(di_type.equals(DI_SH_BEG)) {
				//-- след.узлом будет тот что на первом  выходе, а текущим станет тот что на втром
				next_node = getOutNode(cur_node,0);
cur_node = getOutNode(cur_node,1); 
			} else {
				//-- след.узлом будет тот что на втором выходе, а текущим станет тот что на первом
				next_node = getOutNode(cur_node,1);
cur_node = getOutNode(cur_node,0); 
			}
		} else {
			//-- выходных ребер 1?
			if(getOutDegree(cur_node) == 1) {
				//-- след.узла не будет, а текущим станет тот что на первом
				cur_node = getOutNode(cur_node,0);
next_node = null; 
			} else {
				//-- формируем сообщение о ошибке
				String str = "ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2\n";
 
				//-- ОШИБКА! Число дочерних узлов у начала шампура не равно 1 или 2
				message(str); 
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
			message(str); 
		} else {
			//-- терминатор КОНЕЦ СИЛУЭТА?
			if(getIconType(term).equals(DI_SI_END)) {
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
		//+++++++++++++++++++++++++++++++++++++++++++++++++
//+++++++++++++++++++++++++++++++++++++++++++++++++
	} //-- конец процедуры


	//-- Парсинг следующего узла
	protected Vertex parceNext(Vertex cur_node, int _level) {
/**
 * @param	cur_node
 * @param	res_str
 * @return terminator - последний узел на котором закончилось движение
 */ 
		//-- переменные
		 String comment = getComment(cur_node);
 String di_type = getIconType(cur_node);
 String code = geReleaseCode(cur_node);
 int type;
 String this_comment;
 String spaces = "";
 Vertex next_node;
 Vertex term_yes;
 
		//-- -pnx- n: 
		message("-pnx- n: "+getComment(cur_node)); 
		//-- текущий узел null?
		if(cur_node == null) {
			//-- формируем сообщение о ошибке
			String str = "\nОШИБКА! Следующий за узлом \"" + comment + "\" узел отсутствует."; 
			//-- ОШИБКА! Первый узел должен быть  НАЧАЛО ШАМПУРА
			message(str); 
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
			//-- node = Разбираем ветку
			Vertex node = parceNext(getOutNode(cur_node,0), _level);
			//-- node
			return node; 
		} else {
		}
		//-- тип узла
		switch(di_type) {
		Ошибка! У Иконы ВЫБОР "тип узла" все ребра должны вести к Case иконам!ОШИБКА! в шампуре "Парсинг следующего узла" parceNext() вернул пусой терминатор
	//-- Получение рабочего кода узла
	public String geReleaseCode(Vertex node) { 
		//-- переменная
		String code = ""; 
		//-- реализация установлена?
		if(CURRENT_RELEASE != null && CURRENT_RELEASE.length() != 0) {
			//-- для всех входов
			for(int i = 0; i < getInDegree(node); i++) {
				//-- получаем входной узел
				Vertex in_node = getInNode(node,i); 
				//-- это узел текущей реализации?
				if(getIconType(in_node).equals(CURRENT_RELEASE)) {
					//-- получаем код из вход. узла
					code = getCode(in_node); 
					//-- код
					return code; 
				} else {
				}
			}
		} else {
			//-- получаем код из текущего узла
			code = getCode(node); 
		}
		//-- код
		return code;
	} //-- конец процедуры


	//-- Получение типа иконы узла
	protected String getIconType(Vertex node) { 
		//-- получаем иконый тип
		String type =  (String) node.getProperty("type") ; 
		//-- тип
		return type;
	} //-- конец процедуры


	//-- Получение комента из узла
	protected String getComment(Vertex node) { 
		//-- получаем коментарий
		String ret =  (String) node.getProperty("comment") ; 
		//-- ком.
		return ret;
	} //-- конец процедуры


	//-- Возвращает код из узла
	public String getCode(Vertex node) { 
		//-- строим код
		return (String) node.getProperty("code") ;
 
		//-- //--             
	} //-- конец процедуры


	//-- Парсер схемы ДРАКОНА полученной из графа yEd
	public void parseDrakon() { 
		//-- строим код
		//-- //--             
	} //-- конец процедуры


	//-- Отрисовка графа
	protected void build_vis(Object data) { 
		//-- строим код
		 
		//-- //--             
		
	} //-- конец процедуры


	//-- Масштабирование картинки
	public void zoomAll() { 
		//-- строим код
		 
		//-- //--             
		
	} //-- конец процедуры


	//-- Загрузка данных
	public void loadData(String query) { 
		//-- строим код
		 
		//-- //--             
		
	} //-- конец процедуры


	//-- Конфигурация листнеров
	private void configListeners() { 
		//-- строим код
		//-- //--             
	} //-- конец процедуры


	//-- Обработка события "загрузка завершена"
	private void onLoadingFinesh() { 
		//-- строим код
		//-- //--             
	} //-- конец процедуры


	//-- Отрисовка графа загруженного функцией loadData()
	public void buildVis() { 
		//-- строим код
		//-- //--             
	} //-- конец процедуры


	//-- message()
	public void message(String str) { 
		//-- строим код
		System.out.println(str); 
		//-- //--             
	} //-- конец процедуры


	//-- getInDegree()
	public int getInDegree(Vertex v) { 
		//-- строим код
		int i = 0;
for (Edge e : v.getInEdges()) {
	i++;
}
return i;
 
		//-- //--             
	} //-- конец процедуры


	//-- getOutDegree()
	public int getOutDegree(Vertex v) { 
		//-- строим код
		int i = 0;
for (Edge e : v.getOutEdges()) {
	i++;
}
return i;
 
		//-- //--             
	} //-- конец процедуры


	//-- getInNode()
	public Vertex getInNode(Vertex v, int num) { 
		//-- строим код
		if(v == null)
	return null;
int i = 0;
for (Edge e : v.getInEdges()) {
	if(i==num) 
		return e.getOutVertex();
	i++;
}
return null; 
		//-- //--             
	} //-- конец процедуры


	//-- getOutNode()
	public Vertex getOutNode(Vertex v, int num) { 
		//-- строим код
		if(v == null)
	return null;
int i = 0;
for (Edge e : v.getOutEdges()) {
	if(i==num) 
		return e.getInVertex();
	i++;
}
return null; 
		//-- //--             
	} //-- конец процедуры


	//-- getOutEdge()
	public Edge getOutEdge(Vertex v, int num) { 
		//-- строим код
		if(v == null)
	return null;
int i = 0;
for (Edge e : v.getOutEdges()) {
	if(i==num) 
		return e;
	i++;
}
return null; 
		//-- //--             
	} //-- конец процедуры


	//-- isEdgeYes()
	public boolean isEdgeYes(Edge edge) { 
		//-- строим код
		if(edge == null)
	return false;

String di_type_edge = (String)edge.getProperty("dglabel");
if(di_type_edge == null) return false;

if(di_type_edge.toUpperCase().equals("ДА") || 
		di_type_edge.toUpperCase().equals("YES"))
	return true;
return false; 
		//-- //--             
	} //-- конец процедуры


	//-- isReferenceEdge()
	public boolean isReferenceEdge(Edge edge) { 
		//-- //--             
		} //-- конец класса

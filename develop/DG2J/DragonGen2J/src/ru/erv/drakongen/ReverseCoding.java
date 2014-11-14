
//-- Сборка класса ReverseCoding
	//-- package//-- imports
	package ru.erv.drakongen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import ru.erv.drakongen.utils.FileUtils;
 
	//-- сlass ReverseCoding
	public class ReverseCoding { 
	//-- вспомогательный class Wrapper
	static class Wrapper<T> {
	T value;
	Wrapper(T value){
	this.value = value;
	}

	public T getValue() {
	return value;
	}

	public void setValue(T value) {
	this.value = value;
	}
} 
	//-- константы
	public static final boolean RET_OK  = true;
	public static final boolean RET_ERROR  = false;
	public static final String PREF_MARKER_CDATA = "<data key=\"d4\"><![CDATA[";
	public static final String PREF_MARKER_DG2J = "<DG2J code_mark=\"";
	public static final String PREF_CODE_CDATA = "<data key=\"d6\"><![CDATA[";
	public static final String POST_CODE_CDATA = "]]></data>";
	public static final String PREF_NODE_ID = "<node id=\"";
	public static final String POST_NODE_ID = "\"";
 
	//-- перменные
	List<String> files = new ArrayList<String>();
	
public List<String> getFiles() {
	return files;
}
public void setFiles(List<String> files) {
	this.files = files;
}

Map<String,String> insert_codes = new HashMap<String,String>();
 
	//-- Файлы graphml//-- "../Schemes/test.graphml"
	String gml_src_file = "../Schemes/test.graphml";
String gml_tgt_file = "../Schemes/test2.graphml";
 
	//-- Конструктор
	public ReverseCoding() { 
		//-- список файлов
		files.add("/src/ru/erv/drakongen/test/Test.java"); 
		//-- //--             
		


	} 

	//-- reverseCode()
	public String reverseCode() { 
		//-- задаем переменные
		String out_text = "";
 
		//-- для всех файлов из списка
		Iterator<String> itf = files.iterator();
while (itf.hasNext()) {
			//-- получаем полное имя файла
			String full_file_name = Settings.getProperty("BASE_DIR") + "/" + itf.next(); 
			//-- bret=
			boolean bret = 
			//-- Формирование карты подстановок для одного файла
			geReleaseCode(full_file_name); 
			//-- вернулась ошибка?
			if(bret == RET_ERROR) {
				//-- Ошибка при реверскодинге файла ...
				System.err.println("Ошибка при реверскодинге файла " + full_file_name); 
				//-- null
				return null; 
			} else {
			}
		}
		//-- читаем выходной graphml файл в переменную-буфер
		String gml_text = FileUtils.fileRead(Settings.getProperty("BASE_DIR") +"/" + gml_src_file); 
		//-- Выполняем маркерные замены
		out_text = replaseMarkedCode(gml_text); 
		//-- новый текст gml
		return out_text;
	} 

	//-- перенос кода с заменой по маркерам
	public String replaseMarkedCode(String src_text) { 
		//-- инициируем позиции поиска в начало pos1=0 pos2=0
		int pos1 = 0;
int pos2 = 0;
Wrapper<Integer> i = new Wrapper<Integer>(0);
Wrapper<Integer> i2 = new Wrapper<Integer>(0);
String out_text = ""; 
		//-- бесконечный поиск маркеров
		while(true) {
			//-- выделяем маркер начиная с текущей позиции pos1
			i.setValue(pos1);
String mark_code = getTextBetween(src_text,PREF_MARKER_CDATA,"]",i); 
			//-- нашли маркер? 
			if(mark_code != null) {
				//-- ищем позицию маркера с позиции pos1
				int pos_m = src_text.indexOf(PREF_MARKER_CDATA,pos1); 
				//-- получаем имя узла с позиции маркера - 45
				i2.setValue(pos_m-45);
String node_id = getTextBetween(src_text,PREF_NODE_ID,POST_NODE_ID,i2); 
				//-- добавляем имя узла в маркер
				mark_code = node_id +":" + mark_code; 
			} else {
				//-- дописываем остаток в интервале pos2 - до конца
				out_text += src_text.substring(pos2,src_text.length()); 
				//-- выходим из цикла
				break; 
			}
			//-- находим позицию pos2 = конец BegD от текущей позиции pos1
			pos2 = src_text.indexOf(PREF_CODE_CDATA,pos1) + PREF_CODE_CDATA.length(); 
			//-- переносим в результат код в интервале pos1-pos2
			out_text += src_text.substring(pos1,pos2);  
			//-- получаем новый код по текущему маркеру Mi
			String repl_text = insert_codes.get(mark_code); 
			//-- есть новый код?
			if(repl_text != null) {
			} else {
				//-- Достаем старый код
				i.setValue(pos1);
String old_code = getTextBetween(src_text,PREF_CODE_CDATA,"]",i); 
				//-- новый код = старому 
				repl_text = old_code; 
			}
			//-- добавляем в результат новый код
			out_text += repl_text; 
			//-- устанавливаем pos1 и pos2 за концом данных
			pos1 = src_text.indexOf(POST_CODE_CDATA,pos2);
pos2 = pos1;
 
		}
		//-- результат
		return out_text;
	} 

	//-- Формирование карты подстановок для одного файла
	protected boolean geReleaseCode(String full_file_name) { 
		//-- try
		try { 
		//-- открываем файл и читаем текст
		String text = FileUtils.fileRead(full_file_name);
 
		//-- бесконечный поиск PREF_MARKER_DG2J
		Wrapper<Integer> i = new Wrapper<Integer>(0);
while(true) {
			//-- ищем маркер 
			String mark_code = getTextBetween(text,PREF_MARKER_DG2J,"\"",i); 
			//-- нашли маркер? 
			if(mark_code != null) {
			} else {
				//-- break
				break; 
			}
			//-- уже есть такой маркер в карте подстановок? 
			if(insert_codes.get(mark_code) != null) {
				//-- Ошибка! Дублирование маркера кода ...
				System.err.println("Ошибка! Дублирование маркера кода ..."); 
				//-- Error
				return RET_ERROR; 
			} else {
			}
			//-- ищем текста подстановки
			String out_text = getTextBetween(text,">","</DG2J>",i);
 
			//-- нашли текста подстановки? 
			if(out_text != null) {
			} else {
				//-- break
				break; 
			}
			//-- заносим текст подстановки в карту замен
			insert_codes.put(mark_code, out_text); 
		}
		//-- catch
		} catch(Exception e) {
	System.err.println(e.getMessage() );
} 
		//-- Ok
		return RET_OK;
	} 

	//-- Выделение текста между префиксом и постфиксом начиная с позиции pos
	public static String getTextBetween(String text, String prefix, String postfix,  Wrapper<Integer>  position) { 
		//-- ищем фразу префикса
		int i1 = text.indexOf(prefix,position.getValue()); 
		//-- нашли префикс? 
		if(i1 >= 0) {
			//-- ищем постфикс от конца префикса 
			int i2 = text.indexOf(postfix,i1+prefix.length()+1); 
			//-- нашли постфикс? 
			if(i2>=0) {
				//-- результат
				position.setValue(i2+postfix.length());
return text.substring(i1+prefix.length(),i2); 
			} else {
				//-- null
				return null; 
			}
		} else {
			//-- null
			return null; 
		}
		//-- //--         
		

	} 

	//-- main
	public static void main(String[] args) { 
		//-- устанавливаем BASE_DIR
		Settings.setProperty("BASE_DIR", "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\");
 
		//-- Тест 
		//-- Должна вернуть asdf
String text = " <111>asdf<222>-<111>fdsa<222> ";		
Wrapper<Integer> pos = new Wrapper<Integer>(0);
System.out.println(ReverseCoding.getTextBetween(text,"<111>","<222>",pos)); 
System.out.println(pos.getValue());
System.out.println(ReverseCoding.getTextBetween(text,"<111>","<222>",pos)); 
System.out.println(pos.getValue());


text = "<111>asdf<222><111>fdsa<222> ";		
pos.setValue(0);
System.out.println(ReverseCoding.getTextBetween(text,"<111>","<222>",pos)); 
System.out.println(pos.getValue());
System.out.println(ReverseCoding.getTextBetween(text,"<111>","<222>",pos)); 
System.out.println(pos.getValue());
 
		//-- переменная rc
		ReverseCoding rc  
		//-- экземпляр класса ReverseCoding
		= new ReverseCoding(); 
		//-- переменная out_text
		String out_text 
		//-- реверс кодинд фала gml
		= rc.reverseCode(); 
		//-- записываем новый файл gml
		String gml_tgt_file = "../Schemes/test2.graphml";
FileUtils.fileWrite(Settings.getProperty("BASE_DIR") + gml_tgt_file, out_text);
 
		//-- //--             
	} 

	//-- 
            
	} //-- конец класса
 

//-- Класс DrakonUtils
//-- упоминание о DrakonGen2
/**
 * Этот текст сгенерирован программой DrakonGen2
 * @author Erv +
 */
//-- package//-- imports
package ru.erv.drakongen;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

//-- class DrakonUtils
public class DrakonUtils {
	// -- константы
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
	public final static String DI_START_ACTS = "START_ACTS";

	public static final String REM_TRY = "DI_TRY";
	public static final String REM_CATCH = "DI_CATCH";
	public static final String REM_PROC_DOC = "DI_PROC_DOC";
	public static final String REM_CALL_PROC = "DI_CALL_PROC";

	public final static String RELEASE_TYPE_CODE_JAVA = "CODE_JAVA";
	public final static String RELEASE_TYPE_CODE_AS = "CODE_AS";

	public static enum IcTypes {
		DI_EXT_NEXT, DI_DG_BEG, DI_SI_BEG, DI_SI_END, DI_CLASS_END, DI_COMPIL_BEG, DI_COMPIL_END, DI_SH_BEG, DI_SH_END, DI_PROC_BEG, DI_PROC_END, DI_WR_RES_FILE, DI_AC, DI_ACTION, DI_SUB_COMPIL, DI_IF, DI_RY, DI_DN, DI_EI, DI_UK, DI_FOR_BEG, FOR_EACH_BEG, DI_FOR_END, DI_REF, DI_BREAK,

		DI_CASE, DI_DEFAULT, DI_SW, DI_RETURN, DI_INSERT, DI_OUTPUT, DI_START_ACTS,

		REM_TRY, REM_CATCH, REM_PROC_DOC, REM_CALL_PROC;
	};

	// -- переменные
	// -- Конструктор
	public DrakonUtils() {
		// -- //--
	}

	// -- Получение маркера кода
	public static String getCodeMark(Vertex node) {
		// -- получаем маркер кода
		if (node == null)
			return "";
		String type = (String) node.getProperty("code_mark");
		if (type == null)
			type = "";
		// -- маркер
		return type;
	}

	// -- Получение типа иконы узла
	public static String getIconType(Vertex node) {
		// -- получаем иконый тип
		if (node == null)
			return "";
		String type = (String) node.getProperty("type");
		if (type == null)
			type = "";
		// -- тип
		return type;
	}

	// -- Получение комента из узла
	public static String getComment(Vertex node) {
		// -- получаем коментарий
		if (node == null)
			return "";
		String ret = (String) node.getProperty("comment");
		// -- ком.
		return ret;
	}

	// -- Возвращает код из узла
	public static String getCode(Vertex node) {
		// -- строим код
		if (node == null)
			return "";
		return (String) node.getProperty("code");

		// -- //--
	}

	// -- message()
	public static void message(String str) {
		// -- строим код
		System.out.println(str);
		// -- //--
	}

	// -- error()
	public static void error(String str) {
		// -- строим код
		System.err.println(str);
		// -- //--
	}

	// -- debug()
	public static void debug(String str) {
		// -- строим код
		if (Settings.isDebug())
			System.out.println(str);
		// -- //--
	}

	// -- getInDegree()
	public static int getInDegree(Vertex v) {
		// -- строим код
		int i = 0;
		for (Edge e : v.getInEdges()) {
			i++;
		}
		return i;

		// -- //--
	}

	// -- getOutDegree()
	public static int getOutDegree(Vertex v) {
		// -- строим код
		int i = 0;
		for (Edge e : v.getOutEdges()) {
			i++;
		}
		return i;

		// -- //--
	}

	// -- getInNode()
	public static Vertex getInNode(Vertex v, int num) {
		// -- строим код
		if (v == null)
			return null;
		int i = 0;
		for (Edge e : v.getInEdges()) {
			if (i == num)
				return e.getOutVertex();
			i++;
		}
		return null;
		// -- //--
	}

	// -- getOutNode()
	public static Vertex getOutNode(Vertex v, int num) {
		// -- строим код
		if (v == null)
			return null;
		int i = 0;
		for (Edge e : v.getOutEdges()) {
			if (i == num)
				return e.getInVertex();
			i++;
		}
		return null;
		// -- //--
	}

	// -- getOutEdge()
	public static Edge getOutEdge(Vertex v, int num) {
		// -- строим код
		if (v == null)
			return null;
		int i = 0;
		for (Edge e : v.getOutEdges()) {
			if (i == num)
				return e;
			i++;
		}
		return null;
		// -- //--
	}

	// -- isEdgeYes()
	public static boolean isEdgeYes(Edge edge) {
		// -- строим код
		if (edge == null)
			return false;

		String di_type_edge = (String) edge.getProperty("dglabel");
		if (di_type_edge == null)
			return false;

		if (di_type_edge.toUpperCase().equals("ДА")
				|| di_type_edge.toUpperCase().equals("YES"))
			return true;
		return false;
		// -- //--
	}

	// -- isReferenceEdge()
	public static boolean isReferenceEdge(Edge edge) {
		// -- строим код
		if (edge == null)
			return false;

		String di_type_edge = (String) edge.getProperty("type");
		if (di_type_edge == null)
			return false;

		if (di_type_edge.toUpperCase().equals("REF"))
			return true;
		return false;
		// -- //--
	}

	// -- main
	public static void main(String[] args) {
		// -- //--
	}

	// --

} // -- конец класса


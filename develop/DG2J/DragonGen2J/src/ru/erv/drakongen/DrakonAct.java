//-- Класс DrakonAct
//-- упоминание о DrakonGen2
/**
 * Этот текст сгенерирован программой DrakonGen2
 * @author Erv +
 */
//-- package//-- imports
package ru.erv.drakongen;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;

import ru.erv.drakongen.parser.GraphMLReader;
import ru.erv.drakongen.utils.FileUtils;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

//-- class DrakonAct
public class DrakonAct {
	// -- константы
	public final static String DI_OS_ACTION = "OS_ACTION";
	public final static String DI_DG_LIFT_DOWN = "DG_LIFT_DOWN";
	public final static String DI_DG_LIFT_UP = "DG_LIFT_UP";
	public final static String DI_FILE = "FILE";

	// -- переменные
	protected String CURRENT_RELEASE = DrakonUtils.RELEASE_TYPE_CODE_AS;

	// private static final String BASE_DIR =
	// "..\\..\\..\\WRK\\DG2J\\DragonGen2J\\";
	Vertex in;

	// -- Конструктор
	public DrakonAct() {
		// -- //--
	}

	// -- Запускаем graph на выполнение
	public void activate_drakon(Graph graph) {

		// -- теперь текущий узел null
		Vertex cur_node = null;

		// -- Проходим по всем узлам
		for (Vertex v : graph.getVertices()) {
			// -- узел ЗАПУСК АКТИВНОСТЕЙ?
			if (DrakonUtils.getIconType(v).equals(DrakonUtils.DI_START_ACTS)) {
				// -- теперь текущий узел ЗАПУСК
				cur_node = v;

				// -- break
				break;
			} else {
			}
		}
		// -- нашли узел ЗАПУСКА?
		if (cur_node != null) {
			// -- Проходим по всем узлам ЗАПУСКА
			while (DrakonUtils.getOutDegree(cur_node) == 1) {
				// -- теперь текущий узел тот на который указывает выход
				cur_node = DrakonUtils.getOutNode(cur_node, 0);

				// -- узел НАЧАЛО?
				if (DrakonUtils.getIconType(cur_node).equals(
						DrakonUtils.DI_DG_BEG)) {
				} else {
					// -- ОШИБКА! У иконы ЗАПУСК АКТИВНОСТЕЙ неправильный тип
					// выхода.
					DrakonUtils
							.error("ОШИБКА! У иконы ЗАПУСК АКТИВНОСТЕЙ неправильный тип выхода \""
									+ DrakonUtils.getIconType(cur_node) + "\"");
					// -- //--
					return;
				}
				// -- ищем в кодограмме НАЧАЛО с таким же именем
				Vertex act_node = null;
				String name = DrakonUtils.getComment(cur_node);
				for (Vertex v : graph.getVertices()) {
					// -- узел НАЧАЛО?
					if (v != cur_node
							&& DrakonUtils.getIconType(v).equals(
									DrakonUtils.DI_DG_BEG)) {
						if (DrakonUtils.getComment(v).equals(name)) {
							act_node = v;
							break;
						}
					} else {
					}
				}
				// -- нашли?
				if (act_node != null) {
					// -- Выполняем силуэт от ТС
					activate_siluet(act_node);
				} else {
					// -- ОШИБКА! Не нашли узла с именем ... как у стартовой
					// последовательности.
					DrakonUtils.error("ОШИБКА! У иконы Начало \""
							+ DrakonUtils.getOutDegree(cur_node)
							+ "\" выходов!");
					// -- //--
					return;
				}
			}
		} else {
			// -- Запускаем graph на выполнение по одному
			activate_drakon_single(graph);
		}
		// -- //--
	}

	// -- Запускаем graph на выполнение по одному
	protected void activate_drakon_single(Graph graph) {

		// -- Проходим по всем узлам
		for (Vertex v : graph.getVertices()) {
			// -- узел НАЧАЛО?
			if (DrakonUtils.getIconType(v).equals(DrakonUtils.DI_DG_BEG)) {
				// -- Выполняем силуэт от ТС
				activate_siluet(v);
			} else {
			}
		}
		// -- -- Схема построена
		DrakonUtils.message("--- Конец активности ");
		// -- //--
	}

	// -- Выполняем силуэт от ТС
	protected void activate_siluet(Vertex v) {

		// -- извлекаем из Начало тип реальности
		CURRENT_RELEASE = (String) DrakonUtils.getCode(v);
		// -- ---
		DrakonUtils.message("--->Текущая реальность: " + CURRENT_RELEASE);
		// -- у текущего узла один выход?
		if (DrakonUtils.getOutDegree(v) == 1) {
			// -- теперь текущий узел тот на который указывает выход
			v = DrakonUtils.getOutNode(v, 0);

			// -- ---
			DrakonUtils.message("---> Разбираем силуэт ");
			// -- Разбираем силуэт
			parseSiluet(v);
		} else {
			// -- у текущего узла нет выходов?
			if (DrakonUtils.getOutDegree(v) == 0) {
			} else {
				// -- ОШИБКА! У иконы Начало ...//-- выходов!
				DrakonUtils.error("ОШИБКА! У иконы Начало \""
						+ DrakonUtils.getOutDegree(v) + "\" выходов!");
				// -- //--
				return;
			}
		}
		// -- -- Схема построена
		DrakonUtils.message("--- Конец активности ");
		// -- //--
	}

	// -- Парсер одного силуэта
	public void parseSiluet(Vertex node) {
		/**
		 * Парсер одного силуэта ДРАКОНА
		 * 
		 * @param var
		 *            graph_data
		 */
		// -- переменные
		String file_nm = "";
		// -- -psi- n:
		DrakonUtils.debug("-psi- n: " + DrakonUtils.getComment(node));
		// -- тип узла
		switch (DrakonUtils.getIconType(node)) {
		// -- DG_LIFT_UP
		case DI_DG_LIFT_UP:
			// -- получаем вход который не НАЧАЛО
			in = DrakonUtils.getInNode(node, 0);
			if (DrakonUtils.getIconType(in).equals(DrakonUtils.DI_DG_BEG)) {
				in = DrakonUtils.getInNode(node, 1);
			}

			// -- на входе икона ФАЙЛ?
			if (DrakonUtils.getIconType(in).equals(DI_FILE)) {
			} else {
				// -- На входе DG_LIFT_UP должна быть икона FILE
				DrakonUtils.error("На входе DG_LIFT_UP \""
						+ DrakonUtils.getComment(in)
						+ "\" должна быть икона типа FILE  !");
				// -- //--
				return;
			}
			// -- получаем выход
			Vertex out = DrakonUtils.getOutNode(node, 0);
			// -- на выходе икона ФАЙЛ?
			if (DrakonUtils.getIconType(out).equals(DI_FILE)) {
			} else {
				// -- На выходе DG_LIFT_UP должна быть икона FILE
				DrakonUtils.error("На выходе DG_LIFT_UP \""
						+ DrakonUtils.getComment(in)
						+ "\" должна быть икона типа FILE  !");
				// -- //--
				return;
			}
			// -- try
			try {
				// -- получаем имя входного файла со схемой
				file_nm = DrakonUtils.getCode(in);
				// -- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm;
				// -- производим лексический разбор
				CompilationUnit cu = JavaParser.parse(new File(file_nm));

				// -- получаем текст GML
				String res = cu.toGraphml();

				// -- получаем имя выходного файла кодограммы
				file_nm = DrakonUtils.getCode(out);
				// -- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm;
				// -- записываем текст в выходной файл кодограммы
				FileUtils.fileWrite(file_nm, res);

				// -- ---
				System.out.println(" ---> Записали файл " + file_nm);

				// -- catch
			} catch (Exception e) {
				System.err.println("Error on file: " + file_nm + " "
						+ e.getMessage());
				e.printStackTrace();
				return;
			}

			// -- break
			break;
		// -- неизвестный тип
		default:
			// -- Неизвестный тип активности ...
			DrakonUtils.error("Неизвестный тип активности  \""
					+ DrakonUtils.getIconType(node) + "\" узла  \""
					+ DrakonUtils.getComment(node) + " \".\n");
			// -- //--
			return;
			// -- DG_LIFT_DOWN
		case DI_DG_LIFT_DOWN:
			// -- получаем вход который не НАЧАЛО
			in = DrakonUtils.getInNode(node, 0);
			if (DrakonUtils.getIconType(in).equals(DrakonUtils.DI_DG_BEG)) {
				in = DrakonUtils.getInNode(node, 1);
			}

			// -- на входе икона ФАЙЛ?
			if (DrakonUtils.getIconType(in).equals(DI_FILE)) {
			} else {
				// -- На входе DG_LIFT_DOWN должна быть икона FILE
				DrakonUtils.error("На входе DG_LIFT_DOWN \""
						+ DrakonUtils.getComment(in)
						+ "\" должна быть икона типа FILE  !");
				// -- //--
				return;
			}
			// -- try
			try {
				// -- получаем имя входного файла со схемой
				file_nm = DrakonUtils.getCode(in);
				// -- считаем что имя файла относительно BASE_DIR
				file_nm = Settings.getProperty("BASE_DIR") + file_nm;
				// -- читаем файл GML ридером
				Graph graph = new TinkerGraph();
				GraphMLReader reader = new GraphMLReader(file_nm, graph);
				reader.read();

				// -- ---
				System.out.println(" ---> Прочитали файл " + file_nm);

				// -- производим генерацию кода из кодограммы
				DrakonGen2 dg = new DrakonGen2();
				dg.parse_drakon(graph);

				// -- catch
			} catch (Exception e) {
				System.err.println("Error on file: " + file_nm + " "
						+ e.getMessage());
				e.printStackTrace();
				return;
			}

			// -- break
			break;
		}
		// -- //--
	}

	// -- main
	public static void main(String[] args) {
		Settings.setDebug(true);
		
		// -- устанавливаем BASE_DIR
		Settings.setProperty("BASE_DIR", "C:/YandexDisk/FLASH/WRK/ConfexDG2J/../DG2J/DragonGen2J/");
		String MAIN_DG_FILE = "Main.graphml";
		MAIN_DG_FILE = Settings.getProperty("BASE_DIR") + "../../Schemes/" + MAIN_DG_FILE;
		// -- переменная da
		DrakonAct da
		// -- экземпляр класса DrakonAct
		= new DrakonAct();
		// -- строим Graph из файла Маin
		Graph graph = new TinkerGraph();

		GraphMLReader reader = new GraphMLReader(MAIN_DG_FILE, graph);

		try {
			reader.read();
			System.out.println(" <--- Прочитали файл " + MAIN_DG_FILE);
		} catch (Exception e) {
			System.err.println(" err " + e.getMessage());
			e.printStackTrace();
		}
		// -- выполнение активностей
		da.activate_drakon(graph);
		// -- //--
	}

	// --

} // -- конец класса


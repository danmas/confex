package net.confex.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.confex.tree.ITreeNode;
import net.confex.tree.VarsNode;

// DONE: Добавить поиск наборов переменных в родительских  

public class TreeUtils {

	
	/**
	 * Выполняет подстановки строк
	 * например C:\ANT_BUILD\GAS\#{SRC_DIR}
	 */
	public static String doAllSubstitutions(ITreeNode for_node,
			String src_string) {
		
		if (src_string.indexOf("#{")==-1)
			return src_string;
		
		String ret = src_string;

		ITreeNode parent = for_node.getParent();
		
		if (parent == null)
			return ret;
		//-- сначала для всех которые выше в той же ветке
		for (int i = parent.getChildNumber(for_node) - 1; i >= 0; i--) {
			ITreeNode cur_node = parent.getChildren()[i];
			if (cur_node instanceof VarsNode) {
				// -- производим подстановку
				ret = doAllPercSubst(ret, ((VarsNode)cur_node).getVarsMap());
			}
		}
		
		//-- потом для родительского узла
		if (parent instanceof VarsNode) {
			ret = doAllPercSubst(ret, ((VarsNode)parent).getVarsMap());
		}
		//-- потом рекурсивно вверх
		ret = doAllSubstitutions(parent,ret);

		//-- and last setting up internal confex vars 
		HashMap int_vars = for_node.getConfigTree().getInternalVars();
		return doAllPercSubst(ret, int_vars);
	} 

	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Выполняет подстановки строк
	// например "sfkskjf #{fsdfas} asdfas"
	// ! SLOW
	// здесь ограничение на то что и ключи и величины должны быть строками
	public static String doAllPercSubst(String src_str, HashMap context) {
		String s_ret = src_str;
		Iterator it = context.entrySet().iterator();
		// -- 
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (entry.getKey() != null) {
				// -- есть что подставлять?
				if (s_ret.indexOf("#{") == -1)
					return s_ret;

				String s_key = (String) entry.getKey();
				s_key = "#{" + s_key + "}";

				if (s_ret.indexOf(s_key) != -1) {
					String s_val = (String) entry.getValue();
					s_ret = Strings.replace(s_ret, s_key, s_val);
				}
			}
		}
		return s_ret;
	}

	
	/**
	 * Ищем вверх по всем наборам пременных пока не найдем переменную с 
	 * именем var_name ей присваиваем значение value
	 */
	public static void setVarUp(ITreeNode for_node, String var_name, String value) {
		if (var_name==null||var_name.equals(""))
			return;
		

		ITreeNode parent = for_node.getParent();
		if (parent == null)
			return;
		
		//-- сначала для всех которые выше в той же ветке
		for (int i = parent.getChildNumber(for_node) - 1; i >= 0; i--) {
			ITreeNode cur_node = parent.getChildren()[i];
			if (cur_node instanceof VarsNode) {
				if( ((VarsNode)cur_node).containsVar(var_name) ) {
					//System.out.println(" Выставляем переменную "+ var_name+" = "+value);
					((VarsNode)cur_node).changeVar(var_name,value);
					return;
				}
			}
		}
		
		//-- потом для родительского узла
		if (parent instanceof VarsNode) {
			if( ((VarsNode)parent).containsVar(var_name) ) {
				System.out.println(" Выставляем переменную "+ var_name+" = "+value);
				((VarsNode)parent).changeVar(var_name,value);
				return;
			}
		}
		//-- потом рекурсивно вверх
		setVarUp(parent, var_name, value);
		
	}


	/**
	 * Ищем вверх по всем наборам пременных пока не найдем переменную с 
	 * именем var_name
	 * 
	 * @return null if not found
	 */
	public static String getStrVarUp(ITreeNode for_node, String var_name) {
		if (var_name==null||var_name.equals(""))
			return null;

		//-- first of all search internal vars
		if(var_name.equals("CONFEX_DIR")) {
			return (String)for_node.getConfigTree().getInternalVars().get("CONFEX_DIR");
		}
		
		
		ITreeNode parent = for_node.getParent();
		if (parent == null)
			return null;
		
		//-- сначала для всех которые выше в той же ветке
		for (int i = parent.getChildNumber(for_node) - 1; i >= 0; i--) {
			ITreeNode cur_node = parent.getChildren()[i];
			if (cur_node instanceof VarsNode) {
				if( ((VarsNode)cur_node).containsVar(var_name) ) {
					return ((VarsNode)cur_node).getStrVar(var_name);
				}
			}
		}
		
		//-- потом для родительского узла
		if (parent instanceof VarsNode) {
			if( ((VarsNode)parent).containsVar(var_name) ) {
				return ((VarsNode)parent).getStrVar(var_name);
			}
		}
		//-- потом рекурсивно вверх
		return getStrVarUp(parent, var_name);
	}

	
	/**
	 * Search node with given name up to tree.
	 *  
	 * @param node_name
	 * @return null if not found
	 */
	public static ITreeNode searchNodeUp(ITreeNode for_node, String node_name) {
		if (node_name==null||node_name.equals(""))
			return null;

		ITreeNode parent = for_node.getParent();
		if (parent == null)
			return null;
		
		//-- сначала для всех которые выше в той же ветке
		for (int i = parent.getChildNumber(for_node) - 1; i >= 0; i--) {
			ITreeNode cur_node = parent.getChildren()[i];
			if (cur_node.getName().equals(node_name)) {
				return cur_node; 
			}
		}
		
		if (parent.getName().equals(node_name)) {
			return parent; 
		}
		
		//-- потом рекурсивно вверх
		return searchNodeUp(parent, node_name);
	}
	
	
	/**
	 * Search node with given class type up to tree.
	 *  
	 * @param node_name
	 * @return null if not found
	 */
	public static ITreeNode searchNodeUp(ITreeNode for_node, Class type) {

		ITreeNode parent = for_node.getParent();
		if (parent == null)
			return null;

		//-- сначала для всех которые выше в той же ветке
		for (int i = parent.getChildNumber(for_node) - 1; i >= 0; i--) {
			ITreeNode cur_node = parent.getChildren()[i];
			if (type.isInstance(cur_node)) {
				return cur_node; 
			}
		}
		if (type.isInstance(parent)) {
			return parent; 
		}
		
		//-- потом рекурсивно вверх
		return searchNodeUp(parent, type);
	}
	
}

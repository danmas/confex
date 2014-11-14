package net.confex.db.ora;

import net.confex.db.JdbcConnection;
import net.confex.db.tree.JdbcConnectionNode;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import net.confex.tree.ITreeNode;
import net.confex.utils.TreeUtils;


public class OraJdbcConnectionNode extends JdbcConnectionNode {

	
	public String getAboutString() { 
		return Translator.getString("ABOUT_ORAJDBCCONNECTIONNODE"); 
	}
	
	
	public OraJdbcConnectionNode(ConfigTree configTree, IStateObserver stateObserver) {
		super(configTree,stateObserver);
		super.setJdbcConnection(new OraConnection());	
		setUrl("jdbc:oracle:thin:@server:1521:database_name");
	}

	
	/*
	public OraJdbcConnectionNode(ConfigTree configTree,String name) {
		super(configTree,name);
		super.setJdbcConnection(new OraConnection());	
	}*/

	
	/**
	 * 	Cоздает экземпляр того же класса
	 * !!! Должен быть переопределен в дочерних классах 
	 * @return ITreeNode
	 */
	public ITreeNode createNewITreeNode() {
		return new OraJdbcConnectionNode(getConfigTree(),null); 
	}

	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Получение коннекции
	// Если коннекции еще нет или закрыта то она будет создана
	//
	public JdbcConnection getConnection() {
		return getJdbcConnection();
	}
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Открывается коннекция с клиентской машины
	// Если коннекции еще нет или закрыта то она будет создана
	// R: true - ok
	public boolean createConnection() {
		try {
			getJdbcConnection().createConnection(
					TreeUtils.doAllSubstitutions(this,getUrl())
					, TreeUtils.doAllSubstitutions(this,getUser())
					, TreeUtils.doAllSubstitutions(this,getPassword()));
			setRunState();
			return true;
		} catch(Exception ex) {
			//DebugConsole.getErrStream().println("sfdfsdf");
			
			setErrorState();
			return false;
		}
	}
	
	
	/**
	 * Создаем и открываем диалог свойств для данного класса
	 */
	/*public void openPropertyDialog(NavigationView view, Shell shell,
			boolean new_flg) {
		property_dialog.setNewFlg(new_flg);
		
		//addStateObserver(view);
		
		if(property_dialog==null) {
			property_dialog = new JdbcConnectionPropertyDialog(shell);
		}
		property_dialog.createSShell(); 
		property_dialog.setView(view);
		property_dialog.show();
		property_dialog.setTreeNode(this);
	}*/
	
	
	
}

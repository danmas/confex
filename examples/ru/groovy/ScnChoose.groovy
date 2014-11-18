import org.eclipse.swt.widgets.Composite
import groovy.swt.SwtBuilder
 
import groovy.sql.Sql

import net.confex.tree.ITreeNode
 
import org.eclipse.ui.part.ViewPart
import org.eclipse.core.runtime.IProgressMonitor
 
//ScnChoose form = new ScnChoose(parent,thisGroovyNode,runViewPart,monitor);
//form.run();
 
class ScnChoose{
            def Composite parent         //-- родительский композит
            def ITreeNode thisGroovyNode //-- этот узел в дереве
            def ViewPart  runViewPart    //-- Вид в котором выполняется форма
            def IProgressMonitor monitor //-- Монитор выполнения 
            
            int combo_count = 2
            
            SwtBuilder swt = new SwtBuilder()
            ComboBox[] combos = new ComboBox[2];
            String[] vars = new String[2];
            //l_vars = new String[2]//new String("var_rn");
            //vars.add "var_scenario" 
 
            ScnChoose(){}
            
            ScnChoose(Composite p_parent
                                   , ITreeNode p_thisGroovyNode
                                   , ViewPart p_runViewPart
                                   ,IProgressMonitor p_monitor
                                   ) {
                        parent = p_parent
                        thisGroovyNode = p_thisGroovyNode
                        runViewPart = p_runViewPart
                        monitor = p_monitor
 
            }
            
            public static void main(String[] args) {
            }
 
            public void run() {
                Utils.thisGroovyNode = thisGroovyNode
            	Utils.getSql()
                        try {
                                   //-- включаем swt построитель в родительский композит                                  
                                   swt.setCurrent(parent);
                                   vars[0] = "var_rn"
                                   vars[1] = "var_scenario"
                                               
                                   combos[0] = new ComboBox(thisGroovyNode, swt, "Рабочий набор", thisGroovyNode.getChildWithName("table_rn").getFullText(), this, true)
                                   combos[1] = new ComboBox(thisGroovyNode, swt, "Сценарий", thisGroovyNode.getChildWithName("table_scn").getFullText(), this, true )
                                   
                                   swt.composite { 
                                               //-- задаем тип планировщика
                                       gridLayout(numColumns:1)          
                                               gridData( style:"fill_both" )    //-- растягивать во все стороны
                                               composite { //-- button comp
                                                           gridData( style:"fill_horizontal" )
                                    gridLayout(numColumns:5) 
                                                           button( style:"push",text:"#{btn_refresh}", background:[0, 255, 255] )  {
                                                                       onEvent(type:"Selection", closure:{
                                                                                              this.refresh()
                                                                                  })  
                                                           } //-- refresh
                                                           combos[0].composite()
                                                           combos[1].composite()
                                               } //-- buttons comp
                                   } //-- swt.composite
                                   this.refresh()
                        } catch( Exception e) {
                                   System.err.println(" "+e.getMessage())
                        }
            } //-- run()                      
            
            public void setUpSession(int idx){
                        def node = thisGroovyNode
                        try{                   
                                   while ( node.getName() != "IMAP"){
                                               node = node.getParent()
                                   }
                                   node = node.getChildWithName("session")
                                   node.changeVar(vars[idx], combos[idx].getID())
                        } catch( Exception e) {   System.err.println(" "+e.getMessage()) }
            }
            
            void refresh(){
                        (0..combo_count-1).each{i->
                                   combos[i].Fill()
                                   setUpSession(i)                        
                        }
            }
}


import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Combo
import org.eclipse.swt.widgets.Label
import groovy.swt.SwtBuilder
 
import net.confex.tree.ITreeNode
import org.eclipse.swt.graphics.Color as ColorSwt
import org.eclipse.swt.widgets.Display
 
//ComboBox combo1 = new ComboBox(thisGroovyNode,null,"", "");
 
public class ComboBox{
 
            def ITreeNode thisGroovyNode //-- этот узел в дереве
 
            private List combo_list = [][]
            private int combo_idx = 0
            private Label combo_label = null
            private Label combo_name = null
            private Combo combo_combo = null
            private String name, query
            private def comp = null
            private SwtBuilder swt = null
            private def form = null
            boolean refresh = false
 
            ComboBox() {}
            
            ComboBox(ITreeNode thisGroovyNode_, SwtBuilder swt_, String name_, String query_, def form_ = null, boolean refresh_ = false){
                        thisGroovyNode = thisGroovyNode_
                        swt =  swt_
                        name = name_
                        query = query_
                        form = form_
                        refresh = refresh_
            }
            
            public Composite composite(){ 
                        comp = swt.composite {
                            gridLayout(numColumns:3)
                                   gridData( style:"fill_horizontal" )
                        combo_name = label( style:"none", text:"$name" )       
                        combo_combo = combo(text:"$combo_list[][]", items:combo_list[][]) {  
                                               onEvent(type:"DefaultSelection", closure:{
                                                           try {
                                                                       this.onSelect()
                                                           } catch(Exception e) {
                                                                       System.err.println e.getMessage()
                                                           }
                                               });
                                               onEvent(type:"Selection", closure:{
                                                           try {
                                                                       this.onSelect()
                                                           } catch(Exception e) {
                                                                       System.err.println e.getMessage()
                                                           }
                                               });
                        } //-- combo combo
                    combo_label = label( style:"none", text:"Значение не выбранно" )
                                   combo_label.setForeground(new ColorSwt(Display.getCurrent(), 250, 0, 0))
                        }
                        return comp
            }
            void onSelect(){
                        try {
                                   combo_idx = combo_combo.getSelectionIndex()
                                   combo_label.text = combo_list[0][combo_idx].toString()
                                   combo_label.enabled = false                             
                                   thisGroovyNode.setVarUp("combo_cur_id_"+name, combo_list[0][combo_idx].toString());
                                   thisGroovyNode.setVarUp("combo_cur_"+name, combo_list[1][combo_idx]);
                                   if(refresh && form){
                                               form.refresh()
                                   }
                                   
                        } catch(Exception e) {    System.err.println(e.getMessage())  }                 
            }
            
            void Fill(){
            	println "Fill()"
                        try {
                                   int x_size = Utils.getSql().rows(query as String)[0].size()
                                   int y_size = Utils.getSql().rows(query as String).size()
                                   combo_list = new Object[x_size][y_size]
                                   Utils.getSql().eachRow( query as String ) {
                                               row->
                                               (1..row.getMetaData().getColumnCount()).each(){i->
                                                           combo_list[i-1][row.getRow()-1] = (String)row[i-1];
//                                                         println row.getMetaData().getColumnName(i) //+ row.getMetaData().getTableName(i)
                                               } //*/
                                   }
//                                 println combo_list
                                   if(combo_combo!=null) {
                                               combo_combo.items = combo_list[1]
                                   } else {
                                               System.err.println "combo_combo==null" 
                                   }
                                   this.FirstSelection()
                        } catch(Exception e) {
                                   System.err.println e.getMessage() 
//                                 MessageDialog.openError(main.parent.getShell(), "Ошибка!", e.getMessage())
                        }                       
            }
            
            void FirstSelection(){
                        if(combo_list[1]!=null)
                                   combo_combo.text = combo_list[1][combo_idx]
                        else
                                   combo_combo.text = ""
                        combo_label.text = combo_list[0][combo_idx].toString()
                        combo_label.enabled = false                             
                        thisGroovyNode.setVarUp("combo_cur_id_"+name, combo_list[0][combo_idx].toString());
                        thisGroovyNode.setVarUp("combo_cur_"+name, combo_list[1][combo_idx]);
            }
            
            String getID(){
                        return combo_list[0][combo_idx]
            }
            
}
import org.eclipse.jface.action.Action;
import net.confex.utils.ImageResource
import org.eclipse.jface.resource.ImageDescriptor;

class ActionMy extends Action {

	public ActionMy() { 
		try {
			//def icon_file_name = "D:\\Eclipse\\WRK_CONFEX\\net.confex\\icons\\plus.gif" 
			def icon_file_name = "/icons/sample.gif" 
			//def image = ImageDescriptor.createFromFile(null,icon_file_name)
		if (ImageResource.getImageDescriptor(icon_file_name) == ImageDescriptor
				.getMissingImageDescriptor()) {
			ImageResource.registerImage(icon_file_name,icon_file_name);
			println "missing!!!"
		}
			if(ImageResource.getImageDescriptor(icon_file_name)==null) {
				println "null!!!"
				ImageResource.registerImage(icon_file_name,icon_file_name);
			}
			def image = ImageResource.getImageDescriptor(icon_file_name)
			setImageDescriptor(image)
		} catch( Exception e) {
			System.err.println(" "+e.getMessage())
		}
	}
	
    public void run() {
    	println "!!!!!!!!"
    	//Example1 e = new Example1();
    	//println "!!!!!!!! "+ e.swt
    }
}
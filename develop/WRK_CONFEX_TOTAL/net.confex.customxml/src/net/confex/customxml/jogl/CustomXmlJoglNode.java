package net.confex.customxml.jogl;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;

import net.confex.customxml.CustomXmlNode;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;

import org.eclipse.ui.IViewPart;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.opengl.util.Animator;


public class CustomXmlJoglNode  extends CustomXmlNode {

	public String getAboutString() {
		return Translator.getString("ABOUT_CustomXmlJoglNode");
	}

	protected static String default_image_name = "cube.png";

	public String getDefaultImage() {
		return default_image_name;
	}

	public static String getDefaultImageName() {
		return default_image_name;
	}

	public CustomXmlJoglNode(ConfigTree configTree,
			IStateObserver stateObserver) {
		super(configTree, stateObserver);
	}

	
	/**
	 * Just a test!!!
	 */
	public void run(IViewPart view) {
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();

		JFrame jf = new JFrame("JOGL Test");
		// jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		jf.setBounds(0, 0, screenSize.width / 2, screenSize.height / 2);
	    //Frame frame = new Frame("Gear Demo");
		
	    GLCanvas canvas = new GLCanvas();

	    Gears gears = new Gears(canvas);
	    canvas.addGLEventListener(gears);
	    canvas.addKeyListener(gears);
	    
	    jf.add(canvas);
	    //jf.setSize(300, 300);
	    final Animator animator = new Animator(canvas);
	    jf.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          // Run this on another thread than the AWT event queue to
	          // make sure the call to Animator.stop() completes before
	          // exiting
	          new Thread(new Runnable() {
	              public void run() {
	                animator.stop();
	                //System.exit(0);
	              }
	            }).start();
	        }
	      });
	    //frame.show();
	    animator.start();
		//jf.add("Center", canvas3D);

		jf.setVisible(true);
	}

	
   	//Canvas3D canvas3D;
   	
	/**
	 * Custom method with three params. Main entry point.
	 * 
	 * @param par1 - CustomXmlNode
	 * @param par2 - maps of objects
	 * @param par3 - not used
	 *            
	 * @return 
	 */
	public Object customMethod3(Object par1, Object par2, Object par3) {

		if (!(par1 instanceof CustomXmlNode)) {
			System.err.println("par1 not instanceof CustomXmlNode!");
			return null;
		}
		if(!(par2 instanceof HashMap)) {
			 System.err.println("par3 not instanceof HashMap<String,Object>!");
			return null;
			}
		/*
		if(!(par3 instanceof Canvas3D)) {
			 System.err.println("par3 not instanceof Canvas3D!");
			return null;
			}
		canvas3D = (Canvas3D)par3;
		
		NamedObject no = customXmlBuild((CustomXmlNode) par1,
				(HashMap<String, Object>) par2, null);
		return no.obj;
				*/
		return null;
	}

	/*
	 * Build TransformGroup,BranchGroup
	 */
	/*
	protected NamedObject customXmlBuild(CustomXmlNode tree_node,
			HashMap<String, Object> objects_map, NamedObject parent_no) {
		try {
			String xml_str = tree_node.getCustomXml();
			// -- Create a DocumentBuilderFactory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// -- Create a DocumentBuilder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// -- Parse the input file to get a Document object
			Document doc = db.parse(new InputSource(new java.io.StringReader(
					xml_str)));
			// -- получаем первый элемент
			Element e = doc.getDocumentElement();
			
			if (e.getNodeName().equals("TransformGroup")) {
				return buildTransformGroup(e,tree_node,objects_map);
			} else if (e.getNodeName().equals("Transform3D")) {
				return buildTransform3D(e,tree_node,objects_map);
			} else if (e.getNodeName().equals("MouseTranslate")) {
				return buildMouseTranslate(e,tree_node,objects_map);
			} else if (e.getNodeName().equals("MouseZoom")) {
				return buildMouseZoom(e,tree_node,objects_map);
			} else if (e.getNodeName().equals("MouseRotate")) {
				return buildMouseRotate(e,tree_node,objects_map);
			} else if (e.getNodeName().equals("PickRotateBehavior")) {
				return builPickRotateBehavior(e,tree_node,objects_map,parent_no);
			} else if (e.getNodeName().equals("PickTranslateBehavior")) {
				return builPickTranslateBehavior(e,tree_node,objects_map,parent_no);
			} else if (e.getNodeName().equals("PickZoomBehavior")) {
				return builPickZoomBehavior(e,tree_node,objects_map,parent_no);
			} else if (e.getNodeName().equals("BranchGroup")) {
				return buildBranchGroup(e,tree_node,objects_map);
			} else if (e.getNodeName().equals("ColorCube")) {
				return buildColorCube(e,tree_node,objects_map);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	protected NamedObject buildColorCube(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();
		String size = e.getAttribute("size");
		if (size == null)
			size = "0.5";

		System.out.println(" ColorCube " + name + " = new ColorCube("
				+ size + ");");
		ColorCube cc = new ColorCube(new Double(size));
		NamedObject cc_no = new NamedObject(name,cc);
		if(objects_map.get(name)!=null) {
			System.err.println("Object "+name+" already exist!");
		}
		objects_map.put(name, cc);
		return cc_no;
	}
	
	protected NamedObject buildBranchGroup(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map) {
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();

		System.out.println(" BranchGroup " + name
				+ " = new BranchGroup();");
		BranchGroup bg = new BranchGroup();
		NamedObject bg_no = new NamedObject(name,bg);
		if(objects_map.get(name)!=null) {
			System.err.println("Object "+name+" already exist!");
		}
		objects_map.put(name, bg);

		for (int i = 0; i < tree_node.getChildren().length; i++) {
			if (tree_node.getChildren()[i] instanceof CustomXmlNode
					&& tree_node.getChildren()[i].isUsed())  {
				NamedObject obj = customXmlBuild((CustomXmlNode) tree_node
						.getChildren()[i], objects_map, bg_no);
				if (obj.obj instanceof javax.media.j3d.Node) {
					System.out.println(name
							+ ".addChild("+obj.name+");");
					bg.addChild((javax.media.j3d.Node) obj.obj);
				} else {
					System.err
							.println(" Cant do "
									+ name
									+ ".addChild() not javax.media.j3d.Node type!");
				}
			}
		}
		return bg_no;
	}

	
	private NamedObject buildMouseBehavior(Element e, String name,MouseBehavior mb,
			HashMap<String, Object> objects_map) {
		NamedObject mt_no = new NamedObject(name,mb);
		if(objects_map.get(name)!=null) {
			System.err.println("Object "+name+" already exist!");
		}
		objects_map.put(name, mb);
		//-- some atributes
		
		String tg_name = e.getAttribute("TransformGroup");
		if (tg_name != null && !tg_name.equals("")) {
			 Object o = objects_map.get(tg_name);
			 if(o instanceof TransformGroup) {
					System.out.println(name
							+ ".setTransformGroup("+tg_name+");");
					mb.setTransformGroup((TransformGroup) o);
			 } else {
				 System.err.println("Can't found TransformGroup "+tg_name);
			 }
		}
		System.out.println(name
				+ ".setSchedulingBounds(new BoundingSphere());");
	    mb.setSchedulingBounds(new BoundingSphere());
		return mt_no;
	}
	
	protected NamedObject buildMouseTranslate(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();

		System.out.println(" MouseTranslate " + name
				+ " = new MouseTranslate();");
		MouseTranslate mt = new MouseTranslate();
		return buildMouseBehavior(e, name, mt, objects_map);
	}
	
	
	protected NamedObject buildMouseZoom(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();

		System.out.println(" MouseZoom " + name
				+ " = new MouseZoom();");
		MouseZoom mt = new MouseZoom();
		return buildMouseBehavior(e, name, mt, objects_map);
	}
	
	protected NamedObject buildMouseRotate(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();

		System.out.println(" MouseRotate " + name
				+ " = new MouseRotate();");
		MouseRotate mt = new MouseRotate();
		return buildMouseBehavior(e, name, mt, objects_map);
	}
	
	
	protected NamedObject builPickRotateBehavior(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map, NamedObject parent_no) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();

		if(!(parent_no.obj instanceof BranchGroup)) {
			System.err.println(parent_no.name + " NOT instanceof BranchGroup! for "+name);
			return null;
		}
		System.out.println(" PickRotateBehavior " + name
				+ " = new PickRotateBehavior("+parent_no.name+",canvas3D,new BoundingSphere());");
		PickRotateBehavior ptb = new PickRotateBehavior((BranchGroup)parent_no.obj, canvas3D, new BoundingSphere());
		if(objects_map.get(name)!=null) {
			System.err.println("Object "+name+" already exist!");
			return null;
		}
		objects_map.put(name, ptb);
		return new NamedObject(name,ptb);
	}
	
	
	protected NamedObject builPickTranslateBehavior(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map, NamedObject parent_no) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();

		if(!(parent_no.obj instanceof BranchGroup)) {
			System.err.println(parent_no.name + " NOT instanceof BranchGroup! for "+name);
			return null;
		}
		System.out.println(" PickTranslateBehavior " + name
				+ " = new PickTranslateBehavior("+parent_no.name+",canvas3D,new BoundingSphere());");
		PickTranslateBehavior ptb = new PickTranslateBehavior((BranchGroup)parent_no.obj, canvas3D, new BoundingSphere());
		if(objects_map.get(name)!=null) {
			System.err.println("Object "+name+" already exist!");
			return null;
		}
		objects_map.put(name, ptb);
		return new NamedObject(name,ptb);
	}
	
	
	protected NamedObject builPickZoomBehavior(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map, NamedObject parent_no) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();

		if(!(parent_no.obj instanceof BranchGroup)) {
			System.err.println(parent_no.name + " NOT instanceof BranchGroup! for "+name);
			return null;
		}
		System.out.println(" PickZoomBehavior " + name
				+ " = new PickZoomBehavior("+parent_no.name+",canvas3D,new BoundingSphere());");
		PickZoomBehavior ptb = new PickZoomBehavior((BranchGroup)parent_no.obj, canvas3D, new BoundingSphere());
		if(objects_map.get(name)!=null) {
			System.err.println("Object "+name+" already exist!");
			return null;
		}
		objects_map.put(name, ptb);
		return new NamedObject(name,ptb);
	}
	
	
	protected NamedObject buildTransformGroup(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();

		System.out.println(" TransformGroup " + name
				+ " = new TransformGroup();");
		TransformGroup tg = new TransformGroup();
		NamedObject tg_no = new NamedObject(name,tg);
		if(objects_map.get(name)!=null) {
			System.err.println("Object "+name+" already exist!");
		}
		objects_map.put(name, tg);
		//-- some atributes
		
		String a = e.getAttribute("ALLOW_TRANSFORM_WRITE");
		if (a != null && !a.equals("")) {
		       tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		       System.out.println(name+".setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);");
		}
		a = e.getAttribute("ALLOW_TRANSFORM_READ");
		if (a != null && !a.equals("")) {
		       tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		       System.out.println(name+".setCapability(TransformGroup.ALLOW_TRANSFORM_READ);");
		}
		a = e.getAttribute("ENABLE_PICK_REPORTING");
		if (a != null && !a.equals("")) {
		       tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		       System.out.println(name+".setCapability(TransformGroup.ENABLE_PICK_REPORTING);");
		}
		for (int i = 0; i < tree_node.getChildren().length; i++) {
			if (tree_node.getChildren()[i] instanceof CustomXmlNode
					&& tree_node.getChildren()[i].isUsed()) {
				NamedObject obj = customXmlBuild((CustomXmlNode) tree_node
						.getChildren()[i], objects_map, tg_no);
				if (obj.obj instanceof javax.media.j3d.Node) {
					System.out.println(name
							+ ".addChild("+obj.name+");");
					tg.addChild((javax.media.j3d.Node) obj.obj);
				} else if (obj.obj instanceof Transform3D) {
					System.out.println(name
							+ ".setTransform("+obj.name+");");
					tg.setTransform((Transform3D) obj.obj);
				} else {
					System.err.println(" Cant do " + name
							+ ".addChild()/setTransform()!");
				}
			}
		}
		return tg_no;
	}
	
	
	protected NamedObject buildTransform3D(Element e, CustomXmlNode tree_node,
			HashMap<String, Object> objects_map) {
		// -- его атрибуты
		String name = e.getAttribute("name");
		if (name == null||name.equals(""))
			name = tree_node.getName();
	
		System.out.println(" Transform3D " + name
				+ " = new Transform3D();");
		Transform3D t3d = new Transform3D();
		NamedObject t3d_no = new NamedObject(name,t3d);
		if(objects_map.get(name)!=null) {
			System.err.println("Object "+name+" already exist!");
		}
		objects_map.put(name, t3d);
		//-- internal structure
		for (int i = 0; i < e.getChildNodes().getLength(); i++) {
			Node node = e.getChildNodes().item(i);
			//System.out.println("name "+node.getNodeName());
			if(node.getNodeName().equals("translation")) {
				String sx = node.getAttributes().getNamedItem("x").getNodeValue();
				String sy = node.getAttributes().getNamedItem("y").getNodeValue();
				String sz = node.getAttributes().getNamedItem("z").getNodeValue();
				t3d.setTranslation(new Vector3d(
						Double.valueOf(sx), Double.valueOf(sy),Double.valueOf(sz)));
				System.out.println(
						name+".setTranslation(new Vector3d("+sx+","+sy+","+sz+"));");
			}
			if(node.getNodeName().equals("rotX")) {
				String sa = node.getAttributes().getNamedItem("angle_in_PI").getNodeValue();
				t3d.rotX(Math.PI*Double.valueOf(sa));
				System.out.println(
						name+".rotX(Math.PI*"+sa+"));");
			}
			if(node.getNodeName().equals("rotY")) {
				String sa = node.getAttributes().getNamedItem("angle_in_PI").getNodeValue();
				t3d.rotY(Math.PI*Double.valueOf(sa));
				System.out.println(
						name+".rotY(Math.PI*"+sa+"));");
			}
			if(node.getNodeName().equals("rotZ")) {
				String sa = node.getAttributes().getNamedItem("angle_in_PI").getNodeValue();
				t3d.rotZ(Math.PI*Double.valueOf(sa));
				System.out.println(
						name+".rotZ(Math.PI*"+sa+"));");
			}
		}
		
		for (int i = 0; i < tree_node.getChildren().length; i++) {
			if (tree_node.getChildren()[i] instanceof CustomXmlNode
					&& tree_node.getChildren()[i].isUsed()) {
				NamedObject obj = customXmlBuild((CustomXmlNode) tree_node
						.getChildren()[i], objects_map, t3d_no);
				if (obj.obj instanceof Transform3D) {
					System.out
							.println(name + ".mul("+obj.name+");");
					t3d.mul((Transform3D) obj.obj);
				} else {
					System.err
							.println(" Cant do "
									+ name
									+ ".addChild() not javax.media.j3d.Node type!");
				}
			}
		}
		return t3d_no;
	}
	
	
	class NamedObject {
		public String name;
		public Object obj;
		
		public NamedObject(String name, Object obj) {
			this.name = name; this.obj = obj;
		}
	}
	
	*/
}

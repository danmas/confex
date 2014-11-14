package net.confex.customxml.java3D;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.confex.customxml.CustomXmlNode;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;

import org.eclipse.ui.IViewPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.pickfast.behaviors.PickRotateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickZoomBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class CustomXmlJava3DNode extends CustomXmlNode {

	public String getAboutString() {
		return Translator.getString("ABOUT_CustomXmlJava3DNode");
	}

	protected static String default_image_name = "cube.png";

	public String getDefaultImage() {
		return default_image_name;
	}

	public static String getDefaultImageName() {
		return default_image_name;
	}

	public CustomXmlJava3DNode(ConfigTree configTree,
			IStateObserver stateObserver) {
		super(configTree, stateObserver);
	}

	public void run2(IViewPart view) {
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();

		JFrame jf = new JFrame("test");
		// jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		jf.setBounds(0, 0, screenSize.width / 2, screenSize.height / 2);
		Canvas3D canvas3D = new Canvas3D(config);
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        BranchGroup scene = createSceneGraphPick(canvas3D);

	    simpleU.getViewingPlatform().setNominalViewingTransform();

	    simpleU.addBranchGraph(scene);
	 
		jf.add("Center", canvas3D);

		jf.setVisible(true);
	}

	/**
	 * Just a test!!!
	 */
	public void run(IViewPart view) {
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();

		JFrame jf = new JFrame("test");
		// jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		jf.setBounds(0, 0, screenSize.width / 2, screenSize.height / 2);
		Canvas3D canvas3D = new Canvas3D(config);

		HashMap<String, Object> objects_map = new HashMap<String, Object>();
		
		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		
		BranchGroup bg = (BranchGroup) customMethod3(this.getChildWithName("bg"), objects_map, canvas3D);

		//BranchGroup bg = (BranchGroup) objects_map.get("bg");
		simpleU.addBranchGraph(bg);

		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		simpleU.getViewingPlatform().setNominalViewingTransform();

		jf.add("Center", canvas3D);

		jf.setVisible(true);
	}

   public BranchGroup createSceneGraphPick(Canvas3D canvas3D) {
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();

	        TransformGroup objRotate = null;
	        PickRotateBehavior pickRotate = null;
	        Transform3D transform = new Transform3D();
	        BoundingSphere behaveBounds = new BoundingSphere();

	        // create ColorCube and PickRotateBehavior objects
	        transform.setTranslation(new Vector3f(-0.6f, 0.0f, -0.6f));
	        objRotate = new TransformGroup(transform);
	        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	        objRotate.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

	        objRoot.addChild(objRotate);
	        objRotate.addChild(new ColorCube(0.4));

	        pickRotate = new PickRotateBehavior(objRoot, canvas3D, behaveBounds);
	        objRoot.addChild(pickRotate);
	        
	        PickTranslateBehavior pickTranslate = new PickTranslateBehavior(objRoot, canvas3D, behaveBounds);
	        objRoot.addChild(pickTranslate);
	        
	        PickZoomBehavior pickZoom = new PickZoomBehavior(objRoot, canvas3D, behaveBounds);
	        objRoot.addChild(pickZoom);

	        // add a second ColorCube object to the scene graph
	        transform.setTranslation(new Vector3f( 0.6f, 0.0f, -0.6f));
	        objRotate = new TransformGroup(transform);
	        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	        objRotate.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

	        objRoot.addChild(objRotate);
	        objRotate.addChild(new ColorCube(0.4));

		// Let Java 3D perform optimizations on this scene graph.
	        objRoot.compile();

		return objRoot;
    } 
	
   	Canvas3D canvas3D;
   	
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
		if(!(par3 instanceof Canvas3D)) {
			 System.err.println("par3 not instanceof Canvas3D!");
			return null;
			}
		canvas3D = (Canvas3D)par3;
		
		NamedObject no = customXmlBuild((CustomXmlNode) par1,
				(HashMap<String, Object>) par2, null);
		return no.obj;
	}


	/*
	 * Build TransformGroup,BranchGroup
	 */
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
	
	/*
	  	public Object buildSimpleUniverse(CustomXmlNode su_tree_node,
			Canvas3D canvas3D, HashMap<String, Object> objects_map) {
		try {
			String xml_str = ((CustomXmlNode) su_tree_node).getCustomXml();

			// -- Create a DocumentBuilderFactory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// -- Create a DocumentBuilder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// -- Parse the input file to get a Document object
			Document doc = db.parse(new InputSource(new java.io.StringReader(
					xml_str)));

			// -- получаем первый элемент
			Element e = doc.getDocumentElement();

			if (e.getNodeName().equals("SimpleUniverse")) {
				// -- его атрибуты
				String name = e.getAttribute("name");
				if (name == null||name.equals(""))
					name = su_tree_node.getName();

				System.out.println(" SimpleUniverse " + name
						+ " = new SimpleUniverse();");
				SimpleUniverse su = new SimpleUniverse(canvas3D);
				objects_map.put(name, su);

				for (int i = 0; i < su_tree_node.getChildren().length; i++) {
					if (su_tree_node.getChildren()[i] instanceof CustomXmlNode
					&& tree_node.getChildren()[i].isUsed()) {
						NamedObject obj = customXmlBuild1(
								(CustomXmlNode) su_tree_node.getChildren()[i],
								objects_map);
						// if(obj instanceof BranchGroup) {
						// System.out.println(name+".addBranchGraph((BranchGroup)obj);");
						// su.addBranchGraph((BranchGroup)obj);
						// }
					}
				}
				return su;
				// return null;
			} else {
				System.err.println("Bad SimpleUniverse class "
						+ e.getNodeName());
				return null;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}

	}

	 */
}

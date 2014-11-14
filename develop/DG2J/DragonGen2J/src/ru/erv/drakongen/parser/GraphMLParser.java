/**
 * A SAX Parser for GraphML data file.
 * @author Kozo.Nishida
 *
 */

package ru.erv.drakongen.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.erv.drakongen.MyAttributes;
import ru.erv.drakongen.Settings;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;


public class GraphMLParser extends DefaultHandler {

	/* Internal lists of the created nodes and edges */
	private List<Vertex> nodeList = null;
	private List<Edge> edgeList = null;

	/* Map of XML ID's to nodes */
	private Map<String, Vertex> nodeidMap = null;

	/* Map of data type to nodes or edges */
	private Map<String, String> datatypeMap = null;

	private Vertex currentNode = null;
	private Vertex currentVertix = null;
	
	private Edge currentEdge = null;

	/* Attribute values */
	private String currentAttributeID = null;
	private String currentAttributeKey = null;
	private String currentAttributeData = null;
	private String currentAttributeType = null;
	private String currentEdgeSource = null;
	private String currentEdgeTarget = null;
	private String currentObjectTarget = null;
	private String currentQname = null;

	private MyAttributes nodeAttributes = new MyAttributes();
	//private MyAttributes edgeAttributes = new MyAttributes();

	/* node, edge, data parsing */
	private boolean directed = false;
	private int e_cnt = 0;
	
	Graph graph;
	
	/********************************************************************
	 * Routines to handle keys
	 *******************************************************************/

	/**
	 * Main constructor for our parser. Initialize any local arrays. Note that
	 * this parser is designed to be as memory efficient as possible. As a
	 * result, a minimum number of local data structures
	 */
	GraphMLParser(Graph graph) {
		nodeList = new ArrayList<Vertex>();
		edgeList = new ArrayList<Edge>();
		nodeidMap = new HashMap<String, Vertex>();
		datatypeMap = new HashMap<String, String>();
		this.graph = graph;
	}

	/********************************************************************
	 * Interface routines. These routines are called by the GraphMLReader to get
	 * the resulting data.
	 *******************************************************************/

	/*int[] getNodeIndicesArray() {

		System.out.println("Got nodes: " + nodeList.size());

		int[] array = new int[nodeList.size()];

		for (int i = 0; i < nodeList.size(); i++) {
			array[i] = nodeList.get(i).getRootGraphIndex();
		}
		return array;
	}*/

	/*
	int[] getEdgeIndicesArray() {

		System.out.println("Got edges: " + edgeList.size());

		int[] array = new int[edgeList.size()];
		for (int i = 0; i < edgeList.size(); i++) {
			array[i] = edgeList.get(i).getRootGraphIndex();
		}
		return array;
	}

	String getNetworkName() {
		return networkName;
	}*/

	/********************************************************************
	 * Handler routines. The following routines are called directly from the SAX
	 * parser.
	 *******************************************************************/

	public void startDocument() {

	}

	public void endDocument() throws SAXException {

	}

	boolean catch_label = false;
	boolean catch_edge_label = false;
	
	String d_aspect = "d114";
	String d_code_mark = "d4";
	String d_type = "d5";
	String d_code = "d6";
	//String d_edge_type = "d9";
	
	public void startElement(String namespace, String localName, String qName,
			Attributes atts) throws SAXException {
		if(Settings.isDebug())
			System.out.println("---> " + qName);
		if(qName.equals("y:NodeLabel")) {
			catch_label = true;
			if(Settings.isDebug())
				System.out.println("!Catch y:NodeLabel");
		}
		if(qName.equals("y:EdgeLabel")) {
			catch_edge_label = true;
			if(Settings.isDebug())
				System.out.println("!Catch y:EdgeLabel");
		}
		if (qName.equals(GraphMLToken.GRAPH.getTag())) {
			currentQname = GraphMLToken.GRAPH.getTag();
			// parse directed or undirected
			String edef = atts.getValue(GraphMLToken.EDGEDEFAULT.getTag());
			directed = GraphMLToken.DIRECTED.getTag().equalsIgnoreCase(edef);

			//this.networkName = atts.getValue(GraphMLToken.ID.getTag());

		} else if (qName.equals(GraphMLToken.KEY.getTag())) {
			currentQname = GraphMLToken.KEY.getTag();
			if (atts.getValue(GraphMLToken.FOR.getTag()).equals(
					GraphMLToken.NODE.getTag())) {
				datatypeMap.put(atts.getValue(GraphMLToken.ID.getTag()),
						atts.getValue(GraphMLToken.ATTRTYPE.getTag()));
				
				String s = atts.getValue(GraphMLToken.ID.getTag());
				String s2 = atts.getValue(GraphMLToken.ATTRNAME.getTag());
				if(s2!= null) {
					if(s2.equals("mark") || s2.equals("code_mark")) {
						d_code_mark = s;
					} else if(s2.equals("url")) {
						d_type = s;
					} else if(s2.equals("description")) {
						d_code = s;
					}else if(s2.equals("aspect")) {
						d_aspect = s;
					}
				}
			} else if (atts.getValue(GraphMLToken.FOR.getTag()).equals(
					GraphMLToken.EDGE.getTag())) {
				datatypeMap.put(atts.getValue(GraphMLToken.ID.getTag()),
						atts.getValue(GraphMLToken.ATTRTYPE.getTag()));
			} else if (atts.getValue(GraphMLToken.FOR.getTag()).equals(
					GraphMLToken.ALL.getTag())) {
				datatypeMap.put(atts.getValue(GraphMLToken.ID.getTag()),
						atts.getValue(GraphMLToken.ATTRTYPE.getTag()));
			}
		} else if (qName.equals(GraphMLToken.NODE.getTag())) {
			currentQname = GraphMLToken.NODE.getTag();
			// Parse node entry.
			currentObjectTarget = GraphMLToken.NODE.getTag();
			currentAttributeID = atts.getValue(GraphMLToken.ID.getTag());
			
			//currentNode = MyTest.getMyNode(currentAttributeID);
			
			currentVertix = graph.addVertex(currentNode);
			
			if(Settings.isDebug())
				System.out.println(" + new Node " + currentAttributeID);
			
			//nodeList.add(currentNode);
			nodeList.add(currentVertix);
			
			//nodeidMap.put(currentAttributeID, currentNode);
			nodeidMap.put(currentAttributeID, currentVertix);
			
		} else if (qName.equals(GraphMLToken.EDGE.getTag())) {
			currentQname = GraphMLToken.EDGE.getTag();
			// Parse edge entry
			currentObjectTarget = GraphMLToken.EDGE.getTag();
			currentEdgeSource = atts.getValue(GraphMLToken.SOURCE.getTag());
			currentEdgeTarget = atts.getValue(GraphMLToken.TARGET.getTag());
			
			Vertex sourceNode = nodeidMap.get(currentEdgeSource);
			Vertex targetNode = nodeidMap.get(currentEdgeTarget);
			
			if(Settings.isDebug())
				System.out.println(" + new Edge " + currentEdgeSource + " " + currentEdgeTarget);
			
			//JgraphT currentEdge = MyTest.addEdge(sourceNode, targetNode);
			
			String e_name = "e"+ e_cnt++;
			currentEdge = graph.addEdge(e_name,sourceNode, targetNode,"");
			
			edgeList.add(currentEdge);
		} else if (qName.equals(GraphMLToken.DATA.getTag())) {
			currentQname = GraphMLToken.DATA.getTag();
			currentAttributeKey = atts.getValue(GraphMLToken.KEY.getTag());
			currentAttributeType = datatypeMap.get(currentAttributeKey);
		}
	}

	public void characters(char[] ch, int start, int length) {
		currentAttributeData = new String(ch, start, length);
		if(catch_label) {
			//System.out.println(" === Label = " + currentAttributeData);
			//nodeAttributes.setAttribute(currentAttributeID,
			//		currentAttributeKey, currentAttributeData);
			
			//currentNode.setComment(currentAttributeData);
			//currentNode.setProperty("comment", currentAttributeData); 
			currentVertix.setProperty("comment", currentAttributeData);
			
			catch_label = false;
		} else if(catch_edge_label) {
			currentEdge.setProperty("dglabel", currentAttributeData);
			catch_edge_label = false;
		}
		if (currentObjectTarget != null) {
			if (currentObjectTarget.equals(GraphMLToken.NODE.getTag())) {
				if (currentAttributeType != null) {
					if (currentAttributeType.equals(GraphMLToken.STRING
							.getTag())) {
						// debug
						// System.out.println(currentAttributeData);
						//nodeAttributes.setAttribute(currentAttributeID,
						//		currentAttributeKey, currentAttributeData);
						if(currentAttributeKey.equals(d_code_mark)) {
							currentVertix.setProperty("code_mark", currentAttributeData);
						}
						if(currentAttributeKey.equals(d_type)) {
							currentVertix.setProperty("type", currentAttributeData);
						}
						if(currentAttributeKey.equals(d_code)) {
							currentVertix.setProperty("code", currentAttributeData);
						}
						if(currentAttributeKey.equals(d_aspect)) {
							currentVertix.setProperty("aspect", currentAttributeData);
						}

					} else if (currentAttributeType.equals(GraphMLToken.DOUBLE
							.getTag())) {
						// debug
						// System.out.println(currentAttributeData);
						nodeAttributes.setAttribute(currentAttributeID,
								currentAttributeKey,
								Double.parseDouble(currentAttributeData));
					}
				}
			} else if (currentObjectTarget.equals(GraphMLToken.EDGE.getTag())) {
				if (currentAttributeType != null) {
					if (currentAttributeType.equals(GraphMLToken.STRING
							.getTag())) {
						// debug
						System.err.println(" -!!!- edgeAttributes.setAttribute("+ currentAttributeData);
						currentEdge.setProperty("type", currentAttributeData);
//						edgeAttributes.setAttribute(
//								currentEdge.getIdentifier(),
//								currentAttributeKey, currentAttributeData);
					}
					if (currentAttributeType.equals(GraphMLToken.DOUBLE
							.getTag())) {
						// debug
						System.err.println(" -!!!- edgeAttributes.setAttribute("+ currentAttributeData);
						
//						edgeAttributes.setAttribute(
//								currentEdge.getIdentifier(),
//								currentAttributeKey,
//								Double.parseDouble(currentAttributeData));
					}
				}
			}
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(Settings.isDebug())
			System.out.println("<--- "+ currentQname);
		if (currentQname != GraphMLToken.DATA.getTag()) {
			currentObjectTarget = null;
		}
		currentAttributeType = null;
	}

}
package ru.erv.drakongen;

import java.net.MalformedURLException;

//import org.jgrapht.DirectedGraph;
//import org.jgrapht.graph.DefaultDirectedGraph;
//import org.jgrapht.graph.DefaultEdge;

//import com.tinkerpop.blueprints.pgm.Edge;
//import com.tinkerpop.blueprints.pgm.Vertex;

public class MyTest {
	
	//static String src = "c:/YandexDisk/FLASH/WRK/JavaWRK/graphml/graphmlreader.graphml";
	static String src = "c:/YandexDisk/FLASH/WRK/JavaWRK/graphml/yEd_simple.graphml";

	//static DirectedGraph<Vertex, DefaultEdge> graph =
    //        new DefaultDirectedGraph<Vertex, DefaultEdge>(DefaultEdge.class);

    public static void main(String[] args) {
//        DirectedGraph<Vertex, DefaultEdge> graph =
//                new DefaultDirectedGraph<Vertex, DefaultEdge>(DefaultEdge.class);
        	/*
            try {
            	MyNode amazon = new MyNode("amazon");
            	MyNode yahoo = new MyNode("yahoo");
            	MyNode ebay = new MyNode("ebay");

                // add the vertices
                graph.addVertex(amazon);
                graph.addVertex(yahoo);
                graph.addVertex(ebay);

                // add edges to create linking structure
                graph.addEdge(yahoo, amazon);
                graph.addEdge(yahoo, ebay);
                
                System.out.println( graph.inDegreeOf(amazon) );
                System.out.println( graph.inDegreeOf(yahoo) );
                System.out.println( graph.outDegreeOf(amazon) );
                System.out.println( graph.outDegreeOf(yahoo) );
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
		
            
		/*
		GraphMLReader reader = new GraphMLReader(src, graph);
		
		try {
			reader.read(); 
			System.out.println(graph.toString());
			
			
			graph.
			for (Vertex v : graph.vertexSet()) {
				
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}*/
	}

/*	
	public static Edge addEdge(Vertex source, Vertex target) {
		System.err.println("ВЫЗОВ MyTest addEdge("+source+","+target+")");
		return new Edge(source, target);
	}
	
	public static Vertex getMyNode(String currentAttributeID) {
		System.err.println("ВЫЗОВ MyTest getMyNode("+currentAttributeID+")");
		return new Vertex(currentAttributeID); 
	}
	*/
}

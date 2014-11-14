/*
 * @author kozo.nishida
 */

package ru.erv.drakongen.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//import org.jgrapht.DirectedGraph;
//import org.jgrapht.graph.DefaultEdge;




import com.tinkerpop.blueprints.pgm.Graph;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.ParserAdapter;


public class GraphMLReader /*extends AbstractGraphReader*/ {
	
	
	// GraphML file name to be loaded.
	private String networkName = null;
	private InputStream inputStream;
	private GraphMLParser parser;


	/**
	 * Creates a new GraphMLReader object.
	 * 
	 * @param fileName
	 *            File name of local GraphML file.
	 * @param monitor
	 *            DOCUMENT ME!
	 */
	public GraphMLReader(final String fileName, Graph graph) {
		try {
			inputStream = new FileInputStream(fileName);
				
			parser = new GraphMLParser(graph);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}


	/**
	 * DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public void read() throws IOException {
		try {
			this.readGraphml();
		} catch (SAXException e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Actual method to read GraphML documents.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 */
	private void readGraphml() throws SAXException, IOException {
		try {
			try {
				try {
					/*
					 * Read the file and map the entire XML document into data
					 * structure.
					 */
					// Get out parser
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					ParserAdapter pa = new ParserAdapter(sp.getParser());

					pa.setContentHandler(parser);
					pa.setErrorHandler(parser);
					InputSource is = new InputSource(inputStream);
					pa.parse(is);

				} catch (OutOfMemoryError oe) {
					/*
					 * It's not generally a good idea to catch
					 * OutOfMemoryErrors, but in this case, where we know the
					 * culprit (a file that is too large), we can at least try
					 * to degrade gracefully.
					 */
					System.gc();
					throw new GraphMLException(
							"Out of memory error caught! THe network being loaded is too larage for the current memory allocation. User the -Xmx flag for the java virtual machine to increase the amount of memory available, e.g. java -Xmx1G cytoscape.jar -p plugins ....");
				} catch (ParserConfigurationException e) {
					System.out.println("ParserConfigurationException !!!");
					throw new IOException(e);
				} catch (SAXParseException e) {
					System.err.println("GraphMLParser: fatal parsing error on line "
							+ e.getLineNumber() + " -- '" + e.getMessage()
							+ "'");
					throw new IOException("Could not parse the file.", e);
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		} finally {
			inputStream = null;
		}
	}

	//public int[] getEdgeIndicesArray() {
	//	return parser.getEdgeIndicesArray();
	//}


	//public int[] getNodeIndicesArray() {
	//	return parser.getNodeIndicesArray();
	//}

}
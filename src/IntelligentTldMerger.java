import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IntelligentTldMerger {
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("USAGE: firstTLD secondTLD outputTLD");
			System.exit(1);
		}

		String firstTLD = args[0], secondTLD = args[1], outputTLD = args[2];
		
		File first = new File(firstTLD);
		File second = new File(secondTLD);
		
		boolean existent = true;
		if (!first.exists() || !first.canRead()) {
			System.err.println(firstTLD + " unreadable.");
			existent = false;
		}
		
		if (!second.exists() || !second.canRead()) {
			System.err.println(secondTLD + " unreadable.");
			existent = false;
		}
		
		if (!existent)
			System.exit(1);
		
		File output = new File(outputTLD);
//		if (!output.canWrite()) {
//			System.err.println("Cannot write '" + outputTLD + "'.");
//			System.exit(1);
//		}
		
		process(first, second, output);
	}
	
	public static void process(File first, File second, File output) throws Exception {
		 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		 
		 Document fDoc = docBuilder.parse(first);
		 Document sDoc = docBuilder.parse(second);
		 Document oDoc = (Document) sDoc.cloneNode(true);
		 
		 NodeList fTags = fDoc.getElementsByTagName("tag"); 
		 NodeList sTags = sDoc.getElementsByTagName("tag");
		 NodeList oTags = oDoc.getElementsByTagName("tag");
		 
		 for (int i = 0; i < sTags.getLength(); i++) {
			 Node current = sTags.item(i);
			 NodeList children = current.getChildNodes();
			 for (int j = 0; j < children.getLength(); j++) {
				 Node node = children.item(j);
				 if (node.getNodeName().equals("tag-class")) {
					 String tagClassName = node.getTextContent();
					 if (tagClassName == null)
						 continue;
					 List tagAttributes = getAttributesFromTLD(sDoc, tagClassName);
					 System.out.println("Found Tag: '" + tagClassName + "' #" + tagAttributes.size()  + " attributes.");
					 Class tagClazz = Class.forName(tagClassName);
					 while (tagClazz.getSuperclass() != null) {
						 List additionalAttributes = getAttributesFromTLD(fDoc,  tagClazz.getSuperclass().getName());
						 System.out.println("    Searched for additional attributes from '" + tagClazz.getSuperclass().getName() + "', found " + additionalAttributes.size());
						 for (Iterator it = additionalAttributes.iterator(); it.hasNext(); ) {
							 Node newAttr = (Node) it.next();
							 current.appendChild(sDoc.adoptNode(newAttr.cloneNode(true)));
						 }
						 tagClazz = tagClazz.getSuperclass();
					 }
				 }
			 }
		 }
		 
		 // DOM level 1 (and 2?) does not contain means to save created or modified documents,
		 // so identity transform is used.
		 
		 System.out.println("Get ready for results::::::::::::::::::::::::;");
		 
		Source input = new DOMSource(sDoc);
		Result out = new StreamResult(System.out);
		
		TransformerFactory xformFactory = TransformerFactory.newInstance();
		
		try {
			Transformer idTransformer = xformFactory.newTransformer();
			idTransformer.transform(input, out);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.flush();
	}
	
	public static List getAttributesFromTLD(Document tld, String tagClass) {
		NodeList nodes = tld.getElementsByTagName("tag-class");
		Node tagClassNode = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node current = nodes.item(i);
			if (current.getTextContent() != null && current.getTextContent().equals(tagClass)) {
				tagClassNode = current;
				break;
			}
		}
		
		if (tagClassNode == null)
			return Collections.EMPTY_LIST;
		
		Node node = tagClassNode;
		while (node.getPreviousSibling() != null) {
			node = node.getPreviousSibling();
		}

		List result = new ArrayList();
		
		while (node.getNextSibling() != null) {
			if (node.getNextSibling().getNodeName().equals("attribute"))
				result.add(node.getNextSibling());
			
			node = node.getNextSibling();
		}
		
		return result;
	}
	
	// headers
	
	// tags + additional superclass attributes
	
	// 
}

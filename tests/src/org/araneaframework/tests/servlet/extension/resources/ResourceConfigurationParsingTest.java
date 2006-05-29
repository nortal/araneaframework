package org.araneaframework.tests.servlet.extension.resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.araneaframework.servlet.filter.importer.extension.ExternalResources;
import org.araneaframework.servlet.filter.importer.extension.ExtensionConfigurationHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ResourceConfigurationParsingTest extends TestCase {
	private ExternalResources struct;
	
	private static final List availableGroups = new ArrayList();
	static {
		availableGroups.add("common-styles");
		availableGroups.add("common-js");
	}
	
	private static final List availableContentTypes = new ArrayList();
	static {
		availableContentTypes.add("image/gif");
		availableContentTypes.add("text/javascript");
		availableContentTypes.add("text/css");
	}
	
	private static final List availableFilesInGroup = new ArrayList();
	static {
		availableFilesInGroup.add("js/mce.js");
		availableFilesInGroup.add("js/calendar.js");
		availableFilesInGroup.add("js/prototype.js");
		
		availableFilesInGroup.add("js/behaviour.js");
	}
	
	public void setUp() throws Exception {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		ExtensionConfigurationHandler handler = new ExtensionConfigurationHandler();
		
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
		
		InputStream stream = 
			Thread.currentThread().getContextClassLoader().getResourceAsStream("extensions/resources/sample.xml");
		
		xr.parse(new InputSource(stream));
		
		struct =  handler.getResult();
	}
	
	public void testGetGroups() throws Exception {
		for(Iterator ite = struct.getGroupNames().iterator();ite.hasNext();) {
			String groupName = (String)ite.next();
			if (availableGroups.indexOf(groupName) == -1)
				fail("Unknown group extracted");
		}
	}
	
	public void testGetContentTypes() {
		for (Iterator ite = struct.getContentTypes().iterator();ite.hasNext();) {
			String contentType = (String)ite.next();
			if (availableContentTypes.indexOf(contentType) == -1)
				fail("Unknown content-type extracted");
		}
	}
	
	public void testGetContentsUnion() {
		Map map = struct.getGroupByName("common-js");
		List files = (List)map.get("text/javascript");
		
		for(Iterator ite = files.iterator(); ite.hasNext();) {
			String file = (String)ite.next();
			
			if (availableFilesInGroup.indexOf(file) == -1)
				fail("Unknown file in a group");
		}
	}
}
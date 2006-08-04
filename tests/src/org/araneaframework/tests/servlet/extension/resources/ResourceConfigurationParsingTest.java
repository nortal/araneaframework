package org.araneaframework.tests.servlet.extension.resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.http.extension.ExternalResource;
import org.araneaframework.http.extension.ExternalResourceConfigurationHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ResourceConfigurationParsingTest extends TestCase {
	private ExternalResource struct;
	
	private static final List availableGroups = new ArrayList();
	static {
		availableGroups.add("common-styles");
		availableGroups.add("common-js");
	}
	
	private static final List availableFilesInGroup = new ArrayList();
	static {
		availableFilesInGroup.add("js/mce.js");
		availableFilesInGroup.add("js/calendar.js");
		availableFilesInGroup.add("js/prototype.js");
		
		availableFilesInGroup.add("js/behaviour.js");
	}
	
	private static final List allFiles = new ArrayList();
	static {
		allFiles.add("gfx/alfa.gif");
		allFiles.add("gfx/beta.gif");
		allFiles.add("gfx/deta.gif");
		allFiles.add("css/print.css");
		allFiles.add("css/screen.css");
		allFiles.add("css/sexy.css");
		allFiles.add("js/mce_helper.js");
		allFiles.add("js/mce_popup.js");
		allFiles.add("js/mce_debug.js");
		
		allFiles.addAll(availableFilesInGroup);
	}
	
	public void setUp() throws Exception {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		ExternalResourceConfigurationHandler handler = new ExternalResourceConfigurationHandler();
		
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
		
		InputStream stream = 
			Thread.currentThread().getContextClassLoader().getResourceAsStream("extensions/resources/sample.xml");
		
		xr.parse(new InputSource(stream));
		
		struct =  handler.getResource();
	}
	
	public void testGetGroups() throws Exception {
		for(Iterator ite = struct.getGroupNames().iterator();ite.hasNext();) {
			String groupName = (String)ite.next();
			if (availableGroups.indexOf(groupName) == -1)
				fail("Unknown group extracted");
		}
	}
		
	public void testGetContentsUnion() {
		Map map = struct.getGroupByName("common-js");
		
		for(Iterator ite = map.entrySet().iterator(); ite.hasNext();) {
			String file = (String)((Map.Entry)(ite.next())).getKey();
			
			if (availableFilesInGroup.indexOf(file) == -1)
				fail("Unknown file in a group");
		}
	}
	
	public void testAllowedFilesContains() {		
		for (Iterator iter = allFiles.iterator(); iter.hasNext();) {
			String file = (String)iter.next();
			if (!struct.isAllowedFile(file))
				fail("File is reported not allowed although in allowed list");
		}
	}
	
	public void testNotInAllowedFiles() {
		if (struct.isAllowedFile("nonexistent"))
			fail("Non existent file is reported to be allowed");
	}
}
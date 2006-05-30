package org.araneaframework.extension.resources;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ExtensionConfigurationHandler extends DefaultHandler {
	private static final String TAG_FILES = "files";
	private static final String TAG_FILE = "file";
	
	private static final String ATTRIB_CONTENT_TYPE = "content-type";
	private static final String ATTRIB_GROUP = "group";
	private static final String ATTRIB_PATH = "path";
	
	private ExternalResources result = new ExternalResources();
	private FileGroup fileGroup;
	
	public ExtensionConfigurationHandler() {
		super();
		
		result = new ExternalResources();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (TAG_FILES.equalsIgnoreCase(qName)) {
			result.addGroup(fileGroup);
			fileGroup = new FileGroup();
			
			for (int i=0;i<attributes.getLength();i++) {
				if (ATTRIB_CONTENT_TYPE.equalsIgnoreCase(attributes.getQName(i))) {
					fileGroup.setContentType(attributes.getValue(i));
				}
				else if (ATTRIB_GROUP.equalsIgnoreCase(attributes.getQName(i))) {
					fileGroup.setName(attributes.getValue(i));
				}
			}
		}
		else if (TAG_FILE.equalsIgnoreCase(qName)) {
			int i = attributes.getIndex(ATTRIB_PATH);
			
			if (i != -1) {				
				fileGroup.addFile(attributes.getValue(i));
			}
		}
	} 

	public ExternalResources getResult() {
		return result;
	}	
}

/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.http.extension;

import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.http.extension.ExternalResource.FileGroup;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for parsing external resources' configuration files.
 * See 'etc/aranea-resources.xml' for a sample configuration file.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class ExternalResourceConfigurationHandler extends DefaultHandler {
	private static final String TAG_FILES = "files";
	private static final String TAG_FILE = "file";
	
	private static final String ATTRIB_CONTENT_TYPE = "content-type";
	private static final String ATTRIB_GROUP = "group";
	private static final String ATTRIB_PATH = "path";
	
	private ExternalResource result = new ExternalResource();
	private FileGroup fileGroup;
	
	public ExternalResourceConfigurationHandler() {
		this(new ExternalResource());
	}
	
	public ExternalResourceConfigurationHandler(ExternalResource resource) {
		super();
		result = resource;
	}

	@Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (TAG_FILES.equalsIgnoreCase(qName)) {
			fileGroup = new FileGroup();
			boolean contentTypeSet = false;
			
			for (int i=0;i<attributes.getLength();i++) {
				if (ATTRIB_CONTENT_TYPE.equalsIgnoreCase(attributes.getQName(i))) {
					fileGroup.setContentType(attributes.getValue(i));
					contentTypeSet = true;
				}
				else if (ATTRIB_GROUP.equalsIgnoreCase(attributes.getQName(i))) {
					fileGroup.setName(attributes.getValue(i));
				}
			}
			if (!contentTypeSet)
				throw new AraneaRuntimeException("No content type set for files in resource configuration");
		}
		else if (TAG_FILE.equalsIgnoreCase(qName)) {
			int i = attributes.getIndex(ATTRIB_PATH);
			
			if (i != -1) {				
				fileGroup.addFile(attributes.getValue(i));
			}
		}
	} 

	@Override
  public void endElement(String uri, String localName, String qName) {
		if (TAG_FILES.equalsIgnoreCase(qName)) {
			result.addGroup(fileGroup);	
		}
	}
	
	public ExternalResource getResource() {
		return result;
	}	
}

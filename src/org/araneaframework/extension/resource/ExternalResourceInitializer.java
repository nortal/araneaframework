/**
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
**/

package org.araneaframework.extension.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.araneaframework.core.AraneaRuntimeException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Initializes the external resources of Aranea. External resources are static
 * files that are required by different web components - images, javascripts, html
 * files. All resources are listed in different XML configuration files.Consult the 
 * reference manual for configuring resources how-to.
 * 
 * The order is to search all "conf/aranea-resources.xml" files and then the
 * extensions' configuration files.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class ExternalResourceInitializer {
	private static final Logger log = Logger.getLogger(ExternalResourceInitializer.class);
	
	/**
	 * Main configuration file bundled with the framework.
	 */
	public static final String ARANEA_RESOURCES_FILE_NAME = "conf/aranea-resources.xml";
	
	// TODO: extensions configurable, right now explicit extension configuration file
	public static final String EXTENSION_TINY_MCE = "conf/aranea-extension-tinymce.xml";
	
	public ExternalResource getResources() {
		try {
			XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			ExternalResourceConfigurationHandler handler = new ExternalResourceConfigurationHandler();
			
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			//ClassLoader loader = getClass().getClassLoader();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration resources =  loader.getResources(ARANEA_RESOURCES_FILE_NAME);
			
			if (!resources.hasMoreElements())
				log.warn("No resources configuration file found");
			
			for(;resources.hasMoreElements();) {
				URL fileURL = (URL)resources.nextElement();
				log.debug("Adding resources from file'"+fileURL+"'");
				xr.parse(new InputSource(fileURL.openStream()));
			}
			
			resources =  loader.getResources(EXTENSION_TINY_MCE);
			
			for(;resources.hasMoreElements();) {
				URL fileURL = (URL)resources.nextElement();
				log.debug("Adding resources from file'"+fileURL+"'");
				xr.parse(new InputSource(fileURL.openStream()));
			}

			return handler.getResource();
		}
		catch (ParserConfigurationException e) {
			throw new AraneaRuntimeException("Problem while configuring SAX parser", e);
		}
		catch (SAXException e) {
			throw new AraneaRuntimeException("Problem while parsing resource configuration file", e);
		}
		catch (IOException e) {
			throw new AraneaRuntimeException("Problem while reading configuration file", e);
		}
	}
}

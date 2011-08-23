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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Initializes the external resources of Aranea. External resources are static files that are required by different web
 * components - images, JavaScripts, HTML files. All resources are listed in different XML configuration files. Consult
 * the reference manual for configuring resources how-to.
 * <p>
 * The order is to search all "conf/aranea-resources.xml" files and then the extensions' configuration files.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class ExternalResourceInitializer {

  private static final Log LOG = LogFactory.getLog(ExternalResourceInitializer.class);

  /**
   * Framework application main configuration file.
   */
  public static final String ARANEA_RESOURCES_FILE_NAME = "conf/aranea-resources.xml";

  public static final String EXTENSION_TINY_MCE = "conf/aranea-extension-tinymce.xml";

  /**
   * A list of file names that will be searched.
   */
  protected final List<String> resources = new LinkedList<String>();

  /**
   * A list of directory names separated by semicolon. The directory names are relative to the root directory of the
   * deployment unit and must, therefore, both begin and end with a forward slash.
   */
  protected String searchDirs = "/META-INF/;/WEB-INF/";

  public ExternalResourceInitializer() {
    this.resources.add(ARANEA_RESOURCES_FILE_NAME);
    this.resources.add(EXTENSION_TINY_MCE);
  }

  public ExternalResource getResources(ServletContext context) {
    try {
      XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      ExternalResourceConfigurationHandler handler = new ExternalResourceConfigurationHandler();

      xr.setContentHandler(handler);
      xr.setErrorHandler(handler);

      ClassLoader loader = Thread.currentThread().getContextClassLoader();

      for (String resource : this.resources) {
        Enumeration<URL> classPathResources = loader.getResources(resource);
        Enumeration<URI> contextPathResources = getContextResources(context, resource);

        if (LOG.isWarnEnabled() && !(classPathResources.hasMoreElements() || contextPathResources.hasMoreElements())) {
          LOG.warn("Could not find resource file '" + resource + "'!");
        }

        loadURLResources(classPathResources, xr);
        loadURIResources(contextPathResources, xr);
      }

      return handler.getResource();
    } catch (ParserConfigurationException e) {
      throw new AraneaRuntimeException("Problem while configuring SAX parser", e);
    } catch (SAXException e) {
      throw new AraneaRuntimeException("Problem while parsing resource configuration file", e);
    } catch (IOException e) {
      throw new AraneaRuntimeException("Problem while reading configuration file", e);
    } catch (URISyntaxException e) {
      throw new AraneaRuntimeException("Problem while converting URL into URI", e);
    }
  }

  protected void loadURLResources(Enumeration<URL> resources, XMLReader xr) throws IOException, SAXException {
    while (resources.hasMoreElements()) {
      URL fileURL = resources.nextElement();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Adding resources from file'" + fileURL + "'");
      }
      xr.parse(new InputSource(fileURL.openStream()));
    }
  }
  
  protected void loadURIResources(Enumeration<URI> resources, XMLReader xr) throws IOException, SAXException {
    while (resources.hasMoreElements()) {
      URI fileURL = resources.nextElement();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Adding resources from file'" + fileURL + "'");
      }
      xr.parse(new InputSource(fileURL.toURL().openStream()));
    }
  }

  protected Enumeration<URI> getContextResources(ServletContext ctx, String fileName) throws MalformedURLException,
      URISyntaxException {
    Set<URI> fileURLSet = new HashSet<URI>();
    StringTokenizer paths = new StringTokenizer(this.searchDirs, ";");

    while (paths.hasMoreTokens()) {
      addURL(fileURLSet, ctx, paths.nextToken() + fileName);
    }

    return Collections.enumeration(fileURLSet);
  }

  /**
   * Adds the given URL to the given URL set.
   * <p>
   * Changed this method protected in 2.0. This method was changed for an important blocking behaviour exhibited by
   * {@link java.net.URL}. Since the URI has better performance in collections, the URL is now converted into
   * {@link URI}.
   * 
   * @param fileURLSet A set of URLs.
   * @param ctx The current servlet context.
   * @param urlPath The path that the URL should have.
   * @throws MalformedURLException When the given URL string is not valid.
   * @throws URISyntaxException When a problem occurred converting the URL into URI.
   */
  protected void addURL(Set<URI> fileURLSet, ServletContext ctx, String urlPath) throws MalformedURLException,
      URISyntaxException {
    URL url = ctx.getResource(urlPath);
    if (url != null) {
      fileURLSet.add(url.toURI());
    }
  }

  /**
   * Sets a list of resources (simple file names) that this resource initializer will look for.
   * 
   * @param resources A list of resources (simple file names) to look for.
   * @since 2.0
   */
  public void setResources(List<String> resources) {
    this.resources.clear();
    this.resources.addAll(resources);
  }

  /**
   * Add a resource (simple file name) that this resource initializer will look for.
   * 
   * @param resource A resource (simple file name) to look for.
   * @since 2.0
   */
  public void addResource(String resource) {
    this.resources.add(resource);
  }

  /**
   * Sets a semicolon separated list of paths to directories that will be used for looking these resources. The paths
   * are relative to the root directory of the deployment unit and must, therefore, both begin and end with a forward
   * slash.
   * 
   * @param searchDirs A semicolon separated list of paths to directories.
   * @since 2.0
   */
  public void setSearchDirs(String searchDirs) {
    this.searchDirs = searchDirs;
  }

  /**
   * Adds a path to a directory that will be used for looking resources. The path must be relative to the root directory
   * of the deployment unit and, therefore, must both begin and end with a forward slash.
   * 
   * @param searchDir A path to a directory.
   * @since 2.0
   */
  public void addSearchDir(String searchDir) {
    this.searchDirs = searchDir;
  }
}

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

package org.araneaframework.http.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.support.CachingEntityResolver;
import org.araneaframework.jsp.engine.TldLocationsCache;
import org.araneaframework.jsp.support.TagInfo;
import org.araneaframework.uilib.ConfigurationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StandardJspFilterService extends BaseFilterService implements JspContext {
  private static final Logger log = Logger.getLogger(StandardJspFilterService.class);

  // URI -> Map<TagInfo>
  private Map taglibs = new HashMap();
  
  private String submitCharset;
  
  private String jspPath = "/WEB-INF/jsp";
  
  private LocalizationContext loc;
  
  // Spring injection parameters  
  public void setSubmitCharset(String submitCharset) {
    this.submitCharset = submitCharset;
  }
  
  public void setJspPath(String jspPath) {
    this.jspPath = jspPath;
  }
  
  public String getJspPath() {
    return jspPath;
  }
  
  protected void init() throws Exception {
    super.init();

    loc = (LocalizationContext) getEnvironment().getEntry(LocalizationContext.class);
  }
  
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(getEnvironment(), JspContext.class, this);
  }
  
 protected void action(Path path, InputData input, OutputData output) throws Exception {
    output.pushAttribute(JspContext.JSP_CONFIGURATION_KEY, new JspConfiguration());
    
    try {
      super.action(path, input, output);
    }
    finally {
      output.popAttribute(JspContext.JSP_CONFIGURATION_KEY);
    }
  }
  
  public class JspConfiguration {
    public String getSubmitCharset() {
      return submitCharset;
    }
    
    public String getFormAction() {
      return ((ServletConfig) getEnvironment().getEntry(ServletConfig.class)).getServletContext().getServletContextName();
    }
    
    public ResourceBundle getCurrentBundle() {
      return loc.getResourceBundle();
    }
    
    public Locale getCurrentLocale() {
      return loc.getLocale();
    }
    
    public Map getTagMapping(String uri){
      return getTagMap(uri);
    }
    
    public ConfigurationContext getConfiguration() {
      return (ConfigurationContext) getEnvironment().getEntry(ConfigurationContext.class);
    }
  }
  
  public Map getTagMap(String uri) {
    if (!taglibs.containsKey(uri)) {
      ServletContext ctx = 
        (ServletContext) getEnvironment().getEntry(ServletContext.class);
      String[] locations = TldLocationsCache.getInstance(ctx).getLocation(uri);
      if (locations != null) {
        URL realLoc = null;        
        
        try {
          if (locations[1] == null)
            realLoc = new URL(locations[0]);
          else
            realLoc = new URL("jar:" + locations[0] + "!/" + locations[1]);

          taglibs.put(uri, readTldMapping(realLoc));
        }
        catch (IOException e) {
          throw new AraneaRuntimeException("Failed to read the tag library descriptor for URI '" + uri + "'", e);
        }
      }
    }

    return (Map) taglibs.get(uri);
  }

  private Map readTldMapping(URL location) throws IOException {
    Map result = new HashMap();

    InputStream tldStream = location.openStream();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document tldDoc = null;
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setEntityResolver(CachingEntityResolver.getInstance());
      tldDoc = builder.parse(tldStream);
    }
    catch (ParserConfigurationException e) {
      throw new NestableRuntimeException(e);
    }
    catch (SAXException e) {
      throw new NestableRuntimeException(e);
    }
    catch (IOException e) {
      throw new NestableRuntimeException(e);
    }

    NodeList tagElements = tldDoc.getDocumentElement().getElementsByTagName("tag");

    for (int i = 0; i < tagElements.getLength(); i++) {
      TagInfo tagInfo = TagInfo.readTagInfo((Element) tagElements.item(i));

      result.put(tagInfo.getTagName(), tagInfo);
    }

    return result;
  }

}

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

package org.araneaframework.servlet.filter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.framework.filter.StandardSynchronizingFilterService;
import org.araneaframework.jsp.support.TagInfo;
import org.araneaframework.servlet.JspContext;
import org.araneaframework.uilib.ConfigurationContext;
import org.w3c.dom.Document;

public class StandardJspFilterService extends BaseFilterService implements JspContext {
  public static final String JSP_CONFIGURATION_KEY = "org.araneaframework.jsp.aranea.filter.UiAraneaJspConfigurationFilterService.JspConfiguration";
  private static final Logger log = Logger.getLogger(StandardSynchronizingFilterService.class);
  
  private Map tagMapping;
  
  private String submitCharset;
  private String uiTldPath = "META-INF/aranea-presentation.tld";
  
  private String jspPath = "/WEB-INF/jsp";
  
  private LocalizationContext loc;
  
  // Spring injection parameters  
  
  public void setUiTldPath(String uiTldPath) {
    this.uiTldPath = uiTldPath;
  }

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

    readTldMapping();    
  }
  
  protected Environment getChildEnvironment() {
    Map entries = new HashMap();  
    entries.put(JspContext.class, this);
    
    return new StandardEnvironment(getEnvironment(), entries);
  }
  
  public void readTldMapping() throws Exception {
    InputStream tldStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(uiTldPath);
    
    if (tldStream == null) {
    	throw new FileNotFoundException("Unable to read file: "+uiTldPath);
    }
    	
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document tldDoc = builder.parse(tldStream);
    
    tagMapping = TagInfo.makeTagMapping(tldDoc.getDocumentElement());
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    output.pushAttribute(JSP_CONFIGURATION_KEY, new JspConfiguration());
    
    try {
      super.action(path, input, output);
    }
    finally {
      output.popAttribute(JSP_CONFIGURATION_KEY);
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
    
    public Map getTagMapping() {
      return tagMapping;
    }
    
    public ConfigurationContext getConfiguration() {
      return (ConfigurationContext) getEnvironment().getEntry(ConfigurationContext.class);
    }
  }
}

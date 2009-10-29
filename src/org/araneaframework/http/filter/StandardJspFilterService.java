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

package org.araneaframework.http.filter;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.araneaframework.Environment;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.support.CachingEntityResolver;
import org.araneaframework.http.support.TldLocationsCache;
import org.araneaframework.jsp.support.TagInfo;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StandardJspFilterService extends BaseFilterService implements JspContext {

  // URI -> Map<TagInfo>
  private Map<String, Map<String, TagInfo>> taglibs = new HashMap<String, Map<String,TagInfo>>();

  private String submitCharset;

  private String jspPath = "/WEB-INF/jsp";

  private String jspExtension = ".jsp";

  // Spring injection parameters
  public void setSubmitCharset(String submitCharset) {
    this.submitCharset = submitCharset;
  }

  public String getSubmitCharset() {
    return this.submitCharset;
  }

  public void setJspPath(String jspPath) {
    this.jspPath = jspPath;
  }

  public String getJspPath() {
    return this.jspPath;
  }

  public void setJspExtension(String jspExtension) {
    this.jspExtension = jspExtension;
  }

  public String getJspExtension() {
    return this.jspExtension;
  }

  public String getFormAction() {
    return getEnvironment().requireEntry(ServletConfig.class).getServletContext().getServletContextName();
  }

  public Map<String, TagInfo> getTagMapping(String uri) {
    return getTagMap(uri);
  }

  public ConfigurationContext getConfiguration() {
    return UilibEnvironmentUtil.getConfiguration(getEnvironment());
  }

  protected Environment getChildEnvironment() {
    return new StandardEnvironment(getEnvironment(), JspContext.class, this);
  }

  public Map<String, TagInfo> getTagMap(String uri) {
    if (!this.taglibs.containsKey(uri)) {
      ServletContext ctx = getEnvironment().getEntry(ServletContext.class);
      String[] locations = TldLocationsCache.getInstance(ctx).getLocation(uri);
      if (locations != null) {
        URL realLoc = null;

        try {
          realLoc = new URL(locations[0] == null ? locations[0] : "jar:" + locations[0] + "!/" + locations[1]);
          this.taglibs.put(uri, readTldMapping(realLoc));
        } catch (IOException e) {
          throw new AraneaRuntimeException("Failed to read the tag library descriptor for URI '" + uri + "'", e);
        }
      }
    }

    return this.taglibs.get(uri);
  }

  private Map<String, TagInfo> readTldMapping(URL location) {
    Map<String, TagInfo> result = new HashMap<String, TagInfo>();
    Document tldDoc = null;

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setEntityResolver(CachingEntityResolver.getInstance());
      tldDoc = builder.parse(location.openStream());
    } catch (ParserConfigurationException e) {
      throw new NestableRuntimeException(e);
    } catch (SAXException e) {
      throw new NestableRuntimeException(e);
    } catch (IOException e) {
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

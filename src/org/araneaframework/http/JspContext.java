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

package org.araneaframework.http;

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.jsp.support.TagInfo;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * One of Aranea environment contexts, this one for retrieving information about some JSP configuration.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface JspContext extends Serializable {

  /**
   * Provides the prefix path for resolving JSP file location.
   * 
   * @return The common prefix-path used for all JSP files. Empty string for no common prefix.
   */
  String getJspPath();

  /**
   * Provides the JSP file extension for resolving JSP file location.
   * 
   * @return The common suffix used for all JSP files. Empty string for no common suffix.
   * @since 1.0.1
   */
  String getJspExtension();

  /**
   * The form submit character set. Used commonly when rendering a form.
   * 
   * @return The form submit character encoding. <code>null</code> when unspecified.
   * @since 1.1
   */
  String getSubmitCharset();

  /**
   * The form action URL/path. Used commonly when rendering a form.
   * 
   * @return The form action path. <code>null</code> when unspecified.
   * @since 1.1
   */
  String getFormAction();

  /**
   * Provides a map of all tags by tag name that are registered in the servlet container with given URI.
   * 
   * @param uri The URI used for searching tags that are bound to this URI.
   * @return A map of tags by tag name.
   * @since 1.1
   */
  Map<String, TagInfo> getTagMapping(String uri);

  /**
   * Provides the configuration context used in this session.
   * 
   * @return Current configuration context.
   * @since 1.1
   */
  ConfigurationContext getConfiguration();

}

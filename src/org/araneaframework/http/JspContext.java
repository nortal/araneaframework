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

  public String getJspPath();

  /** @since 1.0.1 */
  public String getJspExtension();

  /** @since 1.1 */
  public String getSubmitCharset();

  /** @since 1.1 */
  public String getFormAction();

  /** @since 1.1 */
  public Map<String, TagInfo> getTagMapping(String uri);

  /** @since 1.1 */
  public ConfigurationContext getConfiguration();

}

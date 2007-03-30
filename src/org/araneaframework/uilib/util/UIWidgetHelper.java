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

package org.araneaframework.uilib.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.jsp.PageContext;
import org.araneaframework.core.AraneaRuntimeException;

public class UIWidgetHelper implements Serializable {

//  private static final Logger log = Logger.getLogger(UIWidgetHelper.class);

  private Set contextEntries;
  private Map hiddenContextEntries;

  public void addContextEntry(String key, Object value) {
    if (hiddenContextEntries != null) {
      hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }
    if (value == null) {
      if (contextEntries != null)
        contextEntries.remove(key);
    } else {
      if (contextEntries == null)
        contextEntries = new HashSet();
      contextEntries.add(key);
    }
  }

  public void hideContextEntries(PageContext pageContext) {
    if (contextEntries == null || contextEntries.size() == 0)
      return;
    if (hiddenContextEntries != null) {
      hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }
    hiddenContextEntries = new HashMap();
    for (Iterator i = contextEntries.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      Object value = pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);
      if (value != null) {
        hiddenContextEntries.put(key, value);
        pageContext.removeAttribute(key, PageContext.REQUEST_SCOPE);
      }
    }
  }

  public void restoreContextEntries(PageContext pageContext) {
    if (hiddenContextEntries == null)
      return;
    for (Iterator i = hiddenContextEntries.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      pageContext.setAttribute((String) entry.getKey(), entry.getValue(), PageContext.REQUEST_SCOPE);
    }
    hiddenContextEntries = null;
  }

  public void reset() {
    if (contextEntries != null)
      contextEntries.clear();
    if (hiddenContextEntries != null) {
      hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }
  }

}

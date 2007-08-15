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

package org.araneaframework.framework.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * Filter widget that enriches children's environment with specified context entries.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardContextMapFilterWidget extends BaseFilterWidget {
  private static final Log log = LogFactory.getLog(StandardContextMapFilterWidget.class);
  
  private Map contexts = new HashMap();
  
  protected void init() throws Exception {
    Map entries = new HashMap();
    
    if (contexts != null)
      for (Iterator i = contexts.entrySet().iterator(); i.hasNext();) {
        Map.Entry entry = (Map.Entry) i.next();
        
        String key = (String) entry.getKey();
        
        if (key.endsWith(".class")) {
          String className =  key.substring(0, key.lastIndexOf('.'));
          Class contextKey = Class.forName(className);
          entries.put(contextKey, entry.getValue());
        }
        else
          entries.put(key, entry.getValue());
      }
    
    childWidget._getComponent().init(getScope(), new StandardEnvironment(getEnvironment(), entries));
           
    if (log.isDebugEnabled())
      log.debug("Following contexts added to environment: " + entries);
  }
  
  /**
   * Set the contexts that are made accessible from children's environments.
   * @param contexts Map &lt;contextKey, contextValue&gt;
   */
  public void setContexts(Map contexts) {
    this.contexts = contexts;
  }
}

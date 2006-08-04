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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.MountContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.ServletInputData;
import org.araneaframework.http.util.URLUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardMountingFilterService extends BaseFilterService implements MountContext {
  private Map mounts = new HashMap();    
  
  public String mount(InputData input, String pathPrefix, MessageFactory messageFactory) {
    pathPrefix = URLUtil.normalizeURI(pathPrefix);
    mounts.put(pathPrefix, messageFactory);

    return getMountURL(input, pathPrefix);
  }
  
  public void unmount(String pathPrefix) {
    mounts.remove(URLUtil.normalizeURI(pathPrefix));
  }
  
  public String getMountURL(InputData input, String pathPrefix) {
    HttpServletRequest req = ((ServletInputData) input).getRequest();
    
    StringBuffer url = new StringBuffer();
    url.append(req.getScheme());
    url.append("://");
    url.append(req.getServerName());    
    url.append(":");
    url.append(req.getServerPort());
    url.append(req.getContextPath());
    url.append(req.getServletPath());
    url.append(MountContext.MOUNT_PATH);
    url.append(URLUtil.normalizeURI(pathPrefix));    
    return url.toString();    
  }
  
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(super.getChildEnvironment(), MountContext.class, this);
  }

  /**
   * This setter allows to configure the default mounts using dependency injection. 
   * It expects as keys the mounting path prefixes and as valued {@link MountContext.MessageFactory}.
   */
  public void setMounts(Map mounts) {
    this.mounts = mounts;
  }

  public Message getMountedMessage(InputData input) {
    HttpServletRequest req = ((ServletInputData) input).getRequest();
    
    String pathInfo = req.getPathInfo();
    String maxPrefix = "";
    
    if (pathInfo != null) {
      for (Iterator i = mounts.keySet().iterator(); i.hasNext();) {
        String mountPrefix = (String) i.next();      
                      
        if (pathInfo.startsWith(MountContext.MOUNT_PATH + mountPrefix) && (mountPrefix.length() > maxPrefix.length())) 
          maxPrefix = mountPrefix;
      }
      
      if (maxPrefix.length() > 0) {
        MessageFactory mountFactory = (MessageFactory) mounts.get(maxPrefix);
        
        int fullPrefixLength = MountContext.MOUNT_PATH.length() + maxPrefix.length();
        String suffix = fullPrefixLength < pathInfo.length() ? pathInfo.substring(fullPrefixLength + 1) : null;
        
        return mountFactory.buildMessage(
              req.getRequestURL().toString(), 
              suffix, 
              input, 
              input.getOutputData());
      }
    }
    
    return null;
  }
}

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
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.MountContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.util.URLUtil;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardMountingFilterService extends BaseFilterService implements MountContext {
  private Map<String, MessageFactory> mounts = new HashMap<String, MessageFactory>();    
  
  public String mount(InputData input, String pathPrefix, MessageFactory messageFactory) {
    Assert.notNullParam(input, "input");
    Assert.notEmptyParam(pathPrefix, "pathPrefix");
    Assert.notNullParam(messageFactory, "messageFactory");
    
    pathPrefix = URLUtil.normalizeURI(pathPrefix);
    mounts.put(pathPrefix, messageFactory);

    return getMountURL(input, pathPrefix);
  }
  
  public void unmount(String pathPrefix) {
    Assert.notEmptyParam(pathPrefix, "pathPrefix");
    
    mounts.remove(URLUtil.normalizeURI(pathPrefix));
  }
  
  public String getMountURL(InputData input, String pathPrefix) {
    Assert.notNullParam(input, "input");
    Assert.notEmptyParam(pathPrefix, "pathPrefix");
    
    HttpInputData req = (HttpInputData) input;
    
    StringBuffer url = new StringBuffer();
    url.append(req.getContainerURL());
    url.append(MountContext.MOUNT_PATH);
    url.append(URLUtil.normalizeURI(pathPrefix));    
    return url.toString();    
  }
  
  @Override
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(super.getChildEnvironment(), MountContext.class, this);
  }

  /**
   * This setter allows to configure the default mounts using dependency injection. 
   * It expects as keys the mounting path prefixes and as values {@link org.araneaframework.framework.MountContext.MessageFactory}.
   */
  public void setMounts(Map<String, MessageFactory> mounts) {
    this.mounts = mounts;
  }

  public Message getMountedMessage(InputData input) {
    Assert.notNullParam(input, "input");
    
    HttpInputData req = (HttpInputData) input;
    
    String pathInfo = req.getPath();
    String maxPrefix = "";
    
    if (pathInfo != null) {
      for (Iterator<String> i = mounts.keySet().iterator(); i.hasNext();) {
        String mountPrefix = i.next();      
                      
        if (pathInfo.startsWith(MountContext.MOUNT_PATH + mountPrefix) && (mountPrefix.length() > maxPrefix.length())) 
          maxPrefix = mountPrefix;
      }
      
      if (maxPrefix.length() > 0) {
        MessageFactory mountFactory = mounts.get(maxPrefix);
        
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

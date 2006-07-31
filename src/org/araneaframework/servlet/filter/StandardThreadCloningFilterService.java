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

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.Service;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.core.StandardRelocatableServiceDecorator;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.ThreadCloningContext;
import org.araneaframework.servlet.util.URLUtil;

/**
 * Filter that clones session threads upon requests and redirects request to cloned session thread. Should 
 * always be configured as the first thread-level filter.
 * 
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardThreadCloningFilterService extends BaseFilterService implements ThreadCloningContext {
  private static final Logger log = Logger.getLogger(StandardThreadCloningFilterService.class);
  private boolean initializeChildren = true;

  public StandardThreadCloningFilterService() {
    super();
  }

  protected StandardThreadCloningFilterService(Service childService, boolean freshChildren) {
    /* if children are not fresh (they are clones) they may not be re-inited */
    this.initializeChildren = freshChildren;
    if (freshChildren)
      setChildService(childService);
    else
      super.setChildService(childService);
  }

  public void setChildService(Service childService) {
    super.setChildService(new StandardRelocatableServiceDecorator(childService));
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (!cloningRequested(input)) {
      super.action(path, input, output);
      return;
    }
    
    ThreadContext threadCtx = getThreadServiceCtx();
    TopServiceContext topCtx = getTopServiceCtx();

    if (log.isDebugEnabled())
      log.debug("Attempting to clone current thread ('" + threadCtx.getCurrentId() + "').");

    // clone the service and set its new environment
    Relocatable.RelocatableService service = (RelocatableService) childService;
    Environment env = service._getRelocatable().getCurrentEnvironment();
    service._getRelocatable().overrideEnvironment(null);
    
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(SerializationUtils.serialize(service)));

    RelocatableService clone = (RelocatableService) ois.readObject();
    clone._getRelocatable().overrideEnvironment(getEnvironment());
    
    // restore cloned service's environment
    service._getRelocatable().overrideEnvironment(env);

    /* wrap the cloned service in a new StandardThreadCloningFilterService.
     * a) created service should not be decorated relocatable again, because clone is relocatable already 
     * b) new StandardThreadCloningFilterService's childService may not be reinited! */
    StandardThreadCloningFilterService wrappedClone = new StandardThreadCloningFilterService(clone, false);

    String cloneServiceId = RandomStringUtils.randomAlphabetic(12);
    if (log.isDebugEnabled())
      log.debug("Attaching the cloned thread as '" + cloneServiceId + "'.");
    threadCtx.addService(cloneServiceId, wrappedClone);

    // send event to cloned service
    clone._getService().action(path, input, output);
    
    // redirect to URL where cloned service resides
    ((ServletOutputData) getCurrentOutput()).getResponse().sendRedirect(getResponseURL(getRequestURL(), (String)topCtx.getCurrentId(), cloneServiceId));
  }

  protected void init() throws Exception {
    if (initializeChildren)
      super.init();
  }
  
  protected boolean cloningRequested(InputData input) throws Exception {
    return input.getGlobalData().get(ThreadCloningContext.CLONING_REQUEST_KEY) != null;
  }
  
  protected String getRequestURL() {
    return URLUtil.getServletRequestURL(getCurrentInput());
  }
  
  protected String getResponseURL(String url, String topServiceId, String threadServiceId) {
    Map m = new HashMap();
    m.put(TopServiceContext.TOP_SERVICE_KEY, topServiceId);
    m.put(ThreadContext.THREAD_SERVICE_KEY, threadServiceId);
    return URLUtil.parametrizeURI(url, m);
  }

  protected ThreadContext getThreadServiceCtx() {
    return ((ThreadContext)getEnvironment().requireEntry(ThreadContext.class));
  }

  protected TopServiceContext getTopServiceCtx() {
    return ((TopServiceContext)getEnvironment().requireEntry(TopServiceContext.class));
  }
}

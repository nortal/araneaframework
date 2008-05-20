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

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.Service;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.ThreadCloningContext;
import org.araneaframework.http.util.URLUtil;

/**
 * Filter that clones session threads upon requests and redirects request to cloned session thread. Should 
 * always be configured as the first thread-level filter.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StandardThreadCloningFilterService extends BaseFilterService implements ThreadCloningContext {
  private static final Log log = LogFactory.getLog(StandardThreadCloningFilterService.class);
  private Long timeToLive;
  private boolean initializeChildren = true;
  private transient byte[] threadSnapshot;

  public StandardThreadCloningFilterService() {
    super();
  }

  protected StandardThreadCloningFilterService(Service childService, boolean freshChildren) {
    /* if children are not fresh (they are clones) they may not be re-inited */
    this.initializeChildren = freshChildren;
    /* fresh children need decoration */
    if (freshChildren)
      setChildService(childService);
    else
      super.setChildService(childService);
  }

  public void setChildService(Service childService) {
    super.setChildService(new RelocatableDecorator(childService));
  }
  
  /** 
   * Sets the time of inactivity after which cloned service may be killed by thread router. 
   * @param timeToLive allowed inactivity time, in milliseconds 
   */
  public void setTimeToLive(Long timeToLive) {
    this.timeToLive = timeToLive;
  }
  
  protected void init() throws Exception {
    if (initializeChildren)
      super.init();
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (cloningRequested(input)) {
      redirect(cloningAction(path, input, output));
    } else if (snapshotRequested(input)) {
      this.threadSnapshot = takeSnapshot((RelocatableService)childService);
      super.action(path, input, output);
    }
    else
      super.action(path, input, output);
    this.threadSnapshot = null;
  }
  
  protected String cloningAction(Path path, InputData input, OutputData output) throws Exception {
    if (log.isDebugEnabled())
      log.debug("Attempting to clone current thread ('" + getThreadServiceCtx().getCurrentId() + "').");

    RelocatableService clone = clone((RelocatableService)childService);
    String cloneServiceId = startClonedThread(clone);
    clone._getService().action(path, input, output);
    
    // return URL where cloned service resides
    return ((HttpOutputData) getOutputData()).encodeURL((getResponseURL(getRequestURL(), (String)getTopServiceCtx().getCurrentId(), cloneServiceId)));
  }
  
  protected void redirect(String location) throws Exception {
	((HttpOutputData) getOutputData()).sendRedirect(location);
  }
  
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(super.getChildEnvironment(), ThreadCloningContext.class, this);
  }

  /**
   * Clones given {@link org.araneaframework.Relocatable.RelocatableService}.
   * Clone is created by first serializing and then deserializing given
   * <code>service</code>. Created clone does not have
   * {@link org.araneaframework.Environment}.
   * 
   * @return clone (without {@link org.araneaframework.Environment}) of given
   *         {@link org.araneaframework.Relocatable.RelocatableService}.
   */
  protected RelocatableService clone(RelocatableService service) throws Exception {
    this.threadSnapshot = takeSnapshot(service);
	ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(this.threadSnapshot));
    return (RelocatableService) ois.readObject();
  }

  protected byte[] takeSnapshot(RelocatableService service) {
    Relocatable.Interface relocatable = service._getRelocatable();
	Environment env = relocatable.getCurrentEnvironment();
	relocatable.overrideEnvironment(null);
	
	byte[] result = SerializationUtils.serialize(service);
	relocatable.overrideEnvironment(env);

	return result;
  }

  /**
   * Wraps the cloned service in a new StandardThreadCloningFilterService,
   * attaches it to {@link ThreadContext}.
   * <ul>
   * <li> created service should not be decorated relocatable again, because
   * <code>clone</code> is relocatable already</li>
   * <li> new StandardThreadCloningFilterService's childService may not be
   * reinited!</li>
   * </ul>
   * This method is public and part of {@link ThreadCloningContext} interface
   * since Aranea 1.1.
   * 
   * @return thread id assigned to wrapped service
   */ 
  public String startClonedThread(RelocatableService clone) {
    StandardThreadCloningFilterService wrappedClone = new StandardThreadCloningFilterService(clone, false);
    String cloneServiceId = RandomStringUtils.randomAlphabetic(12);
    
    if (log.isDebugEnabled())
      log.debug("Attaching the cloned thread as '" + cloneServiceId + "'.");
    
    startThreadService(getThreadServiceCtx(), wrappedClone, cloneServiceId);
    clone._getRelocatable().overrideEnvironment(wrappedClone.getChildEnvironment());
    
    return cloneServiceId;
  }

  public byte[] acquireThreadSnapshot() {
    if (this.threadSnapshot == null && log.isWarnEnabled()) {
      log.warn("Ineffective call to acquireSerializedThread(), no thread snapshot present. Request is probably missing required parameters.");
    }
	return this.threadSnapshot;
  }

  private void startThreadService(ThreadContext threadCtx, StandardThreadCloningFilterService cloneService, String cloneServiceId) {
    if (timeToLive == null)
      threadCtx.addService(cloneServiceId, cloneService);
    else
      threadCtx.addService(cloneServiceId, cloneService, timeToLive);
  }

  protected boolean cloningRequested(InputData input) throws Exception {
    return input.getGlobalData().get(ThreadCloningContext.CLONING_REQUEST_KEY) != null;
  }

  protected boolean snapshotRequested(InputData input) throws Exception {
    return input.getGlobalData().get(ThreadCloningContext.CLONE_ONLY_REQUEST_KEY) != null;
  }

  protected String getRequestURL() {
    return ((HttpInputData) getInputData()).getContainerURL();
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

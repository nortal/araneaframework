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

package org.araneaframework.servlet.router;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.core.StandardRelocatableServiceDecorator;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.util.ClientStateUtil;
import org.jgroups.Channel;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelNotConnectedException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.TimeoutException;

/**
 * Associates this service with the HttpSession. Is a session does not exist, it is created. Also handles the
 * invalidation of the session.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class JGroupClusteredSessionRouterService extends BaseService {
  private static final Logger log = Logger.getLogger(JGroupClusteredSessionRouterService.class);

  public static final String JGROUP_NAME = "org.araneaframework.framework.router.JGroupClusteredSessionRouterService";

  public static final String SESSION_ID_KEY = "ARANEASESSIONID";

  private ServiceFactory serviceFactory;

  private Channel channel;

  private Map sessions = new ConcurrentHashMap();

  public void setSessionServiceFactory(ServiceFactory factory) {
    serviceFactory = factory;
  }

  protected void init() throws Exception {
    super.init();

    String props =  "UDP:" + 
        "PING(num_initial_members=2;timeout=3000):" + 
        "FD:" + 
        "STABLE:" + 
        "NAKACK:" + 
        "UNICAST:" + 
        "FRAG:" + 
        "FLUSH:" + 
        "GMS:" + 
        "VIEW_ENFORCER:" + 
        "STATE_TRANSFER:" + 
        "QUEUE";
    
    channel = new JChannel(props);
    channel.connect(JGROUP_NAME);
    
    new Receiver().start();
  }

  protected void destroy() throws Exception {
    channel.disconnect();
    channel.close();

    super.destroy();
  }

  /**
   * Routes an action to the service in the session. If the service does not exist, it is created.
   */
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletRequest req = ((ServletInputData) input).getRequest();
    HttpServletResponse resp = ((ServletOutputData) output).getResponse();
    
    String sessionId = (String) input.getGlobalData().get(SESSION_ID_KEY);
    
    if (sessionId == null && req.getCookies() != null)
      for (int i = 0; i < req.getCookies().length; i++) {
        Cookie c = req.getCookies()[i];
        
        if (SESSION_ID_KEY.equals(c.getName()))
          sessionId = c.getValue();
      }            
    
    if (sessionId == null) {
      do      
        sessionId = RandomStringUtils.randomAlphanumeric(10);
      while (sessions.containsKey(sessionId));
    }
    
    RelocatableService service = 
      (RelocatableService) sessions.get(sessionId);

    synchronized (sessionId.intern()) {
      if (sessions.get(sessionId) == null) {
        log.debug("Created JGroups session '" + sessionId + "'");
        service = new StandardRelocatableServiceDecorator(serviceFactory.buildService(getEnvironment()));

        service._getComponent().init(getEnvironment());
        
        sessions.put(sessionId, service);
      }
      else {
        service = (RelocatableService) sessions.get(sessionId);
        service._getRelocatable().overrideEnvironment(getEnvironment());
        log.debug("Reusing JGroups session '" + sessionId + "'");
      }

      try {
        //ClientStateUtil.put(SESSION_ID_KEY, sessionId, output);
        resp.addCookie(new Cookie(SESSION_ID_KEY, sessionId));        
        
        service._getService().action(path, input, output);
      }
      finally {
        service._getRelocatable().overrideEnvironment(null);
        
        channel.send(null, null, new SessionSync(sessionId, service));        
      }
    }
  }
  
  private static class SessionSync implements Serializable {
    private String sessionId;
    private RelocatableService service;
    
    public SessionSync(String sessionId, RelocatableService service) {
      this.sessionId = sessionId;
      this.service = service;
    }
    
    public RelocatableService getService() {
      return service;
    }
    public String getSessionId() {
      return sessionId;
    }
  }
  
  private class Receiver extends Thread {
    public void run() {
     while (channel.isConnected()) {
       try {
        Object message = channel.receive(-1);
        if (message instanceof Message) {
          SessionSync sessionSync = 
            (SessionSync) ((Message) message).getObject();
          sessions.put(sessionSync.getSessionId(), sessionSync.getService());
        }
      }
      catch (ChannelNotConnectedException e) {
        throw new NestableRuntimeException(e);
      }
      catch (ChannelClosedException e) {
        throw new NestableRuntimeException(e);
      }
      catch (TimeoutException e) {
        throw new NestableRuntimeException(e);
      }
     }
    }
  }
}

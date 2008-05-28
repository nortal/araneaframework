package org.araneaframework.http.filter;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.StateVersioningContext;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.RelocatableUtil;
import org.araneaframework.http.util.ServletUtil;

/**
 * Filter that supports Aranea state versioning.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.2
 */
public class StandardStateVersioningFilterWidget extends BaseFilterWidget implements StateVersioningContext {
  private static final long serialVersionUID = 1L;
  private static final Log log = LogFactory.getLog(StandardStateVersioningFilterWidget.class);

  /** 
   * Default number of states that is stored when this {@link StandardStateVersioningFilterWidget} 
   * is present in component hierarchy. 
   */
  public static final int DEFAULT_MAX_STATES_STORED = 10;
  private int maxVersionedStates = DEFAULT_MAX_STATES_STORED;
  
  /** State identifier of last component hierarchy serviced by this filter. */
  protected String lastStateId = null;
  
  /** Holds <code>Boolean</code> which indicates whether the state has been saved during current request/response cycle. */
  private transient ThreadLocal stateSavedTL = new ThreadLocal();

  /** 
   * Map of states versioning system keeps track of. When using client side state storage,
   * this map consists of <code>Map&lt;String stateId, byte[] stateSHA1Digest&gt;</code>.
   * When using server-side state storage <code>Map&lt;String stateId, byte[] serializedState&gt;</code> 
   * */
  protected LRUMap versionedStates = new LRUMap(DEFAULT_MAX_STATES_STORED);
  
  /* Overrides for BaseFilterWidget methods */
  /**
   * Sets the child to <code>childWidget</code> decorated with {@link RelocatableDecorator}.
   */
  public void setChildWidget(Widget childWidget) {
    this.childWidget = new RelocatableDecorator(childWidget);
  }
  
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), StateVersioningContext.class, this);
  }

  /**
   * Sets the maximum number of states that are tracked by this {@link StandardStateVersioningFilterWidget}.
   * It should be rather small number when states are stored on server-side and rather large number when states
   * are stored on client side.
   *  
   * @param maxVersionedStates number of states to keep track of
   */
  public void setMaxVersionedStates(int maxVersionedStates) {
    if (maxVersionedStates < versionedStates.size()) {
      if (log.isWarnEnabled())
        log.warn("Setting number of max stored states to " + maxVersionedStates + " failed, " + versionedStates.size() + " states already versioned. Setting number of max stored states to " + versionedStates.size());
      maxVersionedStates = versionedStates.size();
    }

    this.maxVersionedStates = maxVersionedStates;

    Map currentStates = versionedStates;
    versionedStates = new LRUMap(this.maxVersionedStates);
    if (currentStates != null)
      versionedStates.putAll(currentStates);
  }
  
  protected synchronized void initStateSavedTL() {
    if (stateSavedTL == null) {
      stateSavedTL = new ThreadLocal();
    }
    stateSavedTL.set(Boolean.FALSE);
  }

  /* Service methods */
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (log.isDebugEnabled())
      log.debug("StandardStateVersioningFilterWidget is routing widget action.");
    
    try {
      restoreChild(getState(lastStateId));
      //restoreState(input);
      super.action(path, input, output);
      // if server-side storage, update the current state
      saveOrUpdateState(lastStateId);
      setResponseStateHeader(output, lastStateId);
    } finally {
      if (stateSavedTL != null) {
        stateSavedTL.set(null);
      }
      childWidget = null;
    }
  }

  /* Widget methods */
  protected void update(InputData input) throws Exception {
    restoreState(input);
    super.update(input);
  }

  protected void event(Path path, InputData input) throws Exception {
    super.event(path, input);
  }

  protected void render(OutputData output) throws Exception {
    setResponseCacheHeaders(output);
    try {
      initStateSavedTL();
      restoreState(output.getInputData());
      super.render(output);
      if (stateSavedTL.get().equals(Boolean.FALSE)) {
        String regions = (String) getOutputData().getInputData().getGlobalData().get(UpdateRegionContext.UPDATE_REGIONS_KEY);
        if (regions.indexOf(StateVersioningContext.GLOBAL_CLIENT_NAVIGATION_REGION_ID) != -1) {
          saveOrUpdateState(lastStateId);
        } else {
          saveState();
        }
      }
      setResponseStateHeader(output, lastStateId);
    } finally {
      stateSavedTL.set(null);
      childWidget = null;
    }
  }

  protected void propagate(Message message) throws Exception {
    super.propagate(message);
  }

  /** 
   * Sets the response headers that disallow caching in general but still allow for using
   * of browser history navigation facilities (back/forward buttons) in most browsers (IE, FF, Opera). 
   * Safari seems to behaves badly in regard to the RFC. */
  protected void setResponseCacheHeaders(OutputData output) {
    HttpServletResponse response = ServletUtil.getResponse(output);
    response.setHeader("Pragma", null);       
    response.setHeader("Cache-Control", null);
  }

  protected void setResponseStateHeader(OutputData output, String stateId) {
    HttpServletResponse response = ServletUtil.getResponse(output);
    response.setHeader(StateVersioningContext.STATE_ID_RESPONSE_HEADER, stateId);
  }

  /** 
   * Chooses the appropriate state and restores it. 
   * Calls {@link StandardStateVersioningFilterWidget#notifyClientNavigationAwareComponents()} when appropriate. 
   */
  protected void restoreState(InputData input) throws Exception {
    // state already restored (usually the case when update/event/render all get called)
    if (childWidget != null) return;

    byte[] serializedState = getState(input);
    restoreChild(serializedState);

    String requestStateId = getStateId(input);
    if (lastStateId != null && !lastStateId.equals(requestStateId) && requestStateId != null)
      notifyClientNavigationAwareComponents();

    if (requestStateId != null)
      lastStateId = requestStateId;
  }

  protected void restoreChild(byte[] serializedState) {
    childWidget = (Widget) SerializationUtils.deserialize(serializedState);
    ((RelocatableWidget) childWidget)._getRelocatable().overrideEnvironment(getChildWidgetEnvironment());
  }

  /**
   * Gets the serialized state for current request.
   * @throws StateExpirationException if state is not available for some reason
   */
  private byte[] getState(InputData input) throws StateExpirationException {
    String requestStateId = getStateId(input);
    
    if (log.isDebugEnabled())
      log.debug("Received service request for versioned component hierarchy '" + requestStateId + "'.");
    
    if (requestStateId == null)
      requestStateId = lastStateId;
    
    if (!versionedStates.containsKey(requestStateId)) {
      if (log.isWarnEnabled())
        log.warn("Received request for restoration of state '" + requestStateId + "' which was not found within versioned states.");
        throw new StateExpirationException("State '" + requestStateId + "' is expired and cannot be restored.");
     // invoke the ExpiredStateHandlerWidget ? ExpirationHandler
        //requestStateId = lastStateId;
    }

    return getState(requestStateId);
  }

  protected byte[] getState(String stateId) {
    return (byte[]) versionedStates.get(stateId);
  }

  /**
   * Returns the state id (under key {@link StateVersioningContext#STATE_ID_REQUEST_KEY} from current request.
   */
  protected String getStateId(InputData input) {
    return (String) input.getGlobalData().get(StateVersioningContext.STATE_ID_REQUEST_KEY);
  }

  /** 
   * Notifies descendants which implement {@link StateVersioningContext.ClientNavigationAware} when
   * state which was previously stored and modified by later actions becomes active again. 
   */
  protected void notifyClientNavigationAwareComponents() {
    ClientNavigationNotifierMessage.INSTANCE.send(null, childWidget);
  }

  /**
   * @return current state with new random identifier*/
  public State saveState() {
    boolean isUR = getOutputData().getInputData().getGlobalData().get(UpdateRegionContext.UPDATE_REGIONS_KEY) != null;
    String rnd = RandomStringUtils.randomAlphanumeric(30);
    return saveOrUpdateState(isUR ? rnd : "HTTP" + rnd);
  }

  /** 
   * Acquires current state and when server-side state versioning is used, stores that for later use.
   * When this {@link StandardStateVersioningFilterWidget} is configured to hold state on client, it
   * will only register state identifier and state checksums, returned state must be saved by someone else.
   * @return current state with supplied identifier
   */
  public State saveOrUpdateState(String stateId) {
    if (stateId == null)
      return saveState();

    byte[] serializedChild = RelocatableUtil.serializeRelocatable((RelocatableWidget) childWidget);
    versionedStates.put(stateId, serializedChild);
    State result = new State(serializedChild, stateId);

    if (log.isDebugEnabled()) {
      log.debug("Registered client state version: " + stateId + " in thread '" + EnvironmentUtil.getThreadServiceId(getEnvironment()) + "'.");
    }

    lastStateId = stateId;
    stateSavedTL.set(Boolean.TRUE);

    return result;
  }

  public void expire() {
    versionedStates.clear();
  }

  /* PROTECTED CLASSES */
  protected static class ClientNavigationNotifierMessage extends BroadcastMessage {
    private static final long serialVersionUID = 1L;
    public static final ClientNavigationNotifierMessage INSTANCE = new ClientNavigationNotifierMessage();

    protected void execute(Component component) throws Exception {
      if (component instanceof ClientNavigationAware) {
        ClientNavigationAware comp = (ClientNavigationAware) component;
        comp.onClientNavigation();
      }
    }
  }
}

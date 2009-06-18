
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
 * Filter that supports Aranea state versioning. It keeps its "childWidgets" in
 * serialized form associated with a state ID. When a request comes in, it
 * deserializes a childWidget with appropriate state ID and delegates actions to
 * it. When a request ends, it serializes and stores the childWidget once again.
 * <p>
 * Also note that not every state is requested from the client side when the user
 * navigates using the browser's back/forward buttons. Those pages where update
 * regions exist, exist in a state with an ID that does not begin with "HTTP".
 * Only those are requested by "aranea-rsh.js". Those page where state ID starts
 * with "HTTP" are handled by "rsh.js". However, this widget needs to remember
 * all the states to return to those as the user wishes.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.2
 */
public class StandardStateVersioningFilterWidget extends BaseFilterWidget
  implements StateVersioningContext {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(
      StandardStateVersioningFilterWidget.class);

  /**
   * Default number of states that is stored when this
   * {@link StandardStateVersioningFilterWidget} is present in component
   * hierarchy.
   */
  public static final int DEFAULT_MAX_STATES_STORED = 20;

  private int maxVersionedStates = DEFAULT_MAX_STATES_STORED;

  /**
   * State ID of the last component hierarchy serviced by this filter.
   */
  protected String lastStateId = null;

  /**
   * This <code>ThreadLocal</code> is used to hold a <code>Boolean</code>, which
   * indicates whether the state has been saved during current request/response
   * cycle.
   */
  private transient ThreadLocal stateSavedTL = new ThreadLocal();

  /**
   * Map of states that this versioning system keeps track of. When using client
   * side state storage, this map consists of
   * <code>Map&lt;String stateId, byte[] stateSHA1Digest&gt;</code>. When using
   * server-side state storage
   * <code>Map&lt;String stateId, byte[] serializedState&gt;</code>
   */
  protected LRUMap versionedStates = new LRUMap(DEFAULT_MAX_STATES_STORED);

  // Overrides for BaseFilterWidget methods
  /**
   * Sets the child to <code>childWidget</code> decorated with
   * {@link RelocatableDecorator}. This is mainly used internally as the
   * <code>childWidget</code> is serialized and later restored.
   * <p>
   * The difference between this and the parent implementation of this method is
   * that the <code>childWidget</code> is relocatable here.
   */
  public void setChildWidget(Widget childWidget) {
    this.childWidget = new RelocatableDecorator(childWidget);
  }

  /**
   * Enriches the <code>Environment</code> of child-widgets with the current
   * instance of <code>StateVersioningContext</code>.
   */
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(),
        StateVersioningContext.class, this);
  }

  /**
   * Sets the maximum number of states that are tracked by this
   * {@link StandardStateVersioningFilterWidget}. It should be rather small
   * number when states are stored on server-side and rather large number when
   * states are stored on client side.
   * 
   * @param maxVersionedStates number of states to keep track of
   */
  public void setMaxVersionedStates(int maxVersionedStates) {
    if (maxVersionedStates < this.versionedStates.size()) {

      if (log.isWarnEnabled()) {
        log.warn("Changing number of max stored states to "
            + maxVersionedStates + " failed, because "
            + this.versionedStates.size() + " states already versioned. "
            + "Changing the number of max stored states to "
            + this.versionedStates.size());
      }

      maxVersionedStates = this.versionedStates.size();
    }

    this.maxVersionedStates = maxVersionedStates;
    Map currentStates = this.versionedStates;
    this.versionedStates = new LRUMap(this.maxVersionedStates);

    if (currentStates != null) {
      this.versionedStates.putAll(currentStates);
    }
  }

  protected synchronized void initStateSavedTL() {
    if (this.stateSavedTL == null) {    
      this.stateSavedTL = new ThreadLocal();    
    }
    this.stateSavedTL.set(Boolean.FALSE);
  }

  /* Service methods */
  protected void action(Path path, InputData input, OutputData output)
      throws Exception {

    if (log.isDebugEnabled()) {
      log.debug("StandardStateVersioningFilterWidget is routing widget action.");
    }

    try {
      restoreChild(getState(this.lastStateId));

      // restoreState(input);
      super.action(path, input, output);

      // if server-side storage, update the current state
      saveOrUpdateState(this.lastStateId);
      setResponseStateHeader(output, this.lastStateId);

    } finally {
      if (this.stateSavedTL != null) {
        this.stateSavedTL.set(null);
      }
      this.childWidget = null;
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

      // Execute this IF-block when state has not been stored yet:
      if (Boolean.FALSE.equals(this.stateSavedTL.get())) {

        String regions = (String) output.getInputData().getGlobalData().get(
            UpdateRegionContext.UPDATE_REGIONS_KEY);

        // This first condition is true when the "regions" contains
        // "araneaGlobalClientHistoryNavigationUpdateRegion". This should occur
        // when the browser's back/forward navigation is used (a JavaScript will
        // invoke the request with that update region; see aranea-rsh.js).

        if (regions != null
            && regions.indexOf(StateVersioningContext.GLOBAL_CLIENT_NAVIGATION_REGION_ID) != -1) {
          saveOrUpdateState(this.lastStateId);
        } else {
          saveState(); // Saves the new state.
        }
      }

      setResponseStateHeader(output, this.lastStateId);
    } finally {
      this.stateSavedTL.set(null);
      this.childWidget = null;
    }
  }

  /**
   * Sets the response headers that disallow caching in general but still allow
   * for using of browser history navigation facilities (back/forward buttons)
   * in most browsers (IE, FF, Opera). Safari seems to behaves badly in regard
   * to the RFC.
   */
  protected void setResponseCacheHeaders(OutputData output) {
    HttpServletResponse response = ServletUtil.getResponse(output);
    response.setHeader("Pragma", null);
    response.setHeader("Cache-Control", null);
  }

  /**
   * Sets the state ID as a header, which is needed when an AJAX request is
   * coming from the client.
   */
  protected void setResponseStateHeader(OutputData output, String stateId) {
    HttpServletResponse response = ServletUtil.getResponse(output);
    response.setHeader(StateVersioningContext.STATE_ID_RESPONSE_HEADER, stateId);
  }

  /**
   * Chooses the appropriate state and restores it. Calls
   * {@link StandardStateVersioningFilterWidget#notifyClientNavigationAwareComponents()}
   * when appropriate.
   */
  protected void restoreState(InputData input) throws Exception {
    // state already restored (usually the case when update/event/render all get
    // called)
    if (this.childWidget != null) {
      return;
    }

    // The "getState()" method returns the state based on the input data:
    byte[] serializedState = getState(input);

    // Now we deserialize the state and set it as a childWidget:
    restoreChild(serializedState);

    // Now let's compare the state ID from the request and the state ID from the
    // previous request. If they do not match then this indicates a back/forward
    // move (in this case, navigation aware components are notified of this).

    String requestStateId = getStateId(input);

    if (this.lastStateId != null && requestStateId != null
        && !this.lastStateId.equals(requestStateId)) {
      notifyClientNavigationAwareComponents();
    }

    if (requestStateId != null) {
      this.lastStateId = requestStateId;
    }
  }

  protected void restoreChild(byte[] serializedState) {
    this.childWidget = (Widget) SerializationUtils.deserialize(serializedState);
    ((RelocatableWidget) this.childWidget)._getRelocatable()
        .overrideEnvironment(getChildWidgetEnvironment());
  }

  /**
   * Retrieves the serialized state for the current request (<tt>input</tt>).
   * 
   * @throws StateExpirationException if state is not available for some reason
   */
  private byte[] getState(InputData input) throws StateExpirationException {
    String requestStateId = getStateId(input);

    if (log.isDebugEnabled()) {
      log.debug("Received service request for versioned component hierarchy '"
          + requestStateId + "'.");
    }

    if (requestStateId == null || requestStateId.trim().length() == 0) {
      requestStateId = this.lastStateId;
    }

    if (this.versionedStates.get(requestStateId) == null) {
      if (log.isWarnEnabled()) {
        log.warn("Received request for restoration of state '" + requestStateId
            + "' which was not found within versioned states.");
      }
      throw new StateExpirationException("State '" + requestStateId
          + "' is expired and cannot be restored.");
      // Note: here could be a possibility to introduce ExpiredStateHandler
    }

    return getState(requestStateId);
  }

  protected byte[] getState(String stateId) {
    return (byte[]) this.versionedStates.get(stateId);
  }

  /**
   * Returns the state id (under key
   * {@link StateVersioningContext#STATE_ID_REQUEST_KEY} from current request.
   */
  protected String getStateId(InputData input) {
    return (String) input.getGlobalData().get(STATE_ID_REQUEST_KEY);
  }

  /**
   * Notifies descendants which implement
   * {@link StateVersioningContext#ClientNavigationAware} when state which was
   * previously stored and modified by later actions becomes active again.
   */
  protected void notifyClientNavigationAwareComponents() {
    ClientNavigationNotifierMessage.INSTANCE.send(null, this.childWidget);
  }

  /**
   * Saves the current state as a new state. The new state will get a new random
   * ID that will have a prefix "HTTP" if the request is simple browser's HTTP
   * request.
   * 
   * @return The current state with new random identifier
   */
  public State saveState() {
    boolean isUR = getOutputData().getInputData().getGlobalData().get(
        UpdateRegionContext.UPDATE_REGIONS_KEY) != null;

    String rnd = RandomStringUtils.randomAlphanumeric(30);

    return saveOrUpdateState(isUR ? rnd
        : StateVersioningContext.HTTP_REQUEST_STATEPREFIX + rnd);
  }

  /**
   * Acquires current state and when server-side state versioning is used,
   * stores that for later use. When this
   * {@link StandardStateVersioningFilterWidget} is configured to hold state on
   * client, it will only register state identifier and state checksums,
   * returned state must be saved by someone else.
   * 
   * @return current state with supplied identifier
   */
  public State saveOrUpdateState(String stateId) {
    if (stateId == null) {
      return saveState();
    }

    if (this.childWidget == null) {
      if (log.isWarnEnabled()) {
        log.warn("childWidget is null for some unknown reason; returning...");
      }
      return null;
    }

    if (log.isDebugEnabled()) {
      log.debug("Serializing '" + this.childWidget + "' as state '" + stateId
          + "'.");
    }

    byte[] serializedChild = RelocatableUtil
        .serializeRelocatable((RelocatableWidget) this.childWidget);

    this.versionedStates.put(stateId, serializedChild);

    State result = new State(serializedChild, stateId);

    if (log.isDebugEnabled()) {
      log.debug("Registered client state version: " + stateId + " in thread '"
          + EnvironmentUtil.getThreadServiceId(getEnvironment()) + "'.");
    }

    this.lastStateId = stateId;
    this.stateSavedTL.set(Boolean.TRUE);

    return result;
  }

  public void expire() {
    this.versionedStates.clear();
  }

  protected void destroy() throws Exception {
    expire();
    if (this.childWidget != null) {
      super.destroy();
    }
  }

  /**
   * A static class (use <code>ClientNavigationNotifierMessage.INSTANCE</code>)
   * that is used to notify all widgets in the hirearchy that a browser's
   * back/forward navigation has occured.
   * 
   * @see ClientNavigationAware
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.2
   */
  protected static class ClientNavigationNotifierMessage
    extends BroadcastMessage {

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

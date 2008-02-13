package org.araneaframework.http.filter;

import java.util.Map;
import net.iharder.base64.Base64;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.StateVersioningContext;
import org.araneaframework.http.util.EncodingUtil;
import org.araneaframework.http.util.JsonObject;
import org.araneaframework.http.util.RelocatableUtil;

/**
 * Filter that supports Aranea state versioning.
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

  private boolean serverSideStorage;
  private int maxVersionedStates = DEFAULT_MAX_STATES_STORED;
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

  /* INJECTORS/SETTERS */
  public void setServerSideStorage(boolean b) {
    serverSideStorage = b;
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

  /* Service methods */
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    super.action(path, input, output);
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
    try {
      restoreState(output.getInputData());
      super.render(output);
    } finally {
      childWidget = null;
    }
  }
  
  /** Chooses the appropriate state and restores it. */
  protected void restoreState(InputData input) throws Exception {
    // state already restored (usually the case when update/event/render all get called)
    if (childWidget != null) return;
    
    byte[] serializedState = getState(input);
    childWidget = (Widget) SerializationUtils.deserialize(serializedState);
    ((RelocatableWidget) childWidget)._getRelocatable().overrideEnvironment(getChildWidgetEnvironment());
  }

  /**
   * Gets the serialized state for current request.
   * @throws Exception if state is invalid for some reason
   */
  private byte[] getState(InputData input) throws Exception {
    String requestStateId = getStateId(input);
    
    if (log.isDebugEnabled())
      log.debug("Received service request for versioned component hierarchy '" + requestStateId + "'.");
    
    if (!versionedStates.containsKey(requestStateId)) {
      throw new IllegalStateException("Invalid/inaccessible state identifier received from request.");
    }
    
    if (serverSideStorage)
      return (byte[]) versionedStates.get(requestStateId);
    
    // get and verify state submitted by client
    String state = (String)input.getGlobalData().get(StateVersioningContext.STATE_KEY);
    byte[] decodedState = Base64.decode(state);
    byte[] stateDigest = (byte[]) versionedStates.get(requestStateId);
    
    if (!EncodingUtil.checkDigest(decodedState, stateDigest))
      throw new IllegalStateException("Client side state digest invalid.");

    return decodedState;
  }

  /**
   * Returns the state id (under key {@link StateVersioningContext#STATE_ID_KEY} from current request.
   */
  protected String getStateId(InputData input) {
    return (String) input.getGlobalData().get(StateVersioningContext.STATE_ID_KEY);
  }
  
  /* UpdateRegionProvider IMPLEMENTATION */
  public Map getRegions() {
    // do not create new state -- instead update the current one
    State currentState = saveState(getStateId(getInputData()));

    JsonObject stateRegion = new JsonObject();

    stateRegion.setStringProperty(StateVersioningContext.STATE_ID_KEY, currentState.getStateId());
    if (!isServerSideStorage())
      stateRegion.setStringProperty(STATE_KEY, currentState.getState().toString());

    return new SingletonMap(StateVersioningContext.STATE_VERSIONING_UPDATE_REGION_KEY, stateRegion.toString());
  }

  /* 
   * StateVersioningContext IMPLEMENTATION
   */
  public boolean isServerSideStorage() {
    return serverSideStorage;
  }

  /** @return current state with new random identifier*/
  public State saveState() {
    return saveState(RandomStringUtils.randomAlphanumeric(30));
  }

  /** 
   * Acquires current state and when server-side state versioning is used, stores that for later use.
   * When this {@link StandardStateVersioningFilterWidget} is configured to hold state on client, it
   * will only register state identifier and state checksums, returned state must be saved by someone else.
   * @return current state with supplied identifier
   */
  public State saveState(String stateId) {
    byte[] serializedChild = RelocatableUtil.serializeRelocatable((RelocatableWidget) childWidget);
    String b64Child = Base64.encodeBytes(serializedChild, Base64.DONT_BREAK_LINES);
    versionedStates.put(stateId, isServerSideStorage() ? serializedChild : EncodingUtil.buildDigest(serializedChild));
    State result = new State(isServerSideStorage() ? (Object)serializedChild : (Object)b64Child, stateId);

    if (log.isDebugEnabled()) {
      log.debug("Registered client state version: " + stateId);
    }

    return result;
  }
}

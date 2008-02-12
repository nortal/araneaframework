package org.araneaframework.http.filter;

import java.util.Map;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.StateVersioningContext;

/**
 * Filter that supports Aranea state versioning.
 * @author Taimo Peelo (taimo@araneaframework.org)
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
  protected void restoreState(InputData input) {
    // state already restored (usually the case when update/event/render all get called)
    if (childWidget != null) return;
    
    validateState(input);
  }
  
  private byte[] validateState(InputData input) {
    String requestStateId = getStateId(input);
    if (!versionedStates.containsKey(requestStateId)) {
      throw new IllegalStateException("Invalid/inaccessible state identifier received from request.");
    }

    return (byte[]) versionedStates.get(requestStateId);
  }

  protected String getStateId(InputData input) {
    return (String) input.getGlobalData().get(StateVersioningContext.STATE_ID_KEY);
  }

  /* 
   * StateVersioningContext IMPLEMENTATION
   */
  public boolean isServerSideStorage() {
    return serverSideStorage;
  }

  public State registerState() {
    return null;
  }
}

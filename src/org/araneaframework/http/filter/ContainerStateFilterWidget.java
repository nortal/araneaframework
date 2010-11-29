package org.araneaframework.http.filter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.ContainerStateContext;

/**
 * @author Maksim Boiko <mailto:max@webmedia.ee>
 */
public class ContainerStateFilterWidget extends BaseFilterWidget implements ContainerStateContext {
  private static final Log LOG = LogFactory.getLog(ContainerStateFilterWidget.class);
  
  public static final String CONTAINER_STATE_ID_PREFIX = "CONTAINER_CLOSED_STATE";

  protected LinkedList<Map<String, Boolean>> savedContainerStates = new LinkedList<Map<String, Boolean>>();

  protected Map<String, Boolean> currentState = new HashMap<String, Boolean>();

  /* ************************************************************************
   * ContainerStateContext interface methods.
   * **********************************************************************
   */

  public void reset() {
    resetCurrent();
    this.savedContainerStates.clear();

    if (LOG.isDebugEnabled()) {
      LOG.debug("RESETTED all coords ");
    }
  }

  public void pop() {
    if (!this.savedContainerStates.isEmpty()) {
      currentState = this.savedContainerStates.removeFirst();

      if (LOG.isDebugEnabled()) {
        LOG.debug("popped to state " + this.currentState);
      }
    }
  }

  public void push() {
    this.savedContainerStates.addFirst(currentState);

    if (LOG.isDebugEnabled()) {
      LOG.debug("pushed state " + currentState);
    }

    resetCurrent();
  }

  public void resetCurrent() {
    this.currentState = new HashMap<String, Boolean>();
  }
  
  public Map<String, Boolean> getCurrentState() {
    return this.currentState;
  }

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), ContainerStateContext.class, this);
  }

  @Override
  protected void update(InputData input) throws Exception {
    for(Map.Entry<String, String> entry : input.getGlobalData().entrySet()) {
      if(entry.getKey().startsWith(CONTAINER_STATE_ID_PREFIX)) {
        currentState.put(entry.getKey(), "true".equals(entry.getValue()));
      }
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("REQUEST STATE READ TO BE  to " + this.currentState);
    }

    super.update(input);
  }

  @Override
  protected void render(OutputData output) throws Exception {
    super.render(output);
  }
}

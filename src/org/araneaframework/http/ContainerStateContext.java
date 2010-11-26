package org.araneaframework.http;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Maksim Boiko <mailto:max@webmedia.ee>
 */
public interface ContainerStateContext extends Serializable {
  public Map<String, Boolean> getCurrentState();

  /**
   * Resets all the remembered container states to nothing. ({@link #pop()} will not have any further effect).
   */
  void reset();

  /**
   * Resets currently active containers states to null.
   */
  void resetCurrent();

  /**
   * Resets the current containers state, which can be restored with {@link #pop}.
   */
  void push();

  /**
   * Restores the previously pushed containers states.
   */
  void pop();
}

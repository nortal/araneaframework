package org.araneaframework.http;

import java.io.Serializable;
import org.araneaframework.core.ApplicationComponent;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface StateVersioningContext extends Serializable {
  /** Key for request parameter that holds state (if parameter present). */
  public static final String STATE_KEY = "araClientState";
  /** Key for request parameter that holds state identifier (if parameter present). */
  public static final String STATE_ID_KEY = "araClientStateId";

  /**
   * Registers versioned state at the moment of calling this method.
   * @return snapshot of versioned state
   */
  public State registerState();
  
  /**
   * Returns whether the state versions are held on server. If not, and implementation
   * is active in a component hierarchy it means that state is only present on client-side.
   * @return whether the state versions are held on server
   */
  public boolean isServerSideStorage();

  /**
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.2
   */
  public static class State {
    private Object state;
    private String stateId;
    public State(Object state, String stateVersion) {
      this.state = state;
      this.stateId = stateVersion;
    }
    public Object getState() {
      return state;
    }
    public String getStateId() {
      return stateId;
    }
  }

  /**
   * Interface that should be implemented by {@link  org.araneaframework.Component}s 
   * who wish to be notified when client uses browser history mechanism for navigating
   * application states.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  public interface ClientNavigationAware extends ApplicationComponent {
    public void onClientNavigation(String param);
  }
}

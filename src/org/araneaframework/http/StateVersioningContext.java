package org.araneaframework.http;

import org.araneaframework.core.ApplicationComponent;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.2
 */
public interface StateVersioningContext extends UpdateRegionProvider {
  /** Key for request parameter that holds state (if parameter present). */
  public static final String STATE_KEY = "araClientState";
  /** Key for request parameter that holds state identifier (if parameter present). */
  public static final String STATE_ID_KEY = "araClientStateId";
  
  /** Key for */
  public static final String STATE_VERSIONING_UPDATE_REGION_KEY = "araStateVersionRegion";

  /**
   * Saves state at the moment of calling this method.
   * State will have a generated id.
   * @return snapshot of saved state
   */
  public State saveState();

  /**
   * Saves state at the moment of calling this method. State will
   * have a supplied <code>stateId</code>
   * @return snapshot of saved state
   */
  public State saveState(String stateId);

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
    public State(Object state, String stateId) {
      this.state = state;
      this.stateId = stateId;
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

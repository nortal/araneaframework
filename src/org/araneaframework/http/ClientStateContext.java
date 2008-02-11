package org.araneaframework.http;

import org.araneaframework.core.ApplicationComponent;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.2
 */
public interface ClientStateContext {
  /**
   * Global parameter key for the client state form input.
   */
  public static final String CLIENT_STATE = "araClientState";

  /**
   * Global parameter key for the version of the client state form input.
   */
  public static final String CLIENT_STATE_VERSION = "araClientStateVersion";

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
  
  public void addClientNavigationListener(ClientNavigationListener listener);
  public void removeClientNavigationListener(ClientNavigationListener listener);
  
  /**
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.2
   */
  public static class State {
    private Object state;
    private String stateVersion;
    public State(Object state, String stateVersion) {
      this.state = state;
      this.stateVersion = stateVersion;
    }
    public Object getState() {
      return state;
    }
    public String getStateVersion() {
      return stateVersion;
    }
  }
  
  public interface ClientNavigationListener extends ApplicationComponent {
    public void onClientNavigation(String param);
  }
}
package org.araneaframework.http;

public interface ClientStateContext {
  /**
   * Global parameter key for the client state form input.
   */
  public static final String CLIENT_STATE = "araClientState";

  /**
   * Global parameter key for the version of the client state form input.
   */
  public static final String CLIENT_STATE_VERSION = "araClientStateVersion";

  public void addSystemFormState() throws Exception;

  public static class State {
    private Object state;
    private String stateVersion;
    public State(Object state, String stateVersion) {
      this.state = state;
      this.stateVersion = stateVersion;
    }
  }
}
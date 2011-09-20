/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.http;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * Interface of a component hierarchy state versioning filter. This allows to support client-side history navigation by
 * storing the versioned hierarchies between requests.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.2
 */
public interface StateVersioningContext extends Serializable {

  /**
   * Key for request parameter that holds state identifier (if parameter present).
   */
  String STATE_ID_REQUEST_KEY = "araClientStateId";

  /**
   * Identifier of response header which contains state version information.
   */
  String STATE_ID_RESPONSE_HEADER = "Aranea-Application-StateVersion";

  /**
   * Identifier of update region which must be updated when client-side history navigation occurs.
   */
  String GLOBAL_CLIENT_NAVIGATION_REGION_ID = "araneaGlobalClientHistoryNavigationUpdateRegion";

  /**
   * State identifiers that were created during full HTTP requests will get this prefix.
   */
  String HTTP_REQUEST_STATE_PREFIX = "HTTP";

  /**
   * State IDs in request can have this prefix followed by a non-HTTP state ID indicating an HTTP state that started the
   * provided non-HTTP state.
   * 
   * @since 2.0
   */
  String HTTP_REQUEST_LOOKUP_PREFIX = HTTP_REQUEST_STATE_PREFIX + ":";

  /**
   * Saves state of the component tree at the moment of calling this method. State will get an ID.
   * 
   * @return State snapshot
   */
  State saveState();

  /**
   * Provides the current state ID as it will also be included on the rendered page. A state ID is generated before a
   * request is handled. The underlying method should return the same request ID during the same request.
   * 
   * @return The current state ID.
   */
  String getCurrentStateId();

  /**
   * Expires all stored versioned states. Should be called when current flow has completed some truly irreversible
   * operation &mdash; e.g. calling remote service to bill a credit card etc etc.
   */
  void expire();

  /**
   * A static class representing a state in the state versioning context.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.2
   */
  static class State implements Serializable {

    private final Object state;

    private final String stateId;

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
   * A static class representing a state expiration exception that is thrown when some one tries to reach discarded
   * state.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.2
   */
  class StateExpirationException extends NestableRuntimeException {

    public StateExpirationException() {
    }

    public StateExpirationException(String msg, Throwable cause) {
      super(msg, cause);
    }

    public StateExpirationException(String msg) {
      super(msg);
    }

    public StateExpirationException(Throwable cause) {
      super(cause);
    }
  }

  /**
   * Interface that should be implemented by {@link org.araneaframework.Component}s who wish to be notified of various
   * events in state versioning context. These callbacks are guaranteed to be called after the current state has been
   * established. Therefore all implementors can be components.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 1.2 (introduced), 2.0 (refactored)
   */
  interface ClientNavigationAware {

    /**
     * This is called when the incoming request sends an <code>event</code> for a component to do something. The result
     * is not determined and the post-request state will be added to existing states.
     * 
     * @param baseStateId The ID of the state that the request depends on. It is <code>null</code> on first request when
     *          no versioned state exists yet.
     * @param newStateId The ID that will be assigned to the created state.
     */
    void onUsualNavigation(String baseStateId, String newStateId);

    /**
     * This is called when the incoming request sends an AJAX-based <code>action</code> for a component to do something.
     * The hierarchy of the base state is not expected to change, maybe the state of some widgets changes. All states
     * that are altered by <code>action</code> requests are just updated, no new state will be added.
     * 
     * @param currentStateId The ID of the state the request depends on. The state will be updated with the current
     *          state.
     */
    void onCurrentStateUpdate(String currentStateId);

    /**
     * This is called when the incoming request is for retrieving an existing state. No changes will be done to stored
     * states
     * 
     * @param sourceStateId The ID of the state from which navigation is started.
     * @param targetStateId The ID of the state where navigation should end.
     */
    void onHistoryNavigation(String sourceStateId, String targetStateId);

    /**
     * This is called when changes are made to the stored states. The changes include expiring or deleting because of
     * they are not accessible any more. The latter is case like when the user moves in the application from state A
     * through state B to state C, then moves back to state B, and starts a new state D, after which state C will be
     * discarded. The changes do not include updating a state as in {@link #onCurrentStateUpdate(String)}.
     * 
     * @param stateIds The unmodifiable list of current valid states.
     */
    void afterStatesChange(List<String> stateIds);

  }
}

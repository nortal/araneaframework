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
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.araneaframework.core.ApplicationComponent;

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
  public static final String STATE_ID_REQUEST_KEY = "araClientStateId";

  /**
   * Identifier of response header which contains state version information.
   */
  public static final String STATE_ID_RESPONSE_HEADER = "Aranea-Application-StateVersion";

  /**
   * Identifier of update region which must be updated when client-side history navigation occurs.
   */
  public static final String GLOBAL_CLIENT_NAVIGATION_REGION_ID = "araneaGlobalClientHistoryNavigationUpdateRegion";

  /**
   * State identifiers that were created during full HTTP requests will get this prefix.
   */
  public static final String HTTP_REQUEST_STATEPREFIX = "HTTP";

  /**
   * Saves state of the component tree at the moment of calling this method. State will get a random id.
   * 
   * @return State snapshot
   */
  public State saveState();

  /**
   * Saves state at the moment of calling this method. State will have a supplied <code>stateId</code>
   * 
   * @return snapshot of saved state
   */
  public State saveOrUpdateState(String stateId);

  /**
   * Expires all stored versioned states. Should be called when current flow has completed some truly irreversible
   * operation -- i.e calling remote service to bill a credit card etc etc.
   */
  public void expire();

  /**
   * A static class representing a state in the state versioning context.
   * 
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
   * A static class representing a state expiration exception that is thrown when some one tries to reach discarded
   * state.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.2
   */
  public static class StateExpirationException extends NestableRuntimeException {

    public StateExpirationException() {}

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
   * Interface that should be implemented by {@link org.araneaframework.Component}s who wish to be notified when client
   * uses browser history mechanism for navigating application states.
   * 
   * @since 1.2
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  public interface ClientNavigationAware extends ApplicationComponent {

    public void onClientNavigation();
  }
}

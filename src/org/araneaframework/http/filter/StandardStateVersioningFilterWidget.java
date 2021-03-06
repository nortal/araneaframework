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

package org.araneaframework.http.filter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.StateVersioningContext;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.RelocatableUtil;
import org.araneaframework.http.util.ServletUtil;

/**
 * Filter that supports Aranea state versioning. It keeps its "childWidgets" in serialized form associated with a state
 * ID. When a request comes in, it deserializes a childWidget with appropriate state ID and delegates actions to it.
 * When a request ends, it serializes and stores the childWidget once again.
 * <p>
 * Also note that not every state is requested from the client side when the user navigates using the browser's
 * back/forward buttons. Those pages where update regions exist, exist in a state with an ID that does not begin with
 * "HTTP". Only those are requested by "aranea-rsh.js". Those page where state ID starts with "HTTP" are handled by
 * "rsh.js". However, this widget needs to remember all the states to return to those as the user wishes.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2
 */
public class StandardStateVersioningFilterWidget extends BaseFilterWidget implements StateVersioningContext {

  private static final Log LOG = LogFactory.getLog(StandardStateVersioningFilterWidget.class);

  /**
   * Default number of states that is stored when this {@link StandardStateVersioningFilterWidget} is present in
   * component hierarchy.
   */
  public static final int DEFAULT_MAX_STATES_STORED = 20;
  
  public static final String STATES_COOKIE_NAME_SUFFIX = "_araStates";

  /**
   * This field will be filled in when the first request comes in and before the request will be processed by child
   * components. Since the initial state is the same for all threads, we keep it in a static variable.
   */
  protected static byte[] originalState;

  /**
   * Stores the number of states to remember. Let's keep it private so that logic around this parameter would be
   * enforced in {@link #setMaxVersionedStates(int)}.
   */
  private int maxVersionedStates = DEFAULT_MAX_STATES_STORED;

  /**
   * The ID of the state the that incoming request expects to be the last. This is filled in before a request is
   * processed. Therefore, methods can expect this value to be up-to-date and don't have to resolve it each time.
   */
  protected String lastStateId;

  /**
   * The ID of the current state as it will be saved. This is filled in before a request is processed. Therefore,
   * methods can expect this value to be up-to-date and don't have to resolve it each time.
   * 
   * @since 2.0
   */
  protected String newStateId;

  /**
   * Map of states that this versioning system keeps track of. When using client side state storage, this map consists
   * of <code>Map&lt;String stateId, byte[] stateSHA1Digest&gt;</code>. When using server-side state storage
   * <code>Map&lt;String stateId, byte[] serializedState&gt;</code>
   */
  protected LinkedList<State> versionedStates = new LinkedList<State>();
  
  protected boolean expiredDuringRequest = false;

  protected transient ThreadLocal<Boolean> stateSaved; 

  // =============================================================
  // * Overriding BaseFilterWidget Methods
  // =============================================================

  /**
   * Sets the child to <code>childWidget</code> decorated with {@link RelocatableDecorator}. This is mainly used
   * internally as the <code>childWidget</code> is serialized and later restored.
   * <p>
   * The difference between this and the parent implementation of this method is that the <code>childWidget</code> is
   * relocatable here.
   */
  @Override
  public void setChildWidget(Widget childWidget) {
    this.childWidget = new RelocatableDecorator(childWidget);
  }

  /**
   * Enriches the <code>Environment</code> of child-widgets with the current instance of
   * <code>StateVersioningContext</code>.
   */
  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), StateVersioningContext.class, this);
  }

  @Override
  protected void propagate(Message message) throws Exception {
    // Restore the state just in case it's not restored yet:
    if (this.childWidget == null) {
      handleStateRestoration(false);
    }

    super.propagate(message);
    // Not saving the state here. It has to be done by action(...) or render(...).
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    synchronized (this) {
      try {
        expiredDuringRequest = false;
        handleStateRestoration(false);
        notifyAboutNavigation(true);
        writeHeaders();

        // assume that list legal states does not change in action
        addStatesCookie( output );
        this.childWidget._getService().action(path, input, output);

        saveState();
        
      } finally {
        this.childWidget = null;
      }
    }
  }

  @Override
  protected void update(InputData input) throws Exception {
    synchronized (this) {
      expiredDuringRequest = false;
      handleStateRestoration(true);
      notifyAboutNavigation(false);
      writeHeaders(); 

      super.update(input);
    }
  }

  @Override
  protected void event(Path path, InputData input) throws Exception {
    synchronized (this) {
      super.event(path, input);
    }
  }

  @Override
  protected void render(OutputData output) throws Exception {
    synchronized (this) {
      try {
        // assume that list legal states does not change during render
        addStatesCookie( output );


        // Before rendering, we add the system form parameter with stateId value to be rendered with the form.
        // The stateId value does not need to provided when update regions are used.
        String regionsFromRequest = getRequestParam(UpdateRegionContext.UPDATE_REGIONS_KEY);
        String stateId = regionsFromRequest == null ? this.newStateId : "";

        getEnvironment().requireEntry(SystemFormContext.class).addField(STATE_ID_REQUEST_KEY, stateId);

        super.render(output);

        // Finally, when rendering is completed, we store the current state. The saveState() method is given full
        // autonomy to decide whether to save and how to manage the states.
        saveState();
      } finally {
        this.childWidget = null; // Release the child widget. We will use stored states to restore it the next time.
        expiredDuringRequest = false;
      }
    }
  }

  @Override
  protected void destroy() throws Exception {
    expire();

    if (this.childWidget != null) {
      super.destroy();
    }
  }
  
  protected void addStatesCookie( OutputData output ){
    HttpServletResponse response = ServletUtil.getResponse( output );
    ThreadContext threadCtx = getEnvironment().getEntry( ThreadContext.class );
    StringBuilder sb = new StringBuilder();
    Set<String> ids = new LinkedHashSet<String>();
      
    // emulate state discarding/adding done in saveState()/discardOldStatesOnLimitExceed() 
    for (Iterator<State> it =  versionedStates.iterator(); it.hasNext(); ) {
      ids.add( it.next().getStateId() );
    }
    if (!ids.contains( this.newStateId )) {
      ids.add( this.newStateId );
    }
    
    while (!ids.isEmpty() && ids.size() > this.maxVersionedStates) {
      ids.remove( ids.iterator().next());
    }
    
    // cookie form: "stateId|stateId|... "
    for (Iterator<String> it =  ids.iterator(); it.hasNext(); ) {
      sb.append(it.next());
      if (it.hasNext()) {
        sb.append("|");
      }
    }
    
    if (expiredDuringRequest && ids.size() == 1) { // hack -- write out the state identifier -- twice
        sb.append("|").append(ids.iterator().next());
    }
      
    Cookie cookie = new Cookie(threadCtx.getCurrentId() + STATES_COOKIE_NAME_SUFFIX, sb.toString());
    String contextPath = ServletUtil.getRequest( output.getInputData() ).getContextPath();
    cookie.setPath( contextPath);
    response.addCookie(cookie);
  }

  // =============================================================
  // * State Versioning Implementation
  // =============================================================

  /**
   * Executes generalized logic to restore a correct state. Therefore, it's not just about restoring a state, but also
   * about resolving the state ID, handling cases when state is expired.
   * 
   * @param event This parameter should be set to <code>true</code> when the state is restored for an event. Otherwise
   *          should be <code>false</code>.
   * @since 2.0
   */
  protected synchronized void handleStateRestoration(boolean event) throws Exception {
    storeOriginalState();

    if (stateSaved == null) {
      this.stateSaved = new ThreadLocal<Boolean>();
    }

    this.stateSaved.set(false);

//    String previousStateId = this.lastStateId;

    this.lastStateId = resolvePreviousStateId();
    if (event) {
      this.newStateId = resolveCurrentStateId();
    }

    // Handles page refresh: does not generate new state.
    // TODO The commented code is a bad solution:
//    if (previousStateId != null && StringUtils.equals(previousStateId, this.lastStateId)) {
//      this.newStateId = this.lastStateId;
//    }

    checkStateExpired();

    if (LOG.isDebugEnabled()) {
      if (event) {
        LOG.debug("The processed state '" + this.lastStateId + "' will be versioned as '" + this.newStateId + "'.");
      } else {
        LOG.debug("Using state '" + this.lastStateId + "' to route action() to.");
      }
    }

    restoreState();
    discardInaccessibleStates();
  }

  protected synchronized boolean containsState(String id) {
    boolean contains = false;

    for (State state : this.versionedStates) {
      if (state.getStateId().equals(id)) {
        contains = true;
        break;
      }
    }

    return contains;
  }

  /**
   * Used internally to simplify request parameter value lookup.
   * 
   * @param name The name of the parameter.
   * @return The value of the parameter as it is in global data.
   * @since 2.0
   */
  protected synchronized String getRequestParam(String name) {
    return getInputData().getGlobalData().get(name);
  }

  /**
   * Retrieves the state ID value from the request. When the value starts with
   * {@link StateVersioningContext#HTTP_REQUEST_LOOKUP_PREFIX}, the value will be resolved into the last HTTP state ID
   * before the given non-HTTP value.
   * 
   * @return The requested state ID.
   * @since 2.0
   */
  protected synchronized String getStateIdFromRequest() {
    String stateId = getRequestParam(STATE_ID_REQUEST_KEY);

    if (StringUtils.startsWith(stateId, HTTP_REQUEST_LOOKUP_PREFIX)) {

      // The suffix is expected to be non-HTTP state ID, but technically no problem when it's not:
      stateId = StringUtils.substringAfter(stateId, HTTP_REQUEST_LOOKUP_PREFIX);

      // Keeps track whether the current value of stateId has been passed during iterating:
      boolean targetFound = false;

      for (ListIterator<State> i = this.versionedStates.listIterator(this.versionedStates.size()); i.hasPrevious(); ) {
        if (targetFound) {
          State currentState = i.previous();
          if (currentState.getStateId().startsWith(HTTP_REQUEST_STATE_PREFIX)) {

            // In case of overlay, we need the second state before overlay state, because the state before overlay
            // is the one that opened overlay.

            boolean overlay = Boolean.toString(true).equals(getRequestParam(OverlayContext.OVERLAY_REQUEST_KEY));
            stateId = overlay && i.hasPrevious() ? i.previous().getStateId() : currentState.getStateId();
            break;
          }
        } else {
          targetFound = i.previous().getStateId().equals(stateId);
        }
      }

      // When no such HTTP state ID was found, default to the last state.
      if (stateId.startsWith(HTTP_REQUEST_LOOKUP_PREFIX)) {
        stateId = this.versionedStates.isEmpty() ? null : this.versionedStates.getLast().getStateId();
      }
    }

    return StringUtils.isBlank(stateId) ? null : stateId;
  }

  /**
   * Resolves the ID of the state to use as base state for handling the current request. Usually returns the state ID
   * from the request or the last known state ID. Note that it is not verified whether the ID is valid or whether we
   * have a non-expired state for it - it will be done in {@link #checkStateExpired()}.
   * 
   * @return The ID of an existing state to use for handling the current request.
   * @since 2.0
   */
  protected synchronized String resolvePreviousStateId() {
    String lastStateId = null;
    String regions = getRequestParam(UpdateRegionContext.UPDATE_REGIONS_KEY);
    String providedStateId = getRequestParam(STATE_ID_REQUEST_KEY);

    if (StringUtils.contains(regions, GLOBAL_CLIENT_NAVIGATION_REGION_ID)) {
      lastStateId = getStateIdFromRequest();
    } else if (StringUtils.isNotBlank(providedStateId)) {
      lastStateId = providedStateId;
    } else {
      lastStateId = this.newStateId; // The ID of the state that was returned the last time.
    }

    if (lastStateId == null && !this.versionedStates.isEmpty()) {
      LOG.warn("The last state ID was not provided in the request. Using the last versioned state instead.");
      lastStateId = this.versionedStates.getLast().getStateId();
    }

    return lastStateId;
  }

  protected synchronized String resolveCurrentStateId() {
    String newId = null;

    String regions = getRequestParam(UpdateRegionContext.UPDATE_REGIONS_KEY);
    String overlay = getRequestParam(OverlayContext.OVERLAY_REQUEST_KEY);

    // When regions is "araneaGlobalClientHistoryNavigationUpdateRegion" then no new state is created.
    if (StringUtils.contains(regions, GLOBAL_CLIENT_NAVIGATION_REGION_ID)) {
      newId = getStateIdFromRequest(); // Use the same value that also lastStateId uses.
    } else {
      newId = String.valueOf(System.currentTimeMillis()) + RandomStringUtils.randomAlphanumeric(15); // Generate a new state ID.

      // When not an AJAX request, prepend "HTTP" to the ID.
      if (regions == null && overlay == null) {
        newId = HTTP_REQUEST_STATE_PREFIX + newId;
      }
    }

    return newId;
  }

  public String getCurrentStateId() {
    return this.newStateId;
  }

  /**
   * This method will be called after requested state ID has been resolved to analyze the validness of the ID.
   * 
   * @since 2.0
   */
  protected synchronized void checkStateExpired() {
    if (this.lastStateId != null && !containsState(this.lastStateId)) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("State '" + this.lastStateId + "' has expired. Using the original state instead");
      }
      this.versionedStates.add(new State(originalState, this.lastStateId));
      this.newStateId = this.lastStateId; // To make it look like as if it is updating the current state like actions do.
    }
  }

  protected synchronized void storeOriginalState() {
    if (originalState == null && this.childWidget != null) {
      originalState = RelocatableUtil.serializeRelocatable((RelocatableWidget) this.childWidget);
    }
  }

  /**
   * Sets the response headers that disallow caching in general but still allow for using of browser history navigation
   * facilities (back/forward buttons) in most browsers (IE, FF, Opera). Safari seems to behaves badly in regard to the
   * RFC.
   * <p>
   * Sets the state ID as a header, which is needed when an AJAX request is coming from the client.
   */
  protected synchronized void writeHeaders() {
    Assert.notNull(this.newStateId, "The current state ID must not be null at this point.");

    HttpServletResponse response = ServletUtil.getResponse(getOutputData());
    response.setHeader("Pragma", null);
    response.setHeader("Cache-Control", null);
    response.setHeader(STATE_ID_RESPONSE_HEADER, this.newStateId);
  }

  /**
   * Chooses the appropriate state and restores it.
   */
  protected synchronized void restoreState() throws Exception {
    if (this.lastStateId != null) {
      byte[] state = null;
      for (State s : this.versionedStates) {
        if (s.getStateId().equals(this.lastStateId)) {
          state = (byte[]) s.getState();
          break;
        }
      }

      if (state == null) {
        state = originalState;
      }

      restoreState(state);
    }
  }

  protected synchronized void restoreState(byte[] serializedState) {
    Assert.notNull(serializedState, "serializedState");

    RelocatableWidget widget = (RelocatableWidget) SerializationUtils.deserialize(serializedState);
    widget._getRelocatable().overrideEnvironment(getChildWidgetEnvironment());
    this.childWidget = widget;
  }

  /**
   * Acquires current state and when server-side state versioning is used, stores that for later use. When this
   * {@link StandardStateVersioningFilterWidget} is configured to hold state on client, it will only register state
   * identifier and state checksums, returned state must be saved by someone else.
   * 
   * @return The current state with its identifier.
   */
  public synchronized State saveState() {
    if (this.stateSaved.get()) {
      return null;
    } else if (this.childWidget == null) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Child-widget is null for some unknown reason. Cannot serialize it. Returning...");
      }

      return null;
    }

    // Current state versioning work:
    byte[] serializedChild = RelocatableUtil.serializeRelocatable((RelocatableWidget) this.childWidget);
    State state = new State(serializedChild, this.newStateId);

    if (containsState(this.newStateId)) {
      for (ListIterator<State> i = this.versionedStates.listIterator(); i.hasNext(); ) {
        if (i.next().getStateId().equals(this.newStateId)) {
          i.set(state);
          break;
        }
      }
    } else {
      this.versionedStates.add(state);
    }
    this.stateSaved.set(true);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Serialized the current state as '" + this.newStateId + "'.");
    }

    // Work with versioned states:
    discardOldStatesOnLimitExceed();
    logStatus();
    notifyStatesUpdated();

    return state;
  }

  /**
   * When the base state is not the last and the new state is not versioned (is new), we discard all states after
   * {@link #lastStateId} to save some space, because browser back/forward button also discards these states.
   * <p>
   * This method should be called when last and new state IDs are resolved, the previous state is restored, but before
   * the new state ID is saved (e.g. in {@link #handleStateRestoration(boolean)}. This method will also notify listeners
   * about updated changes.
   * 
   * @since 2.0
   */
  protected synchronized void discardInaccessibleStates() {
    if (!this.versionedStates.isEmpty()) {
      if (!StringUtils.equals(this.lastStateId, this.versionedStates.getLast().getStateId())
          && !containsState(this.newStateId)) {

        int targetIndex = this.versionedStates.size();

        for (ListIterator<State> i = this.versionedStates.listIterator(); i.hasNext();) {
          if (i.next().getStateId().equals(this.lastStateId)) {
            targetIndex = i.nextIndex();
            break;
          }
        }

        this.versionedStates.subList(targetIndex, this.versionedStates.size()).clear();
      }
      notifyStatesUpdated();
    }
  }

  /**
   * When {@link #maxVersionedStates} is exceeded, this method will start removing the old states until the limit is not
   * exceeded anymore.
   * <p>
   * This method will NOT notify any listeners about changed states because, by default, this method is used by
   * {@link #saveState()}, which takes care of notifying, too.
   * 
   * @since 2.0
   */
  protected synchronized void discardOldStatesOnLimitExceed() {
    while (!this.versionedStates.isEmpty() && this.maxVersionedStates < this.versionedStates.size()) {
      this.versionedStates.removeFirst();
    }
  }

  protected synchronized void logStatus() {
    // When debugging is enabled, let's write some information about the stored states:
    if (LOG.isDebugEnabled()) {
      long totalSize = 0;
      for (State state : this.versionedStates) {
        totalSize += ((byte[]) state.getState()).length;
      }

      LOG.debug("Versioning contains " + this.versionedStates.size() + " state(s) with total size of "
          + formatSize(totalSize) + ".");
    }
  }

  /**
   * Does the given size formatting to represent it in bytes. For example, for input value 2048, returns "2MB". Supports
   * formatting units up to TB.
   * 
   * @param size The size value to format. 
   * @return The string of formatted input size together with unit information.
   * @since 2.0
   */
  protected static String formatSize(long size) {
    BigDecimal limit = BigDecimal.valueOf(1024);
    BigDecimal fSize = BigDecimal.valueOf(size);

    char[] units = { ' ', 'k', 'M', 'G', 'T' };
    int unit = 0;

    while (unit < units.length - 2 && fSize.compareTo(limit) > -1) {
      fSize = fSize.divide(limit);
      unit++;
    }

    // Do some rounding to make the value easier to understand. 
    fSize = fSize.setScale(2, BigDecimal.ROUND_HALF_UP);
    return fSize.toString() + units[unit] + 'B';
  }

  /**
   * Sets the maximum number of states that are tracked by this {@link StandardStateVersioningFilterWidget}. It should
   * be rather small number when states are stored on server-side and rather large number when states are stored on
   * client side.
   * 
   * @param maxVersionedStates number of states to keep track of
   */
  public synchronized void setMaxVersionedStates(int maxVersionedStates) {
    this.maxVersionedStates = maxVersionedStates;

    // When necessary, remove some states to meet the new limit:

    boolean changed = false;

    while (!this.versionedStates.isEmpty() && this.maxVersionedStates < this.versionedStates.size()) {
      this.versionedStates.removeFirst();
      changed = true;
    }

    if (changed) {
      notifyStatesUpdated();
    }
  }

  public void expire() {
    expiredDuringRequest = true;
    this.versionedStates.clear();
    notifyStatesUpdated();
  }

  // =============================================================
  // * Implementation for calling ClientNavigationAware components 
  // =============================================================

  protected synchronized void notifyAboutNavigation(boolean action) {
    if (action) {

      if (LOG.isDebugEnabled()) {
        LOG.debug("Detected navigation that just updated the current state.");
      }

      notifyCurrentStateUpdate();
    } else if (containsState(this.newStateId)) {

      if (LOG.isDebugEnabled()) {
        LOG.debug("Detected history-based navigation.");
      }

      notifyHistoryNavigation();
    } else {

      if (LOG.isDebugEnabled()) {
        LOG.debug("Detected usual navigation.");
      }

      notifyUsualNavigation();
    }
  }

  /**
   * Notifies {@link org.araneaframework.http.StateVersioningContext.ClientNavigationAware} components that a usual
   * navigation has occurred on client-side. "Usual" means that user clicked on link or button on the last returned
   * page. The user did not use browser history buttons.
   * 
   * @since 2.0
   */
  protected synchronized void notifyUsualNavigation() {
    new UsualNavigationNotifierMessage(this.lastStateId, this.newStateId).send(null, this.childWidget);
  }

  /**
   * Notifies {@link org.araneaframework.http.StateVersioningContext.ClientNavigationAware} components that an action
   * request has arrived and that will just update the last rendered state.
   * 
   * @since 2.0
   */
  protected synchronized void notifyCurrentStateUpdate() {
    new CurrentStateUpdateNotifierMessage(this.lastStateId).send(null, this.childWidget);
  }

  /**
   * Notifies {@link org.araneaframework.http.StateVersioningContext.ClientNavigationAware} components that a browser
   * history based navigation has occurred on client-side.
   * 
   * @since 2.0
   */
  protected synchronized void notifyHistoryNavigation() {
    new HistoryNavigationNotifierMessage(this.lastStateId, this.newStateId).send(null, this.childWidget);
  }

  /**
   * Notifies {@link org.araneaframework.http.StateVersioningContext.ClientNavigationAware} components that our states
   * have been updated (i.e. a state has expired, was removed and/or a new state has been added).
   * 
   * @since 2.0
   */
  protected synchronized void notifyStatesUpdated() {
    List<String> stateIds = new ArrayList<String>(this.versionedStates.size());
    for (State state : this.versionedStates) {
      stateIds.add(state.getStateId());
    }
    new StatesUpdatedMessage(stateIds).send(null, this.childWidget);
  }

  protected static class UsualNavigationNotifierMessage extends BroadcastMessage {

    private String baseStateId;

    private String newStateId;

    public UsualNavigationNotifierMessage(String baseStateId, String newStateId) {
      super(ClientNavigationAware.class);
      this.baseStateId = baseStateId;
      this.newStateId = newStateId;
    }

    @Override
    protected void execute(Component component) throws Exception {
      ClientNavigationAware comp = (ClientNavigationAware) component;
      comp.onUsualNavigation(this.baseStateId, this.newStateId);
    }
  }

  protected static class CurrentStateUpdateNotifierMessage extends BroadcastMessage {

    private String stateId;

    public CurrentStateUpdateNotifierMessage(String currentStateId) {
      super(ClientNavigationAware.class);
      this.stateId = currentStateId;
    }

    @Override
    protected void execute(Component component) throws Exception {
      ClientNavigationAware comp = (ClientNavigationAware) component;
      comp.onCurrentStateUpdate(this.stateId);
    }
  }

  protected static class HistoryNavigationNotifierMessage extends BroadcastMessage {

    private String sourceStateId;

    private String targetStateId;

    public HistoryNavigationNotifierMessage(String sourceStateId, String targetStateId) {
      super(ClientNavigationAware.class);
      this.sourceStateId = sourceStateId;
      this.targetStateId = targetStateId;
    }

    @Override
    protected void execute(Component component) throws Exception {
      ClientNavigationAware comp = (ClientNavigationAware) component;
      comp.onHistoryNavigation(this.sourceStateId, this.targetStateId);
    }
  }

  protected static class StatesUpdatedMessage extends BroadcastMessage {

    private List<String> stateIds;

    public StatesUpdatedMessage(List<String> stateIds) {
      super(ClientNavigationAware.class);
      this.stateIds = stateIds;
    }

    @Override
    protected void execute(Component component) throws Exception {
      ClientNavigationAware comp = (ClientNavigationAware) component;
      comp.afterStatesChange(this.stateIds);
    }
  }

}
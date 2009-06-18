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

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */

var AraneaStore = Class.create({

  initialize: function() {
    this._objects = [];
  },

  add: function(object) {
    if (object) {
      this._objects.push(object);
    }
  },

  clear: function() {
    this._objects.clear();
  },

  length: function() {
    return this._objects.length;
  },

  getContents: function() {
    return this._objects;
  },

  forEach: function(f) {
    for (var i = 0; i < this._objects.length; i++) {
      f(this._objects[i]);
    }
  }

});

var AraneaEventStore = Class.create(AraneaStore, {

  processEvent: function(event) {
    if (Object.isFunction(event)) {
      event();
    } else {
      event;
    }
  },

  execute: function() {
    this.forEach(this.processEvent);
    this.clear();
  }

});

/**
 *  Exactly one AraneaPage object is present on each page served by Aranea and
 *  contains common functionality for setting page related variables, events and
 *  functions.
 */
var AraneaPage = Class.create({

  initialize: function() {
	this.logger = this.dummyLogger;
	if (!(window.console && window.console.log)) {
      this.safariLogger = this.dummyLogger;
    }
    if (!(window.console && window.console.debug)) {
      this.firebugLogger = this.safariLogger;
    }
  },

  /* URL of aranea dispatcher servlet serving current page.
   * This is by default set by Aranea JSP ui:body tag. */
  servletURL: null,

  getServletURL: function() {
    return this.servletURL;
  },

  setServletURL: function(url) {
    this.servletURL = new String(url);
  },

  /* If servlet URL is not enough for some purposes, encoding function should be overwritten. */
  encodeURL: function(url) {
    return url;
  },

  // Enables/disables the effect of "focusedFormElementName" and "focusableElements":
  autofocus: true,

  // To monitor focused element and to make it focused after content updating by Ajax. (Since 1.2)
  focusedFormElementName: null,

  // The elements that will be observed for tracking focus:
  focusableElements: "input, select, textarea, button",

  /* Indicates whether the page is completely loaded or not. Page is considered to
   * be loaded when all system onload events have completed execution. */
  loaded: false,

  isLoaded: function() {
    return this.loaded;
  },

  setLoaded: function(b) {
    if (typeof b == 'boolean') {
      this.loaded = b;
    }
  },

  /* Logger that outputs javascript logging messages. */
  dummyLogger: Object.extend({}, {
    trace: Prototype.emptyFunction,
    debug: Prototype.emptyFunction,
    info: Prototype.emptyFunction,
    warn: Prototype.emptyFunction,
    error: Prototype.emptyFunction,
    fatal: Prototype.emptyFunction
  }),

  safariLogger: Object.extend({}, {
    trace: function(s) { window.console.log(s); },
    debug: function(s) { window.console.log(s); },
    info:  function(s) { window.console.log(s); },
    warn:  function(s) { window.console.log(s); },
    error: function(s) { window.console.log(s); },
    fatal: function(s) { window.console.log(s); }
  }),

  firebugLogger: Object.extend({}, {
    trace: function(s) { window.console.debug(s); },
    debug: function(s) { window.console.debug(s); },
    info:  function(s) { window.console.info(s); },
    warn:  function(s) { window.console.warn(s); },
    error: function(s) { window.console.error(s); },
    fatal: function(s) { window.console.error(s); }
  }),

  logger: this.dummyLogger,

  setDummyLogger: function() {
    this.setLogger(this.dummyLogger);
  },

  setDefaultLogger: function() {
    if (log4javascript && log4javascript.getDefaultLogger) {
      this.setLogger(log4javascript.getDefaultLogger());
    }
  },

  setFirebugLogger: function() {
    this.setLogger(this.firebugLogger);
  },

  setLogger: function(theLogger) {
    this.logger = theLogger;
  },

  getLogger: function() {
    return this.logger;
  },

  /* locale - should be used only for server-side reported locale */
  locale: new AraneaLocale('', ''),

  getLocale: function() {
    return this.locale;
  },

  setLocale: function(loc) {
    this.locale = loc;
  },

  /* Indicates whether some form on page is (being) submitted already
   * by traditional HTTP request. */
  submitted: false,

  isSubmitted: function() {
    return this.submitted;
  },

  setSubmitted: function() {
    this.submitted = true;
  },

  systemForm: null,

  /**
   * @return systemForm currently active in this AraneaPage
   * @since 1.1
   */
  getSystemForm: function() {
    return this.systemForm;
  },

  /**
    * Sets the active system form in this AraneaPage.
    * @since 1.1 */
  setSystemForm: function(_systemForm) {
    this.debug('AraneaPage: Setting systemform to: ' + Object.inspect(_systemForm));
    this.systemForm = _systemForm;
  },

  /** @since 1.1 */
  setSystemFormEncoding: function(encoding) {
    this.addSystemLoadEvent(function() {
      this.systemForm.enctype = encoding;
      this.systemForm.encoding = encoding; // IE
    }.bind(this));
  },

  readAttribute: function(element, attribute) {
    return String.interpret(element.readAttribute(attribute));
  },

  /**
   * Returns the ID of a component who should receive events generated by DOM element.
   * @since 1.1
   */
  getEventTarget: function(element) {
    return this.readAttribute(element, 'arn-trgtwdgt');
  },

  /**
   * Returns event id that should be sent to server when event(element) is called.
   * @since 1.1
   */
  getEventId: function(element) {
    return this.readAttribute(element, 'arn-evntId');
  },

  /** Returns event parameter that should be sent to server when event(element) is called.
    * @since 1.1 */
  getEventParam: function(element) {
    return this.readAttribute(element, 'arn-evntPar');
  },

  /** Returns update regions that should be sent to server when event(element) is called.
    * @since 1.1 */
  getEventUpdateRegions: function(element) {
    return this.readAttribute(element, 'arn-updrgns');
  },

  /** Returns closure that should be evaluated when event(element) is called and
    * needs to decide whether server-side event invocation is needed.
    * @since 1.1 */
  getEventPreCondition: function(element) {
    return this.readAttribute(element, 'arn-evntCond');
  },

  /** Timer that executes keepalive calls, if any. */
  keepAliveTimers: new AraneaStore(),

  /** Variables holding different (un)load events that should be executed when page loads -- on body (un)load or alike. */
  systemLoadEvents: new AraneaEventStore(),
  clientLoadEvents: new AraneaEventStore(),
  systemUnLoadEvents: new AraneaEventStore(),

  submitCallbacks: new Object(),

  addSystemLoadEvent: function(event) {
    this.systemLoadEvents.add(event);
  },

  addClientLoadEvent: function(event) {
    this.clientLoadEvents.add(event);
  },

  addSystemUnLoadEvent: function(event) {
    this.systemUnLoadEvents.add(event);
  },

  onload: function() {
    this.logger.trace('System (on)load events executing (' + this.systemLoadEvents.length() + ').\n');
    this.systemLoadEvents.execute();
    this.setLoaded(true);
    this.logger.trace('System (on)load events are executed.\n');
    this.logger.trace('Client load events executing (' + this.clientLoadEvents.length() + ').\n');
    this.clientLoadEvents.execute();
    this.logger.trace('Client load events are executed.\n');
  },

  onunload: function() {
    this.systemUnLoadEvents.execute();
  },

  // General callbacks executed before each form submit.
  /** Adds callback executed before next form submit. */
  addSubmitCallback: function(callback) {
    this.addSystemFormSubmitCallback('callbacks', callback);
  },

  // General callbacks executed before each submit of the specified system form.
  /** Add callback executed before form with given id is submitted next time. */
  addSystemFormSubmitCallback: function(systemFormId, callback) {
    if (!this.submitCallbacks[systemFormId]) {
      this.submitCallbacks[systemFormId] = new AraneaEventStore();
    }
    this.submitCallbacks[systemFormId].add(callback);
  },

  /**
   * Executes all callbacks that should run before submitting the form with given id.
   * Executed callbacks are removed.
   */
  executeCallbacks: function(systemFormId) {
    this.logger.trace('Request for submit callback execution was received.\n');
    if (this.submitCallbacks['callbacks']) {
      this.logger.trace('General submit callbacks executing.\n');
      this.submitCallbacks['callbacks'].execute();
      this.logger.trace('General submit callbacks are executed.\n');
    }

    if (this.submitCallbacks[systemFormId]) {
      this.logger.trace('Submit callbacks executing.\n');
      this.submitCallbacks[systemFormId].execute();
      this.logger.trace('Submit callbacks are executed.\n');
    }
  },

  /**
   * Chooses appropriate submitting method and submittable form given the HTML element
   * that initiated the submit request. Applies the appropriate paramater values
   * and submits the systemForm which owns the element. */
  event: function(element) {
    if (this.isSubmitted() || !this.isLoaded()) {
      return false;
    }
    
    this.setLoaded(false);
    
    this.setLoaded(false);

    var systemForm = $(element).up('form#overlaySystemForm, form#systemForm');
    var preCondition = this.getEventPreCondition(element);

    if (preCondition) {
      var f = new Function('element', preCondition);
      if (!f(element)) {
    	this.setLoaded(true);
        f = null;
        return false;
      }
      preCondition = null;
    }

    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    var result = this.findSubmitter(element, systemForm).event(element);

    // To avoid memory leaks:
    systemForm = null;
    element = null;

    return result;
  },

  /**
   * This function can be overwritten to support additional submit methods.
   * It is called by event() to determine the appropriate form submitter.
   */
  findSubmitter: function(element, systemForm) {
    if (this.getEventUpdateRegions(element).length > 0) {
      return new DefaultAraneaAJAXSubmitter(systemForm);
    } else if (systemForm.hasClassName('aranea-overlay')) {
      return new DefaultAraneaOverlaySubmitter(systemForm);
    } else {
      return new DefaultAraneaSubmitter(systemForm);
    }
  },

  /** Another submit function, takes all params that are possible to
    * use with Aranea JSP currently.
    * @param systemForm form that will be submitted
    * @param eventId event identifier sent to the server
    * @param eventTarget event target identifier (widget id)
    * @param eventParam event parameter
    * @param eventPrecondition closure, submit is only performed when its evaluation returns true
    * @param eventUpdateRegions identifiers for regions that should be regenerated on server-side */
  // TODO: get rid of duplicated logic from: submit() and findSubmitter()
  event_6: function(systemForm, eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions) {
    if (this.isSubmitted() || !this.isLoaded()) {
      return false;
    }

    this.setLoaded(false);

    if (eventPrecondition) {
      var f = new Function(eventPrecondition);
      if (!f()) {
        f = null;
        this.setLoaded(true);
        return false;
      }
    }

    if (!systemForm) {
      systemForm = AraneaPage.findSystemForm();
    }

    if (Object.isFunction(eventParam)) {
      eventParam = eventParam();
    }

    if (Object.isFunction(eventPrecondition)) {
      eventPrecondition = eventPrecondition();
    }

    if (Object.isFunction(eventUpdateRegions)) {
      eventUpdateRegions = eventUpdateRegions();
    }

    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    if (eventUpdateRegions != null && eventUpdateRegions.length > 0) {
      return new DefaultAraneaAJAXSubmitter().event_5(systemForm, eventId, eventTarget, eventParam, eventUpdateRegions);
    } else if (systemForm.hasClassName('aranea-overlay')) {
      return new DefaultAraneaOverlaySubmitter().event_7(systemForm, eventId, eventTarget, eventParam);
    } else {
      return new DefaultAraneaSubmitter().event_4(systemForm, eventId, eventTarget, eventParam);
    }
  },

  /**
   * Returns URL that can be used to invoke full HTTP request with some predefined request parameters.
   * @param topServiceId server-side top service identifier
   * @param threadServiceId server-side thread service identifier
   * @param araTransactionId transaction id expected by the server
   * @param extraParams more parameters, i.e "p1=v1&p2=v2"
   */
  getSubmitURL: function(topServiceId, threadServiceId, araTransactionId, extraParams) {
    var url = new Array(11);
    url.push(this.encodeURL(this.getServletURL()));
    url.push('?araTransactionId=');
    url.push(araTransactionId);

    if (topServiceId) {
      url.push('&araTopServiceId=');
      url.push(topServiceId);
    }
    if (threadServiceId) {
      url.push('&araThreadServiceId=');
      url.push(threadServiceId);
    }

    if (_ap.getSystemForm().araClientStateId) {
      url.push('&araClientStateId=');
      url.push(_ap.getSystemForm().araClientStateId.value);
    }

    if (extraParams) {
      url.push('&');
      url.push(extraParams);
    }

    return url.join('');
  },

  /**
   * Returns URL that can be used to make server-side action-invoking
   * XMLHttpRequest with some predefined request parameters.
   * @param systemForm form containing information about top service and thread service identifiers
   * @param actionId action identifier sent to the server
   * @param actionTarget action target identifier (widget id)
   * @param actionParam action parameter
   * @param sync whether this action is synchronized on server-side or not (default is synchronized)
   * @param extraParams more parameters, i.e "p1=v1&p2=v2"
   */
  getActionSubmitURL: function(systemForm, actionId, actionTarget, actionParam, sync, extraParams) {
    var url = new Array(9);

    url.push(this.getSubmitURL(systemForm.araTopServiceId.value,
        systemForm.araThreadServiceId.value, 'override', extraParams));

    url.push('&araServiceActionPath=');
    url.push(actionTarget);

    if (actionId) {
      url.push('&araServiceActionHandler=');
      url.push(actionId);
    }
    if (actionParam) {
      url.push('&araServiceActionParameter=');
      url.push(actionParam);
    }
    if (!sync) {
      url.push('&araSync=false');
    }

    return url.join('');
  },

  /**
   * Invokes server-side action listener by performing XMLHttpRequest with correct parameters.
   * @param element unused currently, should be set to DOM element that triggers action invocation
   * @param actionId action identifier sent to the server
   * @param actionTarget action target identifier (widget id)
   * @param actionParam action parameter
   * @param actionCallback callback executed when action response arrives
   * @param options XMLHttpRequest options, see: http://prototypejs.org/api/ajax/options
   * @param sync whether this action is synchronized on server-side or not (default is synchronized)
   * @param extraParams more parameters, i.e "p1=v1&p2=v2"
   */
  action: function(element, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams) {
    return this.action_6(this.getSystemForm(), actionId, actionTarget,
        actionParam, actionCallback, options, sync, extraParams);
  },

  /**
   * Invokes server-side action listener by performing XMLHttpRequest with correct parameters.
   * @param systemForm form that triggers the action request
   * @param actionId action identifier sent to the server
   * @param actionTarget action target identifier (widget id)
   * @param actionParam action parameter
   * @param actionCallback callback executed when action response arrives
   * @param options XMLHttpRequest options, see: http://prototypejs.org/api/ajax/options
   * @param sync whether this action is synchronized on server-side or not (default is synchronized)
   * @param extraParams more parameters, i.e "p1=v1&p2=v2"
   */
  action_6: function(systemForm, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams) {
    options = Object.extend({
      method: 'post',
      onComplete: actionCallback,
      onException: this.handleRequestException
    }, options);
    var url = this.getActionSubmitURL(systemForm, actionId, actionTarget, actionParam, sync, extraParams);
    return new Ajax.Request(url, options);
  },

  debug: function(message) {
    this.getLogger().debug(message);
  },

  /**
   * Provides preferred way of overriding AraneaPage object functions.
   * @param functionName name of AraneaPage function that should be overridden.
   * @param f replacement function
   * 
   * @depracated Use Object.extend(araneaPage(), { funcName: function { ... } }) instead.
   */
  override: function(functionName, f) {
    this.logger.warn('AraneaPage.override is deprecated; use ' +
    		'Object.extend([araneaPage()|_ap], { funcName: function { ... } }) instead!');
    this.logger.info('AraneaPage.' + functionName + ' was overriden.');
    this[functionName] = f;
  },

  /**
   * Adds keepalive function f that is executed periodically after time
   * milliseconds has passed
   */
  addKeepAlive: function(f, time) {
    this.keepAliveTimers.add(setInterval(f, time));
  },

  /**
   * Clears/removes all registered keepalive functions.
   */
  clearKeepAlives: function() {
    this.keepAliveTimers.forEach(function(timer) {clearInterval(timer);});
  },

  /**
   * Whether this application should use ajax form validation.
   */
  ajaxValidation: false,

  /**
   * Returns the flag that determines whether background validation is used by
   * for all forms (FormWidgets) in the application.
   */
  getBackgroundValidation: function() {
    return this.ajaxValidation;
  },

  /**
   * Sets the background form validation flag on client side. Note that
   * server-side must have identical setting for these settings to have effect.
   */
  setBackgroundValidation: function(useAjax) {
    if (typeof useAjax == 'boolean') {
      this.ajaxValidation = useAjax;
    } else {
      throw("AraneaPage.setBackgroundValidation() accepts only Boolean parameters.");
    }
  }
});

// =========================================================================================================================================

Object.extend(AraneaPage, {

  /**
   * Returns a default keepalive function -- to make periodical requests to
   * expiring thread or top level services.
   */
  getDefaultKeepAlive: function(topServiceId, threadServiceId, keepAliveKey) {
    return function() {
        var url = _ap.getSubmitURL(topServiceId, threadServiceId, 'override');
        url += '&' + keepAliveKey + '=true';

        _ap.debug('Sending async service keepalive request to URL "' + url +'"');
        var keepAlive = new Ajax.Request(url, { method: 'post' });
    };
  },

  /**
   * Searches for widget marker around the given element. If found, returns the
   * marker DOM element, else returns null.
   */
  findWidgetMarker: function(element) {
    var ancestors = $(element).ancestors();
    var ancestorCount =  ancestors.length;

    for (var i = 0; i < ancestorCount; i++) {
      var ancestor = ancestors[i];

      var marker = $w(ancestor.className).find(function(name) {
        return (name == 'widgetMarker');
      });

      if (marker) {
        marker = null;
        return ancestor;
      }

      ancestor = null;
    }

    ancestors = null;

    return null;
  },

  /**
   * Random request ID generator. Sent only with XMLHttpRequests which apply to
   * certain update regions. Currently its only purpose is easier debugging
   * (identifying requests).
   */
  getRandomRequestId: function() {
    return Math.round(100000 * Math.random());
  },

  /**
   * @since 1.1
   */
  getFileImportString: function(filename) {
    return _ap.getServletURL() + '/fileimporter/' + filename;
  },

  /**
   * Page initialization function, should be called upon page load.
   */
  init: function() {
    _ap.debug("Executing AraneaPage.init()");
    _ap.addSystemLoadEvent(Aranea.Behaviour.apply);

    // Monitor the currently focused element for Ajax page update (since 1.2)
    if (_ap.autofocus) {
      $$(_ap.focusableElements).each(function (formElement) {
          formElement.observe("focus", function(event) {
            _ap.focusedFormElementName = event.element().name;
            _ap.debug("Focused: " + _ap.focusedFormElementName);
          });
      });
    }
    if (AraneaPage.ajaxUploadInit) {
      AraneaPage.ajaxUploadInit();
    }
  },

  /**
   * Page deinitialization function, should be called upon page unload.
   */
  deinit: function() {
    _ap.debug("Executing AraneaPage.deinit()");
    _ap = null;
  },

  /**
   * Searches for system form in HTML page and registers it in the current
   * AraneaPage object as active systemForm.
   * @return The active system form that was found. 
   * @since 1.1
   */
  findSystemForm: function() {
    _ap.debug("Executing AraneaPage.findSystemForm()");
    var forms = $$('form.aranea-overlay[arn-systemForm="true"]');
    if (!forms || forms.length == 0) {
      forms = $$('form[arn-systemForm="true"]');
    }
    _ap.setSystemForm(forms.first());
    forms = null;
    return _ap.getSystemForm();
  },

  /**
   * RSH initialization for state versioning. Has effect only when
   * "aranea-rsh.js" is also included in the page.
   */
  initRSHURL: function() {
    _ap.debug("Executing AraneaPage.initRSHURL()");
    if (window.dhtmlHistory && _ap.getSystemForm().araClientStateId) {

      window.dhtmlHistory.firstLoad = true;
      window.dhtmlHistory.ignoreLocationChange = true;

      var stateId = _ap.getSystemForm().araClientStateId.value;

      // If we generate hashes to HTTP requests, URL changes cause the browser
      // to never use local history data, as the hash added later is not
      // accessible from its memory cache. Thus we just keep the URL intact
      // for these cases, so that browsers own history mechanisms can take over.
      if (stateId.startsWith('HTTP')) {
        window.location.hash = stateId;
        window.dhtmlHistory.add(stateId, null);
      }
    }
  },

  /* Private fields */
  loadingMessagePositionHack: false,
  receivedRegionCounters: null,
  regionHandlers: new Hash(),

  /* Public fields */

  /**
   * @since 1.1
   */
  loadingMessageContent: 'Loading...',

  /**
   * @since 1.1
   */
  loadingMessageId: 'aranea-loading-message',

  /**
   * @since 1.2
   */
  reloadOnNoDocumentRegions: true,

  /**
   * Add a handler that is invoked for custom data region in updateregions AJAX
   * request. <code>process</code> function will be invoked on the handler
   * during processing the response. Data specific to this handler will be
   * passed as the first parameter to that function (<code>String</code>).
   * @param key update region type identifier
   * @param handler callback that should process region content
   *
   * @since 1.1
   */
  addRegionHandler: function(key, handler) {
    this.regionHandlers[key] = handler;
  },

  /**
   * Process response of an updateregions AJAX request. Should be called only
   * on successful response. Invokes registered region handlers.
   *
   * @since 1.1
   */
  processResponse: function(responseText) {
    var text = new Text(responseText);
    var responseId = text.readLine();
    this.receivedRegionCounters = new Hash();
    while (!text.isEmpty()) {
      var key = text.readLine();
      var length = text.readLine();
      var content = text.readCharacters(length);

      if (this.receivedRegionCounters[key]) {
        this.receivedRegionCounters[key]++;
      } else {
        this.receivedRegionCounters[key] = 1;
      }

      if (this.regionHandlers[key]) {
        _ap.debug('Region type: "' + key + '" (' + length + ' characters)');
        this.regionHandlers[key].process(content);
      } else {
        throw('Region type: "' + key + '" is unknown!');
      }
    }

    var hasRegions = this.receivedRegionCounters['document']
                  || this.receivedRegionCounters['reload']
                  || this.receivedRegionCounters['popups']
                  || this.receivedRegionCounters['aranea-formvalidation'];
    if (this.reloadOnNoDocumentRegions && !hasRegions) {
      _ap.debug('No document regions were received, forcing a reload of the page');
      if (this.regionHandlers['reload']) {
        this.regionHandlers['reload'].process();
      } else {
        _ap.logger.error('No handler is registered for "reload" region, unable to force page reload!');
      }
    }
  },

  /**
   * Exception handler that is invoked on Ajax.Request errors.
   *
   * @since 1.1
   */
  handleRequestException: function(request, exception) {
    _ap.setLoaded(true);
    throw exception;
  },

  /**
   * Create or show loading message at the top corner of the document. Called
   * before initiating an updateregions Ajax.Request.
   *
   * @since 1.1
   */
  showLoadingMessage: function() {
    var element = $(this.loadingMessageId);
    if (!element) {
      element = this.buildLoadingMessage();
      if (!element) return;
      document.body.appendChild(element);
      element = $(element);
    }
    this.positionLoadingMessage(element);
    element.show();
    element = null;
  },

  /**
   * Hide loading message. Called after the completion of updateregions
   * Ajax.Request.
   *
   * @since 1.1
   */
  hideLoadingMessage: function() {
    var element = $(this.loadingMessageId);
    if (element) {
      element.hide();
    }
    element = null;
  },

  /**
   * Build loading message. Called when an existing message element is not
   * found.
   *
   * @since 1.1
   */
  buildLoadingMessage: function() {
    return new Element('div', { id: this.loadingMessageId }).update(this.loadingMessageContent);
  },

  /**
   * Perform positioning of loading message (if needed in addition to CSS).
   * Called before making the message element visible. This implementation
   * provides workaround for IE 6, which doesn't support
   * <code>position: fixed</code> CSS attribute; the element is manually
   * positioned at the top of the document. If you don't need this, overwrite
   * this with an empty function:
   * <code>Object.extend(araneaPage(), { positionLoadingMessage: Prototype.emptyFunction });</code>
   *
   * @since 1.1
   */
  positionLoadingMessage: function(element) {
    if (this.loadingMessagePositionHack || element.offsetTop) {
      this.loadingMessagePositionHack = true;
      $(element).setStyle({
          position:  'absolute',
          top: document.documentElement.scrollTop + 'px'
      });
    }
  }

});

/**
 * A common callback for submitters. The callback handles common data manipulation and validation.
 * This callback should be used to add some custom features to submit data or submit response.
 * @since 1.2.2
 */
AraneaPage.SubmitCallback = {
  /**
   * The only method that element-submitters should call. It takes the type of request, the form
   * containing the element to be submitted, and the function that does the submit work.
   */
  doRequest: function(type, form, element, eventFn) {
    if (!element) {
      return false;
    }

    var widgetId = _ap.getEventTarget(element);
    var eventId = _ap.getEventId(element);
    var eventParam = _ap.getEventParam(element);
    var eventUpdateRgns = _ap.getEventUpdateRegions(element);

    var result;
    if (eventFn) {
      result = eventFn(form, eventId, widgetId, eventParam, eventUpdateRgns);
    }

    return this.getRequestResult(type, element, result);
  },

  /**
   * This method is called to return the result of element-submit. Here is a nice place to implement
   * custom features depending on the element or request type. Feel free to override.
   */
   getRequestResult: function(type, element, result) {
    // If element is checkbox or radio then we return the oppposite value. When a request is
    // successful and false is returned, it would block checkbox or radio to be selected. Therefore,
    // we need to flip the value.
    var type = element.type == null ? null : element.type.toLowerCase();
    return type == 'checkbox' || type == 'radio' ? !result : result;
  },

  /**
   * The method that is called by submitters to store submit data in the form.
   */
  prepare: function(type, form, widgetId, eventId, eventParam) {
    var data = {
      type: String.interpret(type),
      form: form,
      widgetId: String.interpret(widgetId),
      eventId: String.interpret(eventId),
      eventParam: String.interpret(eventParam)
    };
    return this.processData(data);
  },

  /**
   * Processes the submit data. It calls following methods of this object:
   * 1. processSubmitData - to optionally modify the submit data; 
   * 2. storeSubmitData - to store the submit data in the form (if submit is allowed).
   * 3. Adds isSubmitAllowed, beforeSubmit, afterSubmit callbacks to data.
   */
  processData: function(data) {
    this.processSubmitData(data);
    this.storeSubmitData(data);
    data.isSubmitAllowed = this.isSubmitAllowed.curry(data);
    data.beforeSubmit = this.beforeSubmit.curry(data);
    data.afterSubmit = this.afterSubmit.curry(data);
    return data;
  },

  /**
   * A callback to optionally modify submit data.
   */
  processSubmitData: function(data) {},

  /**
   * A callback to store submit data in the form.
   */
  storeSubmitData: function(data) {
    if (data.form) {
      data.form.araWidgetEventPath.value = data.widgetId;
      data.form.araWidgetEventHandler.value = data.eventId;
      data.form.araWidgetEventParameter.value = data.eventParam;
    }
  },

  /**
   * A callback that is checked to enable or disable submit.
   */
  isSubmitAllowed: function(data) {
    return true;
  },

  /**
   * A callback that is called before each submit (no matter whether it is AJAX or not).
   * This method includes default behaviour.
   */
  beforeSubmit: function(data) {
    if (data.type != DefaultAraneaSubmitter.prototype.TYPE) {
      // copy the content of rich editors to corresponding HTML textinputs/textareas
      if (window.tinyMCE) {
        window.tinyMCE.triggerSave();
      }
    }
    if (data.type == DefaultAraneaAJAXSubmitter.prototype.TYPE) {
      _ap.debug('GOOD: showLoadingMessage');
      AraneaPage.showLoadingMessage();
    }
  },

  /**
  * A callback that is called after each submit (no matter whether it is AJAX or not).
  * This method includes default behaviour.
  */
  afterSubmit: function(data) {
    if (data.type == DefaultAraneaAJAXSubmitter.prototype.TYPE) {
      AraneaPage.hideLoadingMessage();
    }
  }
};

// Here are three submitter classes for the standard HTTP submit, AJAX update
// region submit, and AJAX overlay submit.

/**
 * The standard HTTP submitter. Whether it's POST or GET depends on the
 * "method" attribute of the "form" element. (The default is GET submit.)
 */
var DefaultAraneaSubmitter = Class.create({

  TYPE: 'SUBMIT',

  initialize: function(form) {
    this.systemForm = form;
  },

  event: function(element) {
    return AraneaPage.SubmitCallback.doRequest(this.TYPE, this.systemForm, element, this.event_4.bind(this));
  },

  event_4: function(systemForm, eventId, widgetId, eventParam) {
    var callback = AraneaPage.SubmitCallback.prepare(
        this.TYPE, systemForm, widgetId, eventId, eventParam);

    if (callback.isSubmitAllowed()) {
      callback.beforeSubmit();
      _ap.setSubmitted();
      systemForm.submit();
      callback.afterSubmit();
    }

    callback = null;
    return false;
  }
});

/**
 * This class extends the default submitter, and overrides event() to initiate 
 * an AJAX request and to process result specifically for the overlay mode.
 * It expects that aranea-modalbox.js is successfully loaded.
 */
var DefaultAraneaOverlaySubmitter = Class.create(DefaultAraneaSubmitter, {

  TYPE: 'OVERLAY',

  event: function(element) {
    return AraneaPage.SubmitCallback.doRequest(this.TYPE, this.systemForm, element, this.event_7.bind(this));
  },

  event_7: function(systemForm, eventId, widgetId, eventParam) {
    var callback = AraneaPage.SubmitCallback.prepare(
        this.TYPE, systemForm, widgetId, eventId, eventParam);

    if (callback.isSubmitAllowed()) {
      callback.beforeSubmit();

      Aranea.ModalBox.update({ params: systemForm.serialize(true) });

      callback.afterSubmit();
    }

    callback = null;
    return false;
  }
});

var DefaultAraneaAJAXSubmitter = Class.create(DefaultAraneaSubmitter, {

  TYPE: 'AJAX',

  event: function(element) {
    return AraneaPage.SubmitCallback.doRequest(this.TYPE, this.systemForm, element, this.event_5.bind(this));
  },

  event_5: function(systemForm, eventId, widgetId, eventParam, updateRegions) {
    this.callback = AraneaPage.SubmitCallback.prepare(
            this.TYPE, systemForm, widgetId, eventId, eventParam);

    if (this.callback.isSubmitAllowed()) {
      this.callback.beforeSubmit();

      var ajaxRequestId = AraneaPage.getRandomRequestId().toString();
      var neededAraClientStateId = systemForm.araClientStateId ? systemForm.araClientStateId.value : null;
      var neededAraTransactionId = 'override';

      if (updateRegions == 'araneaGlobalClientHistoryNavigationUpdateRegion') {
        neededAraClientStateId = window.dhtmlHistoryListenerRequestedState;
        window.dhtmlHistoryListenerRequestedState = null;
        neededAraTransactionId = 'inconsistent';
      }

      $(systemForm.id).request({
        parameters: this.getAjaxParameters(neededAraTransactionId, ajaxRequestId,
            updateRegions, neededAraClientStateId),
        onSuccess: this.onAjaxSuccess.curry(ajaxRequestId).bind(this),
        onComplete: this.onAjaxComplete.bind(this),
        onFailure: this.onAjaxFailure.bind(this),
        onException: this.onAjaxException.bind(this)
      });
    }

    return false;
  },

  afterRequest: function() {
    if (this.callback) {
      this.callback.afterSubmit();
      this.callback = null;
    }
  },

  getAjaxParameters: function(neededAraTransactionId, ajaxRequestId,
      updateRegions, neededAraClientStateId) {
    return {
        araTransactionId: neededAraTransactionId,
        ajaxRequestId: ajaxRequestId,
        updateRegions: updateRegions,
        araClientStateId: neededAraClientStateId
    };
  },

  onAjaxSuccess: function(ajaxRequestId, transport) {
    // This gets executed twice, it may be that during update region processing
    // something already needs current stateId presence, e.g. the reloading region
    // handler. As it's likely that whole system form will be replaced completely
    // when document region is updated, this must be repeated in onComplete.
    DefaultAraneaAJAXSubmitter.ResponseHeaderProcessor(transport);

    var logmsg = '';

    if (transport.responseText.substr(0, ajaxRequestId.length + 1) == ajaxRequestId + '\n') {
      logmsg += 'Partial rendering: received successful response (';
      logmsg += transport.responseText.length + ' characters): ';
      logmsg += transport.status + ' ' + transport.statusText;
      _ap.debug(logmsg);
      AraneaPage.processResponse(transport.responseText);
      AraneaPage.init();
    } else {
      logmsg += 'Partial rendering: received erroneous response (';
      logmsg += transport.responseText.length + ' characters): ';
      logmsg += transport.status + ' ' + transport.statusText;
      _ap.debug(logmsg);
      // Doesn't work quite well for javascript and CSS, but fine for plain HTML
      document.write(transport.responseText);
      document.close();
    }
  },

  onAjaxComplete: function(transport) {

    // because prototype's Element.update|replace delay execution of scripts,
    // immediate execution of onload() is not guaranteed to be correct
    var f = function() {
      _ap.addSystemLoadEvent(function() { DefaultAraneaAJAXSubmitter.ResponseHeaderProcessor(transport); });
      _ap.onload();

      if (Aranea.ModalBox) {
        Aranea.ModalBox.afterUpdateRegionResponseProcessing(_ap.systemForm);
      }

      // Set the previously focused form control focused again (since 1.2)
      if (_ap.autofocus && _ap.focusedFormElementName) {
        var formElem = $$('[name="' + _ap.focusedFormElementName + '"]').first();
        _ap.focusedFormElementName = null;
        if (formElem) {
          try {
            formElem.focus();
          } catch (e) {}
          formElem = null;
        }
      }

      this.afterRequest();
    }.bind(this);

    // force the delay here
    setTimeout(f, DefaultAraneaAJAXSubmitter.contentUpdateWaitDelay);
  },

  onAjaxFailure: function(transport) {
    _ap.debug(['Partial rendering: received erroneous response (',
               transport.responseText.length, ' characters): ',
               transport.status, ' ', transport.statusText].join(''));

    // Doesn't work quite well for javascript and CSS, but fine for plain HTML
    document.write(transport.responseText);
    document.close();
    this.afterRequest();
  },

  onAjaxException: function(request, exception) {
    AraneaPage.handleRequestException(request, exception);
    this.afterRequest();
  }

});

Object.extend(DefaultAraneaAJAXSubmitter, {

  /**
   * The delay after which Ajax.Request onComplete expects all the DOM updates
   * to have taken place, in milliseconds.
   * @since 1.1
   */
  contentUpdateWaitDelay: 30,

  /**
   * @since 1.2
   */
  ResponseHeaderProcessor: function(transport) {
    var stateVersion = null;
    try {
      stateVersion = transport.getResponseHeader('Aranea-Application-StateVersion');
    } catch (e) {
      stateVersion = null;
    }

    if (stateVersion != null && stateVersion.length > 0) {
      var sForm = _ap.getSystemForm();
      if (sForm.araClientStateId) {
        sForm.araClientStateId.value = stateVersion;
      }
      if (window.dhtmlHistory) {
        window.dhtmlHistory.add(stateVersion, true);
      }
    }
  }

});

/**
 * Region handler that updates transaction id of system form.
 *
 * @since 1.1
 */
AraneaPage.TransactionIdRegionHandler = Class.create({

  process: function(content) {
    var systemForm = _ap.getSystemForm();
    if (systemForm.araTransactionId)
      systemForm.araTransactionId.value = content;
  }

});

/**
 * The Region handler that updates DOM element content.
 *
 * @since 1.1
 */
AraneaPage.DocumentRegionHandler = Class.create({

  process: function(content) {
    var text = new Text(content);
    var length = text.readLine();
    var properties = text.readCharacters(length).evalJSON();

    var id = properties.id;
    var mode = properties.mode;
    var domContentString = text.toString();

    _ap.debug("Updating document region '" + id + "'...");

    if (mode == 'update') {
      $(id).update(domContentString);
    } else if (mode == 'replace') {
      $(id).replace(domContentString);
    } else {
      _ap.logger.error('Document region mode "' + mode + '" is unknown');
    }

    _ap.addSystemLoadEvent(_ap.findSystemForm);
  }
});

/**
 * Region handler that updates the messages area.
 *
 * @since 1.1
 */
AraneaPage.MessageRegionHandler = Class.create({

  /* Public fields */

  /**
   * @since 1.1
   */
  regionClass: '.aranea-messages',

  /**
   * @since 1.1
   */
  regionTypeAttribute: 'arn-msgs-type',

  /**
   * @since 1.1
   */
  messageSeparator: '<br/>',

  process: function(content) {
    var messagesByType = content.evalJSON();
    this.updateRegions(messagesByType);
  },

  updateRegions: function(messagesByType) {
    $$(this.regionClass).each((function(region) {
      var messages = null;
      if (region.hasAttribute(this.regionTypeAttribute)) {
        var type = region.readAttribute(this.regionTypeAttribute);
        if (messagesByType[type]) {
          messages = messagesByType[type];
          if (messages.size() > 0) {
            this.showMessageRegion(region, messages);
            return;
          }
          messages = null;
        }
      } else {
        messages = $H(messagesByType).values().flatten();
        if (messages.size() > 0) {
          this.showMessageRegion(region, messages);
          return;
        }
      }
      this.hideMessageRegion(region);
    }).bind(this));
  },

  showMessageRegion: function(region, messages) {
    this.findContentElement(region).update(this.buildRegionContent(messages));
    this.findDisplayElement(region).show();
  },

  hideMessageRegion: function(region) {
    this.findDisplayElement(region).hide();
    this.findContentElement(region).update();
  },

  findContentElement: function(region) {
    return region;
  },

  findDisplayElement: function(region) {
    return region;
  },

  buildRegionContent: function(messages) {
    return messages.invoke('escapeHTML').join(this.messageSeparator);
  }
});

/**
 * Region handler that opens popup windows.
 *
 * @since 1.1
 */
AraneaPage.PopupRegionHandler = Class.create({

  process: function(content) {
    var popups = content.evalJSON();
    this.openPopups(popups);
    popups = null;
    _ap.addSystemLoadEvent(Aranea.Popups.processPopups);
  },

  openPopups: function(popups) {
    popups.each(function(popup) {
      Aranea.Popups.addPopup(popup.popupId, popup.windowProperties, popup.url);
    });
  }
});

/**
 * Region handler that forces a reload of the page by submitting the system
 * form.
 *
 * @since 1.1
 */
AraneaPage.ReloadRegionHandler = Class.create({

  process: function(content) {
    var systemForm = _ap.getSystemForm();
    if (systemForm.araTransactionId)
      systemForm.araTransactionId.value = 'inconsistent';

    // if current systemform is overlayed, reload only overlay
    if (systemForm.araOverlay) {
      return new DefaultAraneaOverlaySubmitter(systemForm).event(new Element('div'));
    }

    /* Actually we should enter the domain of hackery.
       Reloads can break the back button in a subtle way, because the new state identifier
       would have been already loaded from AJAX response header to the system form. Thus if
       one comes back to a page after AJAX request + full reload, the page shown
       would not match the state identifier actually present in the system form.
       If one just did window.location.href=window.location.href, no history navigation event
       is generated because URL would not change. And the transactionId would have to be
       encoded in the URL because NULL transactionId is consistent.
       So the redirect should:
         set transactionId to non-null inconsistent value, but may not affect the system form.
       Which is not satisfied by current implementation here. */
    var result = new DefaultAraneaSubmitter().event_4(systemForm);

    systemForm = null;

    return result;
  }
});

AraneaPage.FormBackgroundValidationRegionHandler = Class.create({

  process: function(content) {
    var result = content.evalJSON();
    var formelement = $(result.formElementId);

    var inputSpan = this.getParentSpan(formelement);
    var labelSpan = this.getLabelSpan(formelement);

    Aranea.UI.markFEContentStatus(result.valid, inputSpan);
    Aranea.UI.markFEContentStatus(result.valid, labelSpan);

    if (result.valid || result.clientRenderText) {
      $$('[class~="aranea-formelementerrors ' + result.formElementId + '"]').each(
        function(e) { e.remove() }
      );
    }

    if (result.clientRenderText) {
      // invoke the supplied client side error-message renderer
      result.clientRenderText.evalScripts();
    }

    result = null;
    formelement = null;
    inputSpan = null;
    labelSpan = null;
  },

  getParentSpan: function(formelement) {
    return formelement.id ? $('fe-span-' + formelement.id) : null;
  },

  getLabelSpan: function(formelement) {
    return formelement.id ? $('label-' + formelement.id) : null;
  },

  getParentElement: function(el, tagName, className) {
    var x = function(element) { return element.tagName.toUpperCase() == tagName.toUpperCase(); };
    var y = function(element) { return x(element) && Element.hasClassName(element, className); };
    var filter = className ? y : x;
    var result = $(el).ancestors().find(filter);

    x = null;
    y = null;
    filter = null;

    return result;
  }
});

// Initialize new Aranea page.
// Aranea page object is accessible in two ways: _ap and araneaPage().
var _ap = new AraneaPage();
function araneaPage() { return _ap; }
_ap.addSystemLoadEvent(AraneaPage.init);
_ap.addSystemLoadEvent(AraneaPage.findSystemForm);
_ap.addSystemLoadEvent(AraneaPage.initRSHURL);
_ap.addSystemUnLoadEvent(AraneaPage.deinit);

AraneaPage.addRegionHandler('transactionId', new AraneaPage.TransactionIdRegionHandler());
AraneaPage.addRegionHandler('document', new AraneaPage.DocumentRegionHandler());
AraneaPage.addRegionHandler('messages', new AraneaPage.MessageRegionHandler());
AraneaPage.addRegionHandler('popups', new AraneaPage.PopupRegionHandler());
AraneaPage.addRegionHandler('reload', new AraneaPage.ReloadRegionHandler());
AraneaPage.addRegionHandler('aranea-formvalidation', new AraneaPage.FormBackgroundValidationRegionHandler());


/**
 * Aranea object which provides namespace for objects created/needed by
 * different modules.
 * 
 * @since 1.0.11
 */
var Aranea = Aranea ? Aranea : {};

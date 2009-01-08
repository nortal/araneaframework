/**
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
**/

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */

function AraneaStore() {
  var objects = new Array();

  this.add = function(object) {
    var len = objects.length;
    objects[len] = object;
  };
      
  this.clear = function() {
    objects = new Array();   
  };
      
  this.length = function() {   
    return objects.length;   
  };
      
  this.getContents = function() {   
    return objects;   
  };

  this.forEach = function(f) {   
    for(var i = 0; i < objects.length; i++) {   
      f(objects[i]);   
    }   
  };
}

function AraneaEventStore() {
  var araneaEventStore = function() {
    var processEvent = function(event) {
      araneaPage().getLogger().trace("Starting to process event: " + Object.inspect(event));
      if (typeof event != "function") {
        event;
      } else {
        event();
      }
    };

    this.execute = function() {
      this.forEach(processEvent);
      this.clear();
    };
  };
  
  araneaEventStore.prototype = new AraneaStore();
  return new araneaEventStore();
}

/* AraneaPage object is present on each page served by Aranea and contains common
 * functionality for setting page related variables, events and functions. */
function AraneaPage() {
  /* URL of aranea dispatcher servlet serving current page. 
   * This is by default set by Aranea JSP ui:body tag. */ 
  var servletURL = null;
  this.getServletURL = function() { return servletURL; };
  this.setServletURL = function(url) { servletURL = new String(url); };

  /* If servlet URL is not enough for some purposes, encoding function should be overwritten. */
  this.encodeURL = function(url) { return url; };

  // To monitor focused element and to make it focused after content updating by Ajax. (Since 1.2)
  var focusedFormElementName = null;

  /* Indicates whether the page is completely loaded or not. Page is considered to 
   * be loaded when all system onload events have completed execution. */
  var loaded = false;
  this.isLoaded = function() { return loaded; };
  this.setLoaded = function(b) { if (typeof b == "boolean") { loaded = b; } };

  /* Logger that outputs javascript logging messages. */
  var dummyLogger = new function() {
        var dummy = function() {};
        this.trace = dummy;
        this.debug = dummy;
        this.info = dummy;
        this.warn = dummy;
        this.error = dummy;
        this.fatal = dummy;
        dummy = null;
  };

  var safariLogger = (window.console && window.console.log) ? new function() {
        var f = function(s) { window.console.log(s); };
        this.trace = f;
        this.debug = f;
        this.info = f;
        this.warn = f;
        this.error = f;
        this.fatal = f;
        f = null;
  } : dummyLogger;

  var firebugLogger = (window.console && window.console.debug) ? new function() {
        this.trace = window.console.debug;
        this.debug = window.console.debug;
        this.info = window.console.info;
        this.warn = window.console.warn;
        this.error = window.console.error;
        this.fatal = window.console.error;
  } : safariLogger;

  var logger = dummyLogger;
  this.setDummyLogger = function() { logger = dummyLogger; };
  this.setDefaultLogger = function() { 
    if (window['log4javascript/log4javascript.js']) {
      logger = log4javascript.getDefaultLogger();
    }
  };
  this.setFirebugLogger = function() { this.setLogger(firebugLogger); };
  this.setLogger = function(theLogger) { logger = theLogger; };
  this.getLogger = function() { return logger; };
  
  /* locale - should be used only for server-side reported locale */
  var locale = new AraneaLocale("", "");
  this.getLocale = function() { return locale; };
  this.setLocale = function(loc) { locale = loc; };

  /* Indicates whether some form on page is (being) submitted already
   * by traditional HTTP request. */
  var submitted = false;
  this.isSubmitted = function() { return submitted; };
  this.setSubmitted = function() { submitted = true; };
  
  var systemForm = null;
  /** @return systemForm currently active in this AraneaPage
    * @since 1.1 */
  this.getSystemForm = function() { return systemForm; };
  /** 
    * Sets the active system form in this AraneaPage. 
    * @since 1.1 */
  this.setSystemForm = function(_systemForm) {
    this.debug("AraneaPage: Setting systemform to: " + _systemForm);
    systemForm = _systemForm;
  };

  /** @since 1.1 */
  this.setSystemFormEncoding = function(encoding) {
    this.addSystemLoadEvent(function() {
      systemForm.enctype = encoding;
      systemForm.encoding = encoding; // IE
    });
  };

  /** Returns the id of a component who should receive events generated by DOM element.
    * @since 1.1 */
  this.getEventTarget = function(element) {
    return element.getAttribute('arn-trgtwdgt');
  };

  /** Returns event id that should be sent to server when event(element) is called.
    * @since 1.1 */
  this.getEventId = function(element) {
    return element.getAttribute('arn-evntId');
  };

  /** Returns event parameter that should be sent to server when event(element) is called.
    * @since 1.1 */
  this.getEventParam = function(element) {
    return element.getAttribute('arn-evntPar');
  };

  /** Returns update regions that should be sent to server when event(element) is called.
    * @since 1.1 */
  this.getEventUpdateRegions = function(element) {
    return element.getAttribute('arn-updrgns');
  };

  /** Returns closure that should be evaluated when event(element) is called and
    * needs to decide whether server-side event invocation is needed.
    * @since 1.1 */
  this.getEventPreCondition = function(element) {
    return element.getAttribute('arn-evntCond');
  };

  /** Timer that executes keepalive calls, if any. */
  var keepAliveTimers = new AraneaStore();

  /** Variables holding different (un)load events that should be executed when page loads -- on body (un)load or alike. */
  var systemLoadEvents = new AraneaEventStore();
  var clientLoadEvents = new AraneaEventStore();
  var systemUnLoadEvents = new AraneaEventStore();
  
  this.submitCallbacks = new Object();

  this.addSystemLoadEvent = function(event) { systemLoadEvents.add(event); }
  this.addClientLoadEvent = function(event) { clientLoadEvents.add(event); }
  this.addSystemUnLoadEvent = function(event) { systemUnLoadEvents.add(event); }

  this.onload = function() {
    logger.trace('System (on)load events executing.\n');
    systemLoadEvents.execute();
    this.setLoaded(true);
    logger.trace('System (on)load events are executed.\n');
    logger.trace('Client load events executing.\n');
    clientLoadEvents.execute(); 
    logger.trace('Client load events are executed.\n');
  };
  this.onunload = function() { systemUnLoadEvents.execute(); };
  
  // General callbacks executed before each form submit.
  /** Adds callback executed before next form submit. */
  this.addSubmitCallback = function(callback) {
    this.addSystemFormSubmitCallback('callbacks', callback);
  };

  // General callbacks executed before each submit of the specified system form.
  /** Add callback executed before form with given id is submitted next time. */
  this.addSystemFormSubmitCallback = function(systemFormId, callback) {
    if (!this.submitCallbacks[systemFormId]) {
      this.submitCallbacks[systemFormId] = new AraneaEventStore();
    }
    this.submitCallbacks[systemFormId].add(callback);
  };

  /** Executes all callbacks that should run before submitting the form with given id. 
    * Executed callbacks are removed. */
  this.executeCallbacks = function(systemFormId) {
    logger.trace('Request for submit callback execution was received.\n');
    if (this.submitCallbacks['callbacks']) {
      logger.trace('General submit callbacks executing.\n');
      this.submitCallbacks['callbacks'].execute();
      logger.trace('General submit callbacks are executed.\n');
    }

    if (this.submitCallbacks[systemFormId]) {
      logger.trace('Submit callbacks executing.\n');
      this.submitCallbacks[systemFormId].execute();
      logger.trace('Submit callbacks are executed.\n');
    }
  };

  /** 
   * Chooses appropriate submitting method and submittable form given the HTML element
   * that initiated the submit request. Applies the appropriate paramater values
   * and submits the systemForm which owns the element. */
  this.event = function(element) {
    if (this.isSubmitted() || !this.isLoaded())
    return false;
    
    var systemForm = $(element).ancestors().find(function(element) {
    	return element.tagName.toLowerCase() == 'form' && element.hasAttribute('arn-systemForm');
    });
    var preCondition = this.getEventPreCondition(element);

    if (preCondition) {
      var f = new Function("element", preCondition);
      if (!f(element)) {
        f = null;
        return false;
      }
      f = null;
    }

    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    var result = this.findSubmitter(element, systemForm).event(element);

    systemForm = null;
    preCondition = null;

    return result;
  };

  /** 
   * This function can be overwritten to support additional submit methods. 
   * It is called by event() to determine the appropriate form submitter.
   */
  this.findSubmitter = function(element, systemForm) {
    var updateRegions = this.getEventUpdateRegions(element);

    if (updateRegions && updateRegions.length > 0) {
      updateRegions = null; // Not used any more
      return new DefaultAraneaAJAXSubmitter(systemForm);
    } else {
      if (systemForm.hasClassName('aranea-overlay')) {
        return new DefaultAraneaOverlaySubmitter(systemForm);
	  }

      return new DefaultAraneaSubmitter(systemForm);
    }
  };
  
  /** Another submit function, takes all params that are possible to 
    * use with Aranea JSP currently. 
    * @param systemForm form that will be submitted 
    * @param eventId event identifier sent to the server 
    * @param eventTarget event target identifier (widget id) 
    * @param eventParam event parameter 
    * @param eventPrecondition closure, submit is only performed when its evaluation returns true
    * @param eventUpdateRegions identifiers for regions that should be regenerated on server-side */
  // TODO: get rid of duplicated logic from: submit() and findSubmitter()
  this.event_6 = function(systemForm, eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions) {
    if (this.isSubmitted() || !this.isLoaded()) {
      return false;
    }

    if (eventPrecondition) {
      var f = new Function(eventPrecondition);
      if (!f()) {
        return false;
      }
    }

    if (typeof eventParam == 'function') {
      eventParam = eventParam();
    }

    if (typeof eventPrecondition == 'function') {
      eventPrecondition = eventPrecondition();
    }

    if (typeof eventUpdateRegions == 'function') {
      eventUpdateRegions = eventUpdateRegions();
    }

    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    if (eventUpdateRegions != null && eventUpdateRegions.length > 0) 
      return new DefaultAraneaAJAXSubmitter().event_5(systemForm, eventId, eventTarget, eventParam, eventUpdateRegions);
    else if (systemForm.hasClassName('aranea-overlay'))
      return new DefaultAraneaOverlaySubmitter().event_7(systemForm, eventId, eventTarget, eventParam);
    else
      return new DefaultAraneaSubmitter().event_4(systemForm, eventId, eventTarget, eventParam);
  };

  /**
   * Returns URL that can be used to invoke full HTTP request with some predefined request parameters.
   * @param topServiceId server-side top service identifier
   * @param threadServiceId server-side thread service identifier
   * @param araTransactionId transaction id expected by the server
   * @param extraParams more parameters, i.e "p1=v1&p2=v2"
   */
  this.getSubmitURL = function(topServiceId, threadServiceId, araTransactionId, extraParams) {
    var url = this.encodeURL(this.getServletURL());
    url += '?araTransactionId=' + araTransactionId;
    if (topServiceId) 
      url += '&araTopServiceId=' + topServiceId;
    if (threadServiceId) 
      url += '&araThreadServiceId=' + threadServiceId;
  
    if (_ap.getSystemForm().araClientStateId) {
      url += '&araClientStateId=' + _ap.getSystemForm().araClientStateId.value;
    }

    if (extraParams)    
      url += '&' + extraParams;

    return url;
  };

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
  this.getActionSubmitURL = function(systemForm, actionId, actionTarget, actionParam, sync, extraParams) {
    var url = this.getSubmitURL(systemForm.araTopServiceId.value, systemForm.araThreadServiceId.value, 'override', extraParams);
    url += '&araServiceActionPath=' + actionTarget;
    if (actionId)
      url += '&araServiceActionHandler=' + actionId;
    if (actionParam)
      url += '&araServiceActionParameter=' + actionParam;
    if (sync != undefined && !sync)
      url += '&araSync=false';

    return url;
  };

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
  this.action = function(element, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams) {
    var systemForm = this.getSystemForm();
    return this.action_6(systemForm, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams);
  };

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
  this.action_6 = function(systemForm, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams) {
    if (window['prototype/prototype.js']) {
      options = Object.extend({
        method: 'post',
        onComplete: actionCallback,
        onException: AraneaPage.handleRequestException
      }, options);
      var url = this.getActionSubmitURL(systemForm, actionId, actionTarget, actionParam, sync, extraParams);
      return new Ajax.Request(url, options);
    } else {
      araneaPage().getLogger().warn("Prototype library not accessible, action call cannot be made.");
    }
  };

  this.debug = function(message) {
    this.getLogger().debug(message);
  };
  
  /** 
   * Provides preferred way of overriding AraneaPage object functions. 
   * @param functionName name of AraneaPage function that should be overridden. 
   * @param f replacement function 
   */
  this.override = function(functionName, f) {
    logger.info("AraneaPage." +functionName + " was overriden.");
    this[functionName] = f;
  };
  
  /** 
   * Adds keepalive function f that is executed periodically after time 
   * milliseconds has passed 
   */
  this.addKeepAlive = function(f, time) {
    keepAliveTimers.add(setInterval(f, time));
  };

  /** Clears/removes all registered keepalive functions. */
  this.clearKeepAlives = function() {
    keepAliveTimers.forEach(function(timer) {clearInterval(timer);});
  };
  
  /** Whether this application should use ajax form validation. */
  var ajaxValidation = false;
  /** Returns the flag that determines whether background validation is used by 
    * for all forms (FormWidgets) in the application. */
  this.getBackgroundValidation = function() { return ajaxValidation; };
  /** Sets the background form validation flag on client side. Note that server-side
    * must have identical setting for these settings to have effect. */
  this.setBackgroundValidation = function(useAjax) { ajaxValidation = Boolean(useAjax); };
}

/* Returns a default keepalive function -- to make periodical requests to expiring thread
 * or top level services. */
AraneaPage.getDefaultKeepAlive = function(topServiceId, threadServiceId, keepAliveKey) {
  return function() {
    if (window['prototype/prototype.js']) {
      var url = araneaPage().getSubmitURL(topServiceId, threadServiceId, 'override');
      url += "&" + keepAliveKey + "=true";
      araneaPage().debug("Sending async service keepalive request to URL '" + url +"'");
      var keepAlive = new Ajax.Request(
          url,
          { method: 'post' }
      );
    } else {
      araneaPage().getLogger().warn("Prototype library not accessible, service keepalive calls cannot be made.");
    }
  };
};

/** 
  * Searches for widget marker around the given element. 
  * If found, returns the marker DOM element, else returns null. */
AraneaPage.findWidgetMarker = function(element) {
  var ancestors = $(element).ancestors();
  for (var i = 0; i < ancestors.length; i++) {
  	var ancestor = ancestors[i];
  	var marker = $w(ancestor.className).find(function(name) {
      return (name == 'widgetMarker');
    });
    if (marker) {
      marker = null;
      return ancestor; 
    }
  }
  return null;
};

/** Random request id generator. Sent only with XMLHttpRequests which apply to certain update regions.
  * Currently only purpose of it is easier debugging (identifying requests). */
AraneaPage.getRandomRequestId = function() {
  return Math.round(100000*Math.random());
};

/** @since 1.1 */
AraneaPage.getFileImportString = function(filename) {
  return araneaPage().getServletURL() + "/fileimporter/" + filename;
};

//Page initialization function, should be called upon page load.
AraneaPage.init = function() {
  araneaPage().addSystemLoadEvent(Behaviour.apply);

  // Monitor the currently focused element for Ajax page update (since 1.2)
  $$("input, select, textarea, button").each(function (formElement) {
      formElement.observe("focus", function(event) {
          AraneaPage.focusedFormElementName = this.name;
      });
  });
};

//Page deinitialization function, should be called upon page unload.
AraneaPage.deinit = function() {
  this.servletURL = null;
  this.focusedFormElementName = null;
  this.dummyLogger = null;
  this.safariLogger = null;
  this.firebugLogger = null;
  this.logger = null;
  this.locale = null;
  this.systemForm = null;

  this.keepAliveTimers = null;
  this.systemLoadEvents = null;
  this.clientLoadEvents = null;
};

/** Searches for system form in HTML page and registers it in 
  * current AraneaPage object as active systemForm.
  * @param element unused, could be set to some DOM element that definitely falls inside the system form
  * @since 1.1 
  * TODO: badly named, should be deprecated in favour of some well-name function */
AraneaPage.findSystemForm = function(element) {
  araneaPage().setSystemForm($$('form[arn-systemForm="true"]').first());
};

function DefaultAraneaSubmitter(form) {
  var systemForm = form;

  this.event = function(element) {
    if (element) {
      // event information
      var widgetId = araneaPage().getEventTarget(element);
      var eventId = araneaPage().getEventId(element);
      var eventParam = araneaPage().getEventParam(element);

      return this.event_4(systemForm, eventId, widgetId, eventParam);
    }
  };
}

DefaultAraneaSubmitter.prototype.event_4 = function(systemForm, eventId, widgetId, eventParam) {
  systemForm.araWidgetEventPath.value = widgetId ? widgetId : "";
  systemForm.araWidgetEventHandler.value = eventId ? eventId : "";
  systemForm.araWidgetEventParameter.value = eventParam ? eventParam : "";

  araneaPage().setSubmitted();

  systemForm.submit();

  return false;
};

function DefaultAraneaOverlaySubmitter(form) {
  var systemForm = form;

  this.event = function(element) {
    // event information
    var widgetId = araneaPage().getEventTarget(element);
    var eventId = araneaPage().getEventId(element);
    var eventParam = araneaPage().getEventParam(element);

    systemForm.araWidgetEventPath.value = widgetId ? widgetId : "";
    systemForm.araWidgetEventHandler.value = eventId ? eventId : "";
    systemForm.araWidgetEventParameter.value = eventParam ? eventParam : "";

    var options = {params: systemForm.serialize(true)};
    Object.extend(options, Aranea.ModalBox.Options || {});
    Modalbox.show(systemForm.readAttribute('action') + '?araOverlay=true', options);
    return false;
  };

  this.event_7 = function(systemForm, eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions) {
    // event information
    var element = $(eventTarget);
    var widgetId = araneaPage().getEventTarget(element);
    var eventId = araneaPage().getEventId(element);
    var eventParam = araneaPage().getEventParam(element);

    systemForm.araWidgetEventPath.value = widgetId ? widgetId : "";
    systemForm.araWidgetEventHandler.value = eventId ? eventId : "";
    systemForm.araWidgetEventParameter.value = eventParam ? eventParam : "";

    // copy the content of rich editors to corresponding HTML textinputs/textareas
    if (window.tinyMCE) {
      window.tinyMCE.triggerSave();
    }

    var options = {params: systemForm.serialize(true)};
    Object.extend(options, Aranea.ModalBox.Options || {});
    Modalbox.show(systemForm.readAttribute('action') + '?araOverlay=true', options);
    return false;
  };
}

function DefaultAraneaAJAXSubmitter(form) {
  var systemForm = form;

  this.event = function(element) {
    // event information
    var widgetId = araneaPage().getEventTarget(element);
    var eventId = araneaPage().getEventId(element);
    var eventParam = araneaPage().getEventParam(element);
    var updateRegions = araneaPage().getEventUpdateRegions(element);

    return this.event_5(systemForm, eventId, widgetId, eventParam, updateRegions);
  };
}

/**
 * The delay after which Ajax.Request onComplete expects all the DOM updates 
 * to have taken place, in milliseconds.
 * @since 1.1 */
DefaultAraneaAJAXSubmitter.contentUpdateWaitDelay=30;


/**
 * @since 1.2 */
DefaultAraneaAJAXSubmitter.ResponseHeaderProcessor = function(transport) {
	var stateVersion = null;
	try {
		stateVersion = transport.getResponseHeader('Aranea-Application-StateVersion');
	} catch (e) {
		stateVersion = null;
	}
	if (stateVersion != null && stateVersion.length > 0) {
		var sForm = araneaPage().getSystemForm();
		if (sForm.araClientStateId) {
			sForm.araClientStateId.value = stateVersion;
		}
		if (window.dhtmlHistory) {
			window.dhtmlHistory.add(stateVersion, true);
		}
	}
};

DefaultAraneaAJAXSubmitter.prototype.event_5 = function(systemForm, eventId, widgetId, eventParam, updateRegions) {
  systemForm.araWidgetEventPath.value = widgetId ? widgetId : "";
  systemForm.araWidgetEventHandler.value = eventId ? eventId : "";
  systemForm.araWidgetEventParameter.value = eventParam ? eventParam : "";

  // copy the content of rich editors to corresponding HTML textinputs/textareas
  if (window.tinyMCE) { 
    window.tinyMCE.triggerSave(); 
  }

  var ajaxRequestId = AraneaPage.getRandomRequestId().toString();

  var neededAraClientStateId = systemForm.araClientStateId ? systemForm.araClientStateId.value : null;
  var neededAraTransactionId = 'override';
  
  if (updateRegions == 'araneaGlobalClientHistoryNavigationUpdateRegion') {
    neededAraClientStateId = window.dhtmlHistoryListenerRequestedState;
    window.dhtmlHistoryListenerRequestedState = null;
    neededAraTransactionId = 'inconsistent';
  }

  AraneaPage.showLoadingMessage();

  $(systemForm.id).request({
    parameters: {
      araTransactionId: neededAraTransactionId,
      ajaxRequestId: ajaxRequestId,
      updateRegions: updateRegions,
      araClientStateId: neededAraClientStateId
    },
    onSuccess: function(transport) {
      AraneaPage.hideLoadingMessage();
      var logmsg = "";

      // This gets executed twice, it may be that during update region processing
      // something already needs current stateId presence, e.g. the reloading region 
      // handler. As it's likely that whole system form will be replaced completely
      // when document region is updated, this must be repeated in onComplete.
      DefaultAraneaAJAXSubmitter.ResponseHeaderProcessor(transport);

      if (transport.responseText.substr(0, ajaxRequestId.length + 1) == ajaxRequestId + "\n") {
        logmsg += 'Partial rendering: received successful response';
        logmsg += ' (' + transport.responseText.length + ' characters)';
        logmsg += ': ' + transport.status + ' ' + transport.statusText;
        araneaPage().debug(logmsg);
        AraneaPage.processResponse(transport.responseText);
        AraneaPage.init();
      } else {
        logmsg += 'Partial rendering: received erroneous response';
        logmsg += ' (' + transport.responseText.length + ' characters)';
        logmsg += ': ' + transport.status + ' ' + transport.statusText;
        araneaPage().debug(logmsg);
        // Doesn't work quite well for javascript and CSS, but fine for plain HTML
        document.write(transport.responseText);
        document.close();
      }
    },
    onComplete: function(transport) {
      // because prototype's Element.update|replace delay execution of scripts,
      // immediate execution of onload() is not guaranteed to be correct
      var f = function() {
        araneaPage().addSystemLoadEvent(function() { DefaultAraneaAJAXSubmitter.ResponseHeaderProcessor(transport); });
      	araneaPage().onload();
      	if (Aranea.ModalBox) {
      	  Aranea.ModalBox.afterUpdateRegionResponseProcessing(systemForm);
        }
        // Set the previously focused form control focused again (since 1.2)
        if (AraneaPage.focusedFormElementName) {
          var formElem = $$('[name="' + AraneaPage.focusedFormElementName + '"]').first();
          if (formElem) {
            AraneaPage.focusedFormElementName = null;
            formElem.focus();
          }
      	}
      };

      // force the delay here
      setTimeout(f, DefaultAraneaAJAXSubmitter.contentUpdateWaitDelay);
    },
    onFailure: function(transport) {
      AraneaPage.hideLoadingMessage();
      var logmsg = 'Partial rendering: received erroneous response (';
      logmsg += transport.responseText.length
      logmsg += ' characters): ';
      logmsg += transport.status;
      logmsg += ' ';
      logmsg += transport.statusText;
      araneaPage().debug(logmsg);

      // Doesn't work quite well for javascript and CSS, but fine for plain HTML
      document.write(transport.responseText);
      document.close();
    },
    onException: function(request, exception) {
      AraneaPage.hideLoadingMessage();
      AraneaPage.handleRequestException(request, exception);
    }
  });

  return false;
};

Object.extend(AraneaPage, {
  /* Private fields */
  loadingMessagePositionHack: false,
  receivedRegionCounters: null,
  regionHandlers: new Hash(),

  /* Public fields */

  /** @since 1.1 */
  loadingMessageContent: 'Loading...',
  /** @since 1.1 */
  loadingMessageId: 'aranea-loading-message',
  /** @since 1.1 */
  reloadOnNoDocumentRegions: false,

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
        araneaPage().debug('Region type: "' + key + '" (' + length + ' characters)');
        this.regionHandlers[key].process(content);
      } else {
        araneaPage().getLogger().error('Region type: "' + key + '" is unknown!');
      }
    }

    if (this.reloadOnNoDocumentRegions && !this.receivedRegionCounters['document']) {
      araneaPage().debug('No document regions were received, forcing a reload of the page');
      if (this.regionHandlers['reload']) {
        this.regionHandlers['reload'].process();
      } else {
        araneaPage().getLogger().error('No handler is registered for "reload" region, unable to force page reload!');
      }
    }
  },

  /**
   * Exception handler that is invoked on Ajax.Request errors.
   *
   * @since 1.1
   */
  handleRequestException: function(request, exception) {
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
  },

  /**
   * Build loading message. Called when an existing message element is not
   * found.
   *
   * @since 1.1
   */
  buildLoadingMessage: function() {
    return Builder.node('div', {id: this.loadingMessageId}, this.loadingMessageContent);
  },

  /**
   * Perform positioning of loading message (if needed in addition to CSS).
   * Called before making the message element visible. This implementation
   * provides workaround for IE 6, which doesn't support
   * <code>position: fixed</code> CSS attribute; the element is manually
   * positioned at the top of the document. If you don't need this, overwrite
   * this with an empty function:
   * <code>AraneaPage.positionLoadingMessage = Prototype.emptyFunction;</code>
   *
   * @since 1.1
   */
  positionLoadingMessage: function(element) {
    if (this.loadingMessagePositionHack || element.offsetTop) {
      this.loadingMessagePositionHack = true;
      element.style.position = 'absolute';
      element.style.top = document.documentElement.scrollTop + 'px';
    }
  }
});


/**
 * Region handler that updates transaction id of system form.
 *
 * @since 1.1
 */
AraneaPage.TransactionIdRegionHandler = Class.create();
AraneaPage.TransactionIdRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(content) {
    var systemForm = araneaPage().getSystemForm();
    if (systemForm.araTransactionId)
      systemForm.araTransactionId.value = content;
  }
};
AraneaPage.addRegionHandler('transactionId', new AraneaPage.TransactionIdRegionHandler());

/**
 * Region handler that updates DOM element content.
 *
 * @since 1.1
 */
AraneaPage.DocumentRegionHandler = Class.create();
AraneaPage.DocumentRegionHandler.prototype = {
  initialize: function() {},

  process: function(content) {
    var text = new Text(content);
    var length = text.readLine();
    var properties = text.readCharacters(length).evalJSON();
    var id = properties.id;
    var mode = properties.mode;
    var domContentString = text.toString();

    if (mode == 'update') {
      $(id).update(domContentString);
    } else if (mode == 'replace') {
      $(id).replace(domContentString);
    } else {
      araneaPage().getLogger().error('Document region mode "' + mode + '" is unknown');
    }

    araneaPage().addSystemLoadEvent(AraneaPage.findSystemForm);
  }
};
AraneaPage.addRegionHandler('document', new AraneaPage.DocumentRegionHandler());

/**
 * Region handler that updates the messages area.
 *
 * @since 1.1
 */
AraneaPage.MessageRegionHandler = Class.create();
AraneaPage.MessageRegionHandler.prototype = {
  /* Public fields */

  /** @since 1.1 */
  regionClass: '.aranea-messages',
  /** @since 1.1 */
  regionTypeAttribute: 'arn-msgs-type',
  /** @since 1.1 */
  messageSeparator: '<br/>',

  initialize: function() {},

  process: function(content) {
    var messagesByType = content.evalJSON();
    this.updateRegions(messagesByType);
  },

  updateRegions: function(messagesByType) {
    $$(this.regionClass).each((function(region) {
      var messages = null;
      if (region.hasAttribute(this.regionTypeAttribute)) {
        var type = region.getAttribute(this.regionTypeAttribute);
        if (messagesByType[type]) {
          messages = messagesByType[type];
          if (messages.size() > 0) {
            this.showMessageRegion(region, messages);
            return;
          }
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
};
AraneaPage.addRegionHandler('messages', new AraneaPage.MessageRegionHandler());

/**
 * Region handler that opens popup windows.
 *
 * @since 1.1
 */
AraneaPage.PopupRegionHandler = Class.create();
AraneaPage.PopupRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(content) {
    var popups = content.evalJSON();
    this.openPopups(popups);
    araneaPage().addSystemLoadEvent(Aranea.Popups.processPopups);
  },

  openPopups: function(popups) {
    popups.each(function(popup) {
      Aranea.Popups.addPopup(popup.popupId, popup.windowProperties, popup.url);
    });
  }
};
AraneaPage.addRegionHandler('popups', new AraneaPage.PopupRegionHandler());

/**
 * Region handler that forces a reload of the page by submitting the system
 * form.
 *
 * @since 1.1
 */
AraneaPage.ReloadRegionHandler = Class.create();
AraneaPage.ReloadRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(content) {
    var systemForm = araneaPage().getSystemForm();
    if (systemForm.araTransactionId)
      systemForm.araTransactionId.value = 'inconsistent';

    // if current systemform is overlayed, reload only overlay
    if (systemForm.araOverlay) {
      return new DefaultAraneaOverlaySubmitter(systemForm).event(document.createElement("div"));
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
    return new DefaultAraneaSubmitter().event_4(systemForm);
  }
};
AraneaPage.addRegionHandler('reload', new AraneaPage.ReloadRegionHandler());

AraneaPage.FormBackgroundValidationRegionHandler = Class.create();
AraneaPage.FormBackgroundValidationRegionHandler.prototype = {
  initialize: function() {},

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
  },

  getParentSpan: function(formelement) {
    if (formelement.id)
      return $('fe-span-' + formelement.id);
    return null;
  },
  
  getLabelSpan: function(formelement) {
  	if (formelement.id)
      return $('label-' + formelement.id);
    return null;
  },

  getParentElement: function(el, tagName, className) {
    var x = function(element) { return element.tagName.toUpperCase() == tagName.toUpperCase(); };
    var y = function(element) { return x(element) && Element.hasClassName(element, className); };
  	var filter = className ? y : x;
    return $(el).ancestors().find(filter);
  }
};
AraneaPage.addRegionHandler('aranea-formvalidation', new AraneaPage.FormBackgroundValidationRegionHandler());

AraneaPage.RSHURLInit = function() {
  if (window.dhtmlHistory && _ap.getSystemForm().araClientStateId) {
	window.dhtmlHistory.firstLoad = true;
	window.dhtmlHistory.ignoreLocationChange = true;
    var stateId = _ap.getSystemForm().araClientStateId.value;
    // If we generate hashes to HTTP requests, URL changes cause the browser
    // to never use local history data, as the hash added later is not 
    // accessible from its memory cache. Thus we just keep the URL intact
    // for these cases, so that browsers own history mechanisms can take over.
    if (!stateId.startsWith("HTTP")) {
      window.location.hash = stateId;
      window.dhtmlHistory.add(stateId, null);
    }
  }
};

/* Initialize new Aranea page.  */
/* Aranea page object is accessible in two ways -- _ap and araneaPage() */
var _ap = new AraneaPage();
function araneaPage() { return _ap; }
_ap.addSystemLoadEvent(AraneaPage.init);
_ap.addSystemLoadEvent(AraneaPage.findSystemForm);
_ap.addSystemLoadEvent(AraneaPage.RSHURLInit);
_ap.addClientLoadEvent(AraneaPage.RSHInit);
_ap.addSystemUnLoadEvent(AraneaPage.deinit);

/* Aranea object which provides namespace for objects created/needed by different modules. 
 * @since 1.0.11 */
var Aranea = Aranea ? Aranea : {};

window['aranea.js'] = true;

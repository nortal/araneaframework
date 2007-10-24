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
      araneaPage().getLogger().trace(Object.inspect(event));
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

  /* Indicates whether the page is completely loaded or not. Page is considered to 
   * be loaded when all system onload events have completed execution. */
  var loaded = false;
  this.isLoaded = function() { return loaded; };
  this.setLoaded = function(b) { if (typeof b == "boolean") { loaded = b; } };
  
  /* Logger that outputs javascript logging messages. */
  var dummyLogger = new function() { var dummy = function() {}; this.trace = dummy; this.debug = dummy; this.info = dummy; this.warn = dummy; this.error = dummy; this.fatal = dummy;};
  var safariLogger = (window.console && window.console.log) ? new function() {
  	  var f = window.console.log;
  	  this.trace = f; this.debug = f; this.info = f; this.warn = f; this.error = f; this.fatal = f;
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
  /** @since 1.1 */
  this.getSystemForm = function() { return systemForm; };
  /** @since 1.1 */
  this.setSystemForm = function(_systemForm) { systemForm = _systemForm; };

  /** @since 1.1 */
  this.setSystemFormEncoding = function(encoding) {
    this.addSystemLoadEvent(function() {
      systemForm.enctype = encoding;
      systemForm.encoding = encoding; // IE
    });
  };

  /** @since 1.1 */
  this.getEventTarget = function(element) {
    return element.getAttribute('arn-trgtwdgt');
  };
  /** @since 1.1 */
  this.getEventId = function(element) {
    return element.getAttribute('arn-evntId');
  };
  /** @since 1.1 */
  this.getEventParam = function(element) {
    return element.getAttribute('arn-evntPar');
  };
  /** @since 1.1 */
  this.getEventUpdateRegions = function(element) {
    return element.getAttribute('arn-updrgns');
  };
  /** @since 1.1 */
  this.getEventPreCondition = function(element) {
    return element.getAttribute('arn-evntCond');
  };

  /** Timer that executes keepalive calls, if any. */
  var keepAliveTimers = new AraneaStore();

  /** Variables holding different (un)load events that should be executed when page loads -- on body (un)load or alike. */
  var systemLoadEvents = new AraneaEventStore();
  var clientLoadEvents = new AraneaEventStore();
  var systemUnLoadEvents = new AraneaEventStore();
  
  submitCallbacks = new Object();

  this.addSystemLoadEvent = function(event) { systemLoadEvents.add(event); }
  this.addClientLoadEvent = function(event) { clientLoadEvents.add(event); }
  this.addSystemUnLoadEvent = function(event) { systemUnLoadEvents.add(event); }

  this.onload = function() { 
    logger.trace('System load events executing.\n'); 
    systemLoadEvents.execute();
    this.setLoaded(true);
    logger.trace('Client load events executing.\n'); 
    clientLoadEvents.execute(); 
  };
  this.onunload = function() { systemUnLoadEvents.execute(); };
  
  // General callbacks executed before each form submit.
  this.addSubmitCallback = function(callback) {
    this.addSystemFormSubmitCallback('callbacks', callback);
  };

  // General callbacks executed before each submit of the specified system form.
  this.addSystemFormSubmitCallback = function(systemFormId, callback) {
    if (!submitCallbacks[systemFormId]) {
      submitCallbacks[systemFormId] = new AraneaEventStore();
    }
    submitCallbacks[systemFormId].add(callback);
  };

  this.executeCallbacks = function(systemFormId) {
    if (submitCallbacks['callbacks']) {
      submitCallbacks['callbacks'].execute();
    }

    if (submitCallbacks[systemFormId]) {
      submitCallbacks[systemFormId].execute();
    }
  };

  this.e = function(eventOptions) {
    if (eventOptions) {

    } else {
      this.getLogger().fatal("Cannot submit event, no event options specified.");
    }
  };
  
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
        return false;
      }
    }

    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    return (this.findSubmitter(element, systemForm)).event(element);
  };

  /** 
   * This function can be overwritten to support additional
   * submit methods.
   */
  this.findSubmitter = function(element, systemForm) {
	if (systemForm.hasClassName('aranea-overlay')) {
      return new DefaultAraneaOverlaySubmitter(systemForm);
	}

    var updateRegions = this.getEventUpdateRegions(element);

    if (updateRegions && updateRegions.length > 0)
      return new DefaultAraneaAJAXSubmitter(systemForm);
    else
      return new DefaultAraneaSubmitter(systemForm);
  };
  
  // another submit function, takes all params that are currently possible to use.
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
    
    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    if (eventUpdateRegions != null && eventUpdateRegions.length > 0) 
      return new DefaultAraneaAJAXSubmitter().event_5(systemForm, eventId, eventTarget, eventParam, eventUpdateRegions);
    else
      return new DefaultAraneaSubmitter().event_4(systemForm, eventId, eventTarget, eventParam);
  };

  this.getSubmitURL = function(topServiceId, threadServiceId, araTransactionId, extraParams) {
    var url = this.encodeURL(this.getServletURL());
    url += '?araTransactionId=' + araTransactionId;
    if (topServiceId) 
      url += '&araTopServiceId=' + topServiceId;
    if (threadServiceId) 
      url += '&araThreadServiceId=' + threadServiceId;
  
  	if(extraParams)    
     url += '&' + $H(extraParams).toQueryString();

    return url;
  };

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

  this.action = function(element, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams) {
    var systemForm = this.getSystemForm();
    return this.action_6(systemForm, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams);
  };

  this.action_6 = function(systemForm, actionId, actionTarget, actionParam, actionCallback, options, sync, extraParams) {
    if (window['prototype/prototype.js']) {
      options = Object.extend({
        method: 'get',
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
    this.getLogger().info("AraneaPage." +functionName + " was overriden.");
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
  this.getBackgroundValidation = function() { return ajaxValidation; };
  this.setBackgroundValidation = function(useAjax) { ajaxValidation = Boolean(useAjax); };
}

/* Returns a default keepalive function -- to make periodical requests to expiring thread
 * or top level services. */
AraneaPage.getDefaultKeepAlive = function(topServiceId, threadServiceId, keepAliveKey) {
  return function() {
    if (window['prototype/prototype.js']) {
      var url = araneaPage().getSubmitURL(topServiceId, threadServiceId, 'override');
      url += "&" + keepAliveKey + "=true";
      araneaPage().getLogger().debug("Sending async service keepalive request to URL '" + url +"'");
      var keepAlive = new Ajax.Request(
          url,
          { method: 'post' }
      );
    } else {
      araneaPage().getLogger().warn("Prototype library not accessible, service keepalive calls cannot be made.");
    }
  };
};

/** Searches for widget marker around the given element. */
AraneaPage.findWidgetMarker = function(element) {
  var ancestors = $(element).ancestors();
  for (var i = 0; i < ancestors.length; i++) {
  	var ancestor = ancestors[i];
  	var marker = ancestor.classNames().find(function(name) {
      return (name == 'widgetMarker');
    });
    if (marker) { 
      return ancestor; 
    }
  }
  return null;
};

// Random request id generator. Sent only with AA ajax requests.
// Currently only purpose of it is easier debugging (identifying requests).
AraneaPage.getRandomRequestId = function() {
  return Math.round(100000*Math.random());
};

/** @since 1.1 */
AraneaPage.getFileImportString = function(filename) {
  return araneaPage().getServletURL() + "/fileimporter/" + filename;
};

// Page initialization function, should be called upon page load.
AraneaPage.init = function() {
  araneaPage().addSystemLoadEvent(Behaviour.apply);
};
/** @since 1.1 */
AraneaPage.findSystemForm = function() {
  araneaPage().setSystemForm($A(document.getElementsByTagName('form')).find(
    function(element) {
      return $(element).hasAttribute('arn-systemForm');
    }
  ));
};

function DefaultAraneaSubmitter(form) {
  var systemForm = form;

  this.event = function(element) {
    // event information
    var widgetId = araneaPage().getEventTarget(element);
    var eventId = araneaPage().getEventId(element);
    var eventParam = araneaPage().getEventParam(element);
    
    return this.event_4(systemForm, eventId, widgetId, eventParam);
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

   	Modalbox.show(
   	  systemForm.readAttribute('action') + '?araOverlay',
   	  {
   	    method: 'post',
   	    params: systemForm.serialize(true),
   	    overlayClose: false,
   	    width: 800,
   	    slideDownDuration: 0.0,
   	    slideUpDuration: 0.0,
   	    overlayDuration: 0.0,
   	    resizeDuration: 0.0,
   	    afterLoad: function(content) {
          araneaPage().debug("Content = " + content);
   	      if (content == '') {
   	        //Modalbox.hide();
            var systemForm = araneaPage().getSystemForm();
            if (systemForm.transactionId)
              systemForm.transactionId.value = 'override';
            return new DefaultAraneaSubmitter().event_4(araneaPage().getSystemForm());
   	      }
        }
   	  }
   	);
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

DefaultAraneaAJAXSubmitter.prototype.event_5 = function(systemForm, eventId, widgetId, eventParam, updateRegions) {
  systemForm.araWidgetEventPath.value = widgetId ? widgetId : "";
  systemForm.araWidgetEventHandler.value = eventId ? eventId : "";
  systemForm.araWidgetEventParameter.value = eventParam ? eventParam : "";

  var ajaxRequestId = AraneaPage.getRandomRequestId().toString();
  AraneaPage.showLoadingMessage();
  $(systemForm.id).request({
    parameters: {
      araTransactionId: 'override',
      ajaxRequestId: ajaxRequestId,
      updateRegions: updateRegions
    },
    onSuccess: function(transport) {
      AraneaPage.hideLoadingMessage();
      var logmsg = "";
      if (transport.responseText.substr(0, ajaxRequestId.length + 1) == ajaxRequestId + "\n") {
        logmsg += 'Partial rendering: received successful response';
        logmsg += ' (' + transport.responseText.length + ' characters)';
        logmsg += ': ' + transport.status + ' ' + transport.statusText;
        araneaPage().getLogger().debug(logmsg);
        AraneaPage.processResponse(transport.responseText);
        AraneaPage.init();
        araneaPage().onload();
      } else {
        logmsg += 'Partial rendering: received erroneous response';
        logmsg += ' (' + transport.responseText.length + ' characters)';
        logmsg += ': ' + transport.status + ' ' + transport.statusText;
        araneaPage().getLogger().debug(logmsg);
        // Doesn't work quite well for javascript and CSS, but fine for plain HTML
        document.write(transport.responseText);
        document.close();
      }
    },
    onFailure: function(transport) {
      AraneaPage.hideLoadingMessage();
      logmsg += 'Partial rendering: received erroneous response';
      logmsg += ' (' + transport.responseText.length + ' characters)';
      logmsg += ': ' + transport.status + ' ' + transport.statusText;
      araneaPage().getLogger().debug(logmsg);
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
   *
   * @since 1.1
   */
  addRegionHandler: function(key, handler) {
    this.regionHandlers[key] = handler;
  },

  /**
   * Process response of an updateregions AJAX request. Should be called only
   * on successful response. Invokes region handlers.
   *
   * @since 1.1
   */
  processResponse: function(responseText) {
    var text = new Text(responseText);
    text.readLine(); // responseId
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
        araneaPage().getLogger().debug('Region type: "' + key + '" (' + length + ' characters)');
        this.regionHandlers[key].process(content);
      } else {
        araneaPage().getLogger().error('Region type: "' + key + '" is unknown!');
      }
    }
    if (this.reloadOnNoDocumentRegions && !this.receivedRegionCounters['document']) {
      araneaPage().getLogger().debug('No document regions were received, forcing a reload of the page');
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
  initialize: function() {
  },

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

  initialize: function() {
  },

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
    return new DefaultAraneaSubmitter().event_4(araneaPage().getSystemForm());
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

/* Initialize new Aranea page.  */
/* Aranea page object is accessible in two ways -- _ap and araneaPage() */
_ap = new AraneaPage();
function araneaPage() { return _ap; }
_ap.addSystemLoadEvent(AraneaPage.init);
_ap.addSystemLoadEvent(AraneaPage.findSystemForm);

/* Aranea object which provides namespace for objects created/needed by different modules. 
 * @since 1.0.11 */
var Aranea = Aranea ? Aranea : {};

window['aranea.js'] = true;

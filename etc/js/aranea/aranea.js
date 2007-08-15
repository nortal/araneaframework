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
  }
  
  this.clear = function() {
    objects = new Array();
  }
  
  this.length = function() {
    return objects.length;
  }

  this.getContents = function() {
    return objects;
  }
  
  this.forEach = function(f) {
    for(var i = 0; i < objects.length; i++) {
      f(objects[i]);
    }
  }
}

function AraneaEventStore() {
  var araneaEventStore = function() {
    var processEvent = function(event) {
      if (typeof event != "function") {
        event;
      } else {
        event();
      }
    }

    this.execute = function() {
      this.forEach(processEvent);
      this.clear();
    }
  }
  
  araneaEventStore.prototype = new AraneaStore();
  return new araneaEventStore();
}

/* AraneaPage object is present on each page served by Aranea and contains common
 * functionality for setting page related variables, events and functions. */
function AraneaPage() {
  /* URL of aranea dispatcher servlet serving current page. 
   * This is by default set by Aranea JSP ui:body tag. */ 
  var servletURL = null;
  this.getServletURL = function() { return servletURL; }
  this.setServletURL = function(url) { servletURL = new String(url); }

  /* If servlet URL is not enough for some purposes, encoding function should be overwritten. */
  this.encodeURL = function(url) { return url; }

  /* Indicates whether the page is completely loaded or not. Page is considered to 
   * be loaded when all system onload events have completed execution. */
  var loaded = false;
  this.isLoaded = function() { return loaded; }
  this.setLoaded = function(b) { if (typeof b == "boolean") { loaded = b; } }
  
  /* Logger that outputs javascript logging messages. */
  var dummyLogger = new function() { var dummy = function() {}; this.trace = dummy; this.debug = dummy; this.info = dummy; this.warn = dummy; this.error = dummy; this.fatal = dummy;};
  var firebugLogger = window.console ? new function() { this.trace = window.console.debug; this.debug = window.console.debug; this.info = window.console.info; this.warn = window.console.warn; this.error = window.console.error; this.fatal = window.console.error;} : dummyLogger;
  var logger = dummyLogger;
  this.setDummyLogger = function() { logger = dummyLogger; }
  this.setDefaultLogger = function() { 
    if (window['log4javascript/log4javascript.js'])
      logger = log4javascript.getDefaultLogger();
  }
  this.setFirebugLogger = function() { this.setLogger(firebugLogger); }
  this.setLogger = function(theLogger) { logger = theLogger; }
  this.getLogger = function() { return logger; }
  
  /* locale - should be used only for server-side reported locale */
  var locale = new AraneaLocale("", "");
  this.getLocale = function() { return locale; }
  this.setLocale = function(loc) { locale = loc; }

  /* Indicates whether some form on page is (being) submitted already
   * by traditional HTTP request. */
  var submitted = false;
  this.isSubmitted = function() { return submitted; }
  this.setSubmitted = function() { submitted = true; }
  
  var systemForm = null;
  /** @since 1.1 */
  this.getSystemForm = function() { return systemForm; }
  /** @since 1.1 */
  this.setSystemForm = function(_systemForm) { systemForm = _systemForm; }

  /** @since 1.1 */
  this.setSystemFormEncoding = function(encoding) {
    this.addSystemLoadEvent(function() {
      systemForm.enctype = encoding;
      systemForm.encoding = encoding; // IE
    });
  }

  /** @since 1.1 */
  this.getEventTarget = function(element) {
    return element.getAttribute('arn-trgtwdgt');
  }
  /** @since 1.1 */
  this.getEventId = function(element) {
    return element.getAttribute('arn-evntId');
  }
  /** @since 1.1 */
  this.getEventParam = function(element) {
    return element.getAttribute('arn-evntPar');
  }
  /** @since 1.1 */
  this.getEventUpdateRegions = function(element) {
    return element.getAttribute('arn-updrgns');
  }
  /** @since 1.1 */
  this.getEventPreCondition = function(element) {
    return element.getAttribute('arn-evntCond');
  }

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

  this.onload = function() { systemLoadEvents.execute(); this.setLoaded(true); clientLoadEvents.execute(); }
  this.onunload = function() { systemUnLoadEvents.execute(); }
  
  // General callbacks executed before each form submit.
  this.addSubmitCallback = function(callback) {
    this.addSystemFormSubmitCallback('callbacks', callback);
  }

  // General callbacks executed before each submit of the specified system form.
  this.addSystemFormSubmitCallback = function(systemFormId, callback) {
    if (!submitCallbacks[systemFormId])
    submitCallbacks[systemFormId] = new AraneaEventStore();
    submitCallbacks[systemFormId].add(callback);
  }

  this.executeCallbacks = function(systemFormId) {
    if (submitCallbacks['callbacks'])
    submitCallbacks['callbacks'].execute();
    
  if (submitCallbacks[systemFormId])
    submitCallbacks[systemFormId].execute();
  }
  
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
  }

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
  }
  
  // another submit function, takes all params that are currently possible to use.
  // TODO: get rid of duplicated logic from: submit() and findSubmitter()
  this.event_6 = function(systemForm, eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions) {
    if (this.isSubmitted() || !this.isLoaded())
    return false;

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
  }

  this.getSubmitURL = function(topServiceId, threadServiceId, transactionId) {
    var url = this.encodeURL(this.getServletURL());
    url += '?transactionId=' + transactionId;
    if (topServiceId) 
      url += '&topServiceId=' + topServiceId;
    if (threadServiceId) 
      url += '&threadServiceId=' + threadServiceId;
    return url;
  }

  this.getActionSubmitURL = function(systemForm, actionId, actionTarget, actionParam, sync) {
    var url = this.getSubmitURL(systemForm.topServiceId.value, systemForm.threadServiceId.value, 'override');
    url += '&widgetActionPath=' + actionTarget;
    if (actionId)
      url += '&serviceActionHandler=' + actionId;
    if (actionParam)
      url += '&serviceActionParameter=' + actionParam;
    if (sync != undefined && !sync)
      url += '&sync=false';
    url += '&systemFormId=' + systemForm.id;
    return url;
  }

  this.action = function(element, actionId, actionTarget, actionParam, actionCallback, options, sync) {
    var systemForm = this.getSystemForm();
    return this.action_6(systemForm, actionId, actionTarget, actionParam, actionCallback, options, sync);
  }

  this.action_6 = function(systemForm, actionId, actionTarget, actionParam, actionCallback, options, sync) {
    if (window['prototype/prototype.js']) {
      options = Object.extend({
        method: 'get',
        onComplete: actionCallback,
        onException: AraneaPage.handleRequestException
      }, options);
      var url = this.getActionSubmitURL(systemForm, actionId, actionTarget, actionParam, sync);
      return new Ajax.Request(url, options);
    } else {
      araneaPage().getLogger().warn("Prototype library not accessible, action call cannot be made.");
    }
  }

  this.debug = function(message) {
    this.getLogger().debug(message);
  }
  
  /** 
   * Provides preferred way of overriding AraneaPage object functions. 
   * @param functionName name of AraneaPage function that should be overridden. 
   * @param f replacement function 
   */
  this.override = function(functionName, f) {
    this.getLogger().info("AraneaPage." +functionName + " was overriden.");
    this[functionName] = f;
  }
  
  /** 
   * Adds keepalive function f that is executed periodically after time 
   * milliseconds has passed 
   */
  this.addKeepAlive = function(f, time) {
    keepAliveTimers.add(setInterval(f, time));
  }

  /** Clears/removes all registered keepalive functions. */
  this.clearKeepAlives = function() {
    keepAliveTimers.forEach(function(timer) {clearInterval(timer);});
  }
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
          { method: 'get' }
      );
    } else {
      araneaPage().getLogger().warn("Prototype library not accessible, service keepalive calls cannot be made.");
    }
  };
}

// Random request id generator. Sent only with AA ajax requests.
// Currently only purpose of it is easier debugging (identifying requests).
AraneaPage.getRandomRequestId = function() {
  return Math.round(100000*Math.random());
}

// Page initialization function, should be called upon page load.
AraneaPage.init = function() {
  araneaPage().addSystemLoadEvent(Behaviour.apply);
}
/** @since 1.1 */
AraneaPage.findSystemForm = function() {
  araneaPage().setSystemForm($A(document.getElementsByTagName('form')).find(
    function(element) {
      return $(element).hasAttribute('arn-systemForm');
    }
  ));
}

function DefaultAraneaSubmitter(form) {
  var systemForm = form;

  this.event = function(element) {
    // event information
    var widgetId = araneaPage().getEventTarget(element);
    var eventId = araneaPage().getEventId(element);
    var eventParam = araneaPage().getEventParam(element);
    
    return this.event_4(systemForm, eventId, widgetId, eventParam);
  }
}

DefaultAraneaSubmitter.prototype.event_4 = function(systemForm, eventId, widgetId, eventParam) {
  systemForm.widgetEventPath.value = widgetId ? widgetId : "";
  systemForm.widgetEventHandler.value = eventId ? eventId : "";
  systemForm.widgetEventParameter.value = eventParam ? eventParam : "";

  araneaPage().setSubmitted();

  systemForm.submit();

  return false;
}

function DefaultAraneaOverlaySubmitter(form) {
  var systemForm = form;

  this.event = function(element) {
    // event information
    var widgetId = araneaPage().getEventTarget(element);
    var eventId = araneaPage().getEventId(element);
    var eventParam = araneaPage().getEventParam(element);

    systemForm.widgetEventPath.value = widgetId ? widgetId : "";
    systemForm.widgetEventHandler.value = eventId ? eventId : "";
    systemForm.widgetEventParameter.value = eventParam ? eventParam : "";

    araneaPage().setSubmitted();

   	Modalbox.show(
   	  systemForm.readAttribute('action') + '?overlay',
   	  {
   	    method: 'post',
   	    params: systemForm.serialize(true),
   	    overlayClose: false,
   	    width: 800,
   	    slideDownDuration: .0,
   	    slideUpDuration: .0,
   	    overlayDuration: .0,
   	    resizeDuration: .0,
   	    afterLoad: function(content) {
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
  }
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
  }
}

DefaultAraneaAJAXSubmitter.prototype.event_5 = function(systemForm, eventId, widgetId, eventParam, updateRegions) {
  systemForm.widgetEventPath.value = widgetId ? widgetId : "";
  systemForm.widgetEventHandler.value = eventId ? eventId : "";
  systemForm.widgetEventParameter.value = eventParam ? eventParam : "";

  var ajaxRequestId = AraneaPage.getRandomRequestId().toString();
  AraneaPage.showLoadingMessage();
  $(systemForm.id).request({
    parameters: {
      transactionId: 'override',
      ajaxRequestId: ajaxRequestId,
      updateRegions: updateRegions
    },
    onSuccess: function(transport) {
      AraneaPage.hideLoadingMessage();
      if (transport.responseText.substr(0, ajaxRequestId.length + 1) == ajaxRequestId + "\n") {
        araneaPage().getLogger().debug('Partial rendering: received successful response'
          + ' (' + transport.responseText.length + ' characters)'
          + ': ' + transport.status + ' ' + transport.statusText);
        AraneaPage.processResponse(transport.responseText);
        AraneaPage.init();
        araneaPage().onload();
      } else {
        araneaPage().getLogger().debug('Partial rendering: received erroneous response'
          + ' (' + transport.responseText.length + ' characters)'
          + ': ' + transport.status + ' ' + transport.statusText);
        // Doesn't work quite well for javascript and CSS, but fine for plain HTML
        document.write(transport.responseText);
        document.close();
      }
    },
    onFailure: function(transport) {
      AraneaPage.hideLoadingMessage();
      araneaPage().getLogger().debug('Partial rendering: received erroneous response'
        + ' (' + transport.responseText.length + ' characters)'
        + ': ' + transport.status + ' ' + transport.statusText);
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
}

Object.extend(AraneaPage, {
  /* Private fields */
  regionHandlers: new Hash(),
  loadingMessageId: 'aranea-loading-message',

  /* Public fields */

  /** @since 1.1 */
  loadingMessageContent: 'Loading...',
  /** @since 1.1 */
  loadingMessagePositionHack: false,

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
    while (!text.isEmpty()) {
      var key = text.readLine();
      var length = text.readLine();
      var content = text.readCharacters(length);
      if (this.regionHandlers[key]) {
        araneaPage().getLogger().debug('Region type: "' + key + '"');
        this.regionHandlers[key].process(content);
      } else {
        araneaPage().getLogger().error('Region type: "' + key + '" is unknown!');
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
    if (element) {
      if (this.loadingMessagePositionHack) {
        element.style.top = document.documentElement.scrollTop + 'px';
      }
      element.show();
      return;
    }
    var element = Builder.node('div', {id: this.loadingMessageId}, this.loadingMessageContent);
    document.body.appendChild(element);
    if (element.offsetTop) {
      // IE 6 does not support 'position: fixed' CSS attribute value
      this.loadingMessagePositionHack = true;
      element.style.position = 'absolute';
      element.style.top = document.documentElement.scrollTop + 'px';
    }
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
    if (systemForm.transactionId)
      systemForm.transactionId.value = content;
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
    var content = text.toString();
    if (mode == 'update') {
      $(id).update(content);
    } else if (mode == 'replace') {
      $(id).replace(content);
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
  regionClass: '.aranea-messages',
  regionTypeAttribute: 'arn-msgs-type',
  messageSeparator: '<br/>',

  initialize: function() {
  },

  process: function(content) {
    var messagesByType = content.evalJSON();
    this.updateRegions(messagesByType);
  },

  updateRegions: function(messagesByType) {
    $$(this.regionClass).each((function(region) {
      if (region.hasAttribute(this.regionTypeAttribute)) {
        var type = region.getAttribute(this.regionTypeAttribute);
        if (messagesByType[type]) {
          var messages = messagesByType[type];
          if (messages.size() > 0) {
            this.showMessageRegion(region, messages);
            return;
          }
        }
      } else {
        var messages = messagesByType.values().flatten();
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
    araneaPage().addSystemLoadEvent(processPopups);
  },

  openPopups: function(popups) {
    popups.each(function(popup) {
      addPopup(popup.popupId, popup.windowProperties, popup.url);
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
    if (systemForm.transactionId)
      systemForm.transactionId.value = 'inconsistent';
    return new DefaultAraneaSubmitter().event_4(araneaPage().getSystemForm());
  }
};
AraneaPage.addRegionHandler('reload', new AraneaPage.ReloadRegionHandler());


/* Initialize new Aranea page.  */
/* Aranea page object is accessible in two ways -- _ap and araneaPage() */
_ap = new AraneaPage();
function araneaPage() { return _ap; }
_ap.addSystemLoadEvent(AraneaPage.init);
_ap.addSystemLoadEvent(AraneaPage.findSystemForm);

window['aranea.js'] = true;

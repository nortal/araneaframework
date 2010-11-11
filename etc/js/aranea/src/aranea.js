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
 * Exactly one Aranea.Page object is present on each page served by Aranea and contains common functionality for setting
 * page related variables, events and functions.
 * <p>
 * Aranea object which provides namespace for objects created/needed by different modules.
 *
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 */
var Aranea = window.Aranea || {};

/**
 * Dummy implementation of logger. If you want to actually use logging then also include aranea-util.js after this file.
 * @since 2.0
 */
Aranea.Logger = {
	trace: Prototype.emptyFunction,
	debug: Prototype.emptyFunction,
	info: Prototype.emptyFunction,
	warn: Prototype.emptyFunction,
	error: Prototype.emptyFunction,
	fatal: Prototype.emptyFunction,
	setLogger: Prototype.emptyFunction
};

Aranea.Data = {

	/**
	 * URL of aranea dispatcher servlet serving current page. This is by default set by Aranea JSP ui:body tag.
	 */
	servletURL: null,

	/**
	 * Whether get*SubmitURL() method returns an absolute URL or just the part starting from the context.
	 */
	absoluteURLs: true,

	/**
	 * Indicates whether the page is completely 			 or not. Page is considered to be loaded when all system "onload"
	 * events have completed execution.
	 */
	loaded: false,

	/**
	 * The application locale - should be used only for server-side reported locale.
	 */
	locale: { lang: '', country: '' },

	/**
	 * Indicates whether some form on page is (being) submitted already by traditional HTTP request.
	 */
	submitted: false,

	/**
	 * The resolved system-form that is set on page load.
	 */
	systemForm: null,

	/**
	 * Timer that executes keep-alive calls, if any.
	 */
	keepAliveTimers: [],

	/**
	 * The flag that determines whether background validation is used by for all forms (FormWidgets) in the application.
	 */
	backgroundValidation: false,

	/* Private fields */
	loadingMessagePositionHack: false,

	regionHandlers: $H(),
	
	expiredPagedStateWarningShown: false,
	
	expiredPageRedirectInProgress: false
};

Aranea.Page = {

	/**
	 * The content for the loading message that is shown during AJAX requests.
	 * 
	 * @since 1.1
	 */
	LOADING_MESSAGE_CONTENT: 'Loading...',

	/**
	 * The ID of the loading message so that it would be easy to access it, show or hide.
	 * 
	 * @since 1.1
	 */
	LOADING_MESSAGE_ID: 'aranea-loading-message',
	
	EXPIRED_PAGE_STATE_WARNING : 'Back navigation is disallowed for safety reasons',

	/**
	 * Whether to reload page when AJAX response contains no document regions (page is not updated). That behaviour
	 * could indicate some errors on server-side.
	 * 
	 * @since 1.2
	 */
	RELOAD_ON_NO_DOCUMENT_REGIONS: true,

	/**
	 * Space-separated values for system-form lookup by ID. These IDs are processed in the same order, the first
	 * system-form element found will be used.
	 * 
	 * @since 2.0
	 */
	SYSTEM_FORM_IDS: 'aranea-overlay-form aranea-form',

	/**
	 * This function is called by Aranea.Page.onload() to initialize Aranea internals while Aranea.Page.onload is mostly
	 * intended to be overridden by custom projects.
	 */
	onLoad: function() {
		Aranea.Logger.profile('Aranea.Page.onLoad()');

		Aranea.Page.findSystemForm();
		Aranea.Data.loaded = true;

		Aranea.Logger.info('Page loaded and basic initialization done. Firing event "aranea:loaded"...');

		document.fire('aranea:loaded');
		document.stopObserving('aranea:loaded');

		Aranea.Logger.profile('Aranea.Page.onLoad()');
	},

	onUpdate: function(data) {
		Aranea.Logger.profile('Aranea.Page.onUpdate()');
		Aranea.Page.findSystemForm();
		Aranea.Logger.info('Page was updated. Firing event "aranea:updated"...');
		document.fire('aranea:updated');
		Aranea.Logger.profile('Aranea.Page.onUpdate()');
	},

	onUnload: function(data) {
		document.fire('aranea:unloaded');
		Aranea.Logger.debug("Unloaded Aranea scripts!");
		window.Aranea = undefined;
	},

	// Users must provide only form encoding!
	setSystemFormEncoding: function(encoding) {
		if (Aranea.Data.loaded) {
			if (Aranea.Data.systemForm.readAttribute('enctype') != encoding) {
				Aranea.Data.systemForm.writeAttribute({ 'enctype': encoding, 'encoding': encoding });
				Aranea.Logger.debug('System-form encoding was set to "' + encoding + '".');
			}
		} else {
			var type = Aranea.Data.submitted ? 'aranea:updated' : 'aranea:loaded';
			document.observe(type, Aranea.Page.setSystemFormEncoding.curry(encoding));
		}
	},

	encodeURL: function(url) {
		return url;
	},

	/**
	 * Invokes server-side action listener by performing XMLHttpRequest with correct parameters.
	 * @param actionId Action identifier sent to the server.
	 * @param actionTarget Action target identifier (mostly, you'll want to use ${widget id} in JSP).
	 * @param actionParam Action parameter.
	 * @param actionCallback Callback (JS function) executed when action response arrives.
	 * @param options XMLHttpRequest options, see: http://prototypejs.org/api/ajax/options.
	 * @param sync Boolean: whether this action is synchronized on server-side or not (default is synchronized, true).
	 * @param extraParams More parameters to the request, e.g. "p1=v1&p2=v2" or {p1: 'v1', p2: 'v2'}.
	 * @param element form or sub-element of form tag that triggers the action request.
	 */
	action: function(actionId, actionTarget, actionParam, extraParams, actionCallback, options, sync, element) {
		if (!actionId || actionId.blank()) throw ('Aranea.Page.action: parameter "actionId" is required!');
		if (!actionTarget || actionTarget.blank()) throw ('Aranea.Page.action: parameter "actionTarget" is required!');

		element = Aranea.Page.findSystemForm(element);
		options = Object.extend({
			method: 'post',
			onComplete: actionCallback,
			onException: this.handleRequestException
		}, options || {});

		var url = this.getActionSubmitURL(actionId, actionTarget, actionParam, sync, extraParams, element);
		return new Ajax.Request(url, options);
	},

	/**
	 * Sends an event to the server-side. The event type (plain submit or AJAX) is determined automatically using the
	 * given parameters or given element that has event data.
	 * <p>
	 * The expected parameters are:
	 * 1) event(formElement) where formElement has event data.
	 * 2) event(eventId, widgetId, [eventParam], [eventCondition], [eventUpdateRgns], [form]) where
	 *    parameters in brackets are optional.
	 * 
	 * @param formElement The form-element that contains event data.
	 * @param eventId The event identifier sent to the server
	 * @param widgetId The event target identifier (path to widget)
	 * @param eventParam An optional event parameter.
	 * @param eventCondition An optional boolean expression or closure that, when false, suppresses request.
	 * @param eventUpdateRgns Optional identifiers for regions that should be returned from server.
	 * @param form An optional form that will be submitted with the data. Defaults to the current systemForm.
	 * @return Always false.
	 */
	event: function() {
		return Aranea.Page.invokeEvent(null, arguments);
	},

	/**
	 * Sends an AJAX event to the server-side.
	 * <p>
	 * The expected parameters are:
	 * 1) ajax(formElement, eventUpdateRgns) where formElement has event data.
	 * 2) ajax(eventId, widgetId, [eventParam], [eventCondition], eventUpdateRgns, [form]) where parameters in
	 *    brackets are optional.
	 * 
	 * @param formElement The form-element that contains event data.
	 * @param eventId The event identifier sent to the server
	 * @param widgetId The event target identifier (path to widget)
	 * @param eventParam An optional event parameter.
	 * @param eventCondition An optional boolean expression or closure that, when false, suppresses request.
	 * @param eventUpdateRgns Optional identifiers for regions that should be returned from server.
	 * @param form An optional form that will be submitted with the data. Defaults to the current systemForm.
	 * @return Always false.
	 */
	ajax: function() {
		return Aranea.Page.invokeEvent(Aranea.Page.Submitter.TYPE_AJAX, arguments);
	},

	overlayAjax: function() {
		return Aranea.Page.invokeEvent(Aranea.Page.Submitter.TYPE_OVERLAY, arguments);
	},

	submit: function() {
		return Aranea.Page.invokeEvent(Aranea.Page.Submitter.TYPE_PLAIN, arguments);
	},

	invokeEvent: function(type, args) {
		try {
			Aranea.Logger.profile('Aranea.Page.invokeEvent()');
			if (!Aranea.Data.submitted && Aranea.Data.loaded) {
				var data = Aranea.Page.Form.getEventData(type, args);

				if (data.preconditionFailed) {
					Aranea.Logger.debug('Request cancelled because event precondition returned false.');
				} else {
					this.findSubmitter(data).event(data);
				}
				return false; // Return false
			}
		} catch (e) {
			Aranea.Logger.error('An error occurred during "event" request: ' + Object.inspect(e), e);
		} finally {
			Aranea.Logger.profile('Aranea.Page.invokeEvent()');
		}
	},

	/**
	 * This function can be overwritten to support additional submit methods.
	 * It is called by event() to determine the appropriate form submitter.
	 */
	findSubmitter: function(data) {
		var type = Object.isString(data) ? data : data.type;
		if (type == Aranea.Page.Submitter.TYPE_PLAIN) {
			return new Aranea.Page.Submitter.Plain();
		} else if (type == Aranea.Page.Submitter.TYPE_AJAX) {
			return new Aranea.Page.Submitter.AJAX();
		} else if (type == Aranea.Page.Submitter.TYPE_OVERLAY) {
			return new Aranea.Page.Submitter.Overlay();
		}
		throw('Unknown event type ("' + type + '"). Could not find appropriate submitter!');
	},

	/**
	 * Returns URL that can be used to invoke full HTTP request with some predefined request parameters.
	 * 
	 * getSubmitURL()
	 * getSubmitURL(formElement)
	 * getSubmitURL([formElement], 'param1=value1&param2=value2&param3=value3' })
	 * getSubmitURL([formElement], { param1: value1, param2: value2, param3: value3 })
	 * 
	 * @param form An optional form to read form-specific parameters and append them to the request.
	 * @param params Optional parameters to append to the request. May be a string ('a=b&c=d&e=f') or object
	 *        ({ a: 'b', c: 'd', e: 'f' })
	 * @return The encoded URL string with all provided parameters.
	 */
	getSubmitURL: function() {
		var params = $H();

		if (arguments.length >= 1) {
			var index = 0;

			// Read the form element parameter:
			if (Object.isElement(arguments[index])) {
				params = Aranea.Page.getFormParameters(arguments[0]);
				index++;
			}

			// Read the URL parameters:
			if (index < arguments.length && arguments[index]) {
				var param = arguments[index];
				params.update(Object.isString(param) ? param.parseQuery() : $H(param));
			}
		}

		if (!Aranea.Data.servletURL) {
			throw('Aranea.Data.servletURL must not be empty!');
		}

		var urlStr = Aranea.Data.servletURL;
		if (!Aranea.Data.absoluteURLs) {
			urlStr = urlStr.gsub(/.*:\/\/.*?(?=\/)/, ''); // Returns URL starting with "/[context]..."
		}

		var url = Aranea.Page.encodeURL(urlStr);

		if (params.size() > 0) {
			var separator = (url || '').indexOf('?') < 0 ? '?' : '&';
			url += separator + params.toQueryString();
		}

		return url;
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
	getActionSubmitURL: function(actionId, actionTarget, actionParam, sync, extraParams, systemForm) {
		if (systemForm == null) {
			systemForm = Aranea.Data.systemForm;
		} else if (!systemForm.match('form')) {
			systemForm = Aranea.Page.findSystemForm(systemForm, false);
		}

		var params = {
			araTransactionId: 'override',
			araServiceActionPath: actionTarget,
			araServiceActionHandler: actionId,
			araServiceActionParameter: actionParam
		};

		if (sync == false) {
			params.araSync = false;
		}

		if (Object.isString(extraParams)) {
			extraParams = extraParams.parseQuery();
		}
		if (extraParams != null) {
			Object.extend(params, extraParams);
		}

		return this.getSubmitURL(params);
	},

	getFormParameters: function(form) {
		if (form == null) {
			throw('The "form" parameter must not be null!');
		}
		return $H({
			araTransactionId: Aranea.Page.Form.getTransactionId(form),
			araTopServiceId: Aranea.Page.Form.getTopServiceId(form),
			araThreadServiceId: Aranea.Page.Form.getThreadServiceId(form),
			araClientStateId: Aranea.Page.Form.getClientStateId(form)
		});
	},

	/**
	 * Adds keep-alive function f that is executed periodically after time milliseconds has passed
	 */
	addKeepAlive: function(f, time) {
		Aranea.Data.keepAliveTimers.push(window.setInterval(f, time));
 	},

	/**
	 * Clears/removes all registered keep-alive functions.
	 */
	clearKeepAlives: function() {
		Aranea.Data.keepAliveTimers.each(function(timer) {
			window.clearInterval(timer);
		}).clear();
	},

	/**
	 * Returns the default keep-alive function -- to make periodical requests to expiring thread or top level services.
	 */
	getDefaultKeepAlive: function(topServiceId, threadServiceId, keepAliveKey) {
		return function() {
			var params = {
				araTopServiceId: topServiceId,
				araThreadServiceId: threadServiceId,
				araTransactionId: 'override',
				keepAliveKey: keepAliveKey,
				sync: false
			};
			var url = Aranea.Page.getSubmitURL(params);
			Aranea.Logger.debug('Sending async service keepalive request to URL "' + url +'".');
			new Ajax.Request(url, { method: 'post' });
		};
	},

	/**
	 * Searches for widget marker around the given element. If found, returns the marker DOM element, else returns null.
	 */
	findWidgetMarker: function(element) {
		return $(element).up('.widgetMarker');
	},

	/**
	 * Random request ID generator. Sent only with XMLHttpRequests which apply to
	 * certain update regions. Currently its only purpose is easier debugging
	 * (identifying requests).
	 */
	getRandomRequestId: function() {
		return Math.round(100000 * Math.random()).toString();
	},

	/**
	 * Provides the URL for fetching import file with given name. The name must not be null, and may contain slashes.
	 * @since 1.1
	 */
	getFileImportString: function(fileName) {
		if (!fileName) {
			throw('The fileName parameter must not be empty!');
		} else if (!Aranea.Data.servletURL) {
			throw('Aranea.Data.servletURL must not be empty!');
		}

		var str = $A([ Aranea.Data.servletURL ]);
		if (!Aranea.Data.servletURL.endsWith('/')) {
			str.push('/');
		}
		str.push('fileimporter');
		if (!fileName.startsWith('/')) {
			str.push('/');
		}
		str.push(fileName);
		return str.join('');
	},

	/**
	 * Searches for system form in HTML page and registers it in the current
	 * Aranea.Page object as active systemForm.
	 * @return The active system form that was found.
	 * @since 1.1
	 */
	findSystemForm: function(element, changeData) {
		element = $(element);
		var result = null;

		if (!Object.isElement(element)) {
			$w(Aranea.Page.SYSTEM_FORM_IDS).find(function(id) { return result = $(id) });
		} else if (element.match('form') && Aranea.Page.SYSTEM_FORM_IDS.indexOf(element.id) >= 0) {
			result = element;
		} else {
			result = element;
			var targetIds = Aranea.Page.SYSTEM_FORM_IDS;
			var validId = !String.interpret(result.id).empty() && targetIds.indexOf(result.id) >= 0;
			while (result.tagName && (!validId || !result.match('form'))) {
				result = result.up();
				validId = result.tagName && !result.id.empty() && targetIds.indexOf(result.id) >= 0;
			}
			result = result.tagName != null ? result : null;
		}

		if (result == null) {
			throw('findSystemForm: Could not find Aranea system form that has one of those IDs: "'
					+ Aranea.Page.SYSTEM_FORM_IDS + '". Make sure that the form exists, and, if element was given, '
					+ 'element is inside a form.')
		} else if (changeData != false) {
			Aranea.Data.systemForm = result;
			Aranea.Logger.info("Resolved system-form: " + Aranea.Data.systemForm.inspect() + '.');
		}

		return result;
	},

	/**
	 * Exception handler that is invoked on Ajax.Request errors.
	 *
	 * @since 1.1
	 */
	handleRequestException: function(request, exception) {
		Aranea.Data.loaded = true;
		throw exception;
	},

	/**
	 * Create or show loading message at the top corner of the document. Called before initiating an update-regions
	 * Ajax.Request.
	 *
	 * @since 1.1
	 */
	showLoadingMessage: function() {
		var element = $(Aranea.Page.LOADING_MESSAGE_ID);
		if (!element) {
			element = $(Aranea.Page.buildLoadingMessage());
			if (!element) return;
			$(document.body).insert(element.hide());
		}
		Aranea.Page.positionLoadingMessage(element);
		element.show();
		element = null;
	},

	/**
	 * Hide loading message. Called after the completion of update-regions Ajax.Request.
	 *
	 * @since 1.1
	 */
	hideLoadingMessage: function() {
		var element = $(Aranea.Page.LOADING_MESSAGE_ID);
		if (element) element.hide();
		element = null;
	},

	/**
	 * Build loading message. Called when an existing message element is not found.
	 *
	 * @since 1.1
	 */
	buildLoadingMessage: function() {
		return new Element('div', { id: Aranea.Page.LOADING_MESSAGE_ID }).update(Aranea.Page.LOADING_MESSAGE_CONTENT);
	},

	/**
	 * Perform positioning of loading message (if needed in addition to CSS). Called before making the message element
	 * visible. This implementation provides workaround for IE 6, which doesn't support <code>position: fixed</code> CSS
	 * attribute; the element is manually positioned at the top of the document. If you don't need this, overwrite this
	 * with an empty function:
	 * <code>Object.extend(Aranea.Page(), { positionLoadingMessage: Prototype.emptyFunction });</code>
	 *
	 * @since 1.1
	 */
	positionLoadingMessage: function(element) {
		if (this.loadingMessagePositionHack || element.offsetTop) {
			this.loadingMessagePositionHack = true;
			$(element).setStyle({
					position: 'absolute',
					top: document.documentElement.scrollTop + 'px'
			});
		}
	},
	
	/** Gets cookie value, borrowed from http://www.elated.com/articles/javascript-and-cookies tutorial */
	getCookie: function(name) {
      var results = document.cookie.match ( '(^|;) ?' + name + '=([^;]*)(;|$)' );
      return results ? unescape(results[2]) : null;
	},
	
	/** Returns non-negative integer when currently shown page state is among the ones considered valid server-side. */
	isStateValid: function(clientStateId) {
      var threadId = Aranea.Data.systemForm.araThreadServiceId.value;
      var topId = Aranea.Data.systemForm.araTopServiceId.value;
      var statesCookieValue = Aranea.Page.getCookie("" + threadId + "_araStates");
      var found = -1;

      if (statesCookieValue) { // always exists, unless cookies disabled or deleted somehow
        var legalStates = statesCookieValue.split("|");
        // there is a special case where current state is invalid but we should not really report it
        // as states have just been expired and there is only one valid state -- which has not yet
        // activated -- page loading is not complete. In that case, it is contained twice in the cookie
        // like AAA|AAA
        
        if (legalStates.length == 2) {
          var currStateId = clientStateId;
          if (legalStates[0] == legalStates[1]) {
        	  return 0;
          }
        }
        
        for (var c = 0; c < legalStates.length; c++) {
          if (legalStates[c] == clientStateId) {
            found = c;
            break;
          }
        }
      }
      
	  return found;
	},
	
	/** Tests whether the state in the system form is among valid ones, warns user about expiration when not. */
	testStateValidity: function() {
	  if (window.console) {
		  window.console.debug("testing state validity " + new Date());
	  }
      var found = Aranea.Page.isStateValid(Aranea.Data.systemForm.araClientStateId.value);
      if (found < 0 && !Aranea.Data.expiredPagedStateWarningShown && !Aranea.Data.expiredPageRedirectInProgress) {
        // if that value does not exist, we are in wrong moment of time
        if (Aranea.Data.systemForm.araClientStateId.value && !Aranea.Data.submitted) {
          Aranea.Page.warnExpiredPageState();
        }
      }
	},
	
	/** shows message to the end user about expired state navigation on client side */
	warnExpiredPageState: function() {
		Aranea.Data.expiredPagedStateWarningShown = true;
		alert(Aranea.Page.EXPIRED_PAGE_STATE_WARNING);
		Aranea.Data.expiredPagedStateWarningShown = false;
		Aranea.Data.expiredPageRedirectInProgress = true;
		Aranea.Data.systemForm.araClientStateId.value = "invalid_forever"; 
		Aranea.Page.redirectFromExpiredPage(Aranea.Data.systemForm.araTopServiceId.value, Aranea.Data.systemForm.araThreadServiceId.value);
		// theoretically we should set expiredPageRedirectInProgress to false -- practically this will not work reliably,
		// .. so we will not
	},
	
	/** Performs redirect from expired state (detection from client side). */
	redirectFromExpiredPage: function(topId, threadId) {
      var params = {
        araTopServiceId: topId,
        araThreadServiceId: threadId,
      };
      document.location.href = Aranea.Page.getSubmitURL(params);
	},
	
	// http://www.quirksmode.org/js/cookies.html
	createCookie: function(name,value,days, path) {
		if (days) {
			var date = new Date();
			date.setTime(date.getTime()+(days*24*60*60*1000));
			var expires = "; expires="+date.toGMTString();
		}
		else var expires = "";
		document.cookie = name+"="+value+expires+"; path=" + path;
	},
	
	// a hack to normalize cookie, after request processing has been completed after expiration
	// see StandardStateVersioningFilterWidget#addStatesCookie
	normalizeStateCookie: function() {
	   var threadId = Aranea.Data.systemForm.araThreadServiceId.value;
	   var statesCookieValue = Aranea.Page.getCookie("" + threadId + "_araStates");
	   if (statesCookieValue) {
		   var legalStates = statesCookieValue.split("|");
		   if (legalStates.length == 2 && legalStates[0] == legalStates[1]) {
			   var servletUrlSplit = Aranea.Data.servletURL.split('/');
			   var path = '/' + servletUrlSplit[servletUrlSplit.length - 2];
			   Aranea.Page.createCookie("" + threadId + "_araStates","",-1, path); // delete existing cookie
			   Aranea.Page.createCookie("" + threadId + "_araStates", "" + legalStates[0], null, path);
		   }
	   }
	}
};

/**
 * A common callback for submitters. The callback handles common data manipulation and validation.
 * This callback should be used to add some custom features to submit data or submit response.
 * @since 1.2.2
 */
Aranea.Page.Request = {

	/**
	 * A callback that is checked to enable or disable submit.
	 */
	isAllowed: function(data) {
		return true;
	},

	/**
	 * A callback that is called before each submit (no matter whether it is AJAX or not). This method includes default
	 * behaviour.
	 */
	before: function(data) {
		Aranea.Data.loaded = false;
		Aranea.Data.submitted = true;

		document.fire('aranea:beforeRequest', data);

		Aranea.Page.Request.customBefore(data);
		Aranea.Page.Form.writeToForm(data); // Write event data to form so that it would be sent to server-side.
	},

	/**
	* A callback that is called after each submit (no matter whether it is AJAX or not). This method includes default
	* behaviour.
	*/
	after: function(data) {
		Aranea.Data.submitted = false;
		Aranea.Data.loaded = true;

		document.fire('aranea:afterRequest', data);

		Aranea.Page.Request.customAfter(data);
		Aranea.Page.onUpdate(data);
	},

	customBefore: function(data) {
		if (data.type == Aranea.Page.Submitter.TYPE_AJAX) {
			Aranea.Logger.debug('Showing the loading message.');
			Aranea.Page.showLoadingMessage();
		}

		// Copy the content of rich editors to corresponding HTML text-inputs/text-areas.
		if (window.tinyMCE) {
			window.tinyMCE.triggerSave();
		}
	},

	customAfter: function(data) {
		if (data.type == Aranea.Page.Submitter.TYPE_AJAX) {
			Aranea.Logger.debug('Hiding the loading message.');
			Aranea.Page.hideLoadingMessage();
		}
	}
};

// Here are three submitter classes for the standard HTTP submit, AJAX update region submit, and AJAX overlay submit.
Aranea.Page.Submitter = {
	TYPE_PLAIN: 'submit',
	TYPE_AJAX: 'ajax',
	TYPE_OVERLAY: 'overlay'
};

/**
 * The standard HTTP submitter. Whether it's POST or GET depends on the "method" attribute of the "form" element. (The
 * default is GET submit.)
 */
Aranea.Page.Submitter.Plain = Class.create({

	TYPE: Aranea.Page.Submitter.TYPE_PLAIN,

	event_plain: function(args) {
		return this.event(Aranea.Page.Form.getEventData(this.TYPE, args));
	},

	event: function(eventData) {
		var result = false;
		if (eventData.isSubmitAllowed()) {
			Aranea.Data.systemForm.fire('aranea:beforeEvent', eventData);
			eventData.beforeRequest();

			result = this.event_core(eventData);

			// eventData.afterRequest();
			// Aranea.Data.systemForm.fire('aranea:afterEvent', eventData);
		}
		return Object.isUndefined(result) ? false : result;
	},

	event_core: function(data) {
		data.form.submit();
	}
});

/**
 * This class extends the default submitter, and overrides event() to initiate an AJAX request and to process result
 * specifically for the overlay mode. It expects that aranea-modalbox.js is successfully loaded.
 */
Aranea.Page.Submitter.Overlay = Class.create(Aranea.Page.Submitter.Plain, {

	TYPE: Aranea.Page.Submitter.TYPE_OVERLAY,

	event_core: function(data) {
		var submitParams = data.form.serialize(true);

		submitParams.araTransactionId = 'override';

		// For Aranea.History requests we need to set transaction ID to an inconsistent value.
		if (Aranea.History && data.eventUpdateRgns.startsWith(Aranea.History.UPDATE_REGION_ID)) {
			submitParams.araTransactionId = 'inconsistent';
		}
		if (data.eventUpdateRgns) {
			submitParams.updateRegions = data.eventUpdateRgns;
		}

		Aranea.ModalBox.update({ params: submitParams });
	},

	event: function(eventData) {
		Aranea.Logger.profile('Aranea.Page.Submitter.Overlay.event()');

		var result = false;
		if (eventData.isSubmitAllowed()) {
			Aranea.Data.systemForm.fire('aranea:beforeEvent', eventData);
			eventData.beforeRequest();

			result = this.event_core(eventData);

			var afterRequest = function(eventData) {
				eventData.afterRequest();
				Aranea.Data.systemForm.fire('aranea:afterEvent', eventData);
				Aranea.Logger.profile('Aranea.Page.Submitter.Overlay.event()');
			};

			afterRequest.delay(0.5, eventData); // Add some delay here, because loading overlay takes time.
		}
		return Object.isUndefined(result) ? false : result;
	}
}),

Aranea.Page.Submitter.AJAX = Class.create(Aranea.Page.Submitter.Plain, {

	TYPE: Aranea.Page.Submitter.TYPE_AJAX,

	event: function(eventData) {
		Aranea.Logger.profile('Aranea.Page.Submitter.AJAX.event()');

		if (eventData.isSubmitAllowed(eventData)) {
			eventData.beforeRequest();

			this.data = eventData;

			var ajaxRequestId = Aranea.Page.getRandomRequestId();
			var neededAraClientStateId = eventData.form.araClientStateId ? $F(eventData.form.araClientStateId) : null;
			var neededAraTransactionId = 'override';

			// For Aranea.History requests we need to set transaction ID to an inconsistent value.
			if (Aranea.History && eventData.eventUpdateRgns.startsWith(Aranea.History.UPDATE_REGION_ID)) {
				neededAraTransactionId = 'inconsistent';
			}

			$(eventData.form.id).request({
				parameters: this.getAjaxParameters(neededAraTransactionId, ajaxRequestId,
						eventData.eventUpdateRgns, neededAraClientStateId),
				onSuccess: this.onAjaxSuccess.curry(ajaxRequestId).bind(this),
				onComplete: this.onAjaxComplete.bind(this),
				onFailure: this.onAjaxFailure.bind(this),
				onException: this.onAjaxException.bind(this)
			});
		} else {
			Aranea.Logger.profile('Aranea.Page.Submitter.AJAX.event()');
		}
		return false;
	},

	afterRequest: function() {
		this.data.afterRequest();
	},

	getAjaxParameters: function(neededAraTransactionId, ajaxRequestId, updateRegions, neededAraClientStateId) {
		return {
			araTransactionId: neededAraTransactionId,
			ajaxRequestId: ajaxRequestId,
			updateRegions: updateRegions,
			araClientStateId: neededAraClientStateId
		};
	},

	onAjaxSuccess: function(ajaxRequestId, transport) {
		if (transport.responseText.substr(0, ajaxRequestId.length + 1) == ajaxRequestId + '\n') {
			var logmsg = ['Partial rendering: received successful response (', transport.responseText.length,
				' characters): ', transport.status, ' ', transport.statusText].join('');
			Aranea.Logger.debug(logmsg);
			Aranea.Page.Submitter.AJAX.processResponse(transport.responseText);
		} else {
			var logmsg = ['Partial rendering: received erroneous response (', transport.responseText.length,
				' characters): ', transport.status, transport.statusText].join('');
			Aranea.Logger.debug(logmsg);
			// Doesn't work quite well for javascript and CSS, but fine for plain HTML
			document.write(transport.responseText);
			document.close();
		}
	},

	onAjaxComplete: function(transport) {

		// because prototype's Element.update|replace delay execution of scripts,
		// immediate execution of onload() is not guaranteed to be correct
		var f = function() {
			this.afterRequest();

			Aranea.Page.Submitter.AJAX.ResponseHeaderProcessor(transport);

			if (Aranea.ModalBox) {
				Aranea.ModalBox.afterUpdateRegionResponseProcessing(Aranea.Data.systemForm);
			}

			Aranea.Logger.profile('Aranea.Page.Submitter.AJAX.event()');
		}.bind(this);

		// force the delay here
		setTimeout(f, Aranea.Page.Submitter.AJAX.contentUpdateWaitDelay);
	},

	onAjaxFailure: function(transport) {
		Aranea.Logger.debug(['Partial rendering: received erroneous response (',
							 transport.responseText.length, ' characters): ',
							 transport.status, ' ', transport.statusText].join(''));

		// Doesn't work quite well for javascript and CSS, but fine for plain HTML
		document.write(transport.responseText);
		document.close();
		this.afterRequest();
	},

	onAjaxException: function(request, exception) {
		Aranea.Page.handleRequestException(request, exception);
		this.afterRequest();
		Aranea.Logger.profile('Aranea.Page.Submitter.AJAX.event()');
	}
});

Object.extend(Aranea.Page.Submitter.AJAX, {

	/**
	 * The delay after which Ajax.Request onComplete expects all the DOM updates to have taken place, in milliseconds.
	 * @since 1.1
	 */
	contentUpdateWaitDelay: 30,

	/**
	 * @since 1.2
	 */
	ResponseHeaderProcessor: function(transport) {},

	/**
	 * Process response of an update-regions AJAX request. Should be called only on successful response. Invokes
	 * registered region handlers.
	 * @param responseText The AJAX response text.
	 * @since 1.1
	 */
	processResponse: function(responseText) {
		var counter = $H();
		var hasRegions = false;
		var log = Aranea.Logger;
		var handlers = Aranea.Data.regionHandlers;

		new Aranea.Util.AjaxResponse(responseText, true).each(function(key, content, length) {
			counter[key] = (counter[key] || 0) + 1;
			var handler = handlers.get(key);
			if (handler && handler.process) {
				handler.process(content);
				hasRegions = true;
			} else {
				throw('Region type: "' + key + '" (content length: ' + length + ') is unknown: no handler for it!');
			}
		});

		Aranea.Data.receivedRegionCounters = counter;

		if (Aranea.Page.RELOAD_ON_NO_DOCUMENT_REGIONS && !hasRegions) {
			Aranea.Logger.debug('No document regions were received, forcing a reload of the page');
			if (handlers.get('reload')) {
				handlers.get('reload').process();
			} else {
				Aranea.Logger.error('No handler is registered for "reload" region, unable to force page reload!');
			}
		}
	}
});

Aranea.Page.RegionHandler = {

	/**
	 * Region handler that updates transaction id of system form.
	 *
	 * @since 1.1
	 */
	TransactionId: {

		process: function(content) {
			var systemForm = Aranea.Data.systemForm;
			if (Aranea.Data.systemForm.araTransactionId) {
				systemForm.araTransactionId.value = content;
				Aranea.Logger.debug('Transaction ID region: new value is "' + content + '".');
			}
		}
	},

	/**
	 * The Region handler that updates DOM element content.
	 *
	 * @since 1.1
	 */
	Document: {

		process: function(content) {
			var text = new Aranea.Util.AjaxResponse(content);
			var length = parseInt(text.readLine());
			var properties = text.readCharacters(length).evalJSON();

			var id = properties.id;
			var mode = properties.mode;
			var domContentString = text.toString();
			var length = domContentString.length;


			if (mode == 'update') {
				Aranea.Logger.debug('Document region: updating "' + id + '" (' + length + ' characters)...');
				$(id).update(domContentString);
			} else if (mode == 'replace') {
				Aranea.Logger.debug('Document region: replacing "' + id + '" (' + length + ' characters)...');
				$(id).replace(domContentString);
			} else {
				Aranea.Logger.error('Document region: mode "' + mode + '" (' + length + ' characters) is unknown');
			}
		}
	},

	/**
	 * Region handler that updates the messages area.
	 *
	 * @since 1.1
	 */
	Message: {

		/**
		 * The selector that is used for resolving elements where to add messages.
		 * @since 1.1
		 */
		regionClass: '.aranea-messages',

		/**
		 * The attribute that the target element must have to identify the type of messages the target contains. 
		 * @since 1.1
		 */
		regionTypeAttribute: 'arn-msgs-type',

		/**
		 * @since 1.1
		 */
		messageSeparator: '<br/>',

		process: function(content) {
			var messagesByType = $H(content.evalJSON());
			Aranea.Logger.debug('Message region: replacing messages with ' + messagesByType.size() + ' messages...');
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
	},

	/**
	 * Region handler that opens popup windows.
	 *
	 * @since 1.1
	 */
	Popup: {

		process: function(content) {
			this.openPopups(content.evalJSON());
			document.observe('aranea:updated', Aranea.Popup.processPopups);
		},

		openPopups: function(popups) {
			popups.each(function(popup) {
				Aranea.Logger.debug('Popup region: registering popup "' + popup.popupId + '" (' + popup.url + ')...');
				Aranea.Popup.addPopup(popup.popupId, popup.url, popup.windowProperties);
			});
		}
	},

	/**
	 * Region handler that forces a reload of the page by submitting the system
	 * form.
	 *
	 * @since 1.1
	 */
	Reload: {

		process: function(content) {
			Aranea.Logger.debug('Reload region: doing page reload (content: ' + content.length + ' characters)...');

			if (Aranea.Data.systemForm.araTransactionId) {
				Aranea.Data.systemForm.araTransactionId.value = 'inconsistent';
			}

			// if current systemform is overlayed, reload only overlay
			if (Aranea.Data.systemForm.araOverlay) {
				return new Aranea.Page.Submitter.Overlay().event_plain();
			}

			/* Actually we should enter the domain of hackery.
			 * Reloads can break the back button in a subtle way, because the new state identifier
			 * would have been already loaded from AJAX response header to the system form. Thus if
			 * one comes back to a page after AJAX request + full reload, the page shown
			 * would not match the state identifier actually present in the system form.
			 * If one just did window.location.href=window.location.href, no history navigation event
			 * is generated because URL would not change. And the transactionId would have to be
			 * encoded in the URL because NULL transactionId is consistent.
			 * So the redirect should:
			 *    set transactionId to non-null inconsistent value, but may not affect the system form.
			 * Which is not satisfied by current implementation here.
			 */
			return new Aranea.Page.Submitter.Plain().event_plain();
		}
	},

	FormBackgroundValidation: {

		process: function(content) {
			if (!content) throw ('FormBackgroundValidation: content parameter is required!');

			var result = content.evalJSON();

			Aranea.Logger.debug('Form background validation region: form element "' + result.formElementId
					+ '" is ' + (result.valid ? 'VALID' : 'NOT VALID'));

			var formelement = $(result.formElementId);
			var inputSpan = this.getParentSpan(formelement);
			var labelSpan = this.getLabelSpan(formelement);

			Aranea.UI.markFEContentStatus(result.valid, inputSpan);
			Aranea.UI.markFEContentStatus(result.valid, labelSpan);

			if (result.valid || result.clientRenderText) {
				$$('[class~="aranea-formelementerrors ' + result.formElementId + '"]').invoke('remove');
			}

			if (result.clientRenderText) {
				// invoke the supplied client side error-message renderer
				result.clientRenderText.evalScripts();
			}

			result = formelement = inputSpan = labelSpan = null;
		},

		getParentSpan: function(formelement) {
			return formelement.id ? $('fe-span-' + formelement.id) : null;
		},

		getLabelSpan: function(formelement) {
			return formelement.id ? $('label-' + formelement.id) : null;
		},

		getParentElement: function(el, tagName, className) {
			if (!el) throw('Aranea.Page.RegionHandler.FormBackgroundValidation: parameter "element" must not be null!');
			if (!tagName) throw('Aranea.Page.RegionHandler.FormBackgroundValidation: parameter "tagName" must not be null!');
			if (!className) throw('Aranea.Page.RegionHandler.FormBackgroundValidation: parameter "className" must not be null!');

			var x = function(element) { return element.tagName.toUpperCase() == tagName.toUpperCase(); };
			var y = function(element) { return x(element) && Element.hasClassName(element, className); };
			var filter = className ? y : x;
			var result = $(el).ancestors().find(filter);

			x = y = filter = null;

			return result;
		}
	}
};

Aranea.Page.Form = {

	getArrayVal: function(arr, index) {
		return arr != null && arr.length > index ? arr[index] : null;
	},

	// eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions,systemForm
	getEventData: function(type, args) {
		var data = null;
		args = $A(args);
		if (args.length > 1 && Object.isString(args[0])) {
			data = {
				type: String.interpret(type),
				eventId: String.interpret(this.getArrayVal(args,0)),
				widgetId: String.interpret(this.getArrayVal(args,1)),
				eventParam: this.getArrayVal(args,2),
				eventCondition: this.getArrayVal(args,3),
				eventUpdateRgns: this.getArrayVal(args,4),
				form: this.getArrayVal(args,5)
			};
		} else if (args.length >= 1 && !Object.isString(args[0])) {
			var elem = args[0];
			data = {
				type: String.interpret(type),
				form: Aranea.Page.findSystemForm(elem),
				widgetId: this.getEventTarget(elem),
				eventId: this.getEventId(elem),
				eventParam: this.getEventParam(elem),
				eventUpdateRgns: Object.isString(args.last()) ? args.last() : this.getEventUpdateRegions(elem),
				eventCondition: this.getEventPreCondition(elem)
			};
		} else if (args == null || args.length == 0) {
			data = {
				type: String.interpret(type),
				form: Aranea.Data.systemForm
			};
		}

		if (data == null) {
			throw('Creating event data: Could not create event data set based on given input.')
		}

		data.isSubmitAllowed = Aranea.Page.Request.isAllowed.methodize();
		data.beforeRequest = Aranea.Page.Request.before.methodize();
		data.afterRequest = Aranea.Page.Request.after.methodize();

		this.evaluateEventData(data);
		return data;
	},

	evaluateEventData: function(data) {
		if (data.eventCondition) {
			var cancel = false;
			if (Object.isFunction(data.eventCondition)) {
				cancel = data.eventCondition();
			} else if (Object.isString(data.eventCondition) && data.eventCondition.length > 0) {
				cancel = new Function(data.eventCondition).call() == false;
			}
			data.preconditionFailed = cancel;
		}

		if (!data.preconditionFailed) {
			if (!data.form) {
				data.form = Aranea.Page.findSystemForm();
			}
			if (Object.isFunction(data.eventParam)) {
				data.eventParam = data.eventParam();
			}
			if (Object.isFunction(data.eventUpdateRgns)) {
				data.eventUpdateRgns = data.eventUpdateRgns();
			}
			this.evaluateRequestType(data);
			this.evaluateCustom(data);
		}
	},

	evaluateRequestType: function(data) {
		var type = Aranea.Page.Submitter.TYPE_PLAIN;
		if (data.eventUpdateRgns) {
			type = Aranea.Page.Submitter.TYPE_AJAX;
		} else if (data.form.id == 'aranea-overlay-form') {
			type = Aranea.Page.Submitter.TYPE_OVERLAY;
		}
		data.type = type;
	},

	/**
	 * For those who want to additionally modify the data.
	 */
	evaluateCustom: function(data) {},

	writeToForm: function(data) {
		if (data.form) {
			data.form.araWidgetEventPath.value = String.interpret(data.widgetId);
			data.form.araWidgetEventHandler.value = String.interpret(data.eventId);
			data.form.araWidgetEventParameter.value = String.interpret(data.eventParam);
		}
	},

	readAttribute: function(attribute, element) {
		element = $(element);
		return Object.isElement(element) && attribute ? String.interpret(element.readAttribute(attribute)) : null;
	},

	/**
	 * Returns the ID of a component who should receive events generated by DOM element.
	 * @since 1.1
	 */
	getEventTarget: function(element) {
		return this.readAttribute('arn-trgtwdgt', element);
	},

	/**
	 * Returns event id that should be sent to server when event(element) is called.
	 * @since 1.1
	 */
	getEventId: function(element) {
		return this.readAttribute('arn-evntId', element);
	},

	/**
	 * Returns event parameter that should be sent to server when event(element) is called.
	 * @since 1.1
	 */
	getEventParam: function(element) {
		return this.readAttribute('arn-evntPar', element);
	},

	/**
	 * Returns update regions that should be sent to server when event(element) is called.
	 * @since 1.1
	 */
	getEventUpdateRegions: function(element) {
		return this.readAttribute('arn-updrgns', element);
	},

	/**
	 * Returns closure that should be evaluated when event(element) is called and
	 * needs to decide whether server-side event invocation is needed.
	 * @since 1.1
	 */
	getEventPreCondition: function(element) {
		return this.readAttribute('arn-evntCond', element);
	},

	getFormParameter: function(form, attr) {
		form = $(form);
		if (!Object.isElement(form) || !form.match('form')) {
			throw('The "form" parameter must be an HTML form!');
		}
		var input = form.getInputs('hidden', attr);
		return input.length > 0 ? $F(input[0]) : null;
	},

	getTopServiceId: function(form) {
		return this.getFormParameter(form || Aranea.Data.systemForm, 'araTopServiceId');
	},

	getThreadServiceId: function(form) {
		return this.getFormParameter(form || Aranea.Data.systemForm, 'araThreadServiceId');
	},

	getTransactionId: function(form) {
		return this.getFormParameter(form || Aranea.Data.systemForm, 'araTransactionId');
	},

	getClientStateId: function(form) {
		return this.getFormParameter(form || Aranea.Data.systemForm, 'araClientStateId');
	}
};

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
Aranea.Data.regionHandlers.set('transactionId', Aranea.Page.RegionHandler.TransactionId);
Aranea.Data.regionHandlers.set('document', Aranea.Page.RegionHandler.Document);
Aranea.Data.regionHandlers.set('messages', Aranea.Page.RegionHandler.Message);
Aranea.Data.regionHandlers.set('popups', Aranea.Page.RegionHandler.Popup);
Aranea.Data.regionHandlers.set('reload', Aranea.Page.RegionHandler.Reload);
Aranea.Data.regionHandlers.set('aranea-formvalidation', Aranea.Page.FormBackgroundValidation);

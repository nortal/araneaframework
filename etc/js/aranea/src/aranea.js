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
 * @author Martti Tamm (alar@araneaframework.org)
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

	// URL of aranea dispatcher servlet serving current page. This is by default set by Aranea JSP ui:body tag.
	servletURL: null,

	// Whether get*SubmitURL() method returns an absolute URL or just the part starting from the context.
	absoluteURLs: true,

	// Enables/disables the effect of "focusedFormElementName" and "focusableElements":
	autofocus: false,

	// To monitor focused element and to make it focused after content updating by Ajax. (Since 1.2)
	focusedFormElementName: null,

	// The elements that will be observed for tracking focus:
	focusableElements: "input, select, textarea, button",

	/* Indicates whether the page is completely loaded or not. Page is considered to
	 * be loaded when all system onload events have completed execution. */
	loaded: false,

	/* locale - should be used only for server-side reported locale */
	locale: { lang: '', country: '' },

	/* Indicates whether some form on page is (being) submitted already
	 * by traditional HTTP request. */
	submitted: false,

	systemForm: null,

	/**
	 * Timer that executes keepalive calls, if any.
	 */
	keepAliveTimers: [],

	/**
	 * The flag that determines whether background validation is used by for all forms (FormWidgets) in the application.
	 */
	backgroundValidation: false,

	/* Private fields */
	loadingMessagePositionHack: false,

	regionHandlers: $H(),

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

	systemFormId: 'aranea-overlay-form aranea-form'
};

Aranea.Page = {

	/**
	 * This function is called by Aranea.Page.onload() to initialize Aranea internals while Aranea.Page.onload is mostly
	 * intended to be overridden by custom projects.
	 */
	onLoad: function() {
		Aranea.Page.findSystemForm();
		Aranea.Page.initRSHURL();
		Aranea.Page.addAutoFocusObserver();
		Aranea.Data.loaded = true;
		Aranea.Logger.debug("Aranea scripts are now initialized!");
		document.fire('aranea:loaded');
		document.stopObserving('aranea:loaded');
	},

	onUpdate: function(data) {
		Aranea.Page.findSystemForm();
		if (Aranea.Page.ajaxUploadInit) Aranea.Page.ajaxUploadInit();
		Aranea.Logger.debug("Aranea scripts were updated!");
		document.fire('aranea:updated');
	},

	onUnload: function(data) {
		//document.fire('aranea:unloaded');
		Aranea.Logger.debug("Unloaded Aranea scripts!");
		window.Aranea = undefined;
	},

	addAutoFocusObserver: function() {
		// Monitor the currently focused element for Ajax page update (since 1.2)
		if (Aranea.Data.autofocus && Aranea.Data.focusableElements) {
			Aranea.Logger.debug('Enabling focus observer for elements "' + Aranea.Data.focusableElements + '".');
			$$(Aranea.Data.focusableElements).each(function (element) {
				element.observe("focus", function(event) {
					Aranea.Data.focusedFormElementName = event.element().name;
					Aranea.Logger.debug("Focus set to " + Aranea.Data.focusedFormElementName);
				});
			});
		}
	},

	// Users must provide only form encoding!
	setSystemFormEncoding: function(encoding, that, event) {
		if (Aranea.Data.loaded) {
			Aranea.Data.systemForm.writeAttribute({ 'enctype': encoding, 'encoding': encoding });
			Aranea.Logger.debug('SystemForm encoding was set to "' + encoding + '".')
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
		if (actionId == null || actionId.blank()) throw ('Aranea.Page.action: parameter "actionId" is required!');
		if (actionTarget == null || actionTarget.blank()) throw ('Aranea.Page.action: parameter "actionTarget" is required!');

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
	 * 2) event(requestType, eventId, widgetId, [eventParam], [eventCondition], [eventUpdateRgns], [form]) where
	 *    parameters in brackets are optional.
	 * 
	 * @param formElement The form-element that contains event data.
	 * @param requestType The request type that affects how it's done ('submit', 'ajax', 'overlay').
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
	 * 1) event(formElement, eventUpdateRgns) where formElement has event data.
	 * 2) event(eventId, widgetId, [eventParam], [eventCondition], eventUpdateRgns, [form]) where parameters in
	 *    brackets are optional.
	 * 
	 * @param formElement The form-element that contains event data.
	 * @param requestType The request type that affects how it's done ('submit', 'ajax', 'overlay').
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
			if (!Aranea.Data.submitted && Aranea.Data.loaded) {
				var data = Aranea.Page.Parameter.getEventData(type, args);

				if (data.preconditionFailed) {
					Aranea.Logger.debug('Request cancelled because event precondition returned false.');
				} else {
					this.findSubmitter(data).event(data);
				}
			}
		} catch (e) {
			Aranea.Logger.error('An error occurred during request.', e);
		}
		return false;
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
	 * getSubmitURL([formElement], valuesHash)
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

			if (Object.isElement(arguments[0])) {
				params = Aranea.Page.getFormParameters(arguments[0]);
				index++;
			} else if (arguments.length > 1) {
				index++;
			}

			if (arguments.length >= index + 1 && arguments[index]) {
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

		var url = $A(Aranea.Page.encodeURL(urlStr));

		if (params.size() > 0) {
			var separator = (url.length == 0) || (url.first().indexOf('?') < 0) ? '?' : '&';
			url.push(separator);
			url.push(params.toQueryString());
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
			araTransactionId: Aranea.Page.Parameter.getTransactionId(form),
			araTopServiceId: Aranea.Page.Parameter.getTopServiceId(form),
			araThreadServiceId: Aranea.Page.Parameter.getThreadServiceId(form),
			araClientStateId: Aranea.Page.Parameter.getClientStateId(form)
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
		Aranea.Logger.debug("Executing Aranea.Page.findSystemForm()...");
		element = $(element);
		var result = null;

		if (!Object.isElement(element)) {
			$w(Aranea.Data.systemFormId).find(function(id) { return result = $(id) });
		} else if (element.match('form') && Aranea.Data.systemFormId.indexOf(element.id) >= 0) {
			result = element;
		} else {
			result = element;
			var targetIds = Aranea.Data.systemFormId;
			var validId = !String.interpret(result.id).empty() && targetIds.indexOf(result.id) >= 0;
			while (result.tagName && (!validId || !result.match('form'))) {
				result = result.up();
				validId = result.tagName && !result.id.empty() && targetIds.indexOf(result.id) >= 0;
			}
			result = result.tagName != null ? result : null;
		}

		if (result == null) {
			throw('findSystemForm: Could not find Aranea system form that has one of those IDs: "'
					+ Aranea.Data.systemFormId + '". Make sure that the form exists, and, if element was given, '
					+ 'element is inside a form.')
		} else if (changeData != false) {
			Aranea.Data.systemForm = result;
		}

		return result;
	},

	/**
	 * RSH initialization for state versioning. Has effect only when "aranea-rsh.js" is also included in the page.
	 */
	initRSHURL: function() {
		Aranea.Logger.debug("Executing Aranea.Page.initRSHURL()");
		if (window.dhtmlHistory && Aranea.Data.systemForm.araClientStateId) {

			window.dhtmlHistory.firstLoad = true;
			window.dhtmlHistory.ignoreLocationChange = true;

			var stateId = Aranea.Data.systemForm.araClientStateId.value;

			// If we generate hashes to HTTP requests, URL changes cause the browser never uses local history data, as
			// the hash added later is not accessible from its memory cache. Thus we just keep the URL intact for these
			// cases, so that browsers own history mechanisms can take over.
			if (stateId.startsWith('HTTP')) {
				window.location.hash = stateId;
				window.dhtmlHistory.add(stateId, null);
			}
		}
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
		var element = $(Aranea.Data.loadingMessageId);
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
		var element = $(Aranea.Data.loadingMessageId);
		if (element) element.hide();
		element = null;
	},

	/**
	 * Build loading message. Called when an existing message element is not found.
	 *
	 * @since 1.1
	 */
	buildLoadingMessage: function() {
		return new Element('div', { id: Aranea.Data.loadingMessageId }).update(Aranea.Data.loadingMessageContent);
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

		document.fire('aranea:beforeEvent', data, true);

		Aranea.Page.Request.customBefore(data);
		Aranea.Page.Parameter.writeToForm(data); // Write event data to form so that it would be sent to server-side.
	},

	/**
	* A callback that is called after each submit (no matter whether it is AJAX or not). This method includes default
	* behaviour.
	*/
	after: function(data) {
		Aranea.Data.submitted = false;
		Aranea.Data.loaded = true;

		document.fire('aranea:afterEvent', data, true);

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
		return this.event(Aranea.Page.Parameter.getEventData(this.TYPE, args));
	},

	event: function(eventData) {
		var result = false;
		if (eventData.isSubmitAllowed()) {
			Event.fire(Aranea.Data.systemForm, 'aranea:beforeEvent', eventData);
			eventData.beforeRequest();

			result = this.event_core(eventData);

			eventData.afterRequest();
			Event.fire(Aranea.Data.systemForm, 'aranea:afterEvent', eventData);
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
		Aranea.ModalBox.update({ params: data.form.serialize(true) });
	}
}),

Aranea.Page.Submitter.AJAX = Class.create(Aranea.Page.Submitter.Plain, {

	TYPE: Aranea.Page.Submitter.TYPE_AJAX,

	event: function(eventData) {
		if (eventData.isSubmitAllowed(eventData)) {
			eventData.beforeRequest();

			this.data = eventData;

			var ajaxRequestId = Aranea.Page.getRandomRequestId();
			var neededAraClientStateId = eventData.form.araClientStateId ? eventData.form.araClientStateId.value : null;
			var neededAraTransactionId = 'override';

			if (eventData.eventUpdateRgns == 'araneaGlobalClientHistoryNavigationUpdateRegion') {
				neededAraClientStateId = window.dhtmlHistoryListenerRequestedState;
				window.dhtmlHistoryListenerRequestedState = null;
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
		// This gets executed twice, it may be that during update region processing
		// something already needs current stateId presence, e.g. the reloading region
		// handler. As it's likely that whole system form will be replaced completely
		// when document region is updated, this must be repeated in onComplete.
		Aranea.Page.Submitter.AJAX.ResponseHeaderProcessor(transport);

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

			if (Aranea.ModalBox) {
				Aranea.ModalBox.afterUpdateRegionResponseProcessing(Aranea.Data.systemForm);
			}

			// Set the previously focused form control focused again (since 1.2)
			if (Aranea.Data.autofocus && Aranea.Data.focusedFormElementName) {
				var formElem = $$('[name="' + Aranea.Data.focusedFormElementName + '"]').first();
				Aranea.Logger.debug("Trying to set focus to " + Aranea.Data.focusedFormElementName);
				Aranea.Data.focusedFormElementName = null;
				if (formElem) {
					try {
						formElem.focus();
						Aranea.Logger.debug("Focus successfully set to  " + formElem.name);
					} catch (e) {
					} finally {
						formElem = null;
					}
				}
			}
			Aranea.Page.addAutoFocusObserver();
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
	ResponseHeaderProcessor: function(transport) {
		var stateVersion = null;
		try {
			stateVersion = transport.getResponseHeader('Aranea-Application-StateVersion');
		} catch (e) {
			stateVersion = null;
		}

		if (stateVersion != null && stateVersion.length > 0) {
			var sForm = Aranea.Data.systemForm;
			if (sForm.araClientStateId) {
				sForm.araClientStateId.value = stateVersion;
			}
			if (window.dhtmlHistory) {
				window.dhtmlHistory.add(stateVersion, true);
			}
		}
	},

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
			counter[key] = counter[key] ? counter[key]++ : 1;
			if (handlers.get(key)) {
				log.debug('Region type: "' + key + '" (' + length + ' characters)');
				handlers.get(key).process(content);
				hasRegions = true;
			} else {
				throw('Region type: "' + key + '" is unknown!');
			}
		});

		Aranea.Data.receivedRegionCounters = counter;

		if (Aranea.Data.reloadOnNoDocumentRegions && !hasRegions) {
			log.debug('No document regions were received, forcing a reload of the page');
			if (handlers.get('reload')) {
				handlers.get('reload').process();
			} else {
				log.error('No handler is registered for "reload" region, unable to force page reload!');
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

			Aranea.Logger.debug("Updating document region '" + id + "'...");

			if (mode == 'update') {
				$(id).update(domContentString);
			} else if (mode == 'replace') {
				$(id).replace(domContentString);
			} else {
				Aranea.Logger.error('Document region mode "' + mode + '" is unknown');
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
	},

	/**
	 * Region handler that opens popup windows.
	 *
	 * @since 1.1
	 */
	Popup: {

		process: function(content) {
			this.openPopups(content.evalJSON());
			document.observe('aranea:loaded', Aranea.Popup.processPopups);
			document.observe('aranea:updated', Aranea.Popup.processPopups);
		},

		openPopups: function(popups) {
			popups.each(function(popup) {
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
			if (content == null) throw ('FormBackgroundValidation: content parameter is required!');
			var result = content.evalJSON();
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
			if (!el) throw('Aranea.Page.RegionHandler.FormBackgroundValidation: parameter "element" must not be null!');
			if (!tagName) throw('Aranea.Page.RegionHandler.FormBackgroundValidation: parameter "tagName" must not be null!');
			if (!className) throw('Aranea.Page.RegionHandler.FormBackgroundValidation: parameter "className" must not be null!');

			var x = function(element) { return element.tagName.toUpperCase() == tagName.toUpperCase(); };
			var y = function(element) { return x(element) && Element.hasClassName(element, className); };
			var filter = className ? y : x;
			var result = $(el).ancestors().find(filter);

			x = null;
			y = null;
			filter = null;

			return result;
		}
	}
};

Aranea.Page.Parameter = {

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
		if (data.form.id == 'aranea-overlay-form') {
			type = Aranea.Page.Submitter.TYPE_OVERLAY;
		} else if (data.eventUpdateRgns) {
			type = Aranea.Page.Submitter.TYPE_AJAX;
		}
		data.type = type;
	},

	/**
	 * For those who want to additionally modify the data.
	 */
	evaluateCustom: function(data) {},

	writeToForm: function(data) {
		if (data.form) {
			data.form.araWidgetEventPath.value = data.widgetId;
			data.form.araWidgetEventHandler.value = data.eventId;
			data.form.araWidgetEventParameter.value = data.eventParam;
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
		return this.getFormParameter(form, 'araTopServiceId');
	},

	getThreadServiceId: function(form) {
		return this.getFormParameter(form, 'araThreadServiceId');
	},

	getTransactionId: function(form) {
		return this.getFormParameter(form, 'araTransactionId');
	},

	getClientStateId: function(form) {
		return this.getFormParameter(form, 'araClientStateId');
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

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

var Aranea = window.Aranea || {};
Aranea.Data = Aranea.Data || {};

/**
 * Aranea Logging support.
 * Logger implementations (others may add loggers here with the same namespace). New loggers should follow name
 * pattern "[LOGGER_NAME]_LOGGER", where LOGGER_NAME is used by setLogger(name) to find the logger implementation.
 *
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
Aranea.Logger = {

	trace: function(message, exception) {
		Aranea.Data.logger.trace(message || '', exception || '');
	},

	debug: function(message, exception) {
		Aranea.Data.logger.debug(message || '', exception || '');
	},

	info: function(message, exception) {
		Aranea.Data.logger.info(message || '', exception || '');
	},

	warn: function(message, exception) {
		Aranea.Data.logger.warn(message || '', exception || '');
	},

	error: function(message, exception) {
		Aranea.Data.logger.error(message || '', exception || '');
	},

	fatal: function(message, exception) {
		Aranea.Data.logger.fatal(message || '', exception || '');
	},

	profile: function(message, exception) {
		Aranea.Data.logger.profile(message || '', exception || '');
	},

	/**
	 * Sets the logging mechanism to use for logging messages. Accepted types: "dummy", "firebug", "safari", "log4js",
	 * "blackbird". Null parameter defaults to "dummy". The logger will be resolved to a predefined logger
	 * implementation stored in Aranea.Logger.[TYPE]_LOGGER variable, e.g. "dummy" resolves to Aranea.Logger.DUMMY_LOGGER.
	 * 
	 * @param type Accepted types: "dummy" (same as null), "firebug", "safari", "log4js", "blackbird".
	 */
	setLogger: function(type) {
		type = (type || 'DUMMY').toUpperCase()+ '_LOGGER';
		Aranea.Data.logger = type ? this[type] : null;
		if (!type || Aranea.Data.logger == null) {
			throw('The logger type was not recognized: "' + type + '" (expected: "dummy", "firebug", "safari", "log4js", "blackbird", or null)');
		}
	}
};

Aranea.Logger.DUMMY_LOGGER = {
	trace: Prototype.emptyFunction,
	debug: Prototype.emptyFunction,
	info: Prototype.emptyFunction,
	warn: Prototype.emptyFunction,
	error: Prototype.emptyFunction,
	fatal: Prototype.emptyFunction,
	profile: Prototype.emptyFunction
};

Aranea.Logger.SAFARI_LOGGER = window.console && window.console.log ? {
	trace: window.console.log,
	debug: window.console.log,
	info: window.console.log,
	warn: window.console.log,
	error: window.console.log,
	fatal: window.console.log,
	profile: Prototype.emptyFunction
} : Aranea.Logger.DUMMY_LOGGER;

Aranea.Logger.FIREBUG_LOGGER = window.console && window.console.debug ? {
	trace: window.console.debug,
	debug: window.console.debug,
	info: window.console.info,
	warn: window.console.warn,
	error: window.console.error,
	fatal: window.console.error,
	profile: Prototype.emptyFunction
} : Aranea.Logger.DUMMY_LOGGER;

Aranea.Logger.LOG4JS_LOGGER = window.log4javascript && window.log4javascript.getDefaultLogger ?
	window.log4javascript.getDefaultLogger() : Aranea.Logger.DUMMY_LOGGER;

Aranea.Logger.BLACKBIRD_LOGGER = window.log && window.log.toggle ? {
	trace: function(msg, exception) { log.debug(msg.escapeHTML(), exception) },
	debug: function(msg, exception) { log.debug(msg.escapeHTML(), exception) },
	info: function(msg, exception) { log.info(msg.escapeHTML(), exception) },
	warn: function(msg, exception) { log.warn(msg.escapeHTML(), exception) },
	error: function(msg, exception) { log.error(msg.escapeHTML(), exception) },
	fatal: function(msg, exception) { log.error(msg.escapeHTML(), exception) },
	profile: function(msg) { log.profile(msg.escapeHTML()) }
} : Aranea.Logger.DUMMY_LOGGER;

Aranea.Logger.setLogger('dummy');

/**
 * Utility functions. Aranea.Util namespace was created in version 2.0. However, "aranea-util.js" existed before. Some
 * functions were removed, others were consolidated here.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
Aranea.Util = {

	/**
	 * The selector for fetching elements where ID must be set. See Aranea.Util.fillMissingIds().
	 * 
	 * @since 2.0
	 */
	MISSING_IDS_SELECTOR: 'input[type!=hidden]:not([id]), select:not([id]), textarea:not([id]), button:not([id])',

	/**
	 * The selector for setting focus to the first input on the page. See Aranea.Util.focusFormFirstInput().
	 * 
	 * @since 2.0
	 */
	FOCUS_INPUT_SELECTOR: 'input[type!=hidden]:enabled, select:enabled, textarea:enabled, button:enabled',

	/**
	 * The selector for observing the focus status of these elements. See Aranea.Util.observeFormInputFocus().
	 * 
	 * @since 2.0
	 */
	AUTO_FOCUS_INPUT_SELECTOR: 'input[type!=hidden], select, textarea, button',

	/**
	 * Same as Aranea.UI.scrollToCoordinates() but also makes sure that scrolling is done when the page is loaded. Also
	 * adds an event listener that stores window scroll position data before an event is sent to the server.
	 * 
	 * @since 2.0
	 */
	setWindowCoordinates: function(x, y) {
		if (Aranea.Data.loaded) {
			Aranea.UI.scrollToCoordinates(x, y);
		} else {
			document.observe('aranea:loaded', function() {
				var form = Aranea.Data.systemForm;
				x = x || parseInt(form.windowScrollX.value);
				y = y || parseInt(form.windowScrollY.value);
				form = null;

				Aranea.UI.scrollToCoordinates(x, y);
				document.observe('aranea:beforeEvent', Aranea.UI.saveScrollCoordinates);
			});
		}
	},

	/**
	 * A utility method for filling in missing IDs with generated ones. Useful when, for example, making use of
	 * automated testing tools that use XPath to find elements on page. It makes use of Prototype JS library method
	 * element#identify(). It is not called from anywhere. When a project wants to make use of it, it is recommended to
	 * use page loaded event to do that:
	 * 
	 * document.observe('aranea:loaded' , Aranea.Util.fillMissingIds);
	 * document.observe('aranea:updated', Aranea.Util.fillMissingIds);
	 * 
	 * See also Aranea.Util.MISSING_IDS_SELECTOR that can be customized.
	 */
	fillMissingIds: function() {
		$$(Aranea.Util.MISSING_IDS_SELECTOR).invoke('identify');
		Aranea.Logger.debug('Filled in missing IDs for form inputs and buttons.')
	},

	/**
	 * A utility method for automatically setting focus to the first element on form input (including button) inside the
	 * system-form. It should be called on page load, like this:
	 * 
	 * document.observe('aranea:loaded' , Aranea.Util.focusFormFirstInput);
	 * 
	 * See also Aranea.Util.FOCUS_INPUT_SELECTOR that can be customized.
	 */
	focusFormFirstInput: function() {
		if (!Aranea.Data.systemForm) throw('Page system-form is required!');
		var inpts = Aranea.Data.systemForm.select(Aranea.Util.FOCUS_INPUT_SELECTOR);
		if (inpts.length) {
			inpts.first().focus();
			Aranea.Logger.debug('Focused form input/button element: ' + inpts.first().inspect())
		}
		inpts = null;
	},

	/**
	 * Monitor the currently focused form input/button. This should be used together with
	 * Aranea.Util.updateFormInputFocus() to preserve input focus during AJAX requests.
	 * 
	 * document.observe('aranea:loaded' , Aranea.Util.observeFormInputFocus);
	 * document.observe('aranea:updated', Aranea.Util.observeFormInputFocus); // Add second observer for new elements.
	 * document.observe('aranea:updated', Aranea.Util.updateFormInputFocus);
	 * 
	 * @since 1.2 (introduced), 2.0 (implemented in Aranea.Util)
	 */
	observeFormInputFocus: function() {
		if (!Aranea.Data.systemForm) throw('Page system-form is required!');

		Aranea.Data.systemForm.select(Aranea.Util.AUTO_FOCUS_INPUT_SELECTOR).each(function(element) {
			if (!element.retrieve('autoFocusObserverAdded')) {
				element.observe('focus', function(event) {
					var name = event.element().name;
					Aranea.Data.focusedFormElementName = name;

					if (name) {
						Aranea.Logger.debug('Remembered that element [name=' + name + '] was focused.');
					}
				}).store('autoFocusObserverAdded', true);
			}
		});

		Aranea.Logger.debug('Enabled focus observer for form input/button elements.');
	},

	/**
	 * Sets focus to the last known system-form input/button. This should be used together with
	 * Aranea.Util.observeFormInputFocus() to preserve input focus during AJAX requests.
	 * 
	 * document.observe('aranea:loaded' , Aranea.Util.observeFormInputFocus);
	 * document.observe('aranea:updated', Aranea.Util.observeFormInputFocus); // Add second observer for new elements.
	 * document.observe('aranea:updated', Aranea.Util.updateFormInputFocus);
	 * 
	 * @since 1.2 (introduced), 2.0 (implemented in Aranea.Util)
	 */
	updateFormInputFocus: function() {
		if (!Aranea.Data.systemForm) throw('Page system-form is required!');
		if (!Aranea.Data.focusedFormElementName) return;

		var name = Aranea.Data.focusedFormElementName;
		Aranea.Data.focusedFormElementName = null;

		Aranea.Data.systemForm.select('[name="' + name + '"]').invoke('focus');

		name = Aranea.Data.focusedFormElementName;
		if (name) {
			Aranea.Logger.debug('Updated form input/button [name=' + name + '] focus.');
		}
	},

	/**
	 * Action-request (AJAX) based solution for downloading a file. On the server side, there should be a
	 * FileDownloadActionListener or similar listener handling the request.
	 * 
	 * The first three parameters are used for doing action request. The fourth parameter can be specified to override
	 * the default callback for initiating download process in browser.
	 * 
	 * @since 2.0
	 */
	downloadFile: function(actionId, actionTarget, actionParam, callback) {
		callback = callback || function(transport) {
			var url = transport.responseText;
			if (url != 'error') {
				window.location.href = url;
			} else {
				window.alert('Could not download file!');
			}
		};

		Aranea.Page.action(actionId, actionTarget, actionParam, null, callback);
		return false;
	},

	/**
	 * A wrapper around String that lets to read text by lines and by chunks of characters.
	 *
	 * @since 1.1
	 */
	AjaxResponse: Class.create({

		initialize: function(text, readResponseId) {
			this.pos = 0;
			this.text = text;
			if (readResponseId) {
				this.responseId = this.readLine();
			}
		},

		readLine: function() {
			var line = '';
			if (this.pos < this.text.length) {
				var newpos = this.text.indexOf('\n', this.pos);
				if (newpos == -1) {
					line = this.text.substr(this.pos);
					this.pos = this.text.length;
				} else {
					newpos++;
					line = this.text.substr(this.pos, newpos-this.pos-1);
					this.pos = newpos;
				}
			}
			return line;
		},

		readCharacters: function(numberOfCharacters) {
			var content = this.text.substr(this.pos, numberOfCharacters);
			this.pos = this.pos + numberOfCharacters;
			return content;
		},

		isEndOfResponse: function() {
			return this.pos >= this.text.length;
		},

		each: function(fn) {
			if (fn == null) throw('AjaxResponse.each: The provided function must not be null!');
			while (!this.isEndOfResponse()) {
				var key = this.readLine();
				var length = parseInt(this.readLine());
				try {
					fn(key, this.readCharacters(length), length);
				} catch (e) {
					Aranea.Logger.error('Error while processing region "' + key + '".', e);
				}
			}
		},

		getResponseId: function() {
			return this.responseId;
		},

		toString: function() {
			return this.text.substr(this.pos);
		}
	})
};

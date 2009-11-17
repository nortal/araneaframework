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
 * Utility functions.
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */

var Aranea = Aranea ? Aranea : {};

/**
 * Aranea Logging support.
 *
 * @since 2.0
 */
Aranea.Logger = {

	// Logger implementations (others may add loggers here with the same namespace). New loggers should follow name
	// pattern "[LOGGER_NAME]_LOGGER", where LOGGER_NAME is used by setLogger(name) to find the logger implementation.

	DUMMY_LOGGER: {
		trace: Prototype.emptyFunction,
		debug: Prototype.emptyFunction,
		info: Prototype.emptyFunction,
		warn: Prototype.emptyFunction,
		error: Prototype.emptyFunction,
		fatal: Prototype.emptyFunction
	},

	SAFARI_LOGGER: window.console && window.console.log ? {
		trace: function(s) { window.console.log(s); },
		debug: function(s) { window.console.log(s); },
		info: function(s) { window.console.log(s); },
		warn: function(s) { window.console.log(s); },
		error: function(s) { window.console.log(s); },
		fatal: function(s) { window.console.log(s); }
	} : Aranea.Logger.DUMMY_LOGGER,

	FIREBUG_LOGGER: window.console && window.console.debug ? {
		trace: function(s) { window.console.debug(s); },
		debug: function(s) { window.console.debug(s); },
		info: function(s) { window.console.info(s); },
		warn: function(s) { window.console.warn(s); },
		error: function(s) { window.console.error(s); },
		fatal: function(s) { window.console.error(s); }
	} : Aranea.Logger.DUMMY_LOGGER,

	LOG4JS_LOGGER: window.log4javascript && window.log4javascript.getDefaultLogger ?
			window.log4javascript.getDefaultLogger() : Aranea.Logger.DUMMY_LOGGER,

	init: function() {
		var log = Aranea.Logger;
		log.SAFARI_LOGGER = log.SAFARI_LOGGER == null ? log.DUMMY_LOGGER : log.SAFARI_LOGGER;
		log.FIREBUG_LOGGER = log.FIREBUG_LOGGER == null ? log.DUMMY_LOGGER : log.FIREBUG_LOGGER;
		log.LOG4JS_LOGGER = log.LOG4JS_LOGGER == null ? log.DUMMY_LOGGER : log.LOG4JS_LOGGER;
		log.initiated
		log = null;
	},

	trace: function(message, exception) {
		Aranea.Data.logger.trace(message, exception);
	},

	debug: function(message, exception) {
		Aranea.Data.logger.debug(message, exception);
	},

	info: function(message, exception) {
		Aranea.Data.logger.info(message, exception);
	},

	warn: function(message, exception) {
		Aranea.Data.logger.warn(message, exception);
	},

	error: function(message, exception) {
		Aranea.Data.logger.error(message, exception);
	},

	fatal: function(message, exception) {
		Aranea.Data.logger.fatal(message, exception);
	},

	/**
	 * Sets the logging mechanism to use for logging messages. Accepted types: "dummy", "firebug", "safari", "log4js".
	 * Null is not allowed. The logger is resolved as a logger implementation in Aranea.Logger namespace as
	 * [TYPE]_LOGGER variable, e.g. dummy resolves to Aranea.Logger.DUMMY_LOGGER.
	 * 
	 * @param type Not null string, accepted types: "dummy", "firebug", "safari", "log4js".
	 */
	setLogger: function(type) {
		Aranea.Logger.init();
		type = type ? type.toUpperCase()+ '_LOGGER' : null;
		Aranea.Data.logger = type ? this[type] : null;
		if (!type || Aranea.Data.logger == null) {
			throw('The logger type was not recongized: "' + type + '" (expected: "dummy", "firebug", "safari", "log4js", or null)');
		}
	}
};

Aranea.Util = {

	setWindowCoordinates: function(x, y) {
		document.observe('aranea:loaded', function() {
			var form = Aranea.Data.systemForm;
			if (form.windowScrollX && form.windowScrollX) {
				Aranea.UI.scrollToCoordinates(x, y);
			}
			form = null;
			document.observe('aranea:submit', Aranea.UI.saveScrollCoordinates);
			document.stopObserving('aranea:loaded', this);
		});
	},

	/**
	 * A wrapper around String that lets to read text by lines and by chunks of characters.
	 *
	 * @since 1.1
	 */
	AjaxResponse: Class.create({

		initialize: function(text) {
			this.pos = 0;
			this.text = text;
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
			if (Object.isString(numberOfCharacters)) numberOfCharacters = parseInt(numberOfCharacters);
			var content = this.text.substr(this.pos, numberOfCharacters);
			this.pos = this.pos + numberOfCharacters;
			return content;
		},

		isEmpty: function() {
			return this.pos >= this.text.length;
		},

		each: function(fn) {
			if (fn == null) throw('AjaxResponse.each: The provided function must not be null!');
			while (!this.isEmpty()) {
				if (this.responseId == null) this.responseId = this.readLine();
				var key = this.readLine();
				var length = this.readLine();
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

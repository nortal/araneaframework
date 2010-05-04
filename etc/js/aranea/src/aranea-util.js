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

	profile: function(message, exception) {
		Aranea.Data.logger.profile(message, exception);
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
	trace: function(msg, exception) { log.debug((msg||'').escapeHTML(), exception) },
	debug: function(msg, exception) { log.debug((msg||'').escapeHTML(), exception) },
	info: function(msg, exception) { log.info((msg||'').escapeHTML(), exception) },
	warn: function(msg, exception) { log.warn((msg||'').escapeHTML(), exception) },
	error: function(msg, exception) { log.error((msg||'').escapeHTML(), exception) },
	fatal: function(msg, exception) { log.error((msg||'').escapeHTML(), exception) },
	profile: function(msg) { log.profile((msg||'').escapeHTML()) }
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

	setWindowCoordinates: function(x, y) {
		document.observe('aranea:loaded', function() {
			Aranea.UI.scrollToCoordinates(x, y);
			document.observe('aranea:beforeEvent', Aranea.UI.saveScrollCoordinates);
		});
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

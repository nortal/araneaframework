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
 * Aranea keyboard event handlers.
 * 
 * The functions presented here are invoked from Aranea JSP tags.
 * Therefore you CAN'T be too liberal with them unless you know what you are doing.
 *
 * Created: 29.12.2004
 * Author: Konstantin Tretyakov.
 * 
 * The general idea here is extremely simple. When an event happens for a certain form element,
 * a certain 'top-level event handler function' defined here is invoked. This handler looks
 * whether any 'sub-handlers' registered themselves with this event, and if so, invokes 
 * them one by one.
 *
 * So this file contains
 *  - top-level handler routines. (uiHandleKeypress, and someday will perhaps contain handlers for other events)
 *  - subhandler registration routines. (uiRegisterKeypressHandler)
 *  - helper structure for holding registered handlers (UiHandlerRegistry)
 */
 
// Parts of code from http://www.openjs.com/scripts/events/keyboard_shortcuts/
// shortcut library (version 1.00.A by Binny V A) distributed under BSD license.

// ------------------------------- Keyboard Events ---------------------------------- //

var Aranea = window.Aranea || {};

/** @since 1.1 */
Aranea.Keyboard = {

	//NOTE by TP: this actually is only correct for one keyboard layout only...
	/** @since 1.1 */
	SPECIAL_KEYS: {
		'esc':27,
		'escape':27,
		'tab':9,
		'space':32,
		'return':13,
		'enter':13,
		'backspace':8,
		'scrolllock':145,
		'scroll_lock':145,
		'scroll':145,
		'capslock':20,
		'caps_lock':20,
		'caps':20,
		'numlock':144,
		'num_lock':144,
		'num':144,
		'pause':19,
		'break':19,
		'insert':45,
		'home':36,
		'delete':46,
		'end':35,
		'pageup':33,
		'page_up':33,
		'pu':33,
		'pagedown':34,
		'page_down':34,
		'pd':34,
		'left':37,
		'up':38,
		'right':39,
		'down':40,
		'f1':112,
		'f2':113,
		'f3':114,
		'f4':115,
		'f5':116,
		'f6':117,
		'f7':118,
		'f8':119,
		'f9':120,
		'f10':121,
		'f11':122,
		'f12':123
	},

	parseFilterCode: function(elementPrefix, keyCode, metaStr) {
		var result = Aranea.Keyboard.parseFilterStr(metaStr);
		result.keyCode = keyCode;
		result.prefix = elementPrefix;
		return result;
	},

	parseFilterStr: function(filterStr) {
		var result = {};
		var filters = filterStr ? filterStr.split('+') : [];
		$A(filters).each(function(filter) {
			filter = filter.toLowerCase();
			if (Aranea.Keyboard.isMetaStr(filter)) {
				result[filter] = true;
			} else {
				result.key = filter;
			}
		});
		result.toString = function() {
			var str = [];
			Object.keys(this).each(function(prop) {
				var value = this[prop];
				if (value && !Object.isFunction(value)) {
					if (value == true || value == false) {
						str.push(prop);
					} else if (prop == 'key') {
						str.push(value);
					} else {
						str.push(['[', value, ']'].join(''));
					}
				}
			}.bind(this));
			return str.join('+');
		};
		return result;
	},

	isMetaStr: function(filterStr) {
		filterStr = filterStr ? filterStr.toLowerCase() : null;
		return filterStr == 'alt' || filterStr == 'ctrl' || filterStr == 'shift' || filterStr == 'meta';
	},

	/**
	 * The main function used to register a handler for the "keypress" event.
	 * Parameters:
	 *     elementPrefix - the handler will be triggered when this matches the prefix of the 
	 *                     ID of the target form element. E.g. when elementPrefix is 'someForm.someSubelement'
	 *                     the handler will be triggered for form elements 'someForm.someSubelement.someElement', etc.
	 *     keyCode       - the keyCode (as number) to trigger on (may be omitted, if metaCondStr is provided).
	 *     metaCondStr   - a case-insensitive string representing the keypress to react to, e.g. "ctrl+shift+a", "alt+enter".
	 *                     May be omitted, if keyCode is provided (both can be specified together, too).
	 *     handler       - function(event, formElementId) to receive the notification.
	 *                     function's return value determines whether it allows other handlers to be invoked.
	 *                     when it is false, the called handler is the last one to be invoked.
	 * @since 1.1
	 */
	registerKeypressHandler: function(elementPrefix, keyCode, metaCondStr, handler) {
		if (arguments.length == 3) {
			handler = arguments[2];
			if (Object.isNumber(arguments[1])) {
				metaCondStr = null;
			} else if (Object.isString(arguments[1])) {
				metaCondStr = arguments[1];
				keyCode = null;
			}
		}

		if (keyCode == null && metaCondStr == null) {
			throw('Aranea.Keyboard.registerKeypressHandler: At least "keyCode" or "metaCondStr" parameter must be provided!');
		} else if (handler == null) {
			Aranea.Logger.warn('Aranea.Keyboard.registerKeypressHandler: No event handler was registered for element-prefix "'
				+ elementPrefix + '" (keyCode=' + keyCode + ',metaCond="' + metaCondStr + '") because event handler was not provided!');
		}

		elementPrefix = elementPrefix ? elementPrefix : '';

		Aranea.Logger.debug('Registering handler for ' + keyCode + '/"' + metaCondStr + '" (prefix: "' + elementPrefix + '")');

		var filter = Aranea.Keyboard.parseFilterCode(elementPrefix, keyCode, metaCondStr);
		Aranea.Keyboard.HANDLER_REGISTRY.addHandler(filter, handler);

		// The global "keydown" event listener:
		if (!Aranea.Keyboard.handlerRegistered) {
			document.observe("keydown", Aranea.Keyboard.handleKeypress);
			Aranea.Keyboard.handlerRegistered = true;
		}
	},

	 /**
	  * This function will receive all keypress events from all form elements
	  *
	  * Parameters:
	  *     event  - the event object
	  *     formElementId - full (unique) id of the element that received the event.
	  * @since 1.1
	  */
	handleKeypress: function (event) {
		if (event) {
			try {
				var result = Aranea.Keyboard.HANDLER_REGISTRY.invokeHandlers(event);

				if (result == false || event.keyCode == '13' && event.currentTarget.activeElement.nodeName != 'TEXTAREA') { // The result may also be null (which means true) here!
					event.stop();
				}
			} catch (e) {
				// Keyboard handler errors may be thrown after AJAX region updates.
				Aranea.Logger.warn("Keyboard handler error (non-critical): " + e);
			}
		} else {
			throw('The "event" object as parameter is required!')
		}
	},

	/** 
	 * Returns whether the event matches the filter conditions.
	 * @since 1.0.11
	 */
	isMatch: function(event, filter) {
		Aranea.Logger.debug([event.type,' detected! [event.charCode=',event.charCode,', event.keyCode=',event.keyCode,
			', ctrlKey=',event.ctrlKey,', altKey=',event.altKey,', kev.metaKey=',event.metaKey,']'].join(''));


		var result = true;
		result = result && (filter.shift == null || filter.shift == event.shiftKey);
		result = result && (filter.alt == null || filter.alt == event.altKey);
		result = result && (filter.ctrl == null || filter.ctrl == event.ctrlKey);
		result = result && (filter.meta == null || filter.meta == event.metaKey);

		if (filter.keyCode) {
			result = result && filter.keyCode == event.keyCode;
		} else if (filter.key)  {
			var charCode = event.charCode ? event.charCode : null;
			if (Aranea.Keyboard.SPECIAL_KEYS[filter.key]) {
				result = result && event.keyCode == Aranea.Keyboard.SPECIAL_KEYS[filter.key];
			} else {
				result = result && filter.key == String.fromCharCode(charCode);
			}
		}

		Aranea.Logger.debug('Was the keypress the one expected ("' + filter + '"): ' + result);

		return result;
	},

	/**
	 * Returns the function that stops received keyboard event propagation if the input was not allowed by filter.
	 * @since 1.0.11
	 */
	getKeyboardInputFilterFunction: function(filter) {
		return function(event) {
			Aranea.Logger.debug([event.type,' detected! [event.charCode=',event.charCode,', event.keyCode=',event.keyCode,
				', ctrlKey=',event.ctrlKey,', altKey=',event.altKey,', kev.metaKey=',event.metaKey,']'].join(''));

			var eventChar = String.fromCharCode(event.charCode ? event.charCode : event.keyCode);

			if (eventChar) {
				if (filter.indexOf(eventChar) >= 0) {
					event.stop();
				}
			}
		};
	},

	/** 
	 * A map: keyCode -> list of (filter, handler) to store handlers and invoke them.
	 */
	UiHandlerRegistryImpl: Class.create({
		initialize: function() {
			this.handlers = $H(); // This maps from keyCode to array of pairs (filter, handler)
			this.elementPrefixes = {};
		},
 
		/** 
		 * Adds a new handler to the registry
		 */
		addHandler: function(filter, handler) {
			var traditionalHandler = {
				filter: filter,
				handler: handler
			};

			// If not number, register handler by element prefix only
			// NB! that means handler itself must determine whether it should be invoked or not for any event that activates it!
			// Otherwise use old-fashioned by-keycode registered handler.
			var key = Object.isNumber(filter.keyCode) ? filter.keyCode : filter.prefix;
			if (this.handlers.get(key)) {
				this.handlers.get(key).push(traditionalHandler);
			} else {
				this.handlers.set(key, [ traditionalHandler ]);
			}
		},

		/**
		 * Invokes all handlers registered for given key-code and with matching element prefix.
		 * if a handler returns false, the remaining handlers are not invoked
		 */
		invokeHandlers: function(event) {
			var keyCode = event.keyCode ? event.keyCode : event.which;
			var keyHandlers = this.handlers.get(keyCode);
			var elHandlers = this.getElementHandlers(event.element());
			var elementName = event.element().name;

			if (elHandlers) {
				Aranea.Logger.debug('Invoking element handlers, count=' + elHandlers.length);

				for (var i = elHandlers.length - 1; i >= 0; i--) {
					if (Aranea.Keyboard.isMatch(event, elHandlers[i].filter)) {
						var handlerFn = elHandlers[i].handler;
						var prefix    = elHandlers[i].prefix;

						if (handlerFn) {
							Aranea.Logger.debug('Invoking element handler: ' + i + '/' + elHandlers.length);

							var cont = handlerFn(event, elementName);
							if (cont == false) {
								Aranea.Logger.debug('Handler returned false. Returning to the closest exit.');
								return false;
							}
						} else {
							Aranea.Logger.warn('Aranea.Keyboard.UiHandlerRegistryImpl: '
								+ 'Skipping event handler function, because it is null! (event: ' + event.type
								+  ', elementPrefix: ' + elHandlers[i].filter.prefix + ')');
						}
					}
				}
			}

			if (keyHandlers){
				Aranea.Logger.debug("Invoking keyCode handlers, count=" + keyHandlers.length);
				for (var i = keyHandlers.length - 1; i >= 0; i--) {
					var handlerFn     = keyHandlers[i].handler;
					var elementPrefix = keyHandlers[i].filter.prefix;
					if (elementName && (!elementPrefix || elementName.startsWith(elementPrefix))) {
						Aranea.Logger.debug("Invoking key handler: " + handlerFn);
						var cont = handlerFn(event, elementName);
						if (cont == false) {
							return false;
						}
					}
				}
			}

			return true;
		},

		// Note that by the default behaviour the event handlers with the longest matching element name are returned.
		// Those event listeners that match less (or not at all), are not returned.
		getElementHandlers: function(element) {
			var elementName = element ? element.name : '';
			var elPrefix = elementName;
			var elHandlers = null;

			while (elPrefix) {
				if (this.handlers.get(elPrefix)) {
					elHandlers = this.handlers.get(elPrefix);
					break;
				}
				var i = elPrefix.lastIndexOf('.');
				elPrefix = i >= 0 ? elPrefix.substring(0, i) : null;
			} 

			return elHandlers;
		}
	})
};

/**
 * This variable will hold the handlers for the Keypress event.
 */
Aranea.Keyboard.HANDLER_REGISTRY = new Aranea.Keyboard.UiHandlerRegistryImpl();

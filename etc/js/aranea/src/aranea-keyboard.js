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

	/**
	 * Creates an object that stores the information about which kind of event an event handler is expected to respond.
	 * 
	 * @param elementPrefix An optional element prefix to monitor. The ID of the event target must have the same prefix.
	 * @param eventCond A String (e.g. 'alt+enter') or integer (13) to describe the keyboard event.
	 * @return An object holding the data that was given as input (properties: prefix, keyCode, ctrl, alt, shift, meta).
	 * @since 2.0
	 */
	createFilterObject: function(elementPrefix, eventCond) {
		var result = { prefix: elementPrefix };
		var filters = ('' + (eventCond || '')).split('+');

		$A(filters).each(function(filter) {
			filter = filter.toLowerCase();

			if (Aranea.Keyboard.isMetaStr(filter)) {
				result[filter] = true;
			} else {
				// Store the key code under "filter" property "key":
				if (parseInt(filter)) { // If it's a number, use it instead.
					result.keyCode = parseInt(filter);
				} else {
					result.keyCode = Aranea.Keyboard.SPECIAL_KEYS[filter];
				}
			}
		});

		result.toString = function() {
			var str = [];
			if (this.prefix) {
				str.push('[prefix:"');
				str.push(this.prefix);
				str.push('"]');
			}
			$A(['ctrl', 'alt', 'shift', 'meta', 'keyCode']).each(function(prop) {
				var value = this[prop];
				if (value != null) {
					str.push('[');
					str.push(typeof value == 'boolean' ? prop : value);
					str.push(']');
				}
			}.bind(this));
			return str.join('');
		};

		return result;
	},

	/**
	 * Internally used function to test whether the given filter string holds a control key.
	 * 
	 * @param filterStr The string to test. May be null.
	 * @return true, when the filterStr represents alt, ctrl, shift, or meta key.
	 * @since 2.0
	 */
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
	 *     eventCond     - the key number or a case-insensitive string (e.g. "ctrl+shift+a", "alt+enter") representing
	 *                     the key press to react to.
	 *     handler       - function(event, elementId) to receive the notification.
	 *                     function's return value determines whether it allows other handlers to be invoked.
	 *                     when it is false, the called handler is the last one to be invoked.
	 * @since 1.1
	 */
	registerKeypressHandler: function(elementPrefix, eventCond, handler) {
		if (arguments.length != 3) {
			throw('Aranea.Keyboard.registerKeypressHandler: expected 3 parameters instead of ' + arguments.length);
		} else if (!Object.isNumber(eventCond) && !Object.isString(eventCond)) {
			throw('Aranea.Keyboard.registerKeypressHandler: "eventCond" (as int or String) parameter must be provided!');
		} else if (!Object.isFunction(handler)) {
			throw('Aranea.Keyboard.registerKeypressHandler: event-handler-function must be provided!');
		}

		var filter = Aranea.Keyboard.createFilterObject(elementPrefix, eventCond);

		Aranea.Logger.debug('Registering handler for condition ' + filter + '.');

		Aranea.Keyboard.HANDLER_REGISTRY.addHandler(filter, handler);

		// The global "keydown" event listener that will process our registered events:
		if (!Aranea.Data.globalKeyDownListenerRegistered) {
			document.observe('keydown', Aranea.Keyboard.handleKeypress);
			Aranea.Data.globalKeyDownListenerRegistered = true;
		}
	},

	/**
	 * This is a global event listener function. It will respond to the events to which it was registered in
	 * Aranea.Keyboard.registerKeypressHandler().
	 *
	 * @since 1.1
	 */
	handleKeypress: function (event) {
		if (!event) {
			throw('Event listener Aranea.Keyboard.handleKeypress requires the "event" parameter!');
		}

		Aranea.Logger.debug([event.type,' detected! [event.charCode=',event.charCode,', event.keyCode=',event.keyCode,
					', ctrlKey=',event.ctrlKey,', altKey=',event.altKey,', kev.metaKey=',event.metaKey,']'].join(''));

		try {
			// The registry takes care of calling the right handlers:
			var result = Aranea.Keyboard.HANDLER_REGISTRY.invokeHandlers(event);
			if (result == false) {
				event.stop(); // When an handler returned false then the intent was to block event propagation.
			}
		} catch (e) {
			// Keyboard handler errors may be thrown after AJAX region updates.
			Aranea.Logger.warn("Aranea.Keyboard.handleKeypress: a handler threw error (non-critical): " + e.message, e);
		}
	},

	/** 
	 * Returns whether the event matches the filter conditions.
	 * @since 1.0.11
	 */
	isMatch: function(event, filter) {
		var result = true;
		result = result && (filter.shift == null || filter.shift == event.shiftKey);
		result = result && (filter.alt == null || filter.alt == event.altKey);
		result = result && (filter.ctrl == null || filter.ctrl == event.ctrlKey);
		result = result && (filter.meta == null || filter.meta == event.metaKey);

		if (filter.keyCode) {
			result = result && filter.keyCode == (event.keyCode || event.charCode);
		}

		if (filter.prefix) {
			var id = (event.element() || {}).id || '';
			result = result && (id == filter.prefix || id.startsWith(filter.prefix + '.'));
		}

		Aranea.Logger.debug('Aranea.Keyboard.isMatch: was the keypress the one expected (' + filter + '): ' + result);

		return result;
	},

	/**
	 * Creates a function (keyboard event handler) that fires event (eventName) on given element when the occurred event
	 * is not the same event.
	 * 
	 * @param element The element on which the target event will be fired.
	 * @param eventName The name of the event supported by element that will be fired.
	 * @return The created event handler.
	 * @since 2.0
	 */
	createElementEventHandler: function(element, eventName) {
		if (!element) {
			throw('Aranea.Keyboard.createElementEventHandler: The parameter "element" is required!');
		} else if (!eventName) {
			throw('Aranea.Keyboard.createElementEventHandler: The parameter "eventName" is required!');
		}

		var handler = function(expectedElement, expectedEvent, event, elementId) {
			if (elementId != expectedElement && event.type != expectedEvent) {
				$(expectedElement)[expectedEvent]();
				return false;
			}
		};

		return handler.curry(element, eventName);
	},

	/**
	 * Returns the function that stops received keyboard event propagation if the input was not allowed by filter.
	 * 
	 * @since 1.0.11
	 */
	getKeyboardInputFilterFunction: function(filter) {
		return function(event) {
			Aranea.Logger.debug([event.type,' detected! [event.charCode=',event.charCode,', event.keyCode=',event.keyCode,
				', ctrlKey=',event.ctrlKey,', altKey=',event.altKey,', kev.metaKey=',event.metaKey,']'].join(''));

			var char = String.fromCharCode(event.charCode ? event.charCode : event.keyCode);

			if (char) {
				if (filter.indexOf(char) >= 0) {
					event.stop();
				}
			}
		};
	},

	/** 
	 * Stores a registry of event handlers together with their filter data objects. Takes care of invoking them based on
	 * the current event.
	 */
	UiHandlerRegistryImpl: Class.create({

		handlers: $H(), // This maps from keyCode or element prefix to array of pairs (filter, handler)
 
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
			// Otherwise use old-fashioned by-key-code registered handler.
			var key = Object.isNumber(filter.keyCode) ? filter.keyCode : 0;
			if (this.handlers.get(key)) {
				this.handlers.get(key).push(traditionalHandler);
			} else {
				this.handlers.set(key, [ traditionalHandler ]);
			}
		},

		reset: function() {
			this.handlers = $H();
		},

		/**
		 * Invokes all handlers registered for given key-code and with matching element prefix.
		 * if a handler returns false, the remaining handlers are not invoked
		 */
		invokeHandlers: function(event) {
			var elementName = event.element().name;
			var keyCode = event.keyCode ? event.keyCode : event.which;
			var handlers = this.handlers.get(keyCode);

			if (handlers) {
				Aranea.Logger.debug('Searching handlers, count=' + handlers.length);

				for (var i = handlers.length - 1; i >= 0; i--) {
					if (Aranea.Keyboard.isMatch(event, handlers[i].filter)) {
						Aranea.Logger.debug('Invoking keyboard event handler: ' + (i + 1) + '/' + handlers.length);

						var cont = handlers[i].handler(event, elementName);
						if (cont == false) {
							Aranea.Logger.debug('Handler returned false. Returning to the closest exit.');
							return false;
						}
					}
				}
			}
		}
	})
};

/**
 * This variable will hold the handlers for the key-press event.
 */
Aranea.Keyboard.HANDLER_REGISTRY = new Aranea.Keyboard.UiHandlerRegistryImpl();

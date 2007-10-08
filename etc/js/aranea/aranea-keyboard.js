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
 *   - top-level handler routines. (uiHandleKeypress, and someday will perhaps contain handlers
 *                                                    for other events)
 *   - subhandler registration routines. (uiRegisterKeypressHandler)
 *   - helper structure for holding registered handlers (UiHandlerRegistry)
 */
 
// Parts of code lifted from http://www.openjs.com/scripts/events/keyboard_shortcuts/
// shortcut library (version 1.00.A by Binny V A) distributed under BSD license.
 
 // ------------------------------- Keyboard Events ---------------------------------- //
 
 
/** @since 1.1 */
Aranea.KB = {};

//NOTE by TP: this actually is only correct for one keyboard layout only...
/** @since 1.1 */
Aranea.KB.special_keys = {
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
};


//Work around for stupid Shift key bug created by using lowercase - as a result the shift+num combination was broken
//NOTE by TP: this actually is only correct for one keyboard layout only...
/** @since 1.1 */
Aranea.KB.shift_nums = {
	"`":"~",
	"1":"!",
	"2":"@",
	"3":"#",
	"4":"$",
	"5":"%",
	"6":"^",
	"7":"&",
	"8":"*",
	"9":"(",
	"0":")",
	"-":"_",
	"=":"+",
	";":":",
	"'":"\"",
	",":"<",
	".":">",
	"/":"?",
	"\\":"|"
}
 
 /**
  * This function will receive all keypress events from all form elements
  *
  * Parameters:
  *     event  - the event object
  *     formElementId - full (unique) id of the element that received the event.
  */
/** @since 1.1 */
Aranea.KB.handleKeypress = function (event, formElementId) {
 	 if (!event) return;

 	 var keyCode;
 	 if (event.keyCode) keyCode = event.keyCode;
 	 else keyCode = event.which; // Mozilla
 	 
 	 var result = true;
 	 try {
	 	 result = uiKeypressHandlerRegistry.invokeHandlers(formElementId, keyCode, event);	 	 
 	 }
 	 catch (e) { 	
        //Keyboard handler errors may be thrown after AJAX region updates.
 	  	window.status = "Keyboard handler error (non-critical): " + e;
 	 }
 	 
 	 if (result != undefined) {
 	 	if (result == false) {
 			event.cancelBubble = true;
 			event.returnValue = false;
 			return false;
 	 	}
 	 }

 	 return true;
}
var uiHandleKeypress = Aranea.KB.handleKeypress;

 /**
  * Registers a handler for the keypress event.
  * Parameters:
  *     elementPrefix - the handler will be triggered when this matches the prefix of the 
  *                     id of the target form element. E.g. when elementPrefix is 'someForm.someSubelement'
  *                     the handler will be triggered for form elements 'someForm.someSubelement.someElement', etc.
  *     keyCode       - the keyCode to trigger on.
  *     handler       - function(event, formElementId) to receive the notification.
  *                     function's return value determines whether it allows other handlers to be invoked.
  *                     when it is false, the called handler is the last one to be invoked.
  */
/** @since 1.1 */
Aranea.KB.registerKeypressHandler = function(elementPrefix, keyCode, handler) {
  // if elementPrefix is '', we register a global keypress handler.
  if (elementPrefix == '') {
    if (typeof keyCode == 'number')
      Event.observe(document, "keydown", function(event) {if (event.which == keyCode) handler(event, '');});
    else
      Event.observe(document, "keydown", function(event) { handler(event, '');}, true);
  }
  else {
    // else we just store it in the registry
    uiKeypressHandlerRegistry.addHandler(elementPrefix, keyCode, handler);
  }
};
var uiRegisterKeypressHandler = Aranea.KB.registerKeypressHandler;

 /** 
  * A map: keyCode -> list of (elementPrefix, handler) to store handlers and invoke them.
  */
Aranea.KB.UiHandlerRegistry = function() {
   this.handlers = new Object(); // This maps from keyCode to array of pairs (elementPrefix, handler)
 }
 
 /** 
  * Adds a new handler to the registry
  */
Aranea.KB.UiHandlerRegistry.prototype.addHandler = function(elementPrefix, keyCode, handler) {
   var newHandler = {
        elementPrefix: elementPrefix,
        handler: handler
   };
   
   if (this.handlers[keyCode]) { 
   	  // add handler to list
   	  var a = this.handlers[keyCode];
   	  a[a.length] = newHandler;
   }
   else {
      // this is the first handler in the list
   	  this.handlers[keyCode] = new Array(newHandler);
   }
 };
 
 /**
  * Invokes all handlers registered for given keycode and with matching elementprefix.
  * if a handler returns false, the remaining handlers are not invoked
  */
 Aranea.KB.UiHandlerRegistry.prototype.invokeHandlers = function(element, keyCode, event) {
   var handlers = this.handlers[keyCode];
   if (handlers){
    var length = handlers.length;
	  for (i = length-1; i >= 0; i--){
         var handlerFunction = handlers[i].handler;
         var elementPrefix   = handlers[i].elementPrefix;
         if (elementPrefix == element.substring(0, elementPrefix.length)) {
            var result = handlerFunction(event, element);
            return result;
	     }
	  }
   }
 };
 
/**
 * This variable will hold the handlers for the Keypress event.
 */
var uiKeypressHandlerRegistry = new Aranea.KB.UiHandlerRegistry();

var aranea_keyboardinputfilter_last_keydown_keycode = null;
/** 
 * Returns the function that stops received keyboard event 
 * propagation if the input was not allowed by filter. 
 * @since 1.0.11 */
Aranea.KB.getKeyboardInputFilterFunction = function(filter) {
  var f = function(kev) {
  	araneaPage().getLogger().debug(kev.type + " detected!" + "event.charCode="+kev.charCode+", event.keyCode="+kev.keyCode+".");
  	araneaPage().getLogger().debug("ctrlKey="+kev.ctrlKey + " altKey="+kev.altKey + " kev.metaKey=" + kev.metaKey);
  	if (kev.type == "keydown") {
      aranea_keyboardinputfilter_last_keydown_keycode = kev.keycode;
      return true;
  	}
    
    if (kev.ctrlKey) 
      return; // Does this mess with AltGr on some browsers?
  	
    var charcode = kev.charCode;
    araneaPage().getLogger().debug("Handling keypress. Last keydown code=" + aranea_keyboardinputfilter_last_keydown_keycode + ". event.charCode="+charcode+", event.keyCode="+kev.keyCode+". Active filter="+filter);
  
    // if charcode is present, we assume it to be correct (at the time of writing this
    // only gecko based browsers seem to set the charcode properly).
    if (charcode) {
      var ch = String.fromCharCode(charcode);
      if (filter.indexOf(ch) == -1) {
        Event.stop(kev);
      }
      return;
    } else if (Prototype.Browser.Gecko) {
      /* when charcode was not set in gecko, we are dealing with 
         some special key that should be just let through */
      return true;
    }

    var lastkeycode = aranea_keyboardinputfilter_last_keydown_keycode;
    var keycode = kev.keyCode;

    var ch = String.fromCharCode(keycode);
    araneaPage().debug("No charcode. Falling back to keycode which will be assumed to be the charcode. Detected char '"+ch+"'");
    if (filter.indexOf(ch) == -1) {
      if (keycode != 8 && keycode != 13) {
        Event.stop(kev);
      }
      return;
    }
  }

  return f;
};

var getKeyboardInputFilterFunction = Aranea.KB.getKeyboardInputFilterFunction;

Aranea.KB.NewHandler = function(shortcut, callback) {
    var f2 = function(event, element) {//
	  	var e = event;
	
		araneaPage().debug("Running keyboard handler for combo '"+shortcut+ "'");
	
		//Find Which key is pressed
		if (e.keyCode) code = e.keyCode;
		else if (e.which) code = e.which;
		var character = String.fromCharCode(code).toLowerCase();
	
		var keys = shortcut.toLowerCase().split("+");
		//Key Pressed - counts the number of valid keypresses - if it is same as the number of keys, the shortcut function is invoked
		var kp = 0;
	
		//Special Keys - and their codes
	
		for(var i=0; k=keys[i],i<keys.length; i++) {
			//Modifiers
			if(k == 'ctrl' || k == 'control') {
				if(e.ctrlKey) { kp++;
				araneaPage().debug("Detected CTRL"); }
			} else if(k ==  'shift') {
				if(e.shiftKey) { kp++;
				araneaPage().debug("Detected SHIFT"); }
			} else if(k == 'alt') {
				if(e.altKey)  { 
					kp++ ;
					araneaPage().debug("Detected SHIFT");
				}
			} else if(k.length > 1) { //If it is a special key
				if(Aranea.KB.special_keys[k] == code) { kp++;
					araneaPage().debug("Detected special key");
				}
	
			} else { //The special keys did not match
				if(character == k) kp++;
				else {
					if(Aranea.KB.shift_nums[character] && e.shiftKey) { //Stupid Shift key bug created by using lowercase
						character = Aranea.KB.shift_nums[character]; 
						if(character == k) { kp++;
							araneaPage().debug("Detected shifted key");
						}
					}
				}
			}
		}

		if(kp == keys.length) {
			callback(e);
			araneaPage().debug("Ably captured keyboard event and exectued handler.");
	
			//if(!opt['propagate']) { //Stop the event
				//Event.stop(e);
				return true;
			//}
		}
		
		return true;
    }; //
  
  return f2;
};

Aranea.KB.registerKeyComboHandler = function(elementPrefix, keyCombo, handler) {
  var extraHandler = new Aranea.KB.NewHandler(keyCombo, handler);
  araneaPage().debug("Registered key combo handler for '" + keyCombo + "'");
  Aranea.KB.registerKeypressHandler(elementPrefix, keyCombo, extraHandler);
};

window['aranea-keyboard.js'] = true;

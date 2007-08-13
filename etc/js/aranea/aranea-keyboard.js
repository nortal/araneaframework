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
 * Aranea event handlers.
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
 
 // ------------------------------- Keyboard Events ---------------------------------- //
 
 /**
  * This function will receive all keypress events from all form elements
  *
  * Parameters:
  *     event  - the event object
  *     formElementId - full (unique) id of the element that received the event.
  */
 function uiHandleKeypress(event, formElementId) {
 	 // Check the keyCode
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
 	 	}
 	 }

 	 return result;
}


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
 function uiRegisterKeypressHandler(elementPrefix, keyCode, handler) {
 
    // if elementPrefix is '', we register a global keypress handler.
    if (elementPrefix == '') {
      if (document.addEventListener) { // W3C/Mozilla
      	 document.addEventListener('keydown', function(event) { if (event.which == keyCode) handler(event, ''); }, false);
      }
      else { // IE
         document.attachEvent('onkeydown', function() { if (window.event.keyCode == keyCode) handler(window.event, ''); });
      }
    }
    else {
      // else we just store it in the registry
	 		uiKeypressHandlerRegistry.addHandler(elementPrefix, keyCode, handler);
    }
 }


 /** 
  * A map: keyCode -> list of (elementPrefix, handler) to store handlers and invoke them.
  */
 function UiHandlerRegistry() {
   this.handlers = new Object(); // This maps from keyCode to array of pairs (elementPrefix, handler)
 }
 
 /** 
  * Adds a new handler to the registry
  */
 UiHandlerRegistry.prototype.addHandler = function(elementPrefix, keyCode, handler) {
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
 }
 
 /**
  * Invokes all handlers registered for given keycode and with matching elementprefix.
  * if a handler returns false, the remaining handlers are not invoked
  */
 UiHandlerRegistry.prototype.invokeHandlers = function(element, keyCode, event) {
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
 }
 
 /**
  * This variable will hold the handlers for the Keypress event.
  */
 var uiKeypressHandlerRegistry = new UiHandlerRegistry();

var aranea_keyboardinputfilter_last_keydown_keycode = null;
/** 
 * Returns the function that stops received keyboard event 
 * propagation if the input was not allowed by filter. 
 * @since 1.0.11 */
function getKeyboardInputFilterFunction(filter) {
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
      Event.stop(kev);
      return;
    }
  }

  return f;
}

window['aranea-keyboard.js'] = true;
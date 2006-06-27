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
 * Jsp-Ui-Events.js
 *
 * Jsp-Ui event handlers.
 * 
 * The functions presented here are invoked from the custom tags of
 * JSP-UI.
 * Therefore you CAN'T be too liberal with them unless you know what you are doing.
 *
 * Created: 29.12.2004
 * Author: Konstantin Tretyakov.
 *
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
 *
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
 
 /*	Defines up/down key navigation actions on
	radiobuttons. Works assuming that all
	radiobutton group button id-s are named like
	"somePrefix" + parseInt(orderString), where
	ordering integers are sequential. 
	If keyCode is not up or down, passes the event
	forward to uiHandleKeypress.
	*/
	function radioButtonOnUpDownKey(event, formElementId) {
		var e;
		if (event) { e = event; }  else { e = window.event; }
        var current = e.target || e.srcElement;
        
        // theoretically  should stop the further event propagation...
        // use onkeypress not onkeydown to prevent IE from
        // jumping 2 radiobuttons at a time.
        e.cancelBubble = true;
        if (e.stopPropagation) e.stopPropagation();
       
        if (e.keyCode) {
            keyCode = e.keyCode; // ie, opera
        } else if (xDef(e.which)) { keyCode = e.which; } // moz/fb

        // extract the prefix & suffix(integer) from radiobutton ID
        for (var i = 0; i< current.id.length; i++) {
            c = current.id.toUpperCase().charAt(i);
            if (c < 'A' || c > 'Z')
                break;
        }
        idPrefix = current.id.substring(0,i);
        pos = current.id.substring(i);

        // navigate to next button if up/down keys pressed...
        if (e.keyCode == 40 || e.keyCode == 38) { // DOWN || UP
            nextpos = e.keyCode == 40 ? parseInt(pos) + 1 : parseInt(pos) - 1;
            nextbutton = document.getElementById(idPrefix + nextpos.toString());
            if (nextbutton) {
                unCheckRadioButton(current);
                checkRadioButton(nextbutton);
                nextbutton.focus();
            }
        } else {
        	uiHandleKeypress(e, formElementId)
        }
    }

    function checkRadioButton(b) {
        b.checked = true;
    }

    function unCheckRadioButton(b) {
        b.checked = false;
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
 

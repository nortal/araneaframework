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

//
// UI event handling routines.
//

/**
 * This variable shows if page is active.
 * Page becomes inactive after a form is submitted .
 *
 * @author Oleg M?rk
 */
var pageActive = true;

/**
 * This array contains client events that were registered on page load.
 */
var clientLoadEvents = new Array();
/**
 * This array contains system events that were registered on page load.
 */
var systemLoadEvents = new Array();

/** array for system events that should happen when page unload event occurs */
var systemUnloadEvents = new Array();

/** standard parameters, sent to widget container submit function upon event */
function getStandardParameterObject(systemForm, widgetId, eventId, eventParam) {
  result = new Object();
  result.systemForm = systemForm;
  result.widgetId = widgetId;
  result.eventId = eventId;
  result.eventParam = eventParam;
  return result;
}

/**
 * Submit event in a given system form
 *
 * @author Oleg Mürk
 */
function uiStandardSubmitEvent(systemForm, widgetId, eventId, eventParam, call, precondition) {
	if (!pageActive) return false;
	
	if (precondition && !precondition()) return false;
	
	standardParams = getStandardParameterObject(systemForm, widgetId, eventId, eventParam);

	call();
	
	return false;
}


/**
 * Submit form event in a given system form. Validates form if needed.
 *
 * @author Oleg Mürk
 */
function uiStandardSubmitFormEvent(systemForm, formId, elementId, eventId, eventParam, validate, call, precondition) {	
	if (!pageActive) return false;
	
	if (validate) {
	  var valid = uiValidateForm(systemForm, formId);
	  if (!valid) return false;
	}

	if(precondition && !precondition()) return false;
	
	standardParams = getStandardParameterObject(systemForm, formId+"."+elementId, eventId, eventParam);

	call();
	
	return false;	
}

/**
 * Pops up calendar.
 *
 * @param ititiator - object that initiated the function
 * @param srcObject - input element that contains date to use
 * @param dstObject - input element to store selected date to
 * @param format - date format.
 *
 * @author Oleg M?rk
 */
function uiPopUpCalendar(initiator, srcObject, dstObject, format, callback) {
	popUpCalendar(initiator, srcObject, dstObject, format, callback);
}

/**
 * Adds event invoked by client to clientLoadEvents array.
 * @param event - event that was invoked by client.
 *
 * @author Maksim Boiko
 */
function addClientLoadEvent(eventFunction) {
	var length = clientLoadEvents.length;
	clientLoadEvents[length] = eventFunction;
}

/**
 * Adds system event to systemLoadEvents array.
 * @param event - event that was invoked in system.
 *
 * @author Maksim Boiko
 */
function addSystemLoadEvent(eventFunction){
	var length = systemLoadEvents.length;
	systemLoadEvents[length] = eventFunction;
}

function addSystemUnloadEvent(eventFunction) {
	var length = systemUnloadEvents.length;
	systemUnloadEvents[length] = eventFunction;
}

/**
 * Executes system and client events on body load.
 *
 * @author Maksim Boiko
 */
function processLoadEvents() {	
	//process firstly system events 
  for(var i=0; i<systemLoadEvents.length; i++) {
  	processEvent(systemLoadEvents[i]);
  }
  
  systemLoadEvents = new Array();
  
  //client events
  for(var i=0; i<clientLoadEvents.length; i++) {
  	processEvent(clientLoadEvents[i]);
  }
  
  clientLoadEvents = new Array();
}

function processUnloadEvents() {
  //process firstly system events 
  for(var i=0; i<systemUnloadEvents.length; i++) {
  	processEvent(systemUnloadEvents[i]);
  }
  
  systemUnloadEvents = new Array();
}

function processEvent(event) {
	if (typeof event != 'function') {
    event;
  } else {
  	event();
  }
}

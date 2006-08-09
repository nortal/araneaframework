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

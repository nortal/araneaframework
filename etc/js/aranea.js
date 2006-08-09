function AraneaStore() {
  var objects = new Array();

  this.add = function(object) {
    var len = objects.length;
	objects[len] = object;
  }
  
  this.clear = function() {
  	objects = new Array();
  }
  
  this.length = function() {
    return objects.length;
  }

  this.getContents = function() {
    return objects;
  }
  
  this.forEach = function(f) {
    for(var i = 0; i < objects.length; i++) {
      f(objects[i]);
    }
  }
}

AraneaEventStore.prototype = new AraneaStore();
function AraneaEventStore() {
  var that = this;

  var processEvent = function(event) {
    if (typeof event != "function") {
      event;
    } else {
      event();
    }
  }

  this.execute = function() {
    that.forEach(processEvent);
    that.clear();
  }
}

// AraneaPage object is present on each page served by Aranea and contains common
// functionality for setting page related variables, events and functions.
function AraneaPage() {
  var that = this;
  /** Variable that shows if page is active (form has not been submitted yet). */
  var pageActive = true;

  /** Variables holding different (un)load events that should be executed when page loads (body unload or alike) */
  var systemLoadEvents = new AraneaEventStore();
  var clientLoadEvents = new AraneaEventStore();
  var systemUnLoadEvents = new AraneaEventStore();
  
  traverser = new AraneaTraverser();

  loadEvents = new AraneaStore();
  loadEvents.add(systemLoadEvents);
  loadEvents.add(clientLoadEvents);
  
  unloadEvents = new AraneaStore();
  unloadEvents.add(systemUnLoadEvents);
  
  submitCallbacks = new Object();

  /** Return whether this page is active (has not been submitted yet). */
  this.isPageActive = function() {
    return pageActive;
  }

  /** Sets page status. */
  this.setPageActive = function(active) {
    if (typeof active == "boolean")
      pageActive = active;
  }

  this.addClientLoadEvent = function(event) {
  	clientLoadEvents.add(event);
  }
  
  this.addSystemLoadEvent = function(event) {
  	systemLoadEvents.add(event);
  } 
  
  this.onload = function() {
    loadEvents.forEach(function(eventHolder) {eventHolder.execute();});
  }

  this.onunload = function() {
    unloadEvents.forEach(function(eventHolder) {eventHolder.execute();});
  }
  
  // SUBMIT CALLBACKS
  this.addSubmitCallback = function(callback) {
    that.addSystemFormSubmitCallback('callbacks', callback);
  }
  
  this.addSystemFormSubmitCallback = function(systemFormId, callback) {
    if (!submitCallbacks[systemFormId])
	  submitCallbacks[systemFormId] = new AraneaEventStore();
	submitCallbacks[systemFormId].add(callback);
  }
  
  this.executeCallbacks = function(systemFormId) {
    if (that.submitCallbacks['callbacks'])
	  that.submitCallbacks['callbacks'].execute();
    
	if (that.submitCallbacks[systemFormId])
	  that.submitCallbacks[systemFormId].execute();
  }
  // END SUBMIT CALLBACKS  
  
  this.submit = function(element, submitter) {
    if (!that.isPageActive())
	  return false;
    return submitter(element);
  }
  
  this.standardSubmit = function(element) {
  	// systemform information
    var systemForm = this.traverser.findSurroundingSystemForm(element);
	var systemFormId = systemForm['id'];
	
	// event information
    var widgetId = element.getAttribute('arn-trgtwdgt');
    var eventId = element.getAttribute('arn-evntId');
    var eventParam = element.getAttribute('arn-evntPar');
	var updateRegions = element.getAttribute('arn-updrgns');

    systemForm.widgetEventPath.value = widgetId ? widgetId : "";
    systemForm.widgetEventHandler.value = eventId ? eventId : "";
    systemForm.widgetEventParameter.value = eventParam ? eventParam : "";
	
	// execute submit callbacks, first toplevel ones and then systemform specific
	that.executeCallbacks(systemFormId);
	
	if (window[ajaxKey] && updateRegions && updateRegions.length > 0) {
		window[ajaxKey].updateRegions = updateRegions;
		window[ajaxKey].systemForm = systemForm;
		window[ajaxKey].submitAJAX();
	}
	else {
      that.setPageActive(false);
      systemForm.submit();
      return false;
	}
  }
}

function AraneaTraverser() {
  /* returns FORM that is Aranea system form and surrounds given HTML element. */
  this.findSurroundingSystemForm = function(element) {
    do {
      if (element.tagName && element.tagName.toUpperCase() == 'FORM' && element.getAttribute('arn-systemForm'))
	    return element;
      element = element.parentNode;
  	} while (element);

	return null;
  }
}
  
/*		systemForm, 
		widgetId, 
		eventId, 
		eventParam, */ 


// OLD

/*
 * function uiSystemFormSubmit(systemForm, updateRegions){
  if (!systemForm) return;

  if (systemForm.uiProperties && systemForm.uiProperties.submitCallbacks) {
	  var callbacks = systemForm.uiProperties.submitCallbacks;
	  
	  var i;
	  for (i = 0; i < callbacks.length; i++){
	  	var f = callbacks[i];
			f();
	  }  
  }
	
	if (updateRegions && updateRegions.length > 0) {
		window[ajaxKey].updateRegions = updateRegions;
		window[ajaxKey].systemForm = systemForm;
		window[ajaxKey].submitAJAX();
	}
	else {
		pageActive = false;	
		systemForm.submit();		
	}
}
 */
/*
function uiStandardSubmitEvent(systemForm, widgetId, eventId, eventParam, call, precondition) {
	if (!pageActive) return false;
	
	if (precondition && !precondition()) return false;
	
	standardParams = getStandardParameterObject(systemForm, widgetId, eventId, eventParam);

	call();
	
	return false;
}

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
}*/


// new aranea page
_ap = new AraneaPage();

function getActiveAraneaPage() {
  return _ap;
}

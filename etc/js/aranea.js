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
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

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
  
  /** Url of aranea dispatcher servlet serving current page. Set by AraneaTraverser. */ 
  var servletURL = null;
  
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
  
  /** servlet url getters and setters */
  this.getServletURL = function() {
  	return servletURL;
  }
  
  this.setServletURL = function(url) {
  	servletURL = new String(url);
  }

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

  this.addSystemUnLoadEvent = function(event) {
  	systemUnLoadEvents.add(event);
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
    if (submitCallbacks['callbacks'])
	  submitCallbacks['callbacks'].execute();
    
	if (submitCallbacks[systemFormId])
	  submitCallbacks[systemFormId].execute();
  }
  // END SUBMIT CALLBACKS  

  this.submit = function(element) {
    if (!that.isPageActive())
	  return false;

	var systemForm = that.getTraverser().findSurroundingSystemForm(element);
    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    return (this.findSubmitter(element, systemForm)).submit(element);
  }

  /** 
   * This function could be overwritten to support additional
   * submit methods.
   */
  this.findSubmitter = function(element, systemForm) {
    var updateRegions = element.getAttribute('arn-updrgns');

	if (window.AraneaAA_Present && updateRegions && updateRegions.length > 0)
	  return new DefaultAraneaAJAXSubmitter(systemForm);

	return new DefaultAraneaSubmitter(systemForm);
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
	
	if (window.AraneaAA_Present && updateRegions && updateRegions.length > 0) {
		window[ajaxKey].updateRegions = eval("new Array(" + updateRegions + ");");
		window[ajaxKey].systemForm = systemForm;
		window[ajaxKey].submitAJAX();
	}
	else {
      that.setPageActive(false);
      systemForm.submit();
      return false;
	}
  }

  // GETTERS
  this.getTraverser = function() {
  	return traverser;
  }
}

function AraneaTraverser() {
  /* Returns FORM that is Aranea system form and surrounds given HTML element. 
   * When servlet URL is not known to current aranea page, sets it according to information from system form. 
   * Should be overriden with fast constant function when using just one system form. */
  this.findSurroundingSystemForm = function(element) {
    do {
      if (element.tagName && element.tagName.toUpperCase() == 'FORM' && element.getAttribute('arn-systemForm')) {
	  	// if aranea page is still unaware of servlet url.
	  	if (!getActiveAraneaPage().getServletURL() && element['servletURL'])
		  getActiveAraneaPage().setServletURL(element['servletURL'].value);
	    return element;
	  }
      element = element.parentNode;
  	} while (element);

	return null;
  }
}

function DefaultAraneaSubmitter(sysform) {
  var systemForm = sysform;
	
  this.submit = function(element) {
	var systemFormId = systemForm['id'];

	// event information
    var widgetId = element.getAttribute('arn-trgtwdgt');
    var eventId = element.getAttribute('arn-evntId');
    var eventParam = element.getAttribute('arn-evntPar');
	var updateRegions = element.getAttribute('arn-updrgns');

    systemForm.widgetEventPath.value = widgetId ? widgetId : "";
    systemForm.widgetEventHandler.value = eventId ? eventId : "";
    systemForm.widgetEventParameter.value = eventParam ? eventParam : "";

    getActiveAraneaPage().setPageActive(false);
    systemForm.submit();

    return false;
  }
}

function DefaultAraneaAJAXSubmitter(sysform) {
  var systemForm = sysform;

  this.submit = function(element) {
    var systemFormId = systemForm['id'];
	
	// event information
    var widgetId = element.getAttribute('arn-trgtwdgt');
    var eventId = element.getAttribute('arn-evntId');
    var eventParam = element.getAttribute('arn-evntPar');
	var updateRegions = element.getAttribute('arn-updrgns');

    systemForm.widgetEventPath.value = widgetId ? widgetId : "";
    systemForm.widgetEventHandler.value = eventId ? eventId : "";
    systemForm.widgetEventParameter.value = eventParam ? eventParam : "";
	
	window[ajaxKey].updateRegions = eval("new Array(" + updateRegions + ");");
	window[ajaxKey].systemForm = systemForm;
	window[ajaxKey].submitAJAX();
	
	return false;
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
_ap.addSystemLoadEvent(Behaviour.apply);

function getActiveAraneaPage() {
  return _ap;
}

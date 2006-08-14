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
 
/* AraneaTraverser is locator for some Aranea specific elements in DOM tree. */
function AraneaTraverser() {
  /* Returns FORM that is Aranea system form and surrounds given HTML element. 
   * When servlet URL is not yet known to current aranea page, sets its according to
   * acquired from from system form. 
   * Should be overriden with fast constant function when using just one system form. */
  this.findSurroundingSystemForm = function(element) {
    do {
      if (element.tagName && element.tagName.toLowerCase() == 'form' && element.getAttribute('arn-systemForm')) {
	    return element;
	  }
      element = element.parentNode;
  	} while (element);

	return null;
  }
}

AraneaTraverser.prototype.getElementAttribute = function (element, attributeName) {
  //return (!element[attributeName] || element[attributeName] == "") ? null : result;
  return element.getAttribute(attributeName);
}
AraneaTraverser.prototype.getEventTarget = function(element) {
  return this.getElementAttribute(element, 'arn-trgtwdgt');
}
AraneaTraverser.prototype.getEventId = function(element) {
  return this.getElementAttribute(element, 'arn-evntId');
}
AraneaTraverser.prototype.getEventParam = function(element) {
  return this.getElementAttribute(element, 'arn-evntPar');
}
AraneaTraverser.prototype.getEventUpdateRegions = function(element) {
  return this.getElementAttribute(element, 'arn-updrgns');
}
AraneaTraverser.prototype.getEventPreCondition = function(element) {
  return this.getElementAttribute(element, 'arn-evntCond');
}

// END AraneaTraverser

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

function AraneaEventStore() {
  var araneaEventStore = function() {
    var processEvent = function(event) {
      if (typeof event != "function") {
        event;
      } else {
        event();
      }
    }

    this.execute = function() {
      this.forEach(processEvent);
      this.clear();
    }
  }
  
  araneaEventStore.prototype = new AraneaStore();
  return new araneaEventStore();
}

/* AraneaPage object is present on each page served by Aranea and contains common
 * functionality for setting page related variables, events and functions. */
function AraneaPage() {
  /* make object accessible to private functions. */
  var that = this;

  // PRIVATE FUNCTIONS
  function executor(eventHolder) { eventHolder.execute(); }

  /* URL of aranea dispatcher servlet serving current page. 
   * Automatically set by AraneaTraverser.findSurroundingSystemForm(). */ 
  var servletURL = null;
  this.getServletURL = function() { return this.servletURL; }
  this.setServletURL = function(url) { this.servletURL = new String(url); }
  
  /* Indicates whether the page is completely loaded or not. Page is considered to 
   * be loaded when all onload events have completed execution. */
  var loaded = false;
  this.isLoaded = function() { return this.loaded; }
  this.setLoaded = function(b) { if (typeof b == "boolean") { this.loaded = b; } }
  
  /** Indicates whether some form on page is (being) submitted alread. */
  var submitted = false;
  this.isSubmitted = function() { return this.submitted; }
  this.setSubmitted = function(b) { if (typeof b == "boolean") { this.submitted = b; } }
  
  /** Aranea JSP specific DOM tree traverser. */
  var traverser = new AraneaTraverser();
  this.getTraverser = function() { return traverser; }

  /** Variables holding different (un)load events that should be executed when page loads -- on body (un)load or alike. */
  var systemLoadEvents = new AraneaEventStore();
  var clientLoadEvents = new AraneaEventStore();
  var systemUnLoadEvents = new AraneaEventStore();
  
  loadEvents = new AraneaStore();
  loadEvents.add(systemLoadEvents);
  loadEvents.add(clientLoadEvents);
  
  unloadEvents = new AraneaStore();
  unloadEvents.add(systemUnLoadEvents);

  submitCallbacks = new Object();

  this.addSystemLoadEvent = function(event) { systemLoadEvents.add(event); }
  this.addClientLoadEvent = function(event) { clientLoadEvents.add(event); }
  this.addSystemUnLoadEvent = function(event) { systemUnLoadEvents.add(event); }

  this.onload = function() { loadEvents.forEach(executor); this.setLoaded(true); }
  this.onunload = function() { unloadEvents.forEach(executor); }
  
  // General callbacks executed before each form submit.
  this.addSubmitCallback = function(callback) {
    this.addSystemFormSubmitCallback('callbacks', callback);
  }

  // General callbacks executed before each submit of the specified system form.
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

  this.submit = function(element) {
    if (this.isSubmitted() || !this.isLoaded())
	  return false;
	  
    var t = this.getTraverser();
	var systemForm = t.findSurroundingSystemForm(element);
    var preCondition = t.getEventPreCondition(element);

    if (preCondition) {
      var f = eval("function(element) {" + preCondition + "}");
      if (!f()) {
        return false;
      }
    }

    if (systemForm) {
      this.executeCallbacks(systemForm['id']);
    }

    return (this.findSubmitter(element, systemForm)).submit(element);
  }

  /** 
   * This function can be overwritten to support additional
   * submit methods.
   */
  this.findSubmitter = function(element, systemForm) {
    var updateRegions = element.getAttribute('arn-updrgns');

	if (window.AraneaAA_Present && updateRegions && updateRegions.length > 0)
	  return new DefaultAraneaAJAXSubmitter(systemForm);

	return new DefaultAraneaSubmitter(systemForm);
  }
}

// Page initialization function, should be called upon page load.
AraneaPage.init = function() {
  // determine Aranea servlet URL and let active page know about it.
  var ap = getActiveAraneaPage();
  var traverser = ap.getTraverser();
  var forms = document.getElementsByTagName("form");
  for (var i = 0; i < forms.length; i++ ) {
    if (traverser.getElementAttribute(forms[i], "arn-systemForm")) {
      if (forms[i]["arn-servletURL"])
        ap.setServletURL(forms[i]["arn-servletURL"].value);
      break;
    }
  }
}

function DefaultAraneaSubmitter(form) {
  var systemForm = form;

  this.submit = function(element) {
    var systemFormId = systemForm['id'];
    var traverser = getActiveAraneaPage().getTraverser();

    // event information
    var widgetId = traverser.getEventTarget(element);
    var eventId = traverser.getEventId(element);
    var eventParam = traverser.getEventParam(element);

    systemForm.widgetEventPath.value = widgetId ? widgetId : "";
    systemForm.widgetEventHandler.value = eventId ? eventId : "";
    systemForm.widgetEventParameter.value = eventParam ? eventParam : "";

    getActiveAraneaPage().setSubmitted(true);
    systemForm.submit();

    return false;
  }
}

function DefaultAraneaAJAXSubmitter(form) {
  var systemForm = form;

  this.submit = function(element) {
    var systemFormId = systemForm['id'];
    var traverser = getActiveAraneaPage().getTraverser();
	
	// event information
    var widgetId = traverser.getEventTarget(element);
    var eventId = traverser.getEventId(element);
    var eventParam = traverser.getEventParam(element);
	var updateRegions = traverser.getEventUpdateRegions(element);

    systemForm.widgetEventPath.value = widgetId ? widgetId : "";
    systemForm.widgetEventHandler.value = eventId ? eventId : "";
    systemForm.widgetEventParameter.value = eventParam ? eventParam : "";
	
	window[ajaxKey].updateRegions = eval("new Array(" + updateRegions + ");");
	window[ajaxKey].systemForm = systemForm;
	window[ajaxKey].submitAJAX();

	return false;
  }
}

/* Initialize new Aranea page.  */
_ap = new AraneaPage();
function getActiveAraneaPage() { return _ap; }
_ap.addSystemLoadEvent(AraneaPage.init);
_ap.addSystemLoadEvent(Behaviour.apply);

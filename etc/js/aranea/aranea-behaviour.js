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
 * Behaviour rules required for Aranea JSP to work correctly.
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
function setFormElementContext(el) {
	var span = el.parentNode;
  do {
    if (span.tagName && span.tagName.toUpperCase() == 'SPAN')
    	break;
    span = span.parentNode;
  } while (span);
  

  if (span && span.tagName && span.tagName.toUpperCase() == 'SPAN' && el.name) {
    if (document.addEventListener) { // Moz
      span.onkeydown=function(event) { return uiHandleKeypress(event, el.name); };
    } else {
      span.onkeydown=function() { return uiHandleKeypress(event, el.name); };
    }
  }
  
}

function setFormElementValidation(el){
	var ajaxValidationHandler = new AraneaPage.AjaxValidationHandler(el);
	var elId = el.getAttribute("id");
	var actionValidate = function(event) {
		extraParams = new Hash();
		extraParams[elId] = el.value;
		araneaPage().action(el, 'validate', elId, el.value, ajaxValidationHandler.callback.bind(ajaxValidationHandler), null, null, extraParams);
	}
	Event.observe(elId, 'change', actionValidate);
}

function setCloningUrl(el) {
  var eventId = el.getAttribute('arn-evntId');
  var eventParam = el.getAttribute('arn-evntPar');
  var eventTarget = el.getAttribute('arn-trgtwdgt');

  var systemForm = araneaPage().getSystemForm();

  var url = araneaPage().getSubmitURL(systemForm['araTopServiceId'].value, systemForm['araThreadServiceId'].value, 'override');
  url += "&araPleaseClone=true";

  if (eventId)
    url += "&araWidgetEventHandler=" + eventId;
  if (eventParam)  
    url += "&araWidgetEventParameter=" + eventParam;
  if (eventTarget)
    url += "&araWidgetEventPath="+ eventTarget;
      
  el['href'] = url;
}

function applyCharacterFilter(el) {
  var filter = el.getAttribute('arn-charFilter');
  if (filter) {
    Event.observe(el, "keydown", getKeyboardInputFilterFunction(filter));
    if (!Prototype.Browser.IE && !Prototype.Browser.Opera) {
      Event.observe(el, "keypress", getKeyboardInputFilterFunction(filter));
    } else {
      el.attachEvent('onkeypress', function() { getKeyboardInputFilterFunction(filter)(window.event); });
    }
  }
}

function setToolTip(el){
  var toolTip = el.getAttribute("arn-toolTip");
  if (!toolTip) return;
	
  el.onmouseover=function(event) { return aranea_showTooltip(toolTip, el, event);};
  el.onmouseout=function() { aranea_hideTooltip(); };
}

var aranea_rules = {
  'a.aranea-link-button' : function(el) {
    setCloningUrl(el);
  },
  
  'a.aranea-link' : function(el) {
    setCloningUrl(el);
  },

  'input.aranea-text' : function(el) {
    applyCharacterFilter(el);
    setFormElementContext(el);
    setFormElementValidation(el);
  },

  'input.aranea-number' : function(el) {
    applyCharacterFilter(el);
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'input.aranea-float' : function(el) {
    applyCharacterFilter(el);
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'input.aranea-time' : function(el) {
    applyCharacterFilter(el);
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'input.aranea-date' : function(el) {
    applyCharacterFilter(el);
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'input.aranea-date-time' : function(el) {
    applyCharacterFilter(el);
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'input.aranea-checkbox' : function(el) {
    setFormElementContext(el);
  },

  'select.aranea-multi-select' : function(el) {
    setFormElementContext(el);
  },
  
  'input.aranea-multi-checkbox' : function(el) {
    setFormElementContext(el);
  },
  
  'input.aranea-radio' : function(el) {
    setFormElementContext(el);
  },

  'select.aranea-select' : function(el) {
    setFormElementContext(el);
  },
  
  'input.aranea-file-upload' : function(el) {
    setFormElementContext(el);
  },

  'textarea.aranea-textarea' : function(el) {
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'a.aranea-tab-link' : function(el) {
    setToolTip(el);
  }
};

Behaviour.register(aranea_rules);

window['aranea-behaviour.js'] = true;

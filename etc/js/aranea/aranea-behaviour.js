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
	  
    var hiddenPresent = createNamedElement('input', el.name +".__present");
    hiddenPresent.setAttribute('type','hidden');
    hiddenPresent.setAttribute('value','true');
    getActiveAraneaPage().addSystemLoadEvent(function() {span.appendChild(hiddenPresent);});
  }
}

function setCloningUrl(el) {
    var eventId = el.getAttribute('arn-evntId');
	var eventParam = el.getAttribute('arn-evntPar');
	var eventTarget = el.getAttribute('arn-trgtwdgt');

	var systemForm = getActiveAraneaPage().getTraverser().findSurroundingSystemForm(el);

    var url = new String();
    url = getActiveAraneaPage().getServletURL();
	url += "?pleaseClone=true"
    url += "&threadServiceId=" + systemForm['threadServiceId'].value;

	if (eventId)
      url += "&widgetEventHandler=" + eventId;
	if (eventParam)  
      url += "&widgetEventParameter=" + eventParam;
	if (eventTarget)
      url += "&widgetEventPath="+ eventTarget;

    el['href'] = "javascript:window.location='"+ url + "';";
}

var aranea_rules = {
  'a.aranea-link-button' : function(el) {
  	setCloningUrl(el);
  },
  
  'a.aranea-link' : function(el) {
  	setCloningUrl(el);
  },

  'input.aranea-text' : function(el) {
  	setFormElementContext(el);
  },

  'input.aranea-number' : function(el) {
  	setFormElementContext(el);
  },
  
  'input.aranea-float' : function(el) {
  	setFormElementContext(el);
  },
  
  'input.aranea-time' : function(el) {
  	setFormElementContext(el);
  },
  
  'input.aranea-date' : function(el) {
  	setFormElementContext(el);
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
  
  'input.aranea-radioselect' : function(el) {
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
  }
};

Behaviour.register(aranea_rules);

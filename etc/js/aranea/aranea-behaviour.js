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
  if (el.formElementContextBehaviourAttached) return;
  var span = $(el).ancestors().find(function(element) {
  	return element.tagName.toUpperCase() == 'SPAN';
  });

  if (span && el.name) {
    Event.observe(span, 'keydown', function(ev) { return Aranea.KB.handleKeypress(ev, el.name);});
    el.formElementContextBehaviourAttached = true;
  }
}

function formElementValidationActionCall(el) {
  // element serialization here is crucial, otherwise multi-valued controls only submit the most recent value
  araneaPage().action(el, 'bgValidate', el.id, null, function(transport) {AraneaPage.processResponse(transport.responseText);}, null, null, $(el).serialize(true));
}

/** @since 1.1 */
function setFormElementValidation(el){
  if (el.formElementValidationBehaviourAttached) return;
  if(!araneaPage().getBackgroundValidation() && !($(el).hasAttribute('arn-bgValidate'))) {
    return;
  }

  if (($(el).hasAttribute('arn-bgValidate')) && (($(el).getAttribute('arn-bgValidate')) != 'true')) {
    return;
  }

  var elId = el.getAttribute("id");
  var actionValidate = function(event) {
    formElementValidationActionCall(el);
  };
  Event.observe(elId, 'change', actionValidate);
  el.formElementValidationBehaviourAttached = true;
}

function setCloningUrl(el) {
  if (el.cloningUrlBehaviourAttached) return;
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
  el.cloningUrlBehaviourAttached = true;
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

/** TODO: this is not really used in current behaviour rules (only by tooltip tag) */
function setToolTip(el){
  var toolTip = $(el).getAttribute("arn-toolTip");
  if (!toolTip) return;

  new Tip(el, toolTip);
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

  'input.aranea-combo' : function(el) {
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
  
  'input.aranea-checkbox' : function(el) {
    setFormElementContext(el);
    setFormElementValidation(el);
  },

  'select.aranea-multi-select' : function(el) {
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'input.aranea-multi-checkbox' : function(el) {
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'input.aranea-radio' : function(el) {
    setFormElementContext(el);
    setFormElementValidation(el);
  },

  'select.aranea-select' : function(el) {
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'input.aranea-file-upload' : function(el) {
    setFormElementContext(el);
  },

  'textarea.aranea-textarea' : function(el) {
    setFormElementContext(el);
    setFormElementValidation(el);
  },
  
  'a.aranea-tab-link' : function(el) {
    setCloningUrl(el);
  }
};

Behaviour.register(aranea_rules);

window['aranea-behaviour.js'] = true;

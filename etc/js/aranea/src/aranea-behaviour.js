/*
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
 */

/**
 * Behaviour rules required for Aranea JSP to work correctly.
 *
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

function setFormElementContext(el) {
  if (!el._formElementContextBehaviourAttached) {
    var span = $(el).ancestors().find(function(element) {
      return element.tagName.toUpperCase() == 'SPAN';
    });
    if (span && el.name) {
      Event.observe(span, 'keydown', function(ev) { return Aranea.KB.handleKeypress(ev, el.name);});
      el._formElementContextBehaviourAttached = true;
    }
  }
}

function formElementValidationActionCall(event) {
  // element serialization here is crucial, otherwise multi-valued controls only submit the most recent value
  _ap.action(event.target, 'bgValidate', event.target.id, null,
      function(transport) {AraneaPage.processResponse(transport.responseText);}, null, null,
      $(event.target).serialize(true));
}

/** @since 1.1 */
function setFormElementValidation(el){
  if (el && !el._formElementValidationBehaviourAttached) {
    if (!_ap.getBackgroundValidation() && $(el).readAttribute('arn-bgValidate') != 'true') {
      return;
    }
    Event.observe(el, 'change', formElementValidationActionCall);
    el._formElementValidationBehaviourAttached = true;
  }
}

function setCloningUrl(el) {
  if (el._cloningUrlBehaviourAttached) return;
  var eventId = el.readAttribute('arn-evntId');
  var eventParam = el.readAttribute('arn-evntPar');
  var eventTarget = el.readAttribute('arn-trgtwdgt');

  var form = _ap.getSystemForm();

  var params = new Array(7);
  params.push("araPleaseClone=true");

  if (eventId) {
    params.push("&araWidgetEventHandler=");
    params.push(eventId);
  }
  if (eventParam) {
    params.push("&araWidgetEventParameter=");
    params.push(eventParam);
  }
  if (eventTarget) {
    params.push("&araWidgetEventPath=");
    params.push(eventTarget);
  }

  el.href = _ap.getSubmitURL(form.araTopServiceId.value, form.araThreadServiceId.value, 'override', params.join(''));
  el._cloningUrlBehaviourAttached = true;
}

function applyCharacterFilter(el) {
  var filter = el.readAttribute('arn-charFilter');
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
  if (el && Tip) {
    var toolTip = el.readAttribute("arn-toolTip");
    if (toolTip) {
      return new Tip(el, toolTip);
    }
  }
}

/**
 * The class used to consolidate all Aranea behaviour related code into this
 * "namespace".
 * @since 1.2.1 
 */
Aranea.Behaviour = Class.create();
Object.extend(Aranea.Behaviour, {

  /**
   * Applies Aranea standard web page behaviour to form elements.
   * @since 1.2.1
   */
  apply: function() {
    _ap.debug("Applying behaviour rules to form elements...");
    $$('a.aranea-link-button', 'a.aranea-link', 'a.aranea-tab-link').each(function(el) {
      if (el.hasAttribute('href')) {
        var href = el.readAttribute('href');
        if (!href || href.indexOf("://") < 0) {
          setCloningUrl(el);
        }
      }
    });

    $$('input.aranea-text',
       'input.aranea-number',
       'input.aranea-combo',
       'input.aranea-float',
       'input.aranea-time',
       'input.aranea-date').each(function(el) {
      applyCharacterFilter(el);
      setFormElementContext(el);
      setFormElementValidation(el);
    });

    $$('input.aranea-checkbox',
       'select.aranea-multi-select',
       'input.aranea-multi-checkbox',
       'input.aranea-radio',
       'select.aranea-select',
       'textarea.aranea-textarea').each(function(el) {
      setFormElementContext(el);
      setFormElementValidation(el);
    });

    $$('input.aranea-file-upload').each(function(el) {
      setFormElementContext(el);
    });

  },

  /**
	 * Returns the URL that is used to with the Ajax.Autocompleter text input.
	 * @param name -
	 *            name of the text input with autocompletion support
	 * @return the URL that can be used with the Ajax.Autocompleter text input.
	 * @since 1.2.1
	 */
  getAutoCompleteURL: function(name) {
    var form = AraneaPage.findSystemForm();
    var url = new Array();
    url.push(form.action);
    url.push("?araTopServiceId=");
    url.push(form.araTopServiceId.value);
    url.push("&araTransactionId=");
    url.push(form.araTransactionId.value);
    url.push("&araThreadServiceId=");
    url.push(form.araThreadServiceId.value);
    url.push("&araServiceActionPath=");
    url.push(name);
    url.push("&araServiceActionHandler=autocomplete");
    if (form.araClientStateId) {
      url.push("&araClientStateId=");
      url.push(form.araClientStateId.value);
    }
    return url.join('');
  },

  /**
   * Configures Ajax.Autocompleter for a text input with given name and
   * Ajax.Autocompleter options.
   * @param name - The name of the text input.
   * @param eventType - The name of the Aranea event, if there is one registered.
   * @param options - Options for Ajax.Autocompleter.
   * @since 1.2.1
   */
  doAutoCompleteInputSetup: function(name, eventType, updateRegions, options) {
    _ap.addClientLoadEvent(function() {
      var form = AraneaPage.findSystemForm();

      if (!options) {
        options = {};
      }

      if (eventType && !options.afterUpdateElement) {
        options = Object.extend(options, {
          afterUpdateElement: function(el, selectedEl) {
            _ap.event_6(form, eventType, name, null, null, updateRegions);
          }
        });
      }

      form = null;
      var url = Aranea.Behaviour.getAutoCompleteURL(name);
      new Ajax.Autocompleter(name, "ACdiv." + name, url, options);
    });
  }
});

/**
 * @deprecated since 1.2.1; use Aranea.Behaviour.apply().
 */
var aranea_rules = new Error('"aranea_rules is deprecated together with Behaviour; use Aranea.Behaviour[.apply] and Prototype instead.');
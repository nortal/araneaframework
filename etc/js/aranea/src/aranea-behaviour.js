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

var Aranea = Aranea ? Aranea : {};

/**
 * This namespace is used to consolidate all Aranea behaviour related logic that applies to page elements.
 *
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.1
 */
Aranea.Behaviour = {

	ATTR_BG_VALIDATE: 'aranea-bg-validate',

	ATTR_FILTER: 'arn-charFilter',

	ATTR_TOOLTIP: 'arn-toolTip',

	ATTACHED_VALIDATE: 'formElementValidationBehaviourAttached',

	ATTACHED_FE_CONTEXT: 'formElementContextBehaviourAttached',

	ATTACHED_URL: 'cloningUrlBehaviourAttached',

	ATTACHED_FILTER: 'formElementFilterAttached',

	ATTACHED_TOOLTIP: 'formElementFilterAttached',

	ACTION_BG_VALIDATE: 'bgValidate',

	ARANEA_LINKS: 'a.aranea-link-button, a.aranea-link, a.aranea-tab-link',

	ARANEA_INPUT: 'input.aranea-text, input.aranea-number, input.aranea-combo, input.aranea-float, input.aranea-time, input.aranea-date',

	ARANEA_INPUT_OTHER: 'input.aranea-checkbox,select.aranea-multi-select,input.aranea-multi-checkbox, input.aranea-radio,select.aranea-select,textarea.aranea-textarea',

	ARANEA_FILE_UPLOAD: 'input.aranea-file-upload',

	ARANEA_BG_VALIDATE: '.aranea-bg-validate',

	ARANEA_NO_BG_VALIDATE: '.aranea-no-bg-validate',

	setFormElementContext: function(element) {
		element = $(element);
		if (!element.getStorage().get(Aranea.Behaviour.ATTACHED_FE_CONTEXT)) {
			var span = element.up('span');
			if (span && element.name) {
				Event.observe(span, 'keydown', function(event) {
					return Aranea.Keyboard.handleKeypress(event);
				});
				element.getStorage().set(Aranea.Behaviour.ATTACHED_FE_CONTEXT, true);
			}
		}
	},

	formElementValidationActionCall: function(event) {
		// element serialization here is crucial, otherwise multi-valued controls only submit the most recent value:
		Aranea.Page.action(Aranea.Behaviour.ACTION_BG_VALIDATE, event.element().id, null, event.element().serialize(),
				function(transport) { Aranea.Page.Submitter.AJAX.processResponse(transport.responseText); });
	},

	/** @since 1.1 */
	setFormElementValidation: function(element) {
		element = $(element);
		var that = Aranea.Behaviour;
		var globalBgValidate = Aranea.Data.backgroundValidation;

		if (!element.getStorage().get(that.ATTACHED_VALIDATE)
			&& (globalBgValidate && !element.match(that.ARANEA_NO_BG_VALIDATE)
			|| !globalBgValidate &&  element.match(that.ARANEA_BG_VALIDATE))) {

			element.observe('change', that.formElementValidationActionCall);
			element.getStorage().set(that.ATTACHED_VALIDATE, true);
		}
	},

	setCloningUrl: function(element) {
		element = $(element);
		if (!element.readAttribute('href') || element.getStorage().get(Aranea.Behaviour.ATTACHED_URL)) {
			return;
		}

		var eventId = Aranea.Page.Form.getEventId(element);
		var eventParam = Aranea.Page.Form.getEventParam(element);
		var eventTarget = Aranea.Page.Form.getEventTarget(element);
		
		var params = {
			araPleaseClone: true,
			araThreadServiceId : Aranea.Page.Form.getThreadServiceId(null),
			araTransactionId: 'override'
		};
		if (eventId) {
			params.araWidgetEventHandler = eventId;
		}
		if (eventParam) {
			params.araWidgetEventParameter = eventParam;
		}
		if (eventTarget) {
			params.araWidgetEventPath = eventTarget;
		}
		element.href = Aranea.Page.getSubmitURL(params);
		$(element).getStorage().set(Aranea.Behaviour.ATTACHED_URL, true);
	},

	applyCharacterFilter: function(element) {
		var filter = element.readAttribute(Aranea.Behaviour.ATTR_FILTER);
		if (filter && !element.getStorage().get(Aranea.Behaviour.ATTACHED_FILTER)) {
			element.observe('keypress', Aranea.Keyboard.getKeyboardInputFilterFunction(filter));
			element.observe('paste', Aranea.Behaviour.onCharacterFilterPaste); //quirksmode (does not work in Opera)
			Aranea.Behaviour.monitorCharacterFilterInput(element);
			element.getStorage().set(Aranea.Behaviour.ATTACHED_FILTER, true);
		}
	},

	onCharacterFilterPaste: function(event) {
		Aranea.Behaviour.characterFilterInputMonitor.curry(event.element()).defer();
	},

	monitorCharacterFilterInput: function(input) {
		input = $(input);
		if (!input) return;
		window.setInterval(Aranea.Behaviour.characterFilterInputMonitor.curry(input), 1000);
	},

	characterFilterInputMonitor: function(input) {
		if (!(input = $(input))) return;
		var filter = input.readAttribute('arn-charFilter');
		var value = $F(input);
		if (value == null) return;
		for (var i = 0; i < value.length; i++) {
			if (filter.indexOf(value.charAt(i)) == -1) {
				value = value.substring(0, i) + value.substring(i + 1);
				i--;
			}
		}
		if ($F(input) != value) {
			input.value = value;
		}
	},

	/** TODO: this is not really used in current behaviour rules (only by tooltip tag) */
	setToolTip: function(element){
		element = $(element);
		if (Tip && !element.getStorage().get(Aranea.Behaviour.ATTACHED_TOOLTIP)) {
			var toolTip = element.readAttribute(Aranea.Behaviour.ATTR_TOOLTIP);
			if (toolTip) {
				element.getStorage().set(Aranea.Behaviour.ATTACHED_FILTER, true);
				return new Tip(element, toolTip);
			}
		}
	},

	apply: function() {
		Aranea.Logger.debug('Applying behaviour rules to form elements...');
		$$(Aranea.Behaviour.ARANEA_LINKS).each(function(el) {
			Aranea.Behaviour.setCloningUrl(el);
		});

		$$(Aranea.Behaviour.ARANEA_INPUT).each(function(el) {
			Aranea.Behaviour.applyCharacterFilter(el);
			Aranea.Behaviour.setFormElementContext(el);
			Aranea.Behaviour.setFormElementValidation(el);
		});

		$$(Aranea.Behaviour.ARANEA_INPUT_OTHER).each(function(el) {
			Aranea.Behaviour.setFormElementContext(el);
			Aranea.Behaviour.setFormElementValidation(el);
		});
	},

	/**
	 * Returns the URL for fetching suggestions for an Ajax.Autocompleter text input.
	 * @param name - name of the text input with autocompletion support
	 * @return the URL that can be used with the Ajax.Autocompleter text input.
	 * @since 1.2.1
	 */
	getAutoCompleteURL: function(name) {
		return Aranea.Page.getSubmitURL({
			araTopServiceId: Aranea.Page.Form.getTopServiceId(),
			araThreadServiceId: Aranea.Page.Form.getThreadServiceId(),
			araTransactonId: Aranea.Page.Form.getTransactionId(),
			araServiceActionPath: name,
			araServiceActionHandler: 'autocomplete',
			araClientStateId: null
		});
	},

	/**
	 * Configures Ajax.Autocompleter for a text input with given name and Ajax.Autocompleter options.
	 * @param name - The name of the text input.
	 * @param eventType - The name of the Aranea event, if there is one registered.
	 * @param options - Options for Ajax.Autocompleter.
	 * @since 1.2.1
	 */
	doAutoCompleteInputSetup: function(name, eventType, updateRegions, options) {
		options = options || {};
		if (eventType && !options.afterUpdateElement) {
			options = Object.extend(options, {
				afterUpdateElement: function(el, selectedEl) {
					if (Aranea.UI.isChanged(name) && !$('ACdiv.' + name).visible()) {
						Aranea.Page.event(eventType, name, null, null, updateRegions);
					}
				}
			});
		}

		var init = function(event) {
			if (!$(name) || $(name).getStorage().get('autocompleteSetupDone')) {
				document.stopObserving('aranea:loaded', this);
				document.stopObserving('aranea:updated', this);
				return;
			}

			window.setTimeout(function() {
				var url = Aranea.Behaviour.getAutoCompleteURL(name);
				new Ajax.Autocompleter(name, 'ACdiv.' + name, url, options);
				$(name).getStorage().set('autocompleteSetupDone', true);
			});
		};

		document.observe('aranea:loaded', init);
		document.observe('aranea:updated', init);
	}
};
document.observe('aranea:loaded', Aranea.Behaviour.apply);
document.observe('aranea:updated', Aranea.Behaviour.apply);

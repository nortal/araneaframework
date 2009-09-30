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
 * This namespace is used to consolidate all Aranea behaviour related logic that applies to page elements.
 *
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.1
 */
var Aranea = Aranea ? Aranea : {};

Aranea.Behaviour = {

	ATTR_BG_VALIDATE: 'arn-bgValidate',

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

	setFormElementContext: function(element) {
		element = $(element);
		if (!element.getStorage().get(this.ATTACHED_FE_CONTEXT)) {
			var span = element.up('span');
			if (span && element.name) {
				Event.observe(span, 'keydown', function(ev) {
					return Aranea.KB.handleKeypress(ev, element.name);
				});
				element.getStorage().set(this.ATTACHED_FE_CONTEXT, true);
			}
		}
	},

	formElementValidationActionCall: function(event) {
		// element serialization here is crucial, otherwise multi-valued controls only submit the most recent value:
		Aranea.Page.action(this.ACTION_BG_VALIDATE, event.target.id, $(event.target).serialize(true),
				function(transport) { Aranea.Page.processResponse(transport.responseText); });
	},

	/** @since 1.1 */
	setFormElementValidation: function(element) {
		element = $(element);
		if (element.getStorage().get(this.ATTACHED_VALIDATE) || element.readAttribute(this.ATTR_BG_VALIDATE) == 'false') {
			return;
		}
		if(element.readAttribute(this.ATTR_BG_VALIDATE) == 'true' || Aranea.Data.backgroundValidation) {
			Event.observe(element, 'change', Aranea.Behaviour.formElementValidationActionCall);
			element.getStorage().set(this.ATTACHED_VALIDATE, true);
		}
	},

	setCloningUrl: function(element) {
		element = $(element);
		if (String.blank(element.readAttribute('href')) || element.getStorage().get(this.ATTACHED_URL)) {
			return;
		}

		var eventId = Aranea.Page.Attribute.getEventId(element);
		var eventParam = Aranea.Page.Attribute.getEventParam(element);
		var eventTarget = Aranea.Page.Attribute.getEventTarget(element);

		var params = {
			araPleaseClone: true,
			araThreadServiceId: 'override'
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
		$(element).getStorage().set(this.ATTACHED_URL, true);
	},

	applyCharacterFilter: function(element) {
		var filter = element.readAttribute(this.ATTR_FILTER);
		if (filter && !element.getStorage().get(this.ATTACHED_FILTER)) {
			Event.observe(element, 'keydown', Aranea.Keyboard.getKeyboardInputFilterFunction(filter));
			element.getStorage().set(this.ATTACHED_FILTER, true);
		}
	},

	/** TODO: this is not really used in current behaviour rules (only by tooltip tag) */
	setToolTip: function(element){
		element = $(element);
		if (Tip && !element.getStorage().get(this.ATTACHED_TOOLTIP)) {
			var toolTip = element.readAttribute(this.ATTR_TOOLTIP);
			if (toolTip) {
				element.getStorage().set(this.ATTACHED_FILTER, true);
				return new Tip(element, toolTip);
			}
		}
	},

	apply: function() {
		Aranea.Logger.debug('Applying behaviour rules to form elements...');
		$$(this.ARANEA_LINK).each(function(el) {
			this.setCloningUrl(el);
		});

		$$(this.ARANEA_INPUT).each(function(el) {
			this.applyCharacterFilter(el);
			this.setFormElementContext(el);
			this.setFormElementValidation(el);
		});

		$$(this.ARANA_INPUT_OTHER).each(function(el) {
			this.setFormElementContext(el);
			this.setFormElementValidation(el);
		});

		$$(this.ARANEA_FILE_UPLOAD).each(function(el) {
			this.setFormElementContext(el);
		});
	},

	/**
	 * Returns the URL for fetching suggestions for an Ajax.Autocompleter text input.
	 * @param name - name of the text input with autocompletion support
	 * @return the URL that can be used with the Ajax.Autocompleter text input.
	 * @since 1.2.1
	 */
	getAutoCompleteURL: function(name) {
		return Aranea.Page.getSubmitURL(
			Aranea.Attribute.getTopServiceId(),
			Aranea.Attribute.getThreadServiceId(),
			Aranea.Attribute.getTransactionServiceId(),
			{ araServiceActionPath: name, araServiceActionHandler: 'autocomplete' });
	},

	/**
	 * Configures Ajax.Autocompleter for a text input with given name and Ajax.Autocompleter options.
	 * @param name - The name of the text input.
	 * @param eventType - The name of the Aranea event, if there is one registered.
	 * @param options - Options for Ajax.Autocompleter.
	 * @since 1.2.1
	 */
	doAutoCompleteInputSetup: function(name, eventType, updateRegions, options) {
		if (!options) {
			options = {};
		}
		if (eventType && !options.afterUpdateElement) {
			options = Object.extend(options, {
				afterUpdateElement: function(el, selectedEl) {
					Aranea.Page.event(eventType, name, null, null, updateRegions);
				}
			});
		}
		var url = Aranea.Behaviour.getAutoCompleteURL(name);
		Ajax.Autocompleter(name, 'ACdiv.' + name, url, options);
	}
};

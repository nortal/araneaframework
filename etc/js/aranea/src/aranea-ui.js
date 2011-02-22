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
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
Aranea.UI = {

	/**
	 * CSS class applied to form elements that are not valid.
	 * @since 1.1
	 */
	INVALID_FE_CLASS: "aranea-invalid-formelement",

	calendarSetup: function(inputFieldId, dateFormat, alignment) {
		var align = alignment == null ? "Br" : alignment;
		if (window.Calendar) {
			Calendar.setup({
				inputField	: inputFieldId,
				ifFormat	: dateFormat,
				showsTime	: false,
				button		: inputFieldId + '_cbutton',
				singleClick	: true,
				step		: 1,
				firstDay	: 1,
				align		: align,
				electric	: false
			});
		}
	},

	/**
	 * fillTime() and addOptions() functions are used in timeInputs for hour and
	 * minute inputs/selects.
	 * 
	 * @since 1.1
	 */
	fillTimeText: function(el, hourSelect, minuteSelect) {
		if ($(hourSelect).present() || $(minuteSelect).present()) {
			$(el).value = $F(hourSelect) + ':' + $F(minuteSelect);
		} else {
			$(el).clear();
		}
	},

	/**
	 * @since 1.1
	 */
	fillTimeSelect: function(timeInput, hourSelect, minuteSelect) {
		timestr = $F(timeInput).strip();
		separatorPos = timestr.indexOf(':');
		hours = timestr.substr(0, separatorPos);
		$(hourSelect).value = hours.length == 1 ? '0' + hours : hours;
		$(minuteSelect).value = timestr.substr(separatorPos + 1);
	},

	/**
	 * Adds options empty,0-(z-1) to select with option number x preselected.
	 * Used for timeInput hour and minute selects.
	 * @since 1.1
	 */
	addOptions: function(selectId, count, selectedIndex) {
		var select = $(selectId);

		if (!select) {
			throw('No select with name "' + selectName + '" was found!');
		}

		select.insert(new Element('option', { 'value': '' }));

		for (var i = 0; i < count; i++) {
			var value = (i < 10 ? '0' : '') + i;
			var opt = new Element('option', { 'value': value }).update(value);
			if (i == selectedIndex) {
				opt.writeAttribute('selected', 'selected');
			}
			select.insert(opt);
		}

		// IE otherwise sets select width wrong when updating DOM
		if (Prototype.Browser.IE) {
			select.setStyle('visibility', 'visible');
		}
	},

	/**
	 * @since 1.1
	 */
	saveValue: function(element) {
		$(element).getStorage().set('oldValue', $F(element));
	},

	/**
	 * @since 1.1
	 */
	isChanged: function(element) {
		var oldValue = $(element).getStorage().get('oldValue');
		return oldValue != $F(element);
	},

	//--------------- Scroll position saving/restoring --------------//

	/**
	 * @since 1.1
	 */
	saveScrollCoordinates: function() {
		var offset = parent.document.viewport.getScrollOffsets();
		if (Aranea.Data.systemForm.windowScrollX) {
			Aranea.Data.systemForm.windowScrollX.value = offset.left;
		}
		if (Aranea.Data.systemForm.windowScrollY) {
			Aranea.Data.systemForm.windowScrollY.value = offset.top;
		}
	},

	/**
	 * @since 1.1
	 */
	scrollToCoordinates: function(x, y) {
		if (!Object.isNumber(x) || !Object.isNumber(y)) {
			throw ('Cannot scroll to ['+x+','+y+'] because one of given coordinates is not a number!');
		}
		parent.window.scrollTo(x, y);
	},

	/** 
	 * 
	 * @param valid - boolean specifying whether content in form element is valid
	 * @param el - HTML node that contains form element (most often this would be span)
	 * @since 1.1
	 */
	markFEContentStatus: function(valid, element) {
		element = $(element);
		if (element == null) {
			return;
		}

		var parentTagName = element.up().tagName.toLowerCase();
		if (parentTagName == 'td' || parentTagName == 'th') {
			element = element.up();
		}

		if (valid) {
			$(element).removeClassName(Aranea.UI.INVALID_FE_CLASS);
		} else {
			$(element).addClassName(Aranea.UI.INVALID_FE_CLASS);
		}
	},

	/**
	 * Attaches validation errors directly to formelement on client-side.
	 * 
	 * @param el DOM element which accepts validation messages as children
	 * @param html HTML content (error messages prerendered serverside)
	 * @since 1.1
	 */
	appendLocalFEValidationMessages: function(element, html) {
		return $(element) && $(element).insert(html);
	},

	/**
	 * This function may be used to let end-user confirm flow navigation events.
	 * 
	 * @since 1.1
	 */
	confirmFlowEvent: function(message) {
		var confirmationResult = '' + window.confirm(message);
		Aranea.Data.systemForm.araConfirmationContextConfirmationResult.value = confirmationResult;
		Aranea.Page.event();
	},

	/**
	 * This function enables one check box to select all check boxes of given list. It assumes that all check boxes in
	 * the list have an ID that starts with the ID value of the given select-all check box (the parameter).
	 * 
	 * @param chkSelectall The checkbox that toggles other check boxes to be selected or not selected.
	 * @since 1.1.3
	 */
	toggleListCheckBoxes: function(chkSelectAll) {
		if (chkSelectAll) {
			var selector = 'input[type=checkbox][id!="' + chkSelectAll.id + '"][id^="' + chkSelectAll.id + '"]';
			Aranea.Data.systemForm.select(selector).invoke('writeAttribute', 'checked', chkSelectAll.checked);
		}
	},

	/**
	 * This function enables to update the state of the select-all check box. It assumes that all check boxes in the
	 * list have the ID that starts with the ID value of the given select-all check box. The parameter is the check box
	 * that was clicked. If all check boxes are selected, the select-all will be also selected, and vice versa.
	 * @since 1.1.3
	 */
	updateListSelectAll: function(chkSelect) {
		var prefix = chkSelect ? chkSelect.match(/.*(?=\.)/)[0] : null;
		if (prefix) {
			var selector = 'input[type=checkbox][id^="' + prefix + '"][id!="' + prefix + '"]:not(:checked)';
			var allSelected = Aranea.Data.systemForm.down(selector) == null;
			if ($(prefix)) { // The "Select-All" check-box.
				$(prefix).writeAttribute('checked', allSelected);
			}
		}
	}
};
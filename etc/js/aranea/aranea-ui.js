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
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

/** @since 1.1 */
Aranea.UI = Class.create();
Object.extend(Aranea.UI, {

  /**
   * CSS class applied to form elements that are not valid.
   * @since 1.1
   */
  InvalidFormElementClass: "aranea-invalid-formelement",

  calendarSetup: function(inputFieldId, dateFormat, alignment) {
    var CALENDAR_BUTTON_ID_SUFFIX = "_cbutton"; // comes from BaseFormDateTimeInputHtmlTag
    var align = alignment == null ? "Br" : alignment;

    Calendar.setup({
      inputField   : inputFieldId,
      ifFormat     : dateFormat,
      showsTime    : false,
      button       : inputFieldId + CALENDAR_BUTTON_ID_SUFFIX,
      singleClick  : true,
      step         : 1,
      firstDay     : 1,
      align        : align,
      electric     : false
    });
  },

  /**
   * fillTime() and addOptions() functions are used in timeInputs for hour and
   * minute inputs/selects.
   * 
   * @since 1.1
   */
  fillTimeText: function(el, hourSelect, minuteSelect) {
    if ($(hourSelect).value=='' && $(minuteSelect).value=='') {
      $(el).value = '';
    } else {
      $(el).value=$(hourSelect).value+':'+$(minuteSelect).value;
    }
  },

  /**
   * @since 1.1
   */
  fillTimeSelect: function(timeInput, hourSelect, minuteSelect) {
    timestr = $(timeInput).value;
    separatorPos = timestr.indexOf(':');
    hours = timestr.substr(0, separatorPos);
    hourValue = hours.length==1 ? '0' + hours : hours;
    minuteValue = timestr.substr(separatorPos + 1, $(timeInput).value.length);
    $(hourSelect).value = hourValue;
    $(minuteSelect).value = minuteValue;
  },

  /**
   * Adds options empty,0-(z-1) to select with option number x preselected.
   * Used for timeInput hour and minute selects.
   * @since 1.1
   */
  addOptions: function(selectName, z, x) {
    var select = document.getElementsByName(selectName).item(0);
    select.appendChild(new Element('option', { 'value': '' }));

    for (var i = 0; i < z; i++) {
      var value = (i < 10 ? '0' : '') + i;
      var opt = new Element('option', { 'value': value }).update(value);

      if (i == x) {
        opt.setAttribute('selected', 'selected');
      }
      select.appendChild(opt);
    }

    // IE otherwise sets select width wrong when updating DOM
    if (Prototype.Browser.IE) {
      select.style.visibility = 'visible';
    }
  },

  /**
   * @since 1.1
   */
  saveValue: function(element) {
    $(element).oldValue = $(element).value;
  },

  /**
   * @since 1.1
   */
  isChanged: function(elementId) {
    var el = $(elementId);
    if (!el.oldValue) {
      return el.value != '';
    }
    return el.oldValue != el.value;
  },

  //--------------- Scroll position saving/restoring --------------//

  /**
   * @since 1.1
   */
  saveScrollCoordinates: function() {
  	var x, y;

  	if (document.documentElement && document.documentElement.scrollTop) {
  		// IE 6
  		x = document.documentElement.scrollLeft;
  		y = document.documentElement.scrollTop;
  	} else if (document.body) {
  		// IE 5
  		x = document.body.scrollLeft;
  		y = document.body.scrollTop;
  	} else {
  		// Netscape, Mozilla, Firefox etc
  		x = window.pageXOffset;
  		y = window.pageYOffset;
  	}

  	var form = araneaPage().getSystemForm();
  	if (form.windowScrollX) {
  		form['windowScrollX'].value = x;
  	}

  	if (form.windowScrollY) {
  		form['windowScrollY'].value = y;
  	}
  },

  /**
   * @since 1.1
   */
  scrollToCoordinates: function(x, y) {
    window.scrollTo(x, y);
  },

  /** 
   * 
   * @param valid - boolean specifying whether content in form element is valid
   * @param el - HTML node that contains form element (most often this would be span)
   * @since 1.1
   */
  markFEContentStatus: function(valid, el) {
    if (el) { 	
      el = $(el);
    } else {
      return;
    }

    var element = el;
    var parentTagName = element.parentNode.tagName.toLowerCase();
    if (parentTagName == 'td' || parentTagName == 'th') {
      element = element.parentNode;
    }

    if (valid) {
    	$(element).removeClassName(Aranea.UI.InvalidFormElementClass);
    } else {
    	$(element).addClassName(Aranea.UI.InvalidFormElementClass);
    }
  },

  /**
   * Attaches validation errors directly to formelement on client-side.
   * 
   * @param el DOM element which accepts validation messages as children
   * @param html HTML content (error messages prerendered serverside)
   * @since 1.1
   */
  appendLocalFEValidationMessages: function(el, html) {
    if (el) {
    	el = $(el);
    } else {
    	return;
    }
    if (el) {
    	el.insert(html);
    }
  },

  /**
   * This function may be used to let end-user confirm flow navigation events.
   * 
   * @since 1.1
   */
  flowEventConfirm: function(message) {
    var confirmationResult = "" + window.confirm(message);
    _ap.getSystemForm().araConfirmationContextConfirmationResult.value = confirmationResult;
    _ap.event_6(araneaPage().getSystemForm());
  },

  /**
   * This function enables one check box to select all check boxes of given list.
   * It assumes that all check boxes in the list have the ID that starts with the
   * ID value of the given select-all check box (the parameter).
   * 
   * @since 1.1.3
   */
  toggleListCheckBoxes: function(chkSelectAll) {
  	var arrFormElems = _ap.getSystemForm().elements;

  	for (var i = 0; i < arrFormElems.length; i++) {
  	  var elem = arrFormElems[i];
  	  if (elem.getAttribute('type') == 'checkbox' && elem.id != null
  	        && elem.id.startsWith(chkSelectAll.id)) {
  	      elem.checked = chkSelectAll.checked;
  	  }
  	}

  	arrFormElems = null;
  	return true;
  },

  /**
   * This function enables to update the state of the select-all check box.
   * It assumes that all check boxes in the list have the ID that starts with the
   * ID value of the given select-all check box. The parameter is the check box
   * that was clicked. If all check boxes are selected, the select-all will be
   * also selected, and vice versa.
   * @since 1.1.3
   */
  updateListSelectAlls: function(chkSelect) {
  	var arrFormElems = _ap.getSystemForm().elements;

  	var pos = chkSelect.id.lastIndexOf('.');
  	if (pos == -1) {
  		return true;
  	}

  	var prefix = chkSelect.id.substr(0, pos);
  	var allSelected = true;

  	for (var i = 0; i < arrFormElems.length; i++) {
  		var elem = arrFormElems[i];

  		if (elem.getAttribute('type') == 'checkbox'
  				&& elem.id != null && elem.id != prefix
  				&& elem.id.startsWith(prefix)) {

  			if (!elem.checked) {
  				allSelected = false;
  				break;
  			}

  		}
  	}

  	var chkSelectAll = document.getElementById(prefix);

  	if (chkSelectAll != null && chkSelectAll.checked != allSelected) {
  		chkSelectAll.checked = allSelected;
  	}

  	return true;
  }
});

/**
 * @deprecated
 * @since 1.0
 */
var calendarSetup = Aranea.UI.calendarSetup;

/**
 * @deprecated
 * @since 1.0
 */
var fillTimeText = Aranea.UI.fillTimeText;

/**
 * @deprecated
 * @since 1.0
 */
var fillTimeSelect = Aranea.UI.fillTimeSelect;

/**
 * @deprecated
 * @since 1.0
 */
var addOptions = Aranea.UI.addOptions;

/**
 * @deprecated
 * @since 1.0
 */
var saveValue = Aranea.UI.saveValue;

/**
 * @deprecated
 * @since 1.0
 */
var isChanged = Aranea.UI.isChanged;

/**
 * @deprecated
 * @since 1.0
 */
var saveScrollCoordinates = Aranea.UI.saveScrollCoordinates;

/**
 * @deprecated
 * @since 1.0
 */
var scrollToCoordinates = Aranea.UI.scrollToCoordinates;

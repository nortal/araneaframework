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

/** @since 1.1 */
Aranea.UI = {};
/** @since 1.1 */
Aranea.UI.calendarSetup = function(inputFieldId, dateFormat, alignment) {
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
};

/* @deprecated
   @since 1.0 **/
var calendarSetup = Aranea.UI.calendarSetup;

/* fillTime*() and addOptions() functions are used in *timeInputs 
 * for hour and minute inputs/selects. */
/** @since 1.1 */
Aranea.UI.fillTimeText = function(el, hourSelect, minuteSelect) {
  if ($(hourSelect).value=='' && $(minuteSelect).value=='') {
    $(el).value='';
  }
  else {
    $(el).value=$(hourSelect).value+':'+$(minuteSelect).value;
  }
};

/* @deprecated
   @since 1.0 **/
var fillTimeText = Aranea.UI.fillTimeText;

/** @since 1.1 */
Aranea.UI.fillTimeSelect = function(timeInput, hourSelect, minuteSelect) {
  timestr = $(timeInput).value;
  separatorPos = timestr.indexOf(':');
  hours = timestr.substr(0, separatorPos);
  hourValue = hours.length==1 ? '0'+hours : hours;
  minuteValue = timestr.substr(separatorPos+1, $(timeInput).value.length);
  $(hourSelect).value=hourValue;
  $(minuteSelect).value=minuteValue;
};

/* @deprecated
   @since 1.0 **/
var fillTimeSelect = Aranea.UI.fillTimeSelect;

// Adds options empty,0-(z-1) to select with option x preselected. Used for
// *timeInput hour and minute selects.
/** @since 1.1 */
Aranea.UI.addOptions = function(selectName, z, x) {
  var select=document.getElementsByName(selectName).item(0);
  var emptyOpt=document.createElement("option");
  emptyOpt.setAttribute("value", "");
  select.appendChild(emptyOpt);
  for (var i = 0; i < z; i++) {
    var opt = document.createElement("option");
    opt.setAttribute("value", (i < 10 ? "0" : "")+ i);
    if (i == x) { opt.setAttribute("selected", "true") };
    var node = document.createTextNode((i < 10 ? "0" : "")+ i);
    opt.appendChild(node);
    select.appendChild(opt);
  }
  // IE otherwise sets select width wrong when updating DOM
  if (Prototype.Browser.IE) select.style.visibility='visible';
};

/* @deprecated
   @since 1.0 **/
var addOptions = Aranea.UI.addOptions;

/** @since 1.1 */
Aranea.UI.saveValue = function(element) {
  $(element).oldValue = $(element).value; 
};

/* @deprecated
   @since 1.0 **/
var saveValue = Aranea.UI.saveValue;

/** @since 1.1 */
Aranea.UI.isChanged = function(elementId) {
  var el = $(elementId);
  if (!el.oldValue) {
  	if (el.value != '')
      return true;
    return false;
  }
  return (el.oldValue != el.value);
};

/* @deprecated
   @since 1.0 **/
var isChanged = Aranea.UI.isChanged;

//--------------- Scroll position saving/restoring --------------//
/** @since 1.1 */
Aranea.UI.saveScrollCoordinates = function() {
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
};

/* @deprecated
   @since 1.0 **/
var saveScrollCoordinates = Aranea.UI.saveScrollCoordinates;

/** @since 1.1 */
Aranea.UI.scrollToCoordinates = function(x, y) {
  window.scrollTo(x, y);	
};

/* @deprecated
   @since 1.0 **/
var scrollToCoordinates = Aranea.UI.scrollToCoordinates;

/**
 * CSS class applied to form elements that are not valid.
 * @since 1.1 */
Aranea.UI.InvalidFormElementClass = "aranea-invalid-formelement";

/** 
 * @param valid boolean specifying whether content in form element is valid
 * @param el HTML node that contains form element (most often this would be span)
 * @since 1.1 
 * */
Aranea.UI.markFEContentStatus = function(valid, el) {
  if (el) 	
    el = $(el);
  else
    return;

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
};

/**
 * Attaches validation errors directly to formelement on client-side.
 * @param el DOM element which accepts validation messages as children
 * @param html HTML content (error messages prerendered serverside)
 * @since 1.1
 */
Aranea.UI.appendLocalFEValidationMessages = function(el, html) {
  if (el) el = $(el); else return;
  if (el) new Insertion.Bottom(el, html);
};

/**
 * This function may be used to let end-user confirm flow navigation events.
 * @since 1.1 */
Aranea.UI.flowEventConfirm = function(message) {
  var confirmationResult = "" + window.confirm(message);
  araneaPage().getSystemForm().confirmationContextConfirmationResult.value = confirmationResult;
  araneaPage().event_6(araneaPage().getSystemForm());
};

window['aranea-ui.js'] = true;

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

/** DateInput's calendar setup function. See js/calendar/calendar-setup.js for details. */
function calendarSetup(inputFieldId, dateFormat, alignment) {
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
}

/* fillTime*() and addOptions() functions are used in *timeInputs 
 * for hour and minute inputs/selects. */
function fillTimeText(el, hourSelect, minuteSelect) {
  if (document.getElementById(hourSelect).value=='' && document.getElementById(minuteSelect).value=='') {
    document.getElementById(el).value='';
  }
  else {
    document.getElementById(el).value=document.getElementById(hourSelect).value+':'+document.getElementById(minuteSelect).value;
  }
}

function fillTimeSelect(timeInput, hourSelect, minuteSelect) {
  timestr = document.getElementById(timeInput).value;
  separatorPos = timestr.indexOf(':');
  hours = timestr.substr(0, separatorPos);
  hourValue = hours.length==1 ? '0'+hours : hours;
  minuteValue = timestr.substr(separatorPos+1, document.getElementById(timeInput).value.length);
  document.getElementById(hourSelect).value=hourValue;
  document.getElementById(minuteSelect).value=minuteValue;
}

// Adds options empty,0-(z-1) to select with option x preselected. Used for
// *timeInput hour and minute selects.
function addOptions(selectName, z, x) {
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
}

function saveValue(element) {
  element.oldValue = element.value; 
}

function isChanged(elementId) {
  var el = document.getElementById(elementId);
  if (!el.oldValue) {
  	if (el.value != '')
      return true;
    return false;
  }
  return (el.oldValue != el.value);
}

//--------------- Scroll position saving/restoring --------------//
function saveScrollCoordinates() {
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
} 

function scrollToCoordinates(x, y) {
	window.scrollTo(x, y);
}

window['aranea-ui.js'] = true;

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

//
// UI basic functions
// 

/**
 * Invoke all the callbacks registered for given system form.
 * Then call systemForm.submit().
 * @author Konstantin Tretyakov
 */
function uiSystemFormSubmit(systemForm, updateRegions){
  if (!systemForm) return;

  if (systemForm.uiProperties && systemForm.uiProperties.submitCallbacks) {
	  var callbacks = systemForm.uiProperties.submitCallbacks;
	  
	  var i;
	  for (i = 0; i < callbacks.length; i++){
	  	var f = callbacks[i];
			f();
	  }  
  }
	
	if (updateRegions && updateRegions.length > 0) {
		window[ajaxKey].updateRegions = updateRegions;
		window[ajaxKey].systemForm = systemForm;
		window[ajaxKey].submitAJAX();
	}
	else {
		pageActive = false;	
		systemForm.submit();		
	}
}

/**
 * Add a function that will be invoked before form is submitted
 * This is required for HtmlEditor, for example, that
 * will have the possibility to copy its contents in its formelement.
 *
 * @author Konstantin Tretyakov
 */
function uiAddSystemFormSubmitCallback(systemForm, callbackFunction){
  if (!systemForm) return;
  if (!systemForm.uiProperties){
  	systemForm.uiProperties = new Object();
  }
  if (!systemForm.uiProperties.submitCallbacks){
  	systemForm.uiProperties.submitCallbacks = new Array();
  }
  var callbacks = systemForm.uiProperties.submitCallbacks;
  callbacks[callbacks.length] = callbackFunction;
}

/**
 * Validates given form in a given system form
 *
 * @author Konstantin Tretyakov
 */
function uiValidateForm(systemForm, formId) {
	return systemForm.uiProperties[formId].validator.validate();
}

function fillTimeText(systemForm, el, hourSelect, minuteSelect) {
  if (systemForm[hourSelect].value=='' && systemForm[minuteSelect].value=='') {
    systemForm[el].value='';
  }
  else {
    systemForm[el].value=systemForm[hourSelect].value+':'+systemForm[minuteSelect].value;
  }
}

function fillTimeSelect(systemForm, timeInput, hourSelect, minuteSelect) {
  timestr = systemForm[timeInput].value;
  separatorPos = timestr.indexOf(':');
  hours = timestr.substr(0, separatorPos);
  hourValue = hours.length==1 ? '0'+hours : hours;
  minuteValue = timestr.substr(separatorPos+1, systemForm[timeInput].value.length);
  systemForm[hourSelect].value=hourValue;
  systemForm[minuteSelect].value=minuteValue;
}

// b/c braindead IE: The NAME attribute cannot be set at run time on elements dynamically 
// created with the createElement method. To create an element with a name attribute, 
// include the attribute and value when using the createElement method.
// http://www.thunderguy.com/semicolon/2005/05/23/setting-the-name-attribute-in-internet-explorer/
function createNamedElement(type, name) {
   var element = null;
   // Try the IE way; this fails on standards-compliant browsers
   try {
      element = document.createElement('<'+type+' name="'+name+'">');
   } catch (e) {
   }
   if (!element || element.nodeName != type.toUpperCase()) {
      // Non-IE browser; use canonical method to create named element
      element = document.createElement(type);
      element.name = name;
   }
   return element;
}

// adds options empty,0-(z-1) to select with option x preselected
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
  return (el.oldValue != el.value);
}

function setFormEncoding(formName, encoding) {
  document.forms[formName].enctype = encoding;
  document.forms[formName].encoding = encoding; // IE
}

//--------------- Scroll position saving/restoring --------------//
function saveScrollCoordinates(form) {
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
	
	// alert("x = " + x + "; y = " + y);
	
	form.windowScrollX.value = x;
	form.windowScrollY.value = y;
} 

function scrollToCoordinates(x, y) {
	// alert("x = " + x + "; y = " + y);
	window.scrollTo(x, y);
} 

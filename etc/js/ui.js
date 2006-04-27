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

// TODO: remove this function
function fillText(systemForm, el) {
  if (systemForm[el+'.select1'].value=='' && systemForm[el+'.select2'].value=='') {
    systemForm[el+'.time'].value='';
  }
  else {
    systemForm[el+'.time'].value=systemForm[el+'.select1'].value+':'+systemForm[el+'.select2'].value;
  }
}

function fillTimeText(systemForm, el) {
  if (systemForm[el+'.select1'].value=='' && systemForm[el+'.select2'].value=='') {
    systemForm[el].value='';
  }
  else {
    systemForm[el].value=systemForm[el+'.select1'].value+':'+systemForm[el+'.select2'].value;
  }
}
    
// TODO: remove this function
function fillSelect(systemForm, el) {
  separatorPos = systemForm[el+'.time'].value.indexOf(':');
  if (systemForm[el+'.time'].value.substr(0, separatorPos).length==1) {
    firstSelect = '0'+systemForm[el+'.time'].value.substr(0, separatorPos);
  }
  else {
    firstSelect = systemForm[el+'.time'].value.substr(0, separatorPos);
  }
  systemForm[el+'.select1'].value=firstSelect;
  systemForm[el+'.select2'].value=systemForm[el+'.time'].value.substr(separatorPos+1, systemForm[el+'.time'].value.length);
}

function fillTimeSelect(systemForm, el) {
  separatorPos = systemForm[el].value.indexOf(':');
  if (systemForm[el].value.substr(0, separatorPos).length==1) {
    firstSelect = '0'+systemForm[el].value.substr(0, separatorPos);
  }
  else {
    firstSelect = systemForm[el].value.substr(0, separatorPos);
  }
  systemForm[el+'.select1'].value=firstSelect;
  systemForm[el+'.select2'].value=systemForm[el].value.substr(separatorPos+1, systemForm[el].value.length);
}

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
 * Jsp-Ui.js
 *
 * A layer of indirection over the UiFormValidator class presented in
 * jsp-ui-form-validation.js
 *
 * The functions presented here are invoked from the custom tags of
 * JSP-UI.
 * Therefore you CAN'T be too liberal with them unless you know what you are doing.
 *
 * Created: 31.05.2004
 * Author: Konstantin Tretyakov.
 *
 * For documentation consult the README.TXT file.
 */
  
 
 // Override UiFormValidator createDefaultValidatorAction function
 
 function ShowHideValidationFlagAction(elementName){
   this.elementName = elementName;
   
   this.execute = function(checkPassed, errorMessage){
           var el = document.getElementById('img_' + this.elementName);
           if (!el) return;
     if (checkPassed == true) el.style.visibility = 'hidden';
     else el.style.visibility = 'visible';
   }
 }
 
 UiFormValidator.prototype.createDefaultValidatorAction = function(elementName, elementLabel) {
   return new ShowHideValidationFlagAction(elementName);
 }

 
 /** 
  * An object of type UiSystemFormProperties will be assigned to each system form
  * and will be accessible as document.forms[systemFormName].uiProperties
  */
 function UiSystemFormProperties(systemFormName){
   this.name = systemFormName;
   this.widgets = new Object();
 }
 
 /**
  * Analogously, an object of type UiFormProperties will be assigned to each form in a systemform
  * and be accessible as uiSystemFormProperties[formName].
  */
 function UiFormProperties(formName, systemFormName){
   this.name = formName;
   this.validator = UiFormValidator.createDefault(systemFormName);
 }
 
 // The uiSystemFormProperties for the systemform, for which uiSystemFormContext was invoked last.
 var uiSystemFormProperties;
 
 // The uiFormProperties of a form, whose uiFormContext was invoked last.
 var uiFormProperties;        
 
 /**
  * Function that is invoked when a context of a systemForm is reached
  */
 function uiSystemFormContext(systemFormName){
   var form = document.forms[systemFormName];
   if (!form) window.status = 'Javascript error: cannot find html form with name ' + systemFormName + '!';
   else if (!form.uiProperties){
     uiSystemFormProperties = new UiSystemFormProperties(systemFormName);
     form.uiProperties = uiSystemFormProperties;
   }
   else uiSystemFormProperties = form.uiProperties;
 }
 
  /**
  * Function that is called when a context of a certain widget is reached.
  * Assumes that it is in a context of a containing systemform.
  */
 function uiWidgetContext(widgetName){
   if (!uiSystemFormProperties) window.status = 'Javascript error: current system form context not found!';
   else if (!uiSystemFormProperties.widgets[widgetName]) {
     uiSystemFormProperties.widgets[widgetName] = true;
     var widgetPresent=document.createElement('input');
     widgetPresent.setAttribute('name',widgetName+".__present");
     widgetPresent.setAttribute('type','hidden');
     widgetPresent.setAttribute('value','true'); 
     var elems=document.getElementsByTagName("body");
     elems.item(0).appendChild(node);
   }
 }
 
 /**
  * Function that is called when a context of a certain form is reached.
  * Assumes that it is in a context of a containing systemform.
  */
 function uiFormContext(formName){
   if (!uiSystemFormProperties) window.status = 'Javascript error: current system form context not found!';
   else if (!uiSystemFormProperties[formName]) {
     uiFormProperties = new UiFormProperties(formName, uiSystemFormProperties.name);
     uiSystemFormProperties[formName] = uiFormProperties;
   }
   else {
     uiFormProperties = uiSystemFormProperties[formName];
   }
 }
 
 /**
  * Function that is invoked when a context of a certain form element is reached.
  * Assumes that it is in a context of a containing form
  *   elementName is the fully qualified name of the element.
  *   spanId is the id of a span containing this element (there may be several
  *          spans related to an element)
  *   valid  is true/false depending on whether the element is to be displayed as
  *          initially valid or invalid.
  */
 function uiFormElementContext(elementName, spanId, valid){
   span=document.getElementById(spanId);
   if (document.addEventListener) { // Moz
     span.onkeydown=function(event) { return uiHandleKeypress(event, elementName); };
   } else {
     span.onkeydown=function() { return uiHandleKeypress(event, elementName); };
   }
   var hiddenPresent = document.createElement('input');
   hiddenPresent.setAttribute('name',elementName+".__present");
   hiddenPresent.setAttribute('type','hidden');
   hiddenPresent.setAttribute('value','true'); 
   span.appendChild(hiddenPresent);
 }
 
 
 function inFormContext(){
   if (!uiFormProperties) {
     window.status = 'Javascript error: current form context not found!';
     return false;
   }
   return true;
 }
 
 
 // ------------------ Functions to add validators ------------------------ //
 function uiAddDateValidator(name, label, mandatory){
   if (!inFormContext()) return;
   uiFormProperties.validator.addDateValidator(name, label, mandatory);
 }
 
 function uiAddDateTimeValidator(name, label, mandatory){
   if (!inFormContext()) return;
   uiFormProperties.validator.addDateTimeValidator(name, label, mandatory);
 }
 
 function uiAddTimeValidator(name, label, mandatory){
   if (!inFormContext()) return;
   uiFormProperties.validator.addTimeValidator(name, label, mandatory);
 }
 
 function uiAddMultiselectValidator(name, label, mandatory){
   if (!inFormContext()) return;
   uiFormProperties.validator.addMultiselectValidator(name, label, mandatory);
 }
 
 function uiAddRadioValidator(name, label, mandatory){
   if (!inFormContext()) return;
   uiFormProperties.validator.addRadioValidator(name, label, mandatory);
 } 
 
 function uiAddSelectValidator(name, label, mandatory){
   if (!inFormContext()) return;
   uiFormProperties.validator.addSelectValidator(name, label, mandatory);
 }
 
 function uiAddRealValidator(name, label, mandatory, minValue, maxValue){
   if (!inFormContext()) return;
   uiFormProperties.validator.addRealValidator(name, label, minValue, maxValue, mandatory);
 }
 
 function uiAddIntegerValidator(name, label, mandatory, minValue, maxValue){
   if (!inFormContext()) return;
   uiFormProperties.validator.addIntegerValidator(name, label, minValue, maxValue, mandatory);
 }
 
 function uiAddTextAreaValidator(name, label, mandatory) {
   if (!inFormContext()) return;
   uiFormProperties.validator.addTextAreaValidator(name, label, undefined, undefined, mandatory);
 }
 
 // MinLength/maxLength are only applicable if type='TEXT'
 function uiAddTextInputValidator(name, label, mandatory, type, minLength, maxLength) {
   if (!inFormContext()) return;
   if (type == 'TEXT')
     uiFormProperties.validator.addTextInputValidator(name, label, minLength, maxLength, mandatory);
   else if (type == 'PERSONAL_ID')
     uiFormProperties.validator.addIsikukoodValidator(name, label, mandatory);
   else if (type == 'NUMBER_ONLY')
     uiFormProperties.validator.addRegExpValidator(name, label, /^(\+|-)?[0-9]+$/, Localization.validation.FIELD_X_MUST_BE_AN_INTEGER, mandatory);     
   else if (type == 'EMAIL')
     uiFormProperties.validator.addEmailValidator(name, label, mandatory);
   else
     uiFormProperties.validator.addMandatoryValidator(name, label, mandatory);
 }
 
 function uiAddDefaultValidator(name, label, mandatory) {
   if (!inFormContext()) return;
   uiFormProperties.validator.addMandatoryValidator(name, label, mandatory);
 }

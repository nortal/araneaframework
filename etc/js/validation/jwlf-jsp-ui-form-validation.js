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
 * Jwlf-Jsp-Ui-Specific Form Validation.
 *
 * This file contains UiFormValidator, a custom subclass of FormValidator
 * (from clientside-form-validation.js)
 * The new class only provides convenience methods for adding
 * validators, that provide functionality required by Jwlf-Ui-Jsp 
 *
 * This class, UiFormValidator, provides a certain interface for Jwlf-Ui-Jsp,
 * that should be preserved, though if custom behaviour is required 
 * (e.g. if the default date/time formats are not the ones that are needed), 
 * the methods of UiFormValidator may be overwritten.
 * These methods may not be deleted, however, as JWLF-JSP-UI may rely on their existence.
 *
 * The createDefault method of UiFormValidator may need to be redeclared.
 * as it is used to create a new instance of UiFormValidator, and therefore supplies
 * required parameters to the constructor.
 * Analogously, createDefaultAction method may need overriding.
 *
 * Instead of that, an additional layer of indirection may be implemented to encapsulate the details.
 *
 * Created: 19.05.2004
 * Author: Konstantin Tretyakov.
 * For documentation consult the README.TXT file.
 */
  
 /**
  * Create UiFormValidator.
  * Parameters:
  *   formName - the name of the (system) form for which all the element 
  *              validators will be added.
  *   elementPostfix - a String, that should be added to the element's name
  *              to obtain the ID of the span containing it.
  *              The className of this span will then be changed to reflect element's validity.
  *              Typical value could be "-element" or ".validationUnit".
  *              This property (and the properties below) have sense only if that specific model is used
  *              where CSS class of a certain span is toggled to indicate element validity/invalidity.
  *   validClass - the className for valid elements
  *   invalidClass - the className for invalid elements
  */
 function UiFormValidator(systemFormName, elementPostfix, validClass, invalidClass) {
   this.base = FormValidator;
   this.base();
   this.formName = systemFormName;
   this.elementPostfix = elementPostfix;
   this.validClass = validClass;
   this.invalidClass = invalidClass;
   
   /**
    * Returns the first validator found that has a property elementName equal to given.
    * if no such returns null
    */
   this.findValidatorForElement = function(elementName){
   	 for (var i = 0; i < this.validators.length; i++){
   	 	var v = this.validators[i];
   	 	for (var j = 0; j < v.rules.length; j++){
   	 	  var r = v.rules[j];
   	 	  if (r.elementName && r.elementName == elementName) return v;   	
   	 	} 
   	 }
   	 return undefined;
   }
 }
 UiFormValidator.prototype = new FormValidator;

 
 /**
  * Creates a new instance of UiFormValidator with the default actions/validators set up.
  * This method is used to create UiFormValidator by JWLF-JSP-UI custom tags,
  * therefore you may redefine it if you wish to supply custom parameters to the constructor.
  * The method should add the required actions, so that it would be possible to perform
  * all the validation as:
  *   var v = UiFormValidator.createDefault('someForm');
  *   v.add...Validator(..);
  *   v.add...Validator(..);
  *   ...
  *   
  *   if (v.validate()) ....;
  */
 UiFormValidator.createDefault = function(systemFormName) {
   var v = new UiFormValidator(systemFormName, '.validationUnit', 'form-element-valid', 'form-element-invalid');
   v.addAction(new MessageBoxAction());   
   return v;
 }
  

 /**
  * This function creates an action that is appended to all validators created
  * by this validator.
  * Default is ChangeCssClassAction.
  */
 UiFormValidator.prototype.createDefaultValidatorAction = function(elementName, elementLabel) {
   return new ChangeCssClassAction(elementName + this.elementPostfix, this.validClass, this.invalidClass); 
 }
 
 /**
  * Add a text validator.
  * elementName is the full name of the element (as it is in HTML)
  * elementLabel is the Label to be shown in the error message.
  * minLength/maxLength - constraints on text length.
  */
 UiFormValidator.prototype.addTextInputValidator = function(elementName, elementLabel, minLength, maxLength, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true) 
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   v.addRule(new MinMaxLengthRule(this.formName, elementName, elementLabel, minLength, maxLength));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }

 
 /**
  * addTextAreaValidator - currently another name for addTextInputValidator
  */
 UiFormValidator.prototype.addTextAreaValidator = UiFormValidator.prototype.addTextInputValidator;

 
 /**
  * Add a regexp validator
  */
 UiFormValidator.prototype.addRegExpValidator = function(elementName, elementLabel, regExp, messageEnd, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true) 
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   v.addRule(new RegExpRule(this.formName, elementName, elementLabel, regExp, messageEnd));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 } 
 
 
 /**
  * Add an integer validator (note: it does not allow integers like 10.0 or 1E6)
  */
 UiFormValidator.prototype.addIntegerValidator = function(elementName, elementLabel, minValue, maxValue, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   v.addRule(new NumberRule(this.formName, elementName, elementLabel, true, minValue, maxValue));
   v.addRule(new RegExpRule(this.formName, elementName, elementLabel, /^(\+|-)?[0-9]+$/, 
             Localization.validation.FIELD_X_MUST_BE_AN_INTEGER));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }  
 
 
 /**
  * Add an real number validator (note: it does not allow exponent notation like 1.0E+6)
  */
 UiFormValidator.prototype.addRealValidator = function(elementName, elementLabel, minValue, maxValue, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   // NB: We allow comma as decimal separator
   v.addRule(new NumberRule(this.formName, elementName, elementLabel, false, minValue, maxValue, true));
   v.addRule(new RegExpRule(this.formName, elementName, elementLabel, /^(\+|-)?[0-9]+((\.|,)[0-9]+)?$/, 
             Localization.validation.FIELD_X_MUST_BE_A_REAL_NUMBER));
   
   // We could even have replaced comma with a point but that's not needed.
   // We are validating, not correcting.
   //     v.addAction(new ReplaceIfValidAction(this.formName, elementName, /,/, "."));
   
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }  
 
 
 /**
  * Add a validator for isikukood input field
  *   mandatory - if set to false, allows validation to pass if the field is empty.
  */
 UiFormValidator.prototype.addIsikukoodValidator = function(elementName, elementLabel, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   v.addRule(new ElementFormatRule(this.formName, elementName, elementLabel, true, 
     Localization.validation.FIELD_X_MUST_BE_AN_ISIKUKOOD, JSValidation.checkIsikukood));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }
 
 /**
  * Add a validator for email address input field
  *   mandatory - if set to false, allows validation to pass if the field is empty.
  */
 UiFormValidator.prototype.addEmailValidator = function(elementName, elementLabel, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   v.addRule(new ElementFormatRule(this.formName, elementName, elementLabel, true, 
     Localization.validation.FIELD_X_MUST_BE_AN_EMAIL, JSValidation.checkEmail));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }
 
 /**
  * Add a validator for an input element, that checks that there is some value in case the element
  * is mandatory, and does nothing if the element is not mandatory. This kind of validation is used
  * for select/multiselect boxes
  *   mandatory - if this parameter is set to false, validation always passes, otherwise validation fails if
  *               the element's value is empty.
  */
 UiFormValidator.prototype.addMandatoryValidator = function(elementName, elementLabel, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }

 /**
  * Validators for select/multiselect/radio boxes are currently simply the "mandatority validators"
  * We still present them as separate functions so that we may redefine them in future if it is necessary.
  */
 UiFormValidator.prototype.addSelectValidator = UiFormValidator.prototype.addMandatoryValidator;
 UiFormValidator.prototype.addMultiselectValidator = UiFormValidator.prototype.addMandatoryValidator;
 
 
 /**
  * Radio box validator is equivalent to the select/multiselect validators, but we must implement it separately, 
  * because NonEmptyRule doesn't work for radio buttons.
  * One more specific property is that addRadioValidator is typically called once for each
  * radio button. (i.e. it is invoked several times with the same id).
  * So if we see that we already have a validator for the same element, we ignore the call.
  */
 UiFormValidator.prototype.addRadioValidator = function(elementName, elementLabel, mandatory) {
   // Do not add radio validator again for the same element
   if (this.findValidatorForElement(elementName)) return;
   
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRadioRule(this.formName, elementName, elementLabel));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }

 /**
  * Date validator checks that a date is in one of given formats
  */
 UiFormValidator.prototype.addDateValidator = function(elementName, elementLabel, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   v.addRule(new DateFormatRule(this.formName, elementName, elementLabel, 
     "DD.MM.YYYY|DD.MM.YY|DDMMYYYY|DDMMYY|DD,MM,YYYY|DD,MM,YY|DDMM"));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }
 
 /**
  * Time validator checks that time is in one of given formats
  */
 UiFormValidator.prototype.addTimeValidator = function(elementName, elementLabel, mandatory) {
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRule(this.formName, elementName, elementLabel));
   v.addRule(new TimeFormatRule(this.formName, elementName, elementLabel, 
     "HH:MM|HHMM|HH,MM|HH.MM|H:MM|HMM|H,MM|H.MM"));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }
 
 /**
  * Validator for the DateTime conrol.
  * elementName here means the "common" name of both the date and time subcontrols.
  * That is, if the datetime control consists of controls (form.dtctl.time, form.dtctl.date) then
  * elementName should be form.dtctl
  */
 UiFormValidator.prototype.addDateTimeValidator = function(elementName, elementLabel, mandatory){
   var v = new Validator();
   if (mandatory && mandatory == true)
     v.addRule(new NonEmptyRule(this.formName, elementName + ".date", elementLabel));
   v.addRule(new DateFormatRule(this.formName, elementName + ".date", elementLabel, 
     "DD.MM.YYYY|DD.MM.YY|DDMMYYYY|DDMMYY|DD,MM,YYYY|DD,MM,YY|DDMM"));
   v.addRule(new TimeFormatRule(this.formName, elementName + ".time", elementLabel, 
     "HH:MM|HHMM|HH,MM|HH.MM|H:MM|HMM|H,MM|H.MM"));
   v.addAction(this.createDefaultValidatorAction(elementName, elementLabel));
   this.addValidator(v);
 }

 

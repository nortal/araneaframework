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
 * Javascript Client-side Form Validation Utilities
 *
 * This file contains base utility classes required for
 * performing client-side form validation.
 * This file depends on localization.js
 *
 * Created: 18.05.2004
 * Author: Konstantin Tretyakov.
 *
 * For documentation consult the README.TXT file.
 */
  
 

 /** 
  * A Validator is a class that combines several rules with actions.
  * It contains a "validate()" method and a "errorMessage" property. In this
  * sense it is similar to a Rule. Two main differences are:
  *   - the "check()" method of a rule may not have side effects,
  *     but "validate()" method of validator does have.
  *   - there are many different "Rule"s, but only one Validator class.
  *
  * Validator aggregates several rules and on the "validate()" method, it
  * invokes rule checks in order. If any check fails, it stops further
  * checks and sets its "errorMessage" property to the errorMessage of the last 
  * check that failed.
  * After performing the checks (no matter whether they succeeded or not)
  * it invokes the actions in order.
  */ 
  function Validator(){
    this.rules = new Array();
    this.actions = new Array();
    this.errorMessage = "";
    
    this.addRule = function(r){
      this.rules[this.rules.length] = r;
    }
    
    this.addAction = function(a){
      this.actions[this.actions.length] = a;
    }
    
    this.validate = function() {
      var i;
      var result = true;
      
      for (i = 0; i < this.rules.length; i++){
        if (!this.rules[i].check()) {
          result = false;
          this.errorMessage = this.rules[i].errorMessage;
          break;
        }
      }
      
      for (i = 0; i < this.actions.length; i++)
        this.actions[i].execute(result, this.errorMessage);
        
      return result;
    }
  }
  
  
 /**
  * FormValidator class.
  * FormValidator combines several Validators in the same way Validator
  * combines Rules. On the validate() method it first invokes all the
  * preprocessActions it has, then it invokes validate() methods
  * of all the validators. All the errorMessages from the Validators
  * are combined in one.
  *
  * Afterwards, actions are executed in the same way.
  */
  function FormValidator(){
    this.validators = new Array();
    this.actions = new Array();
    this.preprocessActions = new Array();
    this.errorMessage = "";
    
    this.addValidator = function(r){
      this.validators[this.validators.length] = r;
    }
    
    this.addPreprocessAction = function(a){
      this.preprocessActions[this.preprocessActions.length] = a;
    }
    
    this.addAction = function(a){
      this.actions[this.actions.length] = a;
    }
    
    this.validate = function() {
      var i;
      var result = true;
      this.errorMessage = "";

      // Invoke preprocess actions (if any)
      for (i = 0; i < this.preprocessActions.length; i++)
        this.preprocessActions[i].execute();
        
      // Invoke the validators
      for (i = 0; i < this.validators.length; i++){
        var v = this.validators[i];
        if (!v.validate()) {
          result = false;

          if (this.errorMessage == "") this.errorMessage = v.errorMessage;
          else this.errorMessage = this.errorMessage + "\n"
                                           + v.errorMessage;
        
        }
      }
      
      // Invoke actions
      for (i = 0; i < this.actions.length; i++)
        this.actions[i].execute(result, this.errorMessage);
        
      return result;
    }
  }

  

// ----------------------------------- Actions ----------------------------------- //
 /**
  * Change the Css class of a given element according to whether the
  * check passed or not.
  */
  function ChangeCssClassAction(elementId, validClass, invalidClass) {
    this.elementId = elementId;
    this.validClass = validClass;
    this.invalidClass = invalidClass;
    
    this.execute = function(checkPassed, errorMessage) {
      var element = document.getElementById(elementId);
      if (!element) window.status = "Validation error: no element with id '" + this.elementId + "'";
      if (element) {
        element.className = checkPassed == true ? this.validClass : this.invalidClass;
      }
    }
  }

  /**
   * If the element was valid, replaces regexpToReplace with replaceWith
   * Perhaps the only usage for it is to replace ',' with '.' in number fields.
   */
  function ReplaceIfValidAction(formName, elementName, regexpToReplace, replaceWith) {
    this.elementName = elementName;
    this.formName = formName;
    this.regexpToReplace = regexpToReplace;
    this.replaceWith = replaceWith;
    
    this.execute = function(checkPassed, errorMessage) {
      if (checkPassed == true) {
        var element = document.forms[formName].elements[elementName];
        if (!element) window.status = "Validation error: no element with name '" + this.elementName + "'";
        if (element) {
          element.value = element.value.replace(this.regexpToReplace, this.replaceWith);
        }
      }
    }
  }
  
  /**
   * This action is suitable for FormValidator.
   * If errors occured, a given DIV will be shown with errorMessages on it.
   * Otherwise, that DIV will be hidden
   */
  function ShowErrorMessagesDivAction(divId){
    this.divId = divId;
    
    this.execute = function(checkPassed, errorMessage){
      var elem = document.getElementById(this.divId);
      if (checkPassed == true) {
        elem.style.visibility = 'hidden';
        elem.style.height = '0px';
      }
      else {
        elem.style.visibility = 'visible';
        elem.style.height = 'auto';
       
        // Replace all newlines with <br /> tags in errorMessages
        elem.innerHTML = errorMessage.replace(/\n/g, "<br />");
      }
    }
  }  
  
  /**
   * Another action typical for FormValidator.
   * If errors occured, a messagebox is shown with the errors.
   */
  function MessageBoxAction(){
    this.execute = function(checkPassed, errorMessage){
      if (checkPassed == false) alert(errorMessage);
    }
  }  
  
// ----------------------------------- Rules ----------------------------------- //
 
 /**
  * A base class for all Rule-s that check properties of form elements
  * (i.e., practically all rules)
  * Parameters:
  *   formName, elementName - the name of the form and the element within it that identify the
  *                           form element to be checked.
  *   elementLabel          - string to be used in error message to refer to that element.
  */
  function FormElementRule(formName, elementName, elementLabel){
    this.formName = formName;
    this.elementName = elementName;
    this.elementLabel = elementLabel;
    this.errorMessage = "";
    
    this.getElement = function() {
      var el = document.forms[this.formName].elements[this.elementName];
      if (!el) window.status = "Validation error: no element named '" + this.elementName + "' found";
      return el;
    }
    
    this.check = function() {};
  }
  
  
   
  /**
   * A base class for all FormElementRules that check that the contents of an element satisfies some
   * certain function. That is, the rule returns true, if 
   *    checkerFunction(element.value, param1, param2, param3, ...)
   * returns true.
   * Practically all the rules may be expressed in this way, and having this rule allows
   * to separate condition checking function (that takes a string and a condition) from 
   * rule-specific things (like obtaining element's value and returning an error message).
   * Parameters:
   *    formName, elementName, elementLabel - see FormElementRule
   *    allowEmpty           - Setting this parameter to true will allow empty input to pass the test.
   *                           (i.e. when allowEmpty=true and then the element will pass the test
   *                           EITHER if its value is empty OR if it is not empty and satisfies the condition.
   *    message              - Specifies the message to be displayed. 
   *                             Default is like "Field {0} must match format {1}"
   *
   *    formatCheckFunction  -  a function: (string, param1, param2, ...) -> boolean that checks that given 
   *                            string satisfies given conditions.
   *                            Examples are checkMinMaxLength, checkDateInFormat, checkTimeInFormat,
   *                                         checkRegexpMatch, ...
   *    param1, param2, .... -  the parameters that are passed to formatCheckFunction
   */
  function ElementFormatRule(formName, elementName, elementLabel, allowEmpty, message, formatCheckFunction, param1, param2){
    this.base = FormElementRule;
    this.base(formName, elementName, elementLabel);
    this.allowEmpty = typeof(allowEmpty) != 'undefined' ? allowEmpty : true;
    this.formatCheckFunction = formatCheckFunction;
    this.errorMessage = message ? message : Localization.validation.FIELD_X_IS_IN_INCORRECT_FORMAT;
    this.functionArguments = new Array();
    var i;
    for (i = 6; i < arguments.length; i++)
      this.functionArguments.push(arguments[i]);
      
    this.check = function () {
      var el = this.getElement();
      if (!el) return true;
      if (this.allowEmpty == true && el.value.length == 0) return true;
      else {
        // Invoke formatCheckFunction with functionArguments
        // Construct a string like "el.value, this.functionArguments[0], ...";
        var s = "el.value";
        var i;
        for (i = 0; i < this.functionArguments.length; i++)
          s = s + ", this.functionArguments[" + i + "]";
        s = "this.formatCheckFunction(" + s + ")";
        
        var result = eval(s);
        if (!result){
          this.errorMessage = Localization.replaceParam(this.errorMessage, 0, this.elementLabel);
        }
        return result;
      }
    }
  }
  ElementFormatRule.prototype = new FormElementRule;
  
  
    
 /**
  * Checks that the length of the text element is within minLength and maxLength
  * Any of these values may also be left undefined (then corresponding length restriction does not hold)
  * Setting the allowEmpty parameter to true (or omitting it) will allow empty input to pass the test.
  * (i.e. when allowEmpty=true and minLength=3, then in order to pass the test, element's value should
  * be either empty, or no shorter than 3.)
  *
  * Actually in most cases you should omit the allowEmpty parameter,
  * and use the NonEmptyRule for mandatory form elements.
  */
  function MinMaxLengthRule(formName, elementName, elementLabel, minLength, maxLength, allowEmpty){
    this.base = ElementFormatRule;
    
    var message;
    
    if (minLength && maxLength) {
      message = Localization.validation.FIELD_X_LENGTH_MUST_BE_BETWEEN_Y_AND_Z;
      message = Localization.replaceParam(message, 1, minLength);
      message = Localization.replaceParam(message, 2, maxLength);
    }
    else if (minLength && !maxLength){
      message = Localization.validation.FIELD_X_MIN_LENGTH_IS_Y;
      message = Localization.replaceParam(message, 1, minLength);
    }
    else if (maxLength && !minLength){
      message = Localization.validation.FIELD_X_MAX_LENGTH_IS_Y;
      message = Localization.replaceParam(message, 1, maxLength);
    }

    // Call the super constructor
    this.base(formName, elementName, elementLabel, allowEmpty, message, 
              JSValidation.checkMinMaxLength, minLength, maxLength);
  }
  MinMaxLengthRule.prototype = new ElementFormatRule;
  

  /**
   * Checks that the value of the element matches given regular expression.
   * A customization of the ElementFormatRule.
   */
  function RegExpRule(formName, elementName, elementLabel, regExp, message, allowEmpty){
    this.base = ElementFormatRule;
    if (!message) {
      message = Localization.validation.FIELD_X_MUST_MATCH_REGEXP_Y;
      message = Localization.replaceParam(message, 1, regExp.source);
    }
    this.base(formName, elementName, elementLabel, allowEmpty, message, 
              JSValidation.checkRegexpMatch, regExp);
  }
  RegExpRule.prototype = new ElementFormatRule;
  
    
  /**
   * Checks that the value of an element is a number between two given bounds.
   * Note that this check should be used only to check that the number is within the bounds,
   * not that this field "is a number", because this rule will accept any input that javascript understands
   * as a number. You should use the RegExpRule to check that the field "is a number" in your certain format.
   * Also note, that if there is no value in the element, the check always passes.
   *
   * Parameters:
   *   minValue, maxValue - the bounds on the number. Any of them may be omitted.
   *   requireInteger     - accept only integers (i.e. round(v) == v must hold).
   *   allowComma         - allow ',' as the decimal separator (default: no)
   *                        (if you use this rule together with the RegExpRule for validating number format
   *                         then keep this property consistent with your regexp)
   */
  function NumberRule(formName, elementName, elementLabel, requireInteger, minValue, maxValue, allowComma){
    this.base = ElementFormatRule;

    var message;

    if (minValue && maxValue) {
      message = requireInteger ? Localization.validation.FIELD_X_INT_VALUE_MUST_BE_BETWEEN_Y_AND_Z:
                                 Localization.validation.FIELD_X_VALUE_MUST_BE_BETWEEN_Y_AND_Z;
      message = Localization.replaceParam(message, 1, minValue);
      message = Localization.replaceParam(message, 2, maxValue);
    }
    else if (minValue && !maxValue){
      message = requireInteger ? Localization.validation.FIELD_X_INT_MIN_VALUE_IS_Y:
                                 Localization.validation.FIELD_X_MIN_VALUE_IS_Y;
      message = Localization.replaceParam(message, 1, minValue);
    }
    else if (maxValue && !minValue){
      message = requireInteger ? Localization.validation.FIELD_X_INT_MAX_VALUE_IS_Y:
                                 Localization.validation.FIELD_X_MAX_VALUE_IS_Y;
      message = Localization.replaceParam(message, 1, maxValue);      
    }
    else {
      message = requireInteger ? Localization.validation.FIELD_X_MUST_BE_AN_INTEGER:
                                 Localization.validation.FIELD_X_MUST_BE_A_REAL_NUMBER;
    }

    // Call the superconstructor
    this.base(formName, elementName, elementLabel, true, message, 
              JSValidation.checkNumber, minValue, maxValue, requireInteger, allowComma);
  }
  NumberRule.prototype = new ElementFormatRule;
 
  
  /**
   * Checks that element is not empty
   */
  function NonEmptyRule(formName, elementName, elementLabel) {
    this.base = ElementFormatRule;
    this.base(formName, elementName, elementLabel, false, Localization.validation.FIELD_X_IS_MANDATORY,
              JSValidation.checkNotEmpty); // is required
  }
  NonEmptyRule.prototype = new ElementFormatRule;
  
  
  /**
   * Unfortunately NonEmptyRule is not applicable to radio-type elements, so we have to 
   * create a separate version for them
   */
  function NonEmptyRadioRule(formName, elementName, elementLabel) {
    this.base = FormElementRule;
    this.base(formName, elementName, elementLabel);
    this.errorMessage = Localization.validation.FIELD_X_IS_MANDATORY;
    
    this.check = function () {
      var el = this.getElement();
      if (!el) return true;
      // Now we assume that el is a radio input
      var result = false;
      var i;
      for (i = 0; i < el.length; i++)
        if (el[i].checked == true) {
          result = true;
          break;
        }
      if (!result) 
        this.errorMessage = Localization.formatMessage(this.errorMessage, new Array(this.elementLabel));
      return result;
    }
  }
  NonEmptyRule.prototype = new FormElementRule;
  

// ----------------------------------- Utilities  ----------------------------------- //
// ----------------------------------- Format Checkers ----------------------------------- //
// ------------------------------- (see ElementFormatRule) ------------------------------- //
  /**
   * This is not a function nor an object.
   * This is a "scope" for the helper-functions in this module.
   * I.e. it's a static class.
   */
  function JSValidation(){}
  
  // Returns true if the string is not empty
  JSValidation.checkNotEmpty = function (str){
    return (str && str.length > 0);
  } 
   
  // Returns true if string matches given regexp
  JSValidation.checkRegexpMatch = function(str, regexp){
    return (regexp.exec(str) ? true : false)
  }
  
  // Returns true if string length is between given bounds
  JSValidation.checkMinMaxLength = function(str, minLength, maxLength){
    if (minLength && str.length < minLength  ||
        maxLength && str.length > maxLength)
       return false;
    else
       return true;
  }
  
  // Checks that str is a number
  JSValidation.checkNumber = function(str, minValue, maxValue, requireInteger, allowComma){
    if (!str || str.length == 0) return false;
    if (allowComma == true) {
      str = str.replace(/,/, ".");
    }
    if (isNaN(str)) return false;
    else {
      if (maxValue && str > maxValue || minValue && str < minValue) return false;
      else {
        // Check that it is an integer
        if (requireInteger == true && Math.round(str) != str) return false;
          else return true;
      }
    }
  }

  // Returns true if s is a valid isikukood
  JSValidation.checkIsikukood = function (s){
    if (!s || s == "") return false;
    if (!s.match(/^[0-9]{11}$/)) return false;
    else {
      var checksum = s.charAt(10);
      var weight = (s.charAt(0)*1 + s.charAt(1)*2 + s.charAt(2)*3 + s.charAt(3)*4 + s.charAt(4)*5
                  + s.charAt(5)*6 + s.charAt(6)*7 + s.charAt(7)*8 + s.charAt(8)*9 + s.charAt(9)*1) % 11;
      if (weight == 10)
        weight = (s.charAt(0)*3 + s.charAt(1)*4 + s.charAt(2)*5 + s.charAt(3)*6 + s.charAt(4)*7 + s.charAt(5)*8
                + s.charAt(6)*9 + s.charAt(7)*1 + s.charAt(8)*2 + s.charAt(9)*3) % 11;
      return (weight == checksum);
    }
  }

  /**
   * A weirdly complex function to validate emails. Don't ask me why one couldn't make it 
   * in a more simple way, I think I'd just copy-paste it here.
   * Most probably taken from 
   *  http://javascript.internet.com/forms/email-validation---basic.html
   */
  JSValidation.checkEmail = function (s){
    if (!s || s == "") return false;
    var result = true;
    var emailStr = s;
    var emailPat=/^(.+)@(.+)$/;
    var specialChars="\\(\\)<>@,;:\\\\\\\"\\.\\[\\]";
    var validChars="\[^\\s" + specialChars + "\]";
    var quotedUser="(\"[^\"]*\")";
    var ipDomainPat=/^(\d{1,3})[.](\d{1,3})[.](\d{1,3})[.](\d{1,3})$/;
    var atom=validChars + '+';
    var word="(" + atom + "|" + quotedUser + ")";
    var userPat=new RegExp("^" + word + "(\\." + word + ")*$");
    var domainPat=new RegExp("^" + atom + "(\\." + atom + ")*$");
    var matchArray=emailStr.match(emailPat);
    if (matchArray == null) {
      return false;
    }
    var user=matchArray[1];
    var domain=matchArray[2];
    if (user.match(userPat) == null) {
      return false;
    }
    var IPArray = domain.match(ipDomainPat);
    if (IPArray != null) {
      for (var i = 1; i <= 4; i++) {
        if (IPArray[i] > 255) {
          return false;
        }
      }
      return true;
    }
    var domainArray=domain.match(domainPat);
    if (domainArray == null) {
      return false;
    }
    var atomPat=new RegExp(atom,"g");
    var domArr=domain.match(atomPat);
    var len=domArr.length;
    if ((domArr[domArr.length-1].length < 2)) {
      return false;
    }
    if (len < 2) {
      return false;
    }
    return true;
  }
  
  

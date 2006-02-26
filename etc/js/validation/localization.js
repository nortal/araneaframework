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
 * Locale strings and basic localization utilities.
 *
 * This file contains strings used in javascript validation scripts
 * and some useful functions.
 * 
 *  Author: Konstantin Tretyakov
 *  Created: 4.06.2004
 */
 
 
 /**
  * Namespace for stuff related to localization.
  * Functions should be used as static methods of this class
  * (e.g. Localization.formatMessage)
  * specific messages should be stored as constants of this object's properties. 
  * (e.g. Localization.validation.VALIDATION_FAILED)
  */
 function Localization() {}
 
 
 /**
  * Takes a message and an array of parameters.
  * Substitutes parameters[i] for {i} in the message.
  */
 Localization.formatMessage = function(message, parameters){
   if (!parameters) return message;
   var i;
   var result = message;
   for (i = 0; i < parameters.length; i++) {
     var re = new RegExp("[{]" + i + "[}]", "g");
     result = result.replace(re, parameters[i]);
   }
   return result;
 }
 
 /**
  * Replaces given parameter with certain string
  * eg. replaceParam('A{1}{2}{3}{2}', 2, 'B') gives 'A{1}B{3}B'
  */
 Localization.replaceParam = function(message, paramIndex, value) {
   var re = new RegExp("[{]" + paramIndex + "[}]", "g");
   return message.replace(re, value);
 }
 
 
  
 /**
  * Strings required by the javascript validation
  * DON'T add new strings here unless you also add a base validator and
  * update all the applications that depend on these utilities.
  */
 Localization.validation = {
   FIELD_X_IS_MANDATORY : "Field '{0}' is mandatory",
   
   FIELD_X_LENGTH_MUST_BE_BETWEEN_Y_AND_Z: "Field '{0}' length must be between {1} and {2}",
   FIELD_X_MIN_LENGTH_IS_Y: "Field '{0}' minimal allowed length is {1}",
   FIELD_X_MAX_LENGTH_IS_Y: "Field '{0}' maximal allowed length is {1}",
   
   FIELD_X_VALUE_MUST_BE_BETWEEN_Y_AND_Z: "Field '{0}' value must be between {1} and {2}",
   FIELD_X_MIN_VALUE_IS_Y: "Field '{0}' value must be not less than {1}",
   FIELD_X_MAX_VALUE_IS_Y: "Field '{0}' value must be not greater than {1}",
   FIELD_X_INT_VALUE_MUST_BE_BETWEEN_Y_AND_Z: "Field '{0}' value must be an integer between {1} and {2}",
   FIELD_X_INT_MIN_VALUE_IS_Y: "Field '{0}' value must be an integer not less than {1}",
   FIELD_X_INT_MAX_VALUE_IS_Y: "Field '{0}' value must be an integer not greater than {1}",
   
   FIELD_X_IS_IN_INCORRECT_FORMAT: "Field '{0}' is in incorrect format",
   FIELD_X_MUST_MATCH_REGEXP_Y: "Field '{0}' must match regular expression '{1}'",
   
   FIELD_X_MUST_BE_A_DATE: "Field '{0}' must be a valid date",
   FIELD_X_MUST_BE_A_TIME: "Field '{0}' must be a valid time",
   FIELD_X_MUST_BE_A_DATE_IN_FORMAT_Y: "Field '{0}' must be a date in format {1}",
   FIELD_X_MUST_BE_A_TIME_IN_FORMAT_Y: "Field '{0}' must be a time in format {1}",
   
   FIELD_X_MUST_BE_AN_EMAIL: "Field '{0}' must be a valid e-mail address",
   FIELD_X_MUST_BE_AN_ISIKUKOOD: "Field '{0}' must be a valid isikukood",
   
   FIELD_X_MUST_BE_A_REAL_NUMBER: "Field '{0}' must be a real number",
   FIELD_X_MUST_BE_AN_INTEGER: "Field '{0}' must be an integer"
 }
 
 

  

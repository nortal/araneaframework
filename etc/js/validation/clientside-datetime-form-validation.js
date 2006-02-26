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
 *  date-time validation addon.
 *
 * This file contains Rules for validating date/time input.
 * It uses the definitions from the file
 *   clientside-form-validation.js
 * So that file must be included before this one
 *
 * Created: 25.05.2004
 * Author: Konstantin Tretyakov.
 *
 * For documentation consult the README.TXT file.
 */
  
//------------------------------ Helper functions ----------------------------------//
  /**
   * Checks that given value is a date in a given format.
   * Format must be specified as a string like "DD.MM.YYYY".
   * The dateString parameter will be matched against that pattern in the following way:
   *   - symbols D, M and Y in the pattern must correspond to numbers in the same positions 
   *     of the dateString
   *   - any other symbols must correspond to exactly the same symbols in the dateString
   * If dateString matches the pattern, the day, month and year are stripped from it
   * by concatenating the symbols at appropriate positions, and a check is performed that the
   * resulting values represents a valid date.
   * Pattern MUST contain D and M characters, but may lack Y. Then current year will be used to check date.
   * The whole stuff only makes sense if you have 2 D, 2 M and 0/2/4 Y characters in the pattern.
   */
  JSValidation.checkDateFormat = function(dateString, pattern){
    if (!JSValidation.matchesPattern(dateString, pattern, "DMY")) return false;
    
    // Extract the day, month and year from datestring
    var d = JSValidation.extractByMask(dateString, pattern, 'D');
    var m = JSValidation.extractByMask(dateString, pattern, 'M');
    var y = JSValidation.extractByMask(dateString, pattern, 'Y');
    if (y.length == 2) y = "20" + y;
    if (y == "") y = (new Date()).getFullYear();

    // Use Math.floor to convert values to numbers (otherwise my date check fails to recognize "02" as equal to 2)
    return JSValidation.isValidDate(Math.floor(y), Math.floor(m), Math.floor(d));
  }
  
  /**
   * Similar to checkDateFormat. This time special symbols in the pattern are 'H', 'M' and 'S'.
   * A typical pattern is therefore "HH:MM:SS"
   * H and M must be present in the pattern, S may be omitted.
   * Only 24-h format is supported. (i.e. the check is that 23 >= HH >= 00)
   */
  JSValidation.checkTimeFormat = function(timeString, pattern){
    if (!JSValidation.matchesPattern(timeString, pattern, "HMS")) return false;
    
    // Extract the hour, minute and second from timestring
    var h = JSValidation.extractByMask(timeString, pattern, 'H');
    var m = JSValidation.extractByMask(timeString, pattern, 'M');
    var s = JSValidation.extractByMask(timeString, pattern, 'S');
    if (s == "") s = 0;
    
    // Use Math.floor to convert values to numbers (otherwise my function fails to recognize "02" as equal to 2)
    return JSValidation.isValidTime24(Math.floor(h), Math.floor(m), Math.floor(s));
  }
    
  
  /**
   * Returns true, if str matches pattern. Matches means that str[i] == pattern[i] for
   * all i except where pattern[i] is a special symbol. There str[i] must be a number.
   */
  JSValidation.matchesPattern = function (str, pattern, specialSymbols){
    if (str.length != pattern.length) return false;
    var i;
    
    // Check that str matches pattern
    for (i = 0; i < str.length; i++){
      if (specialSymbols.indexOf(pattern.charAt(i)) >= 0){
        if (str.charAt(i) < '0' || str.charAt(i) > '9') return false;
      }
      else if (str.charAt(i) != pattern.charAt(i)) return false;
    }
    return true;
  }
  
  /**
   * Examples:
   *   extractByMask("123456", "AABBCC", 'A')  ->  "12"
   *   extractByMask("123456", "ABABAB", 'A')  ->  "135"
   * Assumes s.length == pattern.length and char is a single character.
   */
  JSValidation.extractByMask = function(str, pattern, maskChar){
    var i;
    var result = "";
    for (i = 0; i < str.length; i++)
      if (pattern.charAt(i) == maskChar) result = result + str.charAt(i);
    return result;
  }
  
  /**
   * Returns true if y/m/d is a valid date
   */
  JSValidation.isValidDate = function(year, month, day){
    var monthDays = new Array(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) monthDays[2] = 29;
    if (month < 1 || month > 12) return false;
    if (day < 1 || day > monthDays[month]) return false;
    return true;
  }
  
  /**
   * Returns true if h/m/s is valid time in 24-h format
   */
  JSValidation.isValidTime24 = function(hour, minute, second){
    return (hour < 24 && hour >= 0 && minute < 60 && minute >= 0 && second < 60 && second >= 0);
  }
  
  /**
   * Ensures that checkDateFormat(str, patterns[i]) returns true for at least one i
   */
  JSValidation.checkDateFormats = function(str, patterns){
    var i;
    for (i = 0; i < patterns.length; i++)
      if (JSValidation.checkDateFormat(str, patterns[i])) return true;
    return false;
  }
  
  /**
   * Ensures that checkTimeFormat(str, patterns[i]) returns true for at least one i
   */
  JSValidation.checkTimeFormats = function(str, patterns){
    var i;
    for (i = 0; i < patterns.length; i++)
      if (JSValidation.checkTimeFormat(str, patterns[i])) return true;
    return false;
  }
  
//------------------------------ Rules ----------------------------------//  

  
  /**
   * ElementFormatRule with checkDateFormat(s) as the formatCheckFunction.
   * The pattern is either a pattern in the sense of checkDateFormat, or
   * several such patterns separated by "|".
   */
  function DateFormatRule(formName, elementName, elementLabel, pattern, allowEmpty){
    this.base = ElementFormatRule;
    var functionToCall = JSValidation.checkDateFormat;
    var arg = pattern;
    var message;
    
    if (pattern.indexOf("|") >= 0) {
      functionToCall = JSValidation.checkDateFormats;
      arg = pattern.split("|");
      message = Localization.validation.FIELD_X_MUST_BE_A_DATE;
    }
    else {
      message = Localization.validation.FIELD_X_MUST_BE_A_DATE_IN_FORMAT_Y;
      message = Localization.replaceParam(message, 1, pattern);
    }
    
    this.base(formName, elementName, elementLabel, allowEmpty, message, 
      functionToCall, arg);
  }
  DateFormatRule.prototype = new ElementFormatRule;

  
  /**
   * ElementFormatRule with checkTimeFormat(s) as the formatCheckFunction.
   */
  function TimeFormatRule(formName, elementName, elementLabel, pattern, allowEmpty){
    this.base = ElementFormatRule;
    var functionToCall = JSValidation.checkTimeFormat;
    var arg = pattern;
    var message;
    
    if (pattern.indexOf("|") >= 0) {
      functionToCall = JSValidation.checkTimeFormats;
      arg = pattern.split("|");
      message = Localization.validation.FIELD_X_MUST_BE_A_TIME;
    }
    else {
      message = Localization.validation.FIELD_X_MUST_BE_A_TIME_IN_FORMAT_Y;
      message = Localization.replaceParam(message, 1, pattern);
    }
    
    this.base(formName, elementName, elementLabel, allowEmpty, message,
      functionToCall, arg);
  }
  TimeFormatRule.prototype = new ElementFormatRule;

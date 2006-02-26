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
 * Jwlf-Jsp-Ui.js template extension
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
 

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
 * Behaviour rules required for Aranea JSF integration to work correctly.
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

function aranea_jsf_input_button_submit(element) {
  var systemForm = araneaPage().getTraverser().findSurroundingSystemForm(element);
  systemForm['onsubmit'] = function() { return true };
}


var aranea_jsf_rules = {
  'input' : function(el) {
    if (el['type'] == 'submit') {
    	el.onclick = aranea_jsf_input_button_submit(el);
    }
  }
};

Behaviour.register(aranea_jsf_rules);

window['aranea-jsf-behaviour.js'] = true;
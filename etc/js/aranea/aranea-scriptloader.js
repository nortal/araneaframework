/**
 * Copyright 2007 Webmedia Group Ltd.
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

var Aranea = Aranea ? Aranea : {};
Aranea.ScriptLoader = {};

// TODO: what happens when script never loads (closure executed forever?)
Aranea.ScriptLoader.loadLazily = function(scriptFile, scriptToExecute, loadedCondition, executionTryInterval) {
  if (loadedCondition())
    return;

  var head = document.getElementsByTagName("head")[0];
  var scriptFileElement = document.createElement('script');
  scriptFileElement.type = 'text/javascript';
  scriptFileElement.src = scriptFile;
  head.appendChild(script);

  // if the script that should be executed after content of scriptFile is defined,
  // try to execute it when the script has fully loaded
  if (scriptToExecute) {
   var uniqueScriptId = "lazyLoadingExecutor" + Math.round(1000000*Math.random());
    if (araneaPage)
      araneaPage().debug('Adding lazy loading executor ' + uniqueScriptId);

    self[uniqueScriptId] = function () {
      if (loadedCondition()) {
      
      } else {
        setTimeout("self['"+uniqueScriptId+"']()", executionTryInterval);
      }
    }
  }
};

var tinymceloadcondition = function() {
  return window['js/tiny_mce/tiny_mce.js'];
}

var tinyMCEInitScript = function() {
  tinyMCE.init({ 
    editor_selector : 'richTextEditor', 
    mode : 'textareas', 
    theme : 'advanced', 
    theme_advanced_buttons1 : 'bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo,link,unlink,code', 
    theme_advanced_buttons3 : '', 
    theme_advanced_toolbar_location: 'top', 
    theme_advanced_toolbar_align: 'top', 
    theme_advanced_path_location : 'bottom', 
    extended_valid_elements : 'a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]'
  });
}

function ensureScriptIsLoaded() {
   if (window['js/tiny_mce/tiny_mce.js']) {
     return;
   }

   var head = document.getElementsByTagName("head")[0];
   var script = document.createElement('script');
   script.id = 'daaa';
   script.type = 'text/javascript';
   script.src = araneaPage().getServletURL() + '/fileimporter/js/tiny_mce/tiny_mce.js';
   head.appendChild(script);

   var script2 =  document.createElement('script');
   script2.type = 'text/javascript';
   
   var initScript = "tinyMCE.init({ editor_selector : 'richTextEditor' , mode : 'textareas', theme : 'advanced', theme_advanced_buttons1 : 'bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo,link,unlink,code', theme_advanced_buttons3 : '', theme_advanced_toolbar_location: 'top', theme_advanced_toolbar_align: 'top', theme_advanced_path_location : 'bottom', extended_valid_elements : 'a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]'});";
   
   self.tinyMCELazyInitializationFunction = function() {
     if (tinyMCE) {
       eval(initScript);
     } else {
       setTimeout("self.tinyMCELazyInitializationFunction();", 50);
     }
   }  

   head.appendChild(script2);
}

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
 * @since 1.1
 */

var Aranea = Aranea ? Aranea : {};
Aranea.ScriptLoader = {};

// TODO: when requested script never loads and executable script is defined, closure executes forever.
Aranea.ScriptLoader.loadHeadScript = function(options) {
  var scriptFile = options.scriptFile;
  var scriptToExecute = options.scriptToExecute;
  var loadedCondition = options.loadedCondition;
  var executionTryInterval = options.executionTryInterval ? options.executionTryInterval : 10;

  if (loadedCondition())
    return;

  var head = document.getElementsByTagName("head")[0];
  var scriptFileElement = document.createElement('script');
  scriptFileElement.type = 'text/javascript';
  scriptFileElement.src = scriptFile;
  head.appendChild(scriptFileElement);

  // if the script that should be executed after content of scriptFile is defined,
  // try to execute it when the script has fully loaded
  if (scriptToExecute) {
   var uniqueScriptId = "lazyLoadingExecutor" + Math.round(1000000*Math.random());
    if (araneaPage)
      araneaPage().debug('Adding lazy loading executor ' + uniqueScriptId);

    self[uniqueScriptId] = function () {
      if (loadedCondition()) {
        if (araneaPage) araneaPage().debug("Executing " + uniqueScriptId);
        scriptToExecute();
      } else {
        if (araneaPage) araneaPage().debug("Scheduling '" + uniqueScriptId + "' for execution after " + executionTryInterval + "ms.");
        setTimeout("self['"+uniqueScriptId+"']()", executionTryInterval);
      }
    };

    var executableScriptElement = document.createElement('script');
    executableScriptElement.type = 'text/javascript';
    var script = "self['"+uniqueScriptId+"']();";
    if (Prototype.Browser.IE) {
      head.appendChild(executableScriptElement);
      var node = document.createTextNode(script);
      executableScriptElement.appendChild(node);
      //executableScriptElement.text = script;
    } else { 
      var node = document.createTextNode(script);
      executableScriptElement.appendChild(node);
      head.appendChild(executableScriptElement);
    }
  }
};

Aranea.ScriptLoader.createPeriodicalConditionalExecutor = function(f, loadedCondition, interval) {
  var later = function(f) { setTimeout(f, interval); };

  var once = function() {
    if (loadedCondition()) {
      f();
    } else {
      later(once);
    }
  };

  return once;
};

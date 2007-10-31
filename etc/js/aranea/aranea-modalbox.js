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
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */

Aranea.ModalBox = {};
Aranea.ModalBox.ModalBoxFileName = 'js/modalbox/modalbox.js';

Aranea.ModalBox.show = function() {
  var showfunc = function() { 
    Modalbox.show(
      _ap.getSubmitURL(_ap.getSystemForm().araTopServiceId.value, _ap.getSystemForm().araThreadServiceId.value, 'override') + '&araOverlay', 
	      {
	 		overlayClose: false, 
			width: 800
		  }
	  );
  };
  
  showfunc();

  /* TODO: lazyload
  var importname = AraneaPage.getFileImportString(Aranea.ModalBox.ModalBoxFileName);
  var modalboxloaded = function() { return window[Aranea.ModalBox.ModalBoxFileName]; };
  
  Aranea.ScriptLoader.loadHeadScript({
  	scriptFile: importname, 
  	scriptToExecute : null, 
  	loadedCondition : modalboxloaded, 
  	executionTryInterval: 10
  });
  
  var lazyshow = Aranea.ScriptLoader.createPeriodicalConditionalExecutor(showfunc, modalboxloaded, 10);
  lazyshow(); */
};

Aranea.ModalBox.close = function() {
	if (Modalbox)
		Modalbox.hide();
};

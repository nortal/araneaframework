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
Aranea.ModalBox.Options = null;

Aranea.ModalBox.show = function(options) {
  var showfunc = function() {
    Modalbox.show(
      araneaPage().getSubmitURL(araneaPage().getSystemForm().araTopServiceId.value, araneaPage().getSystemForm().araThreadServiceId.value, 'override') + '&araOverlay', 
      options
	);
  };

  showfunc();
};

Aranea.ModalBox.afterLoad = function(content) {
  // if no content is returned, overlay has been closed.
  if (content.startsWith='<!-- araOverlaySpecialResponse -->') {
    var rtext = new Text(content);
    rtext.readLine();
  	var stateId = rtext.readLine();
  	if (stateId) {
  	  stateId = stateId.substring(4, stateId.length-3);
  	}

    AraneaPage.findSystemForm();
    var systemForm = araneaPage().getSystemForm();

    if (systemForm.araTransactionId)
      systemForm.araTransactionId.value = 'inconsistent';

    if (stateId) {
      araneaPage().debug("Overlay closing: updating main system form araClientStateId to '" + stateId + "'.");
      systemForm.araClientStateId.value = stateId;
    }
    return new DefaultAraneaSubmitter().event_4(systemForm);
  }
};

// gets executed after update region response has been processed completely
Aranea.ModalBox.afterUpdateRegionResponseProcessing = function(activeSystemForm) {
  if (activeSystemForm.hasClassName('aranea-overlay') && Modalbox) {
    Modalbox.resizeToContent(Aranea.ModalBox.Options);
  }
};

Aranea.ModalBox.close = function() {
  if (Modalbox) Modalbox.hide();
};

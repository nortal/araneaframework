/*
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
 */

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */

Aranea.ModalBox = Class.create();

Object.extend(Aranea.ModalBox, {

  ModalBoxFileName: 'js/modalbox/modalbox.js',

  Options: null,

  getRequestURL: function() {
    var url = null;
    var form = _ap.getSystemForm();

    if ($$('.aranea-overlay').length) {
      url = form.readAttribute('action') + '?araOverlay=true';
    } else {
      url = _ap.getSubmitURL(form.araTopServiceId.value,
            form.araThreadServiceId.value, 'override') + '&araOverlay=true';
    }

    return url;
  },

  show: function(options) {
      Aranea.ModalBox.Options = { afterLoad: Aranea.ModalBox.afterLoad };
      Object.extend(Aranea.ModalBox.Options, options);

      Modalbox.show(Aranea.ModalBox.getRequestURL(), Aranea.ModalBox.Options);

    if (Prototype.Browser.IE) { //Modalbox does not render well in IE without this line (Prototype bug?):
      $(document.body).viewportOffset();
    }
  },

  isCloseOverlay: function(content) {
    return content && content.startsWith("<!-- araOverlaySpecialResponse -->");
  },

  afterLoad: function(content) {
    var f = function() {
      AraneaPage.findSystemForm();
      _ap.addSystemLoadEvent(AraneaPage.init);
      _ap.onload();

      if (Aranea.ModalBox.isCloseOverlay(content)) {
        Aranea.ModalBox.close();
//      Aranea.ModalBox.reloadPage();
      }
    };
    setTimeout(f);
  },

  // gets executed after update region response has been processed completely
  afterUpdateRegionResponseProcessing: function(activeSystemForm) {
    if (activeSystemForm.hasClassName('aranea-overlay') && Modalbox) {
      Modalbox.resizeToContent(Aranea.ModalBox.Options);
    }
  },

  close: function() {
    if (Modalbox) Modalbox.hide();
  },

  reloadPage: function() {
    AraneaPage.findSystemForm();
    var systemForm = _ap.getSystemForm();

    if (systemForm.araTransactionId) {
      systemForm.araTransactionId.value = 'inconsistent';
    }

    if (window.modalTransport) {
      DefaultAraneaAJAXSubmitter.ResponseHeaderProcessor(window.modalTransport);
      window.modalTransport = null;
    }

    return new DefaultAraneaSubmitter().event_4(systemForm);
  }

});
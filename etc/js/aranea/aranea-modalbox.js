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

Aranea.ModalBox = Class.create({

	ModalBoxFileName: 'js/modalbox/modalbox.js',

	Options: null,

	getRequestURL: function() {
		var url = null;
		var form = araneaPage().getSystemForm();

		if ($$('.aranea-overlay').length) {
			url = form.readAttribute('action') + '?araOverlay=true';
		} else {
			url = araneaPage().getSubmitURL(form.araTopServiceId.value,
			      form.araThreadServiceId.value, 'override') + '&araOverlay=true';
		}

		form = null;
		return url;
	},

	show: function(options) {
	    this.Options = { afterLoad: this.afterLoad };
	    Object.extend(this.Options, options);

	    Modalbox.show(this.getRequestURL(), this.Options);

		if (Prototype.Browser.IE) { //Modalbox does not render well in IE without this line (Prototype bug?):
			$(document.body).viewportOffset();
		}
	},

	isCloseOverlay: function(content) {
		return content && content.startsWith("<!-- araOverlaySpecialResponse -->");
	},

	afterLoad: function(content) {
		AraneaPage.findSystemForm();
		araneaPage().addSystemLoadEvent(AraneaPage.init);
		araneaPage().onload();

		if (_overlay.isCloseOverlay(content)) {
			_overlay.close();
			_overlay.reloadPage();
		}

	},

	// gets executed after update region response has been processed completely
	afterUpdateRegionResponseProcessing: function(activeSystemForm) {
		if (activeSystemForm.hasClassName('aranea-overlay') && Modalbox) {
			Modalbox.resizeToContent(this.Options);
		}
	},

	close: function() {
		if (Modalbox) Modalbox.hide();
	},

	reloadPage: function() {
		AraneaPage.findSystemForm();
		var systemForm = araneaPage().getSystemForm();

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

var _overlay = new Aranea.ModalBox();

function araneaOverlay() {
	return _overlay;
}
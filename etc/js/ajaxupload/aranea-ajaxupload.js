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
 * AJAX file upload functionality.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 */
AraneaPage.ajaxUploadURL = function(element) {
    return _ap.encodeURL(_ap.getServletURL());
}
AraneaPage.ajaxUploadData = function(systemForm, element) {
	return {
		topServiceId: systemForm.araTopServiceId.value,
		araThreadServiceId: systemForm.araThreadServiceId.value,
		araTransactionId: 'override',
		araClientStateId: systemForm.araClientStateId ? systemForm.araClientStateId.value: null,
		araServiceActionPath: element.name,
		araServiceActionHandler: 'fileUpload',
		araSync: 'false'
	};
}
AraneaPage.ajaxUploadInit = function() {
	var form = AraneaPage.findSystemForm();
	$$('input[type=file]:not(.ajax-upload)').each(function(element) {
		new AjaxUpload(element, {
			action: AraneaPage.ajaxUploadURL(element),
			name: element.name,
			data: AraneaPage.ajaxUploadData(form, element),
			autoSubmit: true,
			responseType: false,
			onChange: function(file, extension, options){
				window.status = "File was provided.";
			},
			onSubmit: function(file, extension, options) {
				window.status = "File upload submitting.";
			},
			onComplete: function(file, responseText, options) {
				window.status = "File upload completed. File=" + file + "; response=" + responseText;
				if (responseText == 'OK') {
					$(options.target).hide().insert({after:
						'<a href="#" onclick="$(this).previous().show().next().remove(); return false;">' + file + '</a>'});
				} else {
					alert("File upload failed. File=" + file + "; response=" + responseText);
				}
			}
		});
	});
	form = null;
};
_ap.addSystemLoadEvent(AraneaPage.ajaxUploadInit);
_ap.addClientLoadEvent(AraneaPage.ajaxUploadInit);

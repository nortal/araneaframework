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
Object.extend(Aranea.Page, {
	getAjaxUploadURL: function(element) {
		return _ap.encodeURL(_ap.getServletURL());
	},
	getAjaxUploadFormData: function(systemForm, element) {
		return {
			topServiceId: systemForm.araTopServiceId.value,
			araThreadServiceId: systemForm.araThreadServiceId.value,
			araTransactionId: 'override',
			araClientStateId: systemForm.araClientStateId ? systemForm.araClientStateId.value: null,
			araServiceActionPath: element.name,
			araServiceActionHandler: 'fileUpload',
			araSync: 'false'
		};
	},
	ajaxUploadInit: function() {
		var form = this.findSystemForm();
		var opts = Object.clone(this.AjaxUploadOptions);
		$$('input[type=file].ajax-upload,form#overlaySystemForm input[type=file]').each(function(element) {
			new AjaxUpload(element, Object.extend(opts, {
				name: element ? element.name : '',
				action: AraneaPage.getAjaxUploadURL(element),
				data: AraneaPage.getAjaxUploadFormData(form, element)
			}));
			element.removeClassName('ajax-upload');
		});
		form = null;
	},

	// Default options. Feel free to modify, esp. onComplete:
	AjaxUploadOptions: {
		disabled: false,
		autoSubmit: true,
		onChange: function(file, extension, options) {},
		onSubmit: function(file, extension, options) {},
		onComplete: function(file, responseText, failMsg, options) {
			_ap.debug('File upload completed. File="' + file + '"; response="' + responseText + '".');
			if (responseText == 'OK') {
				// Hides the file input and shows a link instead with the file name. Once the link
				// is clicked, the link will be removed and the file input will be shown again.
				$(options.target).hide().insert({after:
					'<a href="#" onclick="$(this).previous().show().next().remove(); return false;">' + file + '</a>'});
			} else {
				if (!failMsg) {
					failMsg = 'Uploading file "' + file + '" failed. There could have been a '
						+ 'problem\nwith the connection or the file was too big. Please try again!';
				}
				alert(failMsg);
			}
		}
	}
});

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

var Aranea = window.Aranea || {};
Aranea.Page = Aranea.Page || {};

/**
 * AJAX file upload functionality. Integrates with AjaxUpload, and therefore "ajaxupload.js" is required.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2 (introduced), 2.0 (namespace)
 */
Aranea.Page.AjaxUpload = {

	/**
	 * The selector used for fetching file upload inputs where AjaxUpload should be enabled.
	 */
	SELECTOR: 'input[type=file].ajax-upload,#aranea-overlay-form input[type=file]',

	/**
	 * Resolves URL where the file should be submitted for given element. By default, uses the same URL as system form.
	 */
	getURL: function(element) {
		return Aranea.Data.systemForm.readAttribute('action');
	},

	/**
	 * This method provides the input data that will be submitted together with file. The system form and element can
	 * be used to copy values.
	 */
	getFormData: function(systemForm, element) {
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

	/**
	 * The main method that initializes AjaxUpload support for found file upload elements (see
	 * Aranea.Page.AjaxUpload.AJAX_FILE_UPLOAD_SELECTOR). This method should be registered for 'aranea:loaded' and
	 * 'aranea:updated' events. The file upload inputs, where AjaxUpload is registered, are stored in
	 * Aranea.Data.ajaxUploadInputs, and are not processed more than once.
	 */
	init: function() {
		var form = Aranea.Data.systemForm;
		var that = Aranea.Page.AjaxUpload;
		var opts = Object.clone(that.Options);
		var inputs = Aranea.Data.ajaxUploadInputs = Aranea.Data.ajaxUploadInputs || $A();

		$$(that.SELECTOR).without(inputs).each(function(element) {

			// Enables AjaxUpload on the given input:
			new AjaxUpload(element, Object.extend(opts, {
				name: element ? element.name : '',
				action: that.getURL(element),
				data: that.getFormData(form, element)
			}));

			// Remember this element so that we would not register AjaxUpload multiple times on one element:
			inputs.push(element);

			Aranea.Logger.debug('Added AJAX file upload support to ' + element.inspect() + '.');
		});

		form = that = opts = inputs = null;
	},

	/**
	 * Default options suitable for Aranea. Feel free to modify, especially onComplete:
	 */
	Options: {
		disabled: false,
		autoSubmit: true,
		onChange: function(file, extension, options) {},
		onSubmit: function(file, extension, options) {},
		onComplete: function(file, responseText, failMsg, options) {
			Aranea.Logger.debug('File upload completed. File="' + file + '"; response="' + responseText + '".');
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
};
document.observe('aranea:loaded', Aranea.Page.AjaxUpload.init);
document.observe('aranea:updated', Aranea.Page.AjaxUpload.init);

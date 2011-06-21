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

/**
 * Aranea integration scripts with ModalBox (http://okonet.ru/projects/modalbox/) to provide the overlay mode effects.
 * However, one can re-implement some methods (update(), afterUpdateRegionResponseProcessing(), close(),
 * closeWithAjax()) to use another kind of library for rendering overlay effects.
 * 
 * The main functions called are Aranea.ModalBox.show() and Aranea.ModalBox.update(), the rest are called by them. The
 * configuration data is stored in Aranea.ModalBox.Options.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1
 */
Aranea.ModalBox = {

	MODALBOX_CLOSE_MSG: '<!-- araOverlaySpecialResponse -->',

	isOverlaySystemForm: function() {
		var form = Aranea.Data.systemForm;
		return form != null && $(form).match('form#aranea-overlay-form');
	},

	/**
	 * Returns the URL for submitting to in overlay mode.
	 * @since 1.2.1
	 */
	getRequestURL: function() {
		if (Aranea.ModalBox.isOverlaySystemForm()) {
			return Aranea.Data.systemForm.readAttribute('action');
		} else {
			return Aranea.Page.getSubmitURL({ araTransactionId: 'override', araOverlay: true });
		}
	},

	/**
	 * A simple implementation of request with optional callbacks for both success and failure. When no callbacks are
	 * defined, no content will be updated. However, default error reaction is that overlay will be closed.
	 * @param options Options for Ajax.Request and this for this function (options.method).
	 * @param success A function (transport data as parameter) called when AJAX request completes successfully.
	 * @param failure A function (transport data and exception as parameters) called when AJAX request fails.
	 * @since 1.2.1.1
	 */
	doRequest: function(options, success, failure) {
		Aranea.Logger.profile('Aranea.ModalBox.doRequest()');

		new Ajax.Request(this.getRequestURL(), {
			method: options.method,
			parameters: options.params,
			onSuccess: function(transport) {
				// The returned HTML:
				var response = transport.responseText.strip();
				if (success) {
					success(response);
				}
			}.bind(this),

			onException: function(request, exception){
				if (failure) {
					failure(request, exception);
				} else {
					this.close();
					Aranea.Logger.error("Exception has occured while processing Modalbox request.", exception);
					throw('Modal dialog loading error: ' + exception);
				}
				Aranea.Logger.profile('Aranea.ModalBox.doRequest()');
			}.bind(this),

			onComplete: function(transport) {
				Aranea.Data.submitted = false;
				Aranea.Data.loaded = true;
				this.afterLoad(transport);
				Aranea.Logger.profile('Aranea.ModalBox.doRequest()');
			}.bind(this)
		});
	},

	/**
	 * The main function called either to start the overlay mode. Feel free to override this and/or "update" for your
	 * own needs. The parameter should contain settings passed by the OverlayContext.
	 * 
	 * @param params The data from the OverlayContext.
	 */
	show: function(params) {
		Aranea.ModalBox.Options = params;
		new Aranea.Page.Submitter.Overlay().event_plain(); // It takes care of calling Aranea.ModalBox.update() the proper way.
	},

	/**
	 * This function is called by events to update the content of overlay dialog. Parameter contains the data to be used
	 * in the request passed by the OverlayContext. The data will be also stored in Aranea.ModalBox.Options.
	 * 
	 * @param params The data from the OverlayContext.
	 * @since 1.2.1
	 */
	update: function(params) {
		Aranea.ModalBox.doRequest(Object.extend(this.Options, params), function(content) {
			Modalbox.show(content, Object.extend(Aranea.ModalBox.Options, params));
			if (Prototype.Browser.IE) {
				// Modalbox does not render well in IE without this line (Prototype bug?):
				document.viewport.getScrollOffsets();
			}
		});
	},

	/**
	 * Utility method for checking whether the response indicates that the overlay mode should be closed.
	 * 
	 * @param content The full content from the AJAX request response.
	 * @return A Boolean value true when the response content indicates that the overlay mode was closed.
	 * @since 1.2.1
	 */
	isCloseOverlay: function(content) {
		return content && content.startsWith(this.MODALBOX_CLOSE_MSG);
	},

	/**
	 * This method is called each time when the request for overlay content ends. The content can be used to check
	 * whether the overlay mode should be closed. Feel free to override, but be careful to preserve important
	 * checks/tasks. This method must be called after content update.
	 * @param transport The AJAX request transport.
	 */
	afterLoad: function(transport) {
		Aranea.Page.findSystemForm();
		Aranea.Page.Submitter.AJAX.ResponseHeaderProcessor(transport);

		if (this.isCloseOverlay(transport.responseText)) {
			this.closeWithAjaxHandler();
		}
	},

	/**
	 * Gets executed after update region response has been processed completely. Note that without update regions, this
	 * is not called. Invoked by "aranea.js". Customize it for your own needs, especially, if you use another overlay
	 * visual implementation.
	 */
	afterUpdateRegionResponseProcessing: function() {
		if (window.Modalbox && Aranea.ModalBox.isOverlaySystemForm()) {
			Modalbox.resizeToContent(this.Options);
		}
	},

	/**
	 * Closes the overlay mode (only visually, client-side).
	 */
	close: function() {
		if (window.Modalbox) Modalbox.hide();
	},

	/**
	 * Reloads the page. This is usually called when the overlay mode terminates. Reloading will refresh the entire page
	 * content and status.
	 * 
	 * @since 1.2.1
	 */ 
	reloadPage: function() {
		var form = Aranea.Page.findSystemForm();
		if (form.araTransactionId) {
			form.araTransactionId.value = 'inconsistent';
		}
		form = null;

		if (window.modalTransport) {
			Aranea.Page.Submitter.AJAX.ResponseHeaderProcessor(window.modalTransport);
		}

		return Aranea.Page.submit();
	},

	/**
	 * Closes the overlay mode. It is meant to be used, if you don't have a "Close" button, but you still want to close
	 * the overlay in some other way (e.g. with an ESC-key). Then the appropriate event will be called by this method.
	 * Once the overlay is closed, a page reload is also done. It's also recommended that you take a look at
	 * Aranea.ModalBox.closeWithAjaxHandler to customize it, if needed.
	 * 
	 * @param eventId The name of the event handler on the server-side that actually closes the overlay mode.
	 * @param eventTarget The full Id of the widget of the event handler. In JSP, you can use the value of ${widgetId}.
	 * @param eventParam An optional parameter to the event handler.
	 * @since 1.2.1
	 */
	closeWithAjax: function(eventId, eventTarget, eventParam) {
		if (!Aranea.ModalBox.isOverlaySystemForm()) {
			throw("No overlay system form found. You're probably not in overlay mode (yet).");
		} else if (!Aranea.Data.loaded) {
			return;
		}

		Aranea.Data.loaded = false;

		Aranea.Data.systemForm.araWidgetEventPath.value = String.interpret(eventTarget);
		Aranea.Data.systemForm.araWidgetEventHandler.value = String.interpret(eventId);
		Aranea.Data.systemForm.araWidgetEventParameter.value = String.interpret(eventParam);

		new Ajax.Request(Aranea.ModalBox.getRequestURL(), {
				method: Aranea.ModalBox.Options.method,
				parameters: Aranea.Data.systemForm.serialize(true),
				onSuccess: function(transport) {
					if (Aranea.ModalBox.isCloseOverlay(transport.responseText)) {
						window.modalTransport = transport;
						this.closeWithAjaxHandler();
						window.modalTransport = null;
					} else {
						throw("Was expecting the request (event) to close the " +
								"modal dialog, but received non-qualifing response.");
					}
				},
				onFailure: function(transport) {
					Aranea.Data.loaded = true;
					Aranea.Logger.error("Received unexpected error response text:\n" + transport.responseText);
					throw("Was expecting the request (event) to close the " +
							"modal dialog, but received non-qualifing (error) response.");
				},
				onException: function(ajaxRequest, e) {
					Aranea.Data.loaded = true;
					Aranea.Logger.error("Exception has occured while processing or receiving overlay request.", e);
				}
			}
		);
	},

	/**
	 * The handler that is called by Aranea.ModalBox.closeWithAjax() when the server-side event handler has closed the
	 * OverlayContext. You may want to re-implement this method to define how exactly overlay is closed in client-side.
	 * You may also access the AJAX request transport object through window.modalTransport. 
	 */
	closeWithAjaxHandler: function() {
		document.stopObserving('aranea:updated');
		Aranea.ModalBox.close();
		Aranea.ModalBox.reloadPage();
	},

	/**
	 * This method can be called to make the modal dialog update its layout due to changes in its content. This method
	 * should not go server-side, just resize the dialog window and/or update its position. There is no structure
	 * changes monitoring mechanism, the logic that does changes is responsible for calling this method!
	 * 
	 * @since 2.0
	 */
	resize: function() {
		if (window.Modalbox) {
			Modalbox.resizeToContent();
			Modalbox._setPosition();
		}
	}
};

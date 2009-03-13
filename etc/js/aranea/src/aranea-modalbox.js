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
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1
 */
Aranea.ModalBox = {

  /**
   * The script file currently used for overlay popup.
   * Will be removed in future, because it's not used.
   */
  ModalBoxFileName: 'js/modalbox/modalbox.js',

  /**
   * Returns the URL for submitting to in overlay mode.
   * @since 1.2.1
   */
  getRequestURL: function() {
    var form = _ap.getSystemForm();
    var url = null;

    if ($$('.aranea-overlay').length) {
      url = form.readAttribute('action');
    } else {
      url = _ap.getSubmitURL(form.araTopServiceId.value,
            form.araThreadServiceId.value, 'override') + '&araOverlay=true';
    }

    return url;
  },

  /**
   * A simple implementation of request with optional callbacks for both success
   * and failure. When no callbacks are defined, no content will be updated.
   * However, default error reaction is that overlay will be closed.
   * @since 1.2.1.1
   */
  doRequest: function(options, success, failure) {

    new Ajax.Request(this.getRequestURL(), { method: options.method,

      parameters: options.params,

      onSuccess: function(transport) {
        // The returned HTML:
        var response = transport.responseText.strip();

        if (success) {
          success(content);
        }

        // Aranea initialization
        this.afterLoad(transport);

      }.bind(this),

      onException: function(request, exception){
        if (failure) {
          failure(request, excpetion);
        } else {
          this.close();
          throw('Modal dialog loading error: ' + exception);
        }
      }.bind(this)

    });
},

  /**
   * The main function called either to start the overlay mode.
   * Feel free to override this and/or "update" for your own needs.
   */
  show: function(options) {
    this.Options = Object.extend({afterLoad: this.afterLoad}, options);
    this.update(this.Options);
  },

  /**
   * This function is called by events to update the content of overlay dialog.
   * Params contains the data to be used in the request.
   * @since 1.2.1
   */
  update: function(params) {
    Modalbox.show(this.getRequestURL(), Object.extend(this.Options, params));
    if (Prototype.Browser.IE) {
      // Modalbox does not render well in IE without this line (Prototype bug?):
      $(document.body).viewportOffset();
    }
  },

  /**
   * Utility method for checking whether the response indicates that the
   * overlay mode should be closed.
   * @since 1.2.1
   */
  isCloseOverlay: function(content) {
    return content && content.startsWith("<!-- araOverlaySpecialResponse -->");
  },

  /**
   * This method is called each time when the request for overlay content ends.
   * The content can be used to check whether the overlay mode should be closed.
   * Feel free to override, but be careful to preserve important checks/tasks.
   * This method must be called (after content update).
   */
  afterLoad: function(transport) {
    AraneaPage.findSystemForm().writeAttribute("ara-overlay", "true");
    var f = function() {
      if (this.isCloseOverlay(transport.responseText)) {
        this.close();
        this.reloadPage(); 
      } else {
        DefaultAraneaAJAXSubmitter.ResponseHeaderProcessor(transport);
        _ap.addSystemLoadEvent(AraneaPage.init);
        _ap.onload();
        window.modalTransport = null;
      }
    };
    setTimeout(f.bind(Aranea.ModalBox));
  },

  /**
   * Gets executed after update region response has been processed completely.
   * Note that without update regions, this is not called. Invoked by "aranea.js".
   * Customize it for your own needs.
   */
  afterUpdateRegionResponseProcessing: function(activeSystemForm) {
    if (Modalbox && activeSystemForm.hasClassName('aranea-overlay')) {
      Modalbox.resizeToContent(this.Options);
    }
  },

  /**
   * Closes the overlay mode (only visually).
   */
  close: function() {
    if (Modalbox) Modalbox.hide();
  },

  /**
   * Reloads the page. This is usually called when the overlay mode terminates.
   * Reloading will refresh the entire page content and status.
   * @since 1.2.1
   */ 
  reloadPage: function(options) {
    var systemForm = AraneaPage.findSystemForm();

    if (systemForm.araTransactionId) {
      systemForm.araTransactionId.value = 'inconsistent';
    }

    if (window.modalTransport) {
      DefaultAraneaAJAXSubmitter.ResponseHeaderProcessor(window.modalTransport);
      window.modalTransport = null;
    }

    return new DefaultAraneaSubmitter().event_4(systemForm);
  },

  /**
   * Closes the overlay mode. It is meant to be used, if you don't have a "Close"
   * button, but you still want to close the overlay in some other way (e.g. with
   * an ESC-key). This method takes following parameters:
   * @param eventId - the name of the event handler on the server-side that
   *                  actually closes the overlay mode.
   * eventTarget - the full id of the widget that contains the event handler.
   *               In JSP you can use ${widgetId} to retrive the Id.
   * eventParam - An optional parameter to the event handler.
   * The first two parameters are mandatory, the event param is not.
   * Once the overlay is closed, a page reload is also done.
   * @since 1.2.1
   */
  closeWithAjax: function(eventId, eventTarget, eventParam) {
    if (!eventId) {
      throw("eventId must not be null");
    }

    if (!eventTarget) {
      throw("eventTarget must not be null");
    }

    var systemForm = $(_ap.getSystemForm());

    if (!systemForm.hasClassName('aranea-overlay')) {
      throw("No overlay system form found. You're probably not " +
          "in overlay mode (yet).");
    }

    if (!_ap.isLoaded()) {
      return;
    }
    _ap.setLoaded(false);

    systemForm.araWidgetEventPath.value = String.interpret(eventTarget);
    systemForm.araWidgetEventHandler.value = String.interpret(eventId);
    systemForm.araWidgetEventParameter.value = String.interpret(eventParam);

    new Ajax.Request(Aranea.ModalBox.getRequestURL(),
        { method: 'post',
          parameters: systemForm.serialize(true),
          onSuccess: function(transport) {
              if (Aranea.ModalBox.isCloseOverlay(transport.responseText)) {
                window.modalTransport = transport;
                Aranea.ModalBox.close();
                Aranea.ModalBox.reloadPage();
              } else {
                throw("Was expecting the request (event) to close the " +
                    "modal dialog, but received non-qualifing response.");
              }
            },
          onFailure: function(transport) {
            _ap.setLoaded(true);
            _ap.debug("Received unexpected error response text:\n" + transport.responseText);
            throw("Was expecting the request (event) to close the " +
                "modal dialog, but received non-qualifing (error) response.");
          },
          onException: function(AjaxRequest, exc) {
            _ap.setLoaded(true);
            _ap.debug("Exception has occured while processing or receiving overlay request.");
            _ap.debug(exc);
          }
        }
    );
  }
}
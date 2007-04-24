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
 * @author Alar Kvell (alar@araneaframework.org)
 */
var Aranea = {
  regionHandlers: new Hash(),
  loadingMessageId: 'aranea-loading-message',
  loadingMessageContent: 'Loading...',
  loadingMessagePositionHack: false,

  /**
   * Add a handler that is invoked for custom data region in updateregions AJAX
   * request. <code>process</code> function will be invoked on the handler
   * during processing the response. Data specific to this handler will be
   * passed as the first parameter to that function (<code>String</code>).
   */
  addRegionHandler: function(key, handler) {
    this.regionHandlers[key] = handler;
  },

  /**
   * Process response of an updateregions AJAX request. Should be called only
   * on successful response. Invokes region handlers.
   */
  processResponse: function(responseText) {
    var text = new Text(responseText);
    text.readLine(); // responseId
    while (!text.isEmpty()) {
      var key = text.readLine();
      var length = text.readLine();
      var content = text.readCharacters(length);
      if (this.regionHandlers[key]) {
        araneaPage().getLogger().debug('Region type: "' + key + '"');
        this.regionHandlers[key].process(content);
      } else {
        araneaPage().getLogger().error('Region type: "' + key + '" is unknown!');
      }
    }
  },

  /**
   * Exception handler that is invoked on Ajax.Request errors.
   */
  handleRequestException: function(request, exception) {
    throw exception;
  },

  /**
   * Create or show loading message at the top corner of the document. Called
   * before initiating an updateregions Ajax.Request.
   */
  showLoadingMessage: function() {
    var element = $(this.loadingMessageId);
    if (element) {
      if (this.loadingMessagePositionHack) {
        element.style.top = document.documentElement.scrollTop + 'px';
      }
      element.show();
      return;
    }
    var element = Builder.node('div', {id: this.loadingMessageId}, this.loadingMessageContent);
    document.body.appendChild(element);
    if (element.offsetTop) {
      // IE 6 does not support 'position: fixed' CSS attribute value
      this.loadingMessagePositionHack = true;
      element.style.position = 'absolute';
      element.style.top = document.documentElement.scrollTop + 'px';
    }
  },

  /**
   * Hide loading message. Called after the completion of updateregions
   * Ajax.Request.
   */
  hideLoadingMessage: function() {
    var element = $(this.loadingMessageId);
    if (element) {
      element.hide();
    }
  }
};

/**
 * Region handler that updates transaction id of system form.
 */
Aranea.TransactionIdRegionHandler = Class.create();
Aranea.TransactionIdRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(content) {
    var systemForm = araneaPage().getSystemForm();
    if (systemForm.transactionId)
      systemForm.transactionId.value = content;
  }
};
Aranea.addRegionHandler('transactionId', new Aranea.TransactionIdRegionHandler());

/**
 * Region handler that updates DOM element content.
 */
Aranea.DomRegionHandler = Class.create();
Aranea.DomRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(content) {
    var text = new Text(content);
    var length = text.readLine();
    var properties = text.readCharacters(length).evalJSON();
    var id = properties.id;
    var mode = properties.mode;
    var content = text.toString();
    if (mode == 'update') {
      $(id).update(content);
    } else if (mode == 'replace') {
      $(id).replace(content);
    } else {
      araneaPage().getLogger().error('DOM region mode "' + mode + '" is unknown');
    }
    araneaPage().addSystemLoadEvent(AraneaPage.findSystemForm);
  }
};
Aranea.addRegionHandler('dom', new Aranea.DomRegionHandler());

/**
 * Region handler that updates the messages area.
 */
Aranea.MessageRegionHandler = Class.create();
Aranea.MessageRegionHandler.prototype = {
  regionClass: '.aranea-messages',
  regionTypeAttribute: 'arn-msgs-type',
  messageSeparator: '<br/>',

  initialize: function() {
  },

  process: function(content) {
    var messagesByType = content.evalJSON();
    this.updateRegions(messagesByType);
  },

  updateRegions: function(messagesByType) {
    $$(this.regionClass).each((function(region) {
      if (region.hasAttribute(this.regionTypeAttribute)) {
        var type = region.getAttribute(this.regionTypeAttribute);
        if (messagesByType[type]) {
          var messages = messagesByType[type];
          if (messages.size() > 0) {
            this.showMessageRegion(region, messages);
            return;
          }
        }
      } else {
        var messages = messagesByType.values().flatten();
        if (messages.size() > 0) {
          this.showMessageRegion(region, messages);
          return;
        }
      }
      this.hideMessageRegion(region);
    }).bind(this));
  },

  showMessageRegion: function(region, messages) {
    this.findContentElement(region).update(this.buildRegionContent(messages));
    region.show();
    return;
  },

  hideMessageRegion: function(region) {
    region.hide();
    this.findContentElement(region).update();
  },

  findContentElement: function(region) {
    return region;
  },

  buildRegionContent: function(messages) {
    return messages.invoke('escapeHTML').join(this.messageSeparator);
  }
};
Aranea.addRegionHandler('messages', new Aranea.MessageRegionHandler());

/**
 * Region handler that opens popup windows.
 */
Aranea.PopupRegionHandler = Class.create();
Aranea.PopupRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(content) {
    var popups = content.evalJSON();
    this.openPopups(popups);
    araneaPage().addSystemLoadEvent(processPopups);
  },

  openPopups: function(popups) {
    popups.each(function(popup) {
      addPopup(popup.popupId, popup.windowProperties, popup.url);
    });
  }
};
Aranea.addRegionHandler('popups', new Aranea.PopupRegionHandler());

/**
 * Region handler that forces a reload of the page by submitting the system
 * form.
 */
Aranea.ReloadRegionHandler = Class.create();
Aranea.ReloadRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(content) {
    var systemForm = araneaPage().getSystemForm();
    if (systemForm.transactionId)
      systemForm.transactionId.value = 'inconsistent';
    return new DefaultAraneaSubmitter().event_4(araneaPage().getSystemForm());
  }
};
Aranea.addRegionHandler('reload', new Aranea.ReloadRegionHandler());


/* ***************************************************************************
 * UTILITIES
 * ***************************************************************************/

/**
 * A wrapper around String that lets to read text by lines and by chunks of
 * characters.
 */
Text = Class.create();
Text.prototype = {
  initialize: function(text) {
    this.text = text;
  },

  readLine: function() {
    var i = this.text.indexOf("\n");
    var line;
    if (i == -1) {
      line = this.text;
      this.text = '';
    } else {
      line = this.text.substr(0, i);
      this.text = this.text.substr(i + 1);
    }
    return line;
  },

  readCharacters: function(characters) {
    var content = this.text.substr(0, characters);
    this.text = this.text.substr(characters);
    return content;
  },

  isEmpty: function() {
    return this.text.length == 0;
  },

  toString: function() {
    return this.text;
  }
};

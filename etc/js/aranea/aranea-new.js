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

  addRegionHandler: function(key, handler) {
    this.regionHandlers[key] = handler;
  },

  processResponse: function(responseText) {
    var text = new Text(responseText);
    text.readLine(); // responseId
    while (!text.empty()) {
      var key = text.readLine();
      if (this.regionHandlers[key]) {
        araneaPage().getLogger().debug('Region type: "' + key + '"');
        this.regionHandlers[key].process(text);
      } else {
        araneaPage().getLogger().error('Region type: "' + key + '" is unknown!');
        return;
      }
    }
  },

  handleRequestException: function(request, exception) {
    throw exception;
  },

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
      this.loadingMessagePositionHack = true;
      element.style.position = 'absolute';
      element.style.top = document.documentElement.scrollTop + 'px';
    }
  },

  hideLoadingMessage: function() {
    var element = $(this.loadingMessageId);
    if (element) {
      element.hide();
    }
  }
};

Aranea.TransactionIdRegionHandler = Class.create();
Aranea.TransactionIdRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var transactionId = text.readLine();
    var systemForm = araneaPage().getSystemForm();
    if (systemForm.transactionId)
      systemForm.transactionId.value = transactionId;
  }
};
Aranea.addRegionHandler('transactionId', new Aranea.TransactionIdRegionHandler());

Aranea.DomRegionHandler = Class.create();
Aranea.DomRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var id = text.readLine();
    var mode = text.readLine();
    var length = text.readLine();
    var content = text.readBytes(length);
    if (mode == 'update') {
      $(id).update(content);
    } else if (mode == 'replace') {
      $(id).update(content);
    } else {
      araneaPage().getLogger().error('DOM region mode "' + mode + '" is unknown');
    }
    araneaPage().addSystemLoadEvent(AraneaPage.findSystemForm);
  }
};
Aranea.addRegionHandler('dom', new Aranea.DomRegionHandler());

Aranea.MessageRegionHandler = Class.create();
Aranea.MessageRegionHandler.prototype = {
  regionClass: '.aranea-messages',
  regionTypeAttribute: 'arn-msgs-type',
  messageSeparator: '<br/>',

  initialize: function() {
  },

  process: function(text) {
    var messagesByType = text.readBytes(text.readLine()).evalJSON();
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

Aranea.PopupRegionHandler = Class.create();
Aranea.PopupRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var popups = text.readBytes(text.readLine()).evalJSON();
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

Aranea.ReloadRegionHandler = Class.create();
Aranea.ReloadRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var systemForm = araneaPage().getSystemForm();
    if (systemForm.transactionId)
      systemForm.transactionId.value = 'inconsistent';
    return new DefaultAraneaSubmitter().event_4(araneaPage().getSystemForm());
  }
};
Aranea.addRegionHandler('reload', new Aranea.ReloadRegionHandler());


/*
 * Utilities
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

  readBytes: function(bytes) {
    var content = this.text.substr(0, bytes);
    this.text = this.text.substr(bytes);
    return content;
  },

  empty: function() {
    return this.text.length == 0;
  }
};

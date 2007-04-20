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

  addRegionHandler: function(key, handler) {
    this.regionHandlers[key] = handler;
  },

  processResponse: function(responseText) {
    var text = new Text(responseText);
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
  }
};

Aranea.TransactionIdRegionHandler = Class.create();
Aranea.TransactionIdRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var transactionId = text.readLine();
    araneaPage().getSystemForm().transactionId.value = transactionId;
  }
};
Aranea.addRegionHandler('transactionId', new Aranea.TransactionIdRegionHandler());

Aranea.DomRegionHandler = Class.create();
Aranea.DomRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var id = text.readLine();
    var length = text.readLine();
    var content = text.readBytes(length);
    $(id).replace(content);
    araneaPage().addSystemLoadEvent(AraneaPage.findSystemForm);
  }
};
Aranea.addRegionHandler('dom', new Aranea.DomRegionHandler());

Aranea.DocumentRegionHandler = Class.create();
Aranea.DocumentRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var length = text.readLine();
    var content = text.readBytes(length);
    Element.update(document.documentElement, content);
  }
};
Aranea.addRegionHandler('document', new Aranea.DocumentRegionHandler());

Aranea.MessageRegionHandler = Class.create();
Aranea.MessageRegionHandler.prototype = {
  initialize: function() {
  },

  process: function(text) {
    var messagesByType = text.readBytes(text.readLine()).evalJSON();
    this.updateRegions(messagesByType);
  },

  updateRegions: function(messagesByType) {
    $$('.aranea-messages').each((function(region) {
      if (region.hasAttribute('arn-msgs-type')) {
        var type = region.getAttribute('arn-msgs-type');
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
    return messages.invoke('escapeHTML').join('<br/>');
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

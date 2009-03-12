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
 * @since 1.1
 */

Aranea.ContextMenuHTMLBuilder = Class.create();
Object.extend(Aranea.ContextMenuHTMLBuilder, {

  MENU_DIV_ID: 'aranea-contextmenu-div',

  MENU_TEMPLATE: new Template('<ul>#{result}</ul>'),

  ENTRY_TEMPLATE: new Template('<li><a href="javascript:" onclick="#{onclick}">#{label}</a></li>'),

  COMBO_TEMPLATE: new Template('<li class="sub"><a href="javascript:;">#{label}</a><ul>#{subresult}</ul></li>'),

  EVENT_TEMPLATE: new Template('_ap.event_6(_ap.getSystemForm(), \'#{id}\', \'#{target}\', #{param}, null, #{updateRegions}); araneaContextMenu.hide();'),

  ACTION_TEMPLATE: new Template('_ap.action_6(_ap.getSystemForm(), \'#{id}\', \'#{target}\', #{param}, #{actionCallback}, function() {}, null); araneaContextMenu.hide();'),

  createMenuDIV: function() {
    var node = $(Aranea.ContextMenuHTMLBuilder.MENU_DIV_ID);

    if (!node) {
      node = new Element('div', {'id': Aranea.ContextMenuHTMLBuilder.MENU_DIV_ID });
      $(document.body).insert(node);
      node.hide();
    }

    return node;
  },

  buildMenu: function(menu) {
    if (menu.label) {
      var entrytemplate = Aranea.ContextMenuHTMLBuilder.ENTRY_TEMPLATE;
      if (menu.submenu && menu.submenu.length > 0) {
        menu.subresult = "";
        $A(menu.submenu).each(function(entry) {
          menu.subresult = menu.subresult + Aranea.ContextMenuHTMLBuilder.buildMenu(entry);
        });

        return Aranea.ContextMenuHTMLBuilder.COMBO_TEMPLATE.evaluate(menu);
      } else {
        if (menu.type == "action") {
          menu.onclick = Aranea.ContextMenuHTMLBuilder.ACTION_TEMPLATE.evaluate(menu);
        } else if (menu.type == "event") {
          menu.onclick = Aranea.ContextMenuHTMLBuilder.EVENT_TEMPLATE.evaluate(menu);
        }
        return entrytemplate.evaluate(menu);
      }
    }

    var content = "";
    $A(menu.submenu).each(function(entry) {
      content = content + Aranea.ContextMenuHTMLBuilder.buildMenu(entry);
    });
    return Aranea.ContextMenuHTMLBuilder.MENU_TEMPLATE.evaluate({ result : content });
  }

});

Aranea.ContextMenuHolder = Class.create({

  menus: {},

  initialize: function() {},

  addMenu: function(widgetId, menu, options) {
    this.menus[widgetId] = menu;
    var src = new Object();
    src.updateRegions = function() { return null; };
    src.actionCallback = Prototype.emptyFunction;
    this.menus[widgetId].options = Object.extend(src, options || {});
    Aranea.ContextMenuHolder.setMenuOptions(this.menus[widgetId]);
  },

  getMenus: function() {
    return this.menus;
  }
});

Object.extend(Aranea.ContextMenuHolder, {
  setMenuOptions: function(menu) {
    if (menu.submenu) {
      Object.extend(menu.submenu, menu.options);
      $A(menu.submenu).each(function(entry) {
        Object.extend(entry, menu.options);
      });

      Aranea.ContextMenuHolder.setMenuOptions(menu.submenu);
    }
  }
});
Aranea.ContextMenu = Class.create({

  contextMenuHolder: null,

  triggeringElement: null,

  initialize: function(contextMenuHolder) {
    this.contextMenuHolder = contextMenuHolder;
  },

  buildMenu: function(widgetId) {
    var node = new Element('div', {'class': 'aranea-contextmenu-class'});
    menu = this.contextMenuHolder.getMenus()[widgetId];
    node.update(Aranea.ContextMenuHTMLBuilder.buildMenu(menu));
    return node;
  },
  
  acquireWidgetId: function(triggeredElement) {
  	var widgetMarker = AraneaPage.findWidgetMarker(triggeredElement);
  	var widgetId = null;
  	if (widgetMarker)
      widgetId = (widgetMarker.readAttribute('arn-widgetId'));

  	return widgetId;
  },

  show: function(event) {
    if (this.contextMenuHolder.getMenus().length == 0)
      return;

    this.triggeringElement = Event.element(event);
  	var widgetId = this.acquireWidgetId(this.triggeringElement);
    if (!widgetId) {
      this.triggeringElement = null;
      return;
    }

    var r = this.buildMenu(widgetId);
    var x = Event.pointerX(event);
    var y = Event.pointerY(event);

    r.style.top = y+'px';
    r.style.left = x+'px';
    var div = Aranea.ContextMenuHTMLBuilder.createMenuDIV();
    
    div.appendChild(r);
    div.show();
    Event.stop(event);
  },

  hide: function(event) {
  	// if event occurred in context menu itself, menu should be cleared by its own handlers
	var menu = $(Aranea.ContextMenuHTMLBuilder.MENU_DIV_ID);

	if (menu) {
		if (event && $(Event.element(event)).descendantOf(menu)) {
	      return true;
	    }

		var x = $$('div.aranea-contextmenu-class');
	    if (x && x.first()) {
	      menu.removeChild(x.first());
	    }

	    menu.hide();
	}
  },

  getTriggeringElement: function() {
    return this.triggeringElement;
  }

});

var araneaContextMenuHolder = new Aranea.ContextMenuHolder();
var araneaContextMenu = new Aranea.ContextMenu(araneaContextMenuHolder);

Event.observe(document, 'contextmenu', araneaContextMenu.show.bindAsEventListener(araneaContextMenu));
Event.observe(document, 'mousedown', araneaContextMenu.hide.bindAsEventListener(araneaContextMenu));

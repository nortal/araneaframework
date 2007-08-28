/**
 * Copyright 2007 Webmedia Group Ltd.
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
 */

Aranea.ContextMenuHTMLBuilder={};
Aranea.ContextMenuHTMLBuilder.MENU_DIV_ID = 'aranea-contextmenu-div';
Aranea.ContextMenuHTMLBuilder.createMenuDIV = function() {
  var node = $(Aranea.ContextMenuHTMLBuilder.MENU_DIV_ID);
  if (node) return node;

  node = Builder.node('div', {'id':Aranea.ContextMenuHTMLBuilder.MENU_DIV_ID }, '');
  document.body.appendChild(node);
  Element.hide(node);
  return node;
};

Aranea.ContextMenuHTMLBuilder.MENU_TEMPLATE = new Template('<ul>#{result}</ul>');
Aranea.ContextMenuHTMLBuilder.ENTRY_TEMPLATE = new Template('<li><a href="">#{label}</a></li>');
Aranea.ContextMenuHTMLBuilder.COMBO_TEMPLATE = new Template('<li class="sub"><a href="">#{label}</a><ul>#{subresult}</ul></li>');
Aranea.ContextMenuHTMLBuilder.buildMenu = function(menu) {
  if (menu.label) {
    var entrytemplate = menu.template ? menu.template : Aranea.ContextMenuHTMLBuilder.ENTRY_TEMPLATE;
    if (menu.submenu && menu.submenu.length > 0) {
      menu.subresult = "";
      $A(menu.submenu).each(function(entry) {
        menu.subresult = menu.subresult + Aranea.ContextMenuHTMLBuilder.buildMenu(entry);
      });

      return Aranea.ContextMenuHTMLBuilder.COMBO_TEMPLATE.evaluate(menu);
    } else {
      return entrytemplate.evaluate(menu);
    }
  }

  var content = "";
  $A(menu.submenu).each(function(entry) {
    content = content + Aranea.ContextMenuHTMLBuilder.buildMenu(entry);
  });
  return Aranea.ContextMenuHTMLBuilder.MENU_TEMPLATE.evaluate({ result : content });
};

Aranea.ContextMenuHolder = Class.create();
Aranea.ContextMenuHolder.prototype = {
  menus: {},

  initialize: function() {
    //araneaPage().addClientLoadEvent(Aranea.ContextMenuHTMLBuilder.createMenuDIV);
  },

  addMenu: function(widgetId, value) {
    this.menus[widgetId] = value;
  },

  getMenus: function() {
    return this.menus;
  }
};

Aranea.ContextMenu = Class.create();
Aranea.ContextMenu.prototype = {
  contextMenuHolder: null,

  initialize: function(contextMenuHolder) {
    this.contextMenuHolder = contextMenuHolder;
  },

  buildMenu: function(widgetId) {
    var node = Builder.node('div', {'class': 'aranea-contextmenu-class'}, '');
    menu = this.contextMenuHolder.getMenus()[widgetId];
    Element.update(node, Aranea.ContextMenuHTMLBuilder.buildMenu(menu));
    return node;
  },

  show : function(event) {
    var r = this.buildMenu('f1.menu.f0.simpleForm');
    var x = Event.pointerX(event);
    var y = Event.pointerY(event);

    r.style.top = y+'px';
    r.style.left = x+'px';
    var div = Aranea.ContextMenuHTMLBuilder.createMenuDIV();
    div.appendChild(r);
    div.show();
    Event.stop(event);
  },

  hide : function(event) {
    var x = $$('div.aranea-contextmenu-class');
    var z = $(Aranea.ContextMenuHTMLBuilder.MENU_DIV_ID);
    if (x && x.first()) {
      z.removeChild(x.first());
    }
    if (z) z.hide();
  }
};

var araneaContextMenuHolder = new Aranea.ContextMenuHolder();
var araneaContextMenu = new Aranea.ContextMenu(araneaContextMenuHolder);

Event.observe(document, 'contextmenu', araneaContextMenu.show.bindAsEventListener(araneaContextMenu));
Event.observe(document, 'mousedown', araneaContextMenu.hide.bindAsEventListener(araneaContextMenu));

window['aranea-contextmenu.js'] = true;

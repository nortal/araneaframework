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
Aranea.ContextMenu = Class.create({

	contextMenuHolder: null,

	triggeringElement: null,

	initialize: function(contextMenuHolder) {
		this.contextMenuHolder = contextMenuHolder;
	},

	buildMenu: function(widgetId) {
		var node = new Element('div', {'class': 'aranea-contextmenu-class'});
		menu = this.contextMenuHolder.getMenus()[widgetId];
		return node.update(this.buildMenu(menu));
	},
	
	acquireWidgetId: function(triggeredElement) {
		var widgetMarker = Aranea.Page.findWidgetMarker(triggeredElement);
		var widgetId = widgetMarker ? widgetMarker.readAttribute('arn-widgetId') : null;
		return widgetId;
	},

	show: function(event) {
		if (this.contextMenuHolder.getMenus().length == 0) {
			return;
		}

		this.triggeringElement = event.element();
		var widgetId = this.acquireWidgetId(this.triggeringElement);
		if (!widgetId) {
			this.triggeringElement = null;
			return;
		}

		var r = this.buildMenu(widgetId);
		r.setStyle({ left: event.pointerX() + 'px', top: event.pointerY() + 'px' });

		var div = this.createMenuDIV();
		div.appendChild(r);
		div.show();

		event.stop();
	},

	hide: function(event) {
		// if event occurred in context menu itself, menu should be cleared by its own handlers
		var menu = $(this.MENU_DIV_ID);

		if (menu) {
			if (event && event.element().descendantOf(menu)) {
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
Object.extend(Aranea.ContextMenu, {

	HTMLBuilder: {

		MENU_DIV_ID: 'aranea-contextmenu-div',

		MENU_TEMPLATE: new Template('<ul>#{result}</ul>'),

		ENTRY_TEMPLATE: new Template('<li><a href="javascript:" onclick="#{onclick}">#{label}</a></li>'),

		COMBO_TEMPLATE: new Template('<li class="sub"><a href="javascript:;">#{label}</a><ul>#{subresult}</ul></li>'),

		EVENT_TEMPLATE: new Template('Aranea.Page.event(\'#{id}\', \'#{target}\', #{param}, null, #{updateRegions}); araneaContextMenu.hide();'),

		ACTION_TEMPLATE: new Template('Aranea.Page.action(\'#{id}\', \'#{target}\', #{param}, #{actionCallback}, function() {}, null); araneaContextMenu.hide();'),

		createMenuDIV: function() {
			var node = $(this.MENU_DIV_ID);
			if (!node) {
				node = new Element('div', {'id': this.MENU_DIV_ID });
				$(document.body).append(node.hide());
			}
			return node;
		},

		buildMenu: function(menu) {
			if (menu.label) {
				var entrytemplate = this.ENTRY_TEMPLATE;
				if (menu.submenu && menu.submenu.length > 0) {
					menu.subresult = '';
					$A(menu.submenu).each(function(entry) {
						menu.subresult = menu.subresult + this.buildMenu(entry);
					});
					return this.COMBO_TEMPLATE.evaluate(menu);
				} else {
					if (menu.type == 'action') {
						menu.onclick = this.ACTION_TEMPLATE.evaluate(menu);
					} else if (menu.type == 'event') {
						menu.onclick = this.EVENT_TEMPLATE.evaluate(menu);
					}
					return entrytemplate.evaluate(menu);
				}
			}

			var content = '';
			$A(menu.submenu).each(function(entry) {
				content = content + this.buildMenu(entry);
			});
			return this.MENU_TEMPLATE.evaluate({ result : content });
		}
	},

	Holder: Class.create({

		initialize: function() {
			this.menus = {}
		},

		addMenu: function(widgetId, menu, options) {
			var src = {
				updateRegions: function() {
					return null;
				},
				actionCallback: Prototype.emptyFunction
			};
			this.menus[widgetId] = menu;
			this.menus[widgetId].options = Object.extend(src, options || {});
			this.setMenuOptions(this.menus[widgetId]);
		},

		getMenus: function() {
			return this.menus;
		},

		setMenuOptions: function(menu) {
			if (menu.submenu) {
				Object.extend(menu.submenu, menu.options);
				$A(menu.submenu).each(function(entry) {
					Object.extend(entry, menu.options);
				});
				this.setMenuOptions(menu.submenu);
			}
		}
	})
});

Aranea.Data.contextMenuHolder = new Aranea.ContextMenu.Holder();
Aranea.Data.contextMenu = new Aranea.ContextMenu(Aranea.Data.contextMenuHolder);

Event.observe(document, 'contextmenu', Aranea.Data.contextMenu.show.bindAsEventListener(Aranea.Data.contextMenu));
Event.observe(document, 'mousedown', Aranea.Data.contextMenu.hide.bindAsEventListener(Aranea.Data.contextMenu));

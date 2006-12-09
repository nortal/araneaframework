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

if (window.parent.Aranea) {
	window.parent.Aranea.Gwt._document = document;
	araneaPage().addSystemLoadEvent(function() {
		window.parent.document.body.appendChild(
			Builder.node('script', {type: 'text/javascript'}, 'Aranea.Gwt.loadPage();')
		);
	});
	araneaPage().addSystemUnLoadEvent(function() {
		window.parent.document.body.appendChild(
			Builder.node('script', {type: 'text/javascript'}, 'Aranea.Gwt.unloadPage();')
		);
	});
} else {
	araneaPage().addSystemLoadEvent(function() {
		window.parent.document.body.appendChild(
			Builder.node('script', {type: 'text/javascript', src: 'gwt/gwt.js'})
		);
	});
}

function araneaGwtAddModule(widgetId, moduleId) {
	if (window.parent.Aranea) {
		window.parent.document.body.appendChild(
			Builder.node(
				'script',
				{type: 'text/javascript'},
				'Aranea.Gwt.addModule("' + widgetId + '", "' + moduleId + '");'
			)
		);
	}
}

function araneaGwtRenderModule(widgetId, moduleId) {
	if (window.parent.Aranea) {
		window.parent.document.body.appendChild(
			Builder.node(
				'script',
				{type: 'text/javascript'},
				'Aranea.Gwt.renderModule("' + widgetId + '");'
			)
		);
	} else {
		var meta = Builder.node(
			'meta',
			{
				name: 'gwt:module',
				content: widgetId + '=gwt/' + moduleId + '=' + moduleId
			}
		);
		document.body.previousSibling.appendChild(meta);
	}
}

window['aranea-gwt.js'] = true;

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
 * Aranea TreeWidget supporting functions.
 * @author Alar Kvell (alar@araneaframework.org)
 */
var AraneaTree = {
	toggleNode: function(element) {
		return this.action(element, 'toggle', null, null);
	},

	displayAction: function(element, actionId, actionParam) {
		return this.action(element, actionId, 'display', actionParam);
	},

	action: function(element, actionId, scopedActionTarget, actionParam) {
		var treeNode = this.getSurroundingTreeNode(element);
		var tree = this.getSurroundingTree(element);
		var noSync = tree.getAttribute('arn-tree-noSync').toLowerCase() == 'true';
		var fullActionTarget = scopedActionTarget ? treeNode.id + '.' + scopedActionTarget : treeNode.id;
		araneaPage().action(element, actionId, fullActionTarget, actionParam, noSync, this.getUpdateFunction(treeNode.id));
		return false;
	},

	// Returns LI that is Aranea tree node and surrounds given HTML element. 
	getSurroundingTreeNode: function(element) {
		do {
			if (element.tagName && element.tagName.toLowerCase() == 'li' && Element.hasClassName(element, 'aranea-tree-node')) {
				return element;
			}
			element = element.parentNode;
		} while (element);
		return null;
	},

	// Returns UL that is Aranea tree and surrounds given HTML element. 
	getSurroundingTree: function(element) {
		do {
			if (element.tagName && element.tagName.toLowerCase() == 'ul' && Element.hasClassName(element, 'aranea-tree')) {
				return element;
			}
			element = element.parentNode;
		} while (element);
		return null;
	},

	getUpdateFunction: function(treeNodeId) {
		return function(request, response) {
			// TODO handle non-200 responses
			Element.update(treeNodeId, request.responseText);
		};
	}
}

window['aranea-tree.js'] = true;

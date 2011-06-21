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
 * Aranea TreeWidget supporting functions.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
Aranea.Tree = {

	/**
	 * Toggles the state of tree node between expanded and collapsed.
	 *
	 * @param element ID or reference of the TreeNodeWidget or its descendant.
	 * @return false. This is useful in onClick handler of an anchor.
	 */
	toggleNode: function(element) {
		return Aranea.Tree.action(element, 'toggle');
	},

	/**
	 * Calls an action on the display Widget of a TreeNodeWidget. The HTML contents of this TreeNodeWidget are replaced
	 * by the contents of the response (therefore, calling getTreeNodeCtx().render() in the end of the action listener
	 * is advised).
	 *
	 * @param element ID or reference of the TreeNodeWidget or its descendant.
	 * @param actionId Action listener id that handles tree events.
	 * @param actionParam A parameter passed to action listener (optional).
	 * @param onComplete A JavaScript function that is called on the completion of action (optional).
	 * @param options Options passed to Ajax.Request (optional).
	 * @return false. This is useful in onClick handler of an anchor.
	 */
	displayAction: function(element, actionId, actionParam, onComplete, options) {
		return Aranea.Tree.action(element, actionId, 'display', actionParam, onComplete, options);
	},

	/**
	 * Calls an action on the TreeNodeWidget. The HTML contents of this TreeNodeWidget are replaced by the contents of
	 * the response (therefore, calling render() in the end of the action listener is advised).
	 *
	 * @param element ID or reference of the TreeNodeWidget or its descendant.
	 * @param actionId Action listener ID.
	 * @param scopedActionTarget ID of the child widget that the action is sent to (or dot-separated relative path of a
	 *                           deeper descendant of the TreeNodeWidget). If empty, null, or undefined, then action is
	 *                           sent to TreeNodeWidget.
	 * @param actionParam A parameter passed to action listener (optional).
	 * @param onComplete Function that is called on the completion of action (optional).
	 * @param options Options passed to Ajax.Request (optional).
	 * @return false. This is useful in onClick handler of an anchor.
	 */
	action: function(element, actionId, scopedActionTarget, actionParam, onComplete, options) {
		var treeNode = Aranea.Tree.getSurroundingTreeOrNode(element);
		var tree = Aranea.Tree.getSurroundingTree(treeNode);
		var sync = !tree.hasClassName('async');
		var fullActionTarget = scopedActionTarget ? treeNode.id + '.' + scopedActionTarget : treeNode.id;
		var actionCallback = Aranea.Tree.getUpdateFunction(treeNode.id, onComplete);
		Aranea.Page.action(actionId, fullActionTarget, actionParam, null, actionCallback, options, sync, tree);
		return false;
	},

	// Returns LI that is Aranea tree node or UL that is Aranea tree and surrounds given HTML element.
	getSurroundingTreeOrNode: function(element) {
		element = $(element);
		do {
			if (element.hasClassName('aranea-tree-node') || element.hasClassName('aranea-tree')) {
				return element;
			}
			element = element.up();
		} while (element && element.tagName);
		return null;
	},

	// Returns UL that is Aranea tree and surrounds given HTML element. 
	getSurroundingTree: function(element) {
		element = $(element);
		do {
			if (element.hasClassName('aranea-tree')) {
				return element;
			}
			element = element.up();
		} while (element);
	},

	getUpdateFunction: function(treeNodeId, onComplete) {
		return function(request, response) {
			// TODO handle non-200 responses
			var element = $(treeNodeId);
			if (!element) { 
				return;
			} else if (element.hasClassName('aranea-tree')) {
				element.replace(request.responseText);
			} else {
				element.update(request.responseText);
			}
			Aranea.Behaviour.apply();
			if (onComplete) {
				onComplete(request, response);
			}
		};
	}
};

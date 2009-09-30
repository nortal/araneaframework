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
 * Aranea TreeWidget supporting functions.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
var AraneaTree = Class.create({

	/**
	 * Toggles the state of tree node between expanded and collapsed.
	 *
	 * @param element Id or reference of the TreeNodeWidget or its descendant.
	 * @return false. This is useful in onclick handler of an anchor.
	 */
	toggleNode: function(element) {
		return this.action(element, 'toggle');
	},

	/**
	 * Calls an action on the display Widget of a TreeNodeWidget. The HTML contents of this TreeNodeWidget are replaced
	 * by the contents of the response (therefore, calling getTreeNodeCtx().render() in the end of the action listener
	 * is advised).
	 *
	 * @param element Id or reference of the TreeNodeWidget or its descendant.
	 * @param actionId Action listener id that handles tree events.
	 * @param actionParam A parameter passed to action listener (optional).
	 * @param onComplete A JavaScript function that is called on the completion of action (optional).
	 * @param options Options passed to Ajax.Request (optional).
	 * @return false. This is useful in onclick handler of an anchor.
	 */
	displayAction: function(element, actionId, actionParam, onComplete, options) {
		return this.action(element, actionId, 'display', actionParam, onComplete, options);
	},

	/**
	 * Calls an action on the TreeNodeWidget. The HTML contents of this TreeNodeWidget are replaced by the contents of
	 * the response (therefore, calling render() in the end of the action listener is advised).
	 *
	 * @param element Id or reference of the TreeNodeWidget or its descendant.
	 * @param actionId Action listener id.
	 * @param scopedActionTarget Id of the child widget that the action is sent to (or dot-separated relative path of a
	 *                           deeper rdescendant of the TreeNodeWidget). If empty, null, or undefined, then action is
	 *                           sent to TreeNodeWidget.
	 * @param actionParam A parameter passed to action listener (optional).
	 * @param onComplete Function that is called on the completion of action (optional).
	 * @param options Options passed to Ajax.Request (optional).
	 * @return false. This is useful in onclick handler of an anchor.
	 */
	action: function(element, actionId, scopedActionTarget, actionParam, onComplete, options) {
		var treeNode = this.getSurroundingTreeOrNode(element);
		var tree = this.getSurroundingTree(treeNode);
		var sync = !tree.hasAttribute('arn-tree-sync') || tree.readAttribute('arn-tree-sync').toLowerCase() != 'false';
		var fullActionTarget = scopedActionTarget ? treeNode.id + '.' + scopedActionTarget : treeNode.id;
		var actionCallback = this.getUpdateFunction(treeNode.id, onComplete);
		Aranea.Page.action(actionId, fullActionTarget, actionParam, actionCallback, options, sync, tree);
		return false;
	},

	// Returns LI that is Aranea tree node or UL that is Aranea tree and surrounds given HTML element.
	getSurroundingTreeOrNode: function(element) {
		element = $(element);
		do {
			if (Element.hasClassName(element, 'aranea-tree-node') || Element.hasClassName(element, 'aranea-tree')) {
				return element;
			}
			element = element.up();
		} while (element);
		return null;
	},

	// Returns UL that is Aranea tree and surrounds given HTML element. 
	getSurroundingTree: function(element) {
		element = $(element);
		do {
			if (Element.hasClassName(element, 'aranea-tree')) {
				return element;
			}
			element = element.up();
		} while (element);
		return null;
	},

	getUpdateFunction: function(treeNodeId, onComplete) {
		return function(request, response) {
			// TODO handle non-200 responses
			var element = $(treeNodeId);
			var parentElement = element.up();
			if (Element.hasClassName(element, 'aranea-tree')) {
				Element.replace(element, request.responseText);
			} else {
				Element.update(element, request.responseText);
			}
			Aranea.Behaviour.apply();
			if (onComplete) {
				onComplete(request, response);
			}
		};
	}

});

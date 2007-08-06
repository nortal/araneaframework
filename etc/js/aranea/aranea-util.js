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
 * Utility functions.
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */
 
// b/c braindead IE: The NAME attribute cannot be set at run time on elements dynamically 
// created with the createElement method. To create an element with a name attribute, 
// include the attribute and value when using the createElement method.
// http://www.thunderguy.com/semicolon/2005/05/23/setting-the-name-attribute-in-internet-explorer/
function createNamedElement(type, name) {
   var element = null;
   // Try the IE way; this fails on standards-compliant browsers
   try {
      element = document.createElement('<'+type+' name="'+name+'">');
   } catch (e) {
   }
   if (!element || element.nodeName != type.toUpperCase()) {
      // Non-IE browser; use canonical method to create named element
      element = document.createElement(type);
      element.name = name;
   }
   return element;
}

function getElementByIdORName(str) {
  var r = document.getElementById(str);
  if (r)
    return r;
  var elements = document.getElementsByName(str);
  // if more or less than one match, do not return anything
  if (elements.length == 1)
    r = elements[0];
  return r;
}

function setElementAttr(elementStr, attrName, attrValue) {
  var el = getElementByIdORName(elementStr);
  if (el) {
    el.setAttribute(attrName, attrValue);
  }
}

/**
 * A wrapper around String that lets to read text by lines and by chunks of
 * characters.
 *
 * @since 1.1
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

window['aranea-util.js'] = true;

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
 * Utility functions.
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */

/**
 * b/c braindead IE: The NAME attribute cannot be set at run time on elements dynamically
 * created with the createElement method. To create an element with a name attribute,
 * include the attribute and value when using the createElement method.
 * http://www.thunderguy.com/semicolon/2005/05/23/setting-the-name-attribute-in-internet-explorer/
 * 
 * @deprecated since 1.2.1; use Prototype for this task as this function does it now. To be removed in 2.0 
 */
function createNamedElement(type, name) {
   return new Element(type, { 'name': name });
};

function getElementByIdORName(str) {
  var r = $(str);
  if (!r) {
    var elements = $$('[name="' + str + '"]');
    // if more or less than one match, do not return anything
    if (elements.length == 1) {
      r = elements.first();
      elements = null;
    }
  }
  return r;
};

function setElementAttr(elementStr, attrName, attrValue) {
  var el = getElementByIdORName(elementStr);
  if (el) {
    el.writeAttribute(attrName, attrValue);
  }
};

/**
 * A wrapper around String that lets to read text by lines and by chunks of
 * characters.
 *
 * @since 1.1
 */
var Text = Class.create({
  pos: 0,

  initialize: function(text) {
    this.text = text;
  },

  readLine: function() {
    var line = "";
    if (this.pos < this.text.length) {
	    var newpos = this.text.indexOf("\n", this.pos);
	    if (newpos == -1) {
	      line = this.text.substr(this.pos);
	      this.pos = this.text.length;
	    } else {
	      newpos++;
	      line = this.text.substr(this.pos, newpos-this.pos-1);
	      this.pos = newpos;
	    }
    }
    return line;
  },

  readCharacters: function(characters) {
    var content = this.text.substr(this.pos, parseInt(characters));
    this.pos = this.pos + parseInt(characters);
    return content;
  },

  isEmpty: function() {
    return this.pos >= this.text.length;
  },

  toString: function() {
    return this.text.substr(this.pos);
  }
});

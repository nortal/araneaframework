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
 * Behaviour rules required by some example JSP tags.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

var example_rules = {
  'a.first' : function(el) {
  	writeCloningUrl(el);
  },

  'a.last' : function(el) {
  	writeCloningUrl(el);
  },
  
  'a.next' : function(el) {
  	writeCloningUrl(el);
  },
  
  'a.prev' : function(el) {
  	writeCloningUrl(el);
  }
}

Behaviour.register(example_rules);
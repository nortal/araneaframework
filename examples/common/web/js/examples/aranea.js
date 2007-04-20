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
var AraneaExamples = {
};

AraneaExamples.MessageRegionHandler = Class.create();
AraneaExamples.MessageRegionHandler.prototype = Object.extend(new Aranea.MessageRegionHandler(), {
  initialize: function() {
  },

  findContentElement: function(region) {
    return region.down().down().down();
  }
});
Aranea.addRegionHandler('messages', new AraneaExamples.MessageRegionHandler());

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
 */

var AraneaLocale = Class.create({

  _language: null,

  _country: null,

  initialize: function(lang, cntry) {
    this._language = lang;
    this._country = cntry;
  },

  getLanguage: function() {
    return this._language;
  },

  setLanguage: function(lang) {
    this._language = lang;
  },

  getCountry: function() {
    return this._country;
  },

  setCountry: function(cntry) {
    this._country = cntry;
  }

});

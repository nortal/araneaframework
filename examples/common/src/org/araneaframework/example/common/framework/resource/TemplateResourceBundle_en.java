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

package org.araneaframework.example.common.framework.resource;

import org.araneaframework.http.support.IdentityResourceBundle;

import java.util.Locale;
import java.util.ResourceBundle;
import org.araneaframework.http.support.FallbackResourceBundle;
import org.araneaframework.http.support.StringResourceBundle;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class TemplateResourceBundle_en extends FallbackResourceBundle {

  public TemplateResourceBundle_en() throws Exception {
    setLocale(Locale.ENGLISH);

    addResourceBundle(new StringResourceBundle());
    addResourceBundle(ResourceBundle.getBundle("resource/template", getLocale()));
    addResourceBundle(ResourceBundle.getBundle("resource/uilib", getLocale()));
    addResourceBundle(new IdentityResourceBundle());
  }
}

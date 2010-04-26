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

package org.araneaframework.http.support;

import java.util.Enumeration;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;

/**
 * The resolver that can resolve strings starting with the hash (#) symbol. If so, the string following the symbol will
 * be returned. Otherwise returns <code>null</code>. Since Aranea 2.0, the default hash symbol can be overridden through
 * constructor parameter.
 */
public class StringResourceBundle extends ResourceBundle {

  protected String prefix = "#";

  public StringResourceBundle() {}

  public StringResourceBundle(String overrideSymbol) {
    this.prefix = overrideSymbol;
  }

  @Override
  public Enumeration<String> getKeys() {
    return null;
  }

  @Override
  protected Object handleGetObject(String key) {
    return StringUtils.startsWith(key, this.prefix) ? StringUtils.substringAfter(key, this.prefix) : null;
  }
}

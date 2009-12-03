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

import org.apache.commons.logging.LogFactory;

import org.apache.commons.logging.Log;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * The resolver that always returns the provided key. Good to be used in {@link FallbackResourceBundle} as the last step
 * because otherwise an exception is thrown if a resource could not be resolved by the given key. Using an instance of
 * this class in the last step always returns a non-null result (if the key is non-null).
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 2.0
 */
public class IdentityResourceBundle extends ResourceBundle {

  private static final Log LOG = LogFactory.getLog(IdentityResourceBundle.class);

  @Override
  public Enumeration<String> getKeys() {
    return null;
  }

  @Override
  protected Object handleGetObject(String key) {
    if (LOG.isWarnEnabled()) {
      LOG.warn("Message for key '" + key + "'could not be resolved! Returning the key as the value.");
    }
    return "???" + key + "???";
  }
}

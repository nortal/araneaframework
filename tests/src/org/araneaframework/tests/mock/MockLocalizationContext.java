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

package org.araneaframework.tests.mock;

import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang.NotImplementedException;
import org.araneaframework.framework.LocalizationContext;


/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class MockLocalizationContext implements LocalizationContext {

  public Locale getLocale() {
    return Locale.ENGLISH;
  }

  public void setLocale(Locale locale) {
  }

  public ResourceBundle getResourceBundle() {
    return new MockResourceBundle();
  }

  public ResourceBundle getResourceBundle(Locale locale) {
    return new MockResourceBundle();
  }

  public String localize(String key) {
    return key;
  }
  
  public String getMessage(String code, Object... args) {
    //XXX
    throw new NotImplementedException();
  }
  
  public String getMessage(String code, String defaultMessage, Object... args) {
    //XXX
    throw new NotImplementedException();
  }

  public void addLocaleChangeListener(LocaleChangeListener listener) {
	// TODO Auto-generated method stub
	  throw new NotImplementedException();
  }

  public boolean removeLocaleChangeListener(LocaleChangeListener listener) {
	// TODO Auto-generated method stub
	throw new NotImplementedException();
  }
}

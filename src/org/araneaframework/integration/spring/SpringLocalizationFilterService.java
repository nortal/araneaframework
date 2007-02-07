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

package org.araneaframework.integration.spring;

import java.util.Locale;
import java.util.ResourceBundle;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.filter.StandardLocalizationFilterService;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author <a href="mailto:rein@webmedia.ee">Rein Raudjärv</a>
 */
public class SpringLocalizationFilterService extends StandardLocalizationFilterService implements SpringLocalizationContext {
	private static final long serialVersionUID = 1L;
	
	public MessageSource getMessageSource() {
		return (WebApplicationContext) getEnvironment().getEntry(WebApplicationContext.class);
	}
	
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return getMessageSource().getMessage(code, args, defaultMessage, getLocale());
	}
	
	public String getMessage(String code, Object[] args) {
		return getMessageSource().getMessage(code, args, getLocale());
	}
	
	protected Environment getChildEnvironment() {
		return new StandardEnvironment(super.getChildEnvironment(), SpringLocalizationContext.class, this);
	}
	
	/* Resource Bundle Localization Context */
	public ResourceBundle getResourceBundle(Locale locale) {
		return new MessageSourceResourceBundle(getMessageSource(), locale);
	}
	
	public String localize(String key) {
		return getMessage(key, null);
	}
}

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

package org.araneaframework.integration.spring;

import java.util.Locale;
import java.util.ResourceBundle;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.filter.StandardLocalizationFilterService;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.web.context.WebApplicationContext;

/**
 * Provides the {@link SpringLocalizationContext} and
 * {@link LocalizationContext} implementation that uses the
 * {@link MessageSource} of the Spring framework. It is mainly targeted for
 * projects that also use the Spring framework IoC container, and want to let
 * the latter handle message context configuration. This service, however, helps
 * to use the same message context in Aranea.
 * <p>
 * This filter service is accessible from the <code>Environment</code> by key
 * <code>SpringLocalizationContext.class</code>.
 * <p>
 * To enable this service instead of the
 * {@link StandardLocalizationFilterService}, one must define (to override the
 * default solution) it in <code>aranea-conf.xml</code>:
 * 
 * <pre><code>
 *   &lt;bean id=&quot;araneaLocalizationFilter&quot; singleton=&quot;false&quot;
 *       class=&quot;org.araneaframework.integration.spring.SpringLocalizationFilterService&quot;&gt;
 *     &lt;property name=&quot;languageName&quot; value=&quot;de&quot; /&gt;
 *   &lt;/bean&gt;
 * </code></pre>
 * 
 * @author <a href="mailto:rein@webmedia.ee">Rein Raudjärv</a>
 */
public class SpringLocalizationFilterService extends StandardLocalizationFilterService implements SpringLocalizationContext {
	private static final long serialVersionUID = 1L;
	
	public MessageSource getMessageSource() {
		return getEnvironment().getEntry(WebApplicationContext.class);
	}
	
	@Override
  public String getMessage(String code, String defaultMessage, Object... args) {
		return getMessageSource().getMessage(code, args, defaultMessage, getLocale());
	}
	
	@Override
  public String getMessage(String code, Object... args) {
		return getMessageSource().getMessage(code, args, getLocale());
	}
	
	@Override
  protected Environment getChildEnvironment() {
		return new StandardEnvironment(super.getChildEnvironment(), SpringLocalizationContext.class, this);
	}
	
	/* Resource Bundle Localization Context */
	@Override
  public ResourceBundle getResourceBundle(Locale locale) {
		return new MessageSourceResourceBundle(getMessageSource(), locale);
	}
	
	@Override
  public String localize(String key) {
		return getMessage(key);
	}
}

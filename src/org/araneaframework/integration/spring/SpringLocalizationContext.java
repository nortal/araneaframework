package org.araneaframework.integration.spring;

import org.araneaframework.framework.LocalizationContext;
import org.springframework.context.MessageSource;

/**
 * @author <a href="mailto:rein@webmedia.ee">Rein Raudjärv</a>
 */
public interface SpringLocalizationContext extends LocalizationContext {
	public MessageSource getMessageSource();
}

package org.araneaframework.ioc.spring;

import org.araneaframework.framework.LocalizationContext;
import org.springframework.context.MessageSource;

/**
 * @author <a href="mailto:rein@webmedia.ee>Rein Raudjärv</a>
 */
public interface SpringLocalizationContext extends LocalizationContext {
	public MessageSource getMessageSource();
	
	String getMessage(String code, Object[] args, String defaultMessage);
	String getMessage(String code, Object[] args);
}

package org.araneaframework.ioc.spring;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * Spring MessageSource-based ResourceBundle.
 * 
 * @author <a href="mailto:rein@webmedia.ee>Rein Raudjärv</a>
 * 
 * @see org.springframework.context.MessageSource
 */
public class MessageSourceAdapterResourceBundle extends ResourceBundle {

	private MessageSource messageSource;

	private Locale locale;

	public MessageSourceAdapterResourceBundle(MessageSource messageSource,
			Locale locale) {
		this.messageSource = messageSource;
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	protected Object handleGetObject(String code) {
		try {
			return messageSource.getMessage(code, null, getLocale());
		} catch (NoSuchMessageException e) {
			return null;
		}
	}

	public Enumeration getKeys() {
		return null;
	}
}

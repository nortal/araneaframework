package org.araneaframework.servlet.support;

import java.util.Locale;
import java.util.ResourceBundle;

public class DefaultResourceBundle extends FallbackResourceBundle {
	
	public DefaultResourceBundle() throws Exception {
		setLocale(Locale.ENGLISH);

		addResourceBundle(new StringResourceBundle());
		addResourceBundle(ResourceBundle.getBundle("resource/uilib", getLocale()));
	}
}

package org.araneaframework.ioc.spring;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.filter.StandardLocalizationFilterService;
import org.springframework.context.MessageSource;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author <a href="mailto:rein@webmedia.ee>Rein Raudjärv</a>
 */
public class SpringLocalizationFilterService extends StandardLocalizationFilterService implements SpringLocalizationContext {
	
	private static final long serialVersionUID = 1L;
	
	public static final String SPRING_LOCALIZATION_CONTEXT_KEY = "ee.pria.web.framework.filter.SpringLocalizationFilterService";
	
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
		Map entries = new HashMap();
		entries.put(SpringLocalizationContext.class, this);
		
		return new StandardEnvironment(super.getChildEnvironment(), entries);
	}
	
	protected void action(Path path, InputData input, OutputData output) throws Exception {
		output.pushAttribute(SPRING_LOCALIZATION_CONTEXT_KEY, this);
		
		try {
			super.action(path, input, output);
		}
		finally {
			output.popAttribute(SPRING_LOCALIZATION_CONTEXT_KEY);
		}
	}
	
	/* Resource Bundle Localization Context */
	
	public ResourceBundle getResourceBundle(Locale locale) {
		return new MessageSourceAdapterResourceBundle(getMessageSource(), locale);
	}
	
	public String localize(String key) {
		return getMessage(key, null);
	}
}

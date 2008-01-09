package org.araneaframework.uilib.form;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.set.ListOrderedSet;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class LocalFormElementValidationErrorRenderer implements FormElementValidationErrorRenderer {
	public static final LocalFormElementValidationErrorRenderer INSTANCE = new LocalFormElementValidationErrorRenderer();
	
	public void addError(GenericFormElement element, String error) {
		Set c = (Set) element.getProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY);
		if (c == null) {
			c = ListOrderedSet.decorate(new HashSet());
			element.setProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY, c);
		}

		c.add(error);
	}

	public void clearErrors(GenericFormElement element) {
		element.setProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY, null);
	}
}

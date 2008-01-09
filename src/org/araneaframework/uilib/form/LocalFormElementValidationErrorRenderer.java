package org.araneaframework.uilib.form;

import org.araneaframework.framework.MessageContext;
import org.araneaframework.http.util.EnvironmentUtil;

public class LocalFormElementValidationErrorRenderer implements FormElementValidationErrorRenderer {
	public static final LocalFormElementValidationErrorRenderer INSTANCE = new LocalFormElementValidationErrorRenderer();
	
	public void addError(GenericFormElement element, String error) {
		MessageContext messageCtx = EnvironmentUtil.requireMessageContext(element.getEnvironment());
		messageCtx.showMessage(element.getScope().toString(), error);
	}

	public void clearErrors(GenericFormElement element) {
		MessageContext messageCtx = EnvironmentUtil.requireMessageContext(element.getEnvironment());
		messageCtx.hideMessages(element.getScope().toString(), element.getErrors());
	}
}

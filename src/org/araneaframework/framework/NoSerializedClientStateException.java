package org.araneaframework.framework;

import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.http.filter.StandardClientStateFilterWidget;

/**
 * @deprecated Together with {@link StandardClientStateFilterWidget}
 */
public class NoSerializedClientStateException extends AraneaRuntimeException {
	public NoSerializedClientStateException() {
		super("No data to deserialize present");
	}
}

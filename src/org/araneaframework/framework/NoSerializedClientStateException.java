package org.araneaframework.framework;

import org.araneaframework.core.AraneaRuntimeException;

public class NoSerializedClientStateException extends AraneaRuntimeException {
	public NoSerializedClientStateException() {
		super("No data to deserialize present");
	}
}

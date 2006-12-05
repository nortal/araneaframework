package org.araneaframework.integration.jsf.core;

import java.io.IOException;
import javax.faces.application.StateManager;
import javax.faces.application.StateManagerWrapper;
import javax.faces.context.FacesContext;

public class AraneaJsfStateManagerWrapper extends StateManagerWrapper {
	private StateManager stateManager;
	
	public AraneaJsfStateManagerWrapper(StateManager wrapped) {
		stateManager = wrapped;
	}

	protected StateManager getWrapped() {
		return stateManager;
	}

	public void writeState(FacesContext context, Object state) throws IOException {
		super.writeState(context, state);
	}

	public void writeState(FacesContext context, SerializedView state) throws IOException {
		super.writeState(context, state);
	}
}

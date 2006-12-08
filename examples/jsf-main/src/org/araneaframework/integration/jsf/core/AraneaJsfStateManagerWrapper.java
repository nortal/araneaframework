package org.araneaframework.integration.jsf.core;

import java.io.IOException;
import javax.faces.application.StateManager;
import javax.faces.application.StateManagerWrapper;
import javax.faces.component.UIViewRoot;
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

	public Object getComponentStateToSave(FacesContext context) {
		return super.getComponentStateToSave(context);
	}

	public Object getTreeStructureToSave(FacesContext context) {
		return super.getTreeStructureToSave(context);
	}

	public boolean isSavingStateInClient(FacesContext context) {
		return super.isSavingStateInClient(context);
	}

	public void restoreComponentState(FacesContext context, UIViewRoot viewRoot, String renderKitId) {
		super.restoreComponentState(context, viewRoot, renderKitId);
	}

	public UIViewRoot restoreTreeStructure(FacesContext context, String viewId, String renderKitId) {
		return super.restoreTreeStructure(context, viewId, renderKitId);
	}

	public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {
		return super.restoreView(context, viewId, renderKitId);
	}

	public SerializedView saveSerializedView(FacesContext context) {
		return super.saveSerializedView(context);
	}

	public Object saveView(FacesContext context) {
		return super.saveView(context);
	}
}

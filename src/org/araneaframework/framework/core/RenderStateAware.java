package org.araneaframework.framework.core;

import java.io.Serializable;

/**
 * Interface to be implemented by the components that wish to be aware of 
 * their render state (whether they were actually rendered to the response or not).
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface RenderStateAware extends Serializable {
	boolean isRendered();
	void setRendered(boolean rendered);
}

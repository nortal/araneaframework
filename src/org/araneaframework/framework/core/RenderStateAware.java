package org.araneaframework.framework.core;

import java.io.Serializable;

/**
 * Interface to be implemented by the components that wish to be aware of 
 * their render state (whether they were actually rendered to the response or not).
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface RenderStateAware extends Serializable {
	/**
	 * Returns the render state of implementing class(component).
	 * @return render state of implementing class(component)
	 */
	boolean isRendered();

	/**
	 * Only use when you really know what you are doing.
	 * @param rendered
	 */
	void _setRendered(boolean rendered);
}

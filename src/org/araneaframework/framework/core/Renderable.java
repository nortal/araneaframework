package org.araneaframework.framework.core;

import java.io.Serializable;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface Renderable extends Serializable {
	void rendered();
	void notRendered();
}

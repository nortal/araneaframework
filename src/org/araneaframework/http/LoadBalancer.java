package org.araneaframework.http;

/**
 * Interface for 'load balancer'.
 * @author Taimo Peelo (taimo@ut.ee)
 */
public interface LoadBalancer {
	/** return the host that should service this request */
	public String getServingHost();
}

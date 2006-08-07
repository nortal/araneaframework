package org.araneaframework.uilib.list.util.like;

/**
 * Base implementation of {@link AnyStringWildcardHandler}.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see AnyStringWildcardHandler
 */
public abstract class BaseAnyStringWildcardHandler implements AnyStringWildcardHandler {
	
	protected Boolean startsWith;
	protected Boolean endsWith;

	public void setStartsWith(boolean startsWith) {
		this.startsWith = new Boolean(startsWith);
	}

	public void setEndsWith(boolean endsWith) {
		this.endsWith = new Boolean(endsWith);
	}

}

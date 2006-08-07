package org.araneaframework.uilib.list.util.like;

/**
 * SQL Like implementation of {@link AnyStringWildcardHandler}.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see AnyStringWildcardHandler
 */
public class DefaultAnyStringWildcardHandler extends BaseAnyStringWildcardHandler {

	public boolean shouldStartWith() {
		return this.startsWith.booleanValue();
	}

	public boolean shouldEndWith() {
		return this.endsWith.booleanValue();
	}

	public AnyStringWildcardHandler newInstance() {
		return new DefaultAnyStringWildcardHandler();
	}	

}

package org.araneaframework.uilib.list.util.like;

/**
 * Automatic implementation of {@link AnyStringWildcardHandler}.
 * 
 * Wildcard is added at the start if there was no wildcard at the end
 *   and it is added at the end if there was no wildcard at the start.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see AnyStringWildcardHandler
 */
public class AutomaticAnyStringWildcardHandler extends BaseAnyStringWildcardHandler {

	public boolean shouldStartWith() {
		return this.startsWith.booleanValue() || !this.endsWith.booleanValue();
	}

	public boolean shouldEndWith() {
		return this.endsWith.booleanValue() || !this.startsWith.booleanValue();
	}

	public AnyStringWildcardHandler newInstance() {
		return new AutomaticAnyStringWildcardHandler();
	}

}

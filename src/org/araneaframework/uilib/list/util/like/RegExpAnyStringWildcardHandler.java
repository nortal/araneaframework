package org.araneaframework.uilib.list.util.like;

/**
 * Regular expressions implementation of {@link AnyStringWildcardHandler}.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see AnyStringWildcardHandler
 */
public class RegExpAnyStringWildcardHandler extends BaseAnyStringWildcardHandler {

	public boolean shouldStartWith() {
		return true;
	}

	public boolean shouldEndWith() {
		return true;
	}

	public AnyStringWildcardHandler newInstance() {
		return new RegExpAnyStringWildcardHandler();
	}

}

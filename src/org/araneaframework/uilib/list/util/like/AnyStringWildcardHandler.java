package org.araneaframework.uilib.list.util.like;

/**
 * Any string wildcard Like pattern handler.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public interface AnyStringWildcardHandler {
	
	/**
	 * Must be set as the Like pattern starts with any string wildcard or not.
	 * 
	 * @param startsWith Like pattern starts with any string wildcard or not.
	 */
	void setStartsWith(boolean startsWith);
	
	/**
	 * Must be set as the Like pattern emds with any string wildcard or not.
	 * 
	 * @param startsWith Like pattern ends with any string wildcard or not.
	 */
	void setEndsWith(boolean endsWith);
	
	/**
	 * Returns whether the Like pattern should start with any string wildcard.
	 * 
	 * @return whether the Like pattern should start with any string wildcard.
	 */
	boolean shouldStartWith();
	
	/**
	 * Returns whether the Like pattern should end with any string wildcard.
	 * 
	 * @return whether the Like pattern should end with any string wildcard.
	 */
	boolean shouldEndWith();
	
	/**
	 * Returns new instance of the same class.
	 * 
	 * @return new instance of the same class.
	 */
	AnyStringWildcardHandler newInstance();
	
}

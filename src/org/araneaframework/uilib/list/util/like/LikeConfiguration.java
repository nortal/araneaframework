package org.araneaframework.uilib.list.util.like;

/**
 * Like filter configuration. 
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class LikeConfiguration {
	
	public static final String[] DEFUALT_ANY_STRING_WILDCARDS = {"*","%"};
	public static final String[] DEFUALT_ANY_CHAR_WILDCARDS = {".","_","?"};
	public static final AnyStringWildcardHandler DEFAULT_HANDLER = new AutomaticAnyStringWildcardHandler();
	
	private String[] anyStringWildcards = DEFUALT_ANY_STRING_WILDCARDS;
	private String[] anyCharWildcards = DEFUALT_ANY_CHAR_WILDCARDS;
	private AnyStringWildcardHandler anyStringWildcardHandler = DEFAULT_HANDLER;
	
	public String[] getAnyCharWildcards() {
		return anyCharWildcards;
	}
	public void setAnyCharWildcards(String[] anyCharWildcards) {
		this.anyCharWildcards = anyCharWildcards;
	}
	public AnyStringWildcardHandler createAnyStringWildcardHandler() {
		if (this.anyStringWildcardHandler == null) {
			throw new IllegalStateException("anyStringWildcardHandler must be set first");
		}
		return anyStringWildcardHandler.newInstance();
	}
	public void setAnyStringWildcardHandler(
			AnyStringWildcardHandler anyStringWildcardHandler) {
		this.anyStringWildcardHandler = anyStringWildcardHandler;
	}
	public String[] getAnyStringWildcards() {
		return anyStringWildcards;
	}
	public void setAnyStringWildcards(String[] anyStringWildcards) {
		this.anyStringWildcards = anyStringWildcards;
	}
}

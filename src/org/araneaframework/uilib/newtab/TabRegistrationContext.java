package org.araneaframework.uilib.newtab;

public interface TabRegistrationContext {
	/** Invoked by {@link TabWidget} when it is initialized. */
	TabWidget registerTab(TabWidget tabWidget);

	/** Invoked by {@link TabWidget} when it is destroyed. */
	TabWidget unregisterTab(TabWidget tabWidget);
}

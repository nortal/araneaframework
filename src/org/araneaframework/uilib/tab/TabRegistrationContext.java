package org.araneaframework.uilib.tab;

public interface TabRegistrationContext {
	/** Invoked by {@link TabWidget} when it is initialized. */
	TabWidget registerTab(TabWidget tabWidget);

	/** Invoked by {@link TabWidget} when it is destroyed. */
	TabWidget unregisterTab(TabWidget tabWidget);
}

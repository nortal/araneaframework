package org.araneaframework.core;

import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.framework.core.BaseFilterService;

public class StandardNonInitializableRelocatableService extends BaseFilterService {
	public StandardNonInitializableRelocatableService(RelocatableService child) {
		super(child);
	}

	protected void init() throws Exception {
		((RelocatableService) childService)._getRelocatable().overrideEnvironment(getEnvironment());
	}
}

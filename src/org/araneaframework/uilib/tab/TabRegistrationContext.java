/*
 * Copyright 2006-2007 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.uilib.tab;

import java.io.Serializable;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface TabRegistrationContext extends Serializable {
	/** Invoked by {@link TabWidget} when it is initialized. */
	TabWidget registerTab(TabWidget tabWidget);

	/** Invoked by {@link TabWidget} when it is destroyed. */
	TabWidget unregisterTab(TabWidget tabWidget);
}

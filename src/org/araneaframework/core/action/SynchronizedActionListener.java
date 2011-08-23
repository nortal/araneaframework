/*
 * Copyright 2006 Webmedia Group Ltd.
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

package org.araneaframework.core.action;

/**
 * If an action wants to be processed in synchronized manner in the context of a non-session-based service, this
 * interface declares that the action listener is synchronized. Note that synchronized actions will block *all* incoming
 * requests until it completes.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public interface SynchronizedActionListener extends ActionListener {}

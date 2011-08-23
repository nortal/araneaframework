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

/**
 * Contains base/standard/default implementations of contracts defined in <tt>org.araneaframwork</tt> package. In
 * addition, defines some additional contracts to enrich Aranea component request handling methods, namely events and
 * actions.
 * <p>
 * For better overview, let's categorize the main classes:
 * <ul>
 * <li><b>implementations for component/service/widget contracts</b>: <tt>BaseComponent</tt>, <tt>BaseService</tt>,
 * <tt>BaseWidget</tt>;
 * <li><b>application-level contracts for component/service/widget</b>: items with <tt>Application*</tt> prefix;
 * <li><b>application-level contract implementations</b>: items with <tt>BaseApplication*</tt> prefix;
 * <li><b>basic abstract classes</b>: <tt>BaseEnvironment</tt>;
 * <li><b>standard implementation classes</b>: items with <tt>Standard*</tt> prefix;
 * <li><b>Factory contracts</b>: <tt>ComponentFactory</tt>, <tt>ServiceFactory</tt>, <tt>WidgetFactory</tt>.
 * </ul>
 * <p>
 * For more information about these contracts/implementations, please see the corresponding JavaDoc.
 * <h3>Notes:</h3>
 * <ul>
 * <li>This package does not contain implementations to all interfaces defined in <tt>org.araneaframework</tt> package,
 * meaning those that are related to requests and responses. They are implemented in the
 * <tt>org.araneaframework.framework</tt> package.
 * <li>Component behaviour/architecture contracts defined in package <tt>org.araneaframework</tt> are used in
 * <tt>BaseApplication*</tt> classes.
 * </ul>
 * <p>
 * <b>This package should be as minimal as possible and shouldn't depend on anything unless absolutely necessary.</b>
 */

package org.araneaframework.core;


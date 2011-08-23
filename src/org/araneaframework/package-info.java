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
 * The most important contract interfaces for Aranea are defined here &mdash; components, services, widgets, environment
 * and more. These are used to build up the framework.
 * <p>
 * For better overview, let's categorize them as following:
 * <ul>
 *  <li><b>Component contracts</b>: <tt>Component</tt>, <tt>Service</tt>, <tt>Widget</tt>;
 *  <li><b>Component behaviour/architecture contracts</b>: <tt>Composite</tt>, <tt>Relocatable</tt>,
 *  <tt>Viewable</tt>;
 *  <li><b>Component location identification contracts</b>: <tt>Path</tt>, <tt>Scope</tt>;
 *  <li><b>Interaction contracts</b>: <tt>Environment</tt>, (<tt>EnvironmentAwareCallback</tt>), <tt>Message</tt>;
 *  <li><b>Request-response contracts</b>: <tt>InputData</tt>, <tt>OutputData</tt>, <tt>Extendable</tt>,
 *  <tt>Narrowable</tt>.
 * </ul>
 * For more information about these contracts, please see JavaDoc of corresponding interfaces.
 * <p>
 * Most of these contracts have their base implementations in <tt>org.araneframework.core</tt> package, except for
 * request-response contracts which have their implementations in <tt>org.araneframework.framework</tt> package.
 */

package org.araneaframework;


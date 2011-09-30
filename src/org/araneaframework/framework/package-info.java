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
 * Interfaces and implementations that make up the executable Aranea framework (filters, routers, etc). Important aspect
 * here is that they do not depend on any protocol, and instead use {@link org.araneaframework.InputData} and
 * {@link org.araneaframework.OutputData}.
 * <p>
 * Obviously, the most important part of this package are formed by the contexts (interfaces) defined here, since they
 * are used by application developers to trigger built-in services offered by Aranea framework. Probably the most used
 * context here is <tt>FlowContext</tt> to manage flow transitions.
 * <p>
 * However, from the framework's perspective, a fundamental context <tt>ManagedServiceContext</tt> which indicates a
 * context, which maintains registered services, and decides where to forward requests. Framework users usally interacts
 * with following managed service context types:
 * <ul>
 * <li><tt>TopServiceContext</tt> - a global/root stateless service context;
 * <li><tt>SessionServiceContext</tt> - an application-based context of session services;
 * <li><tt>ThreadContext</tt> - a session-based context of application threads.
 * </ul>
 * Note that managed service context also supports specifying a time-to-live value for child-services, but it is not
 * supported at <tt>SessionServiceContext</tt> level. Use <tt>ExpiringServiceContext</tt> to test whether a retrieved
 * context instance supports services with limited life-time. You can find implementations for service contexts in
 * package <tt>org.araneaframework.framework.router</tt>.
 * <p>
 * Then there are interfaces <tt>FilterService</tt> and <tt>FilterWidget</tt>, which have baser classes in
 * <tt>org.araneaframework.framework.core</tt> package. A myriad of filters exist to simplify common issues.
 * <p>
 * Let's also take a quick tour on some other useful contexts:
 * <ul>
 * <li><tt>ExceptionHandlerFactory</tt> - a factory that is asked to create a service when a request fails with an
 * exception, the factory can be specified on
 * {@link org.araneaframework.framework.filter.StandardCriticalExceptionHandlingFilterService}.
 * <li><tt>LocalizationContext</tt> - the one-stop central place for localizing.
 * <li><tt>MessageContext</tt> - a context for gathering different types of messages generated during request.
 * <li><tt>TransactionContext</tt> - although not used directly, filters out requests that do not have correct
 * transaction ID.
 * <li><tt>SystemFormContext</tt> - context for registering hidden fields in a system form.
 * <li><tt>MountContext</tt> - context for registering callbacks for custom request paths.
 * </ul>
 */

package org.araneaframework.framework;


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

package org.araneaframework.framework.core;

/**
 * A filtering widget that performs no filtering (<i>no-operation</i>). It can be used in the configuration (
 * <code>aranea-conf.xml</code>) to disable Aranea filter-widget that is enabled by default.
 * <p>
 * For example, to disable the transaction filter, insert the following line to <code>aranea-conf.xml</code>:
 * 
 * <pre>
 * &lt;bean class=&quot;org.araneaframework.framework.core.NopFilterWidget&quot;
 *   id=&quot;araneaTransactionFilter&quot; singleton=&quot;false&quot;/&gt;
 * </pre>
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class NopFilterWidget extends BaseFilterWidget {
}

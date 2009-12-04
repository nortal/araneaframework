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
 * The No-Operation Widget that can be used in the configuration (<code>aranea-conf.xml</code>)
 * to disable a Aranea widget that is enabled by default.
 * <p>
 * For example, to disable the transaction filter, insert the following line to
 * <code>aranea-conf.xml</code>: <br>
 * <code>
 * &lt;bean class=&quot;org.araneaframework.framework.core.NopFilterWidget&quot;
 * id=&quot;araneaTransactionFilter&quot; singleton=&quot;false&quot;/&gt;
 * </code>
 */
public class NopFilterWidget extends BaseFilterWidget {
}

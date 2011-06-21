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

package org.araneaframework.uilib.form;

import java.io.Serializable;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.framework.MessageContext.MessageData;

public interface FormElementContext<C,D> extends Serializable {

	// TODO: isInitialized() ?

	public boolean isValid();

	public void addError(String error, Object... params);

	public void addErrors(Set<MessageData> errors);

	public Environment getEnvironment();

	public String getLabel();

	public boolean isMandatory();

	public boolean isDisabled();

	public boolean isRead();

	public D getValue();

	/** @since 1.1 */
	public Control<C> getControl();

	/** @since 1.1 */
	public Converter<C,D> getConverter();

}

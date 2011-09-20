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

/**
 * An interface for components that mandates that the component is form element dependent and wants a reference to it.
 * 
 * @param <C> The type of the inner value of form element control.
 * @param <D> The type of the form element value.
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface FormElementAware<C, D> extends Serializable {

  /**
   * A method to provide <code>FormElementContext</code> to the <code>FormElementAware</code> component.
   * 
   * @param feCtx The context of a form element.
   */
  void setFormElementCtx(FormElementContext<C, D> feCtx);
}

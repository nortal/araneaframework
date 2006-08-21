/**
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
**/

package org.araneaframework.uilib.form;

import java.io.Serializable;
import org.araneaframework.uilib.form.converter.BaseConverter;

public interface Converter extends Serializable, FormElementAware {

  public void setFormElementCtx(FormElementContext feCtx);
  
  /**
   * This method converts the data from one type to another. If the data is <code>null</code>
   * then <code>null</code> is returned. Otherwise {@link #convertNotNull(Object)}method is used
   * for actual conversion.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  public Object convert(Object data);

  /**
   * This method converts the data from one type to another (though the types are exchanged in
   * comparison with {@link #convert(Object)}). If the data is <code>null</code> then <code>null</code>
   * is returned. Otherwise {@link #reverseConvertNotNull(Object)}method is used for actual
   * conversion.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  public Object reverseConvert(Object data);

  /**
   * This method should return a new converter, of the same type that the class that overrides it,
   * however freshly initialized.
   * 
   * @return a new converter, of the same type that the class that overrides it, however freshly
   * initialized.
   */
  public BaseConverter newConverter();

}

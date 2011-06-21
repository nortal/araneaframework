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
import org.araneaframework.uilib.form.converter.BaseConverter;

/**
 * Form data converter interface, for converting from one data type to another. See {@link BaseConverter} for
 * description of {@link Converter} purpose in forms. The generic <code>C</code> is the source data type, and the
 * generic <code>D</code> is the target data type after conversion.
 */
public interface Converter<C,D> extends Serializable, FormElementAware<C,D> {

  public void setFormElementCtx(FormElementContext<C,D> feCtx);

  /**
   * This method converts the data from one type to another.
   *  
   * @param data Data to convert.
   * @return Converted data.
   */
  public D convert(C data);

  /**
   * This method converts the data from one type to another (though the types are exchanged in
   * comparison with {@link #convert(Object)}).
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  public C reverseConvert(D data);

  /**
   * This method should return a new converter, of the same type that the class that overrides it,
   * however freshly initialized.
   * 
   * @return a new converter, of the same type that the class that overrides it, however freshly
   * initialized.
   */
  public Converter<C,D> newConverter();

}

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

package org.araneaframework.uilib.form.converter;

import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElementContext;

/**
 * Reverses the conversion of a contained converter.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class ReverseConverter<C, D> extends BaseConverter<C, D> {

  protected Converter<D, C> toReverse;

  /**
   * Creates class initializing the contained converter.
   * 
   * @param toReverse converter that will be reversed.
   */
  public ReverseConverter(Converter<D, C> toReverse) {
    this.toReverse = toReverse;
  }

  /**
   * Converts the data using {@link BaseConverter#reverseConvertNotNull}of the contained converter.
   */
  @Override
  public D convertNotNull(C data) {
    D result = this.toReverse.reverseConvert(data);
    return result;
  }

  /**
   * Converts the data using {@link BaseConverter#convertNotNull}of the contained converter.
   */
  @Override
  public C reverseConvertNotNull(D data) {
    C result = this.toReverse.convert(data);
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setFormElementCtx(FormElementContext feCtx) {
    super.setFormElementCtx(feCtx);
    this.toReverse.setFormElementCtx(feCtx);
  }

  /**
   * Returns a <code>new ReverseConverter(toReverse)</code>.
   */
  @Override
  public Converter<C, D> newConverter() {
    return new ReverseConverter<C, D>(this.toReverse.newConverter());
  }
}

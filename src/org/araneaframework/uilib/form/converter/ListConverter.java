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

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElementContext;

/**
 * This converter uses a contained converter to convert individual <code>List</code> items thus converting the entire
 * <code>List</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class ListConverter<C, D> extends BaseConverter<List<C>, List<D>> {

  protected Converter<C, D> listItemConverter;

  /**
   * Creates the converter initializing the contained converter.
   * 
   * @param listItemConverter the contained converter.
   */
  public ListConverter(Converter<C, D> listItemConverter) {
    this.listItemConverter = listItemConverter;
  }

  /**
   * Converts the <code>List</code> using contained converter.
   */
  @Override
  public List<D> convertNotNull(List<C> data) {
    List<D> result = new ArrayList<D>();
    for (C item : data) {
      result.add(this.listItemConverter.convert(item));
    }
    return result;
  }

  /**
   * Converts the <code>List</code> back using contained converter.
   */
  @Override
  public List<C> reverseConvertNotNull(List<D> data) {
    List<C> result = new ArrayList<C>();
    for (D item : data) {
      result.add(this.listItemConverter.reverseConvert(item));
    }
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setFormElementCtx(FormElementContext<List<C>, List<D>> feCtx) {
    super.setFormElementCtx(feCtx);
    this.listItemConverter.setFormElementCtx(FormElementContext.class.cast(feCtx));
  }

  /**
   * Returns a <code>new ListConverter(listItemConverter)</code>.
   */
  @Override
  public Converter<List<C>, List<D>> newConverter() {
    return new ListConverter<C, D>(this.listItemConverter.newConverter());
  }

}

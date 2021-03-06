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

import java.io.Serializable;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.MessageContext.MessageData;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElementContext;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * This class is the base class for form converters. The converters' task is to convert the value of form
 * {@link org.araneaframework.uilib.form.Control} to the value of {@link org.araneaframework.uilib.form.FormElement}
 * {@link org.araneaframework.uilib.form.Data} and back.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BaseConverter<C, D> implements Serializable, Converter<C, D> {

  private FormElementContext<C, D> feCtx;

  /**
   * This method converts the data from one type to another. If the data is <code>null</code> then <code>null</code> is
   * returned. Otherwise {@link #convertNotNull(Object)}method is used for actual conversion.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  public D convert(C data) {
    Assert.notNull(this, getFormElementCtx(), "Form element context must be assigned to the converter before it can "
        + "function! Make sure that the converter is associated with a form element!");
    return data == null ? null : convertNotNull(data);
  }

  /**
   * This method converts the data from one type to another (though the types are exchanged in comparison with
   * {@link #convert(Object)}). If the data is <code>null</code> then <code>null</code> is returned. Otherwise
   * {@link #reverseConvertNotNull(Object)}method is used for actual conversion.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  public C reverseConvert(D data) {
    Assert.notNull(this, getFormElementCtx(), "Form element context must be assigned to the converter before it can "
        + "function! Make sure that the converter is associated with a form element!");
    return data == null ? null : reverseConvertNotNull(data);
  }

  public void setFormElementCtx(FormElementContext<C, D> feCtx) {
    this.feCtx = feCtx;
  }

  public FormElementContext<C, D> getFormElementCtx() {
    return this.feCtx;
  }

  // *********************************************************************
  // * PROTECTED METHODS
  // *********************************************************************

  protected void addError(String error, Object... params) {
    this.feCtx.addError(error, params);
  }

  /**
   * Since mostly the parameter to the error message is the name of the input field, this method automatically adds the
   * resolved input field name to the given error message.
   * 
   * @param error The error message that takes the input field name as its parameter.
   * @since 2.0
   */
  protected void addErrorWithLabel(String error) {
    this.feCtx.addError(error, MessageUtil.localize(getLabel(), getEnvironment()));
  }

  protected void addErrors(Set<MessageData> errors) {
    this.feCtx.addErrors(errors);
  }

  protected Environment getEnvironment() {
    return this.feCtx.getEnvironment();
  }

  protected String getLabel() {
    return this.feCtx.getLabel();
  }

  // *********************************************************************
  // * ABSTRACT INTERFACE METHODS
  // *********************************************************************

  /**
   * This method should return a new converter, of the same type that the class that overrides it, however freshly
   * initialized.
   * 
   * @return a new converter, of the same type that the class that overrides it, however freshly initialized.
   */
  public abstract Converter<C, D> newConverter();

  // *********************************************************************
  // * ABSTRACT IMPLEMENTATION METHODS
  // *********************************************************************

  /**
   * This method should convert the data from one type to another. It may assume that the <code>data</code> is never
   * <code>null</code>.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  protected abstract D convertNotNull(C data);

  /**
   * This method should convert the data from one type to another. It may assume that the <code>data</code> is never
   * <code>null</code>. The types of data are reversed in comparison to {@link #convertNotNull(Object)}.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  protected abstract C reverseConvertNotNull(D data);

}

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

package org.araneaframework.uilib.form.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.araneaframework.Environment;

/**
 * This class is the base class for form converters. The converters' task is to convert the value
 * of form control to the value of form element data and back.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 *  
 */
public abstract class BaseConverter implements java.io.Serializable, Converter {

  //*********************************************************************
  // FIELDS
  //*********************************************************************

  private List errors = new ArrayList();
  private List reverseErrors = new ArrayList();
  
  private Environment environment;

  private String label;

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * Sets the label, that will be used in producing {@link org.araneaframework.uilib.support.UiMessage}s.
   * 
   * @param label the label, that will be used in producing {@link org.araneaframework.uilib.support.UiMessage}s.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * This method converts the data from one type to another. If the data is <code>null</code>
   * then <code>null</code> is returned. Otherwise {@link #convertNotNull(Object)}method is used
   * for actual conversion.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  public Object convert(Object data) {
    if (data == null)
      return null;

    errors.clear();

    return convertNotNull(data);
  }

  /**
   * This method converts the data from one type to another (though the types are exchanged in
   * comparison with {@link #convert(Object)}). If the data is <code>null</code> then <code>null</code>
   * is returned. Otherwise {@link #reverseConvertNotNull(Object)}method is used for actual
   * conversion.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  public Object reverseConvert(Object data) {
    if (data == null)
      return null;

    reverseErrors.clear();
    
    return reverseConvertNotNull(data);
  }

  /**
   * Returns the {@link org.araneaframework.uilib.support.UiMessage}s produced while converting the data.
   * 
   * @return the {@link org.araneaframework.uilib.support.UiMessage}s produced while converting the data.
   */
  public List getErrors() {
    return errors;
  }
  
  /**
   * Returns the {@link org.araneaframework.uilib.support.UiMessage}s produced while reverse converting the data.
   * 
   * @return the {@link org.araneaframework.uilib.support.UiMessage}s produced while reverse converting the data.
   */
  public List getReverseErrors() {
    return reverseErrors;
  }  

  /**
   * Clears the errors produced while converting the data.
   */
  public void clearErrors() {
    errors.clear();
  }
  
  /**
   * Clears the errors produced while reverse converting the data.
   */
  public void clearReverseErrors() {
  	reverseErrors.clear();
  }  

  /**
   * Returns whether the conversion was successful/valid.
   * 
   * @return whether the conversion was successful/valid.
   */
  public boolean isValid() {
    return errors.size() == 0;
  }
  
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  //*********************************************************************
  //* PROTECTED METHODS
  //*********************************************************************

  protected void addError(String error) {
  	errors.add(error);
  }
  
  protected void addReverseError(String error) {
  	reverseErrors.add(error);
  }
  
  protected void addErrors(Collection errorList) {
    errors.addAll(errorList);
  }  
  
  protected void addReverseErrors(Collection errorList) {
    reverseErrors.addAll(errorList);
  } 
  
  protected Environment getEnvironment() {
    return this.environment;
  }
  
  protected String getLabel() {
    return label;
  }
  
  //*********************************************************************
  //* ABSTRACT INTERFACE METHODS
  //*********************************************************************

  /**
   * This method should return a new converter, of the same type that the class that overrides it,
   * however freshly initialized.
   * 
   * @return a new converter, of the same type that the class that overrides it, however freshly
   * initialized.
   */
  public abstract BaseConverter newConverter();

  //*********************************************************************
  //* ABSTRACT IMPLEMENTATION METHODS
  //*********************************************************************

  /**
   * This method should convert the data from one type to another. It may assume that the <code>data</code>
   * is never <code>null</code>.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  protected abstract Object convertNotNull(Object data);

  /**
   * This method should convert the data from one type to another. It may assume that the <code>data</code>
   * is never <code>null</code>. The types of data are reversed in comparison to
   * {@link #convertNotNull(Object)}.
   * 
   * @param data Data to convert.
   * @return Converted data.
   */
  protected abstract Object reverseConvertNotNull(Object data);

}

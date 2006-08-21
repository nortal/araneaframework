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

import org.araneaframework.uilib.form.FormElementContext;

/**
 * Reverses the conversion of a contained converter.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class ReverseConverter extends BaseConverter {

  protected BaseConverter toReverse;

  /**
	 * Creates class initializing the contained converter.
	 * 
	 * @param toReverse converter that will be reversed.
	 */
  public ReverseConverter(BaseConverter toReverse) {
    this.toReverse = toReverse;
  }

  /**
	 * Converts the data using {@link BaseConverter#reverseConvertNotNull}of the
	 * contained converter.
	 */
  public Object convertNotNull(Object data) {
    Object result = toReverse.reverseConvertNotNull(data);
  	return result;    
  }

  /**
	 * Converts the data using {@link BaseConverter#convertNotNull}of the
	 * contained converter.
	 */
  public Object reverseConvertNotNull(Object data) {    
    Object result = toReverse.convertNotNull(data);  
  	return result;       
  }

  public void setFormElementCtx(FormElementContext feCtx) {
    super.setFormElementCtx(feCtx);
    
    toReverse.setFormElementCtx(feCtx);
  }

  /**
   * Returns a <code>new ReverseConverter(toReverse)</code>.
   */
  public BaseConverter newConverter() {
    return new ReverseConverter(toReverse.newConverter());
  }
}

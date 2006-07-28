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

package org.araneaframework.backend.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * @since 1.4.1.20
 */
public class BeanConverter implements Serializable {
  /**
   * Sets all the fields with same names to same values.
   * <p> 
   * NB! the values are references!
   * @param from <code>Value Object</code> from which to convert.
   * @param to <code>Value Object</code> to which to convert.
   */
  public static void convert(Object from, Object to) {
    if (from == null || to == null) {
      throw new NullPointerException("You cannot convert a Value Object to null or vice versa");
    }
    
    //Getting mappers
    BeanMapper fromVoMapper = new BeanMapper(from.getClass());
    BeanMapper toVoMapper = new BeanMapper(to.getClass());
    
    //Setting fields
    List fromVoFields = fromVoMapper.getBeanFields();
    for (Iterator i = fromVoFields.iterator(); i.hasNext();) {
      String field = (String) i.next();
      if (toVoMapper.fieldIsWritable(field) && 
          toVoMapper.getBeanFieldType(field).isAssignableFrom(fromVoMapper.getBeanFieldType(field))) {
        toVoMapper.setBeanFieldValue(to, field, (fromVoMapper.getBeanFieldValue(from, field)));
      }
    }
  }
  
  /**
   * Sets all the fields with same names to same values.
   * <p> 
   * NB! the values are references!
   * @param from <code>Value Object</code> from which to convert.
   * @param to <code>Class</code> of the Value Object to which to convert.
   * @return <code>Value Object</code> converted from <code>from</code>. 
   */  
  public static Object convert(Object from, Class to) {
    Object result = BeanUtil.newInstance(to);
    convert(from, result);
    return result;
  }
}

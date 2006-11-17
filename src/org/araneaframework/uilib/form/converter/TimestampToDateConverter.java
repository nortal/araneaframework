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

import java.sql.Timestamp;
import java.util.Date;
import org.araneaframework.uilib.form.Converter;

/**
 * Converts <code>Timestamp</code> to <code>Date</code> and back.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class TimestampToDateConverter extends BaseConverter {
  
  /**
   * Converts <code>Timestamp</code> to <code>Date</code>.
   */
  protected Object convertNotNull(Object data) {
    return new Date(((Timestamp)data).getTime());
  }
  
  /**
   * Converts <code>Date</code> to <code>Timestamp</code>.
   */
  protected Object reverseConvertNotNull(Object data) {
    return new Timestamp(((Date) data).getTime());
  }
  
  /**
   * Returns <code>new TimestampToDateConverter()</code>.
   */
  public Converter newConverter() {
    return new TimestampToDateConverter();
  }  
}

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

package org.araneaframework.uilib.support;

import java.io.Serializable;


/**
 * This class defines the <code>Map</code> key, that is used to find a
 * converter between data held in {@link org.araneaframework.uilib.form.Control} and 
 * corresponding {@link org.araneaframework.uilib.form.Data}.
 * 
 * @see org.araneaframework.uilib.form.converter.ConverterFactory
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class ConverterKey implements Serializable {
  private String fromType;
  private String toType;

  /**
   * Returns the type from which the conversion goes.
   * 
   * @return the type from which the conversion goes.
   */
  public String getFromType() {
    return fromType;
  }

  /**
   * Returns the type to which the conversion goes.
   * 
   * @return the type to which the conversion goes.
   */
  public String getToType() {
    return toType;
  }

  /**
	 * Creates the class, initializing both "to" and "from" types.
	 * 
	 * @param fromType
	 *          the type from which the conversion goes.
	 * @param toType
	 *          the type to which the conversion goes.
	 */
  public ConverterKey(String fromType, String toType) {
    this.fromType = fromType;
    this.toType = toType;
  }

  /**
   * Implements the {@link Object#equals(java.lang.Object)} method, using both
   * types.
   */
  public boolean equals(Object o) {
    if (!(o instanceof ConverterKey))
      return false;
    ConverterKey inst = (ConverterKey) o;
    if (!(this.fromType == null ? inst.fromType == null : this.fromType.equals(inst.fromType)))
      return false;
    if (!(this.toType == null ? inst.toType == null : this.toType.equals(inst.toType)))
      return false;
    return true;
  }

  /**
   * Implemets the {@link Object#hashCode()} method, using both
   * types. 
   * 
   * 
	 */
  public int hashCode() {
    int result = 17;
    result = 37 * result + fromType.hashCode();
    result = 37 * result + toType.hashCode();
    return result;
  }
}

/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

package org.araneaframework.backend.list.helper;

import java.io.Serializable;

/**
 * This class represents the <code>null</code> value that will be passed to
 * <code>PreparedStatement.setNull</code>. It is used to pass the information
 * about corresponding SQL type.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class NullValue implements Serializable {

  private static final long serialVersionUID = 1L;

  private int type;

  public NullValue(int type) {
    this.type = type;
  }

  /**
   * Returns the type.
   * @return the type.
   */
  public int getType() {
    return this.type;
  }

  /**
   * Sets the type.
   * @param type type.
   */
  public void setType(int type) {
    this.type = type;
  }
}

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

package org.araneaframework.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import org.araneaframework.OutputData;

/**
 * A mimed {@link org.araneaframework.OutputData}. Supports setting the mime-type of the output
 * stream.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public interface MimeOutputData extends OutputData, Serializable {
  /**
   * Sets the mime-type of the output stream to type.
   * @param type
   */
  public void setMimeType(String type);
  
  /**
   * Returns the OutputStream of this class.
   */
  public OutputStream getOutputStream() throws IOException;
}

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

package org.araneaframework.jsp.tag.layout.support;

import javax.servlet.jsp.JspException;

/**
 * Interface that is implemented by tags that provide class to cells inside them.
 * Cells search {@link CellClassProvider} from {@link javax.servlet.jsp.PageContext} under {@link CellClassProvider#KEY}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface CellClassProvider {
  public static final String KEY = "cellClassProvider";

  public String getCellClass() throws JspException;
}

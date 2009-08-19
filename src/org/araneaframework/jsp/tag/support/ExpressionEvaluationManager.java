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

package org.araneaframework.jsp.tag.support;

import java.io.Serializable;
import javax.servlet.jsp.PageContext;
import org.araneaframework.uilib.ConfigurationContext;
import javax.servlet.jsp.JspException;

/**
 * A custom plugin interface for JSP EL evaluation. Since there might be
 * problems with the default solution where Jakarta taglibs is used, in Spring
 * framework application the Spring solution for EL evaluation is automatically
 * used. Anyway the EL evaluation can be overridden with any implementation
 * using {@link ConfigurationContext}.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.0.1
 */
public interface ExpressionEvaluationManager extends Serializable {

  /**
   * The method handles EL evaluation with given data.
   * @param attributeName The name of the JSP tag attribute.
   * @param attributeValue The value of the JSP tag attribute.
   * @param classObject The class name of the tag.
   * @param pageContext The context to resolve variables.
   * @return Evaluated value of the <code>attributeValue</code>.
   * @throws JspException
   */
  Object evaluate(String attributeName, String attributeValue, Class classObject, PageContext pageContext)
      throws JspException;
}

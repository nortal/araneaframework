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

package org.araneaframework.jsp.tag.support;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.araneaframework.integration.spring.SpringExpressionEvaluationManager;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * Default implementation for JSP EL expression evaluation using Jakarta Taglibs. Note that Jakarta Taglibs solution may
 * result with great memory consumption.
 * <p>
 * With Spring framework integration, the {@link SpringExpressionEvaluationManager} is used instead, by default, unless
 * another implementation is specified through {@link ConfigurationContext}.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1.0.1
 */
public class DefaultExpressionEvaluationManager implements ExpressionEvaluationManager {

  public <T> T evaluate(String attributeName, String attributeValue, Class<T> classObject, PageContext pageContext)
      throws JspException {
    T result = null;

    if (attributeValue != null) {
      result = classObject.cast(ExpressionEvaluatorManager.evaluate(attributeName, attributeValue, classObject,
          pageContext));
    }

    return result;
  }
}

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

package org.araneaframework.integration.spring;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.core.Assert;
import org.araneaframework.jsp.tag.support.ExpressionEvaluationManager;
import org.araneaframework.uilib.ConfigurationContext;
import org.springframework.web.util.ExpressionEvaluationUtils;

/**
 * The Spring framework solution for JSP EL evaluation. This class is used by default in a Spring framework application,
 * unless another implementation is specified through {@link ConfigurationContext}.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1.0.1
 */
public class SpringExpressionEvaluationManager implements ExpressionEvaluationManager {

  @SuppressWarnings("unchecked")
  public <T> T evaluate(String attributeName, String attributeValue, Class<T> classObject, PageContext pageContext)
      throws JspException {

    Assert.notNullParam(this, classObject, "classObject");

    if (attributeValue == null) {
      return null;

    } else if (String.class.equals(classObject)) {
      return (T) ExpressionEvaluationUtils.evaluateString(attributeName, attributeValue, pageContext);

    } else if (Boolean.class.equals(classObject)) {
      return (T) Boolean.valueOf(ExpressionEvaluationUtils.evaluateBoolean(attributeName, attributeValue, pageContext));

    } else if (Integer.class.equals(classObject)) {
      return (T) new Integer(ExpressionEvaluationUtils.evaluateInteger(attributeName, attributeValue, pageContext));

    } else if (ExpressionEvaluationUtils.isExpressionLanguage(attributeValue)) {
      return (T) ExpressionEvaluationUtils.evaluate(attributeName, attributeValue, classObject, pageContext);

    } else if (Object.class.equals(classObject)) {
      return (T) ObjectUtils.toString(attributeValue);

    } else {
      try {
        return (T) ConstructorUtils.invokeExactConstructor(classObject, attributeValue);
      } catch (Exception e) {
        throw new JspException("Error while changing the type of a JSP tag attribute value.", e);
      }
    }
  }

}

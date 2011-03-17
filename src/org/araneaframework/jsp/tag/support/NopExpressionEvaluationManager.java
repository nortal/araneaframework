
package org.araneaframework.jsp.tag.support;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.ObjectUtils;

/**
 * Expression evaluation manager for newer JSP tags where JSP container already evaluates expressions so that tags would
 * not have to. Therefore, this class does just data type conversions instead of attempting to parse values - and saves
 * some CPU time and effort.
 * <p>
 * Also note that using this expression evaluation manager is also more secure for newer JSP containers since other
 * expression managers may fail with user input values like "some text ${ABCDEFG} some text", when the user value
 * is provided to a tag that attempts to evaluate it (and ends with expression evaluation error).
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class NopExpressionEvaluationManager implements ExpressionEvaluationManager {

  public <T> T evaluate(String attributeName, String attributeValue, Class<T> classObject, PageContext pageContext)
      throws JspException {

    if (String.class.equals(classObject)) {
      return classObject.cast(attributeValue);

    } else if (Object.class.equals(classObject)) {
      return classObject.cast(ObjectUtils.toString(attributeValue));

    } else {
      try {
        return classObject.cast(ConstructorUtils.invokeExactConstructor(classObject, attributeValue));
      } catch (Exception e) {
        throw new JspException("Error while changing the type of a JSP tag attribute value.", e);
      }
    }
  }

}

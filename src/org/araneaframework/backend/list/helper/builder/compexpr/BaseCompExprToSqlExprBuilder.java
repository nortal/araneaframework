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

package org.araneaframework.backend.list.helper.builder.compexpr;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.CompExprToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;

/**
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class BaseCompExprToSqlExprBuilder implements CompExprToSqlExprBuilder {

  private static final Log LOG = LogFactory.getLog(BaseCompExprToSqlExprBuilder.class);

  private Map<Class<?>, CompExprToSqlExprTranslator> translators = new HashMap<Class<?>, CompExprToSqlExprTranslator>();

  protected void addTranslator(Class<?> expressionClass, CompExprToSqlExprTranslator translator) {
    this.translators.put(expressionClass, translator);
  }

  public SqlExpression buildSqlExpression(ComparatorExpression expression) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("ComparatorExpression class: " + expression.getClass().getName());
    }

    CompExprToSqlExprTranslator translator = this.translators.get(expression.getClass());

    if (translator == null) {
      throw new RuntimeException("ComparatorExpression of class " + expression.getClass() + " not supported");
    }

    return translator.translate(expression, this);
  }
}

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

package org.araneaframework.uilib.list.structure.filter.field;

import java.util.Map;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.list.structure.filter.BaseFilter;
import org.araneaframework.uilib.list.structure.filter.FieldFilter;
import org.araneaframework.uilib.list.util.ExpressionUtil;

/**
 * Base implementation for {@link FieldFilter}.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public abstract class BaseFieldFilter extends BaseFilter implements FieldFilter {

  // Field
  private String fieldId;

  // Value
  private String valueId;

  private Object value;

  private boolean constant = false;

  public String getFieldId() {
    return this.fieldId;
  }

  public void setFieldId(String fieldId) {
    Assert.notNullParam(fieldId, "fieldId");
    this.fieldId = fieldId;
  }

  public String getValueId() {
    return this.valueId;
  }

  public void setValueId(String valueId) {
    this.valueId = valueId;
  }

  public Object getValue() {
    return this.value;
  }

  public void setValue(Object value) {
    this.value = value;
    this.constant = true;
  }

  public boolean isConstant() {
    return this.constant;
  }

  // Building expressions

  protected boolean isActive(Map<String, Object> filterInfo) {
    return isConstant() || filterInfo.containsKey(getValueId());
  }

  protected VariableExpression buildVariableExpression() {
    return ExpressionUtil.var(getFieldId());
  }

  protected ValueExpression<Object> buildValueExpression(Map<String, Object> filterInfo) {
    Object val = isConstant() ? getValue() : filterInfo.get(getValueId());
    return ExpressionUtil.value(getValueId(), val);
  }
}

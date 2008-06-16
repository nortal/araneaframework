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

package org.araneaframework.uilib.list.structure.filter.field;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.structure.filter.field.BaseFieldFilter;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;
import org.araneaframework.uilib.list.util.FormUtil;
import org.araneaframework.uilib.list.util.NestedFormUtil;
import org.araneaframework.uilib.util.Event;

/**
 * IN filter - constructs a query: x IN (y,w,z)
 * 
 * @author Allar Tammik
 * @since 1.1.4
 */
public class InFilter extends BaseFieldFilter {

  private static final long serialVersionUID = 1L;

  protected Comparator comparator;

  /**
   * @param fieldId database column name
   * @param valueId FormElement id
   * @return a new instance of <code>InFilter</code>.
   */
  public static InFilter getInstance(final FilterContext ctx,
      final String fieldId, String valueId) {
    InFilter filter = new InFilter();
    filter.setFieldId(fieldId);
    filter.setValueId(valueId);
    ctx.addInitEvent(filter.new OnInitEvent(ctx, fieldId));
    return filter;
  }

  public static void addToForm(FilterContext ctx, String id) {
    FilterFormUtil.createElement(ctx, id);
  }

  public static void addToForm(FilterContext ctx, String id, FormElement element) {
    NestedFormUtil.addElement(ctx.getForm(), id, element);
  }

  public static void addToForm(FilterContext ctx, String id, Control control, Data data) {
    FormElement result = FormUtil.createElement(
        FilterFormUtil.TEMPORARY_LABEL, control, data, false);

    FilterFormUtil.setLabel(ctx, result, id);
    addToForm(ctx, id, result);
  }

  // private
  private InFilter() {}

  public Comparator getComparator() {
    return comparator;
  }

  public void setComparator(Comparator comparator) {
    Assert.isInstanceOfParam(this, Serializable.class, comparator, "comparator");
    this.comparator = comparator;
  }

  public Expression buildExpression(Map filterInfo) {
    if (!isActive(filterInfo)) {
      return null;
    }

    List valueIds = (List) filterInfo.get(getValueId());
    Iterator valueIdIterator = valueIds.iterator();
    List values = new LinkedList();

    for (int i = 0; valueIdIterator.hasNext(); i++) {
      values.add(ExpressionUtil.value(getValueId() + i, valueIdIterator.next()));
    }

    return ExpressionUtil.in(buildVariableExpression(), // the name of the column
        values); // query parameters
  }

  protected class OnInitEvent implements Event {

    private static final long serialVersionUID = 1L;

    protected FilterContext ctx;

    protected String fieldId;

    public OnInitEvent(FilterContext ctx, String fieldId) {
      this.ctx = ctx;
      this.fieldId = fieldId;
    }

    public void run() {
      setComparator(ctx.getFieldComparator(fieldId));
    }

  }

}

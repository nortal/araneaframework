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

package org.araneaframework.uilib.list;

import org.araneaframework.core.Assert;

/**
 * ListWidget that is aware of field types according to the Bean type.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * @see ListWidget
 */
public class BeanListWidget<T> extends ListWidget<T> {

  protected final Class<T> beanType;

  /**
   * Constructs a {@link BeanListWidget} for specified Bean type.
   * 
   * @param beanType The list row data object type.
   */
  public BeanListWidget(Class<T> beanType) {
    Assert.notNullParam(this, beanType, "beanType");
    this.beanType = beanType;
    this.typeHelper = createTypeHelper();
    this.listStructure = createListStructure();
  }

  @Override
  protected TypeHelper createTypeHelper() {
    // beanType == null when the constructor of the parent class is executed.
    if (this.beanType == null) {
      return super.createTypeHelper();
    } else {
      return new BeanTypeHelper(this.beanType);
    }
  }
}

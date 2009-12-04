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

package org.araneaframework.backend.list.memorybased;

import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.util.BeanMapper;

/**
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class BeanVariableResolver<B> implements VariableResolver {

  private BeanMapper<B> mapper;

  private B bean;

  public BeanVariableResolver(Class<B> clazz) {
    this.mapper = new BeanMapper<B>(clazz);
  }

  public void setBean(B bean) {
    this.bean = bean;
  }

  public Object resolve(Variable variable) {
    return this.mapper.getProperty(this.bean, variable.getName());
  }
}

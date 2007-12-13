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

package org.araneaframework.backend.list.helper.naming;

import org.araneaframework.backend.list.memorybased.Variable;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.core.Assert;

/**
 * VariableResolver that converts a variable (using its name) into column name.
 *
 * @see ColumnAliasVariableResolver
 * 
 * @author Rein Raudjärv
 */
public class ColumnNameVariableResolver implements VariableResolver {
	
	private static final long serialVersionUID = 1L;
	
	private final NamingStrategy namingStrategy;

	public ColumnNameVariableResolver(NamingStrategy namingStrategy) {
		Assert.notNullParam(namingStrategy, "namingStrategy");
		this.namingStrategy = namingStrategy;
	}

	public Object resolve(Variable variable) {
		return namingStrategy.fieldToColumnAlias(variable.getName());
	}

}

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


/**
 * NamingStrategy implementation which does not alter the names.
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public class NopNamingStrategy implements NamingStrategy {
	protected static final NamingStrategy INSTANCE = new NopNamingStrategy();
	
	public static NamingStrategy getInstance() {
		return INSTANCE;
	}
	
	protected NopNamingStrategy() {}
	
	public String beanFieldToResultSetColumnName(String beanField) {
		return beanField;
	}

	public String fieldToColumnAlias(String variableName) {
		return variableName;
	}

	public String fieldToColumnName(String variableName) {
		return variableName;
	}	
}

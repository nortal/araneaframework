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

package org.araneaframework.backend.list.helper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Generic callback interface for code that operates on a JDBC Connection.
 * Allows to execute any number of operations on a single Connection,
 * using any type and number of Statements.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public interface ConnectionCallback {
	/**
	 * Does not need to care about activating or closing the
	 * Connection, or handling transactions.
	 * 
	 * @param con active JDBC Connection
	 * @return a result object, or null if none
	 * @throws SQLException if thrown by a JDBC method
	 */
	Object doInConnection(Connection con) throws SQLException;
}

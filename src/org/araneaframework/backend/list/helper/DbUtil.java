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
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;


/**
 * Utility class providing some general methods for manipulating connection to the database.
 * NB! Every and one class and/or method that utilizes database connection, statement and/or result set
 * should use this class.
 *
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class DbUtil {
	private static Logger log = Logger.getLogger(DbUtil.class);

	private DbUtil() {
		// static methods only - hide constructor
	}
	
	/**
	 * Closes connection to the database along with current statement and result set.
	 * @param con connection to be closed.
	 * @param stmt statement to be closed.
	 * @param rs result set to be closed.
	 */
	public static void closeDbObjects(Connection con, Statement stmt, ResultSet rs){		
		if (rs != null){
			try{
				rs.close();
			}
			catch (Exception e){
				log.warn("Resultset can not be closed.", e);
			}
		}		
		if (stmt != null){
			try{
				stmt.close();
			}
			catch (Exception e){
				log.warn("Statement can not be closed.", e);
			}
		}		
		if (con != null){
			try{
				con.close();
			}
			catch (Exception e){
				log.warn("Connection can not be closed.", e);
			}
		}		
	}
}

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

package org.araneaframework.example.main.business.data;

import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.araneaframework.backend.list.helper.HSqlListSqlHelper;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.example.main.business.model.PersonMO;


public class PersonListDAO {

	private static final Logger log = Logger.getLogger(PersonListDAO.class);

	protected DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ListItemsData getItems(ListQuery request) {
		log.debug("Getting items, start index " + request.getItemRangeStart()
				+ ", count = " + request.getItemRangeCount() + ", filter = "
				+ request.getFilterExpression() + ", order = "
				+ request.getOrderExpression());

		HSqlListSqlHelper helper = new HSqlListSqlHelper(request);

		helper.setColumnMapping("id", "ID");
		helper.setColumnMapping("name", "NAME");
		helper.setColumnMapping("surname", "SURNAME");
		helper.setColumnMapping("phone", "PHONE");
		helper.setColumnMapping("birthdate", "BIRTHDATE");

		StringBuffer query = new StringBuffer();
		query.append(helper.getDatabaseFields());
		query.append(" FROM person");
		query.append(helper.getDatabaseFilterWith(" WHERE ", ""));
		query.append(helper.getDatabaseOrderWith(" ORDER BY ", ""));
		log.debug("SQL Query: " + query);

		helper.setSqlQuery(query.toString());
		helper.addStatementParams(helper.getDatabaseFilterParams());
		helper.addStatementParams(helper.getDatabaseOrderParams());

		ListItemsData data;
		try {
			log.debug("Executing Queries");
			helper.setDataSource(this.dataSource);
			helper.execute();
			data = helper.getListItemsData(PersonMO.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			helper.close();
		}

		return data;
	}
}

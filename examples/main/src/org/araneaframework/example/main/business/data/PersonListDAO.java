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
import org.araneaframework.backend.list.helper.HSqlListSqlHelper;
import org.araneaframework.backend.list.helper.ListSqlHelper;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.example.main.business.model.PersonMO;


/**
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class PersonListDAO {

	protected DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ListItemsData getItems(ListQuery request) {
		ListSqlHelper helper = new HSqlListSqlHelper(this.dataSource, request);

		helper.addMapping("id", "ID");
		helper.addMapping("name", "NAME");
		helper.addMapping("surname", "SURNAME");
		helper.addMapping("phone", "PHONE");
		helper.addMapping("birthdate", "BIRTHDATE");
		helper.addMapping("salary", "SALARY");

		helper.setSimpleSqlQuery("person");
		return helper.execute(PersonMO.class);
	}
}

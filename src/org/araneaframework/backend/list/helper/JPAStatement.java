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

package org.araneaframework.backend.list.helper;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * A statement to be used with {@link JPAListSqlHelper}.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class JPAStatement extends SqlStatement {

  protected String query;

  protected List<Object> parameters;

  public JPAStatement(String query, List<Object> parameters) {
    this.query = query;
    this.parameters = parameters;
  }

  public JPAStatement(String query) {
    this(query, new ArrayList<Object>());
  }

  public JPAStatement() {
    this(null);
  }

  /**
   * Helper method that sets the parameters to the <code>javax.persistence.Query</code>.
   * 
   * @param query <code>query</code> for which parameters will be set.
   */
  protected void propagateStatementWithParams(Query query) {
    for (int i = 1; i <= this.parameters.size(); i++) {
      Object parameter = this.parameters.get(i - 1);
      if (parameter instanceof NullValue) {
        query.setParameter(i, null);
      } else if (parameter != null && parameter.getClass() == java.util.Date.class) {
        query.setParameter(i, (java.util.Date) parameter, TemporalType.TIMESTAMP);
      } else if (parameter != null && parameter.getClass() == java.util.Calendar.class) {
        query.setParameter(i, (java.util.Calendar) parameter, TemporalType.TIMESTAMP);
      } else {
        query.setParameter(i, parameter);
      }
    }
  }
}

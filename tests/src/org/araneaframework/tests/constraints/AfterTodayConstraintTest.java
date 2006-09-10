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

package org.araneaframework.tests.constraints;

import java.util.Date;
import junit.framework.TestCase;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AfterTodayConstraint;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.data.DateData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class AfterTodayConstraintTest extends TestCase {
  public static class FakeDateControl extends DateControl {
    public boolean isRead() { return true; }
  }

  public static final long DAY = 1000l * 60 * 60 * 24;

  protected FormWidget form;
  protected FormElement dateElement;

  public void setUp() throws Exception {
    form = new FormWidget();
    dateElement = form.createElement("#date", new FakeDateControl(), new DateData(), true);
    form.addElement("date", dateElement);
    MockLifeCycle.begin(form, new MockEnvironment());
  }
  
  protected void executeTest(
      Constraint constraint,
      Date dateValue,
      boolean valid
      ) throws Exception {
    dateElement.setConstraint(constraint);
    dateElement.setValue(dateValue);
    dateElement.getControl().setRawValue(dateValue);

    assertEquals(valid, dateElement.validate());
    form.clearErrors();
    assertEquals(valid, form.validate());
    form.clearErrors(); // allows using more than once per setup 
  }

  public void testFuture() throws Exception {
    executeTest(
        new AfterTodayConstraint(false), 
        new Date(System.currentTimeMillis() + 20*AfterTodayConstraintTest.DAY), 
        true);
  }
  
  public void testPast() throws Exception {
    executeTest(
        new AfterTodayConstraint(false), 
        new Date(System.currentTimeMillis() - 20*AfterTodayConstraintTest.DAY), 
        false);
  }

  public void testPresent() throws Exception {
    Date now = new Date();
    // disallow today
    executeTest(
        new AfterTodayConstraint(false), 
        now, 
        false);
    // allow today
    executeTest(
        new AfterTodayConstraint(true), 
        now, 
        true);
  }
}

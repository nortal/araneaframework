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

package org.araneaframework.tests.constraints;

import java.sql.Timestamp;
import java.util.Date;
import junit.framework.TestCase;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.tests.constraints.helper.ConstraintTestHelper;
import org.araneaframework.tests.mock.MockEnvironment;
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

    @Override
    public boolean isRead() {
      return true;
    }
  }

  public static final long DAY = 1000L * 60 * 60 * 24;

  private FormWidget form;

  private FormElement<Timestamp, Date> dateElement;

  private ConstraintTestHelper<Timestamp, Date> helper;

  @Override
  public void setUp() throws Exception {
    this.form = new FormWidget();
    this.dateElement = this.form.createElement("#date", new FakeDateControl(), new DateData(), false);
    this.form.addElement("date", this.dateElement);
    MockLifeCycle.begin(this.form, new MockEnvironment());
    this.helper = new ConstraintTestHelper<Timestamp, Date>(this.form, this.dateElement);
  }

  public void testFuture() throws Exception {
    Date now = new Date(System.currentTimeMillis() + 20 * DAY);
    this.helper.testConstraintValidness(new AfterTodayConstraint(false), now, true);
    this.helper.testConstraintValidness(new AfterTodayConstraint(true), now, true);
  }

  public void testPast() throws Exception {
    Date now = new Date(System.currentTimeMillis() - 20 * DAY);
    this.helper.testConstraintValidness(new AfterTodayConstraint(false), now, false);
    this.helper.testConstraintValidness(new AfterTodayConstraint(true), now, false);
  }

  // test that constraint works correctly with today's date
  public void testPresent() throws Exception {
    Date now = new Date();
    this.helper.testConstraintValidness(new AfterTodayConstraint(false), now, false); // disallow today
    this.helper.testConstraintValidness(new AfterTodayConstraint(true), now, true); // allow today
  }

  // in case of date being null, after today constraint should validate
  public void testNull() throws Exception {
    this.helper.testConstraintValidness(new AfterTodayConstraint(false), null, true);
  }
}

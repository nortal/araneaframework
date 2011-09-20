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

package org.araneaframework.uilib.support;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface UiLibMessages {

  String RANGE_CHECK_FAILED = "uilib.form.rangecheckfailed";

  String DATE_BEFORE_TODAY = "uilib.form.date.beforetoday";

  String DATE_BEFORE_NOW = "uilib.form.date.beforenow";

  String DATE_BEFORE_TODAY_TODAY_ALLOWED = "uilib.form.date.beforetoday.today.allowed";

  String ELEMENT_EMPTY = "uilib.form.element.empty";

  String STRING_NOT_IN_RANGE = "uilib.form.string.notinrange";

  String MANDATORY_FIELD = "uilib.form.mandatoryfield";

  String WRONG_DATE_FORMAT = "uilib.form.date.wrongformat";

  /** @since 1.1 */
  String WRONG_TIME_FORMAT = "uilib.form.time.wrongformat";

  String WRONG_DECIMAL_FORMAT = "uilib.form.decimal.wrongformat";

  String NOT_A_NUMBER = "uilib.form.number.notanumber";

  /** @since 1.0.7 */
  String NOT_INTEGER = "uilib.form.number.notinteger";

  String NUMBER_NOT_BETWEEN = "uilib.form.number.notbetween";

  String NUMBER_NOT_GREATER = "uilib.form.number.notgreater";

  String NUMBER_NOT_LESS = "uilib.form.number.notless";

  String SCALE_NOT_LESS = "uilib.form.scale.notless";

  String FORBIDDEN_MIME_TYPE = "uilib.form.forbiddenmimetype";

  String FILE_UPLOAD_FAILED = "uilib.form.file.upload.failure";

  String STRING_TOO_SHORT = "uilib.form.string.tooshort";

  String STRING_TOO_LONG = "uilib.form.string.toolong";

  String LIST_FILTER_BUTTON_LABEL = "uilib.list.filter.button";

  String LIST_FILTER_CLEAR_BUTTON_LABEL = "uilib.list.filter.clear.button";

  String NOT_AN_EMAIL = "uilib.form.email.notanemail";

  String LOW_OF = "uilib.list.filter.low";

  String HIGH_OF = "uilib.list.filter.high";

  /** @since 1.0.11 */
  String INPUT_FILTER_NOT_MATCHED = "uilib.form.control.inputfilter.nonmatch";
}

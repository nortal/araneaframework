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

package org.araneaframework.jsp;

/**
 * Non-standard-HTML attributes added to specific HTML tags to define
 * Aranea event and content model information.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class AraneaAttributes {
  private AraneaAttributes() {}
	
  /** Attribute identifying Aranea system form */
  public static String SYSTEM_FORM = "arn-systemForm";

  /** Event related non-standard HTML tag attributes */
  public interface Event {
     public static String ID = "arn-evntId";
     public static String TARGET_WIDGET_ID = "arn-trgtwdgt";
     public static String PARAM = "arn-evntPar";
     public static String UPDATE_REGIONS = "arn-updrgns";
     public static String CONDITION = "arn-evntCond";
  }
}

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

package org.araneaframework.template.framework.resource;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class TemplateConfiguration implements ConfigurationContext {
  private static Map configuration = new HashMap();
  
  static {
    configuration.put(ConfigurationContext.CUSTOM_DATE_FORMAT, "dM|dMM|ddM|ddMM|ddMMyy|ddMMyyyy|dd.MM.yyyy|dd:MM:yyyy|dd,MM,yyyy|dd.MM|dd,MM|dd:MM|dd.MM.yy|dd:MM:yy|dd,MM,yy");
    configuration.put(ConfigurationContext.CUSTOM_TIME_FORMAT, "Hmm|HHmm|H:mm|H.mm|H,mm|HH:mm|HH.mm|HH,mm");
  }
	
	public Object getEntry(String entryName) {
		return configuration.get(entryName);
	}
}

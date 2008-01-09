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

package org.araneaframework.uilib.core;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.LocalFormElementValidationErrorRenderer;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardConfiguration implements ConfigurationContext {
  private Map confEntries = new HashMap();
  
  {
	  // XXX: remove after testing done
	  confEntries.put(ConfigurationContext.FORMELEMENT_ERROR_RENDERER, LocalFormElementValidationErrorRenderer.INSTANCE);
  }
  
  public Object getEntry(String entryName) {
    return confEntries.get(entryName);
  }

  public void setConfEntries(Map confEntries) {
    this.confEntries = confEntries;
  }
}

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

package org.araneaframework.template.tags.presentation;

import java.io.Writer;
import org.araneaframework.jsp.tag.UiBaseTag;



/**
 * SAMPLE toolbar left part tag.
 * 
 * @author Marko Muts
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "path"
 *   body-content = "JSP"
 */
public class SamplePathTag extends UiBaseTag {
  public final static String KEY = "ee.wm.util.sample.web.jsp.ui.presentation.SamplePathTag.KEY";
  
  protected boolean hadItems = false;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    addContextEntry(KEY, this);
    return EVAL_BODY_INCLUDE;    
  }

  /**
   * Callback: path item
   */
  protected void onItem() {
    this.hadItems = true;
  }

  /**
   * Tells if this path has had items.
   */
  protected boolean getHadItems() {
    return this.hadItems;
  }
}

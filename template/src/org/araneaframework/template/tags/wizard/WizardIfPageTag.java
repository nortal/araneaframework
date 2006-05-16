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

package org.araneaframework.template.tags.wizard;

import java.io.Writer;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.template.framework.context.WizardContext;


/**
 * This tag includes its body if the wizard's current page index matches specified index.
 * Page index must fit into following format:
 * ["not"] (page index value | "first" | "last")
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 * 
 * @jsp.tag
 *   name = "wizardIfPage"
 *   body-content = "JSP"
 *   description = "Includes its body if the wizard's current page index matches specified index."
 */
public class WizardIfPageTag extends UiBaseTag {
  public static final String INDEX_FIRST = "first";
  public static final String INDEX_LAST = "last";
  public static final String INDEX_NOT_PREFIX = "not";
  public static final String WIDGET_CONTEXT_KEY = "org.araneaframework.jsp.ui.context.UiWidgetContextTag.WIDGET";

  private String index;
  
  protected int doStartTag(Writer out) throws Exception {

    WizardContext wizard = (WizardContext) requireContextEntry(WIDGET_CONTEXT_KEY);    

    int curIndex = wizard.getCurrentPageIndex();
    int count = wizard.countPages();

    String ifIndex = (String) evaluate("index", this.index, String.class);

    boolean not = false;

    if (ifIndex.startsWith(INDEX_NOT_PREFIX)) {
      not = true;
      ifIndex = ifIndex.substring(INDEX_NOT_PREFIX.length());
    }
    int index;
    if (ifIndex.equals(INDEX_FIRST)) {
      index = 0;
    }
    else if (ifIndex.equals(INDEX_LAST)) {
      index = count - 1;
    }
    else {
      index = Integer.parseInt(ifIndex);
    }

    return (!not && index == curIndex || not && index != curIndex) ? EVAL_BODY_INCLUDE : SKIP_BODY;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "true"
   *   description = "Page index with following format:
           ["not"] (page index value | "first" | "last")." 
   */
  public void setIndex(String index) {
    this.index = index;
  }
}

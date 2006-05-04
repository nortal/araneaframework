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

package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import java.util.List;
import org.apache.commons.collections.iterators.LoopingIterator;
import org.araneaframework.jsp.tag.UiPresentationTag;

/**
 * Layout row base tag.
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public abstract class LayoutRowBaseTag extends UiPresentationTag {
  protected List cellClasses;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    requireContextEntry(LayoutInterface.KEY);

    addContextEntry(LayoutRowInterface.KEY, this);
    if (cellClasses != null && !cellClasses.isEmpty())
    	  addContextEntry(LayoutInterface.CELLCLASS_KEY, new LoopingIterator(cellClasses));

    return EVAL_BODY_INCLUDE;
  }
}

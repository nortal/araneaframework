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

package org.araneaframework.jsp.tag.include;

import java.io.Writer;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;

/**
 * Include base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseIncludeTag extends BaseTag implements AttributedTagInterface {

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    addContextEntry(AttributedTagInterface.ATTRIBUTED_TAG_KEY, this);
    return EVAL_BODY_INCLUDE;
  }

  public void addAttribute(String name, String value) {
    this.addContextEntry(name, evaluate("attributeValue", value, Object.class));
  }
}

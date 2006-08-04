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

package org.araneaframework.jsp.tag;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

/**
 * UI iteration base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseIterationTag extends BaseTag implements IterationTag {
  /**
   * Internal callback: afer body.
   * @throws Exception
   */
  protected int afterBody(Writer out) throws Exception {
    return SKIP_BODY;
  }

  //
  // IterationTag methods
  //

  /**
   * Standard implementation of after body.
   */  
  public int doAfterBody() throws JspException {
    try {
      return afterBody(pageContext.getOut());
    }
    catch(RuntimeException e) {
      throw e;
    }
    catch(JspException e) {
      throw e;    
    }    
    catch(Exception e) {
      throw new JspException(e);
    }    
  }
}

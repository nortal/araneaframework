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

package org.araneaframework.jsp.tag.aranea;

import java.io.Writer;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import org.araneaframework.OutputData;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;
import org.araneaframework.servlet.filter.StandardJspFilterService;

/**
 * 
 * @jsp.tag
 *   name = "root"
 *   body-content = "JSP"
 */
public class UiAraneaRootTag extends UiBaseTag {
  public static final String OUTPUT_DATA_KEY = "outputData";
  
  protected int before(Writer out) throws Exception {
    super.before(out);
    
    OutputData output = 
      (OutputData) pageContext.getRequest().getAttribute(
          StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
    StandardJspFilterService.Configuration config = 
      (StandardJspFilterService.Configuration) output.getAttribute(
          StandardJspFilterService.JSP_CONFIGURATION_KEY);
    
    pushAttribute(
        OUTPUT_DATA_KEY, 
        output, 
        PageContext.REQUEST_SCOPE);
    
    Config.set(
        pageContext, 
        Config.FMT_LOCALIZATION_CONTEXT, 
        new LocalizationContext(
            new StringAdapterResourceBundle(config.getCurrentBundle()),
            config.getCurrentLocale()
          ),
          PageContext.REQUEST_SCOPE           
        );
    
    return EVAL_BODY_INCLUDE;
  }
}


/**
 * Adapter resource bundle that converts all objects to string.
 */
class StringAdapterResourceBundle extends ResourceBundle {
  ResourceBundle bundle;
  
  StringAdapterResourceBundle(ResourceBundle bundle) {
    this.bundle = bundle;
  }
  
  protected Object handleGetObject(String key) {
    Object object = bundle.getObject(key);
    return (object != null) ? object.toString() : null;
  } 
  
  public Enumeration getKeys() {
    return bundle.getKeys();
  } 
}

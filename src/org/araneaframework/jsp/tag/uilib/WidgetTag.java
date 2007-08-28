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

package org.araneaframework.jsp.tag.uilib;        

import java.io.Writer;



/**
 * Widget base tag.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "widget"
 *   body-content = "JSP"
 *   description = "UiLib widget tag. <br/> 
           Makes available following page scope variables: 
           <ul>
             <li><i>widget</i> - UiLib widget view model.
           </ul> "
 */
public class WidgetTag extends BaseWidgetTag {
  public static final String WIDGET_ID_KEY = "widgetId";
  public static final String WIDGET_KEY = "widget";
  public static final String WIDGET_VIEW_MODEL_KEY = "viewModel";
  public static final String WIDGET_VIEW_DATA_KEY = "viewData";
  
  public int doStartTag(Writer out) throws Exception {
     super.doStartTag(out);
     
     // Set variables
     addContextEntry(WIDGET_ID_KEY, fullId);
     addContextEntry(WIDGET_VIEW_MODEL_KEY, viewModel);
     addContextEntry(WIDGET_KEY, widget);
     addContextEntry(WIDGET_VIEW_DATA_KEY, viewModel.getData());
     
     // Continue
     return SKIP_BODY;    
  }
}

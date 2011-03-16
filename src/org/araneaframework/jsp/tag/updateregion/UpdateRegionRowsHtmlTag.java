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

package org.araneaframework.jsp.tag.updateregion;


/**
 * Defines the update region in the HTML page that can be updated via AJAX requests.
 * Should be used when updating content inside HTML <code>table</code> rows, due to
 * browser peculiarities these cases are handled differently.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "updateRegionRows" 
 *   body-content = "JSP"
 *   description = "See 'updateRegion' tag. Use this tag if you need to update several table rows (there is no way to update table cells only)."
 */
public class UpdateRegionRowsHtmlTag extends BaseUpdateRegionTag {

  public UpdateRegionRowsHtmlTag() {
    this.tag = "tbody";
  }

}

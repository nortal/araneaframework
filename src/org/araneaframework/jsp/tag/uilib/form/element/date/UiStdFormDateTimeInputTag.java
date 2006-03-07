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

package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.DateTimeControl;



/**
 * Standard date/time input form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "dateTimeInput"
 *   body-content = "JSP"
 *   description = "Form date and time input field (custom control), represents UiLib "DateTimeControl"."
 */
public class UiStdFormDateTimeInputTag extends UiStdFormDateTimeInputBaseTag {	
        
  protected String timeStyleClass;
  protected String dateStyleClass;
  
  protected void init() {
    super.init();
    
    timeStyleClass = "aranea-time-input";
    dateStyleClass = "aranea-date-input";
  }  
  
  public String getDateStyleClass() {
    return dateStyleClass;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Css class for date." 
   */
  public void setDateStyleClass(String dateCssClass) throws JspException {
    this.dateStyleClass = (String) evaluate("dateStyleClass", dateCssClass, String.class);
  }

  public String getTimeStyleClass() {
    return timeStyleClass;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Css class for time." 
   */
  public void setTimeStyleClass(String timeCssClass) throws JspException {
    this.timeStyleClass = (String) evaluate("timeStyleClass", timeCssClass, String.class);
  }
  
  //
  // Implementation
  //  
  
  protected int after(Writer out) throws Exception {
		// Type check
		assertControlType("DateTimeControl");
		
		// Prepare
		String name = this.getScopedFullFieldId(); 		
		DateTimeControl.ViewModel viewModel = ((DateTimeControl.ViewModel)controlViewModel);
				

	    Long  timeInputSize = DEFAULT_TIME_INPUT_SIZE;
	    Long  dateInputSize = DEFAULT_DATE_INPUT_SIZE;
		// Write
		this.writeTimeInput(
									out, 
									name,
									name + ".time", 
									viewModel.getTime(), 
									localizedLabel,
									timeInputSize,
									viewModel.isDisabled(),
                  getTimeStyleClass(),
									accessKey,
                                    viewModel.getTimeViewModel());
		out.write("&nbsp;");
		this.writeDateInput(
										out, 
										null, // The id is needed for the access key.
										name + ".date", 
										viewModel.getDate(), 
										localizedLabel,
										viewModel.isMandatory(), 
										formElementViewModel.isValid(),
										dateInputSize,
										validate,
										viewModel.isDisabled(),
                    getDateStyleClass(),
										null,
                    viewModel.getDateViewModel());
    
    if (validate) writeValidationScript(out, viewModel);
    
    // Continue
    super.after(out);
    return EVAL_PAGE;	
	}
  
  /**
   * Write validation javascript
   * @author Konstantin Tretyakov
   */
  protected void writeValidationScript(Writer out, DateTimeControl.ViewModel viewModel) throws IOException {
    UiUtil.writeStartTag(out, "script");
    out.write("uiAddDateTimeValidator(");
    UiUtil.writeScriptString(out, getScopedFullFieldId());
    out.write(", ");
    UiUtil.writeScriptString(out, localizedLabel);
    out.write(", ");
    out.write(viewModel.isMandatory() ? "true" : "false");
    out.write(");\n");
    UiUtil.writeEndTag_SS(out, "script");
  }     
}





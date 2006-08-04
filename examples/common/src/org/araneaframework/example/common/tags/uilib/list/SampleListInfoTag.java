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

package org.araneaframework.example.common.tags.uilib.list;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.form.BaseSystemFormHtmlTag;
import org.araneaframework.jsp.tag.uilib.list.ListTag;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.SequenceHelper;


/**
 * XXX: remove
 * List widget info tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "listInfo"
 *   body-content = "JSP"
 */
public class SampleListInfoTag extends BaseTag {
  public final static String DEFAULT_TITLE_STRING_ID = "list.info.title";
  public final static String DEFAULT_RECORD_STRING_ID = "list.info.record";
  public final static String DEFAULT_NO_DATA_STRING_ID = "list.info.noData";

  public final static String SHOW_SLICE_EVENT_ID = "showSlice";
  public final static String SHOW_ALL_EVENT_ID = "showAll";

  protected String titleStringId = DEFAULT_TITLE_STRING_ID;
  protected String recordStringId = DEFAULT_RECORD_STRING_ID;
  protected String noDataStringId = DEFAULT_NO_DATA_STRING_ID ;

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    String systemFormId = (String)requireContextEntry(BaseSystemFormHtmlTag.ID_KEY);

    // Get list data
    String listId = (String)requireContextEntry(ListTag.LIST_FULL_ID_KEY);    
    ListWidget.ViewModel viewModel = (ListWidget.ViewModel)requireContextEntry(ListTag.LIST_VIEW_MODEL_KEY);

    // Get sequence data
    SequenceHelper.ViewModel sequenceViewModel = viewModel.getSequence();

    long totalItemCount = sequenceViewModel.getTotalItemCount().longValue();
    boolean allItemsShown = sequenceViewModel.getAllItemsShown().booleanValue();

    long pageFirstItem = sequenceViewModel.getPageFirstItem().longValue();
    long pageLastItem = sequenceViewModel.getPageLastItem().longValue();

    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeCloseStartTag(out);

    if (totalItemCount > 0) {
      JspUtil.writeEscaped(out, JspUtil.getResourceString(pageContext, titleStringId));
      out.write("&nbsp;");
      JspUtil.writeStartTag(out, "b");
      JspUtil.writeEscaped(out, new Long(pageFirstItem).toString());
      out.write("-");   
      JspUtil.writeEscaped(out, new Long(pageLastItem).toString());
      JspUtil.writeEndTag(out, "b");
      out.write("&nbsp;[");
      JspUtil.writeOpenStartTag(out, "a");
      JspUtil.writeAttribute(out, "class", "aranea-link-button");
      JspUtil.writeAttribute(out, "href", "javascript:");
      JspWidgetCallUtil.writeEventAttributeForEvent(
          pageContext,
          out, 
          "onclick", 
          systemFormId, 
          listId, 
          allItemsShown ? SHOW_SLICE_EVENT_ID : SHOW_ALL_EVENT_ID, 
              null,
              null);

      JspUtil.writeCloseStartTag_SS(out);
      JspUtil.writeEscaped(out, new Long(totalItemCount).toString());
      out.write("&nbsp;");    
      JspUtil.writeEscaped(out, JspUtil.getResourceString(pageContext, recordStringId));
      JspUtil.writeEndTag_SS(out, "a");
      out.write("]");
    }
    else
      JspUtil.writeEscaped(out, JspUtil.getResourceString(pageContext, noDataStringId));

    JspUtil.writeEndTag(out, "span");

    return EVAL_BODY_INCLUDE;    
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Layout height." 
   */
  public void setTitleStringId(String titleStringId) throws JspException {
    this.titleStringId = (String)evaluateNotNull("titleStringId", titleStringId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Layout height." 
   */
  public void setNoDataStringId(String noDataStringId) throws JspException {
    this.noDataStringId = (String)evaluateNotNull("noDataStringId", noDataStringId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Layout height." 
   */
  public void setRecordStringId(String recordStringId) throws JspException {
    this.recordStringId = (String)evaluateNotNull("showAll", recordStringId, String.class);
  }
}

package org.araneaframework.jsp.tag.uilib;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.container.StandardContainerWidget;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.ClientStateUtil;
import org.araneaframework.http.util.URLUtil;
import org.araneaframework.jsp.tag.form.BaseSystemFormHtmlTag;

/**
 * DataWidget tag. Makes available <code>dataUrl</code> EL variable that can be
 * used to render {@link org.araneaframework.uilib.DataWidget}'s data.
 *
 * @author Alar Kvell (alar@araneaframework.org)
 *
 * @jsp.tag
 *   name = "dataWidget"
 *   body-content = "JSP" 
 *   description = "DataWidget tag.<br/> 
           Makes available following page scope variables: 
           <ul>
             <li><i>dataUrl</i> - URL which returns DataWidget's data.
           </ul> "
 */
public class DataWidgetTag extends BaseWidgetTag {

  public static final String DATA_URL_KEY = "dataUrl";

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    addContextEntry(DATA_URL_KEY, getDataUrl());
    return EVAL_BODY_INCLUDE;    
  }

  protected String getDataUrl() throws JspException {
    Map state = (Map) getOutputData().getAttribute(ClientStateUtil.SYSTEM_FORM_STATE);
    String systemFormId = (String) requireContextEntry(BaseSystemFormHtmlTag.ID_KEY);
    Map m = new HashMap();
    m.put(TransactionContext.TRANSACTION_ID_KEY, TransactionContext.OVERRIDE_KEY);
    m.put(TopServiceContext.TOP_SERVICE_KEY, state.get(TopServiceContext.TOP_SERVICE_KEY));
    m.put(ThreadContext.THREAD_SERVICE_KEY, state.get(ThreadContext.THREAD_SERVICE_KEY));
    m.put(StandardContainerWidget.ACTION_PATH_KEY, fullId);
    m.put(BaseSystemFormHtmlTag.SYSTEM_FORM_ID_KEY, systemFormId);
    return ((HttpOutputData) getOutputData()).encodeURL(URLUtil.parametrizeURI(((HttpInputData) getOutputData().getInputData()).getContainerURL(), m));
  }

}

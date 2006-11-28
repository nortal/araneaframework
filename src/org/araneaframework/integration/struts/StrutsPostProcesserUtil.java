package org.araneaframework.integration.struts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.regexp.RESyntaxException;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;

public abstract class StrutsPostProcesserUtil {
  public static String writeAttributes(Map attributes) throws RESyntaxException {
    StringBuffer result = new StringBuffer();
    for (Iterator i = attributes.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      result.append(entry.getKey().toString());
      result.append("=\"");
      result.append(entry.getValue().toString());
      result.append("\"");
      if (i.hasNext())
        result.append(" ");
    }
    
    return result.toString();
  }
  
  public static Map extractAttributes(String tagStart) throws RESyntaxException {
    Map result = new HashMap();
    
    Pattern attributePattern = Pattern.compile("([a-zA-Z\\-_]+)=\"(.*?)\"", 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher attributeMatcher = attributePattern.matcher(tagStart);
    
    while (attributeMatcher.find()) {
      result.put(attributeMatcher.group(1).toLowerCase(), attributeMatcher.group(2));
    }
    
    return result;
  }
  
  public static byte[] postProcess(byte[] vanilla, InputData input, OutputData output) throws Exception {        
    String html = new String(vanilla, "UTF-8");
        
    Pattern bodyPattern = Pattern.compile("<body.*?>(.*)</body>", 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher bodyMatcher = bodyPattern.matcher(html);
    bodyMatcher.find();
    html = bodyMatcher.group(1);
    
    StringBuffer result = new StringBuffer();
    
    Pattern formPattern = Pattern.compile("(<form.*?>)(.*)</form>", 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher formMatcher = formPattern.matcher(html);
        
    int formSearchStart = 0;
    
    while (formSearchStart < html.length() && formMatcher.find(formSearchStart)) {
      result.append(html.substring(formSearchStart, formMatcher.start(0)));
      
      String formTagStart = formMatcher.group(1);
      String formBody = formMatcher.group(2);

      Map formTagAttrs = extractAttributes(formTagStart);
      
      result.append("<script>");
      result.append("document.forms[\"");
      result.append(formTagAttrs.get("name"));
      result.append("\"]=_ap.getSystemForm(this);");      
      result.append("</script>");
      
      int submitSearchStart = 0;
            
      Pattern submitPattern = Pattern.compile("<input type=\"submit\".*?>", 
          Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
      Matcher submitMatcher = submitPattern.matcher(formBody);
      
      while (submitMatcher.find(submitSearchStart)) {
        result.append(formBody.substring(submitSearchStart, submitMatcher.start(0)));
        
        StringBuffer submitBuf = new StringBuffer();
        
        Map attrs = extractAttributes(submitMatcher.group(0));
        
        submitBuf.append("<input type=\"submit\" name=\"");
        submitBuf.append("submit".equalsIgnoreCase((String) attrs.get("name")) ? "DO_SUBMIT" : attrs.get("name"));
        submitBuf.append("\" onclick=\"");
        submitBuf.append("sf = _ap.getSystemForm(this); ");
        submitBuf.append("sf.name='");
        submitBuf.append(formTagAttrs.get("name"));  
        submitBuf.append("'; ");
        submitBuf.append("sf.id='");
        submitBuf.append(formTagAttrs.get("name"));  
        submitBuf.append("'; ");        
        if (formTagAttrs.containsKey("onsubmit")) {
          submitBuf.append("sf.onsubmit = function () {");
          submitBuf.append(((String)formTagAttrs.get("onsubmit")));
          submitBuf.append("};");
        } 
        else {
          submitBuf.append("sf.onsubmit = function () {return true;};");
        }
        submitBuf.append("sf.action='");
        submitBuf.append(formTagAttrs.get("action"));
        if (formTagAttrs.containsKey("method")) {
          submitBuf.append("'; sf.method='");
          submitBuf.append(formTagAttrs.get("method"));
        }
        if (formTagAttrs.containsKey("enctype")) {
          submitBuf.append("'; sf.enctype='");
          submitBuf.append(formTagAttrs.get("enctype"));
        }
        submitBuf.append("';");
        if (attrs.containsKey("onclick")) {
          submitBuf.append(attrs.get("onclick"));
          if (!((String) attrs.get("onclick")).trim().endsWith(";"))
            submitBuf.append(";");
        }        
        submitBuf.append("return true;\" ");
        
        attrs.remove("name");
        attrs.remove("type");
        attrs.remove("onclick");
        submitBuf.append(writeAttributes(attrs));
        
        submitBuf.append(">");
        
        result.append(submitBuf.toString());
        
        submitSearchStart = submitMatcher.end(0);
      }
      
      result.append(formBody.substring(submitSearchStart));
      formSearchStart = formMatcher.end(0);
    }
    
    result.append(html.substring(formSearchStart));
        
    String htmlResult = result.toString();
    
    return htmlResult.getBytes("UTF-8"); 
  }
}

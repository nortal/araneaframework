
package org.araneaframework.mock;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.NotImplementedException;
import org.araneaframework.http.HttpInputData;

public class MockHttpInputData extends MockInputData implements HttpInputData {

  public MockHttpInputData(Map<String, String> data) {
    super(data);
  }

  public String getCharacterEncoding() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String getContainerPath() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String getContainerURL() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String getContentType() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String getContextPath() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String getContextURL() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public Locale getLocale() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public Iterator<String> getParameterNames() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String[] getParameterValues(String name) {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String getPath() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String getRequestURL() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String popPathPrefix() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public void pushPathPrefix(String pathPrefix) {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public void setCharacterEncoding(String encoding) {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public String getSimplePath() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

  public void setUseFullURL(boolean useFullURL) {}
}

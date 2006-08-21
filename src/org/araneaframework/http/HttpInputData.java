package org.araneaframework.http;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

import org.araneaframework.InputData;

public interface HttpInputData extends InputData {
	String getPath();
  void pushPathPrefix(String pathPrefix);
  void popPathPrefix();
  
  String[] getParameterValues(String name);
  Iterator getParameterNames();
  
  URL getRequestURL();
  String getContainerURL();
  String getContextURL();
  
  String getCharacterEncoding() ;
  void setCharactedEncoding(String encoding) throws UnsupportedEncodingException;
  
  String getContentType();
  Locale getLocale();
}

package org.araneaframework.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import org.araneaframework.OutputData;

public interface HttpOutputData extends OutputData {
  String encodeURL(String url);
  
  void sendRedirect(String location) throws IOException;
  
  OutputStream getOutputStream() throws IOException;
  PrintWriter getWriter() throws IOException;
  
  void setContentType(String type); 
  
  String getCharacterEncoding();
  void setCharacterEncoding(String encoding);
  
  Locale getLocale();
}

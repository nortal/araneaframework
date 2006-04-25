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

package org.araneaframework.servlet.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.servlet.ServletFileUploadInputExtension;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.ServletOverridableInputData;
import org.araneaframework.servlet.core.StandardServletFileUploadInputExtension;

/**
 * This filter uses Commons FileUpload to parse the request and upload the <code>multipart/form-data</code> 
 * encoded files to a temporary directory.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardServletFileUploadFilterService extends BaseFilterService {
  private static final Logger log = Logger.getLogger(StandardServletFileUploadFilterService.class);
  
  private String multipartEncoding;
  private boolean useRequestEncoding = false; 
  private Integer maximumCachedSize = null;
  private Long maximumSize = null;
  private String tempDirectory = null;
  

  /**
   * Sets the character encoding that will be used to decode the <code>multipart/form-data</code>
   *  encoded strings. The default encoding is determined by Commons FileUpload.
   */
  public void setMultipartEncoding(String multipartEncoding) {
    this.multipartEncoding = multipartEncoding;
  }

  /**
   * When set to "true" will use the request character encoding to parse the <code>multipart/form-data</code>
   * encoded strings.
   */
  public void setUseRequestEncoding(boolean useRequestEncoding) {
    this.useRequestEncoding = useRequestEncoding;
  }

  /**
   * Sets the maximum size of file that may be cached in memory.
   */
  public void setMaximumCachedSize(Integer maximumCachedSize) {
    this.maximumCachedSize = maximumCachedSize;
  }

  /**
   * Sets the maximum size of file that may be uploaded to server.
   */
  public void setMaximumSize(Long maximumSize) {
    this.maximumSize = maximumSize;
  }

  /**
   * Sets the temporary directory to use when uploading files.
   */
  public void setTempDirectory(String tempDirectory) {
    this.tempDirectory = tempDirectory;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletRequest request = ((ServletInputData) input).getRequest();
    
    if (FileUpload.isMultipartContent(request)) {
      Map fileItems = new HashMap();
      Map parameterLists = new HashMap();
      
      // Create a new file upload handler
      DiskFileUpload upload = new DiskFileUpload();
      
      if (useRequestEncoding)
        upload.setHeaderEncoding(request.getCharacterEncoding());
      else if (multipartEncoding != null)
        upload.setHeaderEncoding(multipartEncoding);     

      // Set upload parameters
      if (maximumCachedSize != null)
        upload.setSizeThreshold(maximumCachedSize.intValue());
      if (maximumSize != null)
        upload.setSizeMax(maximumSize.longValue());
      if (tempDirectory != null)
        upload.setRepositoryPath(tempDirectory);

      // Parse the request
      List items = upload.parseRequest(request);

      // Process the uploaded items
      Iterator iter = items.iterator();
      while (iter.hasNext()) {
        FileItem item = (FileItem) iter.next();

        if (!item.isFormField()) {
          fileItems.put(item.getFieldName(), item);
        }
        else {
          List parameterValues = (List) parameterLists.get(item.getFieldName());
          
          if (parameterValues == null) {
            parameterValues = new ArrayList();    
            parameterLists.put(item.getFieldName(), parameterValues);
          }
          
          parameterValues.add(item.getString());
        }
      }
      
      log.debug("Parsed multipart request, found '" + fileItems.size() + 
          "' file items and '" + parameterLists.size() + "' request parameters");
      
      output.extend(
          ServletFileUploadInputExtension.class, 
          new StandardServletFileUploadInputExtension(fileItems));
      
      request = new MultipartWrapper(request, parameterLists);
      ((ServletOverridableInputData) input).setRequest(request);
    }   
    
    super.action(path, input, output);
  }
  
  /**
   * 
   * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
   */
  private static class MultipartWrapper extends HttpServletRequestWrapper {
    Map parameters = new HashMap();
    
    public MultipartWrapper(HttpServletRequest arg0, Map parameterLists) throws Exception {
      super(arg0);
      
      for (Iterator i = parameterLists.entrySet().iterator(); i.hasNext();) {
        Map.Entry entry = (Map.Entry) i.next();
        List parameterList = (List) entry.getValue();
        
        parameters.put(entry.getKey(), toStringArray(parameterList.toArray()));
      }
    }        
    
    private String[] toStringArray(Object[] array) throws Exception { 
      String[] result = new String[array.length];
      
      for (int i = 0; i < array.length; i++) {
        result[i] = (String) array[i];
      }
      
      return result;
    }
    
    public String getParameter(String arg0) {
      String[] result = getParameterValues(arg0);
      
      if (result == null || result.length == 0)
        return null;
      
      return result[0];
    }
    
    public Map getParameterMap() {
      return parameters;
    }
    
    public Enumeration getParameterNames() {
      return Collections.enumeration(parameters.keySet());
    }
    
    public String[] getParameterValues(String arg0) {
      return (String[]) parameters.get(arg0);
    }
  }
}

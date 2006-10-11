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

package org.araneaframework.http.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.FileUploadContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.FileUploadInputExtension;
import org.araneaframework.http.core.StandardFileUploadInputExtension;
import org.araneaframework.http.util.ServletUtil;

/**
 * This filter uses Commons FileUpload to parse the request and upload the <code>multipart/form-data</code> 
 * encoded files to a temporary directory.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardFileUploadFilterService extends BaseFilterService {
  private static final Logger log = Logger.getLogger(StandardFileUploadFilterService.class);
  
  private static boolean commonsFileUploadPresent = true;
  
  static {
    try {
      Class.forName("org.springframework.web.context.WebApplicationContext");
    }
    catch (ClassNotFoundException e) {
      commonsFileUploadPresent = false;
    }
  }
  
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
  
  protected void init() throws Exception {
    if (!commonsFileUploadPresent)
      log.warn("Jakarta Commons FileUpload not found! File uploading and multipart request handling will be disabled!");
    
    super.init();    
  }
  
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(super.getChildEnvironment(), FileUploadContext.class, new FileUploadContextImpl(this.maximumSize));
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletRequest request = ServletUtil.getRequest(input);
   
    if (commonsFileUploadPresent && ServletFileUpload.isMultipartContent(new ServletRequestContext(request))) {
      Map fileItems = new HashMap();
      Map parameterLists = new HashMap();
      
      DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
      // Set upload parameters
      if (maximumCachedSize != null)
        fileItemFactory.setSizeThreshold(maximumCachedSize.intValue());
      if (tempDirectory != null)
        fileItemFactory.setRepository(new File(tempDirectory));

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
      if (maximumSize != null)
          upload.setSizeMax(maximumSize.longValue());

      if (useRequestEncoding)
      	upload.setHeaderEncoding(request.getCharacterEncoding());
      else if (multipartEncoding != null)
        upload.setHeaderEncoding(multipartEncoding);

      // Parse the request
      // FIXME: somehow parse the request so that all information except file upload data 
      // comes through when exception occurs.
      List items = null;
      Exception uploadException = null;
      try {
        items = upload.parseRequest(request);
      } catch (FileUploadException e) {
        if (log.isDebugEnabled())
          log.debug(Assert.thisToString(this) + ": exception occured  parsing multipart request.", e);
        uploadException = e;
      }

      // Process the uploaded items
      if (items != null) {
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
          DiskFileItem item = (DiskFileItem) iter.next();

          if (!item.isFormField()) {
            fileItems.put(item.getFieldName(), item);
          }
          else {
            List parameterValues = (List) parameterLists.get(item.getFieldName());
          
            if (parameterValues == null) {
              parameterValues = new ArrayList();    
              parameterLists.put(item.getFieldName(), parameterValues);
            }

            String encoding = item.getCharSet() != null ? item.getCharSet() : request.getCharacterEncoding();
            parameterValues.add(encoding != null ? item.getString(encoding): item.getString());
          }
        }
      }
      
      if (log.isDebugEnabled())
        log.debug("Parsed multipart request, found '" + fileItems.size() + 
            "' file items and '" + parameterLists.size() + "' request parameters");
      
      input.extend(
          FileUploadInputExtension.class, 
          new StandardFileUploadInputExtension(fileItems, uploadException));
      
      request = new MultipartWrapper(request, parameterLists);
      ServletUtil.setRequest(input, request);
    }   
    
    super.action(path, input, output);
  }
  
  /**
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
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
  
  private static class FileUploadContextImpl implements FileUploadContext {
    private Long maximumSize;

    public FileUploadContextImpl(Long maximumSize) {
      this.maximumSize = maximumSize != null ? maximumSize : new Long(new ServletFileUpload().getSizeMax());
    }

    public Long getFileSizeLimit() {
      return maximumSize;
    }
  }
}

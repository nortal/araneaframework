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

package org.araneaframework.http.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.FileUploadContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.FileUploadInputExtension;
import org.araneaframework.http.core.StandardFileUploadInputExtension;
import org.araneaframework.http.util.ServletUtil;

/**
 * This filter uses Commons FileUpload to parse the request and upload the <code>multipart/form-data</code> encoded
 * files to a temporary directory.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardFileUploadFilterService extends BaseFilterService implements FileUploadContext {

  private static final Log LOG = LogFactory.getLog(StandardFileUploadFilterService.class);

  /**
   * The response message, if the user submits a multipart request that is larger than {@link #maximumSize} or allowed
   * by {@link ServletFileUpload}. Because Apache Commons FileUpload basically blocks and removes the "illegal" content
   * then we have no simple way to access it, and still use it, although, only the file perhaps was too large. Consider
   * it as a security feature. To allow larger requests, use {@link #setMaximumSize(Long)} (at your own risk).
   * 
   * @since 1.2.2
   */
  public static final String FAIL_RESPONSE = "FAIL: YOU SENT TOO MUCH DATA TO THE SERVER, AND HENCE YOUR REQUEST WAS DROPPED.";

  /**
   * An variable that tells whether the Apache Commons FileUpload functionality is in the classpath.
   */
  protected static boolean commonsFileUploadPresent = true;

  static {
    try {
      Class.forName("org.apache.commons.fileupload.servlet.ServletFileUpload");
    } catch (ClassNotFoundException e) {
      commonsFileUploadPresent = false;
    }
  }

  protected String multipartEncoding;

  protected boolean useRequestEncoding = false;

  protected Integer maximumCachedSize = null;

  protected Long maximumSize = null;

  protected Long maximumRequestSize = null;

  protected String tempDirectory = null;

  /**
   * Sets the character encoding that will be used to decode the <code>multipart/form-data</code> encoded strings. The
   * default encoding is determined by Commons FileUpload.
   * 
   * @param multipartEncoding The encoding that is used to read request when {@link #useRequestEncoding} is set to
   *          false.
   */
  public void setMultipartEncoding(String multipartEncoding) {
    this.multipartEncoding = multipartEncoding;
  }

  /**
   * When set to "true" will use the request character encoding to parse the <code>multipart/form-data</code> encoded
   * strings.
   * 
   * @param useRequestEncoding Whether to use the request encoding instead of {@link #multipartEncoding} to read request
   *          data.
   */
  public void setUseRequestEncoding(boolean useRequestEncoding) {
    this.useRequestEncoding = useRequestEncoding;
  }

  /**
   * Sets the maximum size of file that may be cached in memory.
   * 
   * @param maximumCachedSize The maximum size of file that may be cached in memory.
   */
  public void setMaximumCachedSize(Integer maximumCachedSize) {
    this.maximumCachedSize = maximumCachedSize;
  }

  /**
   * Sets the maximum size of file that may be uploaded to server.
   * 
   * @param maximumSize The maximum size of file that may be uploaded to server.
   */
  public void setMaximumSize(Long maximumSize) {
    this.maximumSize = maximumSize;
  }

  /**
   * Sets the maximum size of the request that may be sent to the server
   * 
   * @param maximumRequestSize The maximum size of the request that may be sent to the server.
   */
  public void setMaximumRequestSize(Long maximumRequestSize) {
    this.maximumRequestSize = maximumRequestSize;
  }

  /**
   * Sets the temporary directory to use when uploading files.
   * 
   * @param tempDirectory The temporary directory to use when uploading files.
   */
  public void setTempDirectory(String tempDirectory) {
    this.tempDirectory = tempDirectory;
  }

  /**
   * Returns the maximum allowed file size. If the value of {@link #maximumSize} is not provided then the Apache File
   * Upload is checked for the same limit.
   * 
   * @return The maximum allowed file size;
   */
  public Long getFileSizeLimit() {
    return this.maximumSize != null ? this.maximumSize : new ServletFileUpload().getSizeMax();
  }

  @Override
  protected void init() throws Exception {
    if (!commonsFileUploadPresent) {
      LOG.warn("Jakarta Commons FileUpload not found! File uploading and multipart request handling will be disabled!");
    }
    super.init();
  }

  @Override
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(super.getChildEnvironment(), FileUploadContext.class, this);
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletRequest req = ServletUtil.getRequest(input);

    if (parseMultipart(new ServletRequestContext(req), input, output)) {
      super.action(path, input, output);
    }
  }

  /**
   * Checks whether the request is multipart, and, if so, parses the request. This method may modify the request.
   * 
   * @param request The original request.
   * @param input The Aranea input holder.
   * @param output The Aranea output holder.
   * @return A Boolean indicating whether the request was successful.
   * @throws Exception During content parsing some exceptions may be thrown.
   * @since 1.2.2
   */
  @SuppressWarnings("unchecked")
  private boolean parseMultipart(ServletRequestContext request, InputData input, OutputData output) throws Exception {

    boolean parsingSucceeded = true;

    if (!commonsFileUploadPresent || !FileUploadBase.isMultipartContent(request)) {
      return parsingSucceeded;
    }

    FileUpload upload = createUpload(request);
    List<FileItem> parsedItems = null;

    try {
      // Parse the request:
      parsedItems = upload.parseRequest(request);
    } catch (FileUploadException e) {

      if (LOG.isDebugEnabled()) {
        LOG.debug(Assert.thisToString(this) + ": exception occured  parsing multipart request.", e);
      }

      parsingSucceeded = false;
    }

    Map<String, FileItem> fileItems = new HashMap<String, FileItem>();
    Map<String, List<String>> parameters = new HashMap<String, List<String>>();

    // Process the uploaded items
    Exception uploadException = sortParsedItems(parsedItems, fileItems, parameters, request.getCharacterEncoding());

    if (LOG.isDebugEnabled()) {
      LOG.debug("Parsed multipart request, found '" + fileItems.size() + "' file items and '" + parameters.size()
          + "' request parameters");
    }

    afterMultipartRequestParsing(parsingSucceeded, input, output, parameters, fileItems, uploadException);
    return parsingSucceeded;
  }

  /**
   * Instantiates a {@link FileUpload} with the settings provided to this class.
   * 
   * @param request The request that will be parsed.
   * @return A new {@link FileUpload} instance.
   * @since 1.2.2
   */
  protected FileUpload createUpload(ServletRequestContext request) {
    DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();

    // Set upload parameters
    if (this.maximumCachedSize != null) {
      fileItemFactory.setSizeThreshold(this.maximumCachedSize.intValue());
    }
    if (this.tempDirectory != null) {
      fileItemFactory.setRepository(new File(this.tempDirectory));
    }

    // Create a new file upload handler
    ServletFileUpload upload = new ServletFileUpload(fileItemFactory);

    if (this.maximumRequestSize != null) {
      upload.setSizeMax(this.maximumRequestSize.longValue());
    }

    if (this.useRequestEncoding) {
      upload.setHeaderEncoding(request.getCharacterEncoding());
    } else if (this.multipartEncoding != null) {
      upload.setHeaderEncoding(this.multipartEncoding);
    }

    return upload;
  }

  /**
   * Sorts the parsed items by reading the parsed values and putting them into <code>fileItems</code> or
   * <code>parameters</code>. The <code>requestEncoding</code> parameter is provided to help to read the values from
   * request with the right encoding.
   * <p>
   * This method alse checks that no file is greater in size that allowed. If there is a problem with that then an
   * exception is returned.
   * 
   * @param parsedItems The items parsed from the multipart request.
   * @param fileItems The map where to put all read file items as &lt;field-name,file&gt; pairs.
   * @param parameters The map where to put all other form input as &lt;field-name,value&gt; pairs.
   * @param requestEncoding The request encoding. May be <code>null</code>.
   * @return An Exception object, if there was a problem with a file.
   * @throws Exception Unexpected exceptions related to reading the values.
   * @since 1.2.2
   */
  protected Exception sortParsedItems(List<FileItem> parsedItems, Map<String, FileItem> fileItems,
      Map<String, List<String>> parameters, String requestEncoding) throws Exception {

    if (parsedItems == null) {
      return null;
    }

    Exception uploadException = null;

    for (FileItem item : parsedItems) {
      if (!item.isFormField()) {
        if (this.maximumSize != null && this.maximumSize < item.getSize()) {
          uploadException = new FileUploadBase.FileSizeLimitExceededException("", item.getSize(), this.maximumSize);
          continue;
        } else if (item.getSize() <= 0) {
          continue;
        }

        if (LOG.isWarnEnabled() && fileItems.containsKey(item.getFieldName())) {
          LOG.warn(item.getFieldName() + " already has an associated file, overwriting...");
        }

        fileItems.put(item.getFieldName(), item);

      } else {
        List<String> parameterValues = parameters.get(item.getFieldName());

        if (parameterValues == null) {
          parameterValues = new ArrayList<String>();
          parameters.put(item.getFieldName(), parameterValues);
        }

        String value = requestEncoding != null ? item.getString(requestEncoding) : item.getString();
        parameterValues.add(value);
      }
    }

    return uploadException;
  }

  /**
   * After-processing when multipart request is processed and the request data is sorted.
   * 
   * @param parsingSucceeded A Boolean indicating whether there was a problem with parsing the input (e.g. the request
   *          size was greater than allowed).
   * @param input The Aranea input holder.
   * @param output The Aranea output holder.
   * @param parameters The parsed parameters and their values.
   * @param fileItems The parsed file params and their files.
   * @param uploadException The exception from sorting parsed parameters.
   * @throws Various unexpected exceptions.
   * @since 1.2.2
   */
  protected void afterMultipartRequestParsing(boolean parsingSucceeded, InputData input, OutputData output,
      Map<String, List<String>> parameters, Map<String, ? extends FileItem> fileItems, Exception uploadException)
      throws Exception {

    if (parsingSucceeded) {
      input.extend(FileUploadInputExtension.class, new StandardFileUploadInputExtension(fileItems, uploadException));
      ServletUtil.setRequest(input, new MultipartWrapper(ServletUtil.getRequest(input), parameters));
    } else {
      ServletOutputStream out = ServletUtil.getResponse(output).getOutputStream();
      out.print(FAIL_RESPONSE);
    }
  }

  /**
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  @SuppressWarnings("deprecation")
  protected static class MultipartWrapper extends HttpServletRequestWrapper {

    protected Map<String, String[]> parameters = new HashMap<String, String[]>();

    public MultipartWrapper(HttpServletRequest req, Map<String, List<String>> parameterLists) throws Exception {
      super(req);
      for (Map.Entry<String, List<String>> entry : parameterLists.entrySet()) {
        List<String> parameterList = entry.getValue();
        this.parameters.put(entry.getKey(), toStringArray(parameterList.toArray()));
      }
    }

    private String[] toStringArray(Object[] array) throws Exception {
      String[] result = new String[array.length];
      System.arraycopy(array, 0, result, 0, array.length);
      return result;
    }

    @Override
    public String getParameter(String name) {
      String[] result = getParameterValues(name);
      return result == null || result.length == 0 ? null : result[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
      return this.parameters;
    }

    @Override
    public Enumeration<String> getParameterNames() {
      return Collections.enumeration(this.parameters.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
      return this.parameters.get(name);
    }
  }
}

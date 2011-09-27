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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.extension.ExternalResource;
import org.araneaframework.http.extension.ExternalResourceInitializer;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.http.util.URLUtil;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardFileImportFilterService extends BaseFilterService {

  private static final Log LOG = LogFactory.getLog(StandardFileImportFilterService.class);

  private static boolean isInitialized = false;

  private static ExternalResource resources;

  public static final String OVERRIDE_PREFIX = "override";

  public static final String FILE_IMPORTER_NAME = "fileimporter";

  protected long cacheHoldingTime = 3600000;

  private synchronized void initialize() {
    if (!isInitialized) {
      initialize(getEnvironment().requireEntry(ServletConfig.class).getServletContext());
    }
  }

  private synchronized static void initialize(ServletContext context) {
    if (!isInitialized) {
      resources = new ExternalResourceInitializer().getResources(context);
      isInitialized = true;
    }
  }

  public void setCacheHoldingTime(Long cacheHoldingTime) {
    Assert.notNullParam(this, cacheHoldingTime, "cacheHoldingTime");
    this.cacheHoldingTime = cacheHoldingTime;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    initialize();

    HttpInputData input2 = (HttpInputData) input;
    String uri = URLUtil.normalizeURI(input2.getPath());

    if (uri == null || !uri.startsWith(FILE_IMPORTER_NAME)) {
      getChildService()._getService().action(path, input2, output);
      return;
    }

    input2.pushPathPrefix(FILE_IMPORTER_NAME);

    String target = input2.getSimplePath();

    boolean isGroup = target != null && resources.getGroupByName(target) != null;

    HttpServletResponse response = ServletUtil.getResponse(output);
    List<String> filesToLoad = new ArrayList<String>();

    try {
      if (target != null) {
        if (!isGroup && resources.isAllowedFile(target)) {
          setHeaders(response, resources.getContentType(target));
          filesToLoad.add(target);

        } else if (isGroup) {
          Map<String, String> group = resources.getGroupByName(target);

          if (group != null && !group.isEmpty()) {
            Map.Entry<String, String> entry = group.entrySet().iterator().next();
            setHeaders(response, entry.getValue());
            filesToLoad.addAll(group.keySet());
          } else {
            LOG.warn("Unexistent group specified for file importing, " + target);
            throw new FileNotFoundException();
          }
        }

        loadFiles(filesToLoad, response.getOutputStream());
      }
    } catch (FileNotFoundException e) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Imported file or group '" + target + "' not found.");
    }
  }

  private void setHeaders(HttpServletResponse response, String contentType) {
    response.setHeader("Cache-Control", "max-age=" + this.cacheHoldingTime / 1000);
    response.setDateHeader("Expires", System.currentTimeMillis() + this.cacheHoldingTime);
    response.setContentType(contentType);
  }

  private void loadFiles(List<String> files, OutputStream out) throws Exception {
    ServletContext context = getEnvironment().requireEntry(ServletContext.class);

    for (String fileName : files) {
      // first we try load an override
      URL fileURL = resolveFileURL(context, fileName);
      InputStream fileInputStream = URLUtil.getFileStream(fileURL, fileName);

      if (fileInputStream == null) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Unable to locate resource '" + fileName + "'");
        }
        throw new FileNotFoundException("Unable to locate resource '" + fileName + "'");
      }

      try {
        IOUtils.copy(fileInputStream, out);
      } finally {
        IOUtils.closeQuietly(fileInputStream);
      }
    }
  }

  protected URL resolveFileURL(ServletContext context, String fileName) throws MalformedURLException {
    // first we try load an override
    URL fileURL = context.getResource("/" + OVERRIDE_PREFIX + "/" + fileName);
    if (fileURL == null) {
      fileURL = context.getResource("/" + fileName);
    }

    if (fileURL == null) { // fall-back to the original
      fileURL = getClass().getClassLoader().getResource(fileName);
    } else if (LOG.isDebugEnabled()) {
      LOG.debug("Serving override of file '" + fileName + "'" + " from context path resource '" + fileURL.getFile()
          + "'.");
    }

    return fileURL;
  }
}

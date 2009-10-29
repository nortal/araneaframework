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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.extension.ExternalResource;
import org.araneaframework.http.extension.ExternalResourceInitializer;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.http.util.URLUtil;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardFileImportFilterService extends BaseFilterService {

  private static final Log LOG = LogFactory.getLog(StandardFileImportFilterService.class);

  private static boolean isInitialized = false;

  private static ExternalResource resources;

  private static long cacheHoldingTime = 3600000;

  public static final String CACHE_TIME_PARAM = "fileImporterCacheInMillis";

  public static final String IMPORTER_FILE_NAME = "FileImporterFileName";

  public static final String IMPORTER_GROUP_NAME = "FileImporterGroupName";

  public static final String OVERRIDE_PREFIX = "override";

  public static final String FILE_IMPORTER_NAME = "fileimporter";

  synchronized static void initialize(ServletContext context) {
    if (!isInitialized) {
      resources = new ExternalResourceInitializer().getResources(context);

      String cacheTime = context.getInitParameter(CACHE_TIME_PARAM);
      if (StringUtils.isNumeric(cacheTime) && cacheTime.length() > 0) {
        cacheHoldingTime = Long.parseLong(cacheTime);
      }
      isInitialized = true;
    }
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {

    if (!isInitialized) {
      ServletConfig config = getEnvironment().getEntry(ServletConfig.class);
      initialize(config.getServletContext());
    }

    String uri = URLUtil.normalizeURI(((HttpInputData) input).getPath());

    if (uri == null || URLUtil.splitURI(uri).length == 0 || !URLUtil.splitURI(uri)[0].equals(FILE_IMPORTER_NAME)) {
      childService._getService().action(path, input, output);
      return;
    }

    String fileName = input.getGlobalData().get(IMPORTER_FILE_NAME);
    String groupName = input.getGlobalData().get(IMPORTER_GROUP_NAME);

    if (fileName == null) {
      fileName = uri.substring(FILE_IMPORTER_NAME.length() + 1);
      if (resources.getGroupByName(fileName) != null) {
        groupName = fileName;
        fileName = null;
      }
    } else if (groupName == null) {
      groupName = fileName;
    }

    HttpServletResponse response = ServletUtil.getResponse(output);
    List<String> filesToLoad = new ArrayList<String>();
    OutputStream out = response.getOutputStream();

    try {
      if (fileName != null) {
        if (resources.isAllowedFile(fileName)) {
          setHeaders(response, resources.getContentType(fileName));
          filesToLoad.add(fileName);
          loadFiles(filesToLoad, out);
        }
      } else if (groupName != null) {
        Map<String, String> group = resources.getGroupByName(groupName);

        if (group != null && group.size() > 0) {
          Map.Entry<String, String> entry = group.entrySet().iterator().next();
          setHeaders(response, entry.getValue());
          filesToLoad.addAll(group.keySet());
          loadFiles(filesToLoad, out);
        } else {
          LOG.warn("Unexistent group specified for file importing, " + groupName);
          throw new FileNotFoundException();
        }
      }
    } catch (FileNotFoundException e) {
      String notFoundName = fileName == null ? groupName : fileName;
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Imported file or group '" + notFoundName + "' not found.");
    }
  }

  private void setHeaders(HttpServletResponse response, String contentType) {
    response.setHeader("Cache-Control", "max-age=" + (cacheHoldingTime / 1000));
    response.setDateHeader("Expires", System.currentTimeMillis() + cacheHoldingTime);
    response.setContentType(contentType);
  }

  private void loadFiles(List<String> files, OutputStream out) throws Exception {
    ServletContext context = getEnvironment().getEntry(ServletContext.class);

    for (String fileName : files) {
      ClassLoader loader = getClass().getClassLoader();

      // first we try load an override
      URL fileURL = context.getResource("/" + OVERRIDE_PREFIX + "/" + fileName);
      if (fileURL == null) {
        fileURL = context.getResource("/" + fileName);
      }

      if (fileURL == null) {
        // fallback to the original
        fileURL = loader.getResource(fileName);
      } else if (LOG.isDebugEnabled()) {
        LOG.debug("Serving override of file '" + fileName + "'" + " from context path resource '" + fileURL.getFile()
            + "'.");
      }

      FileInputStream fileInputStream = null;
      // fallback to the filesystem
      if (fileURL == null) {
        try {
          fileInputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
          // not being able to load results in FileNotFoundException, see below
        } catch (SecurityException e) {
          // not being able to load results in FileNotFoundException, see below
        }
      }

      if (fileURL == null && fileInputStream == null) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Unable to locate resource '" + fileName + "'");
        }
        throw new FileNotFoundException("Unable to locate resource '" + fileName + "'");
      }

      InputStream inputStream = null;
      try {
        if (fileInputStream != null) {
          inputStream = fileInputStream;
        } else if (fileURL != null) {
          inputStream = fileURL.openStream();
        }

        if (inputStream != null) {
          int length = 0;
          byte[] bytes = new byte[1024];

          do {
            length = inputStream.read(bytes);
            if (length == -1)
              break;
            out.write(bytes, 0, length);
          } while (length != -1);
        }

      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }

    }

  }

}

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

package org.araneaframework.http.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * Various methods to simplify working with URLs.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class URLUtil {

  /**
   * Removes all leading and trailing slashes from the given <code>uri</code>.
   * 
   * @param uri The URI to normalize (may be <code>nul</code>).
   * @return The URI without leading and trailing forward-slashes.
   */
  public static String normalizeURI(String uri) {
    if (uri == null) {
      return null;
    }

    // lose the first slashes
    while (StringUtils.startsWith(uri, "/")) {
      uri = StringUtils.removeStart(uri, "/");
    }

    // lose the last slashes
    while (StringUtils.endsWith(uri, "/")) {
      uri = StringUtils.removeEnd(uri, "/");
    }

    return uri;
  }

  /**
   * Splits the given <code>uri</code> by forward-slashes. The separators won't be included in the result.
   * 
   * @param uri The URI to split (may be <code>null</code>).
   * @return An array of split URIs.
   */
  public static String[] splitURI(String uri) {
    return StringUtils.split(uri, '/');
  }

  /**
   * Parameterizes the given URI, which is expected not to have any parameters yet. The parameters from the map are used
   * to add parameters to the URI (map key becomes parameter name and corresponding map value becomes the parameter
   * value).
   * 
   * @param uri The URI to parameterize. It is taken as-is, but should not contain any parameters yet. May be
   *          <code>null</code>.
   * @param parameters Parameters to add to the URI. Both keys and values will be escaped before added to the URI. May
   *          be <code>null</code> or empty.
   * @return The parameterized URI. At least an empty string (never <code>null</code>), if parameters were empty.
   */
  public static String parameterizeURI(String uri, Map<String, String> parameters) {
    StringBuffer sb = new StringBuffer(StringUtils.defaultString(uri));

    if (parameters != null && parameters.size() > 0) {
      sb.append('?');
      for (Iterator<Map.Entry<String, String>> i = parameters.entrySet().iterator(); i.hasNext();) {
        Map.Entry<String, String> pair = i.next();
        sb.append(encodeURIComponent(pair.getKey())).append('=').append(encodeURIComponent(pair.getValue()));
        if (i.hasNext()) {
          sb.append('&');
        }
      }
    }

    return sb.toString();
  }

  /**
   * Tries to get input stream from:
   * <ol>
   * <li>the <code>fileURL</code>
   * <li>the <code>filePath</code> when the previous failed.
   * </ol>
   * 
   * @param fileURL The URL pointing to a file, which may also be located outside the current machine. May be
   *          <code>null</code>.
   * @param filePath The path to a file. May be <code>null</code>.
   * @return The resolved input stream or <code>null</code>.
   * @since 2.0
   */
  public static final InputStream getFileStream(URL fileURL, String filePath) {
    InputStream result = null;

    if (fileURL != null) {
      try {
        result = fileURL.openStream();
      } catch (Exception e2) {
        // Not being able to load file results in Exception, which is OK and must be ignored.
      }
    }

    if (result == null && filePath != null) {
      try {
        result = new FileInputStream(filePath);
      } catch (Exception e) {
        // Not being able to load file results in Exception, which is OK and must be ignored.
      }
    }

    return result;
  }

  /**
   * A convenient method escape URI components (e.g. parameter value) to use them in a URL.
   * 
   * @param uriComponent The URI component to encode.
   * @return The encoded version of the URI component.
   * @since 2.0
   */
  public static String encodeURIComponent(String uriComponent) {
    try {
      uriComponent = URLEncoder.encode(uriComponent, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      ExceptionUtil.uncheckException(e);
    }

    return uriComponent.replaceAll("\\+", "%20");
  }

}

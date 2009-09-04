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

package org.araneaframework.http.util;

import javax.servlet.ServletRequest;
import org.araneaframework.InputData;
import org.araneaframework.core.Assert;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.filter.StandardFileImportFilterService;

/**
 * Utility methods for converting file names to a form which allows them to be imported with
 * {@link StandardFileImportFilterService}. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract  class FileImportUtil {  

  /**
   * Given a filename, returns the string that can be used for importing via the
   * file importing service {@link StandardFileImportFilterService}. 
   */
  public final static String getImportString(String fileName) {
    Assert.notEmptyParam(fileName, "fileName");

    StringBuffer sb = new StringBuffer("/");
    sb.append(StandardFileImportFilterService.FILE_IMPORTER_NAME);
    sb.append("/");
    sb.append(fileName);
    return sb.toString();
  }

  /**
   * Given a filename, returns the string with absolute URL that can be used 
   * for importing via the file importing service {@link StandardFileImportFilterService}. 
   */
  public final static String getImportString(String fileName, ServletRequest req) {
    return getImportString(fileName, ServletUtil.getInputData(req));
  }

  /**
   * Given a filename, returns the string with absolute URL that can be used 
   * for importing via the file importing service {@link StandardFileImportFilterService}. 
   */
  public final static String getImportString(String fileName, InputData input) {
    Assert.notNullParam(input, "input");
    Assert.notEmptyParam(fileName, "fileName");
    Assert.isInstanceOfParam(HttpInputData.class, input, "input");

    StringBuffer url = new StringBuffer();
    url.append(((HttpInputData) input).getContainerURL());
    url.append(getImportString(fileName));

    return (url.toString());
  }


  /**
   * Given a filename, returns the string with absolute encoded URL that can be used 
   * for importing via the file importing service {@link StandardFileImportFilterService}.
   */
  public final static String getEncodedImportString(String fileName, InputData input) {
    return ((HttpOutputData) input.getOutputData()).encodeURL(getImportString(fileName, input));
  }
}

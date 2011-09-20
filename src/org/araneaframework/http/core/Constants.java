/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.http.core;

/**
 * Some constants and other global data that are used by the compiler and the runtime.
 * <p>
 * Copied from Apache Tomcat for internal use. Use the original version (http://tomcat.apache.org/) if you need it
 * outside Aranea.
 * 
 * @author Anil K. Vijendran
 * @author Harish Prabandham
 * @author Shawn Bayern
 * @author Mark Roth
 */
public class Constants {

  /**
   * Default servlet content type.
   */
  public static final String SERVLET_CONTENT_TYPE = "text/html";

  /**
   * Default size of the JSP buffer.
   */
  public static final int K = 1024;

  public static final int DEFAULT_BUFFER_SIZE = 8 * K;

  /**
   * Default size for the tag buffers.
   */
  public static final int DEFAULT_TAG_BUFFER_SIZE = 512;

  /**
   * Default tag handler pool size.
   */
  public static final int MAX_POOL_SIZE = 5;

  /**
   * Servlet context and request attributes that the JSP engine uses.
   */
  public static final String INC_REQUEST_URI = "javax.servlet.include.request_uri";

  public static final String INC_SERVLET_PATH = "javax.servlet.include.servlet_path";

  public static final String TMP_DIR = "javax.servlet.context.tempdir";

  public static final String FORWARD_SEEN = "javax.servlet.forward.seen";

  /**
   * Public Id and the Resource path (of the cached copy) of the DTDs for tag library descriptors.
   */
  public static final String TAGLIB_DTD_PUBLIC_ID_11 = "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN";

  public static final String TAGLIB_DTD_RESOURCE_PATH_11 = "/cache/web-jsptaglibrary_1_1.dtd";

  public static final String TAGLIB_DTD_PUBLIC_ID_12 = "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN";

  public static final String TAGLIB_DTD_RESOURCE_PATH_12 = "/cache/web-jsptaglibrary_1_2.dtd";

  /**
   * Public Id and the Resource path (of the cached copy) of the DTDs for web application deployment descriptors
   */
  public static final String WEBAPP_DTD_PUBLIC_ID_22 = "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN";

  public static final String WEBAPP_DTD_RESOURCE_PATH_22 = "/cache/web-app_2_2.dtd";

  public static final String WEBAPP_DTD_PUBLIC_ID_23 = "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN";

  public static final String WEBAPP_DTD_RESOURCE_PATH_23 = "/cache/web-app_2_3.dtd";

  /**
   * List of the Public IDs that we cache, and their associated location. This is used by an EntityResolver to return
   * the location of the cached copy of a DTD.
   */
  public static final String[] CACHED_DTD_PUBLIC_IDS = { TAGLIB_DTD_PUBLIC_ID_11, TAGLIB_DTD_PUBLIC_ID_12,
      WEBAPP_DTD_PUBLIC_ID_22, WEBAPP_DTD_PUBLIC_ID_23, };

  public static final String[] CACHED_DTD_RESOURCE_PATHS = { TAGLIB_DTD_RESOURCE_PATH_11, TAGLIB_DTD_RESOURCE_PATH_12,
      WEBAPP_DTD_RESOURCE_PATH_22, WEBAPP_DTD_RESOURCE_PATH_23, };
}

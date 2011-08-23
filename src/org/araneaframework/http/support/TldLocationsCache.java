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

package org.araneaframework.http.support;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.core.exception.AraneaRuntimeException;

/**
 * A container for all tag libraries that are defined "globally" for the web application.
 * 
 * Tag Libraries can be defined globally in one of two ways:
 * <ol>
 * <li>Via &lt;taglib&gt; elements in web.xml: the URI and location of the tag-library are specified in the
 * &lt;taglib&gt; element.</li>
 * <li>Via packaged jar files that contain .tld files within the META-INF directory, or some sub-directory of it. The
 * TagLib is 'global' if it has the &lt;uri&gt; element defined.</li>
 * </ol>
 * 
 * A mapping between the taglib URI and its associated TaglibraryInfoImpl is maintained in this container. Actually,
 * that's what we'd like to do. However, because of the way the classes TagLibraryInfo and TagInfo have been defined, it
 * is not currently possible to share an instance of TagLibraryInfo across page invocations. A bug has been submitted to
 * the spec lead. In the mean time, all we do is save the 'location' where the TLD associated with a taglib URI can be
 * found.
 * 
 * When a JSP page has a taglib directive, the mappings in this container are first searched (see method getLocation()).
 * If a mapping is found, then the location of the TLD is returned. If no mapping is found, then the uri specified in
 * the taglib directive is to be interpreted as the location for the TLD of this tag library.
 * 
 * @author Pierre Delisle
 * @author Jan Luehe
 */
public class TldLocationsCache {

  // Logger
  private static final Log LOG = LogFactory.getLog(TldLocationsCache.class);

  /**
   * The types of URI one may specify for a tag library
   */
  public static final int ABS_URI = 0;

  public static final int ROOT_REL_URI = 1;

  public static final int NOROOT_REL_URI = 2;

  private static final String WEB_XML = "/WEB-INF/web.xml";

  private static final String FILE_PROTOCOL = "file:";

  private static final String JAR_FILE_SUFFIX = ".jar";

  // Names of JARs that are known not to contain any TLDs
  private static HashSet<String> noTldJars;

  /**
   * The mapping of the 'global' tag library URI to the location (resource path) of the TLD associated with that tag
   * library. The location is returned as a String array: [0] The location [1] If the location is a jar file, this is
   * the location of the TLD.
   */
  private Hashtable<String, String[]> mappings;

  private boolean initialized;

  private ServletContext ctxt;

  private boolean redeployMode;

  private static TldLocationsCache instance;

  public static synchronized TldLocationsCache getInstance(ServletContext ctxt) {
    if (TldLocationsCache.instance == null) {
      TldLocationsCache.instance = new TldLocationsCache(ctxt);
    }
    return TldLocationsCache.instance;
  }

  // *********************************************************************
  // Constructor and Initializations

  /*
   * Initializes the set of JARs that are known not to contain any TLDs
   */
  static {
    TldLocationsCache.noTldJars = new HashSet<String>();
    TldLocationsCache.noTldJars.add("ant.jar");
    TldLocationsCache.noTldJars.add("catalina.jar");
    TldLocationsCache.noTldJars.add("catalina-ant.jar");
    TldLocationsCache.noTldJars.add("catalina-cluster.jar");
    TldLocationsCache.noTldJars.add("catalina-optional.jar");
    TldLocationsCache.noTldJars.add("catalina-i18n-fr.jar");
    TldLocationsCache.noTldJars.add("catalina-i18n-ja.jar");
    TldLocationsCache.noTldJars.add("catalina-i18n-es.jar");
    TldLocationsCache.noTldJars.add("commons-dbcp.jar");
    TldLocationsCache.noTldJars.add("commons-modeler.jar");
    TldLocationsCache.noTldJars.add("commons-logging-api.jar");
    TldLocationsCache.noTldJars.add("commons-beanutils.jar");
    TldLocationsCache.noTldJars.add("commons-fileupload-1.0.jar");
    TldLocationsCache.noTldJars.add("commons-pool.jar");
    TldLocationsCache.noTldJars.add("commons-digester.jar");
    TldLocationsCache.noTldJars.add("commons-logging.jar");
    TldLocationsCache.noTldJars.add("commons-collections.jar");
    TldLocationsCache.noTldJars.add("commons-el.jar");
    TldLocationsCache.noTldJars.add("jakarta-regexp-1.2.jar");
    TldLocationsCache.noTldJars.add("jasper-compiler.jar");
    TldLocationsCache.noTldJars.add("jasper-runtime.jar");
    TldLocationsCache.noTldJars.add("jmx.jar");
    TldLocationsCache.noTldJars.add("jmx-tools.jar");
    TldLocationsCache.noTldJars.add("jsp-api.jar");
    TldLocationsCache.noTldJars.add("jkshm.jar");
    TldLocationsCache.noTldJars.add("jkconfig.jar");
    TldLocationsCache.noTldJars.add("naming-common.jar");
    TldLocationsCache.noTldJars.add("naming-resources.jar");
    TldLocationsCache.noTldJars.add("naming-factory.jar");
    TldLocationsCache.noTldJars.add("naming-java.jar");
    TldLocationsCache.noTldJars.add("servlet-api.jar");
    TldLocationsCache.noTldJars.add("servlets-default.jar");
    TldLocationsCache.noTldJars.add("servlets-invoker.jar");
    TldLocationsCache.noTldJars.add("servlets-common.jar");
    TldLocationsCache.noTldJars.add("servlets-webdav.jar");
    TldLocationsCache.noTldJars.add("tomcat-util.jar");
    TldLocationsCache.noTldJars.add("tomcat-http11.jar");
    TldLocationsCache.noTldJars.add("tomcat-jni.jar");
    TldLocationsCache.noTldJars.add("tomcat-jk.jar");
    TldLocationsCache.noTldJars.add("tomcat-jk2.jar");
    TldLocationsCache.noTldJars.add("tomcat-coyote.jar");
    TldLocationsCache.noTldJars.add("xercesImpl.jar");
    TldLocationsCache.noTldJars.add("xmlParserAPIs.jar");
    TldLocationsCache.noTldJars.add("xml-apis.jar");
    // JARs from J2SE runtime
    TldLocationsCache.noTldJars.add("sunjce_provider.jar");
    TldLocationsCache.noTldJars.add("ldapsec.jar");
    TldLocationsCache.noTldJars.add("localedata.jar");
    TldLocationsCache.noTldJars.add("dnsns.jar");
  }

  private TldLocationsCache(ServletContext ctxt) {
    this(ctxt, true);
  }

  /**
   * Constructor.
   * 
   * @param ctxt the servlet context of the web application in which Jasper is running
   * @param redeployMode if true, then the compiler will allow redeploying a tag library from the same jar, at the
   *          expense of slowing down the server a bit. Note that this may only work on JDK 1.3.1_01a and later, because
   *          of JDK bug 4211817 fixed in this release. If redeployMode is false, a faster but less capable mode will be
   *          used.
   */
  private TldLocationsCache(ServletContext ctxt, boolean redeployMode) {
    this.ctxt = ctxt;
    this.redeployMode = redeployMode;
    this.mappings = new Hashtable<String, String[]>();
    this.initialized = false;
  }

  /**
   * Sets the list of JARs that are known not to contain any TLDs.
   * 
   * @param jarNames List of comma-separated names of JAR files that are known not to contain any TLDs
   */
  public static void setNoTldJars(String jarNames) {
    if (jarNames != null) {
      TldLocationsCache.noTldJars.clear();
      StringTokenizer tokenizer = new StringTokenizer(jarNames, ",");
      while (tokenizer.hasMoreElements()) {
        TldLocationsCache.noTldJars.add(tokenizer.nextToken());
      }
    }
  }

  /**
   * Gets the 'location' of the TLD associated with the given taglib 'uri'.
   * 
   * Returns null if the URI is not associated with any tag library 'exposed' in the web application. A tag library is
   * 'exposed' either explicitly in web.xml or implicitly via the &lt;uri&gt; tag in the TLD of a TagLib deployed in a
   * jar file (WEB-INF/lib).
   * 
   * @param uri The TagLib URI.
   * 
   * @return An array of two Strings: The first element denotes the real path to the TLD. If the path to the TLD points
   *         to a jar file, then the second element denotes the name of the TLD entry in the jar file. Returns null if
   *         the URI is not associated with any tag library 'exposed' in the web application.
   */
  public String[] getLocation(String uri) throws AraneaRuntimeException {
    if (!this.initialized) {
      init();
    }
    return this.mappings.get(uri);
  }

  /**
   * Returns the type of a URI: ABS_URI ROOT_REL_URI NOROOT_REL_URI
   */
  public static int uriType(String uri) {
    if (uri.indexOf(':') != -1) {
      return TldLocationsCache.ABS_URI;
    } else if (uri.startsWith("/")) {
      return TldLocationsCache.ROOT_REL_URI;
    } else {
      return TldLocationsCache.NOROOT_REL_URI;
    }
  }

  private void init() throws AraneaRuntimeException {
    if (this.initialized) {
      return;
    }

    try {
      processWebDotXml();
      scanJars();
      processTldsInFileSystem("/WEB-INF/");
      this.initialized = true;
    } catch (Exception ex) {
      throw new AraneaRuntimeException(ex);
    }
  }

  /*
   * Populates taglib map described in web.xml.
   */
  private void processWebDotXml() throws Exception {

    InputStream is = null;

    try {
      is = this.ctxt.getResourceAsStream(TldLocationsCache.WEB_XML);
      if (is == null && TldLocationsCache.LOG.isWarnEnabled()) {
        TldLocationsCache.LOG.warn("jsp.error.internal.filenotfound" + TldLocationsCache.WEB_XML);
      }

      if (is == null) {
        return;
      }

      // Parse the web application deployment descriptor
      TreeNode webtld = null;
      // altDDName is the absolute path of the DD
      webtld = new ParserUtils().parseXMLDocument(TldLocationsCache.WEB_XML, is);

      // Allow taglib to be an element of the root or jsp-config (JSP2.0)
      TreeNode jspConfig = webtld.findChild("jsp-config");
      if (jspConfig != null) {
        webtld = jspConfig;
      }
      Iterator<TreeNode> taglibs = webtld.findChildren("taglib");
      while (taglibs.hasNext()) {

        // Parse the next <taglib> element
        TreeNode taglib = taglibs.next();
        String tagUri = null;
        String tagLoc = null;
        TreeNode child = taglib.findChild("taglib-uri");
        if (child != null) {
          tagUri = child.getBody();
        }
        child = taglib.findChild("taglib-location");
        if (child != null) {
          tagLoc = child.getBody();
        }

        // Save this location if appropriate
        if (tagLoc == null) {
          continue;
        }
        if (uriType(tagLoc) == TldLocationsCache.NOROOT_REL_URI) {
          tagLoc = "/WEB-INF/" + tagLoc;
        }
        String tagLoc2 = null;
        if (tagLoc.endsWith(TldLocationsCache.JAR_FILE_SUFFIX)) {
          tagLoc = this.ctxt.getResource(tagLoc).toString();
          tagLoc2 = "META-INF/taglib.tld";
        }
        this.mappings.put(tagUri, new String[] { tagLoc, tagLoc2 });
      }
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (Throwable t) {}
      }
    }
  }

  /**
   * Scans the given JarURLConnection for TLD files located in META-INF (or a sub-directory of it), adding an implicit
   * map entry to the TagLib map for any TLD that has a &lt;uri&gt; element.
   * 
   * @param conn The JarURLConnection to the JAR file to scan
   * @param ignore true if any exceptions raised when processing the given JAR should be ignored, false otherwise
   */
  private void scanJar(JarURLConnection conn, boolean ignore) throws AraneaRuntimeException {
    JarFile jarFile = null;
    String resourcePath = conn.getJarFileURL().toString();

    try {
      if (this.redeployMode) {
        conn.setUseCaches(false);
      }

      jarFile = conn.getJarFile();

      for (JarEntry entry : Collections.list(jarFile.entries())) {
        String name = entry.getName();
        if (!name.startsWith("META-INF/") || !name.endsWith(".tld")) {
          continue;
        }

        InputStream stream = jarFile.getInputStream(entry);
        try {
          String uri = getUriFromTld(stream);
          // Add implicit map entry only if its URI is not already present in the map:
          if (!StringUtils.isEmpty(uri) && this.mappings.get(uri) == null) {
            this.mappings.put(uri, new String[] { resourcePath, name });
          }
        } finally {
          IOUtils.closeQuietly(stream);
        }
      }

    } catch (Exception ex) {
      if (!this.redeployMode) {
        // If not in re-deploy mode, close the jar in case of an error:
        if (jarFile != null) {
          try {
            jarFile.close();
          } catch (Throwable t) {}
        }
      }

      if (!ignore) {
        throw new AraneaRuntimeException(ex);
      }

    } finally {
      if (this.redeployMode) {
        // if in re-deploy mode, always close the jar
        if (jarFile != null) {
          try {
            jarFile.close();
          } catch (Throwable t) {}
        }
      }
    }
  }

  /*
   * Searches the filesystem under /WEB-INF for any TLD files, and adds an implicit map entry to the taglib map for any
   * TLD that has a <uri> element.
   */
  @SuppressWarnings("unchecked")
  private void processTldsInFileSystem(String startPath) throws Exception {

    Set<String> dirList = this.ctxt.getResourcePaths(startPath);
    if (dirList != null) {
      for (String path : dirList) {
        if (path.endsWith("/")) {
          processTldsInFileSystem(path);
        }

        if (!path.endsWith(".tld")) {
          continue;
        }

        InputStream stream = this.ctxt.getResourceAsStream(path);
        String uri = null;
        try {
          uri = getUriFromTld(stream);
        } finally {
          IOUtils.closeQuietly(stream);
        }

        // Add implicit map entry only if its URI is not already present in the map:
        if (!StringUtils.isEmpty(uri) && this.mappings.get(uri) == null) {
          this.mappings.put(uri, new String[] { path, null });
        }
      }
    }
  }

  /*
   * Returns the value of the URI element of the given TLD, or null if the given TLD does not contain any such element.
   */
  private String getUriFromTld(InputStream in) throws IOException {
    // Parse the tag library descriptor at the specified resource path
    String result = IOUtils.toString(in);
    IOUtils.closeQuietly(in);

    Matcher m = Pattern.compile("<uri>(.*)</uri>").matcher(result);
    if (m.find() && m.groupCount() == 1) {
      return m.group(1);
    } else {
      return null;
    }
  }

  /*
   * Scans all JARs accessible to the webapp's class-loader and its parent class-loaders for TLDs.
   * 
   * The list of JARs always includes the JARs under WEB-INF/lib, as well as all shared JARs in the class-loader
   * delegation chain of the webapp's class-loader.
   * 
   * Considering JARs in the class-loader delegation chain constitutes a Tomcat-specific extension to the TLD search
   * order defined in the JSP spec. It allows tag libraries packaged as JAR files to be shared by web applications by
   * simply dropping them in a location that all web applications have access to (e.g., <CATALINA_HOME>/common/lib).
   * 
   * The set of shared JARs to be scanned for TLDs is narrowed down by the <tt>noTldJars</tt> class variable, which
   * contains the names of JARs that are known not to contain any TLDs.
   */
  private void scanJars() throws Exception {

    ClassLoader webappLoader = Thread.currentThread().getContextClassLoader();
    ClassLoader loader = webappLoader.getParent();

    scanPathForJars("/WEB-INF/lib/");

    while (loader != null) {
      if (loader instanceof URLClassLoader) {
        URL[] urls = ((URLClassLoader) loader).getURLs();
        for (URL url : urls) {
          URLConnection conn = url.openConnection();
          if (conn instanceof JarURLConnection) {
            if (needScanJar(loader, webappLoader, ((JarURLConnection) conn).getJarFile().getName())) {
              scanJar((JarURLConnection) conn, true);
            }
          } else {
            String urlStr = url.toString();
            if (urlStr.startsWith(TldLocationsCache.FILE_PROTOCOL)
                && urlStr.endsWith(TldLocationsCache.JAR_FILE_SUFFIX) && needScanJar(loader, webappLoader, urlStr)) {
              URL jarURL = new URL("jar:" + urlStr + "!/");
              scanJar((JarURLConnection) jarURL.openConnection(), true);
            }
          }
        }
      }

      loader = loader.getParent();
    }
  }

  @SuppressWarnings("unchecked")
  private void scanPathForJars(String path) throws Exception {
    Set<String> paths = this.ctxt.getResourcePaths(path);

    // nothing on this path
    if (paths == null) {
      return;
    }

    for (String entry : paths) {
      if (entry.charAt(entry.length() - 1) == '/') {
        scanPathForJars(entry);
      } else {
        URL entryUrl = this.ctxt.getResource(entry);

        // Will this work if the WebApp is packaged in a war?
        // Just have to check it...
        if (entryUrl.getProtocol().equals("file")) {
          entryUrl = new URL("jar:" + entryUrl.toExternalForm() + "!/");
        }

        URLConnection con = entryUrl.openConnection();

        if (con instanceof JarURLConnection) {
          scanJar((JarURLConnection) con, true);
        }
      }
    }
  }

  /*
   * Determines if the JAR file with the given <tt>jarPath</tt> needs to be scanned for TLDs.
   * 
   * @param loader The current class loader in the parent chain @param webappLoader The webapp class loader
   * 
   * @param jarPath The JAR file path
   * 
   * @return TRUE if the JAR file identified by <tt>jarPath</tt> needs to be scanned for TLDs, FALSE otherwise
   */
  private boolean needScanJar(ClassLoader loader, ClassLoader webappLoader, String jarPath) {
    if (loader == webappLoader) {
      // JARs under WEB-INF/lib must be scanned unconditionally according to the spec.
      return true;
    } else {
      String jarName = jarPath;
      int slash = jarPath.lastIndexOf('/');
      if (slash >= 0) {
        jarName = jarPath.substring(slash + 1);
      }
      return (!TldLocationsCache.noTldJars.contains(jarName));
    }
  }
}

/*
 * 
 */
package org.araneaframework.http.support;

import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.http.core.Constants;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CachingEntityResolver implements EntityResolver {
  public static final CachingEntityResolver INSTANCE = new CachingEntityResolver();
  
  private CachingEntityResolver() {}
  
  // Logger
  private static final Log LOG = LogFactory.getLog(CachingEntityResolver.class);

  public InputSource resolveEntity(String publicId, String systemId)
  throws SAXException {   
    for (int i = 0; i < Constants.CACHED_DTD_PUBLIC_IDS.length; i++) {
      String cachedDtdPublicId = Constants.CACHED_DTD_PUBLIC_IDS[i];
      if (cachedDtdPublicId.equals(publicId)) {
        String resourcePath = Constants.CACHED_DTD_RESOURCE_PATHS[i];
        InputStream input = this.getClass().getResourceAsStream(
            resourcePath);
        if (input == null) {
          throw new SAXException("Resource not found: '" + resourcePath + "'!");
        }
        InputSource isrc = new InputSource(input);
        return isrc;
      }
    }
    if (LOG.isDebugEnabled())
      LOG.debug("Entity resolving failed '" + publicId + " " + systemId + "'.");
    return null;
  }
  
  public static CachingEntityResolver getInstance() {
    return INSTANCE;
  }
}
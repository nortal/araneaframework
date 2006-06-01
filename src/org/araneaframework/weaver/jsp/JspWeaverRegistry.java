package org.araneaframework.weaver.jsp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.araneaframework.weaver.WeaverElement;
import org.araneaframework.weaver.WeaverFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 *
 */
public class JspWeaverRegistry implements WeaverFactory{
  private ServletContext srvCtx;
  
  private TldLocationsCache tldLocationsCache;
  private Map taglibs = new HashMap();
  
  public JspWeaverRegistry(ServletContext srvCtx) {
    this.srvCtx = srvCtx;
    
    tldLocationsCache = new TldLocationsCache(srvCtx);
  }
  
  public WeaverElement buildElement(Element el) {
    Map tagMap = getTagMap(el.getNamespaceURI());

    if (tagMap == null) return null;
    
    if (!tagMap.containsKey(el.getLocalName()))
      throw new RuntimeException("Tag not found");
    
    return new JspTagWeaverElement(el, (TagInfo) tagMap.get(el.getLocalName()));
  }
  
  private Map getTagMap(String uri) {
    if (!taglibs.containsKey(uri)) {
      String[] locations = tldLocationsCache.getLocation(uri);

      if (locations != null) {
        String tldLoc = locations[1] == null ? locations[0] : locations[1];
        taglibs.put(uri, readTldMapping(tldLoc));
      }
    }

    return (Map) taglibs.get(uri);
  }

  private Map readTldMapping(String location) {
    Map result = new HashMap();

    InputStream tldStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document tldDoc = null;
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      tldDoc = builder.parse(tldStream);
    }
    catch (ParserConfigurationException e) {
      throw new NestableRuntimeException(e);
    }
    catch (SAXException e) {
      throw new NestableRuntimeException(e);
    }
    catch (IOException e) {
      throw new NestableRuntimeException(e);
    }

    NodeList tagElements = tldDoc.getDocumentElement().getElementsByTagName("tag");

    for (int i = 0; i < tagElements.getLength(); i++) {
      TagInfo tagInfo = TagInfo.readTagInfo((Element) tagElements.item(i));

      result.put(tagInfo.getTagName(), tagInfo);
    }

    return result;
  }
}

package org.araneaframework.jsp.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.araneaframework.jsp.util.UiUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LightweightJspServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger(LightweightJspServlet.class);
  
  private TldLocationsCache tldLocationsCache;

  // URI -> Map<TagInfo>
  private Map taglibs = new HashMap();

  public void init() throws ServletException {
    super.init();

    tldLocationsCache = new TldLocationsCache(getServletContext());
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      service(req, resp);
    }
    catch (Exception e) {
      throw new ServletException(e.getMessage(), e);
    }
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      service(req, resp);
    }
    catch (Exception e) {
      throw new ServletException(e.getMessage(), e);
    }
  }

  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    /*
     * Check to see if the requested JSP has been the target of a RequestDispatcher.include()
     */
    String jspUri = (String) req.getAttribute("javax.servlet.include.servlet_path");
    if (jspUri != null) {
      /*
       * Requested JSP has been target of RequestDispatcher.include(). Its path is assembled from the relevant
       * javax.servlet.include.* request attributes
       */
      String pathInfo = (String) req.getAttribute("javax.servlet.include.path_info");
      if (pathInfo != null) {
        jspUri += pathInfo;
      }
    }
    else {
      /*
       * Requested JSP has not been the target of a RequestDispatcher.include(). Reconstruct its path from the request's
       * getServletPath() and getPathInfo()
       */
      jspUri = req.getServletPath();
      String pathInfo = req.getPathInfo();
      if (pathInfo != null) {
        jspUri += pathInfo;
      }
    }

    log.debug("Evaluating JSP DOM from path: " + jspUri);
    
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);

      DocumentBuilder builder = factory.newDocumentBuilder();

      Document jspDom = builder.parse(getServletContext().getResourceAsStream(jspUri));

      PageContext pageContext = new PageContextImpl();
      pageContext.initialize(this, req, resp, null, true, 8096, true);
      processElement(jspDom.getDocumentElement(), pageContext.getOut(), pageContext);
      pageContext.release();
    }
    catch (Exception e) {
      if (ExceptionUtils.getRootCause(e) != null) 
        log.error("Exception root cause:", ExceptionUtils.getRootCause(e));
      else
        log.error("Exception:", e);
      throw new ServletException(e.getMessage(), e);
    }
  }

  private void processElement(Element el, Writer w, PageContext pageContext) throws IOException, JspException,
      ServletException {
    // Here will go specialized renderers
    if (el.getNamespaceURI() != null) {
      processJspTagElement(el, w, pageContext);
      return;
    }

    UiUtil.writeOpenStartTag(w, el.getTagName());
    NamedNodeMap attrs = el.getAttributes();
    for (int i = 0; i < attrs.getLength(); i++) {
      Attr attr = (Attr) attrs.item(i);
      UiUtil.writeAttribute(w, attr.getName(), attr.getValue());
    }
    UiUtil.writeCloseStartTag_SS(w);

    processChildren(el, w, pageContext);

    UiUtil.writeEndTag(w, el.getTagName());
  }

  private void processChildren(Element el, Writer w, PageContext pageContext) throws IOException, JspException,
      ServletException {
    NodeList children = el.getChildNodes();

    for (int i = 0; i < children.getLength(); i++) {
      if (children.item(i) instanceof Comment)
        ;
      else if (children.item(i) instanceof CharacterData)
        w.write(((CharacterData) children.item(i)).getNodeValue());
      else if (children.item(i) instanceof Element) processElement((Element) children.item(i), w, pageContext);
    }
  }

  private void processJspTagElement(Element el, Writer w, PageContext pageContext) throws IOException, JspException,
      ServletException {
    Map tagMap = getTagMap(el.getNamespaceURI());

    if ("http://java.sun.com/JSP/Page".equals(el.getNamespaceURI()) && "include".equals(el.getLocalName())) {
      pageContext.include(el.getAttribute("page"));
    }

    if (tagMap == null) {
      processChildren(el, w, pageContext);
      return;
    }

    TagInfo tagInfo = (TagInfo) tagMap.get(el.getLocalName());
    Class tagClass = null;

    try {
      tagClass = Thread.currentThread().getContextClassLoader().loadClass(tagInfo.getTagClassName());
    }
    catch (ClassNotFoundException e) {
      throw new NestableRuntimeException(e);
    }

    Tag tag = null;
    try {
      tag = (Tag) tagClass.newInstance();
    }
    catch (InstantiationException e) {
      throw new NestableRuntimeException(e);
    }
    catch (IllegalAccessException e) {
      throw new NestableRuntimeException(e);
    }
    try {
      tag.setPageContext(pageContext);
      tag.setParent(null);

      for (int i = 0; i < el.getAttributes().getLength(); i++) {
        Attr attr = (Attr) el.getAttributes().item(i);

        AttrInfo attrInfo = (AttrInfo) tagInfo.getAttributes().get(attr.getLocalName());
        Class attrType;
        try {
          attrType = Thread.currentThread().getContextClassLoader().loadClass(attrInfo.getType());
          Method attrMethod = tagClass.getMethod("set" + attrInfo.getName().substring(0, 1).toUpperCase()
              + attrInfo.getName().substring(1), new Class[] { attrType });
          attrMethod.invoke(tag, new Object[] { attr.getValue() });
        }
        catch (ClassNotFoundException e) {
          throw new NestableRuntimeException(e);
        }
        catch (SecurityException e) {
          throw new NestableRuntimeException(e);
        }
        catch (NoSuchMethodException e) {
          throw new NestableRuntimeException(e);
        }
        catch (IllegalArgumentException e) {
          throw new NestableRuntimeException(e);
        }
        catch (IllegalAccessException e) {
          throw new NestableRuntimeException(e);
        }
        catch (InvocationTargetException e) {
          throw new NestableRuntimeException(e);
        }
      }

      int result = tag.doStartTag();
      
      if (result != Tag.SKIP_BODY) {        
        processChildren(el, w, pageContext);
      }
      
      if (tag instanceof IterationTag) {
        result = ((IterationTag) tag).doAfterBody();
        while (result == IterationTag.EVAL_BODY_AGAIN) {          
          processChildren(el, w, pageContext);
          result = ((IterationTag) tag).doAfterBody();
        }
        
      }

      tag.doEndTag();
    }
    // catch (Exception e) {

    // if (tag instanceof TryCatchFinally)
    // ((TryCatchFinally) tag).doCatch(e);
    // }
    finally {
      if (tag instanceof TryCatchFinally) ((TryCatchFinally) tag).doFinally();
      
      tag.release();
    }
    
    tag.release();
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

  public static class TagInfo implements Serializable {
    protected String tagName;

    protected String tagClassName;

    protected Map attributes = new HashMap();

    public String getTagClassName() {
      return tagClassName;
    }

    public String getTagName() {
      return tagName;
    }

    public static TagInfo readTagInfo(Element tagElement) {
      TagInfo tag = new TagInfo();

      NodeList nameElements = tagElement.getElementsByTagName("name");
      Element nameElement = (Element) nameElements.item(0);
      tag.tagName = nameElement.getFirstChild().getNodeValue();

      NodeList classElements = tagElement.getElementsByTagName("tag-class");
      Element classElement = (Element) classElements.item(0);
      tag.tagClassName = classElement.getFirstChild().getNodeValue();

      NodeList attrElements = tagElement.getElementsByTagName("attribute");

      for (int j = 0; j < attrElements.getLength(); j++) {
        Element attributeEl = (Element) attrElements.item(j);
        AttrInfo attrInfo = AttrInfo.readAttrInfo(attributeEl);
        tag.attributes.put(attrInfo.getName(), attrInfo);
      }

      return tag;
    }

    public Map getAttributes() {
      return attributes;
    }
  }

  public static class AttrInfo implements Serializable {
    protected String name;

    protected String type;

    public static AttrInfo readAttrInfo(Element attributeEl) {
      AttrInfo attr = new AttrInfo();

      attr.name = ((Element) attributeEl.getElementsByTagName("name").item(0)).getFirstChild().getNodeValue();
      if (attributeEl.getElementsByTagName("type").getLength() == 0)
        attr.type = "java.lang.String";
      else
        attr.type = ((Element) attributeEl.getElementsByTagName("type").item(0)).getFirstChild().getNodeValue();

      return attr;
    }

    public String getName() {
      return name;
    }

    public String getType() {
      return type;
    }
  }
}

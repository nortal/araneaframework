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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * XML parsing utilities for processing web application deployment descriptor and tag library descriptor files. FIXME -
 * make these use a separate class loader for the parser to be used.
 * 
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */

public class ParserUtils {

  /**
   * An error handler for use when parsing XML documents.
   */
  static ErrorHandler errorHandler = new MyErrorHandler();

  /**
   * An entity resolver for use when parsing XML documents.
   */
  static EntityResolver entityResolver = CachingEntityResolver.getInstance();

  // Turn off for JSP 2.0 until switch over to using xschema.
  public static boolean validating = false;

  // --------------------------------------------------------- Public Methods

  /**
   * Parse the specified XML document, and return a <code>TreeNode</code> that corresponds to the root node of the
   * document tree.
   * 
   * @param uri URI of the XML document being parsed
   * @param is Input source containing the deployment descriptor
   * @exception AraneaRuntimeException if an input/output error occurs
   * @exception AraneaRuntimeException if a parsing error occurs
   */
  public TreeNode parseXMLDocument(String uri, InputSource is) throws AraneaRuntimeException {

    Document document = null;

    // Perform an XML parse of this document, via JAXP
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setValidating(validating);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setEntityResolver(entityResolver);
      builder.setErrorHandler(errorHandler);
      document = builder.parse(is);
    } catch (ParserConfigurationException ex) {
      throw new AraneaRuntimeException("jsp.error.parse.xml", ex);
    } catch (SAXParseException ex) {
      throw new AraneaRuntimeException("jsp.error.parse.xml.line", ex);
    } catch (SAXException sx) {
      throw new AraneaRuntimeException("jsp.error.parse.xml", sx);
    } catch (IOException io) {
      throw new AraneaRuntimeException("jsp.error.parse.xml", io);
    }

    // Convert the resulting document to a graph of TreeNodes
    return convert(null, document.getDocumentElement());
  }

  /**
   * Parse the specified XML document, and return a <code>TreeNode</code> that corresponds to the root node of the
   * document tree.
   * 
   * @param uri URI of the XML document being parsed
   * @param is Input stream containing the deployment descriptor
   * @exception AraneaRuntimeException if an input/output error occurs
   * @exception AraneaRuntimeException if a parsing error occurs
   */
  public TreeNode parseXMLDocument(String uri, InputStream is) throws AraneaRuntimeException {

    return parseXMLDocument(uri, new InputSource(is));
  }

  // ------------------------------------------------------ Protected Methods

  /**
   * Create and return a TreeNode that corresponds to the specified Node, including processing all of the attributes and
   * children nodes.
   * 
   * @param parent The parent TreeNode (if any) for the new TreeNode
   * @param node The XML document Node to be converted
   */
  protected TreeNode convert(TreeNode parent, Node node) {

    // Construct a new TreeNode for this node
    TreeNode treeNode = new TreeNode(node.getNodeName(), parent);

    // Convert all attributes of this node
    NamedNodeMap attributes = node.getAttributes();
    if (attributes != null) {
      int n = attributes.getLength();
      for (int i = 0; i < n; i++) {
        Node attribute = attributes.item(i);
        treeNode.addAttribute(attribute.getNodeName(), attribute.getNodeValue());
      }
    }

    // Create and attach all children of this node
    NodeList children = node.getChildNodes();
    if (children != null) {
      int n = children.getLength();
      for (int i = 0; i < n; i++) {
        Node child = children.item(i);
        if (child instanceof Comment) {
          continue;
        }
        if (child instanceof Text) {
          String body = ((Text) child).getData();
          if (body != null) {
            body = body.trim();
            if (body.length() > 0) {
              treeNode.setBody(body);
            }
          }
        } else {
          convert(treeNode, child);
        }
      }
    }

    // Return the completed TreeNode graph
    return treeNode;
  }
}

// ------------------------------------------------------------ Private Classes

class MyErrorHandler implements ErrorHandler {

  // Logger
  private static final Log log = LogFactory.getLog(MyErrorHandler.class);

  public void warning(SAXParseException ex) throws SAXException {
    if (log.isDebugEnabled()) {
      log.debug("ParserUtils: warning ", ex);
      // We ignore warnings
    }
  }

  public void error(SAXParseException ex) throws SAXException {
    throw ex;
  }

  public void fatalError(SAXParseException ex) throws SAXException {
    throw ex;
  }
}

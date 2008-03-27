package org.araneaframework.jsp.tag.form;


/**
 * System form tag. System form maps into HTML form. 
 * Canonical use in Aranea - surrounding all body elements. 
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "portletSystemForm"
 *   description = "Puts an HTML <i>form</i> tag with parameters needed by Aranea."
 */
public class AraneaPortletSystemFormHtmlTag extends AraneaSystemFormHtmlTag {
  protected String encodeFormAction(String url) throws Exception {
    return replaceAmpEntitities(super.encodeFormAction(replaceAmpEntitities(url)));
    //return URLEncoder.encode(super.encodeFormAction(url), "UTF-8");
    //return super.encodeFormAction(url);
  }

  public static String replaceAmpEntitities(String url) {
    return url.replaceAll("&amp;", "&");
  }
}

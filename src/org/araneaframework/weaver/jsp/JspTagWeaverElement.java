package org.araneaframework.weaver.jsp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TryCatchFinally;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.araneaframework.weaver.WeaverContext;
import org.araneaframework.weaver.WeaverElement;
import org.araneaframework.weaver.WeaverElementBody;
import org.araneaframework.weaver.WeaverFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class JspTagWeaverElement implements WeaverElement {
  private Element el;
  private TagInfo tagInfo;
  
  private WeaverFactory reg;
  private WeaverElementBody body;
  
  private Class tagClass;
  private Collection attributes = new ArrayList();
 
  public JspTagWeaverElement(Element el, TagInfo tagInfo) {
    this.el = el;
    this.tagInfo = tagInfo;    
  }
  
  public void init(WeaverFactory reg, WeaverElementBody body) {
    this.reg = reg;
    this.body = body;

    try {
      tagClass = Thread.currentThread().getContextClassLoader().loadClass(tagInfo.getTagClassName());
    }
    catch (ClassNotFoundException e) {
      throw new NestableRuntimeException(e);
    }

    for (int i = 0; i < el.getAttributes().getLength(); i++) {
      Attr attr = (Attr) el.getAttributes().item(i);

      AttrInfo attrInfo = (AttrInfo) tagInfo.getAttributes().get(attr.getLocalName());
      Class attrType;
      try {
        attrType = Thread.currentThread().getContextClassLoader().loadClass(attrInfo.getType());
        Method attrMethod = tagClass.getMethod("set" + attrInfo.getName().substring(0, 1).toUpperCase()
            + attrInfo.getName().substring(1), new Class[] { attrType });
        
        attributes.add(new Attribute(attrMethod, attr.getValue()));
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
    }
  }
  
  private static class Attribute {
    private Method attrMethod;
    private Object value;
    
    public Attribute(Method attrMethod, Object value) {
      this.attrMethod = attrMethod;
      this.value = value;
    }
    
    public void set(Object tag) {
      try {
        attrMethod.invoke(tag, new Object[] {value});
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
  }

  public void render(WeaverContext ctx) {
    JspWeaverContext jspCtx = (JspWeaverContext) ctx;
    
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
      tag.setPageContext(jspCtx.getPageContext());
      tag.setParent(jspCtx.getParentTag());

      jspCtx.pushParentTag(tag);
      
      for (Iterator i = attributes.iterator(); i.hasNext();) {
        Attribute attribute = (Attribute) i.next();
        attribute.set(tag);
      }      

      int result = tag.doStartTag();

      if (result != Tag.SKIP_BODY) {        
        body.render(ctx);
      }

      if (tag instanceof IterationTag) {
        result = ((IterationTag) tag).doAfterBody();
        while (result == IterationTag.EVAL_BODY_AGAIN) {
          body.render(ctx);
          result = ((IterationTag) tag).doAfterBody();
        }

      }

      tag.doEndTag();
    }
    catch (Exception e) {  
      Throwable t = e;

      try {
        if (tag instanceof TryCatchFinally)
          ((TryCatchFinally) tag).doCatch(e);
      }
      catch (Throwable tc) {
        t = tc;
      }

      if (t instanceof Error) 
        throw (Error) t;
      else if (t instanceof RuntimeException) 
        throw (RuntimeException) t;
      else
        throw new NestableRuntimeException(t);
    }
    finally {
      if (tag instanceof TryCatchFinally) ((TryCatchFinally) tag).doFinally();

      tag.release();
      
      jspCtx.popParentTag();
    }
  }
  
}
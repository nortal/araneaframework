/*
 * HelloWidget.java
 *
 * Created on 1 November 2006, 15:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jsf.test;

import java.util.Date;
import jsf.test.base.HelloCompositeWidget;
import jsf.test.base.JsfWidget;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class HelloWidget extends BaseUIWidget {
    protected void init() throws Exception {
        setViewSelector("hello");
    }
    
    public String getName() {
        return "Taimo";
    }
    
    public Date getDate() {
        return new Date();
    }
    
    public String getPathInfo() {
    	return ServletUtil.getRequest(getInputData()).getPathInfo();
    }
    public String getServletPath() {
    	return ServletUtil.getRequest(getInputData()).getServletPath();
    }
    
    public String getContextPath() {
    	return ServletUtil.getRequest(getInputData()).getContextPath();
    }
    
    public void handleEventGoJSF() {
        getFlowCtx().start(new JsfWidget("welcomeJSF"), null, null);
    }
    
    public void handleEventGoComposite() {
        getFlowCtx().start(new HelloCompositeWidget(), null, null);
    }
}

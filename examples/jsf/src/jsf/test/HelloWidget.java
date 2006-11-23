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
import jsf.test.base.JsfWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * @author Taimo
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
    
    public void handleEventGoJSF() {
        getFlowCtx().start(new JsfWidget("welcomeJSF"), null, null);
    }
}

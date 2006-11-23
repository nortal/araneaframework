/*
 * JSFContext.java
 *
 * Created on 15 November 2006, 13:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jsf.test.core;

import java.io.Serializable;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;

/**
 * @author Taimo
 */
public interface JSFContext extends Serializable {
    public void initFacesContext(InputData input, OutputData ouput);
    public void releaseFacesContext();
    
    public FacesContext getFacesContext();
    public Lifecycle getLifecycle();
    
    public ApplicationFactory getApplicationFactory();
    public LifecycleFactory getLifecycleFactory();
    public RenderKitFactory getRenderKitFactory();
    public FacesContextFactory getFacesContextFactory();
}

package org.araneaframework.integration.jsf.core;

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
 * @author Taimo Peelo (taimo@araneaframework.org)
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

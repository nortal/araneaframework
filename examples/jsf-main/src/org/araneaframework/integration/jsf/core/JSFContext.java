package org.araneaframework.integration.jsf.core;

import java.io.Serializable;
import javax.faces.application.Application;
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
    public FacesContext initFacesContext(InputData input, OutputData ouput);
    public void destroyFacesContext(FacesContext facesContext);
    
    public ApplicationFactory getApplicationFactory();
    public LifecycleFactory getLifecycleFactory();
    public RenderKitFactory getRenderKitFactory();
    public FacesContextFactory getFacesContextFactory();
    
    public Application getApplication();
    public Lifecycle getLifecycle();
}

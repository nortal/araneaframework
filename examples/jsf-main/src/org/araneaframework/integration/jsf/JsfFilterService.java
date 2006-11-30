package org.araneaframework.integration.jsf;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.integration.jsf.core.AraneaJsfNavigationHandlerWrapper;
import org.araneaframework.integration.jsf.core.JSFContext;
import org.araneaframework.integration.jsf.useless.ViewHandlerDecorator;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfFilterService extends BaseFilterService implements JSFContext {
    private Lifecycle lifecycle = null;
    private Application application = null;
    
    protected Environment getChildEnvironment() {
        return new StandardEnvironment(super.getChildEnvironment(), JSFContext.class, this);
    }
    
    protected void init() throws Exception {
        super.init();

        ApplicationFactory appFactory = getApplicationFactory();
        Assert.notNull(appFactory);
        Assert.notNull(getLifecycleFactory());
        Assert.notNull(getRenderKitFactory());
        Assert.notNull(getFacesContextFactory());
        
        application = appFactory.getApplication();
        application.setViewHandler(new ViewHandlerDecorator(application.getViewHandler()));
        application.setNavigationHandler(new AraneaJsfNavigationHandlerWrapper(application.getNavigationHandler()));
        
        lifecycle =  getLifecycleFactory().getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        Assert.notNull(lifecycle);
    }
    
    protected void action(Path path, InputData input, OutputData output) throws Exception {
        super.action(path, input, output);
    }
    

    
    public FacesContext initFacesContext(InputData input, OutputData output) {
        ServletContext servletCtx = ((ServletConfig)getEnvironment().getEntry(ServletConfig.class)).getServletContext();
        Assert.notNull(servletCtx, "ServletContext");
        Assert.notNull(ServletUtil.getRequest(input), "request");
        Assert.notNull(ServletUtil.getResponse(output), "response");
        Assert.notNull(lifecycle);

        FacesContext result = 
                getFacesContextFactory().getFacesContext(                    
                    ((ServletConfig)getEnvironment().getEntry(ServletConfig.class)).getServletContext(),
                    ServletUtil.getRequest(input),
                    ServletUtil.getResponse(output),
                    lifecycle
                );
        
        return result;
    }

    public void destroyFacesContext(FacesContext facesContext) {
    	facesContext.release();
    }

    /* Factory getters. */
    public ApplicationFactory getApplicationFactory() {
        return (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    }
    
    public LifecycleFactory getLifecycleFactory() {
        return (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    }
    
    public FacesContextFactory getFacesContextFactory() {
        return (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    }
    
    public RenderKitFactory getRenderKitFactory() {
        return (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    }
    
    public Application getApplication() {
    	return application;
    }
    
    public Lifecycle getLifecycle() {
        return lifecycle;
    } 
}

package org.araneaframework.servlet.filter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.StandardNonInitializableRelocatableService;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.ThreadCloningContext;
import org.araneaframework.servlet.filter.StandardPopupFilterWidget.StandardPopupServiceInfo;
import org.araneaframework.servlet.support.PopupWindowProperties;

public class ThreadCloningService extends BaseService implements ThreadCloningContext {
	private static final Logger log = Logger.getLogger(ThreadCloningService.class);

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		ThreadContext tc = (ThreadContext) getEnvironment().getEntry(ThreadContext.class);
		log.debug("Attempting to clone thread with id " + getClonableThreadId(input));
		Relocatable.RelocatableService service = (RelocatableService) tc.getService(getClonableThreadId(input));
		
		Environment env = service._getRelocatable().getCurrentEnvironment();
		service._getRelocatable().overrideEnvironment(null);
		
	    if (1 == 1) {
	        String dumpPath = "/home/taimo/tmp/logs/clone" + "/" + "test.xml";
	        
	        XStream xstream = new XStream(new DomDriver());
	        PrintWriter writer = new PrintWriter(new FileWriter(dumpPath));
	        String serialized = xstream.toXML(service);
	        xstream.toXML(service, writer);
	        writer.close();
	        
	        RelocatableService t = (RelocatableService) xstream.fromXML(serialized);
	        StandardNonInitializableRelocatableService cloneService = new StandardNonInitializableRelocatableService(t);
			// now how to add this?
	        String cloneServiceId = RandomStringUtils.randomAlphabetic(12);
	        tc.addService(cloneServiceId, cloneService);

	        StandardPopupServiceInfo threadInfo = 
	            new StandardPopupServiceInfo((String)getTopServiceCtx().getCurrentId(), cloneServiceId, (PopupWindowProperties) null, getRequestURL());

            ((ServletOutputData) getCurrentOutput()).getResponse().sendRedirect(threadInfo.toURL());
	     }
	    
	    service._getRelocatable().overrideEnvironment(env);
	}

	protected void init() throws Exception {
		super.init();
	}
	
	protected Object getClonableThreadId(InputData input) throws Exception {
		return input.getGlobalData().get(ThreadCloningContext.CLONABLE_THREAD_KEY);
	}

	protected String getRequestURL() {
		return ((HttpServletRequest)((ServletInputData)getCurrentInput()).getRequest()).getRequestURL().toString();
	}
	
	protected TopServiceContext getTopServiceCtx() {
       return ((TopServiceContext)getEnvironment().requireEntry(TopServiceContext.class));
    }
}

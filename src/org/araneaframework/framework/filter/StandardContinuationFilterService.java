/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package org.araneaframework.framework.filter;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ContinuationContext;
import org.araneaframework.framework.ContinuationManagerContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.AtomicResponseHelper;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardContinuationFilterService extends BaseFilterService implements ContinuationManagerContext, ContinuationContext{
  private static final Logger log = Logger.getLogger(StandardContinuationFilterService.class);  
  
  private Service continuation;
  
  protected Environment getChildEnvironment() {
    Map entries = new HashMap();
    entries.put(ContinuationManagerContext.class, this);        
    return new StandardEnvironment(super.getChildEnvironment(), entries);
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    AtomicResponseHelper arUtil = 
      new AtomicResponseHelper(output);
    
    try {
      if (isRunning()) {
        log.debug("Routing action to continuation");
        continuation._getService().action(path, input, output);
      }
      
      if (!isRunning()) {
        arUtil.rollback();
        
        Path scope = output.getScope();             
        
        try {                          
          try {
            log.debug("Routing action to child service");
            childService._getService().action(path, input, output);
          }
          finally {
            output.restoreScope(scope);
          }
        }
        catch (Exception e) {         
          if (continuation == null)
            throw e;

          arUtil.rollback();
          
          log.debug("Routing action to continuation");          
          continuation._getService().action(null, input, output);
        }
      }
    }
    finally {      
      arUtil.commit();
    }
  }

  public void start(Service continuation) {
    this.continuation = continuation;
    
    Map entries = new HashMap();
    entries.put(ContinuationContext.class, this);        
    continuation._getComponent().init(new StandardEnvironment(getEnvironment(), entries));
    
    throw new AraneaRuntimeException("Continuation set!");
  }
  
  public void finish() {
    continuation._getComponent().destroy();
    continuation = null;
  }

  public boolean isRunning() {
    return continuation != null;
  }

	public void runOnce(Service continuation) {
		BaseFilterService service = new BaseFilterService(continuation) {
			protected void action(Path path, InputData input, OutputData output) throws Exception {
        childService._getService().action(path, input, output);
				
				ContinuationContext conCtx = 
					(ContinuationContext) getEnvironment().getEntry(ContinuationContext.class);
				conCtx.finish();
			}
		};
    
		start(service);
	}
}

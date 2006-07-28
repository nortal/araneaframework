package org.araneaframework.framework.filter;

import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.MountContext;
import org.araneaframework.framework.core.BaseFilterService;

public class StandardMountPointFilterService extends BaseFilterService {
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    MountContext mountCtx = 
      (MountContext) getEnvironment().requireEntry(MountContext.class);
    
    Message mountMsg = mountCtx.getMountedMessage(input);
    
    if (mountMsg != null)
      mountMsg.send(null, childService);
    
    super.action(path, input, output);
  }
}

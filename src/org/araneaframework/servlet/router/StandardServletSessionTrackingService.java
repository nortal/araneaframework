package org.araneaframework.servlet.router;

import javax.servlet.http.HttpServletRequest;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.util.ClientStateUtil;

public class StandardServletSessionTrackingService extends BaseService {
	public static final String SESSION_ID = "jsessionid";
	
	protected void action(Path path, InputData input, OutputData output) throws Exception {
		HttpServletRequest req = ((ServletInputData) input).getRequest();		
		
		String sessionId = (String)input.getGlobalData().get(SESSION_ID);
		
		if (sessionId == null) {
			sessionId = req.getSession().getId();
		}
		
		ClientStateUtil.put("jsessionId", sessionId, output);
	}
}

package org.araneaframework.example.main.release.demos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.HttpOutputData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoActionPollWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("release/demos/demoActionPoll");
		addActionListener("pollrequest", new TaskPollingListener());
	}

	public class TaskPollingListener extends StandardActionListener {
		private Random rn = new Random();
		int lastRandom = 0;

		public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
			int random = rn.nextInt(100);
			
			String change = "";

			lastRandom = random;
			
			HttpOutputData httpOutput = (HttpOutputData) output;
			
			String s = 
				"<tr>"+ 
					"<td>" + new SimpleDateFormat("HH:mm.ss").format(new Date()) + "</td>" +
					"<td>" + random + "</td>" +
				"</tr>";
			
			httpOutput.setContentType("text/xml");
			httpOutput.getWriter().write(s);
			
			
		}
	}
}

/*
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
 */

package org.araneaframework.example.main.web.release;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoActionPollWidget extends TemplateBaseWidget {

  @Override
  protected void init() throws Exception {
    setViewSelector("release/demoActionPoll");
    addActionListener("pollrequest", new TaskPollingListener());
  }

  public class TaskPollingListener extends StandardActionListener {

    private Random rn = new Random();

    int lastRandom = 0;

    @Override
    public void processAction(String actionId, String actionParam, InputData input, OutputData output) throws Exception {

      int random = this.rn.nextInt(100);
      this.lastRandom = random;
      HttpOutputData httpOutput = (HttpOutputData) output;
      String s = "NOTHING";

      if (this.rn.nextInt(3) == 1) {
        s = MessageUtil.localizeAndFormat(getEnvironment(), "poll.taskmsg", random, new SimpleDateFormat("HH:mm.ss")
            .format(new Date()))
            + "<br/>";
      }
      httpOutput.setContentType("text/xml");
      httpOutput.getWriter().write(s);
    }

  }

}

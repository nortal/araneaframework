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

package org.araneaframework.core;

import org.araneaframework.Component;
import org.araneaframework.Message;

public class MessageSeries implements Message {
  private Message[] series;
    
  public MessageSeries(Message[] series) {
    this.series = series;
  }

  public void send(Object id, Component component) {
    for (int i = 0; i < series.length; i++)
      series[i].send(id, component);
  }
}

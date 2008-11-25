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

package org.araneaframework.core;

import org.araneaframework.Component;
import org.araneaframework.Message;

/**
 * A <code>Message</code> that contains several messages in one. Provides a
 * way to send more than one message in the given order.
 */
public class SeriesMessage implements Message {

  private static final long serialVersionUID = 1L;

  private Message[] series;

  /**
   * A constructor that takes an array of <code>Message</code>s for argument.
   * These messages are processed in the same order as defined in the array.
   * <p>
   * The array may be empty but <code>null</code>.
   * 
   * @param series An array of messages to send.
   */
  public SeriesMessage(Message[] series) {
    Assert.notNullParam(this, series, "series");
    this.series = series;
  }

  /**
   * For each component the messages are processed in the same order as they
   * appear in the array. {@inheritDoc}
   */
  public void send(Object id, Component component) {
    for (int i = 0; i < series.length; i++)
      series[i].send(id, component);
  }

}

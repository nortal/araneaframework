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

package org.araneaframework.core.message;

import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.core.util.Assert;

/**
 * A <code>Message</code> containing zero-to-many messages. Provides a way to send multiple messages in the given
 * processing order (next message takes over when previous message has completed).
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class SeriesMessage implements Message {

  private final Message[] series;

  /**
   * A constructor that takes an array of <code>Message</code>s for argument. These messages are processed in the same
   * order as defined in the array (next message takes over when previous message has completed).
   * <p>
   * The array may be empty but not <code>null</code>.
   * 
   * @param series An array of messages to send.
   */
  public SeriesMessage(Message... series) {
    Assert.notNullParam(this, series, "series");
    this.series = series;
  }

  /**
   * For each component the messages are processed in the same order as they appear in the array.
   * <p>
   * {@inheritDoc}
   */
  public void send(Object id, Component component) {
    for (Message serie : this.series) {
      serie.send(id, component);
    }
  }

}

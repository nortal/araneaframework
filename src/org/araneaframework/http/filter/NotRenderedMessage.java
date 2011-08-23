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

package org.araneaframework.http.filter;

import org.araneaframework.Component;
import org.araneaframework.core.message.BroadcastMessage;
import org.araneaframework.framework.core.RenderStateAware;

/**
 * A message that can be propagated to sub-components so that all of them that implement {@link RenderStateAware} would
 * be set to *not rendered* state.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class NotRenderedMessage extends BroadcastMessage {

  /**
   * Since <code>NotRenderedMessage</code> does not have any state, the message should be retrieved from this instance.
   */
  public static final NotRenderedMessage INSTANCE = new NotRenderedMessage();

  private NotRenderedMessage() {
    super(RenderStateAware.class);
  }

  @Override
  protected void execute(Component component) throws Exception {
    ((RenderStateAware) component)._setRendered(false);
  }
}

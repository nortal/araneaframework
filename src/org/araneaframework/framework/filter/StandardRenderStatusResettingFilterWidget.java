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

package org.araneaframework.framework.filter;

import org.araneaframework.OutputData;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.filter.NotRenderedMessage;

/**
 * This filter resets all {@link org.araneaframework.framework.core.RenderStateAware} components render state to not
 * rendered prior to calling render on its children. Therefore the parent component(s) of this filter can call
 * {@link #render(OutputData)} several times. (Usually they invoke rendering only once, but UpdateRegionContext may
 * sometimes do it twice.)
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class StandardRenderStatusResettingFilterWidget extends BaseFilterWidget {

  @Override
  protected void render(OutputData output) throws Exception {
    NotRenderedMessage.INSTANCE.send(null, getChildWidget());
    super.render(output);
  }
}

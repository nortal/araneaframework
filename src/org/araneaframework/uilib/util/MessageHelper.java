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

package org.araneaframework.uilib.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.framework.LocalizationContext;

public class MessageHelper {
  private LocalizationContext locCtx;
  private String message;
  private List parameters = new ArrayList();
  
  public MessageHelper(String messageCode, Environment env) {
    locCtx = 
      (LocalizationContext) env.getEntry(LocalizationContext.class);
    this.message = locCtx.localize(messageCode);        
  }
  
  public MessageHelper locParam(String locParam) {
    parameters.add(locCtx.localize(locParam));
    return this;
  }
  
  public MessageHelper constParam(String constParam) {
    parameters.add(constParam);
    return this;
  }
  
  public String toMessage() {
    return MessageFormat.format(message, parameters.toArray());
  }
}

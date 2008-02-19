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

package org.araneaframework.http.util;

import org.araneaframework.Environment;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.http.UpdateRegionContext;

/**
 * Utility class that provides shortcuts for accessing some {@link Environment} entries.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public abstract class EnvironmentUtil {
  public static Object getTopServiceId(Environment env) {
    TopServiceContext topServiceContext = (TopServiceContext) env.getEntry(TopServiceContext.class);
    if (topServiceContext == null)
      return null;
    return topServiceContext.getCurrentId();
  }
  
  public static Object getThreadServiceId(Environment env) {
    ThreadContext threadContext = (ThreadContext) env.getEntry(ThreadContext.class);
    if (threadContext == null)
      return null;
    return threadContext.getCurrentId();
  }
  
  public static Object requireTopServiceId(Environment env) {
    TopServiceContext topServiceContext = (TopServiceContext) env.requireEntry(TopServiceContext.class);
    return topServiceContext.getCurrentId();
  }
  
  public static Object requireThreadServiceId(Environment env) {
    ThreadContext threadContext = (ThreadContext) env.requireEntry(ThreadContext.class);
    return threadContext.getCurrentId();
  }
  
  // THESE ARE NOT CONNECTED TO SYSTEM FORM
  public static LocalizationContext getLocalizationContext(Environment env) {
    return (LocalizationContext)env.getEntry(LocalizationContext.class);
  }
  
  public static LocalizationContext requireLocalizationContext(Environment env) {
    return (LocalizationContext)env.requireEntry(LocalizationContext.class);
  }
  
  public static FlowContext getFlowContext(Environment env) {
    return (FlowContext)env.getEntry(FlowContext.class);
  }

  public static FlowContext requireFlowContext(Environment env) {
    return (FlowContext)env.requireEntry(FlowContext.class);
  }
  
  public static MessageContext getMessageContext(Environment env) {
    return (MessageContext)env.getEntry(MessageContext.class);
  }

  public static MessageContext requireMessageContext(Environment env) {
    return (MessageContext)env.requireEntry(MessageContext.class);
  }
  
  public static SystemFormContext getSystemFormContext(Environment env) {
    return (SystemFormContext)env.getEntry(SystemFormContext.class);
  }
  
  public static SystemFormContext requireSystemFormContext(Environment env) {
    return (SystemFormContext)env.requireEntry(SystemFormContext.class);
  }
  
  public static UpdateRegionContext getUpdateRegionContext(Environment env) {
    return (UpdateRegionContext)env.getEntry(UpdateRegionContext.class);
  }

  public static UpdateRegionContext requireUpdateRegionContext(Environment env) {
    return (UpdateRegionContext)env.requireEntry(UpdateRegionContext.class);
  }
}

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

package org.araneaframework.http.util;

import java.lang.reflect.Field;
import org.araneaframework.Environment;
import org.araneaframework.core.annotation.EnvironmentEntry;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.framework.ContinuationContext;
import org.araneaframework.framework.ExpiringServiceContext;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.StateVersioningContext;
import org.araneaframework.http.UpdateRegionContext;

/**
 * Utility class that provides shortcuts for accessing some {@link Environment}
 * entries.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public abstract class EnvironmentUtil {

  public static TopServiceContext getTopServiceContext(Environment env) {
    return env.getEntry(TopServiceContext.class);
  }

  public static TopServiceContext requireTopServiceContext(Environment env) {
    return env.requireEntry(TopServiceContext.class);
  }

  public static String getTopServiceId(Environment env) {
    TopServiceContext topServiceContext = getTopServiceContext(env);
    return topServiceContext == null ? null : topServiceContext.getCurrentId();
  }

  public static String requireTopServiceId(Environment env) {
    return requireTopServiceContext(env).getCurrentId();
  }

  public static ThreadContext getThreadContext(Environment env) {
    return env.getEntry(ThreadContext.class);
  }

  public static ThreadContext requireThreadContext(Environment env) {
    return env.requireEntry(ThreadContext.class);
  }

  public static String getThreadServiceId(Environment env) {
    ThreadContext threadContext = getThreadContext(env);
    return threadContext == null ? null : threadContext.getCurrentId();
  }
  
  public static String requireThreadServiceId(Environment env) {
    return requireThreadContext(env).getCurrentId();
  }

  // THESE ARE NOT CONNECTED TO SYSTEM FORM
  public static LocalizationContext getLocalizationContext(Environment env) {
    return env.getEntry(LocalizationContext.class);
  }

  public static LocalizationContext requireLocalizationContext(Environment env) {
    return env.requireEntry(LocalizationContext.class);
  }

  public static FlowContext getFlowContext(Environment env) {
    return env.getEntry(FlowContext.class);
  }

  public static FlowContext requireFlowContext(Environment env) {
    return env.requireEntry(FlowContext.class);
  }

  public static MessageContext getMessageContext(Environment env) {
    return env.getEntry(MessageContext.class);
  }

  public static MessageContext requireMessageContext(Environment env) {
    return env.requireEntry(MessageContext.class);
  }

  public static SystemFormContext getSystemFormContext(Environment env) {
    return env.getEntry(SystemFormContext.class);
  }

  public static SystemFormContext requireSystemFormContext(Environment env) {
    return env.requireEntry(SystemFormContext.class);
  }

  public static UpdateRegionContext getUpdateRegionContext(Environment env) {
    return env.getEntry(UpdateRegionContext.class);
  }

  public static UpdateRegionContext requireUpdateRegionContext(Environment env) {
    return env.requireEntry(UpdateRegionContext.class);
  }

  public static PopupWindowContext getPopupWindowContext(Environment env) {
    return env.getEntry(PopupWindowContext.class);
  }

  public static PopupWindowContext requirePopupWindowContext(Environment env) {
    return env.requireEntry(PopupWindowContext.class);
  }

  public static StateVersioningContext getStateVersioningContext(Environment env) {
    return env.getEntry(StateVersioningContext.class);
  }

  public static StateVersioningContext requireStateVersioningContext(Environment env) {
    return env.requireEntry(StateVersioningContext.class);
  }

  public static ContinuationContext getContinuationContext(Environment env) {
    return env.getEntry(ContinuationContext.class);
  }

  // Environment item retrieve methods mostly for Aranea core:
  public static ManagedServiceContext requireManagedService(Environment env) {
    return env.requireEntry(ManagedServiceContext.class);
  }

  public static ExpiringServiceContext getExpiringServiceContext(Environment env) {
    return env.getEntry(ExpiringServiceContext.class);
  }

  public static ConfirmationContext getConfirmationContext(Environment env) {
    return env.getEntry(ConfirmationContext.class);
  }

  public static ConfirmationContext requireConfirmationContext(Environment env) {
    return env.requireEntry(ConfirmationContext.class);
  }

  /**
   * Injects appropriate environment entries to the fields of given object, if the field has annotation {@link EnvironmentEntry}.
   * The annotation can provide the environment entry key or the type of the field will be used. If the annotation
   * declares that the dependency is mandatory and the entry is not found, an exception will be thrown.
   * <p>
   * The parameters to this method must not be null!
   * 
   * @param env The environment to use for entries lookup.
   * @param object The object that may contain fields with the {@link EnvironmentEntry} annotation.
   * @since 2.0
   */
  public static void injectEnvironmentEntries(Environment env, Object object) {
    Assert.notNullParam(EnvironmentUtil.class, env, "env");
    Assert.notNullParam(EnvironmentUtil.class, object, "object");

    for (Field field : object.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(EnvironmentEntry.class)) {
        EnvironmentEntry data = field.getAnnotation(EnvironmentEntry.class);
        Class<?> type = data.value();
        if (type == null) {
          type = field.getType();
        }
        Object entry = data.required() ? env.requireEntry(type) : env.getEntry(type);
        try {
          field.setAccessible(true);
          field.set(object, entry);
        } catch (Exception e) {
          ExceptionUtil.uncheckException(e);
        }
      }
    }
  }
}

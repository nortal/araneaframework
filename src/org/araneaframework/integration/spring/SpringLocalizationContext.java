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

package org.araneaframework.integration.spring;

import org.araneaframework.framework.LocalizationContext;
import org.springframework.context.MessageSource;

/**
 * Extends the <code>LocalizationContext</code> to provide the localization using the <code>MessageSource</code> from
 * the Spring framework.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * @see SpringLocalizationFilterService
 */
public interface SpringLocalizationContext extends LocalizationContext {

  /**
   * Provides access to the <code>MessageSource</code> from the Spring framework.
   * 
   * @return the <code>MessageSource</code> from the Spring framework.
   */
  MessageSource getMessageSource();

}

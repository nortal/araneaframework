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
package org.araneaframework.framework;

import java.io.Serializable;
import org.araneaframework.Service;

/**
 * Service that has at least single child and acts as a guardian, deciding which data should be let 
 * through to children and what services should they be allowed to use.
 * 
 * @see org.araneaframework.framework.core.BaseFilterService
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface FilterService extends Serializable, Service {
  public void setChildService(Service childService);
}

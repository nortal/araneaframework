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

package org.araneaframework.mock;

import java.util.HashMap;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class MockLifeCycle {
  public static Component begin(Component comp, Environment env) {
    comp._getComponent().init(comp.getScope(), env);
    return comp;
  }
  
  public static Component begin(Component comp) {
    comp._getComponent().init(comp.getScope(), new StandardEnvironment(null, new HashMap<Class<?>, Object>()));
    return comp;
  }
  
  public static void end(Component comp) {
    comp._getComponent().destroy();    
  }
}

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

package org.araneaframework;

/**
 * Allows to acquire information about running Aranea version. It depends on JAR file manifest having this information.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class AraneaVersion {

  /**
   * Instantiating this class is prohibited.
   */
  protected AraneaVersion() {
    throw new UnsupportedOperationException();
  }

  /**
   * Provides Aranea implementation title from the JAR file.
   * 
   * @return Aranea implementation title.
   */
  public static String getTitle() {
    Package p = AraneaVersion.class.getPackage();
    return p == null ? null : p.getImplementationTitle();
  }

  /**
   * Provides Aranea implementation version from the JAR file.
   * 
   * @return Aranea implementation version.
   */
  public static String getVersion() {
    Package p = AraneaVersion.class.getPackage();
    return p == null ? null : p.getImplementationVersion();
  }

  /**
   * The main method for running this class alone: just prints out Aranea implementation title and version.
   * 
   * @param args Command line parameters - ignored in this method.
   */
  public static void main(String[] args) {
    System.out.println(getTitle() + " " + getVersion());
  }
}

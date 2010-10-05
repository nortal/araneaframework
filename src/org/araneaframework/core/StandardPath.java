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

package org.araneaframework.core;

import java.util.Collection;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Path;

/**
 * Default implementation of {@link org.araneaframework.Path}, uses simple string identifiers like "a" or "b" and
 * combines them using dots forming full pathes like "a.b.c".
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardPath implements Path {

  private LinkedList<String> path = new LinkedList<String>();

  /**
   * Constructs a path from the fullPath. Expects fullPath to be a dot-separated String.
   * 
   * @param fullPath
   */
  public StandardPath(String fullPath) {
    Assert.notNull(fullPath, "Path cannot be null!");

    StringTokenizer tokenizer = new StringTokenizer(fullPath, SEPARATOR);
    while (tokenizer.hasMoreElements()) {
      this.path.add(tokenizer.nextToken());
    }
  }

  public String getNext() {
    return this.path.getFirst();
  }

  public String next() {
    return this.path.removeFirst();
  }

  public boolean hasNext() {
    return !this.path.isEmpty();
  }

  /**
   * @since 1.1
   */
  public StandardPath(Collection<String> fullPath) {
    this.path.addAll(fullPath);
  }

  /**
   * Returns this {@link org.araneaframework.Path} as a dot-separated String.
   * 
   * @return this {@link org.araneaframework.Path} as a dot-separated String
   */
  @Override
  public String toString() {
    return StringUtils.join(this.path, SEPARATOR).intern();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Path)) {
      return false;
    }
    String other = obj.toString();
    return StringUtils.equals(other, toString());
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}

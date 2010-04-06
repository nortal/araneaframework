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

package org.araneaframework.uilib.list.util.like;

import java.io.Serializable;

/**
 * Base implementation of {@link WildcardHandler}.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * 
 * @see WildcardHandler
 */
public abstract class BaseWildcardHandler implements WildcardHandler, Serializable {
	
	protected int startsWith;
	protected int endsWith;
	protected String escapedMask;

	public void setStartsWith(int startsWith) {
		this.startsWith = startsWith;
	}

	public void setEndsWith(int endsWith) {
		this.endsWith = endsWith;
	}

	public int getEndsWith() {
		return endsWith;
	}

	public int getStartsWith() {
		return startsWith;
	}

  public String getEscapedMask() {
    return escapedMask;
  }

  public void setEscapedMask(String escapedMask) {
    this.escapedMask = escapedMask;
  }
}

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

package org.araneaframework.example.common.framework.context;

import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.FlowContext.Handler;

/**
 * This CallContext.Handler implementation passes return and cancel calls to the
 * next handler in specified CallContext.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class PassthroughCallContextHandler implements Handler {

	private FlowContext ctx;

	public PassthroughCallContextHandler(FlowContext ctx) {
		this.ctx = ctx;
	}

	public void onFinish(Object returnValue) throws Exception {
		ctx.finish(returnValue);
	}

	public void onCancel() throws Exception {
		ctx.cancel();
	}
}

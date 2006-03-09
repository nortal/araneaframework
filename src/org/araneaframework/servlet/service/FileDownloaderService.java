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

package org.araneaframework.servlet.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.uilib.support.FileInfo;

/**
 * Service that allows for downloading of specified files.
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class FileDownloaderService extends BaseService {
	protected FileInfo file;
	
	public FileDownloaderService(FileInfo file) {
		this.file = file;
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		HttpServletResponse response = ((ServletOutputData) output).getResponse();
	    
	    // XXX: basically cast from long to int - might be dangerous!!
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(file.readFileContent().length);
	    byteOutputStream.write(file.readFileContent());

	    response.setContentType(file.getContentType());
	    response.setContentLength(byteOutputStream.size());

	    OutputStream out = response.getOutputStream();
	    byteOutputStream.writeTo(out);
	    out.flush();

		super.action(path, input, output);
	}
}

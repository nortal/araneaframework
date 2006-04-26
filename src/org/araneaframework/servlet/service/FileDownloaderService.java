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
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.uilib.support.FileInfo;

/**
 * Service that allows for downloading of specified files.
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class FileDownloaderService extends BaseService {
	protected FileInfo file;
	
	protected byte[] fileContent;
	protected String contentType;
	protected String fileName;
	
	protected boolean contentDispositionInline = true;
	
	public FileDownloaderService(FileInfo file) {
		this.fileContent = file.readFileContent();
		this.contentType = file.getContentType();
		this.fileName = normalizeFileName(file.getOriginalFilename());
	}
	
	public FileDownloaderService(byte[] fileContent, String contentType, String fileName) {
		this.fileContent = fileContent;
		this.contentType = contentType;
		this.fileName = normalizeFileName(fileName);
	}
	
	/**
	 * @return false if content-disposition header is set to "attachment"
	 */
	public boolean isContentDispositionInline() {
		return contentDispositionInline;
	}

	/**
	 * Sets content-disposition header to "inline" (true) or "attachment" (false).
	 */
	public void setContentDispositionInline(boolean contentDispositionInline) {
		this.contentDispositionInline = contentDispositionInline;
	}
	
	// declare non-static in case derived classes wish to override
	public String normalizeFileName(String fileName) {
		return FileDownloaderService.staticNormalizeFileName(fileName);
	}

	// as IE provides full names for uploaded files, we use some heuristics
	// to ensure filename does not include the full path. By no means bulletproof
	// as it does some harm to filenames containing slashes/backslashes.
	public static String staticNormalizeFileName(String fileName) {
		fileName.trim();
		// shouldn't happen, but anyway...
		if (fileName.endsWith("\\"))
			fileName = fileName.substring(0, fileName.length()-1);
		if (fileName.endsWith("/"))
			fileName = fileName.substring(0, fileName.length()-1);
		
		if (fileName.lastIndexOf('\\') != -1) {
			fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
		}
		// IE on MAC?
		if (fileName.lastIndexOf('/') != -1) {
			fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
		}
		return fileName;
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		HttpServletResponse response = ((ServletOutputData) output).getResponse();
		
	  // XXX: basically cast from long to int - might be dangerous!!
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(fileContent.length);
    byteOutputStream.write(fileContent);

    response.setContentType(contentType);
    response.setHeader("Content-Disposition", (contentDispositionInline ? "inline;" : "attachment;") + "filename=" + fileName);
    response.setContentLength(byteOutputStream.size());

    OutputStream out = response.getOutputStream();
    byteOutputStream.writeTo(out);
    out.flush();
      
    //Ensure that allow to download only once...
    ManagedServiceContext mngCtx = (ManagedServiceContext) getEnvironment().getEntry(ManagedServiceContext.class);
    mngCtx.close(mngCtx.getCurrentId());
	}
}

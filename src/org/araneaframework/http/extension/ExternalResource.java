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

package org.araneaframework.http.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A data structure for holding information about external resources. Different
 * files are grouped by group names. Every file has its specific content-type. Possible
 * to query by file name if a file is allowed to be loaded.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class ExternalResource {
	private Map<String, Map<String, String>> groupsByName = new HashMap<String, Map<String, String>>();
	private Map<String, String> allowedFiles = new HashMap<String, String>();
	
	public void addGroup(FileGroup fileGroup) {
		if (fileGroup == null)
			return;

		// add files to the allowed list with the content type
		for (String element : fileGroup.getFiles()) {
			allowedFiles.put(element, fileGroup.getContentType());
		}
		
		// adding files by group names
		if (fileGroup.getName() == null)
			return;
		
		Map<String, String> group = groupsByName.get(fileGroup.getName());
		if (group == null) { // no group by this name, lets create one
			group = new LinkedHashMap<String, String>();
			groupsByName.put(fileGroup.getName(), group);
		}
		for (String file : fileGroup.getFiles()) {
			group.put(file, fileGroup.getContentType());
		}
	}
	
	/**
	 * Returns a Set of available group names.
	 * @return a Set of available group names.
	 */
	public Set<String> getGroupNames() {
		return groupsByName.keySet();
	}
	
	/**
	 * Returns a Map of filenames paired with respective content types.
	 * @param name the name of the group.
	 * @return Map of filenames paired with respective content types.
	 */
	public Map<String, String> getGroupByName(String name) {
		return groupsByName.get(name);
	}

	/**
	 * Returns true if the fileName is in the allowed list.
	 * @param fileName the name of the file.
	 * @return true if the file is in the allowed list otherwise false.
	 */
	public boolean isAllowedFile(String fileName) {
		return allowedFiles.containsKey(fileName);
	}

	/**
	 * Returns the content-type of the file. Returns null, if no such file is in the allowed list.
	 * @param fileName the name of the file.
	 * @return the content-type of the file.
	 */
	public String getContentType(String fileName) {
		return allowedFiles.get(fileName);
	}
	
	static class FileGroup {
		private String name;
		private String contentType;
		private List<String> files;
		
		public FileGroup() {
			files = new ArrayList<String>();
		}
		
		public FileGroup(String name) {
			this();
			this.name = name;
		}
		
		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public List<String> getFiles() {
			return files;
		}
		
		public void addFile(String file) {
			this.files.add(file);
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}
}
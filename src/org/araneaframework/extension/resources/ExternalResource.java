package org.araneaframework.extension.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.LinkedMap;

public class ExternalResource {
	private Map groupsByName = new HashMap();
	private Map allowedFiles = new HashMap();
	
	public void addGroup(FileGroup fileGroup) {
		if (fileGroup == null)
			return;

		// add files to the allowed list with the content type
		for (Iterator iter = fileGroup.getFiles().iterator(); iter.hasNext();) {
			allowedFiles.put(iter.next(), fileGroup.getContentType());
		}
		
		// adding files by group names
		if (fileGroup.getName() == null)
			return;
		
		Map group = (Map)groupsByName.get(fileGroup.getName());
		if (group == null) { // no group by this name, lets create one
			group = new LinkedMap();
			groupsByName.put(fileGroup.getName(), group);
		}
		for (Iterator iter = fileGroup.getFiles().iterator(); iter.hasNext();) {
			group.put(iter.next(), fileGroup.getContentType());
		}
	}
		
	public Set getGroupNames() {
		return groupsByName.keySet();
	}
	
	public Map getGroupByName(String name) {
		return (Map)groupsByName.get(name);
	}

	public boolean isAllowedFile(String fileName) {
		return allowedFiles.containsKey(fileName);
	}

	public String getContentType(String fileName) {
		return (String)allowedFiles.get(fileName);
	}
}

class FileGroup {
	private String name;
	private String contentType;
	private List files;
	
	public FileGroup() {
		files = new ArrayList();
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

	public List getFiles() {
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
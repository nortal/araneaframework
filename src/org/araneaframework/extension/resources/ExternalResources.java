package org.araneaframework.extension.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExternalResources {
	private Map groupsByContenType = new HashMap();
	private Map groupsByName = new HashMap();
	
	public void addGroup(FileGroup fileGroup) {
		if (fileGroup == null)
			return;

		// adding files by content type
		List files = (List)groupsByContenType.get(fileGroup.getContentType()); 
		if (files == null) {
			files = new ArrayList();
		}
		files.addAll(fileGroup.getFiles());
		groupsByContenType.put(fileGroup.getContentType(), files);
		
		// adding files by group names
		if (fileGroup.getName() == null)
			return;
		
		Map groups = (Map)groupsByName.get(fileGroup.getName());
		if (groups == null) { // no group by this name, lets create one
			
			Map subGroup = new HashMap();
			subGroup.put(fileGroup.getContentType(), fileGroup.getFiles());
			
			groupsByName.put(fileGroup.getName(), subGroup);
		}
		else { // we have a group by the name
			// taking the subgroup by the content-type
			files = (List)groups.get(fileGroup.getContentType());
			
			if (files == null) {
				groups.put(fileGroup.getContentType(), fileGroup.getFiles());
			}
			else {
				files.addAll(fileGroup.getFiles());
			}
		}
	}
	
	public Set getContentTypes() {
		return groupsByContenType.keySet();
	}
	
	public List getFilesByContentType(String contentType) {
		return (List)groupsByContenType.get(contentType);
	}
		
	public Set getGroupNames() {
		return groupsByName.keySet();
	}
	
	public Map getGroupByName(String name) {
		return (Map)groupsByName.get(name);
	}
}

class FileGroup {
	private String contentType = "";
	private String name;
	private List files;
	
	public FileGroup() {
		files = new ArrayList();
	}
	
	public FileGroup(String contentType, String name) {
		this();
		
		this.contentType = contentType;
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
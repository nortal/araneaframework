package org.araneaframework.example.main.release.demos;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.Widget;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.uilib.core.MenuContext;
import org.araneaframework.uilib.core.MenuItem;
import org.araneaframework.uilib.tab.TabContainerWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoNewTabWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
	    setViewSelector("release/demos/tab");
	    
	    TabContainerWidget containerWidget = new TabContainerWidget();
	    addWidget("tabContainer", containerWidget);
	    
	    addTabs(containerWidget);
	}

	// whole method is a hack to determine menu content and show it in different tabs
	private void addTabs(TabContainerWidget containerWidget) throws IllegalAccessException, InstantiationException {
		Map araneaDemos = ((MenuWidget)getEnvironment().getEntry(MenuContext.class)).getAraneaMenu().getSubMenu();
	    for (Iterator i = araneaDemos.entrySet().iterator(); i.hasNext();) {
	    	Map.Entry entry = (Map.Entry) i.next();
	    	MenuItem menuItem = ((MenuItem)entry.getValue());

	    	Field classfield = (Field) CollectionUtils.find(
	    		Arrays.asList(menuItem.getClass().getDeclaredFields()), new BeanPropertyValueEqualsPredicate("name", "flowClass"));

	    	if (classfield == null)
	    		continue;
	    	
	    	classfield.setAccessible(true);
	    	Class clazz = (Class) classfield.get(menuItem);

	    	System.out.println(clazz.getName());
	    	
	    	containerWidget.addTab((String)entry.getKey(), menuItem.getLabel(), (Widget)clazz.newInstance());
	    	if (this.getClass().equals(clazz))
	    		containerWidget.disableTab((String)entry.getKey());
	    }
	}
}

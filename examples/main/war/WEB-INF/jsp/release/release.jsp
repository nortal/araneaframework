<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" 
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">
  <ui:widgetContext>

    <h1><fmt:message key="aranea.welcome"/></h1>

    <h2>
    	<fmt:message key="aranea.About.title"/>
    </h2>
    	<p>
			<fmt:message key="aranea.about"/>
    	</p>
    <h2>
		<fmt:message key="aranea.what.title"/>
    </h2>
    <p>
    	<fmt:message key="aranea.what.how"/>
    </p>

    <h2>
    	<fmt:message key="aranea.whatsnew"/>
    </h2>
    	<p><fmt:message key="aranea.wecomment"/></p>
    	<ul>
    	 <li><h3><fmt:message key="aranea.ComponentModel"/></h3>
    		<p>
				<fmt:message key="aranea.component.scope"/>
	    	</p>

	    	<p>
	    		<fmt:message key="aranea.component.lifecycle"/>
	    	</p>

	    	<p>
				<fmt:message key="aranea.component.realasync"/>
	    	</p>
    	 </li>
    	 
    	 <li><h3><fmt:message key="arenea.component.RenderingModel.title"/></h3>
    	 	<p>
				<fmt:message key="aranea.component.renderingmodeupdate"/>
    	 	</p>
    	 </li>
	    </ul>
    
    <h2>
       <fmt:message key="aranea.whattomigrate.title"/>
    </h2>
    <p>
		<fmt:message key="aranea.migration.help"/>
    </p>

  </ui:widgetContext>
</jsp:root>
<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" 
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">
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
    	<p>Here we shortly comment on high-level modifications/features that we believe make Aranea 1.1 &gt; 1.0</p>
    	<ul>
    	 <li><h3>Component Model</h3>
    		<p>
    			All components now have access to their
    			<a href="http://www.araneaframework.org/docs/1.1/javadoc/org/araneaframework/Scope.html">scope</a>
    			<ui:entity code="mdash"/>component's unique identifier in the component hierarchy. Previously it was 
    			hidden from developers, only accessible under certain conditions. Having access to component scopes 
    			simplifies both framework integration and component rendering.
	    	</p>

	    	<p>
	    		<a href="http://www.araneaframework.org/docs/1.1/javadoc/org/araneaframework/Widget.html">Widget</a>
	    		lifecycle has been simplified by removing the <i>process()</i> method. As an intermediate call between 
	    		component update and component rendering it was prone to misuse that made impossible to be sure
	    		that certain component methods had exactly same effect when called on a component in the middle of its lifecycle
	    		or on some inactive component. To disallow building components that break the POJO contract, it was 
	    		decided to remove this method completely.  
	    	</p>
	    	
	    	<p>
	    		Default component assembly has been seamlessly changed to allow for parallel processing of asynchronous requests. 
	    		In Aranea 1.0, incoming requests were always processed sequentially. 1.1 allows asynchronous processing only when developer 
	    		explicitly opts to use it<ui:entity code="mdash"/>and Widget's lifecycle methods are not invoked in that case, just 
<a href="http://www.araneaframework.org/docs/1.1/javadoc/org/araneaframework/core/BaseApplicationWidget.html#addActionListener(java.lang.Object,%20org.araneaframework.core.ActionListener)">
action listeners</a> registered by the Widget. Initial motivation for allowing optional asynchronous communication with widgets came from
police patrol monitoring application that needed very fast responses.
	    	</p>
    	 </li>
    	 
    	 <li><h3>Rendering Model</h3>
    	 	<p>
    	 		In Aranea 1.0, rendering phase of the lifecycle always rendered whole widget hierarchy. In Aranea 1.1 those requests
				that specify only certain regions of page to be in need of an update, are rendered in a more fine-grained way<ui:entity code="mdash"/>
				the rendering unit will be one render-capable widget that includes all regions that were specified for update. See 
    	 		<a href="http://www.araneaframework.org/docs/kvell-aranea-ajax.pdf">Alar Kvell's bachelor work</a> for description of
    	 		how partial rendering works.
    	 	</p>
    	 </li>
	    </ul>
    
    <h2>
       What needs to be done to migrate from Aranea 1.0 -&gt; 1.1? 
    </h2>
    <p>
    	Take a look at the migration guide. If it should leave you with some unanswered questions, turn to our
    	<a href="http://forum.araneaframework.org">forums</a> for support.
    </p>

  </ui:widgetContext>
</jsp:root>
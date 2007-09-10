<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		<ui:list id="simpleList">
		
			<!-- Label -->
			<tui:componentHeader>
				<tui:componentName>Simple List w/ paging&amp;sorting support</tui:componentName>
	        </tui:componentHeader>
	        
			<tui:component>
				
			<p>
	        	Creating read-only list from a bunch of model objects is really easy. Developer just declares the class
	        	of the model objects and the fields that should be shown and
	        	<a href="http://www.araneaframework.org/docs/1.1/javadoc/org/araneaframework/uilib/list/BeanListWidget.html">BeanListWidget</a>
	        	takes care of the rest<ui:entity code="mdash"/>paging and sorting are provided without any additional code.
	        </p>
	        
	        <p>
	        	You can try out the sorting by clicking on the list header names and paging by using the navigation bar
	        	at the list bottom.
	        </p>
	        <br/>

				<!-- Body -->
				<tui:componentList>

					<!-- Title -->
					<tui:componentListHeader/>

					<!-- Body -->
					<ui:listRows>
						<ui:row>
							<ui:cell>
								<c:out value="${row.booleanValue}" />
							</ui:cell>
							<ui:cell>
								<c:out value="${row.stringValue}" />
							</ui:cell>
							<ui:cell>
								<c:out value="${row.longValue}" />
							</ui:cell>
						</ui:row>
					</ui:listRows>

				</tui:componentList>

				<!-- Sequence -->
				<tui:componentListFooter/>

			</tui:component>
		</ui:list>
	</ui:widgetContext>

</jsp:root>

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
				<tui:componentName>Tutorial simple list</tui:componentName>
	        </tui:componentHeader>

			<tui:component>

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

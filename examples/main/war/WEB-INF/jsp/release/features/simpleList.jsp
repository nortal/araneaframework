<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">

	<ui:widgetContext>
		<ui:list id="simpleList">

			<!-- Label -->
			<tui:componentHeader>
				<tui:componentName>
					<fmt:message key="simple.list.sort.page"/>
				</tui:componentName>
	        </tui:componentHeader>
	        
			<tui:component>
				
			<p>
				<fmt:message key="simple.list.rolistintro"/>
	        </p>
	        
	        <p>
	        	<fmt:message key="simple.list.howtonavigate"/>
	        </p>
	        <br/>

				<!-- Body -->
				<tui:componentList>
					<ui:updateRegionRows id="simpleListBody">

					<!-- Title -->
					<tui:componentListHeader updateRegions="simpleListBody, ${listId}lfooter"/>

						<!-- Body -->
						<ui:listRows>
							<ui:row>
								<ui:cell width="33%">
									<c:out value="${row.booleanValue}" />
								</ui:cell>
								<ui:cell width="33%">
									<c:out value="${row.stringValue}" />
								</ui:cell>
								<ui:cell width="33%">
									<c:out value="${row.longValue}" />
								</ui:cell>
							</ui:row>
						</ui:listRows>

					</ui:updateRegionRows>

				</tui:componentList>

				<!-- Sequence -->
				<ui:updateRegion id="${listId}lfooter">
					<tui:componentListFooter updateRegions="simpleListBody, ${listId}lfooter"/>
				</ui:updateRegion>

			</tui:component>
		</ui:list>
	</ui:widgetContext>

</jsp:root>

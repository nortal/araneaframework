<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.0">

	<ui:widgetContext>
		<ui:list id="simpleList">

			<tui:componentHeader>
				<tui:componentName><fmt:message key="simpleList.title"/></tui:componentName>
			</tui:componentHeader>

			<tui:component>
				<p><fmt:message key="simpleList.intro"/></p>
				<p><fmt:message key="simpleList.howtonavigate"/></p>

				<!-- LIST begins here -->
				<tui:componentList>
					<!-- Let's wrap the list into an update region for smooth navigation: -->
					<ui:updateRegionRows id="simpleListBody">

						<!-- LIST columns headers -->
						<tui:componentListHeader updateRegions="simpleListBody, ${listId}lfooter" />

						<!-- LIST rows -->
						<ui:listRows>
							<ui:row>
								<ui:cell width="33%">${row.booleanValue}</ui:cell>
								<ui:cell width="33%">${row.stringValue}</ui:cell>
								<ui:cell width="33%">${row.longValue}</ui:cell>
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

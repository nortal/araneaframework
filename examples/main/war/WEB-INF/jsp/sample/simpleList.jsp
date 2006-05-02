<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		<ui:list id="simpleList">
		
			<!-- Label -->
			<ui:componentHeader>
				<ui:componentName>Tutorial simple list</ui:componentName>
	        </ui:componentHeader>

			<ui:component>

				<!-- Body -->
				<ui:componentList>

					<!-- Title -->
					<ui:componentListHeader/>

					<!-- Body -->
					<ui:listRows>
						<ui:newRow>
							<ui:newCell>
								<c:out value="${row.booleanValue}" />
							</ui:newCell>
							<ui:newCell>
								<c:out value="${row.stringValue}" />
							</ui:newCell>
							<ui:newCell>
								<c:out value="${row.longValue}" />
							</ui:newCell>
						</ui:newRow>
					</ui:listRows>

				</ui:componentList>

				<!-- Sequence -->
				<ui:componentListFooter/>

			</ui:component>
		</ui:list>
	</ui:widgetContext>

</jsp:root>

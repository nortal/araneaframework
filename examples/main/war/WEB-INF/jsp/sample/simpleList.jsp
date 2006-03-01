<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
		<ui:list id="simpleList">
			<ui:container>

				<!-- Label -->
				<ui:containerLabel>
	            	Tutorial simple list
	          	</ui:containerLabel>

				<!-- Body -->
				<ui:containerListBody>

					<!-- Title -->
					<ui:listTitleRow />

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

				</ui:containerListBody>

				<!-- Sequence -->
				<ui:listSequenceFooter/>

			</ui:container>
		</ui:list>
	</ui:widgetContext>

</jsp:root>

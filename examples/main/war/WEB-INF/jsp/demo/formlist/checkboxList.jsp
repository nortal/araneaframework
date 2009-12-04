<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt" version="2.1">
	<ui:list id="checkList">
		<ui:formList>
			<tui:componentHeader>
				<tui:componentName>Checkbox demo</tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<!-- Body -->
				<tui:componentList>
					<tui:componentListHeader />

					<!-- Body -->
					<ui:formListRows>
						<ui:row>
							<ui:cell styleClass="center">
								<ui:checkbox id="booleanField" />
							</ui:cell>
							<ui:cell>
								<c:out value="${row.stringField}" />
							</ui:cell>
							<ui:cell>
								<c:out value="${row.longField}" />
							</ui:cell>
						</ui:row>
					</ui:formListRows>

				</tui:componentList>

				<!-- Sequence -->
				<tui:componentListFooter />

				<!-- Footer -->
				<tui:componentActions>
					<ui:eventButton labelId="#Save" eventId="save" />
				</tui:componentActions>

			</tui:component>
		</ui:formList>
	</ui:list>
</jsp:root>

<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:list id="checkList">
		<ui:formList>
			<ui:componentHeader>
				<ui:componentName>Checkbox demo</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<!-- Body -->
				<ui:componentList>
					<ui:componentListHeader />

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

				</ui:componentList>

				<!-- Sequence -->
				<ui:componentListFooter />

				<!-- Footer -->
				<ui:componentActions>
					<ui:eventButton labelId="#Save" eventId="save" />
				</ui:componentActions>

			</ui:component>
		</ui:formList>
	</ui:list>
</jsp:root>

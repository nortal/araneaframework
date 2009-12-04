<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">		
	<ui:widgetContext>
		<ui:list id="contractList">

			<tui:componentHeader>
				<tui:componentName><fmt:message key="contracts.title"/></tui:componentName>
			</tui:componentHeader>

			<tui:component>

				<p><fmt:message key="contracts.desc"/></p>

				<!-- Body -->
				<tui:componentList>

					<!-- Title -->
					<tui:componentListHeader/>

					<ui:listRows>
						<ui:row>
							<ui:cell>
								<c:out value="${row.id}"/>
							</ui:cell>
	
							<ui:cell>
								<ui:listRowLinkButton eventId="edit">
									<c:out value="${row.company.name}"/>
								</ui:listRowLinkButton>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.person.name} ${row.person.surname}"/>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.notes}"/>
							</ui:cell>

							<ui:cell>
								<ui:listRowLinkButton eventId="remove">
									<tui:image code="buttonDelete" title="contracts.remove"/>
								</ui:listRowLinkButton>
							</ui:cell>
						</ui:row>
					</ui:listRows>
				</tui:componentList>
			
				<!-- Sequence -->
				<tui:componentListFooter/>

				<tui:componentActions>
					<ui:eventButton eventId="add" labelId="contracts.add"/>
				</tui:componentActions>

			</tui:component>
		
		</ui:list>
	</ui:widgetContext>		
</jsp:root>
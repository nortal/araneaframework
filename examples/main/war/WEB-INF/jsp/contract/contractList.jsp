<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template"  version="1.2">		
	<ui:widgetContext>
		<ui:list id="contractList">
		
			<ui:componentHeader>
				<ui:componentName>Contract list</ui:componentName>
			</ui:componentHeader>
			
			<ui:component>
				<!-- Body -->
				<ui:componentList>
					<!-- Title -->				
					<ui:componentListHeader/>

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
									<ui:image code="buttonDelete" alt="Remove contract" title="Remove contract"/>
								</ui:listRowLinkButton>
							</ui:cell>
						</ui:row>
					</ui:listRows>				
				</ui:componentList>
			
				<!-- Sequence -->
				<ui:listSequenceFooter/>

				<ui:componentActions>
					<ui:eventButton eventId="add" labelId="#Add new contract" />
				</ui:componentActions>

			</ui:component>
		
		</ui:list>

		<ui:element name="p">
			<ui:elementContent>
				This is a list of contracts. You can use links below the list to navigate through pages.
				To choose a contract just click on it's Company.
			</ui:elementContent>
		</ui:element>

	</ui:widgetContext>		
</jsp:root>
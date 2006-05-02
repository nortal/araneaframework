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
						<ui:newRow>
							<ui:newCell>
								<c:out value="${row.id}"/>
							</ui:newCell>
	
							<ui:newCell>
								<ui:listRowLinkButton eventId="edit">
									<c:out value="${row.company.name}"/>
								</ui:listRowLinkButton>
							</ui:newCell>
		
							<ui:newCell>
								<c:out value="${row.person.name} ${row.person.surname}"/>
							</ui:newCell>
		
							<ui:newCell>
								<c:out value="${row.notes}"/>
							</ui:newCell>

							<ui:newCell>
								<ui:listRowLinkButton eventId="remove">
									<ui:image code="buttonDelete" alt="Remove contract" title="Remove contract"/>
								</ui:listRowLinkButton>
							</ui:newCell>
						</ui:newRow>
					</ui:listRows>				
				</ui:componentList>
			
				<!-- Sequence -->
				<ui:componentListFooter/>

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
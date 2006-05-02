<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2"
>
	<ui:widgetContext>
		<ui:list id="personList">
			<ui:componentHeader>
				<ui:componentName>Persons List</ui:componentName>
			</ui:componentHeader>
				
			<ui:component>
						
				<ui:componentList>
					<!-- Title -->
					<ui:componentListHeader/>

					<!-- Filter -->
					<ui:listFilter>
						<ui:newRow styleClass="filter">
							<ui:newCell/>
	
							<ui:newCell>
								<ui:textInput id="name"/>
							</ui:newCell>
	
							<ui:newCell>
								<ui:textInput id="surname"/>
							</ui:newCell>
	
							<ui:newCell>
								<ui:textInput id="phone"/>
							</ui:newCell>
							
							<ui:newCell>
								<ui:dateInput id="birthdate_start"/>
								<br/>
								<ui:dateInput id="birthdate_end"/>
							</ui:newCell>
	
							<ui:newCell>
								<ui:filterButton/>
							</ui:newCell>
						</ui:newRow>
					</ui:listFilter>					
					
					<ui:listRows>
						<ui:newRow>
							<ui:newCell>
								<c:out value="${row.id}"/>
							</ui:newCell>
	
							<ui:newCell>
								<ui:listRowLinkButton eventId="select">
									<c:out value="${row.name}"/>
								</ui:listRowLinkButton>
							</ui:newCell>
		
							<ui:newCell>
								<c:out value="${row.surname}"/>
							</ui:newCell>
		
							<ui:newCell>
								<c:out value="${row.phone}"/>
							</ui:newCell>
							
							<ui:newCell>
								<fmt:formatDate value="${row.birthdate}" pattern="dd.MM.yyyy"/>
							</ui:newCell>

							<ui:newCell>
								<ui:listRowLinkButton eventId="edit">
									<ui:image code="buttonChange" alt="Edit person" title="Edit person"/>
								</ui:listRowLinkButton>
								<ui:listRowLinkButton eventId="remove">
									<ui:image code="buttonDelete" alt="Remove person" title="Remove person"/>
								</ui:listRowLinkButton>
							</ui:newCell>
							
						</ui:newRow>
					</ui:listRows>
				</ui:componentList>

				<ui:componentListFooter/>
				
				<ui:componentActions>
					<ui:eventButton eventId="add" labelId="#Add new person"/>
				</ui:componentActions>

			</ui:component>
		
		</ui:list>
	</ui:widgetContext>
</jsp:root>
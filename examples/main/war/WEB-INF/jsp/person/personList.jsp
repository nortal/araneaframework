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
						<ui:row styleClass="filter">
							<ui:cell/>
	
							<ui:cell>
								<ui:textInput id="name"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="surname"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="phone"/>
							</ui:cell>
							
							<ui:cell>
								<ui:dateInput id="birthdate_start"/>
								<br/>
								<ui:dateInput id="birthdate_end"/>
							</ui:cell>
	
							<ui:cell>
								<ui:filterButton/>
							</ui:cell>
						</ui:row>
					</ui:listFilter>					
					
					<ui:listRows>
						<ui:row>
							<ui:cell>
								<c:out value="${row.id}"/>
							</ui:cell>
	
							<ui:cell>
								<ui:listRowLinkButton eventId="select">
									<c:out value="${row.name}"/>
								</ui:listRowLinkButton>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.surname}"/>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.phone}"/>
							</ui:cell>
							
							<ui:cell>
								<fmt:formatDate value="${row.birthdate}" pattern="dd.MM.yyyy"/>
							</ui:cell>

							<c:if test="${contextWidget.data.allowRemove}">
								<ui:cell>
									<ui:listRowLinkButton id="delete">
										<ui:image code="buttonDelete" alt="Remove person" title="Remove person"/>
									</ui:listRowLinkButton>
								</ui:cell>
							</c:if>
							
						</ui:row>
					</ui:listRows>
				</ui:componentList>

				<ui:componentListFooter/>
				
				<ui:componentActions>
					<c:if test="${contextWidget.data.allowAdd}">
						<ui:eventButton eventId="add" labelId="#Add new person"/>
					</c:if>
				</ui:componentActions>

			</ui:component>
		
		</ui:list>
		
		<p>
			This is a list of persons. You can use a filter to search for a specific person or use
			links below the list to navigate through pages. To choose a person just click on it's First Name.
		</p>

	</ui:widgetContext>
</jsp:root>
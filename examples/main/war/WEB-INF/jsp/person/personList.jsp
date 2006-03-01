<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2"
>
	<ui:widgetContext>
	
		<h2>Persons</h2>
		
		<p>
			This is a list of persons. You can use a filter to search for a specific person or use
			links below the list to navigate through pages. To choose a person just click on it's First Name.
		</p>
		<c:if test="${contextWidget.data.allowAdd}">
			<p>You can also add a new person (use a button below the list).</p>
		</c:if>
		<c:if test="${contextWidget.data.allowRemove}">
			<p>You can also remove a person (use a link on it's row).</p>
		</c:if>
		
		<ui:list id="personList">
		
			<ui:container>
						
				<!-- Body -->
				<ui:containerListBody>
					<!-- Title -->				
					<ui:listTitleRow/>
					
					<!-- Filter -->
					<ui:listFilter>
						<ui:row>
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
								<c:out value="${row.birthdate}"/>
							</ui:cell>

							<c:if test="${contextWidget.data.allowRemove}">
								<ui:cell>
									<ui:listRowLinkButton eventId="remove" labelId="#Remove"/>
								</ui:cell>
							</c:if>
							
						</ui:row>
					</ui:listRows>
				</ui:containerListBody>

				<!-- Sequence -->
				<ui:listSequenceFooter/>
				
				<ui:containerFooter>
					<c:if test="${contextWidget.data.allowAdd}">
						<ui:eventButton eventId="add" labelId="#Add new person"/>
					</c:if>
				</ui:containerFooter>

			</ui:container>
		
		</ui:list>

	</ui:widgetContext>		
</jsp:root>
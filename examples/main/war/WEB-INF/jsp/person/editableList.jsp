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
		
		<ui:list id="list">
		<ui:formList>
		
			<ui:container>
			
				<!-- Header -->
				<ui:containerHeader>
				</ui:containerHeader>
						
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
								<ui:dateInput id="birthdate"/>
							</ui:cell>							
	
							<ui:cell>
								<ui:filterButton/>
							</ui:cell>
						</ui:row>
					</ui:listFilter>					
					
					<ui:formListRows>
					<!-- <ui:listRows> -->
						<ui:row>
							<ui:cell>
								<c:out value="${row.id}"/>
							</ui:cell>
	
							<c:choose>
								<c:when test="${formRow.open}">
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
										<ui:dateInput id="birthdate"/>
									</ui:cell>																																			
								</c:when>
								<c:otherwise>
									<ui:cell>
										<c:out value="${row.name}"/>
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
								</c:otherwise>								
							</c:choose>

							<ui:cell width="0">
								<ui:linkButton id="editSave" showLabel="false"><ui:image code="buttonChange"/></ui:linkButton>
								<ui:linkButton id="delete" showLabel="false"><ui:image code="buttonDelete"/></ui:linkButton>
							</ui:cell>							
						</ui:row>
					<!-- </ui:listRows> -->
					</ui:formListRows>
				
					<ui:formListAddForm>
						<ui:row>
							<ui:cell />
						
							<ui:cell styleClass="center">
								<ui:textInput id="name"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="surname"/>
							</ui:cell>
							
							<ui:cell>
								<ui:textInput id="phone"/>
							</ui:cell>
							
							<ui:cell>
								<ui:dateInput id="birthdate"/>
							</ui:cell>							
	
							<ui:cell width="0">
								<ui:linkButton id="add" showLabel="false"><ui:image code="buttonAdd"/></ui:linkButton>
							</ui:cell>
						</ui:row>		
					</ui:formListAddForm>					
				</ui:containerListBody>
			
				<!-- Sequence -->
				<ui:listSequenceFooter/>			

			</ui:container>
		
		</ui:formList>
		</ui:list>

	</ui:widgetContext>		
</jsp:root>
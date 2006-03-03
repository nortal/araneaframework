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
		
			<ui:componentHeader>
				<ui:componentName>Editable person list</ui:componentName>
			</ui:componentHeader>
		
			<ui:component>
			
				<ui:componentList>
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
								<ui:linkButton id="editSave" showLabel="false"><ui:image code="buttonChange" alt="Edit person" title="Edit person"/></ui:linkButton>
								<ui:linkButton id="delete" showLabel="false"><ui:image code="buttonDelete" alt="Remove person" title="Remove person"/></ui:linkButton>
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

				</ui:componentList>
			
				<!-- Sequence -->
				<ui:listSequenceFooter/>

			</ui:component>
		
		</ui:formList>
		</ui:list>

	</ui:widgetContext>		
</jsp:root>
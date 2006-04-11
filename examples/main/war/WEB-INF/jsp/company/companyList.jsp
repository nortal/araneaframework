<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/template" 
	xmlns:cui="http://araneaframework.org/tag-library/contrib"
	version="1.2">		
	<ui:widgetContext>
		<ui:list id="companyList">
		
			<ui:componentHeader>
				<ui:componentName>Companies</ui:componentName>
			</ui:componentHeader>
			<ui:component>
			
				<!-- Body -->
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
								<ui:textInput id="address"/>
							</ui:cell>
	
							<ui:cell>
								<ui:filterButton/>
							</ui:cell>
						</ui:row>
					</ui:listFilter>
										
					<!-- List body, where all list objects that fit on a current page will be rendered. -->
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
								<c:out value="${row.address}"/>
							</ui:cell>
							
							<ui:cell>
								<ui:listRowLinkButton eventId="edit">
									<ui:image code="buttonChange" alt="Edit company" title="Edit company"/>
								</ui:listRowLinkButton>
								<ui:listRowLinkButton eventId="remove">
									<ui:image code="buttonDelete" alt="Remove company" title="Remove company"/>
								</ui:listRowLinkButton>
							</ui:cell>
							
						</ui:row>
					</ui:listRows>				
				</ui:componentList>
			
				<!-- Sequence -->
				<ui:componentListFooter/>

				<ui:componentActions>
					<ui:eventButton eventId="add" labelId="#Add new company"/>
				</ui:componentActions>
			</ui:component>
		
		</ui:list>

		

	</ui:widgetContext>
</jsp:root>
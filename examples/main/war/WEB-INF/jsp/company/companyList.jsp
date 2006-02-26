<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/template" 
	xmlns:cui="http://araneaframework.org/tag-library/contrib"
	version="1.2">		
	<ui:widgetContext>
	
		<h2>Companies</h2>
		
		<p>
			This is a list of companies. You can use a filter to search for a specific company or use
			links below the list to navigate through pages. To choose a company just click on it's Name.
		</p>
		<c:if test="${contextWidget.data.allowAdd}">
			<p>You can also add a new company (use a button below the list).</p>
		</c:if>
		<c:if test="${contextWidget.data.allowRemove}">
			<p>You can also remove a company (use a link on it's row).</p>
		</c:if>		
		
		<ui:list id="companyList">
		
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
								<ui:textInput id="address"/>
							</ui:cell>
	
							<ui:cell>
								<ui:filterButton/>
							</ui:cell>
						</ui:row>
					</ui:listFilter>
										
					<!-- Body -->
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

			</ui:container>
		
		</ui:list>

		<c:if test="${contextWidget.data.allowAdd}">
			<ui:eventButton eventId="add" labelId="#Add new person"/>
		</c:if>

	</ui:widgetContext>
</jsp:root>
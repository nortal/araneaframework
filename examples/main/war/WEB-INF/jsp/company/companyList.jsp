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
									<ui:listRowLinkButton eventId="remove">
										<ui:image code="buttonDelete" alt="Remove company" title="Remove company"/>
									</ui:listRowLinkButton>
								</ui:cell>
							</c:if>
							
						</ui:row>
					</ui:listRows>				
				</ui:componentList>
			
				<!-- Sequence -->
				<ui:componentListFooter/>

			</ui:component>
		
		</ui:list>

		<c:if test="${contextWidget.data.allowAdd}">
			<ui:eventButton eventId="add" labelId="#Add new person"/>
		</c:if>

	</ui:widgetContext>
</jsp:root>
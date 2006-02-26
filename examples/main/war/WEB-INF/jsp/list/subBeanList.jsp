<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2"
>
	<ui:widgetContext>
	
		<h2>Contacts (SubBeanList)</h2>
		
		<ui:list id="list">
		
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
								<ui:textInput id="name.firstname"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="name.lastname"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="address.country"/>
							</ui:cell>

							<ui:cell>
								<ui:textInput id="address.city"/>
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
									<c:out value="${row.name.firstname}"/>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.name.lastname}"/>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.address.country}"/>
							</ui:cell>

							<ui:cell>
								<c:out value="${row.address.city}"/>
							</ui:cell>

							<ui:cell width="0" />
							
						</ui:row>
					</ui:listRows>				
				</ui:containerListBody>
			
				<!-- Sequence -->
				<ui:listSequenceFooter/>

			</ui:container>
		
		</ui:list>

	</ui:widgetContext>		
</jsp:root>
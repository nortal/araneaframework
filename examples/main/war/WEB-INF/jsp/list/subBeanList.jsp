<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2"
>
	<ui:widgetContext>
	
		<ui:list id="list">
			<ui:componentHeader>
				<ui:componentName>Contacts (SubBeanList)</ui:componentName>
			</ui:componentHeader>
					
			<ui:component>
						
				<!-- Body -->
				<ui:componentList>
					<ui:componentListHeader/>
					
					<!-- Filter -->
					<ui:listFilter>
						<ui:newRow>
							<ui:newCell/>
							
							<ui:newCell>
								<ui:textInput id="name.firstname"/>
							</ui:newCell>
	
							<ui:newCell>
								<ui:textInput id="name.lastname"/>
							</ui:newCell>
	
							<ui:newCell>
								<ui:textInput id="address.country"/>
							</ui:newCell>

							<ui:newCell>
								<ui:textInput id="address.city"/>
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
									<c:out value="${row.name.firstname}"/>
							</ui:newCell>
		
							<ui:newCell>
								<c:out value="${row.name.lastname}"/>
							</ui:newCell>
		
							<ui:newCell>
								<c:out value="${row.address.country}"/>
							</ui:newCell>

							<ui:newCell>
								<c:out value="${row.address.city}"/>
							</ui:newCell>

							<ui:newCell width="0" />
							
						</ui:newRow>
					</ui:listRows>				
				</ui:componentList>
			
				<!-- Sequence -->
				<ui:componentListFooter/>

			</ui:component>
		
		</ui:list>

	</ui:widgetContext>		
</jsp:root>
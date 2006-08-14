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
				</ui:componentList>
			
				<!-- Sequence -->
				<ui:componentListFooter/>

			</ui:component>
		
		</ui:list>

	</ui:widgetContext>		
</jsp:root>
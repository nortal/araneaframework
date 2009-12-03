<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1"
>
	<ui:widgetContext>
	
		<ui:list id="list">
			<tui:componentHeader>
				<tui:componentName>Contacts (SubBeanList)</tui:componentName>
			</tui:componentHeader>
					
			<tui:component>
						
				<!-- Body -->
				<tui:componentList>
					<tui:componentListHeader/>
					
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
								<ui:listFilterButton/>
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
				</tui:componentList>
			
				<!-- Sequence -->
				<tui:componentListFooter/>

			</tui:component>
		
		</ui:list>

	</ui:widgetContext>		
</jsp:root>
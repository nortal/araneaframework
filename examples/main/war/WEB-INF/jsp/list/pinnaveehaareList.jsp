<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template"  version="1.2">		
	<ui:widgetContext>
	
		<h2>Pinnaveehaare (Oracle backend)</h2>
			
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
								<ui:textInput id="nimetus"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="pindala"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="veekogu"/>
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
									<c:out value="${row.nimetus}"/>
								</ui:listRowLinkButton>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.pindala}"/>
							</ui:cell>
		
							<ui:cell/>							
							<ui:cell/>							
							
						</ui:row>
					</ui:listRows>
				</ui:containerListBody>
			
				<!-- Sequence -->
				<ui:listSequenceFooter/>

			</ui:container>
		
		</ui:list>

		<ui:eventButton eventId="cancel" labelId="#Back"/>

	</ui:widgetContext>		
</jsp:root>
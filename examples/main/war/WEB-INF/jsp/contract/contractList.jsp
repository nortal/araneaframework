<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template"  version="1.2">		
	<ui:widgetContext>
		
		<h2>Contracts</h2>
		
		<p>
			This is a list of contracts. You can use links below the list to navigate through pages.
			To choose a contract just click on it's Company.
		</p>
		<c:if test="${contextWidget.data.allowAdd}">
			<p>You can also add a new contract (use a button below the list).</p>
		</c:if>
		<c:if test="${contextWidget.data.allowRemove}">
			<p>You can also remove a contract (use a link on it's row).</p>
		</c:if>		
		
		<ui:list id="contractList">
		
			<ui:container>
			
				<!-- Header -->
				<ui:containerHeader>
				</ui:containerHeader>
						
				<!-- Body -->
				<ui:containerListBody>
					<!-- Title -->				
					<ui:listTitleRow/>				
					
					<ui:listRows>
						<ui:row>
							<ui:cell>
								<c:out value="${row.id}"/>
							</ui:cell>
	
							<ui:cell>
								<ui:listRowLinkButton eventId="select">
									<c:out value="${row.company.name}"/>
								</ui:listRowLinkButton>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.person.name} ${row.person.surname}"/>
							</ui:cell>
		
							<ui:cell>
								<c:out value="${row.notes}"/>
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

		<ui:eventButton eventId="cancel" labelId="#Back"/>
		<c:if test="${contextWidget.data.allowAdd}">
			<ui:eventButton eventId="add" labelId="#Add new contract"/>
		</c:if>

	</ui:widgetContext>		
</jsp:root>
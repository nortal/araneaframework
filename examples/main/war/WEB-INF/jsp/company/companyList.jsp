<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" 
	version="2.0">		
	<ui:widgetContext>
		<ui:list id="companyList">
		
			<tui:componentHeader>
				<tui:componentName>Companies</tui:componentName>
			</tui:componentHeader>
			<tui:component>
			
				<!-- Body -->
				<tui:componentList>
					<!-- Title -->
					<tui:componentListHeader/>
					
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
								<ui:listFilterButton/>
							</ui:cell>
						</ui:row>
					</ui:listFilter>
										
					<!-- List body, where all list objects that fit on a current page will be rendered. -->
					<ui:listRows>
    					<!-- Creating a row inside the listRows signals that list object will be rendered inside it. 
                             ui:listRows is iterating tag, it will render as many rows as there are object to show.
                             Current list object being operated on is accessible as EL variable ${row}. List objects
                             field values that have getters are accessible as ${row.field}.
                        -->
						<ui:row>
						
							<ui:cell>
								<ui:listRowRadioButton/>
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
									<tui:image code="buttonChange" alt="Edit company" title="Edit company"/>
								</ui:listRowLinkButton>
								<ui:listRowLinkButton eventId="remove" onClickPrecondition="return window.confirm('Are you sure?');">
									<tui:image code="buttonDelete" alt="Remove company" title="Remove company"/>
								</ui:listRowLinkButton>
							</ui:cell>
							
						</ui:row>
					</ui:listRows>				
				</tui:componentList>
			
				<!-- Sequence -->
				<tui:componentListFooter/>

				<tui:componentActions>
					<ui:eventButton eventId="add" labelId="#Add new company"/>
					<ui:nbsp/>
					<ui:eventButton eventId="collect" labelId="#Pick the company"/>
				</tui:componentActions>
			</tui:component>
		
		</ui:list>

	</ui:widgetContext>
</jsp:root>
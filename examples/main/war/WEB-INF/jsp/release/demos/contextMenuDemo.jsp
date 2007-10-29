<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt" version="1.2">
	<ui:widgetContext>
		<tui:componentHeader>
			<tui:componentName><fmt:message key="sed.title"/></tui:componentName>
		</tui:componentHeader>
		
		<ui:list id="list">
			<script type="text/javascript">
				var locateRow = function(el) {
                    return $(el).up('tr');
                  };
                  
                var lfId = '<c:out value="${listFullId}"/>';
			
				var cMenuparameterSupplier = function() {
					if (araneaContextMenu.getTriggeringElement()) {
					  var tablerow = locateRow(araneaContextMenu.getTriggeringElement())
					  var trid = tablerow.id;
					  var rrr = trid.substring(lfId.length+4);
					  return rrr;
					} else {
					  
					}

					//return null;
				};
			</script>
		
			<tui:component>
			
			<p>
				<fmt:message key="context.menu.intro"/>
			</p>
			
			<p>
				<fmt:message key="context.menu.howto"/>
			</p>
			
			<br/>
			
			<tui:componentList>
					<ui:updateRegionRows id="listBodyUpdateRegion">

					<tui:componentListHeader updateRegions="listBodyUpdateRegion,${listId}lfooter"/>
					
					<ui:listFilter>
						<ui:row styleClass="filter">
							<ui:cell>
								<ui:textInput id="sex" size="1"/>
							</ui:cell>
	
							<ui:cell width="80px">
								<ui:textInput id="forename"/>
							</ui:cell>
	
							<ui:cell>
								<ui:textInput id="surname" size="20"/>
							</ui:cell>
							
							<ui:cell>
								<ui:textInput id="country"/>
							</ui:cell>
							
							<ui:cell>
								<ui:listFilterButton updateRegions="listBodyUpdateRegion,${listId}lfooter"/>
								<br/>
								<ui:listFilterClearButton updateRegions="listBodyUpdateRegion,${listId}lfooter"/>
							</ui:cell>
						</ui:row>
						
					</ui:listFilter>
				
					
						<ui:listRows>
							<ui:widgetMarker id="list" tag="tbody">
							<ui:row id="${listFullId}.row${rowRequestId}">
	
								<ui:cell>
									<c:out value="${row.sex}" />
								</ui:cell>
								<ui:cell styleClass="right" width="80px">
									<c:out value="${row.forename}" />
								</ui:cell>
								<ui:cell>
									<c:out value="${row.surname}" />
								</ui:cell>
								<ui:cell>
									<c:out value="${row.country}" />
								</ui:cell>
								
								<ui:cell width="0"></ui:cell>
								
							</ui:row>
							</ui:widgetMarker>
						</ui:listRows>
						<ui:contextMenu id="list.cmenu"/>
					
					</ui:updateRegionRows>
			</tui:componentList>
			
			
			<ui:updateRegion id="${listId}lfooter">
				<tui:componentListFooter updateRegions="listBodyUpdateRegion,${listId}lfooter"/>
			</ui:updateRegion>
		</tui:component>
		
		</ui:list>
	</ui:widgetContext>
</jsp:root>

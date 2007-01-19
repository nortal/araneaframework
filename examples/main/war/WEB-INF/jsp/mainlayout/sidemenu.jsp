<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="1.2">
	<!--  variables -->
	<c:set var="activeStyle" value="active"/>

	<ui:widgetContext id="menu">
		<div id="leftcol">
			<ul id="menu2">
				<c:forEach items="${viewData.menu.subMenu}" var="topMenuItem">
					<c:if test="${topMenuItem.value.selected}">
						<c:forEach items="${topMenuItem.value.subMenu}" var="item">
							<li>
								<c:if test="${item.value.selected}">
									<ui:eventLinkButton 
										eventId="menuSelect" 
										eventParam="${topMenuItem.value.label}.${item.value.label}" 
										labelId="${item.value.label}"
										styleClass="${activeStyle}"/>
	
									<c:if test="${item.value.holder}">
									<ul>
										<c:forEach items="${item.value.subMenu}" var="subitem">
											<li>
												<c:if test="${subitem.value.selected}">
													<ui:eventLinkButton 
														eventId="menuSelect" 
														eventParam="${topMenuItem.value.label}.${item.value.label}.${subitem.value.label}" 
														labelId="${subitem.value.label}"
														styleClass="${activeStyle}"/>														
												</c:if>
	
												<c:if test="${not subitem.value.selected}">
													<ui:eventLinkButton 
														eventId="menuSelect" 
														eventParam="${topMenuItem.value.label}.${item.value.label}.${subitem.value.label}" 
														labelId="${subitem.value.label}"/>																
												</c:if>
											</li>
										</c:forEach>
									</ul>
									</c:if>
	
								</c:if>
	
								<c:if test="${not item.value.selected}">
									<ui:eventLinkButton 
										eventId="menuSelect" 
										eventParam="${topMenuItem.value.label}.${item.value.label}" 
										labelId="${item.value.label}"/>														
								</c:if>
							</li>
						</c:forEach>
					</c:if>
				</c:forEach>
			</ul>
		</div>

	</ui:widgetContext>
</jsp:root>
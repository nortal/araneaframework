<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">
	<!-- This is a master page of Aranea framework template application (examples/main/war/WEB-INF/jsp/root.jsp) -->
	<ui:widgetContext>
		<![CDATA[
	   		<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">	        		
  		]]>
		<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

			<!-- Scripts -->
			<jsp:include page="scripts.jsp"/>
			<title>Aranea<ui:entity code="mdash"/>Java Web Framework Construction and Integration Kit</title>
		</head>

		<ui:body>
            
            <ui:updateRegion globalId="globalBackRegion">
				<div id="cont1">
					<ui:systemForm method="POST">
						<ui:registerScrollHandler/>
						<ui:registerPopups/>
						<ui:registerOverlay/>
	
						<!-- Renders the menu on top of the screen -->
						<jsp:include page="/WEB-INF/jsp/mainlayout/menu.jsp"/>
						<div class="stripe1"><ui:nbsp/></div>
		
						<div id="wholder">
							<!-- Renders the side menu on left side of screen -->
							<jsp:include page="/WEB-INF/jsp/mainlayout/sidemenu.jsp"/>
		
							<div id="content">
								<div class="msg-info">
									<div>
										<div>
											<ui:messages type="info"/>
										</div>
									</div>
								</div>
								<ui:messages type="error" styleClass="msg-error"/>
	
								<!-- Renders the menu widget itself. As MenuWidget is subclass
								     of StandardFlowContainerWidget, this means that actual
								     widget rendered here is whatever widget is on top of call
								     stack at the moment of rendering. -->
								<ui:widgetInclude id="menu"/>
							</div>
		
							<div class="clear1"><ui:nbsp/></div>
						</div>
					</ui:systemForm>
				</div>
	
				<ui:widgetInclude id="menu.footer"/>
			</ui:updateRegion>

			<ui:onLoadEvent event="if (dhtmlHistory) { window.dhtmlHistory.firstLoad = true; window.location.hash = _ap.getSystemForm().araClientStateId.value;   dhtmlHistory.add(_ap.getSystemForm().araClientStateId.value, null); }"/>

		</ui:body>
		
		</html>
	</ui:widgetContext>
</jsp:root>

<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    version="1.2">

	        <ui:widgetContext>	        
	        		<![CDATA[
		        		<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">	        		
	        		]]>
	            <html>
	                <head>
	                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	
	                    <!-- Scripts -->
	                    <ui:importScripts/>
	                    <title>Aranea Blank Application</title>
	                </head>
	
	                <ui:body>

	                        <ui:systemForm method="POST">
	                        
	                        <ui:registerPopups/>
	                        
                             <!-- Renders the menu on top of the screen -->
	                         <jsp:include page="/WEB-INF/jsp/menu.jsp"/>
	                            <ui:nbsp/>
	
	                             	<ui:updateRegion globalId="messages">
		                               <ui:messages type="info"/>
	                                   <ui:messages type="error" styleClass="msg-error"/>
                                    </ui:updateRegion>
	                                 <!-- Renders the menu widget itself. As MenuWidget is subclass
	                                 	of StandardFlowContainerWidget, this means that actual
	                                 	widget rendered here is whatever widget is on top of call
	                                 	stack at the moment of rendering. -->
	                                 <ui:widgetInclude id="menu"/>
	   
	 
					
	                        </ui:systemForm>
	  

	                </ui:body>
	            </html>
	        </ui:widgetContext>

</jsp:root>
